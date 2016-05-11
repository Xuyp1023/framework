package com.betterjr.common.security;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.*;

import org.apache.commons.codec.binary.Base64;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.utils.BetterStringUtils;

/**
 * 数字证书读取类
 * 
 * @author zhoucy
 *
 */
public class KeyReader {

    public KeyReader() {
    }

    public static boolean isPublicKeyValid(String key, String algorithmName) {
        try {
            readPublicKey(key, true, algorithmName);
        }
        catch (BytterSecurityException e) {
            return false;
        }
        return true;
    }

    public static boolean isPrivateKeyValid(String key, String algorithmName) {
        try {
            readPrivateKey(key, true, algorithmName);
        }
        catch (BytterSecurityException e) {
            return false;
        }
        return true;
    }

    public static PrivateKey readPrivateKey(String keyStr) {

        return readPrivateKey(keyStr, true, "RSA");
    }

    public static PrivateKey readPrivateKey(String keyStr, boolean base64Encoded, String algorithmName) {
        return (PrivateKey) readKey(keyStr, false, base64Encoded, algorithmName);
    }

    public static PublicKey readPublicKey(String keyStr) {

        return (PublicKey) readKey(keyStr, true, true, "RSA");
    }

    public static PublicKey readPublicKey(String keyStr, boolean base64Encoded, String algorithmName) {
        return (PublicKey) readKey(keyStr, true, base64Encoded, algorithmName);
    }

    private static Key readKey(String keyStr, boolean isPublicKey, boolean base64Encoded, String algorithmName) {
        KeyFactory keyFactory;
        byte encodedKey[];
        try {
            if (BetterStringUtils.isNotBlank(keyStr) || keyStr.length() < 100) {
                keyStr = readByteFromResource(keyStr);
            }

            keyFactory = KeyFactory.getInstance(algorithmName);
            encodedKey = keyStr.getBytes("UTF-8");
            if (base64Encoded) {
                encodedKey = Base64.decodeBase64(encodedKey);
            }

            if (isPublicKey) {
                java.security.spec.EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
                return keyFactory.generatePublic(keySpec);
            }

            java.security.spec.EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
            return keyFactory.generatePrivate(keySpec);
        }
        catch (NoSuchAlgorithmException e) {
            throw new BytterSecurityException(50000, "NoSuchAlgorithmException KeyReader.ReadKey:" + keyStr, e);
        }
        catch (UnsupportedEncodingException e) {
            throw new BytterSecurityException(50000, "UnsupportedEncodingException KeyReader.ReadKey:" + keyStr, e);
        }
        catch (InvalidKeySpecException e) {
            throw new BytterSecurityException(50000, "InvalidKeySpecException KeyReader.ReadKey:" + keyStr, e);
        }
    }

    public static PrivateKey readPrivateKeyfromPKCS12StoredFile(String resourceName, String password) {
        Map<PrivateKey, Certificate> map = readKeyfromPKCS12StoredFile(resourceName, password);
        for (Map.Entry<PrivateKey, Certificate> ent : map.entrySet()) {
            return ent.getKey();
        }
        return null;
    }

    public static Map<PrivateKey, Certificate> readKeyfromPKCS12StoredFile(String resourceName, String password) {
        return readKeyfromPKCS12StoredFile(resourceName, password, true);
    }

    public static Map<PrivateKey, Certificate> readKeyfromPKCS12StoredClassPath(String resourceName, String password) {
        return readKeyfromPKCS12StoredFile(resourceName, password, false);
    }

    public static KeyManager getKeyManager(String anKeyPath, String anPass) throws Exception {
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        ClassPathResource cc = new ClassPathResource(anKeyPath);
        keystore.load(cc.getInputStream(), anPass.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keystore, anPass.toCharArray());
        KeyManager[] km = kmf.getKeyManagers();
        System.out.println("km len: " + km.length);

        return km[0];
    }

    private static Map<PrivateKey, Certificate> readKeyfromPKCS12StoredFile(String resourceName, String password, boolean anFileType) {
        InputStream istream = null;
        try {
            if (anFileType) {
                istream = new FileInputStream(resourceName);
            }
            else {
                ClassPathResource cc = new ClassPathResource(resourceName);
                istream = cc.getInputStream();
            }
            if (resourceName.toLowerCase().endsWith(".p12") || resourceName.toLowerCase().endsWith(".pfx")) {
                anFileType = true;
            }
            else {
                anFileType = false;
            }
            return readKeyfromPKCS12StoredFile(istream, password, anFileType);
        }
        catch (Exception ex) {
            throw new BytterSecurityException(20208, "readKeyfromPKCS12StoredFile error", ex);
        }
    }

    public static Map<PrivateKey, Certificate> readKeyfromPKCS12StoredFile(InputStream istream, String password, boolean anFileType) {
        try {
            KeyStore keystore = null;
            if (anFileType) {
                keystore = KeyStore.getInstance("PKCS12");
            }
            else {
                keystore = KeyStore.getInstance("JKS");
            }

            keystore.load(istream, password.toCharArray());
            Enumeration enumeration = keystore.aliases();
            String alias = null;
            for (int i = 0; enumeration.hasMoreElements(); i++) {
                alias = enumeration.nextElement().toString();
                if (i >= 1) {
                    System.out.println("此文件中含有多个证书!");
                }
            }
            PrivateKey pk = (PrivateKey) keystore.getKey(alias, password.toCharArray());
            System.out.println(pk);
            Certificate pubK = keystore.getCertificate(alias);
            Map map = new HashMap();
            map.put(pk, pubK);
            return map;
        }
        catch (Exception ex) {
            throw new BytterSecurityException(20200, "readKeyfromPKCS12StoredFile error", ex);
        }
        finally {
            IOUtils.closeQuietly(istream);
        }
    }

    public static Certificate fromCerStoredFile(String resourceName) {

        return fromCerStoredXXFile(resourceName, true);
    }

    public static Certificate fromCerStoredClassPath(String resourceName) {

        return fromCerStoredXXFile(resourceName, false);
    }

    public static Certificate fromCerStoredXXFile(String resourceName, boolean anFile) {
        InputStream inputStream = null;
        try {
            if (anFile) {
                inputStream = new FileInputStream(resourceName);
            }
            else {

                ClassPathResource cc = new ClassPathResource(resourceName);
                inputStream = cc.getInputStream();
            }
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return cf.generateCertificate(inputStream);
        }
        catch (Exception ex) {
            throw new BytterSecurityException(20201, "fromCerStoredFile error", ex);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
        // return certificate == null ? null : certificate.getPublicKey();
    }

    public static Certificate fromCerBase64String(String anKeyStr) {
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(Base64.decodeBase64(anKeyStr));
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return cf.generateCertificate(inputStream);
        }
        catch (Exception ex) {
            throw new BytterSecurityException(20201, "fromCerBase64String error", ex);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
        // return certificate == null ? null : certificate.getPublicKey();
    }

    public static Certificate certFromPKCS12StoredFile(String resourceName, String password) {

        Map<PrivateKey, Certificate> map = readKeyfromPKCS12StoredFile(resourceName, password);
        for (Map.Entry<PrivateKey, Certificate> ent : map.entrySet()) {
            return ent.getValue();
        }

        return null;

    }

    public static PublicKey fromPKCS12StoredFile(String resourceName, String password) {

        return certFromPKCS12StoredFile(resourceName, password).getPublicKey();
    }

    public static String readByteFromFile(String resourceName) {
        return readByteFromFile(resourceName, true);
    }

    public static String readByteFromResource(String resourceName) {
        return readByteFromFile(resourceName, false);
    }

    private static String readByteFromFile(String resourceName, boolean anFileType) {
        InputStream istream;
        ByteArrayOutputStream baos;
        istream = null;
        baos = null;
        try {
            if (anFileType) {
                istream = new FileInputStream(resourceName);
            }
            else {
                ClassPathResource cc = new ClassPathResource(resourceName);
                istream = cc.getInputStream();
            }
            baos = new ByteArrayOutputStream();
            IOUtils.copy(istream, baos);

            return new String(baos.toByteArray(), "UTF-8");
        }
        catch (IOException e) {

            throw new BytterException("Failed to read key file: ", e);
        }
        finally {
            IOUtils.closeQuietly(istream);
        }
    }

    /**
     * 将证书文件转入到KeyStore中
     * @param anPath 文件在ClassPath的路径
     * @param anPassword  私钥密码
     * @return
     */
    public static KeyStore readKeyStoreFromPKCS12ClassPath(String anPath, String anPassword) {
        ClassPathResource cc = new ClassPathResource(anPath);
        InputStream inputStream = null;
        KeyStore keystore = null;
        try {
            inputStream = cc.getInputStream();
            keystore = KeyStore.getInstance("PKCS12");
            keystore.load(inputStream, anPassword.toCharArray());
            
            return keystore;
        }
        catch (Exception ex) {
            
            return null;
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static void main(String[] args) throws Exception {
        Certificate pk = fromCerBase64String("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");
        if (pk instanceof X509Certificate) {
            X509Certificate pp = (X509Certificate) pk;
            System.out.println(pp.getSubjectDN());
        }
        System.out.println(pk);
    }
}

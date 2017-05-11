package com.betterjr.common.security;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xml.security.Init;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.ElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xpath.XPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.betterjr.common.data.DataEncoding;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.utils.Encodes;

/**
 * 使用证书签名和加密类
 *
 * @author zhoucy
 *
 */
public class SignHelper {
    private static final Logger logger = LoggerFactory.getLogger(SignHelper.class);

    public static String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final SecureRandom random = new SecureRandom();
    private static Map<Key, Integer> keyMap = new HashMap(100);
    static {
        Init.init();
        try {
            ElementProxy.setDefaultPrefix(Constants.SignatureSpecNS, "ds");
        }
        catch (final XMLSecurityException e) {
            System.err.println("org.apache.xml.security.utils.Constants初始化出错！");
            throw new RuntimeException(e);
        }
    }

    public SignHelper() {
    }

    /**
     * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
     *
     * @param input
     *            原始字节数组
     * @param key
     *            符合AES要求的密钥
     * @param mode
     *            Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     */

    public static String aesEncrypt(final String anStr) {
        if (StringUtils.isNotBlank(anStr)) {
            try {
                return Encodes.encodeHex(aes(anStr.getBytes("UTF-8"), Cipher.ENCRYPT_MODE));
            }
            catch (final UnsupportedEncodingException e) {

            }
        }
        return null;
    }

    public static boolean isAesEncrypt(final String anStr) {
        try {
            final String tmpStr = aesDecrypt(anStr);

            return StringUtils.isNotBlank(tmpStr);
        }
        catch (final Exception ex) {
            return false;
        }
    }

    public static String aesDecrypt(final String anStr) {
        if (StringUtils.isNotBlank(anStr)) {

            try {
                return new String(aes(Encodes.decodeHex(anStr), Cipher.DECRYPT_MODE), "UTF-8");
            }
            catch (final UnsupportedEncodingException e) {

            }

        }
        return null;
    }

    public static byte[] aes(final byte[] input, final int mode) {
        try {
            final byte[] key = new byte[] { -97, 81, -34, 9, 70, -16, 116, 25, 0, 3, -23, 113, 98, 79, 29, 117 };

            final SecretKey secretKey = new SecretKeySpec(key, "AES");
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, secretKey);
            return cipher.doFinal(input);
        }
        catch (final GeneralSecurityException e) {
            throw new BytterException(20206, "decodeHex has error", e);
        }
    }

    public static String readFromFile(final String anFileName) {
        final byte[] bb = new byte[1024];
        InputStream in = null;
        try {
            in = new FileInputStream(anFileName);
            final int count = in.read(bb);
            return new String(bb, 0, count, "UTF-8");
        }
        catch (final IOException ex) {
            ex.printStackTrace();

            return "";
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static String randomBase64(final int length) {
        final byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);

        return Base64.encodeBase64String(randomBytes);
    }

    public static String signData(final String doc, final PrivateKey privateKey, final String anCharSet) {

        return signData(doc, privateKey, SIGN_ALGORITHMS, anCharSet);
    }

    public static String signData(final String doc, final PrivateKey privateKey) {

        return signData(doc, privateKey, SIGN_ALGORITHMS, "UTF-8");
    }

    public static String signData(final String doc, final PrivateKey privateKey, final String anSignAlgorithms, final String anCharSet) {
        try {
            final Signature signature = Signature.getInstance(anSignAlgorithms);
            signature.initSign(privateKey);
            signature.update(doc.getBytes(anCharSet));
            final byte[] signed = signature.sign();

            return Base64.encodeBase64String(signed);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String clearBase64InStr(final String anStr) {
        if (StringUtils.isNotBlank(anStr)) {
            final StringBuilder sb = new StringBuilder(anStr.length());
            for (final char cc : anStr.toCharArray()) {
                if (cc == '-') {
                    sb.append('+');
                }
                else if (cc == '_') {
                    sb.append('/');
                }
                else {
                    sb.append(cc);
                }
            }
            return sb.toString();
        }
        return "";
    }

    public static String clearBase64OutStr(final String anStr) {
        if (StringUtils.isNotBlank(anStr)) {
            final StringBuilder sb = new StringBuilder(anStr.length());
            for (final char cc : anStr.toCharArray()) {
                if (cc == '+') {
                    sb.append('-');
                }
                else if (cc == '/') {
                    sb.append('_');
                }
                else {
                    sb.append(cc);
                }
            }
            return sb.toString();
        }
        return "";
    }

    public static boolean verifySign(final String anContent, final String anSignData, final Certificate anPubKey){

        return verifySign(anContent, anSignData, anPubKey, "UTF-8");
    }

    public static boolean verifySign(final String anContent, final String anSignData, final Certificate anPubKey, final String anCharSet) {

        return verifySign(anContent, anSignData, anPubKey.getPublicKey(), anCharSet);
    }

    public static boolean verifySign(final String anContent, final String anSignData, final PublicKey anPubKey, final String anCharSet) {

        return verifySign(anContent, anSignData, anPubKey, SIGN_ALGORITHMS, anCharSet);
    }

    /**
     * 用SHA算法计算摘要
     *
     * @param contents
     *            字符串
     * @return
     * @throws Exception
     */
    public static String digest(final byte[] contents, final String anAlgorithms) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(anAlgorithms);

            final byte[] digestbyte = messageDigest.digest(contents);
            return new String(Hex.encodeHex(digestbyte));
        }
        catch (final NoSuchAlgorithmException e) {
            throw new BytterSecurityException(13123, " digest  NoSuchAlgorithmException", e);
        }
    }
    public static String signFile(final File anFile, final PrivateKey anPrivateKey) {
        return signFile(anFile, anPrivateKey, SIGN_ALGORITHMS, DataEncoding.BASE64);
    }
    public static String signFile(final File anFile, final PrivateKey anPrivateKey, final String anAlgorithms, final DataEncoding anEncoding) {

        return signAndDigestFile(anFile, anAlgorithms, anEncoding, true, anPrivateKey);
    }

    public static String digestFile(final File anFile, final String anAlgorithms, final DataEncoding anEncoding) {

        return signAndDigestFile(anFile, anAlgorithms, anEncoding, false, null);
    }

    private static String signAndDigestFile(final File anFile, final String anAlgorithms, final DataEncoding anEncoding, final boolean anSign, final PrivateKey anPrivateKey) {
        InputStream in = null;

        logger.info("digestFile =" + anFile + ", use Algorithms = " + anAlgorithms);
        if (anFile != null && anFile.exists() && anFile.isFile()) {
            try {
                in = new BufferedInputStream(new FileInputStream(anFile));
                int readSize = 0;
                byte[] buffer = new byte[4096];
                if (anSign) {
                    final Signature signature = Signature.getInstance(anAlgorithms);
                    signature.initSign(anPrivateKey);
                    while ((readSize = in.read(buffer)) != -1) {
                        signature.update(buffer, 0, readSize);
                    }
                    buffer = signature.sign();
                }
                else {
                    final MessageDigest messageDigest = MessageDigest.getInstance(anAlgorithms);
                    while ((readSize = in.read(buffer)) != -1) {
                        messageDigest.update(buffer, 0, readSize);
                    }
                    buffer = messageDigest.digest();
                }
                IOUtils.closeQuietly(in);
                return anEncoding.encodeData(buffer);
            }
            catch (final IOException e) {
                logger.warn("digest IOException ， anFileName = " + anFile);
                throw new BytterSecurityException(13123, "digest NoSuchAlgorithmException", e);
            }
            catch (final NoSuchAlgorithmException e) {
                logger.warn("digest NoSuchAlgorithmException ， Algorithms = " + anAlgorithms);
                throw new BytterSecurityException(13123, "digest NoSuchAlgorithmException", e);
            }
            catch (final InvalidKeyException e) {
                logger.warn("digest InvalidKeyException ， Algorithms = " + anAlgorithms);
                throw new BytterSecurityException(13123, "sign InvalidKeyException", e);
            }
            catch (final SignatureException e) {
                logger.warn("SignatureException   ， Algorithms = " + anAlgorithms);
                throw new BytterSecurityException(13123, "SignatureException  ", e);
            }
            finally {
                IOUtils.closeQuietly(in);
            }
        }
        else {
            logger.warn("digestFile :" + anFile + ", not exists");
            throw new BytterSecurityException(13123, "digestFile :" + anFile + ", not exists");
        }
    }

    public static String digestBase64(final byte[] contents, final String anAlgorithms) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(anAlgorithms);

            final byte[] digestbyte = messageDigest.digest(contents);
            return Base64.encodeBase64URLSafeString(digestbyte);
        }
        catch (final NoSuchAlgorithmException e) {
            throw new BytterSecurityException(13123, " digest  NoSuchAlgorithmException", e);
        }
    }

    public static boolean verifySign(final String anContent, final String anSignData, final PublicKey anPubKey, final String anSignAlgorithms, final String anCharSet) {
        try {
            return verifySign(anContent.getBytes(anCharSet), anSignData, anPubKey, anSignAlgorithms);
        }
        catch (final UnsupportedEncodingException e) {
            return false;
        }
    }

    public static boolean verifySignStream(final InputStream in, final String anSignData, final PublicKey anPubKey) {
        return verifySignAndDigestStream(in, anSignData, SIGN_ALGORITHMS, DataEncoding.BASE64, true, anPubKey);
    }

    public static boolean verifySignAndDigestStream(
            final InputStream in , final String anSignData, final String anAlgorithms, final DataEncoding anEncoding, final boolean anSign,
            final PublicKey anPubKey) {
        boolean result = false;
        try {
            int readSize = 0;
            byte[] buffer = new byte[4096];
            final byte[] signData = anEncoding.decodeData(anSignData);
            if (anSign) {
                final Signature signature = Signature.getInstance(anAlgorithms);
                signature.initVerify(anPubKey);
                while ((readSize = in.read(buffer)) != -1) {
                    signature.update(buffer, 0, readSize);
                }
                result = signature.verify(signData);
            }
            else {
                final MessageDigest messageDigest = MessageDigest.getInstance(anAlgorithms);
                while ((readSize = in.read(buffer)) != -1) {
                    messageDigest.update(buffer, 0, readSize);
                }
                buffer = messageDigest.digest();
                result = buffer.equals(signData);
            }
            IOUtils.closeQuietly(in);
        }
        catch (final IOException e) {
            logger.warn("digest IOException ， anFileName = " );
            // throw new BytterSecurityException(13123, "digest NoSuchAlgorithmException", e);
        }
        catch (final NoSuchAlgorithmException e) {
            logger.warn("digest NoSuchAlgorithmException ， Algorithms = " + anAlgorithms);
            // throw new BytterSecurityException(13123, "digest NoSuchAlgorithmException", e);
        }
        catch (final InvalidKeyException e) {
            logger.warn("digest InvalidKeyException ， Algorithms = " + anAlgorithms);
            // throw new BytterSecurityException(13123, "sign InvalidKeyException", e);
        }
        catch (final SignatureException e) {
            logger.warn("SignatureException   ， Algorithms = " + anAlgorithms);
            // throw new BytterSecurityException(13123, "SignatureException  ", e);
        }
        finally {
            IOUtils.closeQuietly(in);
        }
        return result;
    }

    public static boolean verifySignFile(final File anFile, final String anSignData, final PublicKey anPubKey) {
        return verifySignAndDigestFile(anFile, anSignData, SIGN_ALGORITHMS, DataEncoding.BASE64, true, anPubKey);
    }
    public static boolean verifySignFile(final File anFile, final String anSignData, final DataEncoding anEncoding, final String anAlgorithms, final PublicKey anPubKey) {

        return verifySignAndDigestFile(anFile, anSignData, anAlgorithms, anEncoding, true, anPubKey);
    }

    public boolean verifyDigestFile(final File anFile, final String anSignData, final DataEncoding anEncoding, final String anAlgorithms) {

        return verifySignAndDigestFile(anFile, anSignData, anAlgorithms, anEncoding, false, null);
    }

    public static boolean verifySignAndDigestFile(final File anFile, final String anSignData, final String anAlgorithms, final DataEncoding anEncoding, final boolean anSign,
            final PublicKey anPubKey) {

        InputStream in = null;

        logger.info("verifySignFile =" + anFile + ", use Algorithms = " + anAlgorithms);
        boolean result = false;
        if (anFile != null && anFile.exists() && anFile.isFile()) {
            try {
                in = new BufferedInputStream(new FileInputStream(anFile));
                int readSize = 0;
                byte[] buffer = new byte[4096];
                final byte[] signData = anEncoding.decodeData(anSignData);
                if (anSign) {
                    final Signature signature = Signature.getInstance(anAlgorithms);
                    signature.initVerify(anPubKey);
                    while ((readSize = in.read(buffer)) != -1) {
                        signature.update(buffer, 0, readSize);
                    }
                    result = signature.verify(signData);
                }
                else {
                    final MessageDigest messageDigest = MessageDigest.getInstance(anAlgorithms);
                    while ((readSize = in.read(buffer)) != -1) {
                        messageDigest.update(buffer, 0, readSize);
                    }
                    buffer = messageDigest.digest();
                    result = buffer.equals(signData);
                }
                IOUtils.closeQuietly(in);
            }
            catch (final IOException e) {
                logger.warn("digest IOException ， anFileName = " + anFile);
                // throw new BytterSecurityException(13123, "digest NoSuchAlgorithmException", e);
            }
            catch (final NoSuchAlgorithmException e) {
                logger.warn("digest NoSuchAlgorithmException ， Algorithms = " + anAlgorithms);
                // throw new BytterSecurityException(13123, "digest NoSuchAlgorithmException", e);
            }
            catch (final InvalidKeyException e) {
                logger.warn("digest InvalidKeyException ， Algorithms = " + anAlgorithms);
                // throw new BytterSecurityException(13123, "sign InvalidKeyException", e);
            }
            catch (final SignatureException e) {
                logger.warn("SignatureException   ， Algorithms = " + anAlgorithms);
                // throw new BytterSecurityException(13123, "SignatureException  ", e);
            }
            finally {
                IOUtils.closeQuietly(in);
            }
        }
        else {
            logger.warn("digestFile :" + anFile + ", not exists");
            // throw new BytterSecurityException(13123, "digestFile :" + anFile + ", not exists");
        }
        return result;
    }

    public static boolean verifySign(final byte[] anContent, final String anSignData, final PublicKey anPubKey, final String anSignAlgorithms) {
        final byte[] decoded = Base64.decodeBase64(anSignData);

        Signature signature;
        try {
            signature = Signature.getInstance(anSignAlgorithms);
            signature.initVerify(anPubKey);
            signature.update(anContent);
            return signature.verify(decoded);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean verifyXmlSign(final String signXml, final PublicKey pubKey) {
        Element signElement;
        XMLSignature signature;
        try {
            System.out.println(signXml);
            final Document doc = DocumentUtil.getDocFromString(signXml);
            final Element nscontext = XMLUtils.createDSctx(doc, "ds", "http://www.w3.org/2000/09/xmldsig#");
            signElement = (Element) XPathAPI.selectSingleNode(doc, "//ds:Signature[1]", nscontext);
            if (signElement == null) {
                return false;
            }
            // String msgType ="ResBody";
            // Element messageNode = (Element) doc.getElementsByTagName("Message").item(0);
            // Element dataNode = (Element) messageNode.getElementsByTagName(msgType).item(0);
            // dataNode.setIdAttributeNS(null, "id", true);
            // dataNode.setAttributeNS(null, "id", msgType);
            // Attr idAttr = dataNode.getAttributeNode("id");
            // dataNode.setIdAttributeNode(idAttr, true);

            signature = new XMLSignature(signElement, doc.getDocumentURI());
            return signature.checkSignatureValue(pubKey);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static String signXml(final Document doc, final PrivateKey privateKey, final String msgType, final String anEncoding) {
        try {

            final XMLSignature sig = new XMLSignature(doc, doc.getDocumentURI(), "http://www.w3.org/2000/09/xmldsig#rsa-sha1");
            // sig.getSignedInfo().addResourceResolver(new OfflineResolver());
            final Element messageNode = (Element) doc.getElementsByTagName("Message").item(0);
            // Element dataNode = (Element) messageNode.getElementsByTagName(msgType).item(0);
            // dataNode.setIdAttributeNS(null, "id", true);

            // dataNode.setAttributeNS(null, "id", msgType);
            // Attr idAttr = dataNode.getAttributeNode("id");
            // dataNode.setIdAttributeNode(idAttr, true);
            messageNode.appendChild(sig.getElement());
            final Transforms transforms = new Transforms(doc);
            transforms.addTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
            sig.addDocument("#" + msgType, transforms, "http://www.w3.org/2000/09/xmldsig#sha1");
            sig.sign(privateKey);
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            XMLUtils.outputDOM(doc, os);
            return os.toString(anEncoding);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int findKeySize(final Key anPublicKey) {
        final String algorithm = anPublicKey.getAlgorithm();
        KeyFactory keyFact;
        final Integer mySize = keyMap.get(anPublicKey);
        if (mySize != null) {
            return mySize.intValue();
        }
        try {
            keyFact = KeyFactory.getInstance(algorithm);
            BigInteger prime = null;
            if ("RSA".equals(algorithm)) {
                if (anPublicKey instanceof PublicKey) {
                    final RSAPublicKeySpec keySpec = keyFact.getKeySpec(anPublicKey, RSAPublicKeySpec.class);
                    prime = keySpec.getModulus();
                }
                else if (anPublicKey instanceof PrivateKey) {
                    final RSAPrivateKeySpec keySpec = keyFact.getKeySpec(anPublicKey, RSAPrivateKeySpec.class);
                    prime = keySpec.getModulus();
                }
            }
            else if ("DSA".equals(algorithm)) {
                if (anPublicKey instanceof PublicKey) {
                    final DSAPublicKeySpec keySpec = keyFact.getKeySpec(anPublicKey, DSAPublicKeySpec.class);
                    prime = keySpec.getP();
                }
                else if (anPublicKey instanceof PrivateKey) {
                    final DSAPrivateKeySpec keySpec = keyFact.getKeySpec(anPublicKey, DSAPrivateKeySpec.class);
                    prime = keySpec.getP();
                }
            }
            if (prime != null) {
                final int len = prime.toString(2).length();
                keyMap.put(anPublicKey, new Integer(len));

                return len;
            }
            else {
                System.out.println("this not find for me!");
                return 1024;
            }
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BytterSecurityException(56789, "findKeySize is NoSuchAlgorithmException | InvalidKeySpecException", e);
        }
    }

    /**
     * 分段解密<br>
     * 用私钥解密
     *
     * @param data
     *            数据
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(final String anData, final PrivateKey privateKey) {

        return decrypt(anData, privateKey, "UTF-8");
    }

    public static String decrypt(final String anData, final PrivateKey privateKey, final String anCharset ) {
        try {
            final int keyBitSize = findKeySize(privateKey);
            final int MAX_DECRYPT_BLOCK = (keyBitSize >> 3);
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final byte[] data = Base64.decodeBase64(anData);
            final int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                }
                else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            final byte[] decryptedData = out.toByteArray();

            return new String(decryptedData, anCharset);
        }
        catch (final Exception e) {
            e.printStackTrace();
            throw new BytterSecurityException(20203, "SignHelper decrypt has error", e);
        }

    }

    /**
     * 分段加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(final String anData, final PublicKey pubKey) {

        return encrypt(anData, pubKey, "UTF-8");
    }

    public static String encrypt(final String anData, final PublicKey pubKey, final String anCharset) {
        try {
            // 对数据加密
            final int keyBitSize = findKeySize(pubKey);
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            // 长度是位数除掉11位
            final int MAX_ENCRYPT_BLOCK = (keyBitSize >> 3) - 11;
            final byte[] data = anData.getBytes(anCharset);
            final int inputLen = data.length;
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                }
                else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            out.flush();
            final byte[] encryptedData = out.toByteArray();
            out.close();
            return Base64.encodeBase64String(encryptedData);
        }
        catch (final Exception e) {
            e.printStackTrace();
            throw new BytterSecurityException(20203, "SignHelper encrypt has error", e);
        }
    }

    public static void main(final String args[]) throws Exception {
        final String fileName = "D:\\Documents\\数字证书\\huaqiang.pfx";
        final String passWord = "huaqiang";
        final String matchKey = "D:\\libs\\test.cer";
        final String tt = aesEncrypt("0");
        System.out.println(tt);
        System.out.println(aesDecrypt(tt));

        /*
         * PrivateKey prvKey = KeyReader.readPrivateKeyfromPKCS12StoredFile(fileName, passWord); PublicKey ppKey =
         * KeyReader.fromPKCS12StoredFile(fileName, passWord); String kk = "this is dog"; CustKeyManager keyManager = new CustKeyManager(fileName,
         * passWord, matchKey); String ss = encrypt(kk, keyManager.getPubKey());
         */
        // System.out.println(ss);
        // System.out.println(keyManager.decrypt(ss));
        // System.out.println(verifySign(kk, ss, keyManager.getPubKey()));
        // System.out.println(randomBase64(64));
    }

}

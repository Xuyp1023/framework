package com.betterjr.common.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

/**
 * 某些参数导致不通用
 */
public class TripleDesUtil {

    private static final byte[] DEFAULT_IVPARAMS = new byte[8];
    private static final String DEFAULT_DESKEYALGORITHM = "DESede";
    private static final String DEFAULT_CIPHERALGORITHM = DEFAULT_DESKEYALGORITHM + "/" + "CBC" + "/" + "PKCS5Padding";
    private static final char[] BCD_LOOKUP = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F' };

    /**
     * 以字节的方式读取文件
     * 
     * @param file
     */
    public static byte[] readFileByByte(File file) {

        InputStream in = null;
        long length = file.length();
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        try {
            in = new FileInputStream(file);
            while (offset < bytes.length && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }

        }
        catch (Exception e) {}
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ex) {}
            }
        }
        return bytes;
    }

    public static byte[] encryptBy3Des(byte[] input, String key) throws Exception {
        if (input == null || input.length == 0) {
            return new byte[] {};
        }
        Cipher encryptCipher = getDesCipher(Cipher.ENCRYPT_MODE, key);
        return encryptCipher.doFinal(input);
    }

    public static String encryptBy3DesInHex(byte[] input, String key) throws Exception {
        byte[] encrypt = encryptBy3Des(input, key);
        return bytesToHexStr(encrypt);
    }

    private static Cipher getDesCipher(int mode, String key) throws Exception {
        Key desKey = getDesKey(key);
        Cipher cipher = Cipher.getInstance(getDESCipherAlgorithm(), getProvider());
        cipher.init(mode, desKey, new IvParameterSpec(getIVParamsContent()));

        return cipher;
    }

    private static String getDESCipherAlgorithm() {
        return DEFAULT_CIPHERALGORITHM;
    }

    private static Provider getProvider() {
        return new org.bouncycastle.jce.provider.BouncyCastleProvider();
    }

    private static byte[] getIVParamsContent() {
        return DEFAULT_IVPARAMS;
    }

    private static Key getDesKey(String keyStr) throws Exception {
        byte input[] = Hex.decode(keyStr);
        DESedeKeySpec keySpec = new DESedeKeySpec(input);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(getDESKeyAlgorithm(), getProvider());
        return keyFactory.generateSecret(keySpec);
    }

    protected static String getDESKeyAlgorithm() {
        return DEFAULT_DESKEYALGORITHM;
    }

    public static String bytesToHexStr(byte[] paramArrayOfByte) {
        StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length * 2 + 1);
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            localStringBuilder.append(BCD_LOOKUP[(paramArrayOfByte[i] >>> 4 & 0xF)]);
            localStringBuilder.append(BCD_LOOKUP[(paramArrayOfByte[i] & 0xF)]);
        }
        return localStringBuilder.toString();
    }

    public static OutputStream getBufferedOutputStream(File paramFile) throws Exception {
        return new BufferedOutputStream(FileUtils.openOutputStream(paramFile));
    }

    public static void closeOutputStream(OutputStream paramOutputStream) {
        IOUtils.closeQuietly(paramOutputStream);
    }

    public static boolean persistFile(String paramString, File paramFile) throws Exception {
        if (paramString == null || paramString.equals("")) {
            return false;
        }
        OutputStream localOutputStream = getBufferedOutputStream(paramFile);
        IOUtils.write(paramString, localOutputStream);
        closeOutputStream(localOutputStream);
        return true;
    }

    public static boolean persistFile(byte[] paramArrayOfByte, File paramFile) throws Exception {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            return false;
        }
        OutputStream localOutputStream = getBufferedOutputStream(paramFile);
        IOUtils.write(paramArrayOfByte, localOutputStream);
        localOutputStream.close();
        return true;
    }

    public static boolean getEncryptFileInHexBy3Des(File origFile, File encryptFile, String key) throws Exception {
        byte[] origFileArray = readFileByByte(origFile);
        String hexContent = encryptBy3DesInHex(origFileArray, key);
        return persistFile(hexContent, encryptFile);
    }

    public static String getFileContent(File paramFile) throws Exception {
        FileInputStream localFileInputStream = FileUtils.openInputStream(paramFile);
        String str = "";
        try {
            str = IOUtils.toString(localFileInputStream);
        }
        catch (Exception e) {}
        finally {
            IOUtils.closeQuietly(localFileInputStream);
        }
        return str;
    }

    public static byte[] hexStrToBytes(String paramString) {
        byte[] arrayOfByte = new byte[paramString.length() / 2];
        int i = 0;
        int j = arrayOfByte.length;
        while (i < j) {
            arrayOfByte[i] = (byte) Integer.parseInt(paramString.substring(2 * i, 2 * i + 2), 16);
            i++;
        }
        return arrayOfByte;
    }

    public static byte[] decryptBy3DesInHex(String hexContent, String key) throws Exception {
        byte[] input = hexStrToBytes(hexContent);
        return decryptBy3Des(input, key);
    }

    public static byte[] decryptBy3Des(byte[] input, String key) throws Exception {
        if (input == null || input.length == 0) {
            return new byte[] {};
        }
        Cipher decryptCipher = getDesCipher(Cipher.DECRYPT_MODE, key);
        return decryptCipher.doFinal(input);
    }

    public static boolean getDecryptFileInHexBy3Des(File encryptFile, File origFile, String key) throws Exception {
        String hexEncrptContent = getFileContent(encryptFile);
        byte[] decryptArray = decryptBy3DesInHex(hexEncrptContent, key);
        return persistFile(decryptArray, origFile);
    }

    @Test
    public void test3DEDES() throws Exception {
        String fileKey = "048401251f27be44d2f2d0f2fc384bc7c96f39b3d71f0544";
        String localFile = "C:\\app\\test.txt";
        TripleDesUtil.getDecryptFileInHexBy3Des(new File(localFile), new File(localFile), fileKey);
    }

    @Test
    public void test3DES() throws Exception {
        String fileKey = "048401251f27be44d2f2d0f2fc384bc7c96f39b3d71f0544";
        String localFile = "C:\\app\\BTDictData.js";
        TripleDesUtil.getEncryptFileInHexBy3Des(new File(localFile), new File(localFile), fileKey);

    }
}

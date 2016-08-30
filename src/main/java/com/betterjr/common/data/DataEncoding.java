package com.betterjr.common.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.Base62;
import com.betterjr.common.utils.Encodes;
/**
 * base系列编码工具类
 *
 */
public enum DataEncoding {

    BASE32, BASE62, BASE64, BASE64_SAFE, BASE16, NORMALl;
    public static DataEncoding checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return DataEncoding.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        return null;
    }

    /**
     * 生成base编码字符串
     * @param anBytes
     * @return
     */
    public String encodeData(byte[] anBytes) {
        switch (this) {
        case BASE32:
            return new Base32().encodeAsString(anBytes);

        case BASE62:
            try {
                return Base62.toBase62(anBytes);
            }
            catch (IOException e1) {
                return "";
            }

        case BASE64:
            return Encodes.encodeBase64(anBytes);

        case BASE64_SAFE:
            return Base64.encodeBase64URLSafeString(anBytes);

        case BASE16:
            return Encodes.encodeHex(anBytes);

        case NORMALl:
        default:
            try {
                return new String(anBytes, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {

            }
        }
        return "";
    }

    private byte[] base64SafeDecode(String anData) {
        StringBuilder sb = new StringBuilder(anData.length());
        for (char cc : anData.toCharArray()){
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
        
        return Encodes.decodeBase64(sb.toString());
    }

    /**
     * 解码base字符串
     * @param anData
     * @return
     */
    public byte[] decodeData(String anData) {
        switch (this) {
        case BASE32:
            return new Base32().decode(anData);

        case BASE62:
            try {
                return Base62.fromBase62(anData);
            }
            catch (IOException e1) {
                return new byte[] {};
            }

        case BASE64:
            return Encodes.decodeBase64(anData);

        case BASE64_SAFE:
            return base64SafeDecode(anData);

        case BASE16:
            return Encodes.decodeHex(anData);

        case NORMALl:
        default:
            try {
                return anData.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException e) {

            }
        }
        return new byte[] {};

    }
}

package com.betterjr.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Base62 编码&解码工具类 
 *
 */
public class Base62 {

    private static String Base62CodingSpace = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String BASE62_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** default constructor prevents util class from being created. */
    private Base62() {}

    /**
     * 生成base62编码字符串
     * @param original
     * @return
     * @throws IOException
     */
    public static String toBase62(byte[] original) throws IOException {
        StringBuilder sb = new StringBuilder();
        BitInputStream stream = new BitInputStream(original);
        byte[] read = new byte[1]; // only read 6-bit at a time

        while (stream.hasMore()) {
            int length = stream.readBits(read, 0, 6); // try to read 5 bits
            if (length <= 0) {
                break;
            }
            if ((read[0] >> 1) == 0x1f) { // first 5-bit is 11111
                sb.append(Base62CodingSpace.charAt(61));
                stream.seekBit(-1);
            } else if ((read[0] >> 1) == 0x1e) { // first 5-bit is 11110
                sb.append(Base62CodingSpace.charAt(60));
                stream.seekBit(-1);
            } else {
                sb.append(Base62CodingSpace.charAt(read[0]));
            }
        }
        return sb.toString();
    }

    /**
     * 从base62字符串中解码
     * @param base62
     * @return
     * @throws IOException
     */
    public static byte[] fromBase62(String base62) throws IOException {
        ByteArrayOutputStream _out = new ByteArrayOutputStream();
        BitOutputStream out = new BitOutputStream(_out);

        for (int i = 0; i < base62.length(); i++) {
            int index = Base62CodingSpace.indexOf(base62.charAt(i));

            if (i == base62.length() - 1) {
                int mod = out.getiBitCount() % 8;
                if (mod != 0) {
                    out.writeBits(index, 8 - mod);
                }
            } else {
                if (index == 60) {
                    out.writeBits(0x1e, 5);
                } else if (index == 61) {
                    out.writeBits(0xf8, 5);
                } else {
                    out.writeBits(index, 6);
                }
            }
        }
        _out.flush();
        return _out.toByteArray();
    }

}

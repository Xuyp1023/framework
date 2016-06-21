package com.betterjr.common.utils;

public class BitInputStream {

    private byte[] buffer;
    private int length;
    private int byteOffset;
    private int bitOffset;

    /**
     * 
     * @param bytes
     */
    public BitInputStream(byte[] bytes) {
        this.buffer = bytes;
        this.length = bytes.length;
        this.byteOffset = 0;
        this.bitOffset = 0;
    }

    /**
     * 
     * @return
     */
    synchronized public boolean hasMore() {
        return ((byteOffset * 8) + bitOffset) < (length * 8);
    }

    /**
     * 
     * @param nbit
     * @return
     */
    synchronized public int seekBit(int pos) {
        int nbyte = (bitOffset + pos) / 8;
        int nbit = (bitOffset + pos) % 8;

        if (nbit < 0 || (byteOffset + nbyte) < 0
                || (((byteOffset + nbyte) * 8) + nbit) > (length * 8)) {
            throw new IndexOutOfBoundsException();
        }

        bitOffset = nbit;
        byteOffset += nbyte;

        return pos > 0 ? pos : pos * -1;
    }

    /**
     * Just for UnitTest
     * 
     * @param args
     */
    public static void main2(String[] args) {
        byte[] bytes = { 67, 68 };

        BitInputStream in = new BitInputStream(bytes);
        int bit = in.seekBit(17);

        System.out.println("seek: " + bit);
        System.out.println(in.readBit());
    }

    /**
     * 
     * @param out
     * @param length
     * @return
     * @throws IOException
     */
    synchronized public int readBits(byte[] out, int offset, int length) {
        if (offset < 0 || (out.length - offset) * 8 < length
                || out.length * 8 < length || length >= 32) {
            throw new IndexOutOfBoundsException();
        }

        int bitCount = 0;
        int byteIndex = offset;

        for (int i = length - 1; i >= 0;) {
            if (!hasMore()) {
                return bitCount;
            }

            int count = 0;
            int value = 0;

            do {
                int bit = readBit();
                if (bit == -1) {
                    break;
                }
                value |= bit << count;
                count++;
                bitCount++;
                i--;
            } while (i >= 0 && count < 8);

            out[byteIndex++] = (byte) (value & 0xff);
        }

        return bitCount;
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    synchronized public int readBit() {
        if (!hasMore()) {
            return -1;
        }
        if (bitOffset == 8) {
            byteOffset++;
            bitOffset = 0;
        }
        int bit = buffer[byteOffset] & (1 << bitOffset++);
        return bit > 0 ? 1 : 0;
    }
}

package com.betterjr.common.mq.codec;

/**
 * 
 * @author liuwl
 *
 */
public enum MQCodecType {
    FST((byte) 0x01), HESSIAN((byte) 0x02), JSON((byte) 0x03), KRYO((byte) 0x04), NATIVEJAVA((byte) 0x05);
    private byte codecType;

    MQCodecType(final byte anCodecType) {
        this.codecType = anCodecType;
    }

    public byte getCodecType() {
        return codecType;
    }

    public void setCodecType(byte codecType) {
        this.codecType = codecType;
    }
}

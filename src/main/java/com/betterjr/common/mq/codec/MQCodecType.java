package com.betterjr.common.mq.codec;

import com.betterjr.common.codec.support.fst.FstSerialization;
import com.betterjr.common.codec.support.hessian.Hessian2Serialization;
import com.betterjr.common.codec.support.json.JacksonSerialization;
import com.betterjr.common.codec.support.kryo.KryoSerialization;
import com.betterjr.common.codec.support.nativejava.NativeJavaSerialization;

/**
 * 
 * @author liuwl
 *
 */
public enum MQCodecType {
    FST(FstSerialization.TYPEID), HESSIAN(Hessian2Serialization.TYPEID), JSON(JacksonSerialization.TYPEID), KRYO(
            KryoSerialization.TYPEID), NATIVEJAVA(NativeJavaSerialization.TYPEID);

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

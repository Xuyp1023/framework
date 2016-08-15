package com.betterjr.common.mq.codec;

import com.betterjr.common.codec.BtCodec;
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
public final class MQCodecFactory {
    private final static BtCodec FST_CODEC = new FstSerialization();
    private final static BtCodec HESSIAN_CODEC = new Hessian2Serialization();
    private final static BtCodec JSON_CODEC = new JacksonSerialization();
    private final static BtCodec KRYO_CODEC = new KryoSerialization();
    private final static BtCodec NATIVEJAVA_CODEC = new NativeJavaSerialization();

    public final static BtCodec getCodec(MQCodecType anCodecType) {
        return getCodec(anCodecType.getCodecType());
    }

    public final static BtCodec getCodec(final byte anCodecFlag) {
        switch (anCodecFlag) {
        case FstSerialization.TYPEID:
            return FST_CODEC;
        case Hessian2Serialization.TYPEID:
            return HESSIAN_CODEC;
        case JacksonSerialization.TYPEID:
            return JSON_CODEC;
        case KryoSerialization.TYPEID:
            return KRYO_CODEC;
        case NativeJavaSerialization.TYPEID:
            return NATIVEJAVA_CODEC;
        default:
            return null;
        }
    }
}

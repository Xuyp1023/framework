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
public final class BtCodecFactory {

    private final static BtCodec FST_CODEC = new FstSerialization();
    private final static BtCodec HESSIAN_CODEC = new Hessian2Serialization();
    private final static BtCodec JSON_CODEC = new JacksonSerialization();
    private final static BtCodec KRYO_CODEC = new KryoSerialization();
    private final static BtCodec NATIVEJAVA_CODEC = new NativeJavaSerialization();

    public final static BtCodec getCodec(BtCodecType anCodecType) {
        return getCodec(anCodecType.getCodecType());
    }

    public final static BtCodec getCodec(final byte anCodecFlag) {
        switch (anCodecFlag) {
        case (byte) 0x01:
            return FST_CODEC;
        case (byte) 0x02:
            return HESSIAN_CODEC;
        case (byte) 0x03:
            return JSON_CODEC;
        case (byte) 0x04:
            return KRYO_CODEC;
        case (byte) 0x05:
            return NATIVEJAVA_CODEC;
        default:
            return null;
        }
    }
}

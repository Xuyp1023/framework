package com.betterjr.common.mq.codec;

import java.util.HashMap;
import java.util.Map;

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

    private final static Map<Byte, BtCodec> btCodecs = new HashMap<>();
    static {
        btCodecs.put(FstSerialization.TYPEID, new FstSerialization());
        btCodecs.put(Hessian2Serialization.TYPEID, new Hessian2Serialization());
        btCodecs.put(JacksonSerialization.TYPEID, new JacksonSerialization());
        btCodecs.put(KryoSerialization.TYPEID, new KryoSerialization());
        btCodecs.put(NativeJavaSerialization.TYPEID, new NativeJavaSerialization());
    }

    public final static BtCodec getCodec(final MQCodecType anCodecType) {
        return getCodec(anCodecType.getCodecType());
    }

    public final static BtCodec getCodec(final byte anCodecFlag) {
        return btCodecs.get(anCodecFlag);
    }
}

package com.betterjr.common.codec.support.kryo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.codec.BtObjectOutput;
import com.betterjr.common.codec.BtOptimizedCodec;
 
public class KryoSerialization implements BtOptimizedCodec {

    public byte getContentTypeId() {
        return 8;
    }

    public String getContentType() {
        return "x-application/kryo";
    }

    public BtObjectOutput serialize(  OutputStream out) throws IOException {
        return new KryoObjectOutput(out);
    }

    public BtObjectInput deserialize( InputStream is) throws IOException {
        return new KryoObjectInput(is);
    }
}
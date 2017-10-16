package com.betterjr.common.codec.support.hessian;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.betterjr.common.codec.BtCodec;
import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.codec.BtObjectOutput;

public class Hessian2Serialization implements BtCodec {

    public static final byte TYPEID = 2;

    @Override
    public byte getContentTypeId() {
        return TYPEID;
    }

    @Override
    public String getContentType() {
        return "x-application/hessian2";
    }

    @Override
    public BtObjectOutput serialize(OutputStream out) throws IOException {

        return new Hessian2ObjectOutput(out);
    }

    @Override
    public BtObjectInput deserialize(InputStream is) throws IOException {

        return new Hessian2ObjectInput(is);
    }

}
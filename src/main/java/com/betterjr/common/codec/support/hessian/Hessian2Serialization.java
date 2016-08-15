package com.betterjr.common.codec.support.hessian;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.betterjr.common.codec.BtCodec;
import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.codec.BtObjectOutput;
 
public class Hessian2Serialization implements BtCodec {

    public static final byte TYPEID = 2;

    public byte getContentTypeId() {
        return TYPEID;
    }

    public String getContentType() {
        return "x-application/hessian2";
    }

    public BtObjectOutput serialize(OutputStream out) throws IOException {
        
        return new Hessian2ObjectOutput(out);
    }

    public BtObjectInput deserialize(InputStream is) throws IOException {
        
        return new Hessian2ObjectInput(is);
    }

}
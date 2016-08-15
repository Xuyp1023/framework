package com.betterjr.common.codec.support.nativejava;

 
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.betterjr.common.codec.BtCodec;
import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.codec.BtObjectOutput;
 
public class NativeJavaSerialization implements BtCodec  {
    public static final byte TYPEID = 7;
    
    public static final String NAME = "nativejava";

    public byte getContentTypeId() {
        return 7;
    }

    public String getContentType() {
        return "x-application/nativejava";
    }

    public BtObjectOutput serialize(  OutputStream output) throws IOException {
        return new NativeJavaObjectOutput(output);
    }

    public BtObjectInput deserialize(  InputStream input) throws IOException {
        return new NativeJavaObjectInput(input);
    }
}

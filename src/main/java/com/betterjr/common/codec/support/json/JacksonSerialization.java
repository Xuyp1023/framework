package com.betterjr.common.codec.support.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.betterjr.common.codec.BtCodec;
import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.codec.BtObjectOutput;

public class JacksonSerialization implements BtCodec {
    public static final byte TYPEID = 20;

    @Override
    public byte getContentTypeId() {
        return TYPEID;
    }

    @Override
    public String getContentType() {
        return "text/json";
    }

    @Override
    public BtObjectOutput serialize(OutputStream output) throws IOException {
        return new JacksonObjectOutput(output);
    }

    @Override
    public BtObjectInput deserialize(InputStream input) throws IOException {
        return new JacksonObjectInput(input);
    }

}
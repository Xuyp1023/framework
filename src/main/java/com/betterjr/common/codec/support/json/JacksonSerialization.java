package com.betterjr.common.codec.support.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.betterjr.common.codec.*;

public class JacksonSerialization implements BtCodec {

    public byte getContentTypeId() {
        return 20;
    }

    public String getContentType() {
        return "text/json";
    }

    public BtObjectOutput serialize(OutputStream output) throws IOException {
        return new JacksonObjectOutput(output);
    }

    public BtObjectInput deserialize(InputStream input) throws IOException {
        return new JacksonObjectInput(input);
    }

}
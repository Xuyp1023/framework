package com.betterjr.common.codec.support.fst;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.codec.BtObjectOutput;
import com.betterjr.common.codec.BtOptimizedCodec;
 
public class FstSerialization implements BtOptimizedCodec {

    public byte getContentTypeId() {
        return 9;
    }

    public String getContentType() {
        return "x-application/fst";
    }

    public BtObjectOutput serialize(  OutputStream out) throws IOException {
        return new FstObjectOutput(out);
    }

    public BtObjectInput deserialize( InputStream is) throws IOException {
        return new FstObjectInput(is);
    }
}
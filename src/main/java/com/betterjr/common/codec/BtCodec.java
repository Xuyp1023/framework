package com.betterjr.common.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface BtCodec {
    byte getContentTypeId();

    String getContentType();

    BtObjectOutput serialize(OutputStream output) throws IOException;

    BtObjectInput deserialize(InputStream input) throws IOException;

}

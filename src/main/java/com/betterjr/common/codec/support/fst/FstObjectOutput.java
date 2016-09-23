package com.betterjr.common.codec.support.fst;

import java.io.IOException;
import java.io.OutputStream;

import com.betterjr.common.codec.BtObjectOutput;

import de.ruedigermoeller.serialization.FSTObjectOutput;


public class FstObjectOutput implements BtObjectOutput {

    private final FSTObjectOutput output;

    public FstObjectOutput(final OutputStream outputStream) {
        output = FstFactory.getDefaultFactory().getObjectOutput(outputStream);
    }

    @Override
    public void writeBool(final boolean v) throws IOException {
        output.writeBoolean(v);
    }

    @Override
    public void writeByte(final byte v) throws IOException {
        output.writeByte(v);
    }

    @Override
    public void writeShort(final short v) throws IOException {
        output.writeShort(v);
    }

    @Override
    public void writeInt(final int v) throws IOException {
        output.writeInt(v);
    }

    @Override
    public void writeLong(final long v) throws IOException {
        output.writeLong(v);
    }

    @Override
    public void writeFloat(final float v) throws IOException {
        output.writeFloat(v);
    }

    @Override
    public void writeDouble(final double v) throws IOException {
        output.writeDouble(v);
    }

    @Override
    public void writeBytes(final byte[] v) throws IOException {
        if (v == null) {
            output.writeInt(-1);
        } else {
            writeBytes(v, 0, v.length);
        }
    }

    @Override
    public void writeBytes(final byte[] v, final int off, final int len) throws IOException {
        if (v == null) {
            output.writeInt(-1);
        } else {
            output.writeInt(len);
            output.write(v, off, len);
        }
    }


    @Override
    public void writeUTF(final String v) throws IOException {
        output.writeUTF(v);
    }

    @Override
    public void writeObject(final Object v) throws IOException {
        output.writeObject(v);
    }

    @Override
    public void flushBuffer() throws IOException {
        output.flush();
    }
}
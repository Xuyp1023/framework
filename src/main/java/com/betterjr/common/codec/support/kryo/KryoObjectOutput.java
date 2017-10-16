package com.betterjr.common.codec.support.kryo;

import java.io.IOException;
import java.io.OutputStream;

import com.betterjr.common.codec.BtObjectOutput;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class KryoObjectOutput implements BtObjectOutput {

    private Kryo kryo = KryoFactory.getDefaultFactory().getKryo();
    private Output output;

    public KryoObjectOutput(OutputStream outputStream) {
        output = new Output(outputStream);
    }

    @Override
    public void writeBool(boolean v) throws IOException {
        output.writeBoolean(v);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        output.writeByte(v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        output.writeShort(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        output.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        output.writeLong(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        output.writeFloat(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        output.writeDouble(v);
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        if (v == null) {
            output.writeInt(-1);
        } else {
            writeBytes(v, 0, v.length);
        }
    }

    @Override
    public void writeBytes(byte[] v, int off, int len) throws IOException {
        if (v == null) {
            output.writeInt(-1);
        } else {
            output.writeInt(len);
            output.write(v, off, len);
        }
    }

    @Override
    public void writeUTF(String v) throws IOException {
        output.writeString(v);
    }

    @Override
    public void writeObject(Object v) throws IOException {
        kryo.writeClassAndObject(output, v);
    }

    @Override
    public void flushBuffer() throws IOException {
        output.flush();
    }
}
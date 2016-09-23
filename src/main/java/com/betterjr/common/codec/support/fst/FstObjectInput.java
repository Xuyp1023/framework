package com.betterjr.common.codec.support.fst;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import com.betterjr.common.codec.BtObjectInput;

import de.ruedigermoeller.serialization.FSTObjectInput;

public class FstObjectInput implements BtObjectInput {

    private final FSTObjectInput input;

    public FstObjectInput(final InputStream inputStream) {
        input = FstFactory.getDefaultFactory().getObjectInput(inputStream);
    }

    @Override
    public boolean readBool() throws IOException {
        return input.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return input.readByte();
    }

    @Override
    public short readShort() throws IOException {
        return input.readShort();
    }

    @Override
    public int readInt() throws IOException {
        return input.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return input.readLong();
    }

    @Override
    public float readFloat() throws IOException {
        return input.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return input.readDouble();
    }

    @Override
    public byte[] readBytes() throws IOException {
        final int len = input.readInt();
        if (len < 0) {
            return null;
        } else if (len == 0) {
            return new byte[]{};
        } else {
            final byte[] b = new byte[len];
            input.readFully(b);
            return b;
        }
    }

    @Override
    public String readUTF() throws IOException {
        return input.readUTF();
    }

    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(final Class<T> clazz) throws IOException, ClassNotFoundException {
        return (T) readObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(final Class<T> clazz, final Type type) throws IOException, ClassNotFoundException {
        return (T) readObject();
    }
}
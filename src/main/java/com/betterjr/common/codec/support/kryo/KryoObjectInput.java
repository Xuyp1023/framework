package com.betterjr.common.codec.support.kryo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import com.betterjr.common.codec.BtObjectInput;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;

public class KryoObjectInput implements BtObjectInput {

    private Kryo kryo = KryoFactory.getDefaultFactory().getKryo();
    private Input input;

    public KryoObjectInput(InputStream inputStream) {
        input = new Input(inputStream);
    }

    @Override
    public boolean readBool() throws IOException {
        try {
            return input.readBoolean();
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public byte readByte() throws IOException {
        try {
            return input.readByte();
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public short readShort() throws IOException {
        try {
            return input.readShort();
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int readInt() throws IOException {
        try {
            return input.readInt();
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public long readLong() throws IOException {
        try {
            return input.readLong();
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public float readFloat() throws IOException {
        try {
            return input.readFloat();
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public double readDouble() throws IOException {
        try {
            return input.readDouble();
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public byte[] readBytes() throws IOException {
        try {
            int len = input.readInt();
            if (len < 0) {
                return null;
            } else if (len == 0) {
                return new byte[] {};
            } else {
                return input.readBytes(len);
            }
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String readUTF() throws IOException {
        try {
            return input.readString();
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        try {
            return kryo.readClassAndObject(input);
        }
        catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> clazz) throws IOException, ClassNotFoundException {

        return (T) readObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> clazz, Type type) throws IOException, ClassNotFoundException {

        return readObject(clazz);
    }

}
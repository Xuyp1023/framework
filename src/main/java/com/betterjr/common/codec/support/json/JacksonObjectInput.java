package com.betterjr.common.codec.support.json;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonObjectInput implements BtObjectInput {
    private static Logger logger = LoggerFactory.getLogger(JacksonObjectInput.class);

    private final ObjectMapper objectMapper;
    private final Map<String, String> data;
    private static final String KEY_PREFIX = "$";
    private int index = 0;

    public JacksonObjectInput(InputStream inputstream) throws IOException {

        this.objectMapper = JsonMapper.getInstance();
        try {
            data = objectMapper.readValue(inputstream, Map.class);
        }
        catch (IOException e) {
            logger.error("parse inputstream error.", e);
            throw e;
        }
    }

    @Override
    public boolean readBool() throws IOException {
        try {
            return readObject(Boolean.class);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public byte readByte() throws IOException {
        try {
            return readObject(Byte.class);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public short readShort() throws IOException {
        try {
            return readObject(Short.class);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public int readInt() throws IOException {
        try {
            return readObject(Integer.class);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public long readLong() throws IOException {
        try {
            return readObject(Long.class);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public float readFloat() throws IOException {
        try {
            return readObject(Float.class);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public double readDouble() throws IOException {
        try {
            return readObject(Double.class);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public String readUTF() throws IOException {
        try {
            return readObject(String.class);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public byte[] readBytes() throws IOException {
        return readUTF().getBytes();
    }

    @Override
    public Object readObject() throws IOException, ClassNotFoundException {

        try {
            return readObject(Object.class);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        String json = this.data.get(KEY_PREFIX + (++index));
        String dataType = this.data.get(KEY_PREFIX + index + "t");
        if (dataType != null) {
            Class clazz = ReflectionUtils.desc2class(dataType);
            if (cls.isAssignableFrom(clazz)) {
                cls = clazz;
            } else {
                throw new IllegalArgumentException("Class \"" + clazz + "\" is not inherited from \"" + cls + "\"");
            }
        }
        logger.debug("index:{}, value:{}", index, json);
        return objectMapper.readValue(json, cls);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        return readObject(cls);
    }

}
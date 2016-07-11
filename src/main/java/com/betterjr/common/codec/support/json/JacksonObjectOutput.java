package com.betterjr.common.codec.support.json;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.betterjr.common.codec.BtObjectOutput;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonObjectOutput implements BtObjectOutput {

    private final ObjectMapper objectMapper;
    private final Map<String, Object> data;
    private final static String KEY_PREFIX = "$";
    private int index = 0;

    private final PrintWriter writer;

    public JacksonObjectOutput(OutputStream out) {
        this(new OutputStreamWriter(out));
    }

    public JacksonObjectOutput(Writer writer) {
        this.objectMapper = JsonMapper.getInstance();
        this.writer = new PrintWriter(writer);
        this.data = new HashMap<String, Object>();
    }

    public void writeBool(boolean v) throws IOException {
        writeObject0(v);
    }

    public void writeByte(byte v) throws IOException {
        writeObject0(v);
    }

    public void writeShort(short v) throws IOException {
        writeObject0(v);
    }

    public void writeInt(int v) throws IOException {
        writeObject0(v);
    }

    public void writeLong(long v) throws IOException {
        writeObject0(v);
    }

    public void writeFloat(float v) throws IOException {
        writeObject0(v);
    }

    public void writeDouble(double v) throws IOException {
        writeObject0(v);
    }

    public void writeUTF(String v) throws IOException {
        writeObject0(v);
    }

    public void writeBytes(byte[] b) throws IOException {
        writeObject0(new String(b));
    }

    public void writeBytes(byte[] b, int off, int len) throws IOException {
        writeObject0(new String(b, off, len));
    }

    public void writeObject(Object obj) throws IOException {
        if (obj == null) {
            writeObject0(obj);
            return;
        }
        writeObject0(obj);
        Class c = obj.getClass();
        String desc = ReflectionUtils.getDesc(c);
        data.put(KEY_PREFIX + (index) + "t", desc);

    }

    private void writeObject0(Object obj) throws IOException {
        data.put(KEY_PREFIX + (++index), objectMapper.writeValueAsString(obj));
    }

    public void flushBuffer() throws IOException {
        objectMapper.writeValue(writer, data);
        writer.println();
        writer.flush();
    }

}
package com.betterjr.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;


import com.betterjr.common.exception.BettjerIOException;
import com.betterjr.common.exception.BytterClassNotFoundException;

import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

/**
 * 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
 * 
 * @author zhoucy
 */
public class BTObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    /**
     * 注解到对象复制，只复制能匹配上的方法。
     * 
     * @param annotation
     * @param object
     */
    public static void annotationToObject(Object annotation, Object object) {
        if (annotation != null) {
            Class<?> annotationClass = annotation.getClass();
            Class<?> objectClass = object.getClass();
            for (Method m : objectClass.getMethods()) {
                if (StringUtils.startsWith(m.getName(), "set")) {
                    try {
                        String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
                        Object obj = annotationClass.getMethod(s).invoke(annotation);
                        if (obj != null && !"".equals(obj.toString())) {
                            if (object == null) {
                                object = objectClass.newInstance();
                            }
                            m.invoke(object, obj);
                        }
                    }
                    catch (Exception e) {
                        // 忽略所有设置失败方法
                    }
                }
            }
        }
    }

    /**
     * 序列化对象
     * 
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            if (object != null) {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化对象
     * 
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            if (bytes != null && bytes.length > 0) {
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeQuietly(Object obj) {
        if (obj != null) {
            try {
                Method mm = obj.getClass().getMethod("close", new Class[] {});
                mm.invoke(obj, new Object[] {});
            }
            catch (Exception ex) {

            }
        }
    }

    public static String fastSerializeStr(Object obj) {
        if (obj == null) {
            return "";
        }

        byte[] bb = fastSerialize(obj);

        return Base64.encodeBase64String(bb);
    }

    public static byte[] fastSerialize(Object obj) {
        ByteArrayOutputStream out = null;
        FSTObjectOutput fout = null;
        GZIPOutputStream gzout = null;
        try {
            out = new ByteArrayOutputStream();
            gzout = new GZIPOutputStream(out);
            fout = new FSTObjectOutput(gzout);
            fout.writeObject(obj);
            fout.flush();
            gzout.finish();
            return out.toByteArray();
        }
        catch (IOException ex) {
            throw new BettjerIOException(68001, "fastSerialize exception", ex);
        }
        finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(gzout);
            closeQuietly(fout);
        }
    }

    public static Object fastDeserializeStr(String bytes) {
        if (BetterStringUtils.isNotBlank(bytes)) {
            try {
                byte[] bb = Base64.decodeBase64(bytes);

                return fastDeserialize(bb);
            }
            catch (Exception ex) {

                return bytes;
            }
        }

        return null;
    }

    public static Object fastDeserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;
        FSTObjectInput in = null;
        ByteArrayInputStream bin = null;
        GZIPInputStream gzin = null;
        try {
            bin = new ByteArrayInputStream(bytes);
            gzin = new GZIPInputStream(bin);
            in = new FSTObjectInput(gzin);

            return in.readObject();
        }
        catch (Exception e) {
            throw new BytterClassNotFoundException(68002, "fastDeserialize", e);
        }
        finally {
            closeQuietly(in);
            IOUtils.closeQuietly(bin);
            IOUtils.closeQuietly(gzin);
        }
    }
    
    /**
     * 对象强制类型转换时，如果为空，返回为空
     */
    public static <T> T castSafety(Object obj,Class<T> cls){
    	if(obj==null){
    		return null;
    	}
    	return (T)obj;
    }
}

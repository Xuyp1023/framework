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
    public static void annotationToObject(final Object annotation, Object object) {
        if (annotation != null) {
            final Class<?> annotationClass = annotation.getClass();
            final Class<?> objectClass = object.getClass();
            for (final Method m : objectClass.getMethods()) {
                if (StringUtils.startsWith(m.getName(), "set")) {
                    try {
                        final String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
                        final Object obj = annotationClass.getMethod(s).invoke(annotation);
                        if (obj != null && !"".equals(obj.toString())) {
                            if (object == null) {
                                object = objectClass.newInstance();
                            }
                            m.invoke(object, obj);
                        }
                    }
                    catch (final Exception e) {
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
    public static byte[] serialize(final Object object) {
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
        catch (final Exception e) {
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
    public static Object unserialize(final byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            if (bytes != null && bytes.length > 0) {
                bais = new ByteArrayInputStream(bytes);
                final ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeQuietly(final Object obj) {
        if (obj != null) {
            try {
                final Method mm = obj.getClass().getMethod("close", new Class[] {});
                mm.invoke(obj, new Object[] {});
            }
            catch (final Exception ex) {

            }
        }
    }

    public static String fastSerializeStr(final Object obj) {
        if (obj == null) {
            return "";
        }

        final byte[] bb = fastSerialize(obj);

        return Base64.encodeBase64String(bb);
    }

    public static byte[] fastSerialize(final Object obj) {
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
        catch (final IOException ex) {
            throw new BettjerIOException(68001, "fastSerialize exception", ex);
        }
        finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(gzout);
            closeQuietly(fout);
        }
    }

    public static Object fastDeserializeStr(final String bytes) {
        if (StringUtils.isNotBlank(bytes)) {
            try {
                final byte[] bb = Base64.decodeBase64(bytes);

                return fastDeserialize(bb);
            }
            catch (final Exception ex) {

                return bytes;
            }
        }

        return null;
    }

    public static Object fastDeserialize(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        FSTObjectInput in = null;
        ByteArrayInputStream bin = null;
        GZIPInputStream gzin = null;
        try {
            bin = new ByteArrayInputStream(bytes);
            gzin = new GZIPInputStream(bin);
            in = new FSTObjectInput(gzin);

            return in.readObject();
        }
        catch (final Exception e) {
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
    public static <T> T castSafety(final Object obj, final Class<T> cls) {
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }
}

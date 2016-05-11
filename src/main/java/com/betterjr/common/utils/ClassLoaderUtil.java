package com.betterjr.common.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;

import com.betterjr.common.exception.BytterException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

/**
 * 打为jar包后也能找到配置文件，并以流的方式读取。 InputStream is = ClassLoaderUtil.getResourceAsStream("config/others/config.properties", MainTest.class); if (null != is)
 * { reader = new InputStreamReader(is, "UTF-8"); }
 * 
 * @author zhoucy
 */
public class ClassLoaderUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClassLoaderUtil.class);

    /**
     * 
     * <p>
     * 比较参数类型是否一致
     * </p>
     *
     * @param types
     *            asm的类型({@link Type})
     * @param clazzes
     *            java 类型({@link Class})
     * @return
     */
    private static boolean sameType(Type[] types, Class<?>[] clazzes) {
        // 个数不同
        if (types.length != clazzes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(clazzes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }

    public static byte[] class2Byte(Class anClass) {
        ByteArrayOutputStream aro = new ByteArrayOutputStream();
        try {
            InputStream in = getClassResource(anClass);

            IOUtils.copy(in, aro);
            byte[] bbc = aro.toByteArray();
            aro.close();
            return bbc;
        }
        catch (IOException e) {

            throw BytterException.unchecked(e);
        }
    }

    /**
     * 
     * <p>
     * 获取方法的参数名
     * </p>
     *
     * @param m
     * @return
     */
    public static String[] getMethodParamNames(Class anClass, final Method m) {
        final String[] paramNames = new String[m.getParameterTypes().length];
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        ClassReader cr = null;
        cr = new ClassReader(class2Byte(anClass));
        cr.accept(new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(desc);
                // 方法名相同并且参数个数相同
                if (!name.equals(m.getName()) || !sameType(args, m.getParameterTypes())) {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
                MethodVisitor v = cv.visitMethod(access, name, desc, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM4, v) {
                    @Override
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if (Modifier.isStatic(m.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < paramNames.length) {
                            paramNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }

                };
            }
        }, 0);
        return paramNames;
    }

    public static String getAppPath(Class cls) {
        ClassLoader loader = cls.getClassLoader();

        // 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
        String clsPath = getResourceUrl(cls);
        java.net.URL url = loader.getResource(clsPath);

        // 从URL对象中获取路径信息

        String realPath = url.getPath();
        // 去掉路径信息中的协议名"file:"
        int pos = realPath.indexOf("file:");
        if (pos > -1) realPath = realPath.substring(pos + 5);

        // 去掉路径信息最后包含类文件信息的部分，得到类所在的路径
        pos = realPath.indexOf(clsPath);
        realPath = realPath.substring(0, pos - 1);

        // 如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
        if (realPath.endsWith("!")) realPath = realPath.substring(0, realPath.lastIndexOf("/"));

        // 结果字符串可能因平台默认编码不同而不同。因此，改用 decode(String,String) 方法指定编码。
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return realPath;
    }

    private static String getResourceUrl(Class cls) {

        // 检查用户传入的参数是否为空
        if (cls == null) throw new java.lang.IllegalArgumentException("参数不能为空！");
        // 获得类的全名，包括包名
        String clsName = cls.getName() + ".class";
        // 获得传入参数所在的包
        Package pack = cls.getPackage();
        String path = "";
        // 如果不是匿名包，将包名转化为路径
        if (pack != null) {
            String packName = pack.getName();
            // 此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库
            if (packName.startsWith("java.") || packName.startsWith("javax.")) throw new java.lang.IllegalArgumentException("不要传送系统类！");
            // 在类的名称中，去掉包名的部分，获得类的文件名
            clsName = clsName.substring(packName.length() + 1);
            // 判定包名是否是简单包名，如果是，则直接将包名转换为路径，
            if (packName.indexOf(".") < 0) path = packName + "/";
            else {// 否则按照包名的组成部分，将包名转换为路径
                int start = 0, end = 0;
                end = packName.indexOf(".");
                while (end != -1) {
                    path = path + packName.substring(start, end) + "/";
                    start = end + 1;
                    end = packName.indexOf(".", start);
                }
                path = path + packName.substring(start) + "/";
            }
        }
        // System.out.println("path + clsName =" + path + clsName);
        // 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
        return path + clsName;
    }

    public static InputStream getClassResource(Class cls) {

        try {
            ClassLoader loader = cls.getClassLoader();

            return loader.getResource(getResourceUrl(cls)).openStream();
        }
        catch (IOException e) {
            throw BytterException.unchecked(e);
        }
    }
 

    public static URL getResource(String resourceName, Class<?> callingClass) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (url == null) {
            url = ClassLoaderUtil.class.getClassLoader().getResource(resourceName);
        }
        if (url == null) {
            ClassLoader cl = callingClass.getClassLoader();
            if (cl != null) {
                url = cl.getResource(resourceName);
            }
        }
        if ((url == null) && (resourceName != null) && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
            return getResource('/' + resourceName, callingClass);
        }
        if (url != null) {
            logger.info("配置文件路径为= " + url.getPath());
        }
        return url;
    }

    public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = getResource(resourceName, callingClass);
        try {
            return (url != null) ? url.openStream() : null;
        }
        catch (IOException e) {
            logger.error("配置文件" + resourceName + "没有找到! ", e);
            return null;
        }
    }

}

package com.betterjr.common.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.exception.BytterClassNotFoundException;

/**
 * 获取Package下的Class
 * 
 * @author zhoucy
 *
 */
public class BetterClassUtils {
    private static Logger logger = LoggerFactory.getLogger(BetterClassUtils.class);

    public static void main(String[] args) {
        // 标识是否要遍历该包路径下子包的类名
        // boolean recursive = false;
        boolean recursive = true;
        // 指定的包名
        // String pkg = "com.betterjr.modules.rule.service";
        String pkg = "org.hibernate";
        List list = null;
        list = getClassList(pkg, recursive, null);

        // list = getClassList(pkg, recursive, null);

        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ":" + list.get(i));
        }
    }

    /**
     * 根据定义路径查找需要的Class
     * 
     * @param anClassName
     *            需要的Class
     * @param anPackgeArr
     *            定义路径列表
     * @return
     */
    public static Class findClassByName(String anClassName, List<String> anPackgeList) {
        if (BetterStringUtils.isBlank(anClassName)) {

            return null;
        }
        for (String tmpClassPath : anPackgeList) {
            if (BetterStringUtils.isNotBlank(tmpClassPath)) {
                tmpClassPath = tmpClassPath.trim().concat(".").concat(anClassName.trim());
            }
            else{
                tmpClassPath = anClassName;
            }
            try {
                if (tmpClassPath.indexOf("Map") > 0){
                    System.out.println(tmpClassPath);
                }
                Class cc = Class.forName(tmpClassPath);
                return cc;
            }
            catch (ClassNotFoundException ex) {

            }
        }
        throw new BytterClassNotFoundException("declare class " + anClassName + ", not fund in declare Packages");
    }

    public static List<Class<?>> getClassList(String pkgName, boolean isRecursive, Class<? extends Annotation> annotation) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            // 按文件的形式去查找
            String strFile = pkgName.replaceAll("\\.", "/");
            Enumeration<URL> urls = loader.getResources(strFile);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    String pkgPath = url.getPath();
                    logger.debug("protocol:" + protocol + " path:" + pkgPath);
                    if ("file".equals(protocol)) {
                        // 本地自己可见的代码
                        findClassName(classList, pkgName, pkgPath, isRecursive, annotation);
                    }
                    else if ("jar".equals(protocol)) {
                        // 引用第三方jar的代码
                        findClassName(classList, pkgName, url, isRecursive, annotation);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return classList;
    }

    public static void findClassName(List<Class<?>> clazzList, String pkgName, String pkgPath, boolean isRecursive,
            Class<? extends Annotation> annotation) {
        if (clazzList == null) {
            return;
        }
        File[] files = filterClassFiles(pkgPath);// 过滤出.class文件及文件夹
        logger.debug("files:" + ((files == null) ? "null" : "length=" + files.length));
        if (files != null) {
            for (File f : files) {
                String fileName = f.getName();
                if (f.isFile()) {
                    // .class 文件的情况
                    String clazzName = getClassName(pkgName, fileName);
                    addClassName(clazzList, clazzName, annotation);
                }
                else {
                    // 文件夹的情况
                    if (isRecursive) {
                        // 需要继续查找该文件夹/包名下的类
                        String subPkgName = pkgName + "." + fileName;
                        String subPkgPath = pkgPath + "/" + fileName;
                        findClassName(clazzList, subPkgName, subPkgPath, true, annotation);
                    }
                }
            }
        }
    }

    /**
     * 第三方Jar类库的引用。<br/>
     * 
     * @throws IOException
     * */
    public static void findClassName(List<Class<?>> clazzList, String pkgName, URL url, boolean isRecursive, Class<? extends Annotation> annotation)
            throws IOException {
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        logger.debug("jarFile:" + jarFile.getName());
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName(); //
            String clazzName = jarEntryName.replace("/", ".");
            int endIndex = clazzName.lastIndexOf(".");
            String prefix = null;
            if (endIndex > 0) {
            	clazzName = clazzName.substring(0, endIndex);
                endIndex = clazzName.lastIndexOf(".");
                if (endIndex > 0) {
                    prefix = clazzName.substring(0, endIndex);
                }
            }
            if (prefix != null && jarEntryName.endsWith(".class")) {
                // System.out.println("prefix:" + prefix +" pkgName:" + pkgName);
                if (prefix.equals(pkgName)) {
                    logger.debug("jar entryName:" + jarEntryName+" ; className:"+clazzName);
                    addClassName(clazzList, clazzName, annotation);
                }
                else if (isRecursive && prefix.startsWith(pkgName)) {
                    // 遍历子包名：子类
                    logger.debug("jar entryName:" + jarEntryName + " isRecursive:" + isRecursive);
                    addClassName(clazzList, clazzName, annotation);
                }
            }
        }
    }

    private static File[] filterClassFiles(String pkgPath) {
        if (pkgPath == null) {
            return null;
        }
        // 接收 .class 文件 或 类文件夹
        return new File(pkgPath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
    }

    private static String getClassName(String pkgName, String fileName) {
        int endIndex = fileName.lastIndexOf(".");
        String clazz = null;
        if (endIndex >= 0) {
            clazz = fileName.substring(0, endIndex);
        }
        String clazzName = null;
        if (clazz != null) {
            clazzName = pkgName + "." + clazz;
        }
        return clazzName;
    }

    private static void addClassName(List<Class<?>> clazzList, String clazzName, Class<? extends Annotation> annotation) {
        if (clazzList != null && clazzName != null) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(clazzName);
            }
            catch (ClassNotFoundException e) {
                logger.error("Has error :" + clazzName, e);
                // e.printStackTrace();
            }
            catch (Exception e) {
                logger.error("Has error :" + clazzName, e);
            }
            catch (Error e) {
                logger.error("Has error :" + clazzName, e);
            }
            if (clazz != null) {
                if (annotation == null) {
                    clazzList.add(clazz);
                }
                else if (clazz.isAnnotationPresent(annotation)) {
                    clazzList.add(clazz);
                }
            }
        }
    }
}
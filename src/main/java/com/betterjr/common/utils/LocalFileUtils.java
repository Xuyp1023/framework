/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.betterjr.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.config.ParamNames;
import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.exception.BettjerIOException;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.modules.sys.service.SysConfigService;

/**
 * 文件操作工具类 实现文件的创建、删除、复制、压缩、解压以及目录的创建、删除、复制、压缩解压等功能
 * 
 * @author ThinkGem
 * @version 2013-06-21
 */
public class LocalFileUtils extends org.apache.commons.io.FileUtils {

    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static long ONE_KB = 1024;
    public static long ONE_MB = ONE_KB * 1024;
    public static long ONE_GB = ONE_MB * 1024;
    public static long ONE_TB = ONE_GB * 1024;
    public static long ONE_PB = ONE_TB * 1024;

    public static Set<String> SupportedUploadFileType = new HashSet<String>();
    static {
        SupportedUploadFileType.add("jpg");
        SupportedUploadFileType.add("jpeg");
        SupportedUploadFileType.add("png");
        SupportedUploadFileType.add("gif");
        SupportedUploadFileType.add("doc");
        SupportedUploadFileType.add("docx");
        SupportedUploadFileType.add("pdf");
        SupportedUploadFileType.add("xls");
        SupportedUploadFileType.add("xlsx");
        SupportedUploadFileType.add("zip");
        SupportedUploadFileType.add("rar");
    }

    public static boolean isSupportedUploadFileType(String type) {
        if (StringUtils.isBlank(type)) {
            return false;
        }
        return SupportedUploadFileType.contains(type.trim().toLowerCase());
    }

    public static String getHumanReadableFileSize(Long fileSize) {
        if (fileSize == null) return null;
        return getHumanReadableFileSize(fileSize.longValue());
    }

    public static String getHumanReadableFileSize(long fileSize) {
        if (fileSize < 0) {
            return String.valueOf(fileSize);
        }
        String result = getHumanReadableFileSize(fileSize, ONE_PB, "PB");
        if (result != null) {
            return result;
        }

        result = getHumanReadableFileSize(fileSize, ONE_TB, "TB");
        if (result != null) {
            return result;
        }
        result = getHumanReadableFileSize(fileSize, ONE_GB, "GB");
        if (result != null) {
            return result;
        }
        result = getHumanReadableFileSize(fileSize, ONE_MB, "MB");
        if (result != null) {
            return result;
        }
        result = getHumanReadableFileSize(fileSize, ONE_KB, "KB");
        if (result != null) {
            return result;
        }
        return String.valueOf(fileSize) + "B";
    }

    private static String getHumanReadableFileSize(long fileSize, long unit, String unitName) {
        if (fileSize == 0) return "0";

        if (fileSize / unit >= 1) {
            double value = fileSize / (double) unit;
            DecimalFormat df = new DecimalFormat("######.##" + unitName);
            return df.format(value);
        }
        return null;
    }

    /**
     * 复制单个文件，如果目标文件存在，则不覆盖
     * 
     * @param srcFileName
     *            待复制的文件名
     * @param descFileName
     *            目标文件名
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String descFileName) {
        return LocalFileUtils.copyFileCover(srcFileName, descFileName, false);
    }

    /**
     * 复制单个文件
     * 
     * @param srcFileName
     *            待复制的文件名
     * @param descFileName
     *            目标文件名
     * @param coverlay
     *            如果目标文件已存在，是否覆盖
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFileCover(String srcFileName, String descFileName, boolean coverlay) {
        File srcFile = new File(srcFileName);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
            return false;
        }
        // 判断源文件是否是合法的文件
        else if (!srcFile.isFile()) {
            log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
            return false;
        }
        File descFile = new File(descFileName);
        // 判断目标文件是否存在
        if (descFile.exists()) {
            // 如果目标文件存在，并且允许覆盖
            if (coverlay) {
                log.debug("目标文件已存在，准备删除!");
                if (!LocalFileUtils.delFile(descFileName)) {
                    log.debug("删除目标文件 " + descFileName + " 失败!");
                    return false;
                }
            } else {
                log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
                return false;
            }
        } else {
            if (!descFile.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建目录
                log.debug("目标文件所在的目录不存在，创建目录!");
                // 创建目标文件所在的目录
                if (!descFile.getParentFile().mkdirs()) {
                    log.debug("创建目标文件所在的目录失败!");
                    return false;
                }
            }
        }

        // 准备复制文件
        // 读取的位数
        int readByte = 0;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            // 打开源文件
            ins = new FileInputStream(srcFile);
            // 打开目标文件的输出流
            outs = new FileOutputStream(descFile);
            byte[] buf = new byte[1024];
            // 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
            while ((readByte = ins.read(buf)) != -1) {
                // 将读取的字节流写入到输出流
                outs.write(buf, 0, readByte);
            }
            log.debug("复制单个文件 " + srcFileName + " 到" + descFileName + "成功!");
            return true;
        }
        catch (Exception e) {
            log.debug("复制文件失败：" + e.getMessage());
            return false;
        }
        finally {
            // 关闭输入输出流，首先关闭输出流，然后再关闭输入流
            if (outs != null) {
                try {
                    outs.close();
                }
                catch (IOException oute) {
                    oute.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                }
                catch (IOException ine) {
                    ine.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制整个目录的内容，如果目标目录存在，则不覆盖
     * 
     * @param srcDirName
     *            源目录名
     * @param descDirName
     *            目标目录名
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String descDirName) {
        return LocalFileUtils.copyDirectoryCover(srcDirName, descDirName, false);
    }

    /**
     * 复制整个目录的内容
     * 
     * @param srcDirName
     *            源目录名
     * @param descDirName
     *            目标目录名
     * @param coverlay
     *            如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay) {
        File srcDir = new File(srcDirName);
        // 判断源目录是否存在
        if (!srcDir.exists()) {
            log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
            return false;
        }
        // 判断源目录是否是目录
        else if (!srcDir.isDirectory()) {
            log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
            return false;
        }
        // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        // 如果目标文件夹存在
        if (descDir.exists()) {
            if (coverlay) {
                // 允许覆盖目标目录
                log.debug("目标目录已存在，准备删除!");
                if (!LocalFileUtils.delFile(descDirNames)) {
                    log.debug("删除目录 " + descDirNames + " 失败!");
                    return false;
                }
            } else {
                log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
                return false;
            }
        } else {
            // 创建目标目录
            log.debug("目标目录不存在，准备创建!");
            if (!descDir.mkdirs()) {
                log.debug("创建目标目录失败!");
                return false;
            }

        }

        boolean flag = true;
        // 列出源目录下的所有文件名和子目录名
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 如果是一个单个文件，则直接复制
            if (files[i].isFile()) {
                flag = LocalFileUtils.copyFile(files[i].getAbsolutePath(), descDirName + files[i].getName());
                // 如果拷贝文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 如果是子目录，则继续复制目录
            if (files[i].isDirectory()) {
                flag = LocalFileUtils.copyDirectory(files[i].getAbsolutePath(), descDirName + files[i].getName());
                // 如果拷贝目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
            return false;
        }
        log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
        return true;

    }

    /**
     * 
     * 删除文件，可以删除单个文件或文件夹
     * 
     * @param fileName
     *            被删除的文件名
     * @return 如果删除成功，则返回true，否是返回false
     */
    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.debug(fileName + " 文件不存在!");
            return true;
        } else {
            if (file.isFile()) {
                return LocalFileUtils.deleteFile(fileName);
            } else {
                return LocalFileUtils.deleteDirectory(fileName);
            }
        }
    }

    /**
     * 
     * 删除单个文件
     * 
     * @param fileName
     *            被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.debug("删除文件 " + fileName + " 成功!");
                return true;
            } else {
                log.debug("删除文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            log.debug(fileName + " 文件不存在!");
            return true;
        }
    }

    /**
     * 
     * 删除目录及目录下的文件
     * 
     * @param dirName
     *            被删除的目录所在的文件路径
     * @return 如果目录删除成功，则返回true，否则返回false
     */
    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            log.debug(dirNames + " 目录不存在!");
            return true;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = LocalFileUtils.deleteFile(files[i].getAbsolutePath());
                // 如果删除文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = LocalFileUtils.deleteDirectory(files[i].getAbsolutePath());
                // 如果删除子目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("删除目录失败!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            log.debug("删除目录 " + dirName + " 成功!");
            return true;
        } else {
            log.debug("删除目录 " + dirName + " 失败!");
            return false;
        }

    }

    /**
     * 创建单个文件
     * 
     * @param descFileName
     *            文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            log.debug("文件 " + descFileName + " 已存在!");
            return false;
        }
        if (descFileName.endsWith(File.separator)) {
            log.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                log.debug("创建文件所在的目录失败!");
                return false;
            }
        }

        // 创建文件
        try {
            if (file.createNewFile()) {
                log.debug(descFileName + " 文件创建成功!");
                return true;
            } else {
                log.debug(descFileName + " 文件创建失败!");
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.debug(descFileName + " 文件创建失败!");
            return false;
        }

    }

    /**
     * 创建目录
     * 
     * @param descDirName
     *            目录名,包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            log.debug("目录 " + descDirNames + " 已存在!");
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            log.debug("目录 " + descDirNames + " 创建成功!");
            return true;
        } else {
            log.debug("目录 " + descDirNames + " 创建失败!");
            return false;
        }

    }

    /**
     * 写入文件
     * 
     * @param file
     *            要写入的文件
     */
    public static void writeToFile(String fileName, String content, boolean append) {
        try {
            org.apache.commons.io.FileUtils.write(new File(fileName), content, "utf-8", append);
            log.debug("文件 " + fileName + " 写入成功!");
        }
        catch (IOException e) {
            log.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
        }
    }

    /**
     * 写入文件
     * 
     * @param file
     *            要写入的文件
     */
    public static void writeToFile(String fileName, String content, String encoding, boolean append) {
        try {
            org.apache.commons.io.FileUtils.write(new File(fileName), content, encoding, append);
            log.debug("文件 " + fileName + " 写入成功!");
        }
        catch (IOException e) {
            log.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
        }
    }

    /**
     * 压缩文件或目录
     * 
     * @param srcDirName
     *            压缩的根目录
     * @param fileName
     *            根目录下的待压缩的文件名或文件夹名，其中*或""表示跟目录下的全部文件
     * @param descFileName
     *            目标zip文件
     */
    public static void zipFiles(String srcDirName, String fileName, String descFileName) {
        // 判断目录是否存在
        if (srcDirName == null) {
            log.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
            return;
        }
        File fileDir = new File(srcDirName);
        if (!fileDir.exists() || !fileDir.isDirectory()) {
            log.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
            return;
        }
        String dirPath = fileDir.getAbsolutePath();
        File descFile = new File(descFileName);
        try {
            ZipOutputStream zouts = new ZipOutputStream(new FileOutputStream(descFile));
            if ("*".equals(fileName) || "".equals(fileName)) {
                LocalFileUtils.zipDirectoryToZipFile(dirPath, fileDir, zouts);
            } else {
                File file = new File(fileDir, fileName);
                if (file.isFile()) {
                    LocalFileUtils.zipFilesToZipFile(dirPath, file, zouts);
                } else {
                    LocalFileUtils.zipDirectoryToZipFile(dirPath, file, zouts);
                }
            }
            zouts.close();
            log.debug(descFileName + " 文件压缩成功!");
        }
        catch (Exception e) {
            log.debug("文件压缩失败：" + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到descFileName目录下
     * 
     * @param zipFileName
     *            需要解压的ZIP文件
     * @param descFileName
     *            目标文件
     */
    public static List<File> unZipFiles(File zipFileName, String descFileName) {
        String descFileNames = descFileName;
        if (!descFileNames.endsWith(File.separator)) {
            descFileNames = descFileNames + File.separator;
        }
        try {
            // 根据ZIP文件创建ZipFile对象
            ZipFile zipFile = new ZipFile(zipFileName);
            ZipEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4096];
            int readByte = 0;
            // 获取ZIP文件里所有的entry
            @SuppressWarnings("rawtypes")
            Enumeration enums = zipFile.entries();
            // 遍历所有entry
            List<File> data = new ArrayList<File>();
            while (enums.hasMoreElements()) {
                entry = (ZipEntry) enums.nextElement();
                // 获得entry的名字
                entryName = entry.getName();
                descFileDir = descFileNames + entryName;
                if (entry.isDirectory()) {
                    // 如果entry是一个目录，则创建目录
                    new File(descFileDir).mkdirs();
                    continue;
                } else {
                    // 如果entry是一个文件，则创建父目录
                    new File(descFileDir).getParentFile().mkdirs();
                }
                File file = new File(descFileDir);
                copyInputStreamToFile(zipFile.getInputStream(entry), file);
                data.add(file);
            }
            zipFile.close();
            log.debug("文件解压成功!");
            return data;
        }
        catch (Exception e) {
            log.warn("文件解压失败：" + e.getMessage());
            throw new BettjerIOException(12313, "文件解压失败：" + zipFileName, e);
        }
    }

    /**
     * 将目录压缩到ZIP输出流
     * 
     * @param dirPath
     *            目录路径
     * @param fileDir
     *            文件信息
     * @param zouts
     *            输出流
     */
    public static void zipDirectoryToZipFile(String dirPath, File fileDir, ZipOutputStream zouts) {
        if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            // 空的文件夹
            if (files.length == 0) {
                // 目录信息
                ZipEntry entry = new ZipEntry(getEntryName(dirPath, fileDir));
                try {
                    zouts.putNextEntry(entry);
                    zouts.closeEntry();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    // 如果是文件，则调用文件压缩方法
                    LocalFileUtils.zipFilesToZipFile(dirPath, files[i], zouts);
                } else {
                    // 如果是目录，则递归调用
                    LocalFileUtils.zipDirectoryToZipFile(dirPath, files[i], zouts);
                }
            }

        }

    }

    /**
     * 将文件压缩到ZIP输出流
     * 
     * @param dirPath
     *            目录路径
     * @param file
     *            文件
     * @param zouts
     *            输出流
     */
    public static void zipFilesToZipFile(String dirPath, File file, ZipOutputStream zouts) {
        FileInputStream fin = null;
        ZipEntry entry = null;
        // 创建复制缓冲区
        byte[] buf = new byte[4096];
        int readByte = 0;
        if (file.isFile()) {
            try {
                // 创建一个文件输入流
                fin = new FileInputStream(file);
                // 创建一个ZipEntry
                entry = new ZipEntry(getEntryName(dirPath, file));
                // 存储信息到压缩文件
                zouts.putNextEntry(entry);
                // 复制字节到压缩文件
                while ((readByte = fin.read(buf)) != -1) {
                    zouts.write(buf, 0, readByte);
                }
                zouts.closeEntry();
                fin.close();
                System.out.println("添加文件 " + file.getAbsolutePath() + " 到zip文件中!");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取待压缩文件在ZIP文件中entry的名字，即相对于跟目录的相对路径名
     * 
     * @param dirPat
     *            目录名
     * @param file
     *            entry文件名
     * @return
     */
    private static String getEntryName(String dirPath, File file) {
        String dirPaths = dirPath;
        if (!dirPaths.endsWith(File.separator)) {
            dirPaths = dirPaths + File.separator;
        }
        String filePath = file.getAbsolutePath();
        // 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储
        if (file.isDirectory()) {
            filePath += "/";
        }
        int index = filePath.indexOf(dirPaths);

        return filePath.substring(index + dirPaths.length());
    }

    /**
     * 修复路径，将 \\ 或 / 等替换为 File.separator
     * 
     * @param path
     * @return
     */
    public static String path(String path) {
        String p = StringUtils.replace(path, "\\", "/");
        p = StringUtils.join(StringUtils.split(p, "/"), "/");
        if (!StringUtils.startsWithAny(p, "/") && StringUtils.startsWithAny(path, "\\", "/")) {
            p += "/";
        }
        if (!StringUtils.endsWithAny(p, "/") && StringUtils.endsWithAny(path, "\\", "/")) {
            p = p + "/";
        }
        return p;
    }

    /**
     * 解压GZ压缩文件，如果扩展名是GZ结束，则文件名去掉.GZ；如果不是GZ结束，则文件名字相同。
     * 
     * @param anFile
     * @param anDirPath
     * @return
     */
    public static File unGzipFile(File anFile, String anDirPath) {
        if (anDirPath.endsWith(File.separator) == false) {
            anDirPath = anDirPath + File.separator;
        }
        File destFile;
        if (anFile.getName().endsWith(".gz")) {
            destFile = new File(anDirPath + anFile.getName().substring(0, anFile.getName().length() - 3));
        } else {
            destFile = new File(anDirPath + anFile.getName());
        }
        GZIPInputStream gzin = null;
        try {
            gzin = new GZIPInputStream(new FileInputStream(anFile));
            copyInputStreamToFile(gzin, destFile);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(gzin);
        }
        return destFile;
    }

    /**
     * 替换文件的扩展名
     * 
     * @param anFile
     * @param anExt
     * @return
     */
    public static File replaceFileExt(File anFile, String anExt) {
        String tmpStr = anFile.getName();
        int pos = tmpStr.lastIndexOf(".");
        if (pos > 0) {
            tmpStr = tmpStr.substring(0, pos);
        }
        tmpStr = tmpStr.concat(".").concat(anExt);

        return new File(anFile.getParent().concat(File.separator).concat(tmpStr));
    }

    public static String removeFileExt(String anFile) {
        int pos = anFile.lastIndexOf(".");
        if (pos > 0) {
            return anFile.substring(0, pos);
        } else {
            return anFile;
        }
    }

    public static String fileUploadRelativePath(File anFile) {
        String abPath = SysConfigService.getString(ParamNames.OPENACCO_FILE_DOWNLOAD_PATH);
        String tmpPath = anFile.getAbsolutePath();
        if (tmpPath.length() > abPath.length()) {
            return tmpPath.substring(abPath.length());
        } else {
            return tmpPath;
        }
    }

    /**
     * 根据默认信息，获得上传的文件路径
     * 
     * @return
     */
    public static KeyAndValueObject findUploadFilePath() {

        return findFilePathWithParent(null);
    }

    /**
     * 根据指定的父文件路径，创建文件路径
     * 
     * @param anParentPath
     *            指定的父文件路径
     * @return
     */
    public static KeyAndValueObject findFilePathWithParent(String anParentPath) {
        // 得到上传服务器的路径
        String basePath = (String) SysConfigService.getObject(ParamNames.OPENACCO_FILE_DOWNLOAD_PATH);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String format = formatter.format(new Date());
        String workPath = null;
        if (StringUtils.isBlank(anParentPath)) {
            workPath = basePath + "/" + format;
        } else {
            workPath = basePath + "/" + anParentPath + "/" + format;
        }
        File file = new File(workPath);

        // 如果文件夹不存在则创建
        if (file.exists()) {

            // 如果是文件，则将文件做重命名
            if (file.isFile()) {
                file.renameTo(new File(workPath + ".rename"));
                file.mkdirs();
            }
        } else {
            // 不存在，就创将目录
            file.mkdirs();
        }
        // 得到上传的文件的文件名
        String fileName = SerialGenerator.uuid();
        String fileAbsoPath = workPath + "/" + fileName;

        // 保存在系统中的相对路径
        String saveUrl = null;
        if (StringUtils.isNotBlank(anParentPath)) {
            saveUrl = "/" + anParentPath;
        } else {
            saveUrl = "";
        }
        saveUrl = saveUrl + "/" + format + "/" + fileName;

        return new KeyAndValueObject(saveUrl, new File(fileAbsoPath));
    }

    /**
     * 根据指定的父文件路径，创建文件路径
     * 
     * @param anParentPath
     *            指定的父文件路径
     * @return
     */
    public static KeyAndValueObject findFilePathWithParent(String anParentPath, String basePath) {
        // 得到上传服务器的路径
        // String basePath = (String) SysConfigService.getObject(ParamNames.OPENACCO_FILE_DOWNLOAD_PATH);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String format = formatter.format(new Date());
        String workPath = null;
        if (StringUtils.isBlank(anParentPath)) {
            workPath = basePath + "/" + format;
        } else {
            workPath = basePath + "/" + anParentPath + "/" + format;
        }
        File file = new File(workPath);

        // 如果文件夹不存在则创建
        if (file.exists()) {

            // 如果是文件，则将文件做重命名
            if (file.isFile()) {
                file.renameTo(new File(workPath + ".rename"));
                file.mkdirs();
            }
        } else {
            // 不存在，就创将目录
            file.mkdirs();
        }
        // 得到上传的文件的文件名
        String fileName = UUIDUtils.uuid();
        String fileAbsoPath = workPath + "/" + fileName;

        // 保存在系统中的相对路径
        String saveUrl = null;
        if (StringUtils.isNotBlank(anParentPath)) {
            saveUrl = "/" + anParentPath;
        } else {
            saveUrl = "";
        }
        saveUrl = saveUrl + "/" + format + "/" + fileName;

        return new KeyAndValueObject(saveUrl, new File(fileAbsoPath));
    }

    /**
     * 获得文件的扩展名
     * @param anFileName
     * @return
     */
    public static String extractFileExt(String anFileName) {
        if (StringUtils.isNotBlank(anFileName)) {
            if (anFileName.contains(".")) {
                return anFileName.substring(anFileName.lastIndexOf(".") + 1);
            }
        }
        return "bin";
    }

    public static File getRealFile(String anBasePath) {
        String tmpStr = fileRealPath(anBasePath);
        if (tmpStr != null) {
            File ff = new File(tmpStr);
            if (ff.exists() && ff.isFile()) {
                System.out.println(tmpStr);
                return ff;
            }
        }
        return null;
    }

    public static String fileRealPath(String anBasePath) {
        if (StringUtils.isNotBlank(anBasePath)) {
            return anBasePath.replaceAll("\\.\\.", "");
        } else {
            return null;
        }
    }

    /**
     * Returns the byte [] content of the specified file.
     *
     * @param file
     * @return the byte content of the file
     */
    public static byte[] readContent(File file) {
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            is.read(buffer, 0, buffer.length);
        }
        catch (Throwable t) {
            System.err.println("Failed to read byte content of " + file.getAbsolutePath());
            t.printStackTrace();
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException ioe) {
                    System.err.println("Failed to close file " + file.getAbsolutePath());
                    ioe.printStackTrace();
                }
            }
        }
        return buffer;
    }

    /**
     * Returns the string content of the specified file.
     *
     * @param file
     * @param lineEnding
     * @return the string content of the file
     */
    public static String readContent(File file, String lineEnding) {
        StringBuilder sb = new StringBuilder();
        InputStreamReader is = null;
        BufferedReader reader = null;
        try {
            is = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                if (lineEnding != null) {
                    sb.append(lineEnding);
                }
            }
        }
        catch (Throwable t) {
            System.err.println("Failed to read content of " + file.getAbsolutePath());
            t.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    System.err.println("Failed to close file " + file.getAbsolutePath());
                    ioe.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException ioe) {
                    System.err.println("Failed to close file " + file.getAbsolutePath());
                    ioe.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
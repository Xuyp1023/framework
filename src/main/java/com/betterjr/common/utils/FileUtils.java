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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.config.ParamNames;
import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.exception.BettjerIOException;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.modules.sys.service.SysConfigService;

/**
 * 文件操作工具类 实现oss文件的创建、删除、复制，读取
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static long ONE_KB = 1024;
    public static long ONE_MB = ONE_KB * 1024;
    public static long ONE_GB = ONE_MB * 1024;
    public static long ONE_TB = ONE_GB * (long) 1024;
    public static long ONE_PB = ONE_TB * (long) 1024;
    
    public static Set<String> SupportedUploadFileType=new HashSet<String>();
    static{
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
        SupportedUploadFileType.add("ftl");
    }
    
    public static boolean isSupportedUploadFileType(String type){
        if(BetterStringUtils.isBlank(type)){
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
    
    
    public static File getRealFile(String anPath) {
        String tmpStr = null;
        if (BetterStringUtils.isNotBlank(anPath)) {
            tmpStr= anPath.replaceAll("\\.\\.", "");
        }
        if (tmpStr != null) {
            File ff = new File(tmpStr);
            if (ff.exists() && ff.isFile()) {
                System.out.println(tmpStr);
                return ff;
            }
        }
        return null;
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

        return findFilePathWithParent(anParentPath,basePath);
    }
    
    /**
     * 根据指定的父文件路径，创建文件路径
     * 
     * @param anParentPath
     *            指定的父文件路径
     * @return
     */
    public static KeyAndValueObject findFilePathWithParent(String anParentPath,String basePath) {
        // 得到上传服务器的路径
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String format = formatter.format(new Date());
        String workPath = null;
        if (BetterStringUtils.isBlank(anParentPath)) {
            workPath = basePath + "/" + format;
        }
        else {
            workPath = basePath + "/" + anParentPath + "/" + format;
        }
        File file = new File(workPath);

        // 如果文件夹不存在则创建
        if (file.exists() ) {
            
            //如果是文件，则将文件做重命名
            if ( file.isFile()){
                file.renameTo(new File(workPath+".rename"));
                file.mkdirs();
            }
        }
        else{
            //不存在，就创将目录
            file.mkdirs(); 
        }
        // 得到上传的文件的文件名
        String fileName = UUIDUtils.uuid();
        String fileAbsoPath  = workPath + "/" + fileName;
        
        //保存在系统中的相对路径
        String saveUrl = null;
        if (BetterStringUtils.isNotBlank(anParentPath)){
            saveUrl ="/" + anParentPath; 
        }
        else{
            saveUrl ="";
        }
        saveUrl = saveUrl + "/" + format + "/" + fileName;

        return new KeyAndValueObject(saveUrl, new File(fileAbsoPath));
    }
    
    /**
     * 获得文件的扩展名
     * @param anFileName
     * @return
     */
    public static String extractFileExt(String anFileName){
        if (BetterStringUtils.isNotBlank(anFileName)){
           if (anFileName.contains(".")){
               return anFileName.substring(anFileName.lastIndexOf(".") + 1);
           }
        }
        return "bin";
    }

    /**
     * Returns the byte [] content of the specified file.
     *
     * @param file
     * @return the byte content of the file
     */
    public static byte [] readContent(File file) {
        byte [] buffer = new byte[(int) file.length()];
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            is.read(buffer,  0,  buffer.length);
        } catch (Throwable t) {
            System.err.println("Failed to read byte content of " + file.getAbsolutePath());
            t.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
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
        } catch (Throwable t) {
            System.err.println("Failed to read content of " + file.getAbsolutePath());
            t.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException ioe) {
                    System.err.println("Failed to close file " + file.getAbsolutePath());
                    ioe.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                    System.err.println("Failed to close file " + file.getAbsolutePath());
                    ioe.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
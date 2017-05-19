package com.betterjr.modules.document.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.betterjr.common.config.ParamNames;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.document.IAgencyAuthFileGroupService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.sys.service.SysConfigService;

/**
 * 操作系统层面的文件管理，实现文件处理抽象类
 * 
 * @author zhoucy
 *
 */
public class FileSystemFileManager implements FileManager {
    private IAgencyAuthFileGroupService fileGroupService;
    
    
    public FileSystemFileManager(IAgencyAuthFileGroupService anFileGroupService){
        this.fileGroupService = anFileGroupService;
    }
    
    @Override
    public boolean save(String anFilePath, InputStream anIn) {
        final String absPath = fileGroupService.findAbsFilePath(anFilePath);
        File tmpFile = new File(absPath);
        File tmpPath = tmpFile.getParentFile();
        if (tmpPath.exists() == false){
            tmpPath.mkdirs();
        }
        
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(tmpFile);
            IOUtils.copy(anIn, outStream);
            return true;
        }
        catch (IOException ex) {
            return false;
        }
        finally {
            IOUtils.closeQuietly(outStream);
        }
    }

    @Override
    public InputStream load(String anFilePath) {
        final String absPath = fileGroupService.findAbsFilePath(anFilePath);
        File tmpFile = new File(absPath);
        if (tmpFile.exists() && tmpFile.isFile()) {
            try {
                return new FileInputStream(tmpFile);
            }
            catch (FileNotFoundException e) {
            }
        }
        return null;
    }

    @Override
    public boolean exists(String anFilePath) {
        if (BetterStringUtils.isBlank(anFilePath)) {
            return false;
        }
        final String absPath = fileGroupService.findAbsFilePath(anFilePath);
        File tmpFile = new File(absPath);
        return tmpFile.exists() && tmpFile.isFile();
    }

    @Override
    public long findSize(String anFilePath) {
        final String absPath = fileGroupService.findAbsFilePath(anFilePath);
        File tmpFile = new File(absPath);
        if (tmpFile.exists() && tmpFile.isFile()){
            return tmpFile.length();
        }
        return -1;
    }
}

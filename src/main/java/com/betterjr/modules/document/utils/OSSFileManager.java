package com.betterjr.modules.document.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.SimplifiedObjectMeta;
import com.betterjr.common.config.ParamNames;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.Cryptos;
import com.betterjr.modules.document.IAgencyAuthFileGroupService;
import com.betterjr.modules.document.data.OSSConfigInfo;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.sys.service.SysConfigService;

public class OSSFileManager implements FileManager {
    private static OSSConfigInfo configInfo = null;
    private static OSSClient ossClient = null;
    private static long lastAccess = System.currentTimeMillis();
    private IAgencyAuthFileGroupService fileGroupService;

    public OSSFileManager(IAgencyAuthFileGroupService anFileGroupService) {

        this.fileGroupService = anFileGroupService;
    }

    @Override
    public boolean save(String anFilePath, InputStream anIn) {
        initConfig();
        try {
            ossClient.putObject(new PutObjectRequest(configInfo.getBucketName(), anFilePath, anIn));
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    @Override
    public InputStream load(String anFilePath) {
        initConfig();
        OSSObject object = ossClient.getObject(configInfo.getBucketName(), anFilePath);
        if (object != null) {
            return object.getObjectContent();
        }
        return null;
    }

    @Override
    public boolean exists(String anFilePath) {
        initConfig();
        try {
            SimplifiedObjectMeta ccs = ossClient.getSimplifiedObjectMeta(configInfo.getBucketName(), anFilePath);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    private void initConfig() {
        if (configInfo == null) {
            configInfo = this.fileGroupService.findOSSConfigInfo();
        }
        if (checkAccessTime()) {
            try {
                ossClient.listBuckets();
            }
            catch (Exception ex) {
                ossClient = null;
            }
        }
        if (ossClient == null) {

            ossClient = new OSSClient(configInfo.getEndPoint(), configInfo.getAccessKeyId(), configInfo.getAccessKeySecret());
        }
    }

    private static boolean checkAccessTime() {
        long tmpTime = lastAccess;
        lastAccess = System.currentTimeMillis();
        return (lastAccess - tmpTime) > 10 * 60 * 1000;
    }

    @Override
    public long findSize(String anFilePath) {
        initConfig();
        try {
            SimplifiedObjectMeta ccs = ossClient.getSimplifiedObjectMeta(configInfo.getBucketName(), anFilePath);

            return ccs.getSize();
        }
        catch (Exception ex) {
            
        }
        return -1;
    }
}

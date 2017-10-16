package com.betterjr.modules.document.utils;

import java.io.InputStream;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.SimplifiedObjectMeta;
import com.betterjr.modules.document.IAgencyAuthFileGroupService;
import com.betterjr.modules.document.data.OSSConfigInfo;

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

            ossClient = new OSSClient(configInfo.getEndPoint(), configInfo.getAccessKeyId(),
                    configInfo.getAccessKeySecret());
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

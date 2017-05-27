package com.betterjr.modules.document.data;

/**
 * 阿里云OSS存储服务配置信息，数据需要加密存储
 * 
 * @author zhoucy
 *
 */
public class OSSConfigInfo implements java.io.Serializable {
    private static final long serialVersionUID = -2471884050403670457L;
    private String endPoint = "oss-cn-hzfinance.aliyuncs.com";
    private String accessKeyId = "LTAIYK9U0eAC7UvM";
    private String accessKeySecret = "MzB2XKHJfI2pSGfeol4DmwS51SFDVq";
    private String bucketName = "zcytest";

    public String getEndPoint() {
        return this.endPoint;
    }

    public void setEndPoint(String anEndPoint) {
        this.endPoint = anEndPoint;
    }

    public String getAccessKeyId() {
        return this.accessKeyId;
    }

    public void setAccessKeyId(String anAccessKeyId) {
        this.accessKeyId = anAccessKeyId;
    }

    public String getAccessKeySecret() {
        return this.accessKeySecret;
    }

    public void setAccessKeySecret(String anAccessKeySecret) {
        this.accessKeySecret = anAccessKeySecret;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String anBucketName) {
        this.bucketName = anBucketName;
    }

}

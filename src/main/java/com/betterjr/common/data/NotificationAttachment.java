package com.betterjr.common.data;

/**
 * 封装邮件发送的附件
 * @author liuwl
 *
 */
public final class NotificationAttachment {
    private String fileName;
    private String filePath;

    public NotificationAttachment(String anFileName, String anFilePath) {
        this.fileName = anFileName;
        this.filePath = anFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }
}

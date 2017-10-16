package com.betterjr.modules.document.utils;

import com.betterjr.modules.document.IAgencyAuthFileGroupService;
import com.betterjr.modules.document.data.FileStoreType;

public class FileManagerFactory {
    public static FileManager create(FileStoreType anStoreType, IAgencyAuthFileGroupService anGroupService) {
        if (FileStoreType.FILE_STORE == anStoreType) {
            return new FileSystemFileManager(anGroupService);
        } else if (FileStoreType.OSS_STORE == anStoreType) {
            return new OSSFileManager(anGroupService);
        }
        return new FileSystemFileManager(anGroupService);
    }
}

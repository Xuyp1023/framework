package com.betterjr.modules.document.data;

import org.apache.commons.lang3.StringUtils;

public enum FileStoreType {
    FILE_STORE("0"), OSS_STORE("1");
    private final String value;

    FileStoreType(String anValue) {
        this.value = anValue;
    }

    public String getValue() {
        return this.value;
    }

    public static FileStoreType checking(String anWorkType) {
        if (StringUtils.isNotBlank(anWorkType)) {
            for (FileStoreType storeType : FileStoreType.values()) {
                if (storeType.value.equals(anWorkType)) {
                    return storeType;
                }
            }
        }
        return OSS_STORE;
    }
}

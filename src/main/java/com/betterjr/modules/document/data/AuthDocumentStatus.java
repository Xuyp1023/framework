package com.betterjr.modules.document.data;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.utils.BetterStringUtils;

public enum AuthDocumentStatus {
    NOT_AUTH("0", "未认证"), AUTHED("1", "认证通过"), PRIMARY_AUTH("2", "初步认证"), AUTHING("3", "认证中"), AUTH_REQUIRE("4",
            "资料待完善"), AUTH_FAILURE("9", "审核失败");
    private final String title;
    private final String value;

    AuthDocumentStatus(String anValue, String anTitle) {
        this.value = anValue;
        this.title = anTitle;
    }

    public String getTitle() {
        return this.title;
    }

    public String getValue() {
        return this.value;
    }

    public static AuthDocumentStatus checking(String anWorkType) {
        if (StringUtils.isBlank(anWorkType)) {
            anWorkType = "X";
        }
        try {
            if (StringUtils.isNotBlank(anWorkType)) {
                for (AuthDocumentStatus statusType : AuthDocumentStatus.values()) {
                    if (statusType.value.equals(anWorkType)) {

                        return statusType;
                    }
                }
                return AuthDocumentStatus.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }

        throw new BytterValidException("not find declare AuthDocumentStatus :" + anWorkType);
    }
}

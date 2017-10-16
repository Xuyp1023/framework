package com.betterjr.modules.rule.entity;

import org.apache.commons.lang3.StringUtils;

public enum RuleFuncType {
    OBJECT, CLASS, MACRO;
    public static RuleFuncType checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return RuleFuncType.valueOf(anWorkType.trim().toUpperCase());
            } else {
                return MACRO;
            }
        }
        catch (Exception ex) {

        }
        return null;
    }

}

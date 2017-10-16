package com.betterjr.common.data;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterStringUtils;

/**
 * 
 * 
 * @author zhoucy
 *
 */
public enum AccetMethodInfo {
    COUNTER_METHOD("0"), PHONE_METHOD("1"), WEB_METHOD("2"), FAX_METHOD("3"), MOBILE_METHOD("4"), ORG_METHOD("5");
    private final String value;

    public String getValue() {
        return value;
    }

    public static AccetMethodInfo checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {
                for (AccetMethodInfo mm : AccetMethodInfo.values()) {
                    if (mm.value.equals(anWorkType)) {
                        return mm;
                    }
                }
                return AccetMethodInfo.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        return null;
    }

    AccetMethodInfo(String anValue) {
        this.value = anValue;
    }
}

package com.betterjr.common.data;

import com.betterjr.common.utils.BetterStringUtils;

public enum PlatformBusinClass {
    FUND("fund"), SCF("scf"), MONEY("money");

    private final String value;

    PlatformBusinClass(String anValue) {
        this.value = anValue;
    }

    public String getValue() {
        return this.value;
    }

    public static PlatformBusinClass checking(String anWorkType) {
        try {
            if (BetterStringUtils.isNotBlank(anWorkType)) {
                for (PlatformBusinClass mm : PlatformBusinClass.values()) {
                    if (mm.value.equalsIgnoreCase(anWorkType)) {
                        
                        return mm;
                    }
                }
                
                return PlatformBusinClass.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        return null;
    }
}

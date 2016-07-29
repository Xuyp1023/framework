package com.betterjr.common.data;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterStringUtils;

 
public enum PlatformBaseRuleType {
    NONE_USER("陌生人"), CORE_USER("核心企业"), SUPPLIER_USER("供应商"), FACTOR_USER("资金方"), SELLER_USER("经销商"), PLATFORM_USER("平台");

    private final String title;

    PlatformBaseRuleType(String anTitle) {
        this.title = anTitle;
    }

    public String getTitle() {
        return this.title;
    }
    
    public static PlatformBaseRuleType checking(String anWorkType){
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return PlatformBaseRuleType.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        return NONE_USER;
    }
    
    public static List<PlatformBaseRuleType> checkList(String anRules){
        List<PlatformBaseRuleType> result = new ArrayList();
        if (BetterStringUtils.isNotBlank(anRules)){
            PlatformBaseRuleType tmpRule; 
            for(String tmpStr : anRules.split(anRules)){
                tmpRule = checking(tmpStr);
                if (tmpRule != NONE_USER){
                   result.add(tmpRule); 
                }
            }
        }
        return result;
    }
}

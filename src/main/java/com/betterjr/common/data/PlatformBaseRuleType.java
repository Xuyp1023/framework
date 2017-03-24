package com.betterjr.common.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.cert.entity.CustCertRule;


public enum PlatformBaseRuleType {
    NONE_USER("陌生人"), CORE_USER("核心企业"), SUPPLIER_USER("供应商"), FACTOR_USER("资金方"), SELLER_USER("经销商"), PLATFORM_USER("平台"), WOS("电子合同服务商");

    private final String title;

    PlatformBaseRuleType(final String anTitle) {
        this.title = anTitle;
    }

    public String getTitle() {
        return this.title;
    }

    public static PlatformBaseRuleType checking(final String anWorkType){
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return PlatformBaseRuleType.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (final Exception ex) {

        }
        return NONE_USER;
    }

    public static List<PlatformBaseRuleType> checkList(final List<CustCertRule> anCertRules){
        final List<PlatformBaseRuleType> result = new ArrayList<>();

        for (final CustCertRule certRule: anCertRules) {
            if (BetterStringUtils.isNotBlank(certRule.getRule())){
                final PlatformBaseRuleType tmpRule  = checking(certRule.getRule());
                if (tmpRule != NONE_USER){
                    result.add(tmpRule);
                }
            }
        }

        return result;
    }

}

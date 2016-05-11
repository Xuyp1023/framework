package com.betterjr.modules.rule.validator;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

public class DataEqualsValidator extends BaseDataValidator {

    @Override
    public String getValidatorName() {
        return "equalsValid";
    }

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness anRuleBusin,
            Object anValue, Object anBusinValue, String anMessage) {
        if (anValue == null) {
            
            return false;
        }
        if (BetterStringUtils.isBlank(anMessage)){
            anMessage = "数据不同！";
        }
        
        return returnValue(anValue.equals(anBusinValue), anResult, anMessage);
    }

}

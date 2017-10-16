package com.betterjr.modules.rule.validator;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

public class DataContainValidator extends BaseDataValidator {

    @Override
    public String getValidatorName() {

        return "containValid";
    }

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult,
            RuleBusiness anRuleBusin, Object anValue, Object anBusinValue, String anMessage) {

        if (anValue == null || anBusinValue == null) {

            return true;
        }
        boolean bb = anBusinValue.toString().contains(anValue.toString());

        if (bb == false && StringUtils.isBlank(anMessage)) {
            anMessage = "不在业务范围内！";
        }

        return returnValue(bb, anResult, anMessage);
    }

}

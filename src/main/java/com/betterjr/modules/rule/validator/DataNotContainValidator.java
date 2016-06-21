package com.betterjr.modules.rule.validator;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

public class DataNotContainValidator extends BaseDataValidator {

    @Override
    public String getValidatorName() {

        return "notContainValid";
    }

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness anRuleBusin,
            Object anValue, Object anBusinValue, String anMessage) {

        if (anValue == null || anBusinValue == null) {

            return true;
        }
        boolean bb = (anBusinValue.toString().contains(anValue.toString()) == false);

        if (bb == false && BetterStringUtils.isBlank(anMessage)) {
            anMessage = "在业务限制范围内！";
        }

        return returnValue(bb, anResult, anMessage);
    }

}

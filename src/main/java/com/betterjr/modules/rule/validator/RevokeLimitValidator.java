package com.betterjr.modules.rule.validator;

import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

/**
 * 撤单验证包括：申请单的状态
 * @author zhoucy
 *
 */
public class RevokeLimitValidator extends BaseDataValidator {

    @Override
    public String getValidatorName() {
        
        return "revokeValid";
    }

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness anRuleBusin,
            Object anValue, Object anBusinValue, String anMessage) {
        
        return false;
    }
}

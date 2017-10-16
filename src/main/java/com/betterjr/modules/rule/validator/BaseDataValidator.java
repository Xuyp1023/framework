package com.betterjr.modules.rule.validator;

import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

/**
 * 验证接口的基类
 * @author zhoucy
 *
 */
public abstract class BaseDataValidator implements DataValidatorFace {

    @Override
    public abstract boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext,
            RuleCheckResult anResult, RuleBusiness anRuleBusin, Object anValue, Object anBusinValue, String anMessage);

    public boolean returnValue(boolean anOk, RuleCheckResult anResult, String anMessage) {
        if (anOk == false) {
            anResult.addMessage(anMessage);
        }

        return anOk;
    }
}

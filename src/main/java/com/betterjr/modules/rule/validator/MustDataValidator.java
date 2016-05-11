package com.betterjr.modules.rule.validator;

import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

/**
 * 必须输入的数据判断
 * @author zhoucy
 *
 */
public class MustDataValidator extends BaseDataValidator implements DataValidatorFace {

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness anRuleBusin,
            Object anValue, Object anBusinValue, String anMessage) {
        boolean bb = true;
        if (anValidator.isMustItem() && (anValue == null)) {
            bb = false;
        }
        
        return returnValue(bb, anResult, anValidator.dataFormatMessage());
    }
    
    public String getValidatorName(){
        
        return "mustValid";
    }
}

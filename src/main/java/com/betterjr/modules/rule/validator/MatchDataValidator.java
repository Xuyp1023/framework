package com.betterjr.modules.rule.validator;

import java.util.regex.Pattern;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

/**
 * 正则匹配
 * @author zhoucy
 *
 */
public class MatchDataValidator extends BaseDataValidator {
    
    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness anRuleBusin,
            Object anValue, Object anBusinValue, String anMessage) {
        String regex = anValidator.getWorkPattern();
        if (anValue == null || BetterStringUtils.isBlank(regex)) {
            
            return true;
        }

        boolean bb = true;
        String msg = anMessage;
        bb = Pattern.matches(regex, anValue.toString());
        if (BetterStringUtils.isBlank(msg)) {
            msg = anValidator.matchMessage();
        }

        return returnValue(bb, anResult, msg);
    }
    public String getValidatorName(){
        
        return "matchValid";
    }
}
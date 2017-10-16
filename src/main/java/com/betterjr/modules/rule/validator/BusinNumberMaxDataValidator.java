package com.betterjr.modules.rule.validator;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

/**
 * 最大值验证
 * 
 * @author zhoucy
 *
 */
public class BusinNumberMaxDataValidator extends BaseDataValidator implements DataValidatorFace {

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult,
            RuleBusiness anRuleBusin, Object anValue, Object anBusinValue, String anMessage) {
        if (anValue == null || anBusinValue == null) {

            return true;
        }

        if (anBusinValue instanceof BigDecimal) {
            BigDecimal bdb = (BigDecimal) anBusinValue;
            if (bdb.compareTo(BigDecimal.ZERO) == 0) {
                return true;
            }
        }
        boolean bb = false;
        String msg = anMessage;

        // 数字，就判断数字的大小
        if (anValue instanceof Number && anBusinValue instanceof Number) {

            bb = MathExtend.compareTo(anValue, anBusinValue, anValidator.getDataScale().intValue()) <= 0;
            if (StringUtils.isBlank(msg)) {
                msg = anValidator.dateMaxMessage();
            }
        }

        return returnValue(bb, anResult, msg);
    }

    @Override
    public String getValidatorName() {

        return "businMaxNumberValid";
    }
}

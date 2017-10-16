package com.betterjr.modules.rule.validator;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.TypeCaseHelper;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

/**
 * 最大值验证
 * @author zhoucy
 *
 */
public class MaxDataValidator extends BaseDataValidator implements DataValidatorFace {

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult,
            RuleBusiness anRuleBusin, Object anValue, Object anBusinValue, String anMessage) {
        if (anValue == null || anBusinValue == null) {

            return true;
        }

        boolean bb = false;
        String msg = anMessage;

        // 字符，判断字符串的长度
        if (anValue instanceof String && anBusinValue != null && anBusinValue instanceof Number) {
            String tmpStr = (String) anValue;
            Number nn = (Number) anBusinValue;
            bb = tmpStr.length() <= nn.intValue();
            msg = anValidator.strMaxLengthMessage();
        }

        // 数字，就判断数字的大小
        if (anValue instanceof Number && anBusinValue instanceof Number) {

            bb = MathExtend.compareTo(anValue, anBusinValue, anValidator.getDataScale().intValue()) <= 0;
            if (StringUtils.isBlank(msg)) {
                msg = anValidator.dateMaxMessage();
            }
        }

        // 日期，判断是否大于该日期
        if (anValue instanceof Date) {
            anBusinValue = TypeCaseHelper.convert(anBusinValue, anValue.getClass().getSimpleName(),
                    BetterDateUtils.DATE_DEFFMT);
            Date dd = (Date) anValue;
            bb = dd.compareTo((Date) anBusinValue) <= 0;
            if (StringUtils.isBlank(msg)) {
                msg = anValidator.dateMaxMessage();
            }
        }

        return returnValue(bb, anResult, msg);
    }

    @Override
    public String getValidatorName() {

        return "maxValid";
    }
}

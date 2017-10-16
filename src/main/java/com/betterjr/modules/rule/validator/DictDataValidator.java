package com.betterjr.modules.rule.validator;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.exception.BytterDeclareException;
import com.betterjr.common.utils.AreaUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

/**
 * 验证数据是否属于一个数据字典的值；确保存入的数据有效
 * 
 * @author zhoucy
 *
 */
public class DictDataValidator extends BaseDataValidator {

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult,
            RuleBusiness anRuleBusin, Object anValue, Object anBusinValue, String anMessage) {
        if (anValue == null) {

            return true;
        }

        String workType = anValidator.getWorkPattern();
        if (StringUtils.isBlank(workType)) {
            throw new BytterDeclareException(60002, "Base Dict Validator, Must Declare T_RULE_VALIDATOR.C_PATTERN");
        }

        boolean bb = false;
        if (workType.equalsIgnoreCase(AreaUtils.CACHE_AREA_MAP)) {

            bb = AreaUtils.checkExists(anValue.toString());
        } else {
            bb = DictUtils.isDictValue(workType, anValue.toString());
        }
        String msg = anMessage;
        if (StringUtils.isBlank(msg)) {
            msg = anValidator.dictMessage();
        }

        return returnValue(bb, anResult, msg);
    }

    @Override
    public String getValidatorName() {
        return "dictValid";
    }
}

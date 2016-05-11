package com.betterjr.modules.rule.validator;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.IdcardUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

import java.util.*;

public class IdentNoCheckValidator extends BaseDataValidator {

    @Override
    public String getValidatorName() {

        return "identNoValid";
    }

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness anRuleBusin,
            Object anValue, Object anBusinValue, String anMessage) {
        DataValidContext validConext = DataValidContext.getValidContext();
        Map<String, Object> refValue = validConext.getRefValue();
        if (refValue == null) {
            return returnValue(false, anResult, "在字段请C_REF_VALUE定义证件类型！");
        }
        String idType = (String) refValue.get("idType");
        if (BetterStringUtils.isBlank(idType)) {
            return returnValue(false, anResult, "在字段请C_REF_VALUE定义证件类型idType:证件类型属性名称！");
        }
        String custType = (String) refValue.get("custType");

        // 机构一般不验证证件号码！
        if ("0".equals(custType)) {

            return true;
        }
        boolean bb = IdcardUtils.validByIdCard(anValue.toString(), idType);
        if (bb == false && BetterStringUtils.isBlank(anMessage)){
        
            anMessage = "身份证信息无效，请确认填写的内容！";
        }
        
        return returnValue(bb, anResult, anMessage);
    }

}
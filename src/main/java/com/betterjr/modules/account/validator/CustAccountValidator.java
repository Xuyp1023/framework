package com.betterjr.modules.account.validator;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;
import com.betterjr.modules.rule.validator.BaseDataValidator;

/**
 * 判断操作员能否操作该客户资料
 * @author zhoucy
 *
 */
public class CustAccountValidator extends BaseDataValidator {

    @Override
    public String getValidatorName() {

        return "custNoValid";
    }

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult,
            RuleBusiness anRuleBusin, Object anValue, Object anBusinValue, String anMessage) {
        if (anValue != null) {
            Long custNo = (Long) anValue;
            CustContextInfo contextInfo = UserUtils.getOperatorContextInfo();
            CustInfo custInfo = contextInfo.findCust(custNo);
            if (StringUtils.isBlank(anMessage)) {
                anMessage = "操作员不能操作该客户，客户号是：" + custNo;
            }

            return returnValue(custInfo != null, anResult, String.format(anMessage, custNo));
        }

        return true;
    }

}

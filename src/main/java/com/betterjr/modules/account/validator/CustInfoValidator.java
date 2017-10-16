package com.betterjr.modules.account.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.account.data.SaleRequestFace;
import com.betterjr.modules.account.entity.SaleAccoRequestInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;
import com.betterjr.modules.rule.validator.BaseDataValidator;

//验证客户是否存在，存在返回False
public class CustInfoValidator extends BaseDataValidator {

    @Autowired
    private CustAccountService accountService;

    @Override
    public String getValidatorName() {

        return "custInfoValid";
    }

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult,
            RuleBusiness anRuleBusin, Object anValue, Object anBusinValue, String anMessage) {
        SaleRequestFace req = anContext.getRequest();
        if (req != null && req instanceof SaleAccoRequestInfo) {
            SaleAccoRequestInfo request = (SaleAccoRequestInfo) req;
            if (this.accountService == null) {
                this.accountService = anContext.getBean(CustAccountService.class);
            }

            boolean bb = (this.accountService.checkAccountExists(request.getCustType(), request.getIdentType(),
                    request.getIdentNo()) == false);

            if (bb == false) {
                if (StringUtils.isBlank(anMessage)) {
                    anMessage = "客户资料已经存在，请换其他账户！";
                }
            }

            return returnValue(bb, anResult, anMessage);
        }
        return false;
    }
}

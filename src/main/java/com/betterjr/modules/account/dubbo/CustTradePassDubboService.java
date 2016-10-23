// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月19日, liuwl, creation
// ============================================================================
package com.betterjr.modules.account.dubbo;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.JedisUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.account.dubbo.interfaces.ICustTradePassService;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustPassInfo;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.account.service.CustPassService;
import com.betterjr.modules.sms.constants.SmsConstants;
import com.betterjr.modules.sms.dubbo.interfaces.IVerificationCodeService;
import com.betterjr.modules.sms.entity.VerifyCode;
import com.betterjr.modules.sms.util.VerifyCodeType;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm.HashPassword;
import com.betterjr.modules.wechat.service.CustWeChatService;

/**
 * @author liuwl
 *
 */
@Service(interfaceClass = ICustTradePassService.class)
public class CustTradePassDubboService implements ICustTradePassService {

    @Reference(interfaceClass = IVerificationCodeService.class)
    private IVerificationCodeService verificationCodeService;

    @Resource
    private CustOperatorService custOperatorService;

    @Resource
    private CustPassService custPassService;

    @Resource
    private CustWeChatService wechatService;


    /* (non-Javadoc)
     * @see com.betterjr.modules.operator.ITradePassService#sendVerifyCode()
     */
    @Override
    public String webSendVerifyCode() {
        // 首先判断有没有绑定微信号
        BTAssert.isTrue(wechatService.checkBindStatus(), "当前操作员还未绑定微信号，不允许修改交易密码！");

        final CustOperatorInfo operator = UserUtils.getOperatorInfo();

        final CustOperatorInfo  tempOperator = custOperatorService.findCustOperatorInfo(operator.getId());

        final String mobile = tempOperator.getMobileNo();

        BTAssert.isTrue(BetterStringUtils.isNotBlank(mobile), "经办人手机号码不允许为空！");

        final VerifyCode verifyCode = verificationCodeService.sendVerifyCode(mobile, VerifyCodeType.CHANGE_TRADE_PASSWORD);
        BTAssert.notNull(verifyCode, "没有生成验证码！");

        JedisUtils.setObject(SmsConstants.smsModifyTradePassVerifyCodePrefix + operator.getId(), verifyCode, SmsConstants.SEC_600);
        return AjaxObject.newOk("发送验证码成功").toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.operator.ITradePassService#checkVerifiCode(java.lang.String)
     */
    @Override
    public String webCheckVerifyCode(final String anVerifyCode) {
        // 首先判断有没有绑定微信号
        BTAssert.isTrue(wechatService.checkBindStatus(), "当前操作员还未绑定微信号，不允许修改交易密码！");

        final CustOperatorInfo operator = UserUtils.getOperatorInfo();

        final VerifyCode verifyCode = JedisUtils.getObject(SmsConstants.smsModifyTradePassVerifyCodePrefix + operator.getId());

        BTAssert.notNull(verifyCode, "验证码已过期");

        if (BetterStringUtils.equals(verifyCode.getVerifiCode(), anVerifyCode)) {
            JedisUtils.setObject(SmsConstants.smsModifyTradePassVerifyCodePrefix + operator.getId(), "true", SmsConstants.SEC_300);

            return AjaxObject.newOk("验证码验证成功").toJson();
        } else {
            return AjaxObject.newError("验证码不匹配").toJson();
        }
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.operator.ITradePassService#saveModifyTradePass(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String webSaveModifyTradePass(final String anNewPassword, final String anOkPassword, final String anOldPassword) {
        // 首先判断有没有绑定微信号
        BTAssert.isTrue(wechatService.checkBindStatus(), "当前操作员还未绑定微信号，不允许修改交易密码！");

        final CustOperatorInfo operator = UserUtils.getOperatorInfo();
        final Object verifyCode = JedisUtils.getObject(SmsConstants.smsModifyTradePassVerifyCodePrefix + operator.getId());

        BTAssert.notNull(verifyCode, "验证信息已过期，不允许修改密码！");

        BTAssert.isTrue(verifyCode instanceof String && BetterStringUtils.equals((String)verifyCode, "true"), "错误的提交");

        return AjaxObject.newOk("交易密码保存成功", custOperatorService.saveModifyTradePassword(anNewPassword, anOkPassword, anOldPassword)).toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.operator.ITradePassService#checkTradePassword(java.lang.String)
     */
    @Override
    public boolean checkTradePassword(final CustOperatorInfo anOperator, final String anTradePassword) {
        final CustPassInfo custPassInfo = custPassService.getOperaterPassByCustNo(anOperator.getId(), CustPasswordType.PERSON_TRADE);
        BTAssert.notNull(custPassInfo, "没有找到相应的交易密码信息！");

        final HashPassword result = SystemAuthorizingRealm.encrypt(anTradePassword);

        if (StringUtils.equals(result.password, custPassInfo.getPasswd())) {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.account.dubbo.interfaces.ICustTradePassService#webCheckTradePassword(java.lang.String)
     */
    @Override
    public String webCheckTradePassword(final String anTradePassword) {
        final CustOperatorInfo operator = UserUtils.getOperatorInfo();
        BTAssert.notNull(operator, "没有找到登陆信息");

        if (checkTradePassword(operator, anTradePassword) == true) {

            return AjaxObject.newOk("验证交易密码成功").toJson();
        } else {
            return AjaxObject.newError("验证交易密码失败").toJson();
        }
    }

}

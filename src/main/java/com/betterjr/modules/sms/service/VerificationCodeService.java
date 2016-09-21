// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月19日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sms.service;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.JedisUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.notification.INotificationSendService;
import com.betterjr.modules.notification.NotificationModel;
import com.betterjr.modules.notification.NotificationModel.Builder;
import com.betterjr.modules.sms.constants.SmsConstants;
import com.betterjr.modules.sms.entity.VerifyCode;
import com.betterjr.modules.sms.util.VerifyCodeType;

/**
 * @author liuwl
 *
 */
@Service
public class VerificationCodeService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Reference(interfaceClass = INotificationSendService.class)
    public INotificationSendService notificationSendService;

    @Resource
    private CustAccountService accountService;

    @Resource
    private CustOperatorService custOperatorService;

    /**
     *
     * @param anMobile
     * @param anType
     */
    public VerifyCode sendVerificationCode(final String anMobile, final VerifyCodeType anType) {
        final String checkKey = SmsConstants.smsVerifiCodeFrequencyPrefix + anType.getType() + anMobile;

        final String oneMinute = JedisUtils.get(checkKey);
        if (BetterStringUtils.isBlank(oneMinute)) {
            JedisUtils.set(checkKey, "1", SmsConstants.SEC_60);
        } else {
            logger.info("该接口调用太频繁");
            return null;
        }

        final VerifyCode verificationCode = createVerifiCode(anMobile, anType, SmsConstants.SEC_300);
        sendNotification(verificationCode);
        return verificationCode;
    }

    /**
     * @param anVerificationCode
     */
    private void sendNotification(final VerifyCode anVerificationCode) {
        final Long platformCustNo = Long.valueOf(Collections3.getFirst(DictUtils.getDictList("PlatformGroup")).getItemValue());
        final CustInfo platformCustomer = accountService.findCustInfo(platformCustNo);
        final CustOperatorInfo platformOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(platformCustomer.getCustNo()));

        final Builder builder = NotificationModel.newBuilder("平台短信验证码", platformCustomer, platformOperator);
        builder.addParam("verifiCode", anVerificationCode.getVerifiCode());
        builder.addReceiveMobile(anVerificationCode.getMobile());
        notificationSendService.sendNotification(builder.build());
    }

    /**
     * 创建验证码
     * @param anKey
     * @param anExpire
     * @return
     */
    private VerifyCode createVerifiCode(final String anMobile, final VerifyCodeType anType, final int anSeconds) {
        final String verifiCodeKey = SmsConstants.smsVerifiCodePrefix + anType.getType() + anMobile;

        // 如果上一次还未过期 继续使用上一次验证码
        String verifiCode = JedisUtils.get(verifiCodeKey);
        if (null == verifiCode) {
            // 创建 verification code
            verifiCode = createNewVerifiCode();
        }
        JedisUtils.set(verifiCodeKey, verifiCode, anSeconds);

        final VerifyCode verifyCode = new VerifyCode();
        verifyCode.setMobile(anMobile);
        verifyCode.setSeconds(anSeconds);
        verifyCode.setGenTime(new Date());
        verifyCode.setKey(verifiCodeKey);
        verifyCode.setVerifiCode(verifiCode);

        return verifyCode;
    }

    /**
     * 6 位随机验证码
     * @return
     */
    private String createNewVerifiCode() {
        final int verifiCode = SerialGenerator.randomInt(999999) % (999999 - 100000 + 1) + 100000;

        return String.valueOf(verifiCode);
    }

}

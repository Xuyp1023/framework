// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月19日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sms.dubbo;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.sms.dubbo.interfaces.IVerificationCodeService;
import com.betterjr.modules.sms.entity.VerifyCode;
import com.betterjr.modules.sms.service.VerificationCodeService;
import com.betterjr.modules.sms.util.VerifyCodeType;

/**
 * @author liuwl
 *
 */
@Service(interfaceClass = IVerificationCodeService.class)
public class VerificationCodeDubboService implements IVerificationCodeService {
    @Resource
    private VerificationCodeService verifiCodeService;

    @Override
    public VerifyCode sendVerifyCode(final String anMobile, final VerifyCodeType anType) {
        return verifiCodeService.sendVerificationCode(anMobile, anType);
    }
}

// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月19日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sms.dubbo.interfaces;

import com.betterjr.modules.sms.entity.VerifyCode;
import com.betterjr.modules.sms.util.VerifyCodeType;

/**
 * @author liuwl
 *
 */
public interface IVerificationCodeService {
    VerifyCode sendVerifyCode(final String anMobile, final VerifyCodeType anType);
}

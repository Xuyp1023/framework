// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月19日, liuwl, creation
// ============================================================================
package com.betterjr.modules.account.dubbo.interfaces;

import com.betterjr.modules.account.entity.CustOperatorInfo;

/**
 * @author liuwl
 *
 */
public interface ICustTradePassService {

    /**
     * @return
     */
    String webSendVerifyCode();

    /**
     * @param anVerifyCode
     * @return
     */
    String webCheckVerifyCode(String anVerifyCode);

    /**
     *
     * @param anTradePassword
     * @return
     */
    boolean checkTradePassword(CustOperatorInfo anOperator, String anTradePassword);

    /**
     *
     * @param anTradePassword
     * @return
     */
    String webCheckTradePassword(String anTradePassword);


    /**
     * @param anNewPassword
     * @param anOkPassword
     * @param anOldPassword
     * @return
     */
    String webSaveModifyTradePass(String anNewPassword, String anOkPassword, String anOldPassword);

    /**
     * @param anOperatorId
     * @param anPassword
     * @return
     */
    boolean checkTradePassword(Long anOperatorId, String anPassword);

}

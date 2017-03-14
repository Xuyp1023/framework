// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年3月6日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sys.dubbo.interfaces;

import java.util.Map;

import com.betterjr.common.annotation.AnnotRuleService;
import com.betterjr.modules.rule.annotation.AnnotRuleFunc;
import com.betterjr.modules.rule.entity.RuleFuncType;

/**
 * @author liuwl
 *
 */

@AnnotRuleService("accessWebServiceDubboService")
public interface IAccessWebService {


    /**
     * 获取ticket
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @AnnotRuleFunc(name = "accessTicket", fundType = RuleFuncType.OBJECT)
    public String ticket(Map anMap/*String anToken, String anSign*/);


    /**
     * 首次登陆
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @AnnotRuleFunc(name = "accessFristLogin", fundType = RuleFuncType.OBJECT)
    public String firstLogin(Map anMap/*String anMark, String anSign*/);

}

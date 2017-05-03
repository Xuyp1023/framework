// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.3 : 2017年5月3日, liuwl, creation
// ============================================================================
package com.betterjr.modules.config.dubbo.interfaces;

import java.math.BigDecimal;

import com.betterjr.modules.config.entity.DomainAttribute;

/**
 * @author liuwl
 *
 */
public interface IDomainAttributeService {
    DomainAttribute saveString(String anCode, String anValue);

    DomainAttribute saveString(String anOperOrg, String anCode, String anValue);

    DomainAttribute saveString(String anOperOrg, Long anCustNo, String anCode, String anValue);

    DomainAttribute saveNumber(String anCode, Long anValue);

    DomainAttribute saveNumber(String anOperOrg, String anCode, Long anValue);

    DomainAttribute saveNumber(String anOperOrg, Long anCustNo, String anCode, Long anValue);

    DomainAttribute saveMoney(String anCode, BigDecimal anValue);

    DomainAttribute saveMoney(String anOperOrg, String anCode, BigDecimal anValue);

    DomainAttribute saveMoney(String anOperOrg, Long anCustNo, String anCode, BigDecimal anValue);

    DomainAttribute saveObject(String anCode, Object anValue);

    DomainAttribute saveObject(String anOperOrg, String anCode, Object anValue);

    DomainAttribute saveObject(String anOperOrg, Long anCustNo, String anCode, Object anValue);

    String findString(String anCode);

    String findString(String anOperOrg, String anCode);

    String findString(String anOperOrg, Long anCustNo, String anCode);

    Long findNumber(String anCode);

    Long findNumber(String anOperOrg, String anCode);

    Long findNumber(String anOperOrg, Long anCustNo, String anCode);

    BigDecimal findMoney(String anCode);

    BigDecimal findMoney(String anOperOrg, String anCode);

    BigDecimal findMoney(String anOperOrg, Long anCustNo, String anCode);

    Object findObject(String anCode);

    Object findObject(String anOperOrg, String anCode);

    Object findObject(String anOperOrg, Long anCustNo, String anCode);

}

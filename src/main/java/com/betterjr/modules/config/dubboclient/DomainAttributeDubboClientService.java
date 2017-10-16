// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.3 : 2017年5月3日, liuwl, creation
// ============================================================================
package com.betterjr.modules.config.dubboclient;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService;
import com.betterjr.modules.config.entity.DomainAttribute;

/**
 * @author liuwl
 *
 */
@Service
public class DomainAttributeDubboClientService implements IDomainAttributeService {
    @Reference(interfaceClass = IDomainAttributeService.class)
    private IDomainAttributeService domainAttributeService;

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveString(java.lang.String, java.lang.String)
     */
    @Override
    public DomainAttribute saveString(final String anCode, final String anValue) {
        return domainAttributeService.saveString(anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveString(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public DomainAttribute saveString(final String anOperOrg, final String anCode, final String anValue) {
        return domainAttributeService.saveString(anOperOrg, anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveString(java.lang.String, java.lang.Long, java.lang.String,
     * java.lang.String)
     */
    @Override
    public DomainAttribute saveString(final String anOperOrg, final Long anCustNo, final String anCode,
            final String anValue) {
        return domainAttributeService.saveString(anOperOrg, anCustNo, anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveNumber(java.lang.String, java.lang.Long)
     */
    @Override
    public DomainAttribute saveNumber(final String anCode, final Long anValue) {
        return domainAttributeService.saveNumber(anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveNumber(java.lang.String, java.lang.String, java.lang.Long)
     */
    @Override
    public DomainAttribute saveNumber(final String anOperOrg, final String anCode, final Long anValue) {
        return domainAttributeService.saveNumber(anOperOrg, anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveNumber(java.lang.String, java.lang.Long, java.lang.String,
     * java.lang.Long)
     */
    @Override
    public DomainAttribute saveNumber(final String anOperOrg, final Long anCustNo, final String anCode,
            final Long anValue) {
        return domainAttributeService.saveNumber(anOperOrg, anCustNo, anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveMoney(java.lang.String, java.math.BigDecimal)
     */
    @Override
    public DomainAttribute saveMoney(final String anCode, final BigDecimal anValue) {
        return domainAttributeService.saveMoney(anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveMoney(java.lang.String, java.lang.String, java.math.BigDecimal)
     */
    @Override
    public DomainAttribute saveMoney(final String anOperOrg, final String anCode, final BigDecimal anValue) {
        return domainAttributeService.saveMoney(anOperOrg, anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveMoney(java.lang.String, java.lang.Long, java.lang.String,
     * java.math.BigDecimal)
     */
    @Override
    public DomainAttribute saveMoney(final String anOperOrg, final Long anCustNo, final String anCode,
            final BigDecimal anValue) {
        return domainAttributeService.saveMoney(anOperOrg, anCustNo, anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveObject(java.lang.String, java.lang.Object)
     */
    @Override
    public DomainAttribute saveObject(final String anCode, final Object anValue) {
        return domainAttributeService.saveObject(anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveObject(java.lang.String, java.lang.String, java.lang.Object)
     */
    @Override
    public DomainAttribute saveObject(final String anOperOrg, final String anCode, final Object anValue) {
        return domainAttributeService.saveObject(anOperOrg, anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#saveObject(java.lang.String, java.lang.Long, java.lang.String,
     * java.lang.Object)
     */
    @Override
    public DomainAttribute saveObject(final String anOperOrg, final Long anCustNo, final String anCode,
            final Object anValue) {
        return domainAttributeService.saveObject(anOperOrg, anCustNo, anCode, anValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findString(java.lang.String)
     */
    @Override
    public String findString(final String anCode) {
        return domainAttributeService.findString(anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findString(java.lang.String, java.lang.String)
     */
    @Override
    public String findString(final String anOperOrg, final String anCode) {
        return domainAttributeService.findString(anOperOrg, anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findString(java.lang.String, java.lang.Long, java.lang.String)
     */
    @Override
    public String findString(final String anOperOrg, final Long anCustNo, final String anCode) {
        return domainAttributeService.findString(anOperOrg, anCustNo, anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findNumber(java.lang.String)
     */
    @Override
    public Long findNumber(final String anCode) {
        return domainAttributeService.findNumber(anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findNumber(java.lang.String, java.lang.String)
     */
    @Override
    public Long findNumber(final String anOperOrg, final String anCode) {
        return domainAttributeService.findNumber(anOperOrg, anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findNumber(java.lang.String, java.lang.Long, java.lang.String)
     */
    @Override
    public Long findNumber(final String anOperOrg, final Long anCustNo, final String anCode) {
        return domainAttributeService.findNumber(anOperOrg, anCustNo, anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findMoney(java.lang.String)
     */
    @Override
    public BigDecimal findMoney(final String anCode) {
        return domainAttributeService.findMoney(anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findMoney(java.lang.String, java.lang.String)
     */
    @Override
    public BigDecimal findMoney(final String anOperOrg, final String anCode) {
        return domainAttributeService.findMoney(anOperOrg, anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findMoney(java.lang.String, java.lang.Long, java.lang.String)
     */
    @Override
    public BigDecimal findMoney(final String anOperOrg, final Long anCustNo, final String anCode) {
        return domainAttributeService.findMoney(anOperOrg, anCustNo, anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findObject(java.lang.String)
     */
    @Override
    public Object findObject(final String anCode) {
        return domainAttributeService.findObject(anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findObject(java.lang.String, java.lang.String)
     */
    @Override
    public Object findObject(final String anOperOrg, final String anCode) {
        return domainAttributeService.findObject(anOperOrg, anCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService#findObject(java.lang.String, java.lang.Long, java.lang.String)
     */
    @Override
    public Object findObject(final String anOperOrg, final Long anCustNo, final String anCode) {
        return domainAttributeService.findObject(anOperOrg, anCustNo, anCode);
    }

}

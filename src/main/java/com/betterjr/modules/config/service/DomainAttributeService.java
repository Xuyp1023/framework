// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.3 : 2017年5月3日, liuwl, creation
// ============================================================================
package com.betterjr.modules.config.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.config.constants.ConfigType;
import com.betterjr.modules.config.dao.DomainAttributeMapper;
import com.betterjr.modules.config.entity.DomainAttribute;

/**
 * @author liuwl
 *
 */
@Service
public class DomainAttributeService extends BaseService<DomainAttributeMapper, DomainAttribute> {
    @Resource
    private CustAccountService custAccountService;

    /**
     * 存储参数
     * @param anCode
     * @param anValue
     * @return
     */
    public DomainAttribute saveString(final String anCode, final String anValue) {
        return saveString(null, null, anCode, anValue);
    }

    public DomainAttribute saveString(final String anOperOrg, final String anCode, final String anValue) {
        return saveString(anOperOrg, null, anCode, anValue);
    }

    public DomainAttribute saveString(final String anOperOrg, final Long anCustNo, final String anCode, final String anValue) {
        return saveCommon(anOperOrg, anCustNo, anCode, anValue, ConfigType.STRING);
    }

    public DomainAttribute saveNumber(final String anCode, final Long anValue) {
        return saveNumber(null, null, anCode, anValue);
    }

    public DomainAttribute saveNumber(final String anOperOrg, final String anCode, final Long anValue) {
        return saveNumber(anOperOrg, null, anCode, anValue);
    }

    public DomainAttribute saveNumber(final String anOperOrg, final Long anCustNo, final String anCode, final Long anValue) {
        return saveCommon(anOperOrg, anCustNo, anCode, anValue, ConfigType.NUMBER);
    }

    public DomainAttribute saveMoney(final String anCode, final BigDecimal anValue) {
        return saveMoney(null, null, anCode, anValue);
    }

    public DomainAttribute saveMoney(final String anOperOrg, final String anCode, final BigDecimal anValue) {
        return saveMoney(anOperOrg, null, anCode, anValue);
    }

    public DomainAttribute saveMoney(final String anOperOrg, final Long anCustNo, final String anCode, final BigDecimal anValue) {
        return saveCommon(anOperOrg, anCustNo, anCode, anValue, ConfigType.MONEY);
    }

    public DomainAttribute saveObject(final String anCode, final Object anValue) {
        return saveObject(null, null, anCode, anValue);
    }

    public DomainAttribute saveObject(final String anOperOrg, final String anCode, final Object anValue) {
        return saveObject(anOperOrg, null, anCode, anValue);
    }

    public DomainAttribute saveObject(final String anOperOrg, final Long anCustNo, final String anCode, final Object anValue) {
        return saveCommon(anOperOrg, anCustNo, anCode, anValue, ConfigType.OBJECT);
    }


    public DomainAttribute saveCommon(final String anOperOrg, final Long anCustNo, final String anCode, final Object anValue,
            final ConfigType anType) {
        BTAssert.isTrue(BetterStringUtils.isNotBlank(anCode), "参数代码不允许为空！");

        String operOrg = "DEFAULT";
        if (BetterStringUtils.isNotBlank(anOperOrg)) {
            operOrg = anOperOrg;
        }
        Long custNo = 0L;
        if (anCustNo != null) {
            custNo = anCustNo;
        }

        CustInfo custInfo = null;
        if (BetterStringUtils.equals("DEFAULT", operOrg) && !custNo.equals(0L)) {
            custInfo = custAccountService.findCustInfo(custNo);
            BTAssert.notNull(custInfo, "没有找到指定公司！");

            BTAssert.isTrue(BetterStringUtils.equals(operOrg, custInfo.getOperOrg()), "操作机构不匹配！");
        }

        final Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put("operOrg", operOrg);
        conditionMap.put("custNo", custNo);
        conditionMap.put("code", anCode);

        DomainAttribute domainAttribute = Collections3.getFirst(this.selectByProperty(conditionMap));
        final CustOperatorInfo operator = UserUtils.getOperatorInfo();

        if (domainAttribute != null) { // update

            setValue(domainAttribute, anValue, anType);
            domainAttribute.modify(operator);

            final int result = this.updateByPrimaryKeySelective(domainAttribute);
            BTAssert.isTrue(result == 1, "操作失败！");
        }
        else { // insert
            domainAttribute = new DomainAttribute();
            domainAttribute.setOperOrg(operOrg);
            domainAttribute.setCustNo(custNo);
            domainAttribute.setCode(anCode);

            setValue(domainAttribute, anValue, anType);

            domainAttribute.init(operator);

            if (custInfo != null) {
                domainAttribute.setCustName(custInfo.getName());
            }

            final int result = this.insert(domainAttribute);
            BTAssert.isTrue(result == 1, "操作失败！");
        }

        return domainAttribute;
    }

    /**
     * 获取参数值
     * @param anCode
     * @return
     */
    public String findString(final String anCode) {
        return findString(null, null, anCode);
    }

    public String findString(final String anOperOrg, final String anCode) {
        return findString(anOperOrg, null, anCode);
    }

    public String findString(final String anOperOrg, final Long anCustNo, final String anCode) {
        final DomainAttribute domainAttribute = findCommon(anOperOrg, anCustNo, anCode);
        if (domainAttribute == null) {
            return null;
        }
        BTAssert.isTrue(BetterStringUtils.equals("00", domainAttribute.getDataType()), "数据类型不匹配！");
        return domainAttribute.getStringValue();
    }

    public Long findNumber(final String anCode) {
        return findNumber(null, null, anCode);
    }

    public Long findNumber(final String anOperOrg, final String anCode) {
        return findNumber(anOperOrg, null, anCode);
    }

    public Long findNumber(final String anOperOrg, final Long anCustNo, final String anCode) {
        final DomainAttribute domainAttribute = findCommon(anOperOrg, anCustNo, anCode);
        if (domainAttribute == null) {
            return null;
        }
        BTAssert.isTrue(BetterStringUtils.equals("01", domainAttribute.getDataType()), "数据类型不匹配！");
        return domainAttribute.getNumberValue();
    }

    public BigDecimal findMoney(final String anCode) {
        return findMoney(null, null, anCode);
    }

    public BigDecimal findMoney(final String anOperOrg, final String anCode) {
        return findMoney(anOperOrg, null, anCode);
    }

    public BigDecimal findMoney(final String anOperOrg, final Long anCustNo, final String anCode) {
        final DomainAttribute domainAttribute = findCommon(anOperOrg, anCustNo, anCode);
        if (domainAttribute == null) {
            return null;
        }
        BTAssert.isTrue(BetterStringUtils.equals("02", domainAttribute.getDataType()), "数据类型不匹配！");
        return domainAttribute.getMoneyValue();
    }

    public Object findObject(final String anCode) {
        return findObject(null, null, anCode);
    }

    public Object findObject(final String anOperOrg, final String anCode) {
        return findObject(anOperOrg, null, anCode);
    }

    public Object findObject(final String anOperOrg, final Long anCustNo, final String anCode) {
        final DomainAttribute domainAttribute = findCommon(anOperOrg, anCustNo, anCode);
        if (domainAttribute == null) {
            return null;
        }
        BTAssert.isTrue(BetterStringUtils.equals("03", domainAttribute.getDataType()), "数据类型不匹配！");
        String objectString = domainAttribute.getObjectValue();
        Object object = JsonMapper.parserJson(objectString);
        return object;
    }

    public DomainAttribute findCommon(final String anOperOrg, final Long anCustNo, final String anCode) {
        BTAssert.isTrue(BetterStringUtils.isNotBlank(anCode), "参数代码不允许为空！");

        String operOrg = "DEFAULT";
        if (BetterStringUtils.isNotBlank(anOperOrg)) {
            operOrg = anOperOrg;
        }
        Long custNo = 0L;
        if (anCustNo != null) {
            custNo = anCustNo;
        }

        CustInfo custInfo = null;
        if (BetterStringUtils.equals("DEFAULT", operOrg) && !custNo.equals(0L)) {
            custInfo = custAccountService.findCustInfo(custNo);
            BTAssert.notNull(custInfo, "没有找到指定公司！");

            BTAssert.isTrue(BetterStringUtils.equals(operOrg, custInfo.getOperOrg()), "操作机构不匹配！");
        }

        final Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put("operOrg", operOrg);
        conditionMap.put("custNo", custNo);
        conditionMap.put("code", anCode);

        final DomainAttribute domainAttribute = Collections3.getFirst(this.selectByProperty(conditionMap));

        return domainAttribute;
    }

    /**
     * @param anDomainAttribute
     * @param anValue
     * @param anType
     */
    private void setValue(final DomainAttribute anDomainAttribute, final Object anValue, final ConfigType anType) {
        switch (anType) {
        case STRING:
            anDomainAttribute.setStringValue(anValue == null ? "" : String.valueOf(anValue));
            anDomainAttribute.setDataType("00");
            break;
        case NUMBER:
            anDomainAttribute.setNumberValue(anValue == null ? null : (Long)anValue);
            anDomainAttribute.setDataType("01");
            break;
        case MONEY:
            anDomainAttribute.setMoneyValue(anValue == null ? null : (BigDecimal)anValue);
            anDomainAttribute.setDataType("02");
            break;
        case OBJECT:
            anDomainAttribute.setObjectValue(anValue == null ? null: JsonMapper.toJsonString(anValue));
            anDomainAttribute.setDataType("03");
            break;
        }
    }

}

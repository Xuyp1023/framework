// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION 
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月9日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sys.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.sys.dao.SysParamMapper;
import com.betterjr.modules.sys.entity.SysParam;

/**
 * @author liuwl
 *
 */
public class SysParamService extends BaseService<SysParamMapper, SysParam> {
    @Resource
    private SysParamItemService paramItemService;

    /**
     * 保存单个参数
     * 
     * @return
     */
    public SysParam saveParameter(final SysParam anParameter) {

        return null;
    }

    public void saveParameters(final Collection<SysParam> anParameters) {

    }

    /**
     * 通过参数名，客户编号，操作员编号查找 参数 允许 操作员编号为空
     * 
     * @param anName
     * @param anCustNo
     * @param anOperId
     * @return
     */
    public SysParam findParameterByCondition(String anName, Long anCustNo, Long anOperId) {

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("name", anName);
        conditionMap.put("custNo", anCustNo);
        Optional<Long> operIdOpt = Optional.ofNullable(anOperId);
        operIdOpt.ifPresent(operId -> {
            conditionMap.put("operId", operId);
        });

        Collection<SysParam> parameters = this.selectByProperty(conditionMap);
        Optional<SysParam> sysParameterOpt = Optional.ofNullable(Collections3.getFirst(parameters));

        sysParameterOpt.ifPresent(sysParameter -> {
            sysParameter.setParameterItems(paramItemService.queryParameterItemsByParameter(sysParameter));
        });

        return sysParameterOpt.get();
    }

}

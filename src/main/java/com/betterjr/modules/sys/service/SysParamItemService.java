// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION 
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月9日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sys.service;

import java.util.Collection;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.sys.dao.SysParamItemMapper;
import com.betterjr.modules.sys.entity.SysParam;
import com.betterjr.modules.sys.entity.SysParamItem;

/**
 * @author liuwl
 *
 */
public class SysParamItemService extends BaseService<SysParamItemMapper, SysParamItem> {

    /**
     * 
     * @param anSysParameter
     * @return
     */
    public Collection<SysParamItem> queryParameterItemsByParameter(final SysParam anSysParameter) {

        return null;
    }
}

package com.betterjr.modules.account.dubbo.interfaces;

import java.util.Collection;

import com.betterjr.modules.account.entity.CustInfo;

/**
 * 机构基本信息
 * @author liuwl
 *
 */
public interface ICustInfoService {
    /**
     * 查询当前操作员下的所有机构-对WEB提供
     * @return
     */
    public String webQueryCustInfo();
    
    /**
     * 查询当前操作员下的所有机构
     * @return
     */
    public Collection<CustInfo> queryCustInfo();
}

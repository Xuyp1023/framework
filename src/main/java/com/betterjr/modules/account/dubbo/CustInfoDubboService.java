package com.betterjr.modules.account.dubbo;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.account.dubbo.interfaces.ICustInfoService;
import com.betterjr.modules.account.entity.CustInfo;

/**
 * 机构基本信息
 * 
 * @author liuwl
 *
 */
@Service(interfaceClass = ICustInfoService.class)
public class CustInfoDubboService implements ICustInfoService {
    private static Logger logger = LoggerFactory.getLogger(CustInfoDubboService.class);

    @Override
    public String webQueryCustInfo() {
        Collection<CustInfo> custInfos = queryCustInfo();
        return AjaxObject.newOk("查询操作员所有的公司列表成功", custInfos).toJson();
    }

    @Override
    public Collection<CustInfo> queryCustInfo() {
        return UserUtils.findCustInfoList();
    }

}

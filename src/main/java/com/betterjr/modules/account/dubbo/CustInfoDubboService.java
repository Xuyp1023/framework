package com.betterjr.modules.account.dubbo;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.account.dubbo.interfaces.ICustInfoService;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;

/**
 * 机构基本信息
 * 
 * @author liuwl
 *
 */
@Service(interfaceClass = ICustInfoService.class)
public class CustInfoDubboService implements ICustInfoService {
    private static Logger logger = LoggerFactory.getLogger(CustInfoDubboService.class);

    @Autowired
    private CustAndOperatorRelaService custOperatorRelaService;
    
    @Override
    public String webQueryCustInfo() {
        Collection<CustInfo> custInfos = queryCustInfo();
        return AjaxObject.newOk("查询操作员所有的公司列表成功", custInfos).toJson();
    }

    @Override
    public Collection<CustInfo> queryCustInfo() {
        return UserUtils.findCustInfoList();
    }
    
    /***
     * 获取当前登录的客户号
     * @return
     */
    public Long findCustNo(){
        List<Long> custList=custOperatorRelaService.findCustNoList(UserUtils.getOperatorInfo().getId(),UserUtils.getOperatorInfo().getOperOrg());
        logger.info("findCustNo custList:"+custList);
        return Collections3.getFirst(custList);
    }

}

package com.betterjr.modules.account.dubbo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.account.dubbo.interfaces.ICustOperatorService;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustOperatorService;

@Service(interfaceClass = ICustOperatorService.class)
public class CustOperatorDubboService implements ICustOperatorService {

    @Autowired
    private CustOperatorService custOperatorService;

    @Override
    public CustOperatorInfo findCustOperatorByOperCode(final String anOperOrg, final String anOperCode) {
        return custOperatorService.findCustOperatorByOperCode(anOperOrg, anOperCode);
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.account.dubbo.interfaces.ICustOperatorService#findCustOperatorById(java.lang.Long)
     */
    @Override
    public CustOperatorInfo findCustOperatorById(final Long anId) {
        return custOperatorService.findCustOperatorInfo(anId);
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.account.dubbo.interfaces.ICustOperatorService#queryOperatorByCustNo(java.lang.Long)
     */
    @Override
    public List<CustOperatorInfo> queryOperatorByCustNo(final Long anCustNo) {
        return custOperatorService.queryOperatorInfoByCustNo(anCustNo);
    }

}

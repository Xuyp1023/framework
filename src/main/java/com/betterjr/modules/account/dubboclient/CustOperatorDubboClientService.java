package com.betterjr.modules.account.dubboclient;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.account.dubbo.interfaces.ICustInfoService;
import com.betterjr.modules.account.dubbo.interfaces.ICustOperatorService;
import com.betterjr.modules.account.entity.CustOperatorInfo;

@Service
public class CustOperatorDubboClientService implements ICustOperatorService {

    @Reference(interfaceClass=ICustOperatorService.class)
    private ICustOperatorService custOperatorService;

    @Reference(interfaceClass=ICustInfoService.class)
    private ICustInfoService custInfoService;

    @Override
    public CustOperatorInfo findCustOperatorByOperCode(final String anOperOrg, final String anOperCode) {
        // TODO Auto-generated method stub
        return custOperatorService.findCustOperatorByOperCode(anOperOrg, anOperCode);
    }

    public Long findCustNo(){
        return custInfoService.findCustNo();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.account.dubbo.interfaces.ICustOperatorService#findCustOperatorById(java.lang.Long)
     */
    @Override
    public CustOperatorInfo findCustOperatorById(final Long anId) {
        return custOperatorService.findCustOperatorById(anId);
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.account.dubbo.interfaces.ICustOperatorService#queryOperatorByCustNo(java.lang.Long)
     */
    @Override
    public List<CustOperatorInfo> queryOperatorByCustNo(final Long anCustNo) {
        return custOperatorService.queryOperatorByCustNo(anCustNo);
    }

}

package com.betterjr.modules.account.dubboclient;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.modules.account.dubbo.interfaces.ICustPassService;
import com.betterjr.modules.account.dubbo.interfaces.ICustTradePassService;
import com.betterjr.modules.account.entity.CustPassInfo;

@Service
public class CustPassDubboClientService implements ICustPassService {

    @Reference(interfaceClass=ICustPassService.class)
    private ICustPassService custPassService;

    @Reference(interfaceClass=ICustTradePassService.class)
    private ICustTradePassService custTradePassService;

    @Override
    public CustPassInfo getOperaterPassByCustNo(final Long anPassID, final CustPasswordType anPassType) {
        // TODO Auto-generated method stub
        return custPassService.getOperaterPassByCustNo(anPassID, anPassType);
    }

    @Override
    public List<SimpleDataEntity> findPassAndSalt(final Long anPassID, final String[] anPassTypes) {
        // TODO Auto-generated method stub
        return custPassService.findPassAndSalt(anPassID, anPassTypes);
    }

    public boolean checkTradePassword(final Long anOperatorId, final String anPassword) {
        return custTradePassService.checkTradePassword(anOperatorId, anPassword);
    }
}

package com.betterjr.modules.account.dubboclient;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.modules.account.dubbo.interfaces.ICustPassService;
import com.betterjr.modules.account.entity.CustPassInfo;

@Service
public class CustPassDubboClientService implements ICustPassService {

	@Reference(interfaceClass=ICustPassService.class)
	private ICustPassService custPassService;
	
    @Override
    public CustPassInfo getOperaterPassByCustNo(Long anPassID, CustPasswordType anPassType) {
        // TODO Auto-generated method stub
        return custPassService.getOperaterPassByCustNo(anPassID, anPassType);
    }

    @Override
    public List<SimpleDataEntity> findPassAndSalt(Long anPassID, String[] anPassTypes) {
        // TODO Auto-generated method stub
        return custPassService.findPassAndSalt(anPassID, anPassTypes);
    }

}

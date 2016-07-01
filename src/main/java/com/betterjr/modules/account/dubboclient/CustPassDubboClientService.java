package com.betterjr.modules.account.dubboclient;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.account.dubbo.interfaces.ICustPassService;
import com.betterjr.modules.account.entity.CustPassInfo;

@Service
public class CustPassDubboClientService implements ICustPassService {

	@Reference(interfaceClass=ICustPassService.class)
	private ICustPassService custPassService;
	
	@Override
	public CustPassInfo getOperaterPassByCustNo(Long anPassID) {
		// TODO Auto-generated method stub
		return custPassService.getOperaterPassByCustNo(anPassID);
	}

}

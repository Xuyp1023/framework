package com.betterjr.modules.account.dubboclient;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.dubbo.interfaces.ICustLoginService;
import com.betterjr.modules.account.entity.CustOperatorInfo;

@Service
public class CustLoginDubboClientService implements ICustLoginService {
	
	@Reference(interfaceClass=ICustLoginService.class)
	private ICustLoginService custLoginService;

	@Override
	public CustContextInfo saveFormLogin(CustOperatorInfo anUser) {
		// TODO Auto-generated method stub
		return custLoginService.saveFormLogin(anUser);
	}

	@Override
	public CustContextInfo saveTokenLogin(String anToken) {
		// TODO Auto-generated method stub
		return custLoginService.saveTokenLogin(anToken);
	}

}

package com.betterjr.modules.account.dubboclient;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.account.dubbo.interfaces.ICustOperatorService;
import com.betterjr.modules.account.entity.CustOperatorInfo;

@Service
public class CustOperatorDubboClientService implements ICustOperatorService {

	@Reference(interfaceClass=ICustOperatorService.class)
	private ICustOperatorService custOperatorService;
	
	@Override
	public CustOperatorInfo findCustOperatorByOperCode(String anOperOrg, String anOperCode) {
		// TODO Auto-generated method stub
		return custOperatorService.findCustOperatorByOperCode(anOperOrg, anOperCode);
	}

}

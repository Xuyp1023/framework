package com.betterjr.modules.account.dubbo;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.account.dubbo.interfaces.ICustOperatorService;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustOperatorService;

@Service(interfaceClass=ICustOperatorService.class)
public class CustOperatorDubboService implements ICustOperatorService {

	@Autowired
	private CustOperatorService custOperatorService;
	@Override
	public CustOperatorInfo findCustOperatorByOperCode(String anOperOrg, String anOperCode) {
		// TODO Auto-generated method stub
		return custOperatorService.findCustOperatorByOperCode(anOperOrg, anOperCode);
	}

}

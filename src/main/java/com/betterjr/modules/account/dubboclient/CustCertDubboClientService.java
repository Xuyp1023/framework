package com.betterjr.modules.account.dubboclient;

import java.security.cert.X509Certificate;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.account.dubbo.interfaces.ICustCertService;
import com.betterjr.modules.account.entity.CustCertInfo;

@Service
public class CustCertDubboClientService implements ICustCertService {

	@Reference(interfaceClass=ICustCertService.class)
	private ICustCertService custCertService;
	@Override
	public CustCertInfo checkValidity(X509Certificate anX509) {
		// TODO Auto-generated method stub
		return custCertService.checkValidity(anX509);
	}

}

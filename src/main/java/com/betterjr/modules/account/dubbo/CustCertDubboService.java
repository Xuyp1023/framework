package com.betterjr.modules.account.dubbo;

import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.account.dubbo.interfaces.ICustCertService;
import com.betterjr.modules.account.entity.CustCertInfo;
import com.betterjr.modules.account.service.CustCertService;

@Service(interfaceClass=ICustCertService.class)
public class CustCertDubboService implements ICustCertService {

	@Autowired
	private CustCertService custCertService;
	@Override
	public CustCertInfo checkValidity(X509Certificate anX509) {
		// TODO Auto-generated method stub
		return custCertService.checkValidity(anX509);
	}
	
    @Override
    public CustCertInfo findFirstCertInfoByOperOrg(String anOperOrg) {
        // TODO Auto-generated method stub
        return custCertService.findFirstCertInfoByOperOrg(anOperOrg);
    }

}

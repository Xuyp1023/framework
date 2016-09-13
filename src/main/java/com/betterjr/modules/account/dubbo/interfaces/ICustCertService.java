package com.betterjr.modules.account.dubbo.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import com.betterjr.modules.account.entity.CustCertInfo;

public interface ICustCertService {

	/**
	 * 
	 * 校验证书公钥是否正确
	 * 
	 * @param 证书公钥信息
	 * @return 成功返回 证书信息，失败抛出异常
	 * @throws 异常情况
	 */
	CustCertInfo checkValidity(X509Certificate anX509);
	
	
    /**
     * 根据机构信息获得这个几个的第一个数字证书
     * 
     * @param anOperOrg
     * @return
     */
    public CustCertInfo findFirstCertInfoByOperOrg(String anOperOrg);

}
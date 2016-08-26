package com.betterjr.modules.account.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.exception.ServiceException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.account.dao.CustCertInfoMapper;
import com.betterjr.modules.account.entity.CustCertInfo;

@Service
public class CustCertService extends BaseService<CustCertInfoMapper, CustCertInfo> {

    @Autowired
    private CustCertInfoMapper certInfoMapper;

    private CustCertInfo createCertInfo(X509Certificate anX509) {
        CustCertInfo certInfo = new CustCertInfo();
        certInfo.setSerialNo(anX509.getSerialNumber().toString());
        certInfo.setVersionUid(anX509.getSigAlgOID());
        certInfo.setSubject(anX509.getSubjectDN().getName());
        try {
            certInfo.setCertInfo(Base64.getEncoder().encodeToString(anX509.getEncoded()));
        }
        catch (CertificateEncodingException e) {
            throw new BytterSecurityException(20203, "createCertInfo has error", e);
        }
        certInfo.setValidDate(BetterDateUtils.formatNumberDate(anX509.getNotAfter()));
        certInfo.setCreateDate(BetterDateUtils.formatNumberDate(anX509.getNotBefore()));
        certInfo.setOperNo("admin");
        certInfo.setStatus("9");
        certInfo.setOperOrg(findOperOrgData(anX509));
        
        return certInfo;
    }

    public static String findOperOrgData(X509Certificate anX509) {
        LdapName ldapDN;
        try {
            ldapDN = new LdapName(anX509.getSubjectX500Principal().getName());

            Map<String, Rdn> map = ReflectionUtils.listConvertToMap(ldapDN.getRdns(), "type");
            StringBuilder sb = new StringBuilder();
            Rdn aa;
            for (String tmpKey : new String[] { "O", "OU" }) {
                aa = map.get(tmpKey);
                if (aa != null) {
                    sb.append(aa.getValue()).append(";");
                }
            }
            if (sb.length() > 10) {
                sb.setLength(sb.length() - 1);
                return sb.toString();
            }
            else {
                return anX509.getSubjectDN().getName();
            }
        }
        catch (InvalidNameException e) {
            return anX509.getSubjectDN().getName();
        }

    }

    /**
     * 
     * 通过证书公钥信息产生证书信息
     * 
     * @param 公钥文件
     * @throws 异常情况
     */
    public void saveFromFile(File anFile) throws GeneralSecurityException, FileNotFoundException {
        FileInputStream fileIn = new FileInputStream(anFile);
        saveFromFile(fileIn);
    }

    public void saveFromFile(InputStream inFile) throws GeneralSecurityException, FileNotFoundException {
        CertificateFactory certificate_factory = CertificateFactory.getInstance("X.509");
        X509Certificate x509 = (X509Certificate) certificate_factory.generateCertificate(inFile);
        CustCertInfo certInfo = createCertInfo(x509);
        this.insert(certInfo);
    }

    public void saveFromFile(String inFile, String anPass) throws GeneralSecurityException, IOException {

        KeyStore ks = null;
        if (inFile.toLowerCase().endsWith(".p12") || inFile.toLowerCase().endsWith(".pfx")) {
            ks = KeyStore.getInstance("PKCS12");
        }
        else {
            ks = KeyStore.getInstance("JKS");
        }
        FileInputStream fis = new FileInputStream(inFile);
        ks.load(fis, anPass.toCharArray());
        String alias = ks.aliases().nextElement();
        X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
        CustCertInfo certInfo = createCertInfo(x509);
        this.insert(certInfo);
    }

    /**
     * 
     * 客户开户成功后，更新证书中的客户资料信息；开户成功、变更客户关键信息，变更默认经办人需要更新该值
     * 
     * @param 证书信息
     * @return 是否成功更新证书中的客户资料信息
     * @throws 异常情况
     */
    public boolean updateCustInfo(CustCertInfo anCertInfo) {
        if (this.updateByPrimaryKey(anCertInfo) == 1) {
            return true;
        }

        return false;
    }

    /**
     * 
     * 校验证书公钥是否正确
     * 
     * @param 证书公钥信息
     * @return 成功返回 证书信息，失败抛出异常
     * @throws 异常情况
     */
    public CustCertInfo checkValidity(X509Certificate anX509) {
        CustCertInfo certInfo = this.selectByPrimaryKey(anX509.getSerialNumber().toString());
        CustCertInfo requestCertInfo = createCertInfo(anX509);
        if (certInfo.validCertInfo(requestCertInfo)) {
            return certInfo;
        }
        else {
            throw new ServiceException(20001, "证书验证失败");
        }
    }

    public CustCertInfo findCertByToken(String anCustToken) {
        List<CustCertInfo> list = selectByProperty("token", anCustToken);
        if (list.size() == 1) {
            return list.get(0);
        }
        else {
            return new CustCertInfo();
        }
    }

    /**
     * 
     * 创建首次访问产生的Token
     * 
     * @param 证书信息
     * @return 动态创建的Token
     * @throws 异常情况
     */
    public int updateToken(CustCertInfo anCertInfo) {
        anCertInfo.setStatus("0");
        logger.info(anCertInfo.toString());
        return this.updateByPrimaryKeySelective(anCertInfo);
        /*
         * String token = Base64.getEncoder().encodeToString(Digests.generateSalt(64)); anCertInfo.setToken(token);
         * 
         * return this.sqlMapper.update(
         * "update T_CUST_CERTINFO a set a.c_status ='0', a.c_token = #{token} where a.c_subject = #{subject} and a.c_status='9'", anCertInfo);
         */
    }

    /**
     * 根据operOrg 取到CertInfo
     * @param anOperOrg
     * @return
     */
    public CustCertInfo findCertByOperOrg(String anOperOrg) {
        BTAssert.notNull(anOperOrg);
        return Collections3.getFirst(this.selectByProperty("operOrg", anOperOrg));
    }
}
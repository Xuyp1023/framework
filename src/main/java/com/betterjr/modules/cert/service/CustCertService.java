package com.betterjr.modules.cert.service;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.exception.ServiceException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.cert.dao.CustCertInfoMapper;
import com.betterjr.modules.cert.entity.BetterX509CertInfo;
import com.betterjr.modules.cert.entity.CustCertInfo;
import com.betterjr.modules.cert.utils.BetterX509CertStore;

@Service
public class CustCertService extends BaseService<CustCertInfoMapper, CustCertInfo> {
    private final String[] QUERY_TERM = new String[] { "status", "GTEregDate", "LTEregDate", "GTEcreateDate", "LTEvalidDate", "contName", "custName" };

    @Autowired
    private BetterX509CertService betterCertService;

    @Resource
    private CustCertRuleService certRuleService;

    private CustCertInfo createCertInfo(final X509Certificate anX509) {
        final CustCertInfo certInfo = new CustCertInfo();
        certInfo.setSerialNo(anX509.getSerialNumber().toString());
        certInfo.setVersionUid(anX509.getSigAlgOID());
        certInfo.setSubject(anX509.getSubjectDN().getName());
        try {
            certInfo.setCertInfo(Base64.getEncoder().encodeToString(anX509.getEncoded()));
        }
        catch (final CertificateEncodingException e) {
            throw new BytterSecurityException(20203, "createCertInfo has error", e);
        }
        certInfo.setValidDate(BetterDateUtils.formatNumberDate(anX509.getNotAfter()));
        certInfo.setCreateDate(BetterDateUtils.formatNumberDate(anX509.getNotBefore()));
        certInfo.setOperNo("admin");
        certInfo.setStatus("9");
        certInfo.setOperOrg(findOperOrgData(anX509));

        return certInfo;
    }

    /**
     * 发布数字证书
     *
     * @param anSerialNo
     *            证书序列号
     * @param anPublishMode
     *            发布模式
     * @return
     */
    public Map<String, Object> savePublishCert(final String anSerialNo, final String anPublishMode) {
        final Map result = new HashMap();
        final CustCertInfo certInfo = findBySerialNo(anSerialNo);
        BTAssert.notNull(certInfo, "数字证书信息不能为空！");

        final String password = createPassword(certInfo.getEmail());
        final BetterX509CertStore certStore = betterCertService.savePublishX509Cert(certInfo.getCertId(), password);
        BTAssert.notNull(certStore, String.format("发布数字证书时，不能获取有效的数字证书信息；可能数字证书【%s】已经投入使用", certInfo.getSubject()));
        final X509Certificate x509Certinfo = certStore.findCertificate();
        certInfo.setVersionUid(x509Certinfo.getSigAlgOID());
        certInfo.setValidDate(BetterDateUtils.formatNumberDate(x509Certinfo.getNotAfter()));
        certInfo.setCreateDate(BetterDateUtils.formatNumberDate(x509Certinfo.getNotBefore()));
        certInfo.setSubject(x509Certinfo.getSubjectDN().getName());
        try {
            certInfo.setCertInfo(Base64.getEncoder().encodeToString(x509Certinfo.getEncoded()));
        }
        catch (final CertificateEncodingException e) {

        }
        certInfo.publishModifyValue(createToken(80), anPublishMode);
        certInfo.modifyValue(UserUtils.getOperatorInfo());
        certInfo.setStatus("3");
        this.updateByPrimaryKey(certInfo);
        return result;
    }

    /**
     * 下载颁发的数字证书
     *
     * @param anToken
     *            数字证书关联的token
     * @return
     */
    public byte[] saveDownloadCert(final String anToken) {
        if (BetterStringUtils.isBlank(anToken) || BetterStringUtils.trim(anToken).length() < 80) {
            return new byte[0];
        }
        final List<CustCertInfo> tmpList = selectByProperty("token", anToken);
        final CustCertInfo certInfo = Collections3.getFirst(tmpList);
        if (certInfo == null || tmpList.size() > 1 || certInfo.invalidDownload()) {

            return new byte[0];
        }
        final byte[] bbs = this.betterCertService.downloadPublishCert(certInfo.getCertId(), certInfo.getSerialNo());
        if (Collections3.isEmptyObject(bbs) == false) {
            certInfo.setStatus("0");
            this.updateByPrimaryKey(certInfo);
        }
        return bbs;
    }

    public static String createToken(final int anLength) {
        if (anLength <= 32) {
            return SerialGenerator.uuid();
        }
        else {
            return SerialGenerator.uuid() + SerialGenerator.randomBase62(anLength - 32);
        }
    }

    protected static String createPassword(final String anComplexValue) {
        final String tmpStr = BetterStringUtils.createRandomCharAndNum(8);
        if (BetterStringUtils.isNotBlank(anComplexValue) && (anComplexValue.length() > 6)) {

            return tmpStr.concat(anComplexValue.substring(0, 6));
        }
        else {
            return BetterStringUtils.createRandomCharAndNum(12);
        }
    }

    /**
     * 数字证书未发布前，客户证书作废处理
     *
     * @param anSerialNo
     *            数字证书序列号
     * @param anReason
     *            作废原因
     */
    public void saveCancelCustCert(final String anSerialNo, final String anReason) {
        final CustCertInfo certInfo = this.findBySerialNo(anSerialNo);
        if (certInfo != null) {
            certInfo.setStatus("8");
            certInfo.setSerialNo("#" + certInfo.getSerialNo() + ";" + Integer.toString(SerialGenerator.randomInt(10000)));
            certInfo.setCertId(-certInfo.getCertId());
            certInfo.setDescription(anReason);
            certInfo.modifyValue(UserUtils.getOperatorInfo());
            this.updateByPrimaryKey(certInfo);
        }
    }

    /**
     * 保存客户数字证书信息
     *
     * @param anMap
     *            数字证书入参信息
     * @return
     */
    public CustCertInfo saveCustCertInfo(final CustCertInfo anCertInfo) {

        return saveCustCertInfo(anCertInfo, false);
    }

    private CustCertInfo saveCustCertInfo(final CustCertInfo anCertInfo, final boolean anCreate) {
        final CustCertInfo tmpCertInfo = this.selectByPrimaryKey(anCertInfo.getSerialNo());
        if (tmpCertInfo == null) {
            anCertInfo.initValue(UserUtils.getOperatorInfo());
            anCertInfo.setStatus("0");
            this.insert(anCertInfo);
        }
        else {
            BTAssert.isTrue(anCreate == false, "创建客户数字证书时，选择的数字证书已经存在！");
            anCertInfo.modifyValue(UserUtils.getOperatorInfo(), tmpCertInfo);
            this.updateByPrimaryKey(anCertInfo);
        }

        return anCertInfo;
    }

    /**
     * 增加客户数字证书信息
     *
     * @param anMap
     * @return
     */
    public CustCertInfo addCustCertInfo(final Map anMap) {
        final CustCertInfo certInfo = BeanMapper.map(anMap, CustCertInfo.class);
        return saveCustCertInfo(certInfo, true);
    }

    /**
     * 分页查询客户数字证书信息
     *
     * @param anParam
     *            查询条件
     * @param anPageNum
     *            页码
     * @param anPageSize
     *            分页大小
     * @param anFlag
     *            是否需要计算
     * @return
     */
    public List<CustCertInfo> queryCustCertInfo(final Map<String, Object> anParam, final int anPageNum, final int anPageSize, final String anFlag) {
        final Map termMap = Collections3.fuzzyMap(Collections3.filterMap(anParam, QUERY_TERM), new String[] { "contName", "custName" });

        return this.selectPropertyByPage(termMap, anPageNum, anPageSize, "1".equals(anFlag));
    }

    /**
     * 根据数字证书序列号，查找客户数字证书信息
     *
     * @param anSerialNo
     *            序列号
     * @return
     */
    public CustCertInfo findBySerialNo(final String anSerialNo) {

        return this.selectByPrimaryKey(anSerialNo);
    }

    /**
     * 根据Role 列表查询对应的 OperOrg 集合
     *
     * @param anRules
     * @return
     */
    public Set<String> queryOperOrgByRoles(final String... anRoles) {
        final Set<String> operOrgSet = new HashSet<>();

        for (final String role: anRoles) {
            operOrgSet.addAll(this.selectByProperty("LIKEruleList", "%" + role + "%").stream().map(certInfo -> certInfo.getOperOrg())
                    .collect(Collectors.toSet()));
        }

        return operOrgSet;
    }

    /**
     * 从数字证书中查找客户数字证书相关的信息
     *
     * @param anSerialNo
     *            序列号
     * @return
     */
    public CustCertInfo findCustCertFromX509Cert(final String anParam) {
        try {
            final String[] arrParam = anParam.split(";|,");
            final Long certId = Long.parseLong(arrParam[0]);
            final String serialNo = arrParam[1];
            return findCustCertFromX509Cert(certId, serialNo);
        }
        catch (final Exception ex) {
            return null;
        }
    }

    public CustCertInfo findCustCertFromX509Cert(final Long anCerId, final String anSerialNo) {
        final BetterX509CertInfo x509CertInfo = betterCertService.findX509CertInfo(anCerId, anSerialNo);
        final CustCertInfo certInfo = new CustCertInfo();
        BTAssert.notNull(x509CertInfo, "数字证书信息不能为空！");
        if (x509CertInfo != null) {
            BeanMapper.copy(x509CertInfo, certInfo);
            certInfo.setCertId(x509CertInfo.getId());
            certInfo.setCustName(x509CertInfo.getOrgName());
            certInfo.setOperOrg(x509CertInfo.findOperOrg());
            certInfo.setSubject(BetterX509CertService.buildSubjectDN(x509CertInfo));
        }

        return certInfo;
    }

    public static String findOperOrgData(final X509Certificate anX509) {
        LdapName ldapDN;
        try {
            ldapDN = new LdapName(anX509.getSubjectX500Principal().getName());

            final Map<String, Rdn> map = ReflectionUtils.listConvertToMap(ldapDN.getRdns(), "type");
            final StringBuilder sb = new StringBuilder();
            Rdn aa;
            for (final String tmpKey : new String[] { "O", "OU" }) {
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
        catch (final InvalidNameException e) {
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
    public void saveFromFile(final File anFile) throws GeneralSecurityException, FileNotFoundException {
        final FileInputStream fileIn = new FileInputStream(anFile);
        saveFromFile(fileIn);
    }

    public void saveFromFile(final InputStream inFile) throws GeneralSecurityException, FileNotFoundException {
        final CertificateFactory certificate_factory = CertificateFactory.getInstance("X.509");
        final X509Certificate x509 = (X509Certificate) certificate_factory.generateCertificate(inFile);
        final CustCertInfo certInfo = createCertInfo(x509);
        this.insert(certInfo);
    }

    public void saveFromFile(final String inFile, final String anPass) throws GeneralSecurityException, IOException {

        KeyStore ks = null;
        if (inFile.toLowerCase().endsWith(".p12") || inFile.toLowerCase().endsWith(".pfx")) {
            ks = KeyStore.getInstance("PKCS12");
        }
        else {
            ks = KeyStore.getInstance("JKS");
        }
        final FileInputStream fis = new FileInputStream(inFile);
        ks.load(fis, anPass.toCharArray());
        final String alias = ks.aliases().nextElement();
        final X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
        final CustCertInfo certInfo = createCertInfo(x509);
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
    public boolean updateCustInfo(final CustCertInfo anCertInfo) {
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
    public CustCertInfo checkValidity(final X509Certificate anX509) {
        final CustCertInfo certInfo = this.selectByPrimaryKey(anX509.getSerialNumber().toString());
        final CustCertInfo requestCertInfo = createCertInfo(anX509);
        if (certInfo.validCertInfo(requestCertInfo)) {
            // 找回Rule列表
            certInfo.setCertRuleList(certRuleService.queryCertRuleListBySerialNo(certInfo.getSerialNo()));
            return certInfo;
        }
        else {
            throw new ServiceException(20001, "证书验证失败");
        }
    }

    public CustCertInfo findCertByToken(final String anCustToken) {
        final List<CustCertInfo> list = selectByProperty("token", anCustToken);
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
    public int updateToken(final CustCertInfo anCertInfo) {
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
     * 根据机构信息获得这个几个的第一个数字证书
     *
     * @param anOperOrg
     * @return
     */
    public CustCertInfo findFirstCertInfoByOperOrg(final String anOperOrg) {
        if (BetterStringUtils.isBlank(anOperOrg)) {

            return null;
        }
        return Collections3.getFirst(selectByProperty("operOrg", anOperOrg, "serialNo"));
    }

    /**
     * 根据operOrg 取到CertInfo
     *
     * @param anOperOrg
     * @return
     */
    public CustCertInfo findCertByOperOrg(final String anOperOrg) {
        BTAssert.notNull(anOperOrg);
        return Collections3.getFirst(this.selectByProperty("operOrg", anOperOrg));
    }
}
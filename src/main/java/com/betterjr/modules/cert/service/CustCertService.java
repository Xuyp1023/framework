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
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.exception.ServiceException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.security.PasswordCreate;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.cert.dao.CustCertInfoMapper;
import com.betterjr.modules.cert.dubbo.CustCertNotificationService;
import com.betterjr.modules.cert.entity.BetterX509CertInfo;
import com.betterjr.modules.cert.entity.CustCertInfo;
import com.betterjr.modules.cert.entity.CustCertRule;
import com.betterjr.modules.cert.utils.BetterX509CertStore;
import com.betterjr.modules.operator.service.OperatorRequestService;
import com.betterjr.modules.role.service.RoleService;

@Service
public class CustCertService extends BaseService<CustCertInfoMapper, CustCertInfo> {
    private final static Pattern COMMA_PATTERN = Pattern.compile(",");

    private final static PasswordCreate passwordCreate = new PasswordCreate();

    private final static String[] QUERY_TERM = new String[] { "status", "GTEregDate", "LTEregDate", "GTEcreateDate",
            "LTEvalidDate", "contName", "custName" };

    @Autowired
    private BetterX509CertService betterCertService;

    @Resource
    private CustCertRuleService certRuleService;

    @Resource
    private OperatorRequestService operatorRequestService;

    @Resource
    private RoleService roleService;

    @Resource
    private CustCertNotificationService certNotificationService;

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
    public CustCertInfo savePublishCert(final String anSerialNo, final String anPublishMode) {
        final CustCertInfo certInfo = findBySerialNo(anSerialNo);
        BTAssert.notNull(certInfo, "没有找到客户证书信息！");

        final String certPassword = createPassword(certInfo.getEmail());
        final String loginPassword = createPassword(certInfo.getEmail());

        final BetterX509CertStore certStore = betterCertService.savePublishX509Cert(certInfo.getCertId(), certPassword);
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

        // 添加 admin
        operatorRequestService.addDefaultOperator(certInfo.getOperOrg(), "管理员", loginPassword);

        // 添加 default role
        roleService.addDefaultRole(certInfo.getOperOrg());

        certNotificationService.sendNotification(certInfo, anPublishMode, certPassword, loginPassword);

        return certInfo;
    }

    /**
     * 下载颁发的数字证书
     *
     * @param anToken
     *            数字证书关联的token
     * @return
     */
    public byte[] saveDownloadCert(final String anToken) {
        if (StringUtils.isBlank(anToken) || StringUtils.trim(anToken).length() < 80) {
            return new byte[0];
        }
        final List<CustCertInfo> tmpList = selectByProperty("token", anToken);
        final CustCertInfo certInfo = Collections3.getFirst(tmpList);
        if (certInfo == null || tmpList.size() > 1 || certInfo.invalidDownload()) {

            return new byte[0];
        }
        final byte[] bbs = this.betterCertService.downloadPublishCert(certInfo.getCertId(), certInfo.getSerialNo());
        if (Collections3.isEmptyObject(bbs) == false) {
            certInfo.setStatus("9");
            this.updateByPrimaryKey(certInfo);
        }
        return bbs;
    }

    public static String createToken(final int anLength) {
        if (anLength <= 32) {
            return SerialGenerator.uuid();
        } else {
            return SerialGenerator.uuid() + SerialGenerator.randomBase62(anLength - 32);
        }
    }

    public static String getRandomString(final int length) {
        final StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz");
        final StringBuffer sb = new StringBuffer();
        final Random r = new Random();
        final int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }

    protected static String createPassword(final String anComplexValue) {

        return passwordCreate.createPassWord(8);
        /*final String tmpStr = BetterStringUtils.createRandomCharAndNum(8);
        if (BetterStringUtils.isNotBlank(anComplexValue) && (anComplexValue.length() > 6)) {
        
            return tmpStr.concat(anComplexValue.substring(0, 6));
        }
        else {
            return BetterStringUtils.createRandomCharAndNum(12);
        }*/
    }

    /**
     * 数字证书未发布前，客户证书作废处理
     *
     * @param anSerialNo
     *            数字证书序列号
     * @param anReason
     *            作废原因
     */
    public void saveCancelCustCert(final String anSerialNo) {
        final CustCertInfo certInfo = this.findBySerialNo(anSerialNo);
        if (certInfo != null) {
            // 检查是否可以作废
            BTAssert.isTrue(StringUtils.equals(certInfo.getStatus(), "1"), "证书状态不允许作废！");

            betterCertService.saveCertStatus(certInfo.getCertId(), anSerialNo, "0"); // 释放数字证书

            certRuleService.saveDelCertRuleBySerialNo(certInfo.getSerialNo()); // 删除关联关系

            this.delete(certInfo); // 删除当前数字证书
        } else {
            throw new BytterTradeException("没有找到对应的数字证书！");
        }
    }

    /**
     * 保存来自微信的证书，删除之前的certInfo, 保存新的 certInfo
     * @param anTempCertInfo
     * @param anCertInfo
     * @return
     */
    public Object saveCustCertInfo(final CustCertInfo anTempCertInfo, final CustCertInfo anCertInfo) {

        anCertInfo.setOperOrg(anTempCertInfo.getOperOrg());

        final CustCertInfo certInfo = saveCustCertInfo(anCertInfo, false);

        return certInfo;
    }

    /**
     * @param anTempCertInfo
     */
    private void saveDelOrginCertInfo(final String anSerialNo) {
        certRuleService.saveDelCertRuleBySerialNo(anSerialNo);
        this.deleteByPrimaryKey(anSerialNo);
    }

    /**
     * 保存客户数字证书信息
     *
     * @param anMap
     *            数字证书入参信息
     * @return
     */
    public CustCertInfo saveCustCertInfo(final CustCertInfo anCertInfo) {
        // X509 find certificate

        return saveCustCertInfo(anCertInfo, false);
    }

    /**
     * @param anRuleList
     */
    private void validateRuleList(final String[] anRules) {
        final List<String> ruleList = Arrays.asList("CORE_USER", "SUPPLIER_USER", "FACTOR_USER", "SELLER_USER");

        BTAssert.isTrue(anRules.length != 0, "默认角色必需输入");

        for (final String rule : anRules) {
            BTAssert.isTrue(ruleList.contains(rule), "默认角色的可选值为：CORE_USER, SUPPLIER_USER, FACTOR_USER, SELLER_USER");
        }
    }

    private CustCertInfo saveCustCertInfo(final CustCertInfo anCertInfo, final boolean anCreate) {
        // 校验角色列表
        final String[] rules = COMMA_PATTERN.split(anCertInfo.getRuleList());
        validateRuleList(rules);

        final String serialNo = anCertInfo.getSerialNo();
        final String custName = anCertInfo.getCustName();

        final CustCertInfo tmpCertInfo = this.selectByPrimaryKey(anCertInfo.getSerialNo());
        if (tmpCertInfo == null) {
            anCertInfo.initValue(UserUtils.getOperatorInfo());
            if (!StringUtils.equals(anCertInfo.getStatus(), "8")) {
                anCertInfo.setStatus("1");
            }
            this.insert(anCertInfo);

            for (final String rule : rules) {
                certRuleService.addCustCertRule(serialNo, rule, custName);
            }
        } else {
            BTAssert.isTrue(anCreate == false, "创建客户数字证书时，选择的数字证书已经存在！");

            final String orginSerialNo = anCertInfo.getSerialNo();
            final Long orginCertId = tmpCertInfo.getCertId();
            if (StringUtils.equals(tmpCertInfo.getStatus(), "8")) {
                final BetterX509CertInfo x509CertInfo = betterCertService.findX509CertInfo(anCertInfo.getCertId());

                BTAssert.notNull(x509CertInfo, "找不到相应的数字证书！");
                BTAssert.isTrue(StringUtils.equals(x509CertInfo.getCertStatus(), "0"), "数字证书已使用！");
                final CustCertInfo tempCertInfo = this.findBySerialNo(x509CertInfo.getSerialNo());
                BTAssert.isNull(tempCertInfo, "该数字证书已经使用！");

                tmpCertInfo.setSerialNo(x509CertInfo.getSerialNo());
                tmpCertInfo.setCreateDate(x509CertInfo.getCreateDate());
                tmpCertInfo.setValidDate(x509CertInfo.getValidDate());
                tmpCertInfo.setCertId(anCertInfo.getCertId());
            }
            // 两个序列号相同处理
            if (StringUtils.equals(orginSerialNo, tmpCertInfo.getSerialNo())) {
                tmpCertInfo.modifyValue(UserUtils.getOperatorInfo(), anCertInfo);
                this.updateByPrimaryKeySelective(tmpCertInfo);

                // 处理角色关系 certRuleService
                final List<CustCertRule> certRules = certRuleService.queryCertRuleListBySerialNo(serialNo);
                for (final String rule : rules) {
                    final CustCertRule certRule = findCertRule(serialNo, rule, certRules);
                    if (certRule != null) {
                        certRules.remove(certRule);
                    } else {
                        certRuleService.addCustCertRule(serialNo, rule, custName);
                    }
                }

                for (final CustCertRule certRule : certRules) {
                    certRuleService.saveDelCertRule(certRule);
                }
            } else {
                betterCertService.saveCertStatus(orginCertId, orginSerialNo, "0"); // 释放数字证书
                saveDelOrginCertInfo(orginSerialNo);

                betterCertService.saveCertStatus(tmpCertInfo.getCertId(), tmpCertInfo.getSerialNo(), "2"); // 占用数字证书
                tmpCertInfo.initValue(UserUtils.getOperatorInfo());
                tmpCertInfo.setStatus("1");
                this.insert(tmpCertInfo);

                for (final String rule : rules) {
                    certRuleService.addCustCertRule(tmpCertInfo.getSerialNo(), rule, custName);
                }
            }
        }

        return anCertInfo;
    }

    private CustCertRule findCertRule(final String anSerialNo, final String anRule,
            final List<CustCertRule> anCertRules) {

        for (final CustCertRule certRule : anCertRules) {
            if (StringUtils.equals(certRule.getSerialNo(), anSerialNo)
                    && StringUtils.equals(certRule.getRule(), anRule)) {
                return certRule;
            }
        }
        return null;
    }

    /**
     * 增加客户数字证书信息
     *
     * @param anMap
     * @return
     */
    public CustCertInfo addCustCertInfo(final CustCertInfo certInfo) {

        if (certInfo.getCertId() > 0) {
            final BetterX509CertInfo x509CertInfo = betterCertService.findX509CertInfo(certInfo.getCertId());

            BTAssert.notNull(x509CertInfo, "找不到相应的数字证书！");
            BTAssert.isTrue(StringUtils.equals(x509CertInfo.getCertStatus(), "0"), "数字证书已使用！");
            final CustCertInfo tempCertInfo = this.findBySerialNo(x509CertInfo.getSerialNo());
            BTAssert.isNull(tempCertInfo, "该数字证书已经使用！");

            certInfo.setSerialNo(x509CertInfo.getSerialNo());
            certInfo.setCreateDate(x509CertInfo.getCreateDate());
            certInfo.setValidDate(x509CertInfo.getValidDate());

            betterCertService.saveCertStatus(x509CertInfo.getId(), x509CertInfo.getSerialNo(), "2");
        }

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
    public Page<CustCertInfo> queryCustCertInfo(final Map<String, Object> anParam, final int anPageNum,
            final int anPageSize, final String anFlag) {
        final Map termMap = Collections3.fuzzyMap(Collections3.filterMap(anParam, QUERY_TERM),
                new String[] { "contName", "custName" });

        final Page<CustCertInfo> certInfos = this.selectPropertyByPage(termMap, anPageNum, anPageSize,
                "1".equals(anFlag));

        certInfos.forEach(certInfo -> {
            final BetterX509CertInfo x509CertInfo = betterCertService.findX509CertInfo(certInfo.getCertId());
            certInfo.setCommName(x509CertInfo != null ? x509CertInfo.getCommName() : "");
            certInfo.setCertRuleList(certRuleService.queryCertRuleListBySerialNo(certInfo.getSerialNo()));
        });
        return certInfos;
    }

    /**
     * 根据数字证书序列号，查找客户数字证书信息
     *
     * @param anSerialNo
     *            序列号
     * @return
     */
    public CustCertInfo findBySerialNo(final String anSerialNo) {

        final CustCertInfo certInfo = this.selectByPrimaryKey(anSerialNo);

        if (certInfo != null) {
            certInfo.setCertRuleList(certRuleService.queryCertRuleListBySerialNo(certInfo.getSerialNo()));
        }

        return certInfo;
    }

    /**
     * 根据Role 列表查询对应的 OperOrg 集合
     *
     * @param anRules
     * @return
     */
    public Set<String> queryOperOrgByRoles(final String... anRoles) {
        final Set<String> operOrgSet = new HashSet<>();

        for (final String role : anRoles) {
            operOrgSet.addAll(this.selectByProperty("LIKEruleList", "%" + role + "%").stream()
                    .map(certInfo -> certInfo.getOperOrg()).collect(Collectors.toSet()));
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
            } else {
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
        } else {
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
        logger.debug(" checkValidity 1 :" + certInfo);
        if (certInfo.validCertInfo(requestCertInfo)) {
            // 找回Rule列表
            certInfo.setCertRuleList(certRuleService.queryCertRuleListBySerialNo(certInfo.getSerialNo()));

            if (StringUtils.equals(certInfo.getStatus(), "9") == true) { // 首次访问
                certInfo.setStatus("0");
                this.updateByPrimaryKeySelective(certInfo);
            }
            logger.debug(" checkValidity 2 :" + certInfo);
            return certInfo;
        } else {
            throw new ServiceException(20001, "证书验证失败");
        }
    }

    public CustCertInfo findCertByToken(final String anCustToken) {
        final List<CustCertInfo> list = selectByProperty("token", anCustToken);
        if (list.size() == 1) {
            final CustCertInfo certInfo = list.get(0);
            certInfo.setCertRuleList(certRuleService.queryCertRuleListBySerialNo(certInfo.getSerialNo()));
            return certInfo;
        } else {
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
        if (StringUtils.isBlank(anOperOrg)) {

            return null;
        }
        final CustCertInfo certInfo = Collections3.getFirst(selectByProperty("operOrg", anOperOrg, "serialNo"));
        if (certInfo != null) {
            certInfo.setCertRuleList(certRuleService.queryCertRuleListBySerialNo(certInfo.getSerialNo()));
        }
        return certInfo;
    }

    /**
     * 根据operOrg 取到CertInfo
     *
     * @param anOperOrg
     * @return
     */
    public CustCertInfo findCertByOperOrg(final String anOperOrg) {
        BTAssert.notNull(anOperOrg);
        final CustCertInfo certInfo = Collections3.getFirst(this.selectByProperty("operOrg", anOperOrg));
        if (certInfo != null) {
            certInfo.setCertRuleList(certRuleService.queryCertRuleListBySerialNo(certInfo.getSerialNo()));
        }

        return certInfo;
    }

    /**
     * @param anSerialNo
     * @param anReason
     */
    public void saveRevokeCustCertificate(final String anSerialNo, final String anReason) {
        final CustCertInfo certInfo = this.findBySerialNo(anSerialNo);

        BTAssert.notNull(certInfo, "没有找到相应的客户证书！");

        betterCertService.saveRevokeCert(certInfo.getCertId(), anSerialNo, anReason);

        certInfo.setStatus("2");
        this.updateByPrimaryKeySelective(certInfo);
    }

    /**
     * @param anSerialNo
     * @return
     */
    public List<SimpleDataEntity> queryCustCertRoleList(final String anSerialNo) {
        final CustCertInfo certInfo = this.findBySerialNo(anSerialNo);

        BTAssert.notNull(certInfo, "没有找到相应的客户证书！");

        final List<CustCertRule> roleList = certRuleService.queryCertRuleListBySerialNo(anSerialNo);

        return roleList.stream().map(certRole -> new SimpleDataEntity(roleName(certRole.getRule()), certRole.getRule()))
                .collect(Collectors.toList());
    }

    public String roleName(final String anRole) {
        String roleName;
        switch (anRole) {
        case "CORE_USER":
            roleName = "我是买方";
            break;
        case "PLATFORM_USER":
            roleName = "平台角色";
            break;
        case "FACTOR_USER":
            roleName = "我是资金方";
            break;
        case "SELLER_USER":
            roleName = "我是买方";
            break;
        case "SUPPLIER_USER":
            roleName = "我是卖方";
            break;
        default:
            roleName = "";
        }
        return roleName;
    }

}
package com.betterjr.modules.cert.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CRLReason;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.Cryptos;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.cert.dao.BetterX509CertInfoMapper;
import com.betterjr.modules.cert.data.BetterX509CertType;
import com.betterjr.modules.cert.entity.BetterX509CertInfo;
import com.betterjr.modules.cert.utils.BetterX509CertFileStore;
import com.betterjr.modules.cert.utils.BetterX509CertStore;
import com.betterjr.modules.cert.utils.BetterX509CertStreamStore;
import com.betterjr.modules.cert.utils.BetterX509MetaData;
import com.betterjr.modules.cert.utils.BetterX509Utils;
import com.betterjr.modules.sys.service.SysConfigService;

@Service
public class BetterX509CertService extends BaseService<BetterX509CertInfoMapper, BetterX509CertInfo> {

    private final String[] QUERY_TERM = new String[] { "certStatus", "GTEregDate", "LTEregDate", "GTEcreateDate", "LTEvalidDate" };

    /**
     * 发布数字证书
     *
     * @param anId
     * @return
     */
    public BetterX509CertStore savePublishX509Cert(final Long anId, final String anPassword) {
        if (anId == null) {

            return null;
        }

        final BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo == null || " 0, 1".indexOf(certInfo.getCertStatus()) < 0) {

            return null;
        }

        final String keyAlgorithm = BetterX509Utils.KEY_ALGORITHM;

        // 获取中级证书信息
        final BetterX509CertStore middleCertStore = findMiddleCertStore(certInfo.getSigner());

        // 数字证书已经产生，只是重新发布
        /*
         * if ("1".equals(certInfo.getCertStatus())){ return new BetterX509CertStreamStore(middleCertStore, certInfo.getData(),
         * Cryptos.aesDecrypt(certInfo.getPasswd()), certInfo.getCertAlias(), BetterX509CertType.checking( certInfo.getCertType())); }
         */
        logger.info("this is password :" + anPassword);
        final Date now = BetterDateUtils.getNow();
        certInfo.setCreateDate(BetterDateUtils.formatNumberDate(now));
        certInfo.setValidDate(BetterDateUtils.formatNumberDate(BetterDateUtils.addYears(now, certInfo.getYear())));

        final BetterX509MetaData metaData = buildMetaData(certInfo, anPassword);

        final PublicKey pubKey = BetterX509Utils.readPublicKeyFromStream(certInfo.getPublicKey());
        final PrivateKey privKey = BetterX509Utils.readPrivateKeyFromStream(Cryptos.aesDecrypt(certInfo.getPrivateKey()));
        final BetterX509CertStore certStore = BetterX509Utils.newCertificate(metaData, middleCertStore, privKey, pubKey, "D:\\cert\\certs\\13.p12");
        final X509Certificate x509Certinfo = certStore.findCertificate();

        /*  certInfo.setValidDate(BetterDateUtils.formatNumberDate(x509Certinfo.getNotAfter()));
        certInfo.setCreateDate(BetterDateUtils.formatNumberDate(x509Certinfo.getNotBefore()));*/
        certInfo.setData(certStore.readOrignData());
        certInfo.setPasswd(Cryptos.aesEncrypt(anPassword));
        certInfo.setCertStatus("1");
        this.updateByPrimaryKey(certInfo);

        return certStore;
    }

    private BetterX509CertStore findMiddleCertStore(final String anSigner) {

        final Map<String, BetterX509CertStore> middleCertMap = findMiddleCertStore();
        final BetterX509CertStore middleCertStore = middleCertMap.get(anSigner);
        BTAssert.notNull(middleCertStore, "该数字证书没有定义合法的中级证书，不能发表；中级证书别名是:" + anSigner);

        return middleCertStore;
    }

    /**
     * 制作证书
     *
     * @param anId
     * @param anSerialNo
     */
    public void saveMakeCertificate(final Long anId, final String anSerialNo) {
        BTAssert.notNull(anId, "数字证书编号不允许为空！");
        BTAssert.isTrue(BetterStringUtils.isNotBlank(anSerialNo), "数字证书序列号不允许为空！");

        final BetterX509CertInfo certInfo = findX509CertInfo(anId, anSerialNo);
        BTAssert.notNull(certInfo, "没有找到对应的数字证书！");


    }

    /**
     * 作废证书
     *
     * @param anId
     * @param anSerialNo
     */
    public void saveCancelCertificate(final Long anId, final String anSerialNo) {
        BTAssert.notNull(anId, "数字证书编号不允许为空！");
        BTAssert.isTrue(BetterStringUtils.isNotBlank(anSerialNo), "数字证书序列号不允许为空！");

        final BetterX509CertInfo certInfo = findX509CertInfo(anId, anSerialNo);
        BTAssert.notNull(certInfo, "没有找到对应的数字证书！");

        BTAssert.isTrue(BetterStringUtils.equals("0", certInfo.getCertStatus()), "此证书不允许作废！");

        this.saveCertStatus(anId, anSerialNo, "9");
    }

    /**
     * 回收数字证书
     *
     * @param anId
     * @param anSerialNo
     */
    public void saveRevokeCert(final Long anId, final String anSerialNo, final String anReason) {
        BTAssert.notNull(anId, "数字证书编号不允许为空！");
        BTAssert.isTrue(BetterStringUtils.isNotBlank(anSerialNo), "数字证书序列号不允许为空！");
        BTAssert.isTrue(BetterStringUtils.isNotBlank(anReason), "回收原因不允许为空！");

        final BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo.getSerialNo().equals(anSerialNo)) {
            final CRLReason tmpReason = CRLReason.valueOf(anReason);

            // 获取中级证书信息
            final BetterX509CertStore middleCertStore = findMiddleCertStore(certInfo.getSigner());

            final BetterX509CertStore certStore = new BetterX509CertStreamStore(middleCertStore, certInfo.getData(),
                    Cryptos.aesDecrypt(certInfo.getPasswd()), certInfo.getCommName(), BetterX509CertType.checking(certInfo.getCertType()));
            final String revokeFile = SysConfigService.getString("CertificateCRLFile");
            if (BetterX509Utils.revoke(certStore.findCertificate(), tmpReason, middleCertStore, revokeFile)) {
                if (CRLReason.REMOVE_FROM_CRL == tmpReason) {
                    certInfo.setCertStatus("1");
                }
                else {
                    certInfo.setCertStatus("9");
                }
                certInfo.setRevokeReason(anReason);
                this.updateByPrimaryKey(certInfo);
            }
        }
    }

    /**
     * 查找一个数字证书
     *
     * @param anId
     *            数字证书的ID
     * @return
     */
    public BetterX509CertInfo findX509CertInfo(final Long anId) {
        final BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        return certInfo;
    }

    /**
     * 查找一个数字证书
     *
     * @param anId
     *            数字证书的ID
     * @param anSerialNo
     *            数字证书序列号
     * @return
     */
    public BetterX509CertInfo findX509CertInfo(final Long anId, final String anSerialNo) {
        final BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo.getSerialNo().equals(anSerialNo)) {

            return certInfo;
        }
        else {

            throw new BytterTradeException("没有找到对应的数字证书！");
        }
    }

    /**
     * 分页查询数字证书信息
     *
     * @param anParam
     *            查询条件
     * @param anPageNum
     *            页码
     * @param anPageSize
     *            每页条数
     * @param anFlag
     *            是否需要统计
     * @return
     */
    public Page<BetterX509CertInfo> queryX509CertInfo(final Map<String, Object> anParam, final int anPageNum, final int anPageSize, final int anFlag) {
        final Map termMap = Collections3.filterMap(anParam, QUERY_TERM);
        termMap.put("certType", "3");
        return this.selectPropertyByPage(termMap, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * 修改数字证书状态
     *
     * @param anId
     *            流水号
     * @param anSerialNo
     *            数字证书序列号
     * @param anStatus
     *            数字证书状态
     */
    public void saveCertStatus(final Long anId, final String anSerialNo, final String anStatus) {
        final BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo.getSerialNo().equals(anSerialNo)) {
            certInfo.setCertStatus(anStatus);
            this.updateByPrimaryKey(certInfo);
        }

    }

    /**
     * 下载数字证书，只能下载最终数字证书和已经签发的数字证书。
     *
     * @param anId
     *            流水号
     * @param anSerialNo
     *            数字证书序列号
     * @return
     */
    public byte[] downloadPublishCert(final Long anId, final String anSerialNo) {

        final BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo == null) {

            return new byte[0];
        }

        if (certInfo.validDownload(anSerialNo)) {

            return certInfo.getData();
        }
        return new byte[0];
    }

    protected BetterX509MetaData buildMetaData(final BetterX509CertInfo anCertInfo, final String anPassword) {
        final BetterX509MetaData metaData = new BetterX509MetaData(anCertInfo.getCommName(), anPassword);
        metaData.addData(exportMetaOids(anCertInfo), anCertInfo.getEmail(), anCertInfo.getCreateDate(), anCertInfo.getValidDate(),
                anCertInfo.getSerialNo());

        return metaData;
    }

    public static String buildSubjectDN(final BetterX509CertInfo anCertInfo) {
        final X500NameBuilder dnBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        Field field;
        try {
            for (final Map.Entry<String, String> ent : exportMetaOids(anCertInfo).entrySet()) {
                if (BetterStringUtils.isNotBlank(ent.getValue())) {
                    field = BCStyle.class.getField(ent.getKey());
                    final ASN1ObjectIdentifier objectId = (ASN1ObjectIdentifier) field.get(null);
                    dnBuilder.addRDN(objectId, ent.getValue());
                }
            }
        }
        catch (final Exception e) {

        }
        final X500Name dn = dnBuilder.build();

        return dn.toString();
    }

    private static Map<String, String> exportMetaOids(final BetterX509CertInfo anCertInfo) {
        final Map result = new LinkedHashMap();
        result.put("C", anCertInfo.getCountryCode());
        result.put("L", anCertInfo.getCityName());
        result.put("ST", anCertInfo.getProvinceName());
        result.put("O", anCertInfo.getOrgName());
        result.put("OU", anCertInfo.getOrgUnitName());
        result.put("E", anCertInfo.getEmail());
        result.put("CN", anCertInfo.getCommName());

        return result;
    }

    /**
     * 保存从WEB端提交来的数字证书信息
     *
     * @param anCertInfo
     * @return
     */
    public BetterX509CertInfo saveX509CertFromWeb(final BetterX509CertInfo anCertInfo) {
        final KeyPair pair = BetterX509Utils.newRsaKeyPair(BetterX509Utils.KEY_LENGTH);
        anCertInfo.setPublicKey(pair.getPublic().getEncoded());
        anCertInfo.setPrivateKey(Cryptos.aesEncrypt(pair.getPrivate().getEncoded()));

        return saveX509Cert(anCertInfo);
    }

    /**
     * 保存数字证书信息
     *
     * @param anCertInfo
     *            数字证书
     * @param anData
     *            数字证书关联的数据流
     * @return
     */
    public BetterX509CertInfo saveX509Cert(final BetterX509CertInfo anCertInfo) {

        return saveX509Cert(anCertInfo, null);
    }

    public BetterX509CertInfo saveX509Cert(final BetterX509CertInfo anCertInfo, final InputStream anData) {
        try {
            if (BetterStringUtils.isNotBlank(anCertInfo.getPasswd())) {
                anCertInfo.setPasswd(Cryptos.aesEncrypt(anCertInfo.getPasswd()));
            }
            if (anData != null) {
                anCertInfo.setData(IOUtils.toByteArray(anData));
            }
        }
        catch (final IOException e) {
            throw new BytterTradeException(50009, "保存数字证书出现异常", e);
        }
        final BetterX509CertInfo certInfo = Collections3.getFirst(selectByProperty("serialNo", anCertInfo.getSerialNo()));
        if (certInfo == null) {
            CustOperatorInfo operator = null;
            try {
                operator = UserUtils.getOperatorInfo();
            } catch (final Exception e) {
            }
            anCertInfo.initDefValue(operator);
            this.insert(anCertInfo);
        }
        else {
            CustOperatorInfo operator = null;
            try {
                operator = UserUtils.getOperatorInfo();
            } catch(final Exception e) {

            }
            anCertInfo.modifyValue(operator, certInfo);
            this.updateByPrimaryKey(anCertInfo);
        }

        return anCertInfo;
    }

    private BetterX509CertInfo extractCertInfo(final BetterX509CertInfo certInfo, final X509Certificate anX509) {
        final Map<String, String> subItemMap = BetterX509Utils.findCertificateSubjectMap(anX509);
        certInfo.setCityName(subItemMap.get("L"));
        certInfo.setProvinceName(subItemMap.get("S"));
        if (BetterStringUtils.isBlank(certInfo.getProvinceName())) {
            certInfo.setProvinceName(subItemMap.get("ST"));
        }
        certInfo.setCommName(subItemMap.get("CN"));
        certInfo.setCountryCode(subItemMap.get("C"));
        certInfo.setEmail(subItemMap.get("E"));
        certInfo.setOrgName(subItemMap.get("O"));
        certInfo.setOrgUnitName(subItemMap.get("OU"));
        certInfo.setSerialNo(anX509.getSerialNumber().toString());
        certInfo.setPublicKey(anX509.getPublicKey().getEncoded());
        certInfo.setValidDate(BetterDateUtils.formatNumberDate(anX509.getNotAfter()));
        certInfo.setCreateDate(BetterDateUtils.formatNumberDate(anX509.getNotBefore()));

        return certInfo;
    }

    /**
     * 保存数字证书文件到数据库
     *
     * @param anType
     *            数字证书类型
     * @param anPassword
     *            数字证书密码
     * @param anFilePath
     *            数字证书文件路径
     * @return
     */
    public BetterX509CertInfo saveX509CertInfoFromFile(final String anFilePath, final String anPassword) {
        final BetterX509CertFileStore fileStore = new BetterX509CertFileStore(null, anFilePath, anPassword, null);
        final BetterX509CertInfo certInfo = extractCertInfo(new BetterX509CertInfo(), fileStore.findCertificate());
        certInfo.setPasswd(anPassword);
        certInfo.setSigner(fileStore.findSigner());
        certInfo.setCertAlias(fileStore.findCertAlias());
        certInfo.setCertType(fileStore.findCertType().getValue());
        // certInfo.initDefValue(UserUtils.getOperatorInfo());
        certInfo.setPrivateKey(Cryptos.aesEncrypt(fileStore.findPrivateKeyEncode()));
        certInfo.setData(fileStore.readOrignData());

        return saveX509Cert(certInfo);
    }

    private Map<String, BetterX509CertStore> buildRootCertStore(final Map<String, BetterX509CertInfo> anRootMap) {
        final Map<String, BetterX509CertStore> result = new HashMap<String, BetterX509CertStore>();
        BetterX509CertInfo certInfo;
        for (final Map.Entry<String, BetterX509CertInfo> ent : anRootMap.entrySet()) {
            certInfo = ent.getValue();
            result.put(ent.getKey(), new BetterX509CertStreamStore(null, certInfo.getData(), null, certInfo.getCertAlias(), BetterX509CertType.ROOT_CA));
        }
        return result;
    }

    /**
     * 查找中级数字证书信息，用于增加数字证书时颁发者列表
     *
     * @return
     */
    public List<SimpleDataEntity> findMiddleX509Cert() {

        return findX509CertByType("1", "1");
    }

    /**
     * 查找未制作的数字证书。
     *
     * @return
     */
    public List<SimpleDataEntity> findEndX509Cert() {

        return findX509CertByType("3", "0");
    }

    private List<SimpleDataEntity> findX509CertByType(final String anCertType, final String anStatus) {
        final List<SimpleDataEntity> result = new ArrayList<>();
        final Map<String, Object> termMap = QueryTermBuilder.newInstance().put("certType", anCertType).put("certStatus", anStatus).build();

        for (final BetterX509CertInfo certInfo : this.selectByProperty(termMap)) {
            if (anCertType.equals("3")) {
                result.add(new SimpleDataEntity(certInfo.getCommName(), Long.toString(certInfo.getId()) + ";" + certInfo.getSerialNo()));
            }
            else {
                result.add(new SimpleDataEntity(certInfo.getCertAlias(), certInfo.getCommName()));
            }
        }

        return result;
    }

    /**
     * 查找中级数字证书，用于数字证书的颁发
     *
     * @return
     */
    public Map<String, BetterX509CertStore> findMiddleCertStore() {
        final Map<String, BetterX509CertStore> rootMap = buildRootCertStore(
                ReflectionUtils.listConvertToMap(selectByProperty("certType", "0"), "certAlias"));
        final Map<String, BetterX509CertStore> result = new LinkedHashMap();
        BetterX509CertStore rootStore;
        for (final BetterX509CertInfo certInfo : this.selectByProperty("certType", "1")) {
            rootStore = rootMap.get(certInfo.getSigner());
            if (rootStore != null) {
                result.put(certInfo.getCertAlias(), new BetterX509CertStreamStore(rootStore, certInfo.getData(),
                        Cryptos.aesDecrypt(certInfo.getPasswd()), certInfo.getCertAlias(), BetterX509CertType.MIDDLE_CA));
            }
        }

        return result;
    }
}
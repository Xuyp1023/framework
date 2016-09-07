package com.betterjr.modules.cert.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CRLReason;
import java.security.cert.X509Certificate;

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
import com.betterjr.modules.cert.dao.BetterX509CertInfoMapper;
import com.betterjr.modules.cert.data.BetterX509CertType;
import com.betterjr.modules.cert.entity.BetterX509CertInfo;
import com.betterjr.modules.cert.utils.BetterX509CertFileStore;
import com.betterjr.modules.cert.utils.BetterX509CertStore;
import com.betterjr.modules.cert.utils.BetterX509CertStreamStore;
import com.betterjr.modules.cert.utils.BetterX509MetaData;
import com.betterjr.modules.cert.utils.BetterX509Utils;
import com.betterjr.modules.sys.service.SysConfigService;

import java.util.*;

@Service
public class BetterX509CertService extends BaseService<BetterX509CertInfoMapper, BetterX509CertInfo> {

    private final String[] QUERY_TERM = new String[] { "certStatus", "GTEregDate", "LTEregDate", "GTEcreateDate", "LTEvalidDate" };

    /**
     * 发布数字证书
     * 
     * @param anId
     * @return
     */
    public BetterX509CertStore savePublishX509Cert(Long anId, String anPassword) {
        if (anId == null) {

            return null;
        }

        BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo == null || " 0, 1".indexOf(certInfo.getCertStatus()) < 0) {

            return null;
        }

        // 获取中级证书信息
        BetterX509CertStore middleCertStore = findMiddleCertStore(certInfo.getSigner());

        // 数字证书已经产生，只是重新发布
        /*
         * if ("1".equals(certInfo.getCertStatus())){ return new BetterX509CertStreamStore(middleCertStore, certInfo.getData(),
         * Cryptos.aesDecrypt(certInfo.getPasswd()), certInfo.getCertAlias(), BetterX509CertType.checking( certInfo.getCertType())); }
         */
        logger.info("this is password :" + anPassword);
        BetterX509MetaData metaData = buildMetaData(certInfo, anPassword);

        PublicKey pubKey = BetterX509Utils.readPublicKeyFromStream(certInfo.getPublicKey());
        PrivateKey privKey = BetterX509Utils.readPrivateKeyFromStream(Cryptos.aesDecrypt(certInfo.getPrivateKey()));
        BetterX509CertStore certStore = BetterX509Utils.newCertificate(metaData, middleCertStore, privKey, pubKey, "D:\\cert\\certs\\13.p12");
        X509Certificate x509Certinfo = certStore.findCertificate();

        certInfo.setValidDate(BetterDateUtils.formatNumberDate(x509Certinfo.getNotAfter()));
        certInfo.setCreateDate(BetterDateUtils.formatNumberDate(x509Certinfo.getNotBefore()));
        certInfo.setData(certStore.readOrignData());
        certInfo.setPasswd(Cryptos.aesEncrypt(anPassword));
        certInfo.setCertStatus("1");
        this.updateByPrimaryKey(certInfo);

        return certStore;
    }

    private BetterX509CertStore findMiddleCertStore(String anSigner) {

        Map<String, BetterX509CertStore> middleCertMap = findMiddleCertStore();
        BetterX509CertStore middleCertStore = middleCertMap.get(anSigner);
        BTAssert.notNull(middleCertStore, "该数字证书没有定义合法的中级证书，不能发表；中级证书别名是:" + anSigner);

        return middleCertStore;
    }

    /**
     * 回收数字证书
     * 
     * @param anId
     * @param anSerialNo
     */
    public void saveRevokeCert(Long anId, String anSerialNo, String anReason) {
        BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo.getSerialNo().equals(anSerialNo)) {
            CRLReason tmpReason = CRLReason.valueOf(anReason);

            // 获取中级证书信息
            BetterX509CertStore middleCertStore = findMiddleCertStore(certInfo.getSigner());

            BetterX509CertStore certStore = new BetterX509CertStreamStore(middleCertStore, certInfo.getData(),
                    Cryptos.aesDecrypt(certInfo.getPasswd()), certInfo.getCommName(), BetterX509CertType.checking(certInfo.getCertType()));
            String revokeFile = SysConfigService.getString("CertificateCRLFile");
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
     * @param anSerialNo
     *            数字证书序列号
     * @return
     */
    public BetterX509CertInfo findX509CertInfo(Long anId, String anSerialNo) {
        BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo.getSerialNo().equals(anSerialNo)) {

            return certInfo;
        }
        else {

            return new BetterX509CertInfo();
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
    public Page<BetterX509CertInfo> queryX509CertInfo(Map<String, Object> anParam, int anPageNum, int anPageSize, int anFlag) {
        Map termMap = Collections3.filterMap(anParam, QUERY_TERM);
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
    public void saveCertStatus(Long anId, String anSerialNo, String anStatus) {
        BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo.getSerialNo().equals(anSerialNo)) {
            certInfo.setCertStatus(anStatus);
            this.updateByPrimaryKey(certInfo);
        }

    }
    
    /**
     * 制作证书
     * @param anId
     * @param anSerialNo
     */
    public void saveMakeCertificate(Long anId, String anSerialNo) {
        
    }
    
    
    /**
     * 作废证书
     * @param anId
     * @param anSerialNo
     */
    public void saveCancelCertificate(Long anId, String anSerialNo) {
        
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
    public byte[] downloadPublishCert(Long anId, String anSerialNo) {

        BetterX509CertInfo certInfo = this.selectByPrimaryKey(anId);
        if (certInfo == null) {

            return new byte[0];
        }

        if ( certInfo.validDownload(anSerialNo)) {

            return certInfo.getData();
        }
        return new byte[0];
    }

    protected BetterX509MetaData buildMetaData(BetterX509CertInfo anCertInfo, String anPassword) {
        BetterX509MetaData metaData = new BetterX509MetaData(anCertInfo.getCommName(), anPassword);
        metaData.addData(exportMetaOids(anCertInfo), anCertInfo.getEmail(), anCertInfo.getCreateDate(), anCertInfo.getValidDate(),
                anCertInfo.getSerialNo());

        return metaData;
    }

    public static String buildSubjectDN(BetterX509CertInfo anCertInfo) {
        X500NameBuilder dnBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        Field field;
        try {
            for (Map.Entry<String, String> ent : exportMetaOids(anCertInfo).entrySet()) {
                if (BetterStringUtils.isNotBlank(ent.getValue())) {
                    field = BCStyle.class.getField(ent.getKey());
                    ASN1ObjectIdentifier objectId = (ASN1ObjectIdentifier) field.get(null);
                    dnBuilder.addRDN(objectId, ent.getValue());
                }
            }
        }
        catch (Exception e) {

        }
        X500Name dn = dnBuilder.build();

        return dn.toString();
    }

    private static Map<String, String> exportMetaOids(BetterX509CertInfo anCertInfo) {
        Map result = new LinkedHashMap();
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
    public BetterX509CertInfo saveX509CertFromWeb(BetterX509CertInfo anCertInfo) {
        KeyPair pair = BetterX509Utils.newRsaKeyPair(BetterX509Utils.KEY_LENGTH);
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
    public BetterX509CertInfo saveX509Cert(BetterX509CertInfo anCertInfo) {

        return saveX509Cert(anCertInfo, null);
    }

    public BetterX509CertInfo saveX509Cert(BetterX509CertInfo anCertInfo, InputStream anData) {
        try {
            if (BetterStringUtils.isNotBlank(anCertInfo.getPasswd())) {
                anCertInfo.setPasswd(Cryptos.aesEncrypt(anCertInfo.getPasswd()));
            }
            if (anData != null) {
                anCertInfo.setData(IOUtils.toByteArray(anData));
            }
        }
        catch (IOException e) {
            throw new BytterTradeException(50009, "保存数字证书出现异常", e);
        }
        BetterX509CertInfo certInfo = Collections3.getFirst(selectByProperty("serialNo", anCertInfo.getSerialNo()));
        if (certInfo == null) {
            anCertInfo.initDefValue(UserUtils.getOperatorInfo());
            this.insert(anCertInfo);
        }
        else {
            anCertInfo.modifyValue(UserUtils.getOperatorInfo(), certInfo);
            this.updateByPrimaryKey(anCertInfo);
        }

        return anCertInfo;
    }

    private BetterX509CertInfo extractCertInfo(BetterX509CertInfo certInfo, X509Certificate anX509) {
        Map<String, String> subItemMap = BetterX509Utils.findCertificateSubjectMap(anX509);
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
    public BetterX509CertInfo saveX509CertInfoFromFile(String anFilePath, String anPassword) {
        BetterX509CertFileStore fileStore = new BetterX509CertFileStore(null, anFilePath, anPassword, null);
        BetterX509CertInfo certInfo = extractCertInfo(new BetterX509CertInfo(), fileStore.findCertificate());
        certInfo.setPasswd(anPassword);
        certInfo.setSigner(fileStore.findSigner());
        certInfo.setCertAlias(fileStore.findCertAlias());
        certInfo.setCertType(fileStore.findCertType().getValue());
        // certInfo.initDefValue(UserUtils.getOperatorInfo());
        certInfo.setPrivateKey(Cryptos.aesEncrypt(fileStore.findPrivateKeyEncode()));
        certInfo.setData(fileStore.readOrignData());

        return saveX509Cert(certInfo);
    }

    private Map<String, BetterX509CertStore> buildRootCertStore(Map<String, BetterX509CertInfo> anRootMap) {
        Map<String, BetterX509CertStore> result = new HashMap<String, BetterX509CertStore>();
        BetterX509CertInfo certInfo;
        for (Map.Entry<String, BetterX509CertInfo> ent : anRootMap.entrySet()) {
            certInfo = ent.getValue();
            result.put(ent.getKey(),
                    new BetterX509CertStreamStore(null, certInfo.getData(), null, certInfo.getCertAlias(), BetterX509CertType.ROOT_CA));
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

    private List<SimpleDataEntity> findX509CertByType(String anCertType, String anStatus) {
        List<SimpleDataEntity> result = new ArrayList<>();
        Map<String, Object> termMap = QueryTermBuilder.newInstance().put("certType", anCertType).put("certStatus", anStatus).build();
        
        for (BetterX509CertInfo certInfo : this.selectByProperty(termMap)){
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
        Map<String, BetterX509CertStore> rootMap = buildRootCertStore(
                ReflectionUtils.listConvertToMap(selectByProperty("certType", "0"), "certAlias"));
        Map<String, BetterX509CertStore> result = new LinkedHashMap();
        BetterX509CertStore rootStore;
        for (BetterX509CertInfo certInfo : this.selectByProperty("certType", "1")) {
            rootStore = rootMap.get(certInfo.getSigner());
            if (rootStore != null) {
                result.put(certInfo.getCertAlias(), new BetterX509CertStreamStore(rootStore, certInfo.getData(),
                        Cryptos.aesDecrypt(certInfo.getPasswd()), certInfo.getCertAlias(), BetterX509CertType.MIDDLE_CA));
            }
        }

        return result;
    }
}
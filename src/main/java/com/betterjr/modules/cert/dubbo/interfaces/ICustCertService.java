package com.betterjr.modules.cert.dubbo.interfaces;

import java.security.cert.X509Certificate;
import java.util.Map;

import com.betterjr.modules.cert.entity.CustCertInfo;

public interface ICustCertService {

    /**
     *
     * 校验证书公钥是否正确
     *
     * @param 证书公钥信息
     * @return 成功返回 证书信息，失败抛出异常
     * @throws 异常情况
     */
    public CustCertInfo checkValidity(X509Certificate anX509);

    public CustCertInfo checkValidityWithBase64(final String anX509Str);

    /**
     * 根据机构信息获得这个几个的第一个数字证书
     *
     * @param anOperOrg
     * @return
     */
    public CustCertInfo findFirstCertInfoByOperOrg(String anOperOrg);

    /**
     * 查询认证信息
     *
     */
    public String webFindCustCertificate(String anSerialNo);

    /**
     * 分页查询证书信息
     */
    public String webQueryCustCertificate(Map<String, Object> anParam, int anFlag, int anPageNum, int anPageSize);

    /**
     * 添加客户证书
     */
    public String webAddCustCertificate(Map<String, Object> anParam);

    /**
     * 修改客户证
     */
    public String webSaveCustCertificate(final String anSerialNo, Map<String, Object> anParam);

    /**
     * 修改来自微信的客户证书
     */
    public String webSaveCustCertificate(String anSerialNo, String anOrginSerialNo, Map<String, Object> anParam);

    /**
     * 发布证书
     */
    public String webPublishCustCertificate(String anSerialNo, final String anPublishMode);

    /**
     * 作废证书
     */
    public String webCancelCustCertificate(String anSerialNo);

    /**
     * 回收证书
     */
    public String webRevokeCustCertificate(String anSerialNo, final String anReason);

    /**
     * 获取证书数据
     */
    public byte[] webDownloadCertificate(final String anToken);

    /**
     * 获取证书角色列表
     */
    public String webQueryCustCertRoleList(String anSerialNo);

}
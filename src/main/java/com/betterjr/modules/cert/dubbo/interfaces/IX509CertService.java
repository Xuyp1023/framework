package com.betterjr.modules.cert.dubbo.interfaces;

import java.util.Map;

/**
 * 证书管理
 * @author liuwl
 *
 */
public interface IX509CertService {
    /**
     * 查询证书信息
     */
    public String webFindCertificateInfo(Long anId);

    /**
     * 查询证书信息
     */
    public String webFindCertificateInfo(Long anId, String anSerialNo);

    /**
     * 查询签发者列表
     */
    public String webQuerySignerList();

    /**
     * 分页查询证书信息
     */
    public String webQueryCertificateInfo(Map<String, Object> anParam, int anFlag, int anPageNum, int anPageSize);

    /**
     * 分页查询证书信息
     */
    public String webQueryUnusedCertificateInfo();

    /**
     * 添加证书
     */
    public String webAddCertificateInfo(Map<String, Object> anParam);

    /**
     * 修改证书
     */
    public String webSaveCertificateInfo(Long anId, Map<String, Object> anParam);

    /**
     * 制作证书
     */
    public String webMakeCertificateInfo(Long anId, String anSerialNo);

    /**
     * 作废证书
     */
    public String webCancelCertificateInfo(Long anId, String anSerialNo);

    /**
     * 回收证书
     */
    public String webRevokeCertificateInfo(Long anId, String anSerialNo, String anReason);
}

package com.betterjr.modules.cert.dubbo.interfaces;

import java.util.Map;

/**
 * 
 * @author liuwl
 *
 */
public interface ICustomerCertificateService {
    /**
     * 查询认证信息
     * 
     */
    public String webFindCustCertificate(Long anId);
    
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
    public String webSaveCustCertificate(Map<String, Object> anParam);
    
    /**
     * 发布证书 
     */
    public String webPublishCustCertificate(Long anId);
    
    /**
     * 作废证书 
     */
    public String webCancelCustCertificate(Long anId);
}

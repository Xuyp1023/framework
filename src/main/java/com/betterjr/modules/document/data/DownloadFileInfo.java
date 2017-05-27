package com.betterjr.modules.document.data;

import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.mapper.entity.ReferClass;
import com.betterjr.modules.document.entity.CustFileItem;

@ReferClass(CustFileItem.class)
public class DownloadFileInfo extends CustFileItem  implements  BaseRemoteEntity, java.io.Serializable{
     private static final long serialVersionUID = 6995726576721339354L;

    /**
      * 合作伙伴中客户号
      */
     private String partnerCode;
     
     /**
      * 平台中的客户号
      */
     private Long custNo;
     
     /**
      * 业务类型，根据不同接口定义的业务类型
      */
     private String businType;

    private String accessToken;
    public String getPartnerCode() {
        return this.partnerCode;
    }

    public void setPartnerCode(String anPartnerCode) {
        this.partnerCode = anPartnerCode;
    }


    public String findComposeKey(){
        return this.partnerCode.concat(",").concat(this.getFileInfoType()).concat(",").concat(this.businType);
    }
    public String getAccessToken() {
        return this.accessToken;
    }
    public void setAccessToken(String anAccessToken) {
        this.accessToken = anAccessToken;
    }
    public Long getCustNo() {
        return this.custNo;
    }

    public void setCustNo(Long anCustNo) {
        this.custNo = anCustNo;
    }

    public String getBusinType() {
        return this.businType;
    }

    public void setBusinType(String anBusinType) {
        this.businType = anBusinType;
    }
     
    
}

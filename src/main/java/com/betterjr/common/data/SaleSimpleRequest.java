package com.betterjr.common.data;


public interface SaleSimpleRequest extends BaseRemoteEntity {

    String getTradeDate();

    void setTradeDate(String tradeDate);

    String getTano();

    void setTano(String tano);

    Long getCustNo();

    void setCustNo(Long custNo);

    String getAgencyNo();

    void setAgencyNo(String agencyNo);

    String getPayCenterNo();

    void setPayCenterNo(String payCenterNo);

    String getNetNo();

    void setNetNo(String netNo);

    String getBusinFlag();

    void setBusinFlag(String businFlag);

    String getCustType();

    void setCustType(String custType);

    String getTradeStatus();

    void setTradeStatus(String tradeStatus);

    String getAcceptMode();

    void setAcceptMode(String acceptMode);

    String getFundCode();
    
    void setFundCode(String fundCode);
    
    String getTradeAccount();

    String getFundAccount();
 
}
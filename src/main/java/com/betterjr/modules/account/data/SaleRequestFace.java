package com.betterjr.modules.account.data;

import com.betterjr.common.data.SaleSimpleRequest;

 
public interface SaleRequestFace extends SaleSimpleRequest {

    String getRequestNo();

    void setRequestNo(String requestNo);

    void setTradeAccount(String tradeAccount);

    void setFundAccount(String fundAccount);

    String getCityNo();

    void setCityNo(String anCityNo);

    String getPreStatus();

    void setPreStatus(String preStatus);

    String getLastTradeStatus();

    void setLastTradeStatus(String anLastTradeStatus);

    String getTradeTime();

    void setTradeTime(String tradeTime);

    String getOperDate();

    void setOperDate(String operDate);

    String getOperTime();

    void setOperTime(String operTime);

    String getOperNo();

    void setOperNo(String operNo);

    String getCheckerNo();

    void setCheckerNo(String checkerNo);

    String getPromotion();

    void setPromotion(String promotion);

    String getCanceled();

    void setCanceled(String canceled);

    String getOrderWay();

    void setOrderWay(String orderWay);

    String getOperOrg();

    void setOperOrg(String operOrg);

    String getBroker();

    void setBroker(String broker);

    String getReferral();

    void setReferral(String referral);

    String getReferralMobile();

    void setReferralMobile(String referralMobile);

    String getSourceKey();

    void setSourceKey(String sourceKey);

    String getOperWay();

    void setOperWay(String operWay);

    Long getMoneyAccount();

    void setMoneyAccount(Long moneyAccount);

    String getConfirmDate();

    void setConfirmDate(String confirmDate);

    String getErrCode();

    void setErrCode(String errCode);

    String getErrDetail();

    void setErrDetail(String errDetail);

    String getConfirmCause();

    void setConfirmCause(String confirmCause);

    String getConfirmSerialno();

    void setConfirmSerialno(String confirmSerialno);

    String getTagetTradeAccount();

    void setTagetTradeAccount(String tagetTradeAccount);

    String getSpecification();

    void setSpecification(String specification);

    String getFrozenEndLine();

    void setFrozenEndLine(String frozenEndLine);

    String getFrozenCause();

    void setFrozenCause(String frozenCause);

    String getOriginConfirmNo();

    void setOriginConfirmNo(String anOriginConfirmNo);

    String getOriginRequestNo();

    void setOriginRequestNo(String anOriginRequestNo);

    String getOriginConfirmDate();

    void setOriginConfirmDate(String anOriginConfirmDate);

    String getChecker();

    void setChecker(String checker);

    String getSaleCustNo();

    void setSaleCustNo(String anSaleCustno);

    String getSaleRequestNo();

    void setSaleRequestNo(String saleRequestNo);

    String getIpaddr();

    void setIpaddr(String ipaddr);

    String getCustManager();

    void setCustManager(String custManager);

    Long getContactor();

    void setContactor(Long contactor);

    String getBankAccount();

    String getSaleNetNo();

    void setSaleNetNo(String anSaleNetNo);

    String getSalePayCenterNo();

    void setSalePayCenterNo(String anSalePayCenterNo);

    String getSaleTradeAccount();

    void setSaleTradeAccount(String anSaleTradeAccount);

    void setDealFlag(String anDealFlag);

    String getDealFlag();

    String getContName();

    void setContName(String contName);

    String getAduitFlag();

    void setAduitFlag(String anAduitFlag);

    String getSaleMoneyAccount();

    void setSaleMoneyAccount(String anSaleMoneyAccount);

}
package com.betterjr.common.data;

import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;

/**
 * 需要处理修改信息或登记信息的类的超类，主要处理初始化这些默认参数
 * @author zhoucy
 *
 */
public abstract class BetterBaseEntity {
    
    /**
     * 初始化实体信息
     * @param anOperInfo
     */
    public void initValue(CustOperatorInfo anOperInfo) {
        this.modifyValue(anOperInfo);
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        if (anOperInfo != null) {
            this.setRegOperId(anOperInfo.getId());
            this.setRegOperName(anOperInfo.getName());
        }
    }
    
    public void modifyValue(CustOperatorInfo anOperInfo, BetterBaseEntity anEntity){
        modifyValue(anEntity);
        modifyValue(anOperInfo);
    }
    
    /**
     * 修改实体信息
     * @param anEntity
     */
    public void modifyValue(BetterBaseEntity anEntity){
        this.setRegDate( anEntity.getRegDate());
        this.setRegTime( anEntity.getRegTime());
        this.setRegOperId(anEntity.getRegOperId());
        this.setRegOperName(anEntity.getRegOperName());
    }

    /**
     * 初始化修改内容的信息
     * @param anOperInfo
     */
    public void modifyValue(CustOperatorInfo anOperInfo) {
        this.setModiDate(BetterDateUtils.getNumDate());
        this.setModiTime(BetterDateUtils.getNumTime());
        if (anOperInfo != null) {
            this.setModiOperId(anOperInfo.getId());
            this.setModiOperName(anOperInfo.getName());
        }
    }
    
    public abstract String getRegDate();
    public abstract String getRegTime();
    public abstract String getRegOperName();
    public abstract Long getRegOperId();

    public abstract void setRegDate(String anValue);

    public abstract void setRegTime(String anValue);

    public abstract void setRegOperName(String anValue);

    public abstract void setRegOperId(Long anValue);

    public abstract void setModiDate(String anValue);

    public abstract void setModiTime(String anValue);

    public abstract void setModiOperName(String anValue);

    public abstract void setModiOperId(Long anValue);
}

package com.betterjr.modules.wechat.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.data.BetterBaseEntity;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cust_wxuser")
public class CustWeChatInfo extends BetterBaseEntity implements BetterjrEntity {
    /**
     * 微信AppID
     */
    @Column(name = "c_appid", columnDefinition = "VARCHAR")
    @MetaData(value = "微信AppID", comments = "微信AppID")
    private String appId;

    /**
     * 用户的标识，对当前公众号唯一
     */
    @Id
    @Column(name = "c_openid", columnDefinition = "VARCHAR")
    @MetaData(value = "用户的标识", comments = "用户的标识，对当前公众号唯一")
    private String openId;

    /**
     * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号
     */
    @Column(name = "c_subscribe", columnDefinition = "VARCHAR")
    @MetaData(value = "用户是否订阅该公众号标识", comments = "用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号")
    private String subscribeStatus;

    /**
     * 用户的昵称
     */
    @Column(name = "c_nickname", columnDefinition = "VARCHAR")
    @MetaData(value = "用户的昵称", comments = "用户的昵称")
    private String nickName;

    /**
     * 性别
     */
    @Column(name = "c_sex", columnDefinition = "VARCHAR")
    @MetaData(value = "性别", comments = "性别")
    private String custSex;

    /**
     * 所在城市
     */
    @Column(name = "c_city", columnDefinition = "VARCHAR")
    @MetaData(value = "所在城市", comments = "所在城市")
    private String cityName;

    /**
     * 所在省份
     */
    @Column(name = "c_province", columnDefinition = "VARCHAR")
    @MetaData(value = "所在省份", comments = "所在省份")
    private String provinceName;

    /**
     * 所在国家
     */
    @Column(name = "c_country", columnDefinition = "VARCHAR")
    @MetaData(value = "所在国家", comments = "所在国家")
    private String countryName;

    /**
     * 用户关注时间，为时间戳
     */
    @Column(name = "t_subscribe_time", columnDefinition = "VARCHAR")
    @MetaData(value = "用户关注时间", comments = "用户关注时间，为时间戳")
    private String subscribeTime;

    /**
     * 公众号运营者对粉丝的备注
     */
    @Column(name = "c_description", columnDefinition = "VARCHAR")
    @MetaData(value = "公众号运营者对粉丝的备注", comments = "公众号运营者对粉丝的备注")
    private String description;

    /**
     * 用户所在的分组ID
     */
    @Column(name = "c_groupid", columnDefinition = "VARCHAR")
    @MetaData(value = "用户所在的分组ID", comments = "用户所在的分组ID")
    private String groupId;

    /**
     * 用户被打上的标签ID列表
     */
    @Column(name = "c_tagid_list", columnDefinition = "VARCHAR")
    @MetaData(value = "用户被打上的标签ID列表", comments = "用户被打上的标签ID列表")
    private String tagIdList;

    /**
     * 操作员所在机构
     */
    @Column(name = "c_operorg", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在机构", comments = "操作员所在机构")
    private String operOrg;

    /**
     * 状态，0未关注，1关注，2暂停
     */
    @Column(name = "c_status", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态，0未关注，1关注，2暂停")
    private String businStatus;

    /**
     * 登记日期
     */
    @Column(name = "d_reg_date", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 登记时间
     */
    @Column(name = "t_reg_time", columnDefinition = "VARCHAR")
    @MetaData(value = "登记时间", comments = "登记时间")
    private String regTime;

    /**
     * 修改日期
     */
    @Column(name = "d_modi_date", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 修改时间
     */
    @Column(name = "t_modi_time", columnDefinition = "VARCHAR")
    @MetaData(value = "修改时间", comments = "修改时间")
    private String modiTime;

    /**
     * 取消关注的时间
     */
    @Column(name = "t_unsubscribe_time", columnDefinition = "VARCHAR")
    @MetaData(value = "取消关注的时间", comments = "取消关注的时间")
    private String unSubscribeTime;

    /**
     * 操作员ID号
     */
    @Column(name = "l_operid", columnDefinition = "INTEGER")
    @MetaData(value = "操作员ID号", comments = "操作员ID号")
    private Long operId;

    /**
     * 操作员名字
     */
    @Column(name = "c_opername", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员名字", comments = "操作员名字")
    private String operName;

    /**
     * 客户号
     */
    @Column(name = "l_custno", columnDefinition = "INTEGER")
    @MetaData(value = "客户号", comments = "客户号")
    private Long custNo;

    /**
     * 首次登录
     */
    @Column(name = "c_first", columnDefinition = "VARCHAR")
    @MetaData(value = "首次登录", comments = "首次登录")
    private Boolean firstLogin;

    @Transient
    @JsonIgnore
    private Long regOperId;

    @Transient
    @JsonIgnore
    private String regOperName;

    @Transient
    @JsonIgnore
    private String modiOperName;

    @Transient
    @JsonIgnore
    private Long modiOperId;

    private static final long serialVersionUID = 5203187710146876885L;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(String subscribeStatus) {
        this.subscribeStatus = subscribeStatus == null ? null : subscribeStatus.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getCustSex() {
        return custSex;
    }

    public void setCustSex(String custSex) {
        this.custSex = custSex == null ? null : custSex.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName == null ? null : countryName.trim();
    }

    public String getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime == null ? null : subscribeTime.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getTagIdList() {
        return tagIdList;
    }

    public void setTagIdList(String tagIdList) {
        this.tagIdList = tagIdList == null ? null : tagIdList.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime == null ? null : modiTime.trim();
    }

    public String getUnSubscribeTime() {
        return unSubscribeTime;
    }

    public void setUnSubscribeTime(String unSubscribeTime) {
        this.unSubscribeTime = unSubscribeTime == null ? null : unSubscribeTime.trim();
    }

    public Long getOperId() {
        return operId;
    }

    public void setOperId(Long operId) {
        this.operId = operId;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Boolean getFirstLogin() {
        return this.firstLogin;
    }

    public void setFirstLogin(Boolean anFirstLogin) {
        this.firstLogin = anFirstLogin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" appId=").append(appId);
        sb.append(", openId=").append(openId);
        sb.append(", subscribeStatus=").append(subscribeStatus);
        sb.append(", nickName=").append(nickName);
        sb.append(", custSex=").append(custSex);
        sb.append(", cityName=").append(cityName);
        sb.append(", provinceName=").append(provinceName);
        sb.append(", countryName=").append(countryName);
        sb.append(", subscribeTime=").append(subscribeTime);
        sb.append(", description=").append(description);
        sb.append(", groupId=").append(groupId);
        sb.append(", tagIdList=").append(tagIdList);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", unSubscribeTime=").append(unSubscribeTime);
        sb.append(", operId=").append(operId);
        sb.append(", operName=").append(operName);
        sb.append(", custNo=").append(custNo);
        sb.append(", firstLogin=").append(firstLogin);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CustWeChatInfo other = (CustWeChatInfo) that;
        return (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
                && (this.getOpenId() == null ? other.getOpenId() == null : this.getOpenId().equals(other.getOpenId()))
                && (this.getSubscribeStatus() == null ? other.getSubscribeStatus() == null
                        : this.getSubscribeStatus().equals(other.getSubscribeStatus()))
                && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
                && (this.getCustSex() == null ? other.getCustSex() == null : this.getCustSex().equals(other.getCustSex()))
                && (this.getCityName() == null ? other.getCityName() == null : this.getCityName().equals(other.getCityName()))
                && (this.getProvinceName() == null ? other.getProvinceName() == null : this.getProvinceName().equals(other.getProvinceName()))
                && (this.getCountryName() == null ? other.getCountryName() == null : this.getCountryName().equals(other.getCountryName()))
                && (this.getSubscribeTime() == null ? other.getSubscribeTime() == null : this.getSubscribeTime().equals(other.getSubscribeTime()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
                && (this.getTagIdList() == null ? other.getTagIdList() == null : this.getTagIdList().equals(other.getTagIdList()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
                && (this.getUnSubscribeTime() == null ? other.getUnSubscribeTime() == null
                        : this.getUnSubscribeTime().equals(other.getUnSubscribeTime()))
                && (this.getOperId() == null ? other.getOperId() == null : this.getOperId().equals(other.getOperId()))
                && (this.getOperName() == null ? other.getOperName() == null : this.getOperName().equals(other.getOperName()))
                && (this.getFirstLogin() == null ? other.getFirstLogin() == null : this.getFirstLogin().equals(other.getFirstLogin()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getOpenId() == null) ? 0 : getOpenId().hashCode());
        result = prime * result + ((getSubscribeStatus() == null) ? 0 : getSubscribeStatus().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getCustSex() == null) ? 0 : getCustSex().hashCode());
        result = prime * result + ((getCityName() == null) ? 0 : getCityName().hashCode());
        result = prime * result + ((getProvinceName() == null) ? 0 : getProvinceName().hashCode());
        result = prime * result + ((getCountryName() == null) ? 0 : getCountryName().hashCode());
        result = prime * result + ((getSubscribeTime() == null) ? 0 : getSubscribeTime().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getTagIdList() == null) ? 0 : getTagIdList().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getUnSubscribeTime() == null) ? 0 : getUnSubscribeTime().hashCode());
        result = prime * result + ((getOperId() == null) ? 0 : getOperId().hashCode());
        result = prime * result + ((getOperName() == null) ? 0 : getOperName().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getFirstLogin() == null) ? 0 : getFirstLogin().hashCode());
        return result;
    }

    @Override
    public String getRegOperName() {

        return this.operName;
    }

    @Override
    public Long getRegOperId() {

        return this.operId;
    }

    @Override
    public void setRegOperName(String anValue) {

    }

    @Override
    public void setRegOperId(Long anValue) {

    }

    @Override
    public void setModiOperName(String anValue) {

    }

    @Override
    public void setModiOperId(Long anValue) {

    }

    public CustWeChatInfo() {

    }

    public void putCustSex(int anSex) {
        if (anSex == 0) {
            this.custSex = "3";
        }
        else if (anSex == 2) {
            this.custSex = "0";
        }
        else if (anSex == 1) {
            this.custSex = "1";
        }
        else {
            this.custSex = "3";
        }
    }

    public void modifySubscribe(String anSubStatus, Long anSubscribeTime) {
        this.setSubscribeStatus(anSubStatus);
        if (anSubscribeTime > 10000) {
            this.subscribeTime = BetterDateUtils.formatDate(new Date(anSubscribeTime * 1000), "yyyyMMdd HHmmss");
        }
        if ("0".equals(anSubStatus)) {
            this.businStatus = "0";
        }
        else if ("1".equals(anSubStatus)) {
            if ("3".equals(this.businStatus)) {
                this.businStatus = "1";
            }
        }
    }

    public static boolean checkSendStatus(CustWeChatInfo anInfo) {
        if (anInfo == null) {
            return false;
        }
        if ("0".equals(anInfo.getSubscribeStatus())) {
            return false;
        }
        return "1".equals(anInfo.getBusinStatus());
    }

    public CustWeChatInfo(BasicEvent anEvent, String anSubStatus) {
        this.appId = anEvent.getToUserName();
        this.openId = anEvent.getFromUserName();
        this.subscribeStatus = anSubStatus;
    }
}
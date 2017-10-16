package com.betterjr.modules.sys.entity;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CFG_AREA")
public class AreaInfo implements BetterjrEntity {
    /**
     * 区域代码
     */
    @Id
    @Column(name = "C_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "区域代码", comments = "区域代码")
    @OrderBy
    private String areaCode;

    /**
     * 区域名称
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "区域名称", comments = "区域名称")
    private String areaName;

    /**
     * 省份代码
     */
    @Column(name = "C_PROVINCE_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "省份代码", comments = "省份代码")
    private String provCode;

    /**
     * 状态；0停用，1正常
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态；0停用，1正常")
    private String status;

    /**
     * 注册日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "注册日期", comments = "注册日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 签约标志 0未签约 1已签约
     */
    @Column(name = "C_SIGNSTATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "签约标志 0未签约 1已签约", comments = "签约标志 0未签约 1已签约")
    private Boolean signStatus;

    private static final long serialVersionUID = 1443357040062L;

    @Transient
    private Map<String, AreaInfo> cityMap = null;

    public Map<String, AreaInfo> getCityMap() {
        return cityMap;
    }

    public void setCityMap(Map<String, AreaInfo> cityMap) {
        this.cityMap = cityMap;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode == null ? null : provCode.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public Boolean getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Boolean signStatus) {
        this.signStatus = signStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", areaCode=").append(areaCode);
        sb.append(", areaName=").append(areaName);
        sb.append(", provCode=").append(provCode);
        sb.append(", status=").append(status);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", signStatus=").append(signStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
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
        AreaInfo other = (AreaInfo) that;
        return (this.getAreaCode() == null ? other.getAreaCode() == null
                : this.getAreaCode().equals(other.getAreaCode()))
                && (this.getAreaName() == null ? other.getAreaName() == null
                        : this.getAreaName().equals(other.getAreaName()))
                && (this.getProvCode() == null ? other.getProvCode() == null
                        : this.getProvCode().equals(other.getProvCode()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()))
                && (this.getSignStatus() == null ? other.getSignStatus() == null
                        : this.getSignStatus().equals(other.getSignStatus()));
    }

    public static int findLevel(String anAreaCode) {
        if (anAreaCode.endsWith("0000")) {
            return 1;
        } else if (anAreaCode.endsWith("00")) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAreaCode() == null) ? 0 : getAreaCode().hashCode());
        result = prime * result + ((getAreaName() == null) ? 0 : getAreaName().hashCode());
        result = prime * result + ((getProvCode() == null) ? 0 : getProvCode().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getSignStatus() == null) ? 0 : getSignStatus().hashCode());
        return result;
    }
}
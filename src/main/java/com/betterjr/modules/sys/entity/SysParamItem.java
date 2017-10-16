package com.betterjr.modules.sys.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cfg_param_item")
public class SysParamItem implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Long id;

    /**
     * 参数表关联编号
     */
    @Column(name = "L_PARAM_ID", columnDefinition = "INTEGER")
    @MetaData(value = "参数表关联编号", comments = "参数表关联编号")
    private Long paramId;

    /**
     * KEY值
     */
    @Column(name = "C_KEY", columnDefinition = "VARCHAR")
    @MetaData(value = "KEY值", comments = "KEY值")
    private String key;

    /**
     * VALUE1
     */
    @Column(name = "C_VALUE1", columnDefinition = "VARCHAR")
    @MetaData(value = "VALUE1", comments = "VALUE1")
    private String value1;

    /**
     * VALUE2
     */
    @Column(name = "C_VALUE2", columnDefinition = "VARCHAR")
    @MetaData(value = "VALUE2", comments = "VALUE2")
    private String value2;

    /**
     * VALUE3
     */
    @Column(name = "C_VALUE3", columnDefinition = "VARCHAR")
    @MetaData(value = "VALUE3", comments = "VALUE3")
    private String value3;

    /**
     * VALUE4
     */
    @Column(name = "C_VALUE4", columnDefinition = "VARCHAR")
    @MetaData(value = "VALUE4", comments = "VALUE4")
    private String value4;

    /**
     * VALUE5
     */
    @Column(name = "C_VALUE5", columnDefinition = "VARCHAR")
    @MetaData(value = "VALUE5", comments = "VALUE5")
    private String value5;

    /**
     * VALUE6
     */
    @Column(name = "C_VALUE6", columnDefinition = "VARCHAR")
    @MetaData(value = "VALUE6", comments = "VALUE6")
    private String value6;

    /**
     * VALUE7
     */
    @Column(name = "C_VALUE7", columnDefinition = "VARCHAR")
    @MetaData(value = "VALUE7", comments = "VALUE7")
    private String value7;

    /**
     * VALUE8
     */
    @Column(name = "C_VALUE8", columnDefinition = "VARCHAR")
    @MetaData(value = "VALUE8", comments = "VALUE8")
    private String value8;

    /**
     * VALUE9
     */
    @Column(name = "C_VALUE9", columnDefinition = "VARCHAR")
    @MetaData(value = "VALUE9", comments = "VALUE9")
    private String value9;

    /**
     * 创建人(操作员)ID号
     */
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "创建人(操作员)ID号", comments = "创建人(操作员)ID号")
    private Long regOperId;

    /**
     * 创建人(操作员)姓名
     */
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建人(操作员)姓名", comments = "创建人(操作员)姓名")
    private String regOperName;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "创建日期", comments = "创建日期")
    private String regDate;

    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建时间", comments = "创建时间")
    private String regTime;

    /**
     * 修改人(操作员)ID号
     */
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "修改人(操作员)ID号", comments = "修改人(操作员)ID号")
    private Long modiOperId;

    /**
     * 修改人(操作员)姓名
     */
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改人(操作员)姓名", comments = "修改人(操作员)姓名")
    private String modiOperName;

    /**
     * 修改日期
     */
    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modifyDate;

    /**
     * 修改时间
     */
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改时间", comments = "修改时间")
    private String modifyTime;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 状态
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "CHAR")
    @MetaData(value = "状态", comments = "状态")
    private String businStatus;

    @Column(name = "C_LAST_STATUS", columnDefinition = "CHAR")
    @MetaData(value = "", comments = "")
    private String lastStatus;

    private static final long serialVersionUID = 1473405744352L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1 == null ? null : value1.trim();
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2 == null ? null : value2.trim();
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3 == null ? null : value3.trim();
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4 == null ? null : value4.trim();
    }

    public String getValue5() {
        return value5;
    }

    public void setValue5(String value5) {
        this.value5 = value5 == null ? null : value5.trim();
    }

    public String getValue6() {
        return value6;
    }

    public void setValue6(String value6) {
        this.value6 = value6 == null ? null : value6.trim();
    }

    public String getValue7() {
        return value7;
    }

    public void setValue7(String value7) {
        this.value7 = value7 == null ? null : value7.trim();
    }

    public String getValue8() {
        return value8;
    }

    public void setValue8(String value8) {
        this.value8 = value8 == null ? null : value8.trim();
    }

    public String getValue9() {
        return value9;
    }

    public void setValue9(String value9) {
        this.value9 = value9 == null ? null : value9.trim();
    }

    public Long getRegOperId() {
        return regOperId;
    }

    public void setRegOperId(Long regOperId) {
        this.regOperId = regOperId;
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(String regOperName) {
        this.regOperName = regOperName == null ? null : regOperName.trim();
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

    public Long getModiOperId() {
        return modiOperId;
    }

    public void setModiOperId(Long modiOperId) {
        this.modiOperId = modiOperId;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    public void setModiOperName(String modiOperName) {
        this.modiOperName = modiOperName == null ? null : modiOperName.trim();
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate == null ? null : modifyDate.trim();
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime == null ? null : modifyTime.trim();
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

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus == null ? null : lastStatus.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", paramId=").append(paramId);
        sb.append(", key=").append(key);
        sb.append(", value1=").append(value1);
        sb.append(", value2=").append(value2);
        sb.append(", value3=").append(value3);
        sb.append(", value4=").append(value4);
        sb.append(", value5=").append(value5);
        sb.append(", value6=").append(value6);
        sb.append(", value7=").append(value7);
        sb.append(", value8=").append(value8);
        sb.append(", value9=").append(value9);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", lastStatus=").append(lastStatus);
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
        SysParamItem other = (SysParamItem) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getParamId() == null ? other.getParamId() == null
                        : this.getParamId().equals(other.getParamId()))
                && (this.getKey() == null ? other.getKey() == null : this.getKey().equals(other.getKey()))
                && (this.getValue1() == null ? other.getValue1() == null : this.getValue1().equals(other.getValue1()))
                && (this.getValue2() == null ? other.getValue2() == null : this.getValue2().equals(other.getValue2()))
                && (this.getValue3() == null ? other.getValue3() == null : this.getValue3().equals(other.getValue3()))
                && (this.getValue4() == null ? other.getValue4() == null : this.getValue4().equals(other.getValue4()))
                && (this.getValue5() == null ? other.getValue5() == null : this.getValue5().equals(other.getValue5()))
                && (this.getValue6() == null ? other.getValue6() == null : this.getValue6().equals(other.getValue6()))
                && (this.getValue7() == null ? other.getValue7() == null : this.getValue7().equals(other.getValue7()))
                && (this.getValue8() == null ? other.getValue8() == null : this.getValue8().equals(other.getValue8()))
                && (this.getValue9() == null ? other.getValue9() == null : this.getValue9().equals(other.getValue9()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null
                        : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null
                        : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null
                        : this.getRegTime().equals(other.getRegTime()))
                && (this.getModiOperId() == null ? other.getModiOperId() == null
                        : this.getModiOperId().equals(other.getModiOperId()))
                && (this.getModiOperName() == null ? other.getModiOperName() == null
                        : this.getModiOperName().equals(other.getModiOperName()))
                && (this.getModifyDate() == null ? other.getModifyDate() == null
                        : this.getModifyDate().equals(other.getModifyDate()))
                && (this.getModifyTime() == null ? other.getModifyTime() == null
                        : this.getModifyTime().equals(other.getModifyTime()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null
                        : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getBusinStatus() == null ? other.getBusinStatus() == null
                        : this.getBusinStatus().equals(other.getBusinStatus()))
                && (this.getLastStatus() == null ? other.getLastStatus() == null
                        : this.getLastStatus().equals(other.getLastStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getParamId() == null) ? 0 : getParamId().hashCode());
        result = prime * result + ((getKey() == null) ? 0 : getKey().hashCode());
        result = prime * result + ((getValue1() == null) ? 0 : getValue1().hashCode());
        result = prime * result + ((getValue2() == null) ? 0 : getValue2().hashCode());
        result = prime * result + ((getValue3() == null) ? 0 : getValue3().hashCode());
        result = prime * result + ((getValue4() == null) ? 0 : getValue4().hashCode());
        result = prime * result + ((getValue5() == null) ? 0 : getValue5().hashCode());
        result = prime * result + ((getValue6() == null) ? 0 : getValue6().hashCode());
        result = prime * result + ((getValue7() == null) ? 0 : getValue7().hashCode());
        result = prime * result + ((getValue8() == null) ? 0 : getValue8().hashCode());
        result = prime * result + ((getValue9() == null) ? 0 : getValue9().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getLastStatus() == null) ? 0 : getLastStatus().hashCode());
        return result;
    }
}
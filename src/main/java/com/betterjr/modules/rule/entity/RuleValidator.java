package com.betterjr.modules.rule.entity;

import java.math.BigDecimal;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_RULE_VALIDATOR")
public class RuleValidator implements BetterjrEntity {
    /**
     * 验证名称
     */
    @Id
    @Column(name = "C_VALID_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "验证名称", comments = "验证名称")
    private String validName;

    /**
     * 所属系统简称
     */
    @Column(name = "C_SYS", columnDefinition = "VARCHAR")
    @MetaData(value = "所属系统简称", comments = "所属系统简称")
    private String sysNo;

    /**
     * 显示名称
     */
    @Column(name = "C_SHOW_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "显示名称", comments = "显示名称")
    private String showName;

    /**
     * 字段名称
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "字段名称", comments = "字段名称")
    private String fieldName;

    /**
     * 0不是必须内容，1必须内容
     */
    @Column(name = "C_MUSTITEM", columnDefinition = "VARCHAR")
    @MetaData(value = "0不是必须内容", comments = "0不是必须内容，1必须内容")
    private boolean mustItem;

    /**
     * C 字符型(String) A 数字字符型(Integer)，T时间日期型，D日期型， 限于0—9 N 数值型(Double)，其长度不包含小数点，B逻辑型(Boolean), LONG长整型(Long) 可参与数值计算 TEXT 不定长文本 BINARY 二进制数据
     */
    @Column(name = "C_DATA_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "C 字符型(String) A 数字字符型(Integer)", comments = "C 字符型(String) A 数字字符型(Integer)，T时间日期型，D日期型， 限于0—9 N 数值型(Double)，其长度不包含小数点，B逻辑型(Boolean), LONG长整型(Long) 可参与数值计算 TEXT 不定长文本 BINARY 二进制数据")
    private String dataType;

    /**
     * 数据项长度
     */
    @Column(name = "N_DATA_LEN", columnDefinition = "INTEGER")
    @MetaData(value = "数据项长度", comments = "数据项长度")
    private Integer dataLen;

    /**
     * 数据项小数位
     */
    @Column(name = "N_DATA_SCALE", columnDefinition = "INTEGER")
    @MetaData(value = "数据项小数位", comments = "数据项小数位")
    private Integer dataScale;

    /**
     * 最大值
     */
    @Column(name = "F_MAX", columnDefinition = "DOUBLE")
    @MetaData(value = "最大值", comments = "最大值")
    private BigDecimal maxValues;

    /**
     * 最小值
     */
    @Column(name = "F_MIN", columnDefinition = "DOUBLE")
    @MetaData(value = "最小值", comments = "最小值")
    private BigDecimal minValue;

    /**
     * 验证器
     */
    @Column(name = "C_VALIDATOR", columnDefinition = "VARCHAR")
    @MetaData(value = "验证器", comments = "验证器")
    private String validator;

    /**
     * 模式
     */
    @Column(name = "C_PATTERN", columnDefinition = "VARCHAR")
    @MetaData(value = "模式", comments = "模式")
    private String workPattern;

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
     * 描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "描述", comments = "描述")
    private String description;

    /**
     * 业务规则字段；对象属性@对象的方式。
     */
    @Column(name = "C_BUSIN_FIELD", columnDefinition = "VARCHAR")
    @MetaData(value = "业务规则字段", comments = "业务规则字段；对象属性@对象的方式。")
    private String businField;

    /**
     * 依赖的验证器；使用分号分隔
     */
    @Column(name = "C_DEPENDS", columnDefinition = "VARCHAR")
    @MetaData(value = "依赖的验证器", comments = "依赖的验证器；使用分号分隔")
    private String depends;

    /**
     * 描述
     */
    @Column(name = "C_MESSAGE", columnDefinition = "VARCHAR")
    @MetaData(value = "验证返回信息", comments = "验证返回信息")
    private String message;

    /**
     * 描述
     */
    @Column(name = "C_REF_VALUE", columnDefinition = "VARCHAR")
    @MetaData(value = "参考值", comments = "参考值，使用处理过程中的参考值达到灵活处理的目的，名称:字段；名称:字段的模式处理")    
    private String refValue;

    private static final long serialVersionUID = 1443883172535L;

    public String getValidName() {
        return validName;
    }

    public void setValidName(String validName) {
        this.validName = validName == null ? null : validName.trim();
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo == null ? null : sysNo.trim();
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName == null ? null : showName.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public boolean isMustItem() {
        return mustItem;
    }

    public void setMustItem(boolean mustItem) {
        this.mustItem = mustItem;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    public Integer getDataLen() {
        return dataLen;
    }

    public void setDataLen(Integer dataLen) {
        this.dataLen = dataLen;
    }

    public Integer getDataScale() {
        return dataScale;
    }

    public void setDataScale(Integer dataScale) {
        this.dataScale = dataScale;
    }
 
    public BigDecimal getMaxValues() {
        return this.maxValues;
    }

    public void setMaxValues(BigDecimal anMaxValues) {
        this.maxValues = anMaxValues;
    }

    public BigDecimal getMinValue() {
        return this.minValue;
    }

    public void setMinValue(BigDecimal anMinValue) {
        this.minValue = anMinValue;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator == null ? null : validator.trim();
    }

    public String getWorkPattern() {
        return workPattern;
    }

    public void setWorkPattern(String workPattern) {
        this.workPattern = workPattern == null ? null : workPattern.trim();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getBusinField() {
        return this.businField;
    }

    public void setBusinField(String anBusinField) {
        this.businField = anBusinField;
    }

    public String getDepends() {
        return this.depends;
    }

    public void setDepends(String anDepends) {
        this.depends = anDepends;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String anMessage) {
        this.message = anMessage;
    }

    public String getRefValue() {
        return this.refValue;
    }

    public void setRefValue(String anRefValue) {
        this.refValue = anRefValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(", validName=").append(validName);
        sb.append(", sysNo=").append(sysNo);
        sb.append(", showName=").append(showName);
        sb.append(", fieldName=").append(fieldName);
        sb.append(", mustItem=").append(mustItem);
        sb.append(", dataType=").append(dataType);
        sb.append(", dataLen=").append(dataLen);
        sb.append(", dataScale=").append(dataScale);
        sb.append(", maxValue=").append(maxValues);
        sb.append(", minValue=").append(minValue);
        sb.append(", validator=").append(validator);
        sb.append(", workPattern=").append(workPattern);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", description=").append(description);
        sb.append(", businField=").append(businField);
        sb.append(", depends=").append(depends);
        sb.append(", message=").append(message);
        sb.append(", refValue=").append(refValue);
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
        RuleValidator other = (RuleValidator) that;
        return (this.getValidName() == null ? other.getValidName() == null : this.getValidName().equals(other.getValidName()))
                && (this.getSysNo() == null ? other.getSysNo() == null : this.getSysNo().equals(other.getSysNo()))
                && (this.getShowName() == null ? other.getShowName() == null : this.getShowName().equals(other.getShowName()))
                && (this.getFieldName() == null ? other.getFieldName() == null : this.getFieldName().equals(other.getFieldName()))
                && (this.isMustItem() == other.isMustItem())
                && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
                && (this.getDataLen() == null ? other.getDataLen() == null : this.getDataLen().equals(other.getDataLen()))
                && (this.getDataScale() == null ? other.getDataScale() == null : this.getDataScale().equals(other.getDataScale()))
                && (this.getMaxValues() == null ? other.getMaxValues() == null : this.getMaxValues().equals(other.getMaxValues()))
                && (this.getMinValue() == null ? other.getMinValue() == null : this.getMinValue().equals(other.getMinValue()))
                && (this.getValidator() == null ? other.getValidator() == null : this.getValidator().equals(other.getValidator()))
                && (this.getWorkPattern() == null ? other.getWorkPattern() == null : this.getWorkPattern().equals(other.getWorkPattern()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getBusinField() == null ? other.getBusinField() == null : this.getBusinField().equals(other.getBusinField()))
                && (this.getDepends() == null ? other.getDepends() == null : this.getDepends().equals(other.getDepends()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getMessage() == null ? other.getMessage() == null : this.getMessage().equals(other.getMessage()))
                && (this.getRefValue() == null ? other.getRefValue() == null : this.getRefValue().equals(other.getRefValue()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getValidName() == null) ? 0 : getValidName().hashCode());
        result = prime * result + ((getSysNo() == null) ? 0 : getSysNo().hashCode());
        result = prime * result + ((getShowName() == null) ? 0 : getShowName().hashCode());
        result = prime * result + ((getFieldName() == null) ? 0 : getFieldName().hashCode());
        result = prime * result + (isMustItem() ? 1231 : 1237);
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getDataLen() == null) ? 0 : getDataLen().hashCode());
        result = prime * result + ((getDataScale() == null) ? 0 : getDataScale().hashCode());
        result = prime * result + ((getMaxValues() == null) ? 0 : getMaxValues().hashCode());
        result = prime * result + ((getMinValue() == null) ? 0 : getMinValue().hashCode());
        result = prime * result + ((getValidator() == null) ? 0 : getValidator().hashCode());
        result = prime * result + ((getWorkPattern() == null) ? 0 : getWorkPattern().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getBusinField() == null) ? 0 : getBusinField().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getDepends() == null) ? 0 : getDepends().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        result = prime * result + ((getRefValue() == null) ? 0 : getRefValue().hashCode());
    return result;
    }
}
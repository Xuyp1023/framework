package com.betterjr.modules.sys.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.config.ConfigFace;
import com.betterjr.common.config.ConfigItemOperatorImpl;
import com.betterjr.common.data.DataTypeInfo;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CFG_CONFIG")
public class SysConfigInfo extends ConfigItemOperatorImpl implements BetterjrEntity, ConfigFace {
    /**
     * 参数编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "参数编号", comments = "参数编号")
    private Integer id;

    /**
     * 所属系统简称
     */
    @Column(name = "C_SYS", columnDefinition = "VARCHAR")
    @MetaData(value = "所属系统简称", comments = "所属系统简称")
    private String sysName;

    /**
     * 参数组别
     */
    @Column(name = "N_GROUPNO", columnDefinition = "INTEGER")
    @MetaData(value = "参数组别", comments = "参数组别")
    private Integer groupNo;

    /**
     * 参数名称
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "参数名称", comments = "参数名称")
    private String itemName;

    /**
     * 数据项类型； C 字符型(String) A 数字字符型(Integer)，T时间日期型，D日期型， 限于0—9 N 数值型(Double)，其长度不包含小数点，B逻辑型(Boolean), LONG长整型(Long) 可参与数值计算 TEXT 不定长文本 BINARY 二进制数据
     */
    @Column(name = "C_DATA_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="数据项类型", comments = "数据项类型； C 字符型(String) A 数字字符型(Integer)，T时间日期型，D日期型， 限于0—9 N 数值型(Double)，其长度不包含小数点，B逻辑型(Boolean), LONG长整型(Long) 可参与数值计算 TEXT 不定长文本 BINARY 二进制数据")
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
     * 参数字符内容；取值范围
     */
    @Column(name = "C_CHARSET", columnDefinition = "VARCHAR")
    @MetaData(value = "参数字符内容", comments = "参数字符内容；取值范围")
    private String dataScope;

    /**
     * 参数处理模式
     */
    @Column(name = "C_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "参数处理模式", comments = "参数处理模式")
    private String workMode;

    /**
     * 参数控制
     */
    @Column(name = "C_CTRLTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "参数控制", comments = "参数控制")
    private String itemType;

    /**
     * 参数显示宽度
     */
    @Column(name = "N_WIDTH", columnDefinition = "INTEGER")
    @MetaData(value = "参数显示宽度", comments = "参数显示宽度")
    private Integer disWidth;

    /**
     * 参数数据字典
     */
    @Column(name = "C_DICTCODE", columnDefinition = "VARCHAR")
    @MetaData(value = "参数数据字典", comments = "参数数据字典")
    private String dictItem;

    /**
     * 参数选择列表值，列表之间使用分号
     */
    @Column(name = "C_VALUELIST", columnDefinition = "VARCHAR")
    @MetaData(value = "参数选择列表值", comments = "参数选择列表值，列表之间使用分号")
    private String valuesList;

    /**
     * 参数值，多值使用分号分割
     */
    @Column(name = "C_VALUE", columnDefinition = "VARCHAR")
    @MetaData(value = "参数值", comments = "参数值，多值使用分号分割")
    private String itemValue;

    /**
     * 参数排序号
     */
    @Column(name = "N_SORT", columnDefinition = "INTEGER")
    @MetaData(value = "参数排序号", comments = "参数排序号")
    private Integer configOrder;

    /**
     * 状态，0 停用 1启用
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态，0 停用 1启用")
    private String status;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
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
     * 参数修改显示控制；0可修改，可显示；1可显示，不可修改，2不显示；不修改
     */
    @Column(name = "C_MODIFY", columnDefinition = "VARCHAR")
    @MetaData(value = "参数修改显示控制", comments = "参数修改显示控制；0可修改，可显示；1可显示，不可修改，2不显示；不修改")
    private String dataModify;

    /**
     * 是否分割为,0不分割，1分割为数组，2分割为有序的MAP
     */
    @Column(name = "C_SPLIT",  columnDefinition="VARCHAR" )
    @MetaData( value="是否分割为,0不分割", comments = "是否分割为,0不分割，1分割为数组，2分割为有序的MAP，3处理为JSON")
    private String split;

    private static final long serialVersionUID = 1442539923537L;

    public Integer getId() {
        return id;
    }

    public DataTypeInfo findDataType() {

        return DataTypeInfo.checking(this.dataType);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Integer getDisWidth() {
        return disWidth;
    }

    public void setDisWidth(Integer disWidth) {
        this.disWidth = disWidth;
    }

    public String getDictItem() {
        return dictItem;
    }

    public void setDictItem(String dictItem) {
        this.dictItem = dictItem;
    }

    public String getValuesList() {
        return valuesList;
    }

    public void setValuesList(String valuesList) {
        this.valuesList = valuesList;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Integer getConfigOrder() {
        return configOrder;
    }

    public void setConfigOrder(Integer configOrder) {
        this.configOrder = configOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataModify() {
        return dataModify;
    }

    public void setDataModify(String dataModify) {
        this.dataModify = dataModify;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sysName=").append(sysName);
        sb.append(", groupNo=").append(groupNo);
        sb.append(", itemName=").append(itemName);
        sb.append(", dataType=").append(dataType);
        sb.append(", dataLen=").append(dataLen);
        sb.append(", dataScale=").append(dataScale);
        sb.append(", dataScope=").append(dataScope);
        sb.append(", workMode=").append(workMode);
        sb.append(", itemType=").append(itemType);
        sb.append(", disWidth=").append(disWidth);
        sb.append(", dictItem=").append(dictItem);
        sb.append(", valuesList=").append(valuesList);
        sb.append(", itemValue=").append(itemValue);
        sb.append(", configOrder=").append(configOrder);
        sb.append(", status=").append(status);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", description=").append(description);
        sb.append(", dataModify=").append(dataModify);
        sb.append(", split=").append(split);
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
        SysConfigInfo other = (SysConfigInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSysName() == null ? other.getSysName() == null : this.getSysName().equals(other.getSysName()))
                && (this.getGroupNo() == null ? other.getGroupNo() == null : this.getGroupNo().equals(other.getGroupNo()))
                && (this.getItemName() == null ? other.getItemName() == null : this.getItemName().equals(other.getItemName()))
                && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
                && (this.getDataLen() == null ? other.getDataLen() == null : this.getDataLen().equals(other.getDataLen()))
                && (this.getDataScale() == null ? other.getDataScale() == null : this.getDataScale().equals(other.getDataScale()))
                && (this.getDataScope() == null ? other.getDataScope() == null : this.getDataScope().equals(other.getDataScope()))
                && (this.getWorkMode() == null ? other.getWorkMode() == null : this.getWorkMode().equals(other.getWorkMode()))
                && (this.getItemType() == null ? other.getItemType() == null : this.getItemType().equals(other.getItemType()))
                && (this.getDisWidth() == null ? other.getDisWidth() == null : this.getDisWidth().equals(other.getDisWidth()))
                && (this.getDictItem() == null ? other.getDictItem() == null : this.getDictItem().equals(other.getDictItem()))
                && (this.getValuesList() == null ? other.getValuesList() == null : this.getValuesList().equals(other.getValuesList()))
                && (this.getItemValue() == null ? other.getItemValue() == null : this.getItemValue().equals(other.getItemValue()))
                && (this.getConfigOrder() == null ? other.getConfigOrder() == null : this.getConfigOrder().equals(other.getConfigOrder()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getDataModify() == null ? other.getDataModify() == null : this.getDataModify().equals(other.getDataModify()))
                && (this.getSplit() == null ? other.getSplit() == null : this.getSplit().equals(other.getSplit()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSysName() == null) ? 0 : getSysName().hashCode());
        result = prime * result + ((getGroupNo() == null) ? 0 : getGroupNo().hashCode());
        result = prime * result + ((getItemName() == null) ? 0 : getItemName().hashCode());
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getDataLen() == null) ? 0 : getDataLen().hashCode());
        result = prime * result + ((getDataScale() == null) ? 0 : getDataScale().hashCode());
        result = prime * result + ((getDataScope() == null) ? 0 : getDataScope().hashCode());
        result = prime * result + ((getWorkMode() == null) ? 0 : getWorkMode().hashCode());
        result = prime * result + ((getItemType() == null) ? 0 : getItemType().hashCode());
        result = prime * result + ((getDisWidth() == null) ? 0 : getDisWidth().hashCode());
        result = prime * result + ((getDictItem() == null) ? 0 : getDictItem().hashCode());
        result = prime * result + ((getValuesList() == null) ? 0 : getValuesList().hashCode());
        result = prime * result + ((getItemValue() == null) ? 0 : getItemValue().hashCode());
        result = prime * result + ((getConfigOrder() == null) ? 0 : getConfigOrder().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getDataModify() == null) ? 0 : getDataModify().hashCode());
        result = prime * result + ((getSplit() == null) ? 0 : getSplit().hashCode());
        return result;
    }

    @Override
    public String getClassType() {

        return this.valuesList;
    }
}
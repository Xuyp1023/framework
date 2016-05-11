package com.betterjr.modules.sys.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CFG_DICTITEM")
public class DictItemInfo implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号")
    private Integer id;

    /**
     * 数据字典
     */
    @Column(name = "L_ITEMNO",  columnDefinition="INTEGER" )
    @MetaData( value="数据字典", comments = "数据字典")
    private Integer itemNo;

    /**
     * 字典值
     */
    @Column(name = "C_VALUE",  columnDefinition="VARCHAR" )
    @MetaData( value="字典值", comments = "字典值")
    private String itemValue;

    /**
     * 字典编码
     */
    @Column(name = "C_CODE",  columnDefinition="VARCHAR" )
    @MetaData( value="字典编码", comments = "字典编码")
    private String itemCode;

    /**
     * 字典内容
     */
    @Column(name = "C_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="字典内容", comments = "字典内容")
    private String itemName;

    /**
     * 主题
     */
    @Column(name = "C_SUBJECT",  columnDefinition="VARCHAR" )
    @MetaData( value="主题", comments = "主题")
    private String subject;

    /**
     * 排序序号
     */
    @Column(name = "L_SORT",  columnDefinition="INTEGER" )
    @MetaData( value="排序序号", comments = "排序序号")
    @OrderBy
    private Integer itemOrder;

    /**
     * 状态，0 停用 1启用，停用只能显示，即增加、修改字段不显示该条目
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "状态，0 停用 1启用，停用只能显示，即增加、修改字段不显示该条目")
    private Boolean status;

    /**
     * 默认值
     */
    @Column(name = "C_DEFAULT",  columnDefinition="VARCHAR" )
    @MetaData( value="默认值", comments = "默认值")
    private Boolean defItem;

    /**
     * 描述
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="描述", comments = "描述")
    private String description;

    /**
     * 是否输出到前端
     */
    @Column(name = "C_OUT",  columnDefinition="VARCHAR" )
    @MetaData( value="是否输出到前端", comments = "是否输出到前端")
    private Boolean exportOut;

    private static final long serialVersionUID = 1439623644389L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue == null ? null : itemValue.trim();
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public Integer getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getDefItem() {
        return defItem;
    }

    public void setDefItem(Boolean defItem) {
        this.defItem = defItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Boolean getExportOut() {
        return exportOut;
    }

    public void setExportOut(Boolean exportOut) {
        this.exportOut = exportOut;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", itemNo=").append(itemNo);
        sb.append(", itemValue=").append(itemValue);
        sb.append(", itemCode=").append(itemCode);
        sb.append(", itemName=").append(itemName);
        sb.append(", subject=").append(subject);
        sb.append(", itemOrder=").append(itemOrder);
        sb.append(", status=").append(status);
        sb.append(", defItem=").append(defItem);
        sb.append(", description=").append(description);
        sb.append(", exportOut=").append(exportOut);
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
        DictItemInfo other = (DictItemInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getItemNo() == null ? other.getItemNo() == null : this.getItemNo().equals(other.getItemNo()))
            && (this.getItemValue() == null ? other.getItemValue() == null : this.getItemValue().equals(other.getItemValue()))
            && (this.getItemCode() == null ? other.getItemCode() == null : this.getItemCode().equals(other.getItemCode()))
            && (this.getItemName() == null ? other.getItemName() == null : this.getItemName().equals(other.getItemName()))
            && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
            && (this.getItemOrder() == null ? other.getItemOrder() == null : this.getItemOrder().equals(other.getItemOrder()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getDefItem() == null ? other.getDefItem() == null : this.getDefItem().equals(other.getDefItem()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getExportOut() == null ? other.getExportOut() == null : this.getExportOut().equals(other.getExportOut()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getItemNo() == null) ? 0 : getItemNo().hashCode());
        result = prime * result + ((getItemValue() == null) ? 0 : getItemValue().hashCode());
        result = prime * result + ((getItemCode() == null) ? 0 : getItemCode().hashCode());
        result = prime * result + ((getItemName() == null) ? 0 : getItemName().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + ((getItemOrder() == null) ? 0 : getItemOrder().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDefItem() == null) ? 0 : getDefItem().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getExportOut() == null) ? 0 : getExportOut().hashCode());
        return result;
    }
}
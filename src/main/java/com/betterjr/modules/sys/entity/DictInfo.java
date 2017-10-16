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
@Table(name = "T_CFG_DICT")
public class DictInfo implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Integer id;

    /**
     * 所属系统简称
     */
    @Column(name = "C_SYS", columnDefinition = "VARCHAR")
    @MetaData(value = "所属系统简称", comments = "所属系统简称")
    private String sysNo;

    /**
     * 字典名称
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "字典名称", comments = "字典名称")
    private String dictName;

    /**
     * 分组编号；主要按用途分组，便于使用
     */
    @Column(name = "C_GROUP", columnDefinition = "VARCHAR")
    @MetaData(value = "分组编号", comments = "分组编号；主要按用途分组，便于使用")
    private String groupNo;

    /**
     * 是否允许修改；0禁止修改，1允许修改
     */
    @Column(name = "C_MODIFY", columnDefinition = "VARCHAR")
    @MetaData(value = "是否允许修改", comments = "是否允许修改；0禁止修改，1允许修改")
    private Boolean dataModify;

    /**
     * 备注，注明用途和使用
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "备注", comments = "备注，注明用途和使用")
    private String description;

    /**
     * 创建者
     */
    @Column(name = "C_OWNER", columnDefinition = "VARCHAR")
    @MetaData(value = "创建者", comments = "创建者")
    private String owner;

    /**
     * 修改时间
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改时间", comments = "修改时间")
    private String modiDate;

    /**
     * 是否输出到前端脚本；0不输出，1JAVASCIPT；2APP应用
     */
    @Column(name = "C_SCRIPT", columnDefinition = "VARCHAR")
    @MetaData(value = "是否输出到前端脚本", comments = "是否输出到前端脚本；0不输出，1JAVASCIPT；2APP应用")
    private String outScipt;

    /**
     * 字典编码，也是输出到前端的对象名称
     */
    @Column(name = "C_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "字典编码", comments = "字典编码，也是输出到前端的对象名称")
    private String dictCode;

    private static final long serialVersionUID = 2004210232843958557L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo == null ? null : sysNo.trim();
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName == null ? null : dictName.trim();
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo == null ? null : groupNo.trim();
    }

    public Boolean getDataModify() {
        return dataModify;
    }

    public void setDataModify(Boolean dataModify) {
        this.dataModify = dataModify;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getOutScipt() {
        return outScipt;
    }

    public void setOutScipt(String outScipt) {
        this.outScipt = outScipt == null ? null : outScipt.trim();
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode == null ? null : dictCode.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sysNo=").append(sysNo);
        sb.append(", dictName=").append(dictName);
        sb.append(", groupNo=").append(groupNo);
        sb.append(", dataModify=").append(dataModify);
        sb.append(", description=").append(description);
        sb.append(", owner=").append(owner);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", outScipt=").append(outScipt);
        sb.append(", dictCode=").append(dictCode);
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
        DictInfo other = (DictInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSysNo() == null ? other.getSysNo() == null : this.getSysNo().equals(other.getSysNo()))
                && (this.getDictName() == null ? other.getDictName() == null
                        : this.getDictName().equals(other.getDictName()))
                && (this.getGroupNo() == null ? other.getGroupNo() == null
                        : this.getGroupNo().equals(other.getGroupNo()))
                && (this.getDataModify() == null ? other.getDataModify() == null
                        : this.getDataModify().equals(other.getDataModify()))
                && (this.getDescription() == null ? other.getDescription() == null
                        : this.getDescription().equals(other.getDescription()))
                && (this.getOwner() == null ? other.getOwner() == null : this.getOwner().equals(other.getOwner()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()))
                && (this.getOutScipt() == null ? other.getOutScipt() == null
                        : this.getOutScipt().equals(other.getOutScipt()))
                && (this.getDictCode() == null ? other.getDictCode() == null
                        : this.getDictCode().equals(other.getDictCode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSysNo() == null) ? 0 : getSysNo().hashCode());
        result = prime * result + ((getDictName() == null) ? 0 : getDictName().hashCode());
        result = prime * result + ((getGroupNo() == null) ? 0 : getGroupNo().hashCode());
        result = prime * result + ((getDataModify() == null) ? 0 : getDataModify().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getOwner() == null) ? 0 : getOwner().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getOutScipt() == null) ? 0 : getOutScipt().hashCode());
        result = prime * result + ((getDictCode() == null) ? 0 : getDictCode().hashCode());
        return result;
    }
}
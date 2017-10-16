package com.betterjr.modules.sys.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.betterjr.mapper.entity.ReferClass;

@ReferClass(DictInfo.class)
public class DictInfoTest implements java.io.Serializable {

    private static final long serialVersionUID = 4022139141687749821L;

    private Integer id;

    @NotNull()
    @Size(min = 3, max = 9)
    private String sysNo;

    private String dictName;

    private String groupNo;

    private String dictCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysNo() {
        return sysNo;
    }

    public String getSysName() {
        return sysNo + "Test";
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
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
        sb.append(", dictCode=").append(dictCode);
        sb.append("]");
        return sb.toString();
    }

}
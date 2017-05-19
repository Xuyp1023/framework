package com.betterjr.modules.document.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;

import java.io.File;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CFG_AUTH_FILEGROUP_AGENCY")
public class AgencyAuthorFileGroup implements BetterjrEntity {

    /**
     * 销售人代码
     */
    @Column(name = "C_AGENCYNO",  columnDefinition="VARCHAR" )
    @MetaData( value="销售人代码", comments = "销售人代码")
    private String agencyNo;

    /**
     * 业务代码
     */
    @Column(name = "C_BUSINFLAG",  columnDefinition="VARCHAR" )
    @MetaData( value="业务代码", comments = "业务代码；取文件的分割")
    private String businFlag;

    /**
     * 文件信息类型
     */
    @Column(name = "C_FILEINFOTYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="文件信息类型", comments = "文件信息类")
    private String fileInfoType;

    /**
     * 业务描述
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="业务描述", comments = "业务描述")
    private String description;
    
    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 失效时间
     */
    @Column(name = "N_INVALID_TIME",  columnDefinition="INTEGER" )
    @MetaData( value="修改日期", comments = "修改日期")
    private Integer invalidTime;

    /**
     * 状态，0 停用 ;1启用
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态，0 停用;  1启用")
    private String groupStatus;

    /**
     * 业务分类,fund基金，scf供应链金融，money理财
     */
    @Column(name = "C_BUSINCLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "业务分类", comments = "fund基金，scf供应链金融，money理财")
    private String businClass;

    /**
     *文档样张文件路径
     */
    @Column(name = "c_demo_file_path", columnDefinition = "VARCHAR")
    @MetaData(value = "文档样张文件路径", comments = "文档样张文件路径")
    private String demoFilePath;

    /**
     * 模板文件路径
     */
    @Column(name = "c_temp_file_path", columnDefinition = "VARCHAR")
    @MetaData(value = "模板文件路径", comments = "模板文件路径")
    private String tempFilePath;
    
    /**
     * 文件模板类型，0：业务表格文件，1：电子模板文件
     */
    @Column(name = "c_file_class", columnDefinition = "VARCHAR")
    @MetaData(value = "文件模板类型", comments = "文件模板类型，0：业务表格文件，1：电子模板文件")
    private String modeFileClass;
    
    private static final long serialVersionUID = 1440667936389L;
     public String getAgencyNo() {
        return agencyNo;
    }

    public void setAgencyNo(String agencyNo) {
        this.agencyNo = agencyNo == null ? null : agencyNo.trim();
    }

    public String getBusinFlag() {
        return businFlag;
    }

    public void setBusinFlag(String businFlag) {
        this.businFlag = businFlag == null ? null : businFlag.trim();
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

    public String getFileInfoType() {
        return this.fileInfoType;
    }

    public void setFileInfoType(String anFileInfoType) {
        this.fileInfoType = anFileInfoType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

    public Integer getInvalidTime() {
        return this.invalidTime;
    }

    public void setInvalidTime(Integer anInvalidTime) {
        this.invalidTime = anInvalidTime;
    }

    public String getGroupStatus() {
        return this.groupStatus;
    }

    public void setGroupStatus(String anGroupStatus) {
        this.groupStatus = anGroupStatus;
    }

    public String getBusinClass() {
        return this.businClass;
    }

    public void setBusinClass(String anBusinClass) {
        this.businClass = anBusinClass;
    }

    /**
     * 获得组合的主键
     * @return
     */
    public String findComposeKey(){
        
        return this.agencyNo.concat(",").concat(this.fileInfoType).concat(",").concat(businFlag);
    }
    
    public String getDemoFilePath() {
        return this.demoFilePath;
    }

    public void setDemoFilePath(String anDemoFilePath) {
        this.demoFilePath = anDemoFilePath;
    }

    public String getTempFilePath() {
        return this.tempFilePath;
    }

    public void setTempFilePath(String anTempFilePath) {
        this.tempFilePath = anTempFilePath;
    }

    public String getModeFileClass() {
        return this.modeFileClass;
    }

    public void setModeFileClass(String anModeFileClass) {
        this.modeFileClass = anModeFileClass;
    }

    /**
     * 查找demo文件路径
     * @return
     */
    public String findDemoPath(){
        
        return this.agencyNo.concat(File.separator).concat("demo").concat(this.fileInfoType);
    }
    
    /**
     * 查找电子模板文件路径
     * @return
     */
    public String findTempPath(){
       if ("0".equals(this.modeFileClass) || "9".equals(this.modeFileClass)){           
           if ("0".equals(this.modeFileClass)){
               return this.agencyNo.concat(File.separator).concat("normalTemp").concat(this.fileInfoType);
           }
           else{
               return "";
           }
       }
       else{
           
           return this.agencyNo.concat(File.separator).concat("elecTemp").concat(this.fileInfoType);
       }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("fileInfoType=").append(fileInfoType);
        sb.append(", agencyNo=").append(agencyNo);
        sb.append(", businFlag=").append(businFlag);
        sb.append(", description=").append(description);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", invalidTime=").append(invalidTime);
        sb.append(", groupStatus=").append(groupStatus);
        sb.append(", businClass=").append(businClass);
        sb.append(", demoFilePath=").append(demoFilePath);
        sb.append(", tempFilePath=").append(tempFilePath);
        sb.append(", modeFileClass=").append(modeFileClass);
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
        AgencyAuthorFileGroup other = (AgencyAuthorFileGroup) that;
        return (this.getFileInfoType() == null ? other.getFileInfoType() == null : this.getFileInfoType().equals(other.getFileInfoType()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getAgencyNo() == null ? other.getAgencyNo() == null : this.getAgencyNo().equals(other.getAgencyNo()))
            && (this.getBusinFlag() == null ? other.getBusinFlag() == null : this.getBusinFlag().equals(other.getBusinFlag()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getInvalidTime() == null ? other.getInvalidTime() == null : this.getInvalidTime().equals(other.getInvalidTime()))
            && (this.getGroupStatus() == null ? other.getGroupStatus() == null : this.getGroupStatus().equals(other.getGroupStatus()))
            && (this.getBusinClass() == null ? other.getBusinClass() == null : this.getBusinClass().equals(other.getBusinClass()))
            && (this.getDemoFilePath() == null ? other.getDemoFilePath() == null : this.getDemoFilePath().equals(other.getDemoFilePath()))
            && (this.getTempFilePath() == null ? other.getTempFilePath() == null : this.getTempFilePath().equals(other.getTempFilePath()))
            && (this.getBusinClass() == null ? other.getBusinClass() == null : this.getBusinClass().equals(other.getBusinClass()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFileInfoType() == null) ? 0 : getFileInfoType().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getAgencyNo() == null) ? 0 : getAgencyNo().hashCode());
        result = prime * result + ((getBusinFlag() == null) ? 0 : getBusinFlag().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getInvalidTime() == null) ? 0 : getInvalidTime().hashCode());
        result = prime * result + ((getGroupStatus() == null) ? 0 : getGroupStatus().hashCode());
        result = prime * result + ((getBusinClass() == null) ? 0 : getBusinClass().hashCode());
        result = prime * result + ((getDemoFilePath() == null) ? 0 : getDemoFilePath().hashCode());
        result = prime * result + ((getTempFilePath() == null) ? 0 : getTempFilePath().hashCode());
        result = prime * result + ((getBusinClass() == null) ? 0 : getBusinClass().hashCode());
        return result;
    }
}
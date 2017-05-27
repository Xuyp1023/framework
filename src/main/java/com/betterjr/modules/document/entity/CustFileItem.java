package com.betterjr.modules.document.entity;

import java.io.File;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_FILEITEM")
public class CustFileItem implements BetterjrEntity,java.io.Serializable {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Long id;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;

    /**
     * 文件名称
     */
    @Column(name = "C_FILENAME", columnDefinition = "VARCHAR")
    @MetaData(value = "文件名称", comments = "文件名称")
    private String fileName;

    /**
     * 文件类型
     */
    @Column(name = "C_FILETYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "文件类型", comments = "文件类型")
    private String fileType;

    /**
     * 保存的文件编码
     */
    @Column(name = "C_FILENO", columnDefinition = "VARCHAR")
    @MetaData(value = "保存的文件编码", comments = "保存的文件编码")
    private String fileNo;

    /**
     * 保存文件路径，相对路径
     */

    @JsonIgnore
    @Column(name = "C_FILEPATH", columnDefinition = "VARCHAR")
    @MetaData(value = "保存文件路径", comments = "保存文件路径，相对路径")
    private String filePath;

    /**
     * 文件大小
     */
    @Column(name = "N_LENGTH", columnDefinition = "INTEGER")
    @MetaData(value = "文件大小", comments = "文件大小")
    private Long fileLength;

    /**
     * 注册日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "注册日期", comments = "注册日期")
    private String regDate;

    /**
     * 注册时间
     */
    @Column(name = "D_REGTIME", columnDefinition = "VARCHAR")
    @MetaData(value = "注册时间", comments = "注册时间")
    private String regTime;

    /**
     *
     */
    @Column(name = "C_FILEINFOTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "文件信息类型", comments = "文件信息类")
    private String fileInfoType;


    /**
     * 创建人(操作员)ID号
     */
    @JsonIgnore
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "创建人(操作员)ID号", comments = "创建人(操作员)ID号")
    private Long regOperId;

    /**
     * 创建人(操作员)姓名
     */
    @JsonIgnore
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建人(操作员)姓名", comments = "创建人(操作员)姓名")
    private String regOperName;

    /**
     * 修改人(操作员)ID号
     */
    @JsonIgnore
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "修改人(操作员)ID号", comments = "修改人(操作员)ID号")
    private Long modiOperId;

    /**
     * 修改人(操作员)姓名
     */
    @JsonIgnore
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改人(操作员)姓名", comments = "修改人(操作员)姓名")
    private String modiOperName;

    /**
     * 修改日期
     */
    @JsonIgnore
    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 修改时间
     */
    @JsonIgnore
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改时间", comments = "修改时间")
    private String modiTime;

    /**
     * 操作机构
     */
    @JsonIgnore
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 0:文件系统，1:阿里云
     */
    @JsonIgnore
    @Column(name = "C_STORETYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "0:文件系统，1:阿里云", comments = "0:文件系统，1:阿里云")
    private String storeType;

    @Transient
    private CustFileInfo fileInfo;
    
    /***
     * 文件类型名称
     */
    @Transient
    private String fileDescription;
    /***
     * 是否通过
     */
    @Transient
    private String businStatus;
    
 
    public String getFileDescription() {
        return this.fileDescription;
    }

    public void setFileDescription(String anFileDescription) {
        this.fileDescription = anFileDescription;
    }

    public String getStoreType() {
        return this.storeType;
    }

    public void setStoreType(String anStoreType) {
        this.storeType = anStoreType;
    }

    private static final long serialVersionUID = 1440667936398L;

    public CustFileInfo getFileInfo() {
        return this.fileInfo;
    }

    public void setFileInfo(final CustFileInfo anFileInfo) {
        this.fileInfo = anFileInfo;
    }

    public String getFileInfoType() {
        return this.fileInfoType;
    }

    public void setFileInfoType(final String anFileInfoType) {
        this.fileInfoType = anFileInfoType;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(final Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(final String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(final String fileNo) {
        this.fileNo = fileNo == null ? null : fileNo.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(final Long fileLength) {
        this.fileLength = fileLength;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(final String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(final String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public Long getRegOperId() {
        return regOperId;
    }

    public void setRegOperId(final Long anRegOperId) {
        regOperId = anRegOperId;
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(final String anRegOperName) {
        regOperName = anRegOperName;
    }

    public Long getModiOperId() {
        return modiOperId;
    }

    public void setModiOperId(final Long anModiOperId) {
        modiOperId = anModiOperId;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    public void setModiOperName(final String anModiOperName) {
        modiOperName = anModiOperName;
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(final String anModiDate) {
        modiDate = anModiDate;
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(final String anModiTime) {
        modiTime = anModiTime;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(final String anOperOrg) {
        operOrg = anOperOrg;
    }
 
    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", fileName=").append(fileName);
        sb.append(", fileType=").append(fileType);
        sb.append(", fileNo=").append(fileNo);
        sb.append(", filePath=").append(filePath);
        sb.append(", fileLength=").append(fileLength);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", fileInfoType=").append(fileInfoType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        final CustFileItem other = (CustFileItem) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
                && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
                && (this.getFileType() == null ? other.getFileType() == null : this.getFileType().equals(other.getFileType()))
                && (this.getFileNo() == null ? other.getFileNo() == null : this.getFileNo().equals(other.getFileNo()))
                && (this.getFilePath() == null ? other.getFilePath() == null : this.getFilePath().equals(other.getFilePath()))
                && (this.getFileLength() == null ? other.getFileLength() == null : this.getFileLength().equals(other.getFileLength()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
                && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
                && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getFileInfoType() == null ? other.getFileInfoType() == null : this.getFileInfoType().equals(other.getFileInfoType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getFileType() == null) ? 0 : getFileType().hashCode());
        result = prime * result + ((getFileNo() == null) ? 0 : getFileNo().hashCode());
        result = prime * result + ((getFilePath() == null) ? 0 : getFilePath().hashCode());
        result = prime * result + ((getFileLength() == null) ? 0 : getFileLength().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getFileInfoType() == null) ? 0 : getFileInfoType().hashCode());
        return result;
    }

    public boolean isInner() {

        return isInner(null);
    }

    public boolean isInner(String anType) {
        if (BetterStringUtils.isBlank(anType)) {
            anType = this.fileType;
        }
        if (BetterStringUtils.isNotBlank(anType)) {
            final String[] arrStr = new String[] { "image", "text", "html", "plain" };
            for (final String tmpStr : arrStr) {
                if (anType.indexOf(tmpStr) > -1) {
                    return true;
                }
            }
        }

        return false;
    }

    public void initDuplicateConflictValue(final CustFileItem anFileItem) {
        this.id = (SerialGenerator.getLongValue("CustFileItem.id"));
        this.fileLength = anFileItem.getFileLength();
        this.fileInfoType = anFileItem.getFileInfoType();
        this.filePath = anFileItem.getFilePath();
        this.fileName = anFileItem.getFileName();
        this.fileType = anFileItem.getFileType();
        this.storeType = anFileItem.getStoreType();
    }

    public void initAddValue(final CustOperatorInfo anOperator) {
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.regOperId = anOperator.getId();
        this.regOperName = anOperator.getName();
        this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
        this.modiOperId = anOperator.getId();
        this.modiOperName = anOperator.getName();
        this.operOrg = anOperator.getOperOrg();
    }

    public void initModifyValue(CustOperatorInfo anOperator){
         this.modiDate = BetterDateUtils.getNumDate();
        this.modiTime = BetterDateUtils.getNumTime();
        this.modiOperId = anOperator.getId();
        this.modiOperName = anOperator.getName();
    }
}
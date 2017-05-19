package com.betterjr.modules.document.entity;

import com.alibaba.rocketmq.common.protocol.header.QueryMessageRequestHeader;
import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.document.data.FileStoreType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CFG_AUTH_FILEGROUP")
public class AuthorFileGroup implements BetterjrEntity {

    /**
     * 文件信息类型
     */
    @Id
    @Column(name = "C_FILEINFOTYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="文件信息类型", comments = "文件信息类")
    private String fileInfoType;

    /**
     * 业务代码 00：其它文件，01账户文件,02交易文件
     */
    @Column(name = "C_BUSINFLAG",  columnDefinition="VARCHAR" )
    @MetaData( value="业务代码", comments = "业务代码；取文件的分割")
    private String businFlag;

    /**
     * 业务描述
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="业务描述", comments = "业务描述")
    private String description;
    
    /**
     * 适用范围
     */
    @Column(name = "C_SUB_RANG",  columnDefinition="VARCHAR" )
    @MetaData( value="适用范围", comments = "适用范围")
    private String workRang;
 
    /**
     * 状态，0 停用 ;1启用
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态，0 停用;  1启用")
    private String groupStatus;

    /**
     * 缺少的消息提示
     */
    @Column(name = "C_DEFICIENCYINFO", columnDefinition = "VARCHAR")
    @MetaData(value = "缺少的消息提示", comments = "缺少的消息提示")
    private String deficiencyInfo;

    /**
     * 存儲路徑
     */
    @JsonIgnore
    @Column(name = "c_storepath", columnDefinition = "VARCHAR")
    @MetaData(value = "存储路径", comments = "存储路径")
    private String storePath;

    /**
     * 0:文件系统，1:阿里云
     */
    @JsonIgnore
    @Column(name = "C_STORETYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "0:文件系统，1:阿里云", comments = "0:文件系统，1:阿里云")
    private String storeType;

    /**
     * 该类型允许的文件类型，多个使用逗号分隔，默认是全部平台允许的类型
     */
    @JsonIgnore
    @Column(name = "c_permit_filetypes", columnDefinition = "VARCHAR")
    @MetaData(value = "该类型允许的文件类型，多个使用逗号分隔，默认是全部平台允许的类型", comments = "该类型允许的文件类型，多个使用逗号分隔，默认是全部平台允许的类型")
    private String permitFileTypes;
    
    private static final long serialVersionUID = 1440667936389L;
 
    public String getStoreType() {
        return this.storeType;
    }

    public void setStoreType(String anStoreType) {
        this.storeType = anStoreType;
    }

    public String getBusinFlag() {
        return businFlag;
    }

    public void setBusinFlag(String businFlag) {
        this.businFlag = businFlag == null ? null : businFlag.trim();
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
 
    public String getGroupStatus() {
        return this.groupStatus;
    }

    public void setGroupStatus(String anGroupStatus) {
        this.groupStatus = anGroupStatus;
    }
  
    public String getWorkRang() {
        return this.workRang;
    }

    public void setWorkRang(String anWorkRang) {
        this.workRang = anWorkRang;
    }

    public String getDeficiencyInfo() {
        return this.deficiencyInfo;
    }

    public void setDeficiencyInfo(String anDeficiencyInfo) {
        this.deficiencyInfo = anDeficiencyInfo;
    }

    public String getPermitFileTypes() {
        return this.permitFileTypes;
    }

    public void setPermitFileTypes(String anPermitFileTypes) {
        this.permitFileTypes = anPermitFileTypes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("fileInfoType=").append(fileInfoType);
        sb.append(", workRang=").append(workRang);
        sb.append(", businFlag=").append(businFlag);
        sb.append(", description=").append(description);
        sb.append(", deficiencyInfo=").append(deficiencyInfo);
        sb.append(", groupStatus=").append(groupStatus);
        sb.append(", storeType=").append(storeType);
        sb.append(", storePath=").append(storePath);
        sb.append(", permitFileTypes=").append(permitFileTypes);
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
        AuthorFileGroup other = (AuthorFileGroup) that;
        return (this.getFileInfoType() == null ? other.getFileInfoType() == null : this.getFileInfoType().equals(other.getFileInfoType()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getWorkRang() == null ? other.getWorkRang() == null : this.getWorkRang().equals(other.getWorkRang()))
            && (this.getBusinFlag() == null ? other.getBusinFlag() == null : this.getBusinFlag().equals(other.getBusinFlag()))
            && (this.getDeficiencyInfo() == null ? other.getDeficiencyInfo() == null : this.getDeficiencyInfo().equals(other.getDeficiencyInfo()))
            && (this.getStoreType() == null ? other.getStoreType() == null : this.getStoreType().equals(other.getStoreType()))
            && (this.getStorePath() == null ? other.getStorePath() == null : this.getStorePath().equals(other.getStorePath()))
            && (this.getPermitFileTypes() == null ? other.getPermitFileTypes() == null : this.getPermitFileTypes().equals(other.getPermitFileTypes()))
            && (this.getGroupStatus() == null ? other.getGroupStatus() == null : this.getGroupStatus().equals(other.getGroupStatus()));
    }

    public String getStorePath() {
        return this.storePath;
    }

    public void setStorePath(String anStorePath) {
        this.storePath = anStorePath;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFileInfoType() == null) ? 0 : getFileInfoType().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getWorkRang() == null) ? 0 : getWorkRang().hashCode());
        result = prime * result + ((getBusinFlag() == null) ? 0 : getBusinFlag().hashCode());
        result = prime * result + ((getDeficiencyInfo() == null) ? 0 : getDeficiencyInfo().hashCode());
        result = prime * result + ((getGroupStatus() == null) ? 0 : getGroupStatus().hashCode());
        result = prime * result + ((getStoreType() == null) ? 0 : getStoreType().hashCode());
        result = prime * result + ((getStorePath() == null) ? 0 : getStorePath().hashCode());
        result = prime * result + ((getPermitFileTypes() == null) ? 0 : getPermitFileTypes().hashCode());
        return result;
    }
    
    public AuthorFileGroup(){
        
    }
    
    public AuthorFileGroup(String anBusinClass, String anFileInfoType){
        this.businFlag = anBusinClass;
        this.fileInfoType = anFileInfoType;
    }
    
    private static String findStorePath(String anBusinFlag){
        String tmpStr =(String) QueryTermBuilder.newInstance().put("00", "other").put("01", "account").put("02", "trade").build().get(anBusinFlag);
        if (BetterStringUtils.isBlank(tmpStr)){
            tmpStr = "other";
        }
        return tmpStr;
    }
    
    /**
     * 查找文件创建路径信息
     * @return
     */
    public String findCreateFilePath(FileStoreType anStoreType){
       StringBuilder sb = new StringBuilder();
       if (anStoreType != FileStoreType.OSS_STORE){
           sb.append("/");
       }
       sb.append(BetterDateUtils.getNumDate()).append("/");
       sb.append(findStorePath(this.businFlag)).append("/");
       if (BetterStringUtils.isNotBlank(this.storePath)){
          sb.append(this.storePath).append("/"); 
       }
       if (BetterStringUtils.isNotBlank(fileInfoType)){
          sb.append(this.fileInfoType).append("/");
       }
       sb.append(SerialGenerator.uuid());
       return sb.toString();
    }
    
    /**
     *检查该业务类型允许的文件类型 
     * @param anFileType 文件类型
     * @return
     */
    public boolean checkFileType(String anFileType){
       if (BetterStringUtils.isBlank(this.permitFileTypes) || BetterStringUtils.isBlank(anFileType)){
 
           return true;
       }
       
       return this.permitFileTypes.indexOf(anFileType) > -1;
    }
}
package com.betterjr.modules.document.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_FILEADUIT")
public class CustFileAduit implements BetterjrEntity {
    /**
     * 编号；使用FileInfo的序号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号；使用FileInfo的序号")
    private Long id;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 文件数量
     */
    @Column(name = "N_COUNT",  columnDefinition="INTEGER" )
    @MetaData( value="文件数量", comments = "文件数量")
    private Integer fileCount;

    /**
     * 审批状态；0：未审批，1：审批通过，2：审核资料待完善，3：审批中，4：平台初审，9：审批失败
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="审批状态", comments = "审批状态；0：未审批，1：审批通过，2：审核资料待完善，3：审批中，4：平台初审，9：审批失败")
    private String auditStatus;

    /**
     * 类型
     */
    @Column(name = "C_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="类型", comments = "类型")
    private String workType;

    /**
     * 业务描述
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="业务描述", comments = "业务描述")
    private String description;

    /**
     * 注册日期
     */
    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="注册日期", comments = "注册日期")
    private String regDate;

    /**
     * 注册时间
     */
    @Column(name = "D_REGTIME",  columnDefinition="VARCHAR" )
    @MetaData( value="注册时间", comments = "注册时间")
    private String regTime;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 操作员编码
     */
    @Column(name = "C_OPERNO",  columnDefinition="VARCHAR" )
    @MetaData( value="操作员编码", comments = "操作员编码")
    private String operNo;

    /**
     * 审批单位编码，通常为保理公司
     */
    @Column(name = "L_ADUIT_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="审批单位编码", comments = "审批单位编码，通常为保理公司")
    private Long aduitCustNo;

    /**
     * 审批单位的操作员编码
     */
    @Column(name = "C_ADUITORNO",  columnDefinition="VARCHAR" )
    @MetaData( value="审批单位的操作员编码", comments = "审批单位的操作员编码")
    private String aduitorNo;

    /**
     * 审批时间
     */
    @Column(name = "D_ADUIT_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="审批时间", comments = "审批时间")
    private String authorTime;
    
    /**
     * 用户认证文件的ID字符串
     */
    @Transient
    private String fileIds;
    
    /**
     * 包含认证文件ID和name的字符串数组
     */
    @Transient
    private String[] fileList;

    private static final long serialVersionUID = 1459952422046L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus == null ? null : auditStatus.trim();
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType == null ? null : workType.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(String operNo) {
        this.operNo = operNo == null ? null : operNo.trim();
    }

    public Long getAduitCustNo() {
        return aduitCustNo;
    }

    public void setAduitCustNo(Long aduitCustNo) {
        this.aduitCustNo = aduitCustNo;
    }

    public String getAduitorNo() {
        return aduitorNo;
    }

    public void setAduitorNo(String anAduitorNo) {
        this.aduitorNo = anAduitorNo == null ? null : anAduitorNo.trim();
    }
    
    public String getFileIds() {
        return this.fileIds;
    }

    public void setFileIds(String anFileIds) {
        this.fileIds = anFileIds;
    }

    public String[] getFileList() {
        return this.fileList;
    }

    public void setFileList(String[] anFileList) {
        this.fileList = anFileList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", custNo=").append(custNo);
        sb.append(", fileCount=").append(fileCount);
        sb.append(", auditStatus=").append(auditStatus);
        sb.append(", workType=").append(workType);
        sb.append(", description=").append(description);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", operNo=").append(operNo);
        sb.append(", aduitCustNo=").append(aduitCustNo);
        sb.append(", aduitorNo=").append(aduitorNo);
        sb.append(", fileIds=").append(fileIds);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
    public String getAuthorTime() {
        return this.authorTime;
    }
    public void setAuthorTime(String anAuthorTime) {
        this.authorTime = anAuthorTime;
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
        CustFileAduit other = (CustFileAduit) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getFileCount() == null ? other.getFileCount() == null : this.getFileCount().equals(other.getFileCount()))
            && (this.getAuditStatus() == null ? other.getAuditStatus() == null : this.getAuditStatus().equals(other.getAuditStatus()))
            && (this.getWorkType() == null ? other.getWorkType() == null : this.getWorkType().equals(other.getWorkType()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getOperNo() == null ? other.getOperNo() == null : this.getOperNo().equals(other.getOperNo()))
            && (this.getAduitCustNo() == null ? other.getAduitCustNo() == null : this.getAduitCustNo().equals(other.getAduitCustNo()))
            && (this.getAuthorTime() == null ? other.getAuthorTime() == null : this.getAuthorTime().equals(other.getAuthorTime()))
            && (this.getAduitorNo() == null ? other.getAduitorNo() == null : this.getAduitorNo().equals(other.getAduitorNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getFileCount() == null) ? 0 : getFileCount().hashCode());
        result = prime * result + ((getAuditStatus() == null) ? 0 : getAuditStatus().hashCode());
        result = prime * result + ((getWorkType() == null) ? 0 : getWorkType().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getOperNo() == null) ? 0 : getOperNo().hashCode());
        result = prime * result + ((getAduitCustNo() == null) ? 0 : getAduitCustNo().hashCode());
        result = prime * result + ((getAduitorNo() == null) ? 0 : getAduitorNo().hashCode());
        result = prime * result + ((getAuthorTime() == null) ? 0 : getAuthorTime().hashCode());
        return result;
    }
    
    public CustFileAduit() {
        
    }
    
    public static void init(CustFileAduit anAuditInfo, String anType, Long custNo) {
//        anAuditInfo.id = SerialGenerator.getLongValue("CustFileInfo.id");
        anAuditInfo.workType = anType;
        anAuditInfo.custNo = custNo;
        anAuditInfo.auditStatus = "0";
        anAuditInfo.regDate = BetterDateUtils.getNumDate();
        anAuditInfo.regTime = BetterDateUtils.getNumTime();
        anAuditInfo.modiDate = BetterDateUtils.getNumDate();
        anAuditInfo.operNo = UserUtils.getOperatorInfo().getId().toString();//getUser().getId().toString();
    }
}
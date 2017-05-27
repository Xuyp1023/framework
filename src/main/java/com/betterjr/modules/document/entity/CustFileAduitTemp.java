package com.betterjr.modules.document.entity;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cust_fileaduit_temp")
public class CustFileAduitTemp implements BetterjrEntity {
    @Column(name = "ID",  columnDefinition="INTEGER" )
    private Long id;

    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    private Long custNo;

    @Column(name = "N_COUNT",  columnDefinition="INTEGER" )
    private Long fileCount;

    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    private String auditStatus;

    @Column(name = "C_TYPE",  columnDefinition="VARCHAR" )
    private String workType;

    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    private String description;

    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    private String regDate;

    @Column(name = "D_REGTIME",  columnDefinition="VARCHAR" )
    private String regTime;

    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    private String modiDate;

    @Column(name = "C_OPERNO",  columnDefinition="VARCHAR" )
    private String operNo;

    @Column(name = "L_ADUIT_CUSTNO",  columnDefinition="INTEGER" )
    private Long aduitCustNo;

    @Column(name = "C_ADUITORNO",  columnDefinition="VARCHAR" )
    private String aduitorNo;

    @Column(name = "D_ADUIT_DATE",  columnDefinition="VARCHAR" )
    private String authorTime;

    private static final long serialVersionUID = -3360359762618145250L;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate;
    }

    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(String operNo) {
        this.operNo = operNo;
    }

    public String getAduitorNo() {
        return aduitorNo;
    }

    public void setAduitorNo(String aduitorNo) {
        this.aduitorNo = aduitorNo;
    }

    public Long getFileCount() {
        return this.fileCount;
    }

    public void setFileCount(Long anFileCount) {
        this.fileCount = anFileCount;
    }

    public String getAuditStatus() {
        return this.auditStatus;
    }

    public void setAuditStatus(String anAuditStatus) {
        this.auditStatus = anAuditStatus;
    }

    public String getWorkType() {
        return this.workType;
    }

    public void setWorkType(String anWorkType) {
        this.workType = anWorkType;
    }

    public Long getAduitCustNo() {
        return this.aduitCustNo;
    }

    public void setAduitCustNo(Long anAduitCustNo) {
        this.aduitCustNo = anAduitCustNo;
    }

    public String getAuthorTime() {
        return this.authorTime;
    }

    public void setAuthorTime(String anAuthorTime) {
        this.authorTime = anAuthorTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", custNo=").append(custNo);
        sb.append(", count=").append(fileCount);
        sb.append(", status=").append(auditStatus);
        sb.append(", type=").append(workType);
        sb.append(", description=").append(description);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", operNo=").append(operNo);
        sb.append(", aduitCustNo=").append(aduitCustNo);
        sb.append(", aduitorNo=").append(aduitorNo);
        sb.append(", authorTime=").append(authorTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
    
    public void initValue(){
        this.fileCount=1l;
        this.regDate=BetterDateUtils.getNumDate();
        this.regTime=BetterDateUtils.getNumTime();
    }
    
    public void saveInitValue(){
        this.modiDate=BetterDateUtils.getNumDate();
        this.aduitorNo=UserUtils.getOperatorInfo().getOperCode();
        this.modiDate=BetterDateUtils.getNumDate();
        this.authorTime=BetterDateUtils.getNumDate();
    }
}
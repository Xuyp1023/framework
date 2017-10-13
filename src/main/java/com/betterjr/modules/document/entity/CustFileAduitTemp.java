package com.betterjr.modules.document.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.UserUtils;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cust_fileaduit_temp")
public class CustFileAduitTemp implements BetterjrEntity {
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Long id;

    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    private Long custNo;

    @Column(name = "N_COUNT", columnDefinition = "INTEGER")
    private Long fileCount;

    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    private String auditStatus;

    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    private String workType;

    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    private String description;

    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    private String regDate;

    @Column(name = "D_REGTIME", columnDefinition = "VARCHAR")
    private String regTime;

    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    private String modiDate;

    @Column(name = "C_OPERNO", columnDefinition = "VARCHAR")
    private String operNo;

    @Column(name = "L_ADUIT_CUSTNO", columnDefinition = "INTEGER")
    private Long aduitCustNo;

    @Column(name = "C_ADUITORNO", columnDefinition = "VARCHAR")
    private String aduitorNo;

    @Column(name = "D_ADUIT_DATE", columnDefinition = "VARCHAR")
    private String authorTime;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;

    private static final long serialVersionUID = -3360359762618145250L;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(final Long custNo) {
        this.custNo = custNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(final String regDate) {
        this.regDate = regDate;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(final String regTime) {
        this.regTime = regTime;
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(final String modiDate) {
        this.modiDate = modiDate;
    }

    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(final String operNo) {
        this.operNo = operNo;
    }

    public String getAduitorNo() {
        return aduitorNo;
    }

    public void setAduitorNo(final String aduitorNo) {
        this.aduitorNo = aduitorNo;
    }

    public Long getFileCount() {
        return this.fileCount;
    }

    public void setFileCount(final Long anFileCount) {
        this.fileCount = anFileCount;
    }

    public String getAuditStatus() {
        return this.auditStatus;
    }

    public void setAuditStatus(final String anAuditStatus) {
        this.auditStatus = anAuditStatus;
    }

    public String getWorkType() {
        return this.workType;
    }

    public void setWorkType(final String anWorkType) {
        this.workType = anWorkType;
    }

    public Long getAduitCustNo() {
        return this.aduitCustNo;
    }

    public void setAduitCustNo(final Long anAduitCustNo) {
        this.aduitCustNo = anAduitCustNo;
    }

    public String getAuthorTime() {
        return this.authorTime;
    }

    public void setAuthorTime(final String anAuthorTime) {
        this.authorTime = anAuthorTime;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(final Long anBatchNo) {
        this.batchNo = anBatchNo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
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
        sb.append(", batchNo=").append(batchNo);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.aduitCustNo == null) ? 0 : this.aduitCustNo.hashCode());
        result = prime * result + ((this.aduitorNo == null) ? 0 : this.aduitorNo.hashCode());
        result = prime * result + ((this.auditStatus == null) ? 0 : this.auditStatus.hashCode());
        result = prime * result + ((this.authorTime == null) ? 0 : this.authorTime.hashCode());
        result = prime * result + ((this.batchNo == null) ? 0 : this.batchNo.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.fileCount == null) ? 0 : this.fileCount.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.modiDate == null) ? 0 : this.modiDate.hashCode());
        result = prime * result + ((this.operNo == null) ? 0 : this.operNo.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.workType == null) ? 0 : this.workType.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CustFileAduitTemp)) {
            return false;
        }
        final CustFileAduitTemp other = (CustFileAduitTemp) obj;
        if (this.aduitCustNo == null) {
            if (other.aduitCustNo != null) {
                return false;
            }
        }
        else if (!this.aduitCustNo.equals(other.aduitCustNo)) {
            return false;
        }
        if (this.aduitorNo == null) {
            if (other.aduitorNo != null) {
                return false;
            }
        }
        else if (!this.aduitorNo.equals(other.aduitorNo)) {
            return false;
        }
        if (this.auditStatus == null) {
            if (other.auditStatus != null) {
                return false;
            }
        }
        else if (!this.auditStatus.equals(other.auditStatus)) {
            return false;
        }
        if (this.authorTime == null) {
            if (other.authorTime != null) {
                return false;
            }
        }
        else if (!this.authorTime.equals(other.authorTime)) {
            return false;
        }
        if (this.batchNo == null) {
            if (other.batchNo != null) {
                return false;
            }
        }
        else if (!this.batchNo.equals(other.batchNo)) {
            return false;
        }
        if (this.custNo == null) {
            if (other.custNo != null) {
                return false;
            }
        }
        else if (!this.custNo.equals(other.custNo)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        }
        else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.fileCount == null) {
            if (other.fileCount != null) {
                return false;
            }
        }
        else if (!this.fileCount.equals(other.fileCount)) {
            return false;
        }
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.modiDate == null) {
            if (other.modiDate != null) {
                return false;
            }
        }
        else if (!this.modiDate.equals(other.modiDate)) {
            return false;
        }
        if (this.operNo == null) {
            if (other.operNo != null) {
                return false;
            }
        }
        else if (!this.operNo.equals(other.operNo)) {
            return false;
        }
        if (this.regDate == null) {
            if (other.regDate != null) {
                return false;
            }
        }
        else if (!this.regDate.equals(other.regDate)) {
            return false;
        }
        if (this.regTime == null) {
            if (other.regTime != null) {
                return false;
            }
        }
        else if (!this.regTime.equals(other.regTime)) {
            return false;
        }
        if (this.workType == null) {
            if (other.workType != null) {
                return false;
            }
        }
        else if (!this.workType.equals(other.workType)) {
            return false;
        }
        return true;
    }

    public void initValue() {
        this.fileCount = 1l;
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
    }

    public void saveInitValue() {
        this.modiDate = BetterDateUtils.getNumDate();
        this.aduitorNo = UserUtils.getOperatorInfo().getOperCode();
        this.modiDate = BetterDateUtils.getNumDate();
        this.authorTime = BetterDateUtils.getNumDate();
    }
}
package com.betterjr.modules.generator.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.entity.BetterjrEntity;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_sys_seq_record")
public class SequenceRecord implements BetterjrEntity {
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Long id;

    @Column(name = "N_VERSION", columnDefinition = "INTEGER")
    private Long version;

    @Column(name = "C_CYCLE", columnDefinition = "CHAR")
    private String cycle;

    @Column(name = "D_CYCLE_START_DATE", columnDefinition = "VARCHAR")
    private String cycleStartDate;

    @Column(name = "N_NEXT_VALUE", columnDefinition = "INTEGER")
    private Long nextValue;

    @Column(name = "N_INCREMENT_STEP", columnDefinition = "INTEGER")
    private Long incrementStep;

    @Column(name = "N_START_VALUE", columnDefinition = "INTEGER")
    private Long startValue;

    @Column(name = "N_MAX_VALUE", columnDefinition = "INTEGER")
    private Long maximumValue;

    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    private String operOrg;

    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    private Long custNo;

    @Column(name = "C_SEQ_ID", columnDefinition = "VARCHAR")
    private String seqId;

    private static final long serialVersionUID = 8887222374692267337L;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(final String cycle) {
        this.cycle = cycle;
    }

    public String getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(final String cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public Long getNextValue() {
        return nextValue;
    }

    public void setNextValue(final Long nextValue) {
        this.nextValue = nextValue;
    }

    public Long getIncrementStep() {
        return incrementStep;
    }

    public void setIncrementStep(final Long incrementStep) {
        this.incrementStep = incrementStep;
    }

    public Long getStartValue() {
        return startValue;
    }

    public void setStartValue(final Long startValue) {
        this.startValue = startValue;
    }

    public Long getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(final Long maximumValue) {
        this.maximumValue = maximumValue;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(final String operOrg) {
        this.operOrg = operOrg;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(final Long custNo) {
        this.custNo = custNo;
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(final String seqId) {
        this.seqId = seqId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", cycle=").append(cycle);
        sb.append(", cycleStartDate=").append(cycleStartDate);
        sb.append(", nextValue=").append(nextValue);
        sb.append(", incrementStep=").append(incrementStep);
        sb.append(", startValue=").append(startValue);
        sb.append(", maximumValue=").append(maximumValue);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", custNo=").append(custNo);
        sb.append(", seqId=").append(seqId);
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
        final SequenceRecord other = (SequenceRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
                && (this.getCycle() == null ? other.getCycle() == null : this.getCycle().equals(other.getCycle()))
                && (this.getCycleStartDate() == null ? other.getCycleStartDate() == null : this.getCycleStartDate().equals(other.getCycleStartDate()))
                && (this.getNextValue() == null ? other.getNextValue() == null : this.getNextValue().equals(other.getNextValue()))
                && (this.getIncrementStep() == null ? other.getIncrementStep() == null : this.getIncrementStep().equals(other.getIncrementStep()))
                && (this.getStartValue() == null ? other.getStartValue() == null : this.getStartValue().equals(other.getStartValue()))
                && (this.getMaximumValue() == null ? other.getMaximumValue() == null : this.getMaximumValue().equals(other.getMaximumValue()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getSeqId() == null ? other.getSeqId() == null : this.getSeqId().equals(other.getSeqId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getCycle() == null) ? 0 : getCycle().hashCode());
        result = prime * result + ((getCycleStartDate() == null) ? 0 : getCycleStartDate().hashCode());
        result = prime * result + ((getNextValue() == null) ? 0 : getNextValue().hashCode());
        result = prime * result + ((getIncrementStep() == null) ? 0 : getIncrementStep().hashCode());
        result = prime * result + ((getStartValue() == null) ? 0 : getStartValue().hashCode());
        result = prime * result + ((getMaximumValue() == null) ? 0 : getMaximumValue().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getSeqId() == null) ? 0 : getSeqId().hashCode());
        return result;
    }
}
package com.betterjr.modules.account.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_PWD_PROTECTED")
public class CustPassProtectedInfo implements BetterjrEntity {
    /**
     * 客户编号
     */
    @Id
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 问题
     */
    @Column(name = "C_QUESTION",  columnDefinition="INTEGER" )
    @MetaData( value="问题", comments = "问题")
    private String question;

    /**
     * 答案
     */
    @Column(name = "C_ANSWER",  columnDefinition="VARCHAR" )
    @MetaData( value="答案", comments = "答案")
    private String answer;

    /**
     * 录入系统日期(物理日期)
     */
    @Column(name = "D_OPERDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="录入系统日期(物理日期)", comments = "录入系统日期(物理日期)")
    private String operDate;

    /**
     * 录入系统时间(物理时间)
     */
    @Column(name = "T_OPERTIME",  columnDefinition="VARCHAR" )
    @MetaData( value="录入系统时间(物理时间)", comments = "录入系统时间(物理时间)")
    private String operTime;

    /**
     * IP地址
     */
    @Column(name = "C_IPADDR",  columnDefinition="VARCHAR" )
    @MetaData( value="IP地址", comments = "IP地址")
    private String ipaddr;

    private static final long serialVersionUID = 1439797394185L;

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate == null ? null : operDate.trim();
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime == null ? null : operTime.trim();
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr == null ? null : ipaddr.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", custNo=").append(custNo);
        sb.append(", question=").append(question);
        sb.append(", answer=").append(answer);
        sb.append(", operDate=").append(operDate);
        sb.append(", operTime=").append(operTime);
        sb.append(", ipaddr=").append(ipaddr);
        sb.append(", serialVersionUID=").append(serialVersionUID);
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
        CustPassProtectedInfo other = (CustPassProtectedInfo) that;
        return (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getQuestion() == null ? other.getQuestion() == null : this.getQuestion().equals(other.getQuestion()))
            && (this.getAnswer() == null ? other.getAnswer() == null : this.getAnswer().equals(other.getAnswer()))
            && (this.getOperDate() == null ? other.getOperDate() == null : this.getOperDate().equals(other.getOperDate()))
            && (this.getOperTime() == null ? other.getOperTime() == null : this.getOperTime().equals(other.getOperTime()))
            && (this.getIpaddr() == null ? other.getIpaddr() == null : this.getIpaddr().equals(other.getIpaddr()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getQuestion() == null) ? 0 : getQuestion().hashCode());
        result = prime * result + ((getAnswer() == null) ? 0 : getAnswer().hashCode());
        result = prime * result + ((getOperDate() == null) ? 0 : getOperDate().hashCode());
        result = prime * result + ((getOperTime() == null) ? 0 : getOperTime().hashCode());
        result = prime * result + ((getIpaddr() == null) ? 0 : getIpaddr().hashCode());
        return result;
    }
}
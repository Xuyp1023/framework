package com.betterjr.modules.account.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.web.Servlets;
import javax.persistence.*;
 
@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_LOGIN")
public class CustLoginRecord implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号")
    private Long id;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 用户类型；0：机构，1：个人，6操作员
     */
    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "用户类型", comments = "0：机构，1：个人，6操作员")
    private String userType;

    /**
     * 分类
     */
    @Column(name = "C_CLASS",  columnDefinition="VARCHAR" )
    @MetaData( value="分类", comments = "分类")
    private String loginClass;

    /**
     * IP地址
     */
    @Column(name = "C_IPADDR",  columnDefinition="VARCHAR" )
    @MetaData( value="IP地址", comments = "IP地址")
    private String ipaddr;

    /**
     * 系统名称
     */
    @Column(name = "C_SYSNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="系统名称", comments = "系统名称")
    private String sysName;

    /**
     * 接入方式
     */
    @Column(name = "C_ACCESS_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="接入方式", comments = "接入方式")
    private String accessType;

    /**
     * 内容
     */
    @Column(name = "C_MARK",  columnDefinition="VARCHAR" )
    @MetaData( value="内容", comments = "内容")
    private String mark;

    /**
     * 登录日期
     */
    @Column(name = "D_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="登录日期", comments = "登录日期")
    private String loginDate;

    /**
     * 登录时间
     */
    @Column(name = "T_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="登录时间", comments = "登录时间")
    private String loginTime;

    /**
     * 操作员编码
     */
    @Column(name = "C_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员编码", comments = "操作员编码")
    private String operCode;

    /**
     * 操作员所在机构，如果证书登录，则
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在机构", comments = "操作员所在机构，如果证书登录，则")
    private String operOrg;

    private static final long serialVersionUID = 1439797394182L;
 

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime == null ? null : loginTime.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getLoginClass() {
        return loginClass;
    }

    public void setLoginClass(String loginClass) {
        this.loginClass = loginClass == null ? null : loginClass.trim();
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr == null ? null : ipaddr.trim();
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName == null ? null : sysName.trim();
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType == null ? null : accessType.trim();
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate == null ? null : loginDate.trim();
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String anUserType) {
        this.userType = anUserType;
    }

    public String getOperCode() {
        return this.operCode;
    }

    public void setOperCode(String anOperCode) {
        this.operCode = anOperCode;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", loginTime=").append(loginTime);
        sb.append(", custNo=").append(custNo);
        sb.append(", loginClass=").append(loginClass);
        sb.append(", ipaddr=").append(ipaddr);
        sb.append(", sysName=").append(sysName);
        sb.append(", accessType=").append(accessType);
        sb.append(", mark=").append(mark);
        sb.append(", loginDate=").append(loginDate);
        sb.append(", userType=").append(userType);
        sb.append(", operCode=").append(operCode);
        sb.append(", operOrg=").append(operOrg);
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
        CustLoginRecord other = (CustLoginRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLoginTime() == null ? other.getLoginTime() == null : this.getLoginTime().equals(other.getLoginTime()))
            && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getLoginClass() == null ? other.getLoginClass() == null : this.getLoginClass().equals(other.getLoginClass()))
            && (this.getIpaddr() == null ? other.getIpaddr() == null : this.getIpaddr().equals(other.getIpaddr()))
            && (this.getSysName() == null ? other.getSysName() == null : this.getSysName().equals(other.getSysName()))
            && (this.getAccessType() == null ? other.getAccessType() == null : this.getAccessType().equals(other.getAccessType()))
            && (this.getMark() == null ? other.getMark() == null : this.getMark().equals(other.getMark()))
            && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
            && (this.getLoginDate() == null ? other.getLoginDate() == null : this.getLoginDate().equals(other.getLoginDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLoginTime() == null) ? 0 : getLoginTime().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getLoginClass() == null) ? 0 : getLoginClass().hashCode());
        result = prime * result + ((getIpaddr() == null) ? 0 : getIpaddr().hashCode());
        result = prime * result + ((getSysName() == null) ? 0 : getSysName().hashCode());
        result = prime * result + ((getAccessType() == null) ? 0 : getAccessType().hashCode());
        result = prime * result + ((getMark() == null) ? 0 : getMark().hashCode());
        result = prime * result + ((getLoginDate() == null) ? 0 : getLoginDate().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        return result;
    }
    
    public static CustLoginRecord createByOperator(CustOperatorInfo anOperator, String anAccessType){
        CustLoginRecord loginRecord = new CustLoginRecord();
        loginRecord.fillByOperatorInfo(anOperator, anAccessType);
        
        return loginRecord;
    } 
    
    private void fillByOperatorInfo(CustOperatorInfo anOperator, String anAccessType){
        this.operCode = anOperator.getOperCode();
        this.operOrg = anOperator.getOperOrg();
        this.custNo = anOperator.getId();
        this.userType ="6";
        this.accessType = anAccessType;
        this.id = SerialGenerator.getLongValue("CustLoginRecord.id");
        this.loginDate = BetterDateUtils.getNumDate();
        this.loginTime = BetterDateUtils.getNumTime();
        this.ipaddr = Servlets.getRemoteAddr();
    }
}
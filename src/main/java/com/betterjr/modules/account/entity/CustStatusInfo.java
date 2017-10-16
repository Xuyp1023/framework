package com.betterjr.modules.account.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_STATUS")
public class CustStatusInfo implements BetterjrEntity {
    /**
     * 客户编号
     */
    @Id
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 客户类型：0：基金持有人；2：客户经理；3：P2P客户，4：私募客户，5：保险客户，8：网站注册客户
     */
    @Column(name = "C_CUSTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "客户类型：0：基金持有人", comments = "客户类型：0：基金持有人；2：客户经理；3：P2P客户，4：私募客户，5：保险客户，8：网站注册客户")
    private String custType;

    /**
     * 用户类型；0：机构，1：个人，6操作员
     */
    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "用户类型", comments = "0：机构，1：个人，6操作员")
    private String userType;

    /**
     * 手机绑定；0：未绑定，1：绑定
     */
    @Column(name = "C_MOBILE_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "手机绑定", comments = "手机绑定；0：未绑定，1：绑定")
    private Boolean mobile;

    /**
     * 邮箱绑定；0：未绑定，1：绑定
     */
    @Column(name = "C_EMAIL_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "邮箱绑定", comments = "邮箱绑定；0：未绑定，1：绑定")
    private Boolean email;

    /**
     * 开通基金销售；0：未开通，1：开通
     */
    @Column(name = "C_SALE_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "开通基金销售", comments = "开通基金销售；0：未开通，1：开通")
    private Boolean sale;

    /**
     * 开通货币基金；0：未开通，1：开通
     */
    @Column(name = "C_MONEY_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "开通货币基金", comments = "开通货币基金；0：未开通，1：开通")
    private Boolean money;

    /**
     * 开通银行卡；0：未开通，1：开通
     */
    @Column(name = "C_CARD_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "开通银行卡", comments = "开通银行卡；0：未开通，1：开通")
    private Boolean bankCard;

    /**
     * 登录次数
     */
    @Column(name = "N_COUNT", columnDefinition = "INTEGER")
    @MetaData(value = "登录次数", comments = "登录次数")
    private Integer count;

    /**
     * 最后登陆IP
     */
    @Column(name = "C_IPADDR", columnDefinition = "VARCHAR")
    @MetaData(value = "最后登陆IP", comments = "最后登陆IP")
    private String ipaddr;

    /**
     * 首次登陆时间
     */
    @Column(name = "T_FIRSTTIME", columnDefinition = "VARCHAR")
    @MetaData(value = "首次登陆时间", comments = "首次登陆时间")
    private String firstTime;

    /**
     * 最后登录时间
     */
    @Column(name = "T_LASTTIME", columnDefinition = "VARCHAR")
    @MetaData(value = "最后登录时间", comments = "最后登录时间")
    private String lastTime;

    /**
     * 通过手机方式登录的登录令牌
     */
    @Column(name = "C_TOKEN", columnDefinition = "VARCHAR")
    @MetaData(value = "通过手机方式登录的登录令牌", comments = "通过手机方式登录的登录令牌")
    private String token;

    /**
     * 微信绑定；0：未绑定，1：绑定
     */
    @Column(name = "C_WEIXIN_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "微信绑定", comments = "微信绑定；0：未绑定，1：绑定")
    private Boolean weiXin;

    /**
     * QQ绑定；0：未绑定，1：绑定
     */
    @Column(name = "C_QQ_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "QQ绑定", comments = "QQ绑定；0：未绑定，1：绑定")
    private Boolean qq;

    /**
     * 微博绑定；0：未绑定，1：绑定
     */
    @Column(name = "C_WEIBO_FLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "微博绑定", comments = "微博绑定；0：未绑定，1：绑定")
    private Boolean weiBo;

    /**
     * 开通票据业务；0：未绑定，1：绑定
     */
    @Column(name = "C_BILL", columnDefinition = "VARCHAR")
    @MetaData(value = "开通票据业务", comments = "开通票据业务；0：未绑定，1：绑定")
    private Boolean bill;

    /**
     * 开通保险业务；0：未绑定，1：绑定
     */
    @Column(name = "C_INSURE", columnDefinition = "VARCHAR")
    @MetaData(value = "开通保险业务", comments = "开通保险业务；0：未绑定，1：绑定")
    private Boolean insure;

    /**
     * 摘要说明
     */
    @Column(name = "C_SPECIFICATION", columnDefinition = "VARCHAR")
    @MetaData(value = "摘要说明", comments = "摘要说明")
    private String specification;

    /**
     * 识别标记
     */
    @Column(name = "C_IDENT", columnDefinition = "VARCHAR")
    @MetaData(value = "识别标记", comments = "识别标记")
    private String identContent;

    private static final long serialVersionUID = 1439797394187L;

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType == null ? null : custType.trim();
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getSale() {
        return sale;
    }

    public void setSale(Boolean sale) {
        this.sale = sale;
    }

    public Boolean getMoney() {
        return money;
    }

    public void setMoney(Boolean money) {
        this.money = money;
    }

    public Boolean getBankCard() {
        return bankCard;
    }

    public void setBankCard(Boolean bankCard) {
        this.bankCard = bankCard;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr == null ? null : ipaddr.trim();
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime == null ? null : firstTime.trim();
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime == null ? null : lastTime.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Boolean getWeiXin() {
        return weiXin;
    }

    public void setWeiXin(Boolean weiXin) {
        this.weiXin = weiXin;
    }

    public Boolean getQq() {
        return qq;
    }

    public void setQq(Boolean qq) {
        this.qq = qq;
    }

    public Boolean getWeiBo() {
        return weiBo;
    }

    public void setWeiBo(Boolean weiBo) {
        this.weiBo = weiBo;
    }

    public Boolean getBill() {
        return bill;
    }

    public void setBill(Boolean bill) {
        this.bill = bill;
    }

    public Boolean getInsure() {
        return insure;
    }

    public void setInsure(Boolean insure) {
        this.insure = insure;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification == null ? null : specification.trim();
    }

    public String getIdentContent() {
        return identContent;
    }

    public void setIdentContent(String identContent) {
        this.identContent = identContent == null ? null : identContent.trim();
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String anUserType) {
        this.userType = anUserType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", custNo=").append(custNo);
        sb.append(", custType=").append(custType);
        sb.append(", mobile=").append(mobile);
        sb.append(", email=").append(email);
        sb.append(", sale=").append(sale);
        sb.append(", money=").append(money);
        sb.append(", bankCard=").append(bankCard);
        sb.append(", count=").append(count);
        sb.append(", ipaddr=").append(ipaddr);
        sb.append(", firstTime=").append(firstTime);
        sb.append(", lastTime=").append(lastTime);
        sb.append(", token=").append(token);
        sb.append(", weiXin=").append(weiXin);
        sb.append(", qq=").append(qq);
        sb.append(", weiBo=").append(weiBo);
        sb.append(", bill=").append(bill);
        sb.append(", insure=").append(insure);
        sb.append(", specification=").append(specification);
        sb.append(", identContent=").append(identContent);
        sb.append(", userType=").append(userType);
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
        CustStatusInfo other = (CustStatusInfo) that;
        return (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getCustType() == null ? other.getCustType() == null
                        : this.getCustType().equals(other.getCustType()))
                && (this.getMobile() == null ? other.getMobile() == null : this.getMobile().equals(other.getMobile()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getSale() == null ? other.getSale() == null : this.getSale().equals(other.getSale()))
                && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
                && (this.getBankCard() == null ? other.getBankCard() == null
                        : this.getBankCard().equals(other.getBankCard()))
                && (this.getCount() == null ? other.getCount() == null : this.getCount().equals(other.getCount()))
                && (this.getIpaddr() == null ? other.getIpaddr() == null : this.getIpaddr().equals(other.getIpaddr()))
                && (this.getFirstTime() == null ? other.getFirstTime() == null
                        : this.getFirstTime().equals(other.getFirstTime()))
                && (this.getLastTime() == null ? other.getLastTime() == null
                        : this.getLastTime().equals(other.getLastTime()))
                && (this.getToken() == null ? other.getToken() == null : this.getToken().equals(other.getToken()))
                && (this.getWeiXin() == null ? other.getWeiXin() == null : this.getWeiXin().equals(other.getWeiXin()))
                && (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()))
                && (this.getWeiBo() == null ? other.getWeiBo() == null : this.getWeiBo().equals(other.getWeiBo()))
                && (this.getBill() == null ? other.getBill() == null : this.getBill().equals(other.getBill()))
                && (this.getInsure() == null ? other.getInsure() == null : this.getInsure().equals(other.getInsure()))
                && (this.getSpecification() == null ? other.getSpecification() == null
                        : this.getSpecification().equals(other.getSpecification()))
                && (this.getUserType() == null ? other.getUserType() == null
                        : this.getUserType().equals(other.getUserType()))
                && (this.getIdentContent() == null ? other.getIdentContent() == null
                        : this.getIdentContent().equals(other.getIdentContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustType() == null) ? 0 : getCustType().hashCode());
        result = prime * result + ((getMobile() == null) ? 0 : getMobile().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getSale() == null) ? 0 : getSale().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getBankCard() == null) ? 0 : getBankCard().hashCode());
        result = prime * result + ((getCount() == null) ? 0 : getCount().hashCode());
        result = prime * result + ((getIpaddr() == null) ? 0 : getIpaddr().hashCode());
        result = prime * result + ((getFirstTime() == null) ? 0 : getFirstTime().hashCode());
        result = prime * result + ((getLastTime() == null) ? 0 : getLastTime().hashCode());
        result = prime * result + ((getToken() == null) ? 0 : getToken().hashCode());
        result = prime * result + ((getWeiXin() == null) ? 0 : getWeiXin().hashCode());
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        result = prime * result + ((getWeiBo() == null) ? 0 : getWeiBo().hashCode());
        result = prime * result + ((getBill() == null) ? 0 : getBill().hashCode());
        result = prime * result + ((getInsure() == null) ? 0 : getInsure().hashCode());
        result = prime * result + ((getSpecification() == null) ? 0 : getSpecification().hashCode());
        result = prime * result + ((getIdentContent() == null) ? 0 : getIdentContent().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        return result;
    }
}
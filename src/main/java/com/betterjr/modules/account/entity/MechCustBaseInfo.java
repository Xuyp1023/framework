package com.betterjr.modules.account.entity;

import java.math.BigDecimal;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.MathExtend;
 
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUSTBASE_MECH")
public class MechCustBaseInfo implements BetterjrEntity {
    /**
     * 客户编号
     */
    @Id
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 英文名称
     */
    @Column(name = "C_ENG", columnDefinition = "VARCHAR")
    @MetaData(value = "英文名称", comments = "英文名称")
    private String engName;

    /**
     * 机构类别；企业法人、机关法人、事业法人、社团法人、工会法人、其他非金融机构法人、证券公司、银行、信托投资公司、基金管理公司、保险公司、
     * 其他金融机构法人、普通合伙企业、特殊普通合伙企业、有限合伙企业、非法人非合伙制创投企业、境外一般机构、境外代理人、境外证券公司、境外基金公司、
     * 破产管理人、中国金融期货交易所、其他
     */
    @Column(name = "C_CLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "机构类别", comments = "机构类别；企业法人、机关法人、事业法人、社团法人、工会法人、其他非金融机构法人、证券公司、银行、信托投资公司、基金管理公司、保险公司、其他金融机构法人、普通合伙企业、特殊普通合伙企业、有限合伙企业、非法人非合伙制创投企业、境外一般机构、境外代理人、境外证券公司、境外基金公司、破产管理人、中国金融期货交易所、其他")
    private String category;

    /**
     * 国有属性；国务院国资委管辖、地方国资委管辖、其他国有企业、非国有
     */
    @Column(name = "C_NATION_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "国有属性", comments = "国有属性；国务院国资委管辖、地方国资委管辖、其他国有企业、非国有")
    private String nationType;

    /**
     * 资本属性；境内资本、三资（合资、合作、外资）、境外资本
     */
    @Column(name = "C_CAPITAL", columnDefinition = "VARCHAR")
    @MetaData(value = "资本属性", comments = "资本属性；境内资本、三资（合资、合作、外资）、境外资本")
    private String capital;

    /**
     * 法人证件类型:0-身份证，1-护照，2-军官证，3-士兵证，4-回乡证，5-户口本，6-外国护照
     */
    @Column(name = "C_LAWTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "法人证件类型:0-身份证", comments = "法人证件类型:0-身份证，1-护照，2-军官证，3-士兵证，4-回乡证，5-户口本，6-外国护照")
    private String lawIdentType;

    /**
     * 法人证件号码
     */
    @Column(name = "C_LAWNO", columnDefinition = "VARCHAR")
    @MetaData(value = "法人证件号码", comments = "法人证件号码")
    private String lawIdentNo;

    /**
     * 法人姓名
     */
    @Column(name = "C_LAWNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "法人姓名", comments = "法人姓名")
    private String lawName;

    /**
     * 法人证件有效期
     */
    @Column(name = "D_LAWCERT_VALIDDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "法人证件有效期", comments = "法人证件有效期")
    private String lawIdentValidDate;

    /**
     * 企业注册地址
     */
    @Column(name = "C_REGADDR", columnDefinition = "VARCHAR")
    @MetaData(value = "企业注册地址", comments = "企业注册地址")
    private String regAddr;

    /**
     * 法人代表联系电话
     */
    @Column(name = "C_LAW_PHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "法人代表联系电话", comments = "法人代表联系电话")
    private String lawPhone;

    /**
     * 行业
     */
    @Column(name = "C_CORPVOCATE", columnDefinition = "VARCHAR")
    @MetaData(value = "行业", comments = "行业")
    private String corpVocate;

    /**
     * 企业性质；0-国企，1-民营，2-合资，3-境外资本，9-其它
     */
    @Column(name = "C_CORPROPERTIY", columnDefinition = "VARCHAR")
    @MetaData(value = "企业性质", comments = "企业性质；0-国企，1-民营，2-合资，3-境外资本，9-其它")
    private String corpProperty;

    /**
     * 注册资本 0-500万以下，1-500万－1000万，2-1000万－3000万，3-3000万－5000万，4-5000万－10000万，5-
     * 10000万以上
     */
    @Column(name = "C_REGCAPITAL", columnDefinition = "VARCHAR")
    @MetaData(value = "注册资本  0-500万以下", comments = "注册资本  0-500万以下，1-500万－1000万，2-1000万－3000万，3-3000万－5000万，4-5000万－10000万，5-10000万以上")
    private String regCapital;

    /**
     * 投资经历 0：无经验，1：1-3年，2：3-5年，3：5-10年，4：10年以上
     */
    @Column(name = "C_INVEST", columnDefinition = "VARCHAR")
    @MetaData(value = "投资经历 0：无经验", comments = "投资经历 0：无经验，1：1-3年，2：3-5年，3：5-10年，4：10年以上")
    private String investType;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 机构类型；0-保险机构，1-基金公司，2-上市公司，3-信托公司，4-证券公司，5-理财产品，6-企业年金，7-社保基金，8-其他机构
     */
    @Column(name = "C_INSTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "机构类型", comments = "机构类型；0-保险机构，1-基金公司，2-上市公司，3-信托公司，4-证券公司，5-理财产品，6-企业年金，7-社保基金，8-其他机构")
    private String instType;

    /**
     * 客户分类；01普通客户；02企业年金计划；03银行、券商、信托公司等的理财产品或理财计划；04保险产品；05社保基金组合；09其他
     */
    @Column(name = "C_CUSTCLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "客户分类", comments = "客户分类；01普通客户；02企业年金计划；03银行、券商、信托公司等的理财产品或理财计划；04保险产品；05社保基金组合；09其他")
    private String custClass;

    /**
     * 组织机构代码证
     */
    @Column(name = "C_BUSI_LICENCE",  columnDefinition="VARCHAR" )
    @MetaData( value="组织机构代码证", comments = "组织机构代码证")
    private String busiLicence;

    /**
     * 营业执照登记日期
     */
    @Column(name = "D_BUSI_LICENCE_REG",  columnDefinition="VARCHAR" )
    @MetaData( value="营业执照登记日期", comments = "营业执照登记日期")
    private String businLicRegDate;

    /**
     * 注册资本金额
     */
    @Column(name = "F_REG_BALANCE",  columnDefinition="BigDecimal" )
    @MetaData( value="注册资本金额", comments = "注册资本金额")
    private BigDecimal regBalance;

    /**
     * 实收资本
     */
    @Column(name = "F_PAID_CAPITAL",  columnDefinition="BigDecimal" )
    @MetaData( value="实收资本", comments = "实收资本")
    private BigDecimal paidCapital;

    /**
     * 人数
     */
    @Column(name = "N_PERSON",  columnDefinition="Integer" )
    @MetaData( value="人数", comments = "人数")
    private Integer personCount;

    /**
     * 经营面积（平方米）
     */
    @Column(name = "F_OPERAT_AREA",  columnDefinition="BigDecimal" )
    @MetaData( value="经营面积（平方米）", comments = "经营面积（平方米）")
    private BigDecimal operateArea;

    /**
     * 经营场地所有权年限(年)
     */
    @Column(name = "F_OWNERSHIP_YEAR",  columnDefinition="Integer" )
    @MetaData( value="经营场地所有权年限(年)", comments = "经营场地所有权年限(年)")
    private Integer ownerShipYear;

    /**
     * 经营范围
     */
    @Column(name = "C_BUSI_SCOPE",  columnDefinition="VARCHAR" )
    @MetaData( value="经营范围", comments = "经营范围")
    private String busiScope;

    /**
     * 成立日期
     */
    @Column(name = "D_SETUPDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="成立日期", comments = "成立日期")
    private String setupDate;
    
    /**
     * 截止日期
     */
    @Column(name = "D_VALIDDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="营业执照截止日期", comments = "营业执照截止日期")
    private String validDate;

    private static final long serialVersionUID = 1459952422046L;

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName == null ? null : engName.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getNationType() {
        return nationType;
    }

    public void setNationType(String nationType) {
        this.nationType = nationType == null ? null : nationType.trim();
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital == null ? null : capital.trim();
    }

    public String getLawIdentType() {
        return lawIdentType;
    }

    public void setLawIdentType(String lawIdentType) {
        this.lawIdentType = lawIdentType == null ? null : lawIdentType.trim();
    }

    public String getLawIdentNo() {
        return lawIdentNo;
    }

    public void setLawIdentNo(String lawIdentNo) {
        this.lawIdentNo = lawIdentNo == null ? null : lawIdentNo.trim();
    }

    public String getLawName() {
        return lawName;
    }

    public void setLawName(String lawName) {
        this.lawName = lawName == null ? null : lawName.trim();
    }

    public String getLawIdentValidDate() {
        return lawIdentValidDate;
    }

    public void setLawIdentValidDate(String lawIdentValidDate) {
        this.lawIdentValidDate = lawIdentValidDate == null ? null : lawIdentValidDate.trim();
    }

    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr == null ? null : regAddr.trim();
    }

    public String getLawPhone() {
        return lawPhone;
    }

    public void setLawPhone(String lawPhone) {
        this.lawPhone = lawPhone == null ? null : lawPhone.trim();
    }

    public String getCorpVocate() {
        return corpVocate;
    }

    public void setCorpVocate(String corpVocate) {
        this.corpVocate = corpVocate == null ? null : corpVocate.trim();
    }

    public String getCorpProperty() {
        return corpProperty;
    }

    public void setCorpProperty(String corpProperty) {
        this.corpProperty = corpProperty == null ? null : corpProperty.trim();
    }

    public String getRegCapital() {
        return regCapital;
    }

    public void setRegCapital(String regCapital) {
        this.regCapital = regCapital == null ? null : regCapital.trim();
    }

    public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType == null ? null : investType.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType == null ? null : instType.trim();
    }

    public String getCustClass() {
        return custClass;
    }

    public void setCustClass(String custClass) {
        this.custClass = custClass == null ? null : custClass.trim();
    }

    public String getBusiLicence() {
        return busiLicence;
    }

    public void setBusiLicence(String busiLicence) {
        this.busiLicence = busiLicence == null ? null : busiLicence.trim();
    }

    public String getBusinLicRegDate() {
        return businLicRegDate;
    }

    public void setBusinLicRegDate(String businLicRegDate) {
        this.businLicRegDate = businLicRegDate == null ? null : businLicRegDate.trim();
    }

    public BigDecimal getRegBalance() {
        return regBalance;
    }

    public void setRegBalance(BigDecimal regBalance) {
        this.regBalance = regBalance;
    }

    public BigDecimal getPaidCapital() {
        return paidCapital;
    }

    public void setPaidCapital(BigDecimal paidCapital) {
        this.paidCapital = paidCapital;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public BigDecimal getOperateArea() {
        return operateArea;
    }

    public void setOperateArea(BigDecimal operateArea) {
        this.operateArea = operateArea;
    }

    public Integer getOwnerShipYear() {
        return ownerShipYear;
    }

    public void setOwnerShipYear(Integer ownerShipYear) {
        this.ownerShipYear = ownerShipYear;
    }

    public String getBusiScope() {
        return busiScope;
    }

    public void setBusiScope(String busiScope) {
        this.busiScope = busiScope == null ? null : busiScope.trim();
    }

    public String getSetupDate() {
        return setupDate;
    }

    public void setSetupDate(String setupDate) {
        this.setupDate = setupDate == null ? null : setupDate.trim();
    }

    public String getValidDate() {
        return this.validDate;
    }

    public void setValidDate(String anValidDate) {
        this.validDate = anValidDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" ["); 
        sb.append(" custNo=").append(custNo);
        sb.append(", engName=").append(engName);
        sb.append(", category=").append(category);
        sb.append(", nationType=").append(nationType);
        sb.append(", capital=").append(capital);
        sb.append(", lawIdentType=").append(lawIdentType);
        sb.append(", lawIdentNo=").append(lawIdentNo);
        sb.append(", lawName=").append(lawName);
        sb.append(", lawIdentValidDate=").append(lawIdentValidDate);
        sb.append(", regAddr=").append(regAddr);
        sb.append(", lawPhone=").append(lawPhone);
        sb.append(", corpVocate=").append(corpVocate);
        sb.append(", corpProperty=").append(corpProperty);
        sb.append(", regCapital=").append(regCapital);
        sb.append(", investType=").append(investType);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", instType=").append(instType);
        sb.append(", custClass=").append(custClass);
        sb.append(", busiLicence=").append(busiLicence);
        sb.append(", businLicRegDate=").append(businLicRegDate);
        sb.append(", regBalance=").append(regBalance);
        sb.append(", paidCapital=").append(paidCapital);
        sb.append(", personCount=").append(personCount);
        sb.append(", operateArea=").append(operateArea);
        sb.append(", ownerShipYear=").append(ownerShipYear);
        sb.append(", busiScope=").append(busiScope);
        sb.append(", setupDate=").append(setupDate);
        sb.append(", validDate=").append(validDate); 
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
        MechCustBaseInfo other = (MechCustBaseInfo) that;
        return (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getEngName() == null ? other.getEngName() == null : this.getEngName().equals(other.getEngName()))
                && (this.getCategory() == null ? other.getCategory() == null : this.getCategory().equals(other.getCategory()))
                && (this.getNationType() == null ? other.getNationType() == null : this.getNationType().equals(other.getNationType()))
                && (this.getCapital() == null ? other.getCapital() == null : this.getCapital().equals(other.getCapital()))
                && (this.getLawIdentType() == null ? other.getLawIdentType() == null : this.getLawIdentType().equals(other.getLawIdentType()))
                && (this.getLawIdentNo() == null ? other.getLawIdentNo() == null : this.getLawIdentNo().equals(other.getLawIdentNo()))
                && (this.getLawName() == null ? other.getLawName() == null : this.getLawName().equals(other.getLawName()))
                && (this.getLawIdentValidDate() == null ? other.getLawIdentValidDate() == null
                        : this.getLawIdentValidDate().equals(other.getLawIdentValidDate()))
                && (this.getRegAddr() == null ? other.getRegAddr() == null : this.getRegAddr().equals(other.getRegAddr()))
                && (this.getLawPhone() == null ? other.getLawPhone() == null : this.getLawPhone().equals(other.getLawPhone()))
                && (this.getCorpVocate() == null ? other.getCorpVocate() == null : this.getCorpVocate().equals(other.getCorpVocate()))
                && (this.getCorpProperty() == null ? other.getCorpProperty() == null : this.getCorpProperty().equals(other.getCorpProperty()))
                && (this.getRegCapital() == null ? other.getRegCapital() == null : this.getRegCapital().equals(other.getRegCapital()))
                && (this.getInvestType() == null ? other.getInvestType() == null : this.getInvestType().equals(other.getInvestType()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getInstType() == null ? other.getInstType() == null : this.getInstType().equals(other.getInstType()))
                && (this.getCustClass() == null ? other.getCustClass() == null : this.getCustClass().equals(other.getCustClass()))
            && (this.getBusiLicence() == null ? other.getBusiLicence() == null : this.getBusiLicence().equals(other.getBusiLicence()))
            && (this.getBusinLicRegDate() == null ? other.getBusinLicRegDate() == null : this.getBusinLicRegDate().equals(other.getBusinLicRegDate()))
            && (this.getRegBalance() == null ? other.getRegBalance() == null : this.getRegBalance().equals(other.getRegBalance()))
            && (this.getPaidCapital() == null ? other.getPaidCapital() == null : this.getPaidCapital().equals(other.getPaidCapital()))
            && (this.getPersonCount() == null ? other.getPersonCount() == null : this.getPersonCount().equals(other.getPersonCount()))
            && (this.getOperateArea() == null ? other.getOperateArea() == null : this.getOperateArea().equals(other.getOperateArea()))
            && (this.getOwnerShipYear() == null ? other.getOwnerShipYear() == null : this.getOwnerShipYear().equals(other.getOwnerShipYear()))
            && (this.getBusiScope() == null ? other.getBusiScope() == null : this.getBusiScope().equals(other.getBusiScope()))
            && (this.getSetupDate() == null ? other.getSetupDate() == null : this.getSetupDate().equals(other.getSetupDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getEngName() == null) ? 0 : getEngName().hashCode());
        result = prime * result + ((getCategory() == null) ? 0 : getCategory().hashCode());
        result = prime * result + ((getNationType() == null) ? 0 : getNationType().hashCode());
        result = prime * result + ((getCapital() == null) ? 0 : getCapital().hashCode());
        result = prime * result + ((getLawIdentType() == null) ? 0 : getLawIdentType().hashCode());
        result = prime * result + ((getLawIdentNo() == null) ? 0 : getLawIdentNo().hashCode());
        result = prime * result + ((getLawName() == null) ? 0 : getLawName().hashCode());
        result = prime * result + ((getLawIdentValidDate() == null) ? 0 : getLawIdentValidDate().hashCode());
        result = prime * result + ((getRegAddr() == null) ? 0 : getRegAddr().hashCode());
        result = prime * result + ((getLawPhone() == null) ? 0 : getLawPhone().hashCode());
        result = prime * result + ((getCorpVocate() == null) ? 0 : getCorpVocate().hashCode());
        result = prime * result + ((getCorpProperty() == null) ? 0 : getCorpProperty().hashCode());
        result = prime * result + ((getRegCapital() == null) ? 0 : getRegCapital().hashCode());
        result = prime * result + ((getInvestType() == null) ? 0 : getInvestType().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getInstType() == null) ? 0 : getInstType().hashCode());
        result = prime * result + ((getCustClass() == null) ? 0 : getCustClass().hashCode());
        result = prime * result + ((getBusiLicence() == null) ? 0 : getBusiLicence().hashCode());
        result = prime * result + ((getBusinLicRegDate() == null) ? 0 : getBusinLicRegDate().hashCode());
        result = prime * result + ((getRegBalance() == null) ? 0 : getRegBalance().hashCode());
        result = prime * result + ((getPaidCapital() == null) ? 0 : getPaidCapital().hashCode());
        result = prime * result + ((getPersonCount() == null) ? 0 : getPersonCount().hashCode());
        result = prime * result + ((getOperateArea() == null) ? 0 : getOperateArea().hashCode());
        result = prime * result + ((getOwnerShipYear() == null) ? 0 : getOwnerShipYear().hashCode());
        result = prime * result + ((getBusiScope() == null) ? 0 : getBusiScope().hashCode());
        result = prime * result + ((getSetupDate() == null) ? 0 : getSetupDate().hashCode());
        return result;
    }

    public MechCustBaseInfo() {

    }

    public MechCustBaseInfo(SaleAccoRequestInfo request) {
        BeanMapper.copy(request, this, "LawCertValidDate:LawIdentValidDate");
        this.setModiDate(BetterDateUtils.getNumDateTime());
        this.setCustClass("01");
        this.regBalance = BigDecimal.ZERO;
        this.paidCapital = BigDecimal.ZERO;
        this.operateArea = BigDecimal.ZERO;
        this.personCount = 0;
        this.ownerShipYear = 0;        
    }
}
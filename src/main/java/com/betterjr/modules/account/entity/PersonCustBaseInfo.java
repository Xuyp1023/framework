package com.betterjr.modules.account.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
 import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUSTBASE_PERSON")
public class PersonCustBaseInfo implements BetterjrEntity {
    /**
     * 客户编号
     */
    @Id
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 出生日期
     */
    @Column(name = "D_BIRTHDAY",  columnDefinition="VARCHAR" )
    @MetaData( value="出生日期", comments = "出生日期")
    private String birthDay;

    /**
     * 客户性别:0-女，1-男
     */
    @Column(name = "C_SEX",  columnDefinition="VARCHAR" )
    @MetaData( value="客户性别:0-女", comments = "客户性别:0-女，1-男")
    private String sex;

    /**
     * 职业:01-政府部门，02-教科文，03-金融，04-商贸，05-房地产，06-制造业，07-自由职业，08-其它
     */
    @Column(name = "C_VOCATION",  columnDefinition="VARCHAR" )
    @MetaData( value="职业:01-政府部门", comments = "职业:01-政府部门，02-教科文，03-金融，04-商贸，05-房地产，06-制造业，07-自由职业，08-其它")
    private String vocation;

    /**
     * 学历:01-初中及以下，02-高中/中专，03-大专/本科，04-硕士及以上
     */
    @Column(name = "C_EDUCATION",  columnDefinition="VARCHAR" )
    @MetaData( value="学历:01-初中及以下", comments = "学历:01-初中及以下，02-高中/中专，03-大专/本科，04-硕士及以上")
    private String education;

    /**
     * 民族
     */
    @Column(name = "C_NATION_CODE",  columnDefinition="VARCHAR" )
    @MetaData( value="民族", comments = "民族")
    private String nationCode;

    /**
     * 年收入
     */
    @Column(name = "C_INCOME",  columnDefinition="VARCHAR" )
    @MetaData( value="年收入", comments = "年收入")
    private String incoming;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    private static final long serialVersionUID = 1442500881780L;

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay == null ? null : birthDay.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getVocation() {
        return vocation;
    }

    public void setVocation(String vocation) {
        this.vocation = vocation == null ? null : vocation.trim();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode == null ? null : nationCode.trim();
    }

    public String getIncoming() {
        return incoming;
    }

    public void setIncoming(String incoming) {
        this.incoming = incoming == null ? null : incoming.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", custNo=").append(custNo);
        sb.append(", birthDay=").append(birthDay);
        sb.append(", sex=").append(sex);
        sb.append(", vocation=").append(vocation);
        sb.append(", education=").append(education);
        sb.append(", nationCode=").append(nationCode);
        sb.append(", incoming=").append(incoming);
        sb.append(", modiDate=").append(modiDate);
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
        PersonCustBaseInfo other = (PersonCustBaseInfo) that;
        return (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getBirthDay() == null ? other.getBirthDay() == null : this.getBirthDay().equals(other.getBirthDay()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getVocation() == null ? other.getVocation() == null : this.getVocation().equals(other.getVocation()))
            && (this.getEducation() == null ? other.getEducation() == null : this.getEducation().equals(other.getEducation()))
            && (this.getNationCode() == null ? other.getNationCode() == null : this.getNationCode().equals(other.getNationCode()))
            && (this.getIncoming() == null ? other.getIncoming() == null : this.getIncoming().equals(other.getIncoming()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getBirthDay() == null) ? 0 : getBirthDay().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getVocation() == null) ? 0 : getVocation().hashCode());
        result = prime * result + ((getEducation() == null) ? 0 : getEducation().hashCode());
        result = prime * result + ((getNationCode() == null) ? 0 : getNationCode().hashCode());
        result = prime * result + ((getIncoming() == null) ? 0 : getIncoming().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        return result;
    }
}
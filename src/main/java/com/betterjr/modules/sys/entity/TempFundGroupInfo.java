package com.betterjr.modules.sys.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.betterjr.common.entity.BetterjrEntity;

@Access(AccessType.FIELD)
@Entity
@Table(name = "TEMP_FUNDINFO")
public class TempFundGroupInfo implements BetterjrEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "FUNDCODE", columnDefinition = "VARCHAR")
    private String fundCode;

    @Column(name = "FUNDDAY", columnDefinition = "VARCHAR")
    private String fundDay;

    @Column(name = "INCOME", columnDefinition = "VARCHAR")
    private String income;

    @Column(name = "YIELD", columnDefinition = "VARCHAR")
    private String yield;

    public String getFundCode() {
        return this.fundCode;
    }

    public void setFundCode(String anFundCode) {
        this.fundCode = anFundCode;
    }

    public String getFundDay() {
        return this.fundDay;
    }

    public void setFundDay(String anFundDay) {
        this.fundDay = anFundDay;
    }

    public String getIncome() {
        return this.income;
    }

    public void setIncome(String anIncome) {
        this.income = anIncome;
    }

    public String getYield() {
        return this.yield;
    }

    public void setYield(String anYield) {
        this.yield = anYield;
    }
}
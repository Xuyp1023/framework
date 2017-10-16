package com.betterjr.common.entity;

import javax.persistence.Column;

import com.betterjr.common.annotation.AnnonVersionField;
import com.betterjr.common.annotation.MetaData;

public class BetterjrVersionImpl implements BetterjrVersionEntity {

    private static final long serialVersionUID = -5256413872909144851L;

    /**
     * 数据版本号
     */
    @Column(name = "N_VERSION", columnDefinition = "INTEGER")
    @MetaData(value = "数据版本号", comments = "数据版本号")
    @AnnonVersionField
    private Integer dataVersion;

    public Integer getDataVersion() {

        return this.dataVersion;
    }

    @Override
    public void addDataVersion() {
        if (this.dataVersion == null) {
            this.dataVersion = new Integer(1);
        } else {
            this.dataVersion = new Integer(this.dataVersion.intValue()) + 1;
        }
    }

    public void setDataVersion(Integer anDataVersion) {
        this.dataVersion = anDataVersion;
    }

}

package com.betterjr.modules.document.entity;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cust_resolve_file")
public class CustResolveFile implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8072269417576073962L;

    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Long id;

    /**
     * 上传文件类型
     */
    @Column(name = "C_FILETYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "上传文件类型", comments = "上传文件类型")
    private String fileType;

    /**
     * 上传文件大小
     */
    @Column(name = "N_FILElENGTH", columnDefinition = "INTEGER")
    @MetaData(value = "上传文件大小", comments = "上传文件大小")
    private Long fileLength;

    /**
     * 注册日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "注册日期", comments = "注册日期")
    private String regDate;

    /**
     * 注册时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "注册时间", comments = "注册时间")
    private String regTime;

    /**
     * 解析结果状态 0 正在解析  1解析错误 2解析成功
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "解析结果状态 0 正在解析  1解析错误 2解析成功", comments = "解析结果状态 0 正在解析  1解析错误 2解析成功")
    private String businStatus;

    /**
     * 解析完成描述信息
     */
    @Column(name = "C_SHOW_MESSAGE", columnDefinition = "VARCHAR")
    @MetaData(value = "解析完成描述信息", comments = "解析完成描述信息")
    private String showMessage;

    /**
     * 解析操作员id
     */
    @JsonIgnore
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "解析操作员id", comments = "解析操作员id")
    private Long modiOperId;

    /**
     * 解析操作员名字
     */
    @JsonIgnore
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "解析操作员名字", comments = "解析操作员名字")
    private String modiOperName;

    /**
     * 修改日期
     */
    @JsonIgnore
    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 修改时间
     */
    @JsonIgnore
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改时间", comments = "修改时间")
    private String modiTime;

    /**
     * 所属机构
     */
    @JsonIgnore
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 客户号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户号", comments = "客户号")
    private Long custNo;

    /**
     * 客户企业名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户企业名称", comments = "客户企业名称")
    private String custName;

    /**
     * 核心企业编号
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "核心企业编号", comments = "核心企业编号")
    private Long coreCustNo;

    /**
     * 核心企业名称
     */
    @Column(name = "C_CORE_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "核心企业名称", comments = "核心企业名称")
    private String coreCustName;

    /**
     * 文件上传id关联
     */
    @Column(name = "N_FILEITEM_ID", columnDefinition = "INTEGER")
    @MetaData(value = "文件上传id关联", comments = "文件上传id关联")
    private Long fileItemId;

    /**
     * 文件上传批次号
     */
    @Column(name = "N_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "文件上传批次号", comments = "文件上传批次号")
    private Long batchNo;

    /**
     * 解析的文件类型1订单 2票据  3应收账款 4发票 5 合同
     */
    @Column(name = "C_INFO_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "解析的文件类型1订单 2票据  3应收账款 4发票 5 合同", comments = "解析的文件类型1订单 2票据  3应收账款 4发票 5 合同")
    private String infoType;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String anFileType) {
        this.fileType = anFileType;
    }

    public Long getFileLength() {
        return this.fileLength;
    }

    public void setFileLength(Long anFileLength) {
        this.fileLength = anFileLength;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    public String getRegTime() {
        return this.regTime;
    }

    public void setRegTime(String anRegTime) {
        this.regTime = anRegTime;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getShowMessage() {
        return this.showMessage;
    }

    public void setShowMessage(String anShowMessage) {
        this.showMessage = anShowMessage;
    }

    public Long getModiOperId() {
        return this.modiOperId;
    }

    public void setModiOperId(Long anModiOperId) {
        this.modiOperId = anModiOperId;
    }

    public String getModiOperName() {
        return this.modiOperName;
    }

    public void setModiOperName(String anModiOperName) {
        this.modiOperName = anModiOperName;
    }

    public String getModiDate() {
        return this.modiDate;
    }

    public void setModiDate(String anModiDate) {
        this.modiDate = anModiDate;
    }

    public String getModiTime() {
        return this.modiTime;
    }

    public void setModiTime(String anModiTime) {
        this.modiTime = anModiTime;
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public Long getCustNo() {
        return this.custNo;
    }

    public void setCustNo(Long anCustNo) {
        this.custNo = anCustNo;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String anCustName) {
        this.custName = anCustName;
    }

    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    public void setCoreCustNo(Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    public String getCoreCustName() {
        return this.coreCustName;
    }

    public void setCoreCustName(String anCoreCustName) {
        this.coreCustName = anCoreCustName;
    }

    public Long getFileItemId() {
        return this.fileItemId;
    }

    public void setFileItemId(Long anFileItemId) {
        this.fileItemId = anFileItemId;
    }

    public String getInfoType() {
        return this.infoType;
    }

    public void setInfoType(String anInfoType) {
        this.infoType = anInfoType;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(Long anButchNo) {
        this.batchNo = anButchNo;
    }

    @Override
    public String toString() {
        return "CustResolveFile [id=" + this.id + ", fileType=" + this.fileType + ", fileLength=" + this.fileLength
                + ", regDate=" + this.regDate + ", regTime=" + this.regTime + ", businStatus=" + this.businStatus
                + ", showMessage=" + this.showMessage + ", modiOperId=" + this.modiOperId + ", modiOperName="
                + this.modiOperName + ", modiDate=" + this.modiDate + ", modiTime=" + this.modiTime + ", operOrg="
                + this.operOrg + ", custNo=" + this.custNo + ", custName=" + this.custName + ", coreCustNo="
                + this.coreCustNo + ", coreCustName=" + this.coreCustName + ", fileItemId=" + this.fileItemId
                + ", butchNo=" + this.batchNo + ", infoType=" + this.infoType + "]";
    }

    public CustResolveFile() {
        super();

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.fileItemId == null) ? 0 : this.fileItemId.hashCode());
        result = prime * result + ((this.fileLength == null) ? 0 : this.fileLength.hashCode());
        result = prime * result + ((this.fileType == null) ? 0 : this.fileType.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.infoType == null) ? 0 : this.infoType.hashCode());
        result = prime * result + ((this.modiDate == null) ? 0 : this.modiDate.hashCode());
        result = prime * result + ((this.modiOperId == null) ? 0 : this.modiOperId.hashCode());
        result = prime * result + ((this.modiOperName == null) ? 0 : this.modiOperName.hashCode());
        result = prime * result + ((this.modiTime == null) ? 0 : this.modiTime.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.showMessage == null) ? 0 : this.showMessage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CustResolveFile other = (CustResolveFile) obj;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        } else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        } else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        } else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        } else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        } else if (!this.custNo.equals(other.custNo)) return false;
        if (this.fileItemId == null) {
            if (other.fileItemId != null) return false;
        } else if (!this.fileItemId.equals(other.fileItemId)) return false;
        if (this.fileLength == null) {
            if (other.fileLength != null) return false;
        } else if (!this.fileLength.equals(other.fileLength)) return false;
        if (this.fileType == null) {
            if (other.fileType != null) return false;
        } else if (!this.fileType.equals(other.fileType)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        } else if (!this.id.equals(other.id)) return false;
        if (this.infoType == null) {
            if (other.infoType != null) return false;
        } else if (!this.infoType.equals(other.infoType)) return false;
        if (this.modiDate == null) {
            if (other.modiDate != null) return false;
        } else if (!this.modiDate.equals(other.modiDate)) return false;
        if (this.modiOperId == null) {
            if (other.modiOperId != null) return false;
        } else if (!this.modiOperId.equals(other.modiOperId)) return false;
        if (this.modiOperName == null) {
            if (other.modiOperName != null) return false;
        } else if (!this.modiOperName.equals(other.modiOperName)) return false;
        if (this.modiTime == null) {
            if (other.modiTime != null) return false;
        } else if (!this.modiTime.equals(other.modiTime)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        } else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        } else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        } else if (!this.regTime.equals(other.regTime)) return false;
        if (this.showMessage == null) {
            if (other.showMessage != null) return false;
        } else if (!this.showMessage.equals(other.showMessage)) return false;
        return true;
    }

    public void initAddVlaue(CustOperatorInfo anOperatorInfo) {

        this.setId(SerialGenerator.getLongValue("CustResolveFile.id"));
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.modiOperId = anOperatorInfo.getId();
        this.modiOperName = anOperatorInfo.getName();
        this.operOrg = anOperatorInfo.getOperOrg();

    }

}

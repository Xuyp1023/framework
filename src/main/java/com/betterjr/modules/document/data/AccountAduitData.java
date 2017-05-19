package com.betterjr.modules.document.data;

import com.betterjr.common.data.BaseRemoteEntity;

public class AccountAduitData implements  BaseRemoteEntity, java.io.Serializable {

    private static final long serialVersionUID = -3564038015203402834L;

    /**
     * 供应商客户编号
     */
    private Long custNo;

    /**
     * 审批状态；0未审批，1审批通过，2审批中，9审批失败
     */
    private String auditStatus;

    /**
     * 客户在保理公司客户号
     */
    private String scfId;

    /**
     * 审批时间
     */
    private String aduitDate;

    /**
     * 审核失败附件类别
     */
    private String attachFalseList;

    /**
     * 在拜特这边的文件编号
     */
    private String falseFileNoList;

    /**
     * 描述，描述具体失败原因
     */
    private String description;

    private String factorNo;
    public Long getCustNo() {
        return this.custNo;
    }

    public void setCustNo(Long anCustNo) {
        this.custNo = anCustNo;
    }

    public String getAuditStatus() {
        return this.auditStatus;
    }

    public void setAuditStatus(String anAuditStatus) {
        this.auditStatus = anAuditStatus;
    }

    public String getScfId() {
        return this.scfId;
    }

    public void setScfId(String anScfId) {
        this.scfId = anScfId;
    }

    public String getAduitDate() {
        return this.aduitDate;
    }

    public void setAduitDate(String anAduitDate) {
        this.aduitDate = anAduitDate;
    }

    public String getAttachFalseList() {
        return this.attachFalseList;
    }

    public void setAttachFalseList(String anAttachFalseList) {
        this.attachFalseList = anAttachFalseList;
    }

    public String getFalseFileNoList() {
        return this.falseFileNoList;
    }

    public void setFalseFileNoList(String anFalseFileNoList) {
        this.falseFileNoList = anFalseFileNoList;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String anDescription) {
        this.description = anDescription;
    }

    public String getFactorNo() {
        return this.factorNo;
    }
    public void setFactorNo(String anFactorNo) {
        this.factorNo = anFactorNo;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" custNo=").append(custNo);
        sb.append(", auditStatus=").append(auditStatus);
        sb.append(", scfId=").append(scfId);
        sb.append(", aduitDate=").append(aduitDate);
        sb.append(", attachFalseList=").append(attachFalseList);
        sb.append(", falseFileNoList=").append(falseFileNoList);
        sb.append(", description=").append(description);
        sb.append(", factorNo=").append(factorNo);
        sb.append(" ]");

        return sb.toString();
    }
}

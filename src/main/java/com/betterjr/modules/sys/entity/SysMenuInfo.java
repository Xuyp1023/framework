package com.betterjr.modules.sys.entity;

import java.util.List;

import com.betterjr.common.annotation.*;
import com.betterjr.common.data.PlatformBaseRuleType;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterStringUtils;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CFG_MENU")
public class SysMenuInfo implements BetterjrEntity {
    /**
     * 菜单编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="菜单编号", comments = "菜单编号")
    private Integer id;

    /**
     * 系统缩写
     */
    @Column(name = "C_SYS",  columnDefinition="VARCHAR" )
    @MetaData( value="系统缩写", comments = "系统缩写")
    private String sysNo;

    /**
     * 菜单名称
     */
    @Column(name = "C_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="菜单名称", comments = "菜单名称")
    private String menuName;

    /**
     * 菜单标题
     */
    @Column(name = "C_TITLE",  columnDefinition="VARCHAR" )
    @MetaData( value="菜单标题", comments = "菜单标题")
    private String menuTitle;

    /**
     * 访问URL地址
     */
    @Column(name = "C_URL",  columnDefinition="VARCHAR" )
    @MetaData( value="访问URL地址", comments = "访问URL地址")
    private String menuUrl;

    /**
     * 访问的目标区域
     */
    @Column(name = "C_TARGET",  columnDefinition="VARCHAR" )
    @MetaData( value="访问的目标区域", comments = "访问的目标区域")
    private String workTarget;

    /**
     * 父菜单编号，如果父菜单编号为0表示一级菜单
     */
    @Column(name = "N_PARENT_ID",  columnDefinition="INTEGER" )
    @MetaData( value="父菜单编号", comments = "父菜单编号，如果父菜单编号为0表示一级菜单")
    private Integer parentId;

    /**
     * 菜单序号，将按照该字段做排序
     */
    @Column(name = "N_ORDER",  columnDefinition="INTEGER" )
    @MetaData( value="菜单序号", comments = "菜单序号，将按照该字段做排序")
    private Integer menuOrder;

    /**
     * 描述
     */
    @Column(name = "C_DESCRIPTION",  columnDefinition="VARCHAR" )
    @MetaData( value="描述", comments = "描述")
    private String description;

    /**
     * 状态，0 停用 1启用
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "状态，0 停用 1启用")
    private String status;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 地址的访问方式
     */
    @Column(name = "C_OPENMODE",  columnDefinition="VARCHAR" )
    @MetaData( value="地址的访问方式", comments = "地址的访问方式")
    private String workOpenMode;

    /**
     * 是否是末端节点，0有字节点，1无子节点
     */
    @Column(name = "C_ENDNODE",  columnDefinition="VARCHAR" )
    @MetaData( value="是否是末端节点", comments = "是否是末端节点，0有字节点，1无子节点")
    private Boolean endNode;

    /**
     * 功能特殊角色
     */
    @Column(name = "C_RULE_LIST",  columnDefinition="VARCHAR" )
    @MetaData( value="功能特殊角色", comments = "功能特殊角色")
    private String ruleList;

    private static final long serialVersionUID = -2555616939997634419L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo == null ? null : sysNo.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle == null ? null : menuTitle.trim();
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    public String getWorkTarget() {
        return workTarget;
    }

    public void setWorkTarget(String workTarget) {
        this.workTarget = workTarget == null ? null : workTarget.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getWorkOpenMode() {
        return workOpenMode;
    }

    public void setWorkOpenMode(String workOpenMode) {
        this.workOpenMode = workOpenMode == null ? null : workOpenMode.trim();
    }

    public Boolean getEndNode() {
        return endNode;
    }

    public void setEndNode(Boolean endNode) {
        this.endNode = endNode;
    }

    public String getRuleList() {
        return this.ruleList;
    }

    public void setRuleList(String anRuleList) {
        this.ruleList = anRuleList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sysNo=").append(sysNo);
        sb.append(", menuName=").append(menuName);
        sb.append(", menuTitle=").append(menuTitle);
        sb.append(", menuUrl=").append(menuUrl);
        sb.append(", workTarget=").append(workTarget);
        sb.append(", parentId=").append(parentId);
        sb.append(", menuOrder=").append(menuOrder);
        sb.append(", description=").append(description);
        sb.append(", status=").append(status);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", workOpenMode=").append(workOpenMode);
        sb.append(", endNode=").append(endNode);
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
        SysMenuInfo other = (SysMenuInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSysNo() == null ? other.getSysNo() == null : this.getSysNo().equals(other.getSysNo()))
            && (this.getMenuName() == null ? other.getMenuName() == null : this.getMenuName().equals(other.getMenuName()))
            && (this.getMenuTitle() == null ? other.getMenuTitle() == null : this.getMenuTitle().equals(other.getMenuTitle()))
            && (this.getMenuUrl() == null ? other.getMenuUrl() == null : this.getMenuUrl().equals(other.getMenuUrl()))
            && (this.getWorkTarget() == null ? other.getWorkTarget() == null : this.getWorkTarget().equals(other.getWorkTarget()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getMenuOrder() == null ? other.getMenuOrder() == null : this.getMenuOrder().equals(other.getMenuOrder()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getWorkOpenMode() == null ? other.getWorkOpenMode() == null : this.getWorkOpenMode().equals(other.getWorkOpenMode()))
            && (this.getEndNode() == null ? other.getEndNode() == null : this.getEndNode().equals(other.getEndNode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSysNo() == null) ? 0 : getSysNo().hashCode());
        result = prime * result + ((getMenuName() == null) ? 0 : getMenuName().hashCode());
        result = prime * result + ((getMenuTitle() == null) ? 0 : getMenuTitle().hashCode());
        result = prime * result + ((getMenuUrl() == null) ? 0 : getMenuUrl().hashCode());
        result = prime * result + ((getWorkTarget() == null) ? 0 : getWorkTarget().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getMenuOrder() == null) ? 0 : getMenuOrder().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getWorkOpenMode() == null) ? 0 : getWorkOpenMode().hashCode());
        result = prime * result + ((getEndNode() == null) ? 0 : getEndNode().hashCode());
        return result;
    }

    public boolean hasValidMenu(List<PlatformBaseRuleType> anRules){
        if (BetterStringUtils.isBlank(this.ruleList)){
            return true;
        }
        PlatformBaseRuleType tmpInnerRule;
//        System.out.println("this SystemMenu use ruleList is " + this.ruleList);
        for(String tmpStr : BetterStringUtils.split(ruleList, ",|;")){
            tmpInnerRule = PlatformBaseRuleType.checking(tmpStr);
            if (anRules.contains(tmpInnerRule)){
                
                return true;
            }
        }
        
        return false;
    }
}
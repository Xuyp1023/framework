package com.betterjr.modules.sys.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CFG_MENU_ROLE")
public class SysMenuRuleInfo implements BetterjrEntity {
    /**
     * 编号
     */
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号")
    private Integer id;

    /**
     * 角色编号
     */
    @Column(name = "N_RULE_ID",  columnDefinition="INTEGER" )
    @MetaData( value="角色编号", comments = "角色编号")
    private Integer ruleId;

    /**
     * 角色名称
     */
    @Column(name = "C_RULE_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="角色名称", comments = "角色名称")
    private String ruleName;

    /**
     * 菜单编号
     */
    @Column(name = "N_MENU_ID",  columnDefinition="INTEGER" )
    @MetaData( value="菜单编号", comments = "菜单编号")
    private Integer menuId;

    /**
     * 菜单名称
     */
    @Column(name = "C_MENU_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="菜单名称", comments = "菜单名称")
    private String menuName;

    /**
     * 状态，0 停用 1启用
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "状态，0 停用 1启用")
    private String status;

    private static final long serialVersionUID = 1449457003176L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ruleId=").append(ruleId);
        sb.append(", ruleName=").append(ruleName);
        sb.append(", menuId=").append(menuId);
        sb.append(", menuName=").append(menuName);
        sb.append(", status=").append(status);
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
        SysMenuRuleInfo other = (SysMenuRuleInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRuleId() == null ? other.getRuleId() == null : this.getRuleId().equals(other.getRuleId()))
            && (this.getRuleName() == null ? other.getRuleName() == null : this.getRuleName().equals(other.getRuleName()))
            && (this.getMenuId() == null ? other.getMenuId() == null : this.getMenuId().equals(other.getMenuId()))
            && (this.getMenuName() == null ? other.getMenuName() == null : this.getMenuName().equals(other.getMenuName()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRuleId() == null) ? 0 : getRuleId().hashCode());
        result = prime * result + ((getRuleName() == null) ? 0 : getRuleName().hashCode());
        result = prime * result + ((getMenuId() == null) ? 0 : getMenuId().hashCode());
        result = prime * result + ((getMenuName() == null) ? 0 : getMenuName().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }
}
package com.betterjr.modules.rule.entity;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.BetterStringUtils;

public class WorkRuleValidator extends RuleValidator {

    private static final long serialVersionUID = 4018775331174560204L;
    private Long id;
    private Integer priority;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String anStatus) {
        this.status = anStatus;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(Integer anPriority) {
        this.priority = anPriority;
    }

    public WorkRuleValidator(RuleValidator anV, RuleBusinValidator anBusinV) {
        BeanMapper.copy(anV, this);
        this.id = anBusinV.getId();
        this.priority = anBusinV.getValidPrior();
        this.status = anBusinV.getStatus();

        if (StringUtils.isNotBlank(anBusinV.getFieldName())) {
            this.setFieldName(anBusinV.getFieldName());
        }

        if (StringUtils.isNotBlank(anBusinV.getShowName())) {
            this.setShowName(anBusinV.getShowName());
        }

        if (StringUtils.isNotBlank(anBusinV.getMustItem())) {
            if ("0".equals(anBusinV.getMustItem())) {
                this.setMustItem(false);
            } else {
                this.setMustItem(true);
            }
        }
    }

    public String dateMinMessage() {

        return getShowName().concat("太小，请重新输入！");
    }

    public String dateMaxMessage() {

        return getShowName().concat("太大，请重新输入！");
    }

    public String strMaxLengthMessage() {

        return getShowName().concat("的内容太多，请适当减少！");
    }

    public String strMinLengthMessage() {

        return getShowName().concat("的内容太少，请完善！");
    }

    public String dataFormatMessage() {

        return getShowName().concat(" 数据格式有误！");
    }

    public String mustMessage() {

        return getShowName().concat(" 必须填写内容！");
    }

    public String matchMessage() {

        return getShowName().concat("的数据不匹配，请修正！");
    }

    public String dictMessage() {

        return getShowName().concat("无效");
    }
}

package com.betterjr.modules.rule;

import java.util.Map;

import com.ql.util.express.DefaultContext;

/**
 * 
 * 规则上下文<br>
 * 
 * @author zhoucy
 */
public class RuleContext<K, V> extends DefaultContext<K, V> {

    private static final long serialVersionUID = 20150124L;
    private Integer groupType;
    private String groupName;

    public RuleContext() {

    }

    public static RuleContext create(String anKey, Object anData) {
        RuleContext context = new RuleContext();
        context.put(anKey, anData);
        return context;
    }

    public RuleContext(Map anMap) {
        super(anMap);
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}

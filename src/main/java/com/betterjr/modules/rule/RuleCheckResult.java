package com.betterjr.modules.rule;

import java.io.Serializable;
import java.util.*;

import com.betterjr.common.utils.BetterStringUtils;

public class RuleCheckResult implements Serializable {

    private static final long serialVersionUID = -1514379340469018290L;
    private List<String> errorList = new LinkedList();
    private Map<String, Object> ruleWorkResult = new HashMap();

    private boolean workStatus = false;

    public void addRuleResult(String anRuleName, Object anObj) {
        workStatus = true;

        this.ruleWorkResult.put(anRuleName, anObj);
    }

    public boolean isWorking() {
        return workStatus;
    }

    public void addMessage(String anMessage) {

        this.errorList.add(anMessage);
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public boolean isOk() {

        return this.errorList.size() == 0;
    }

    public boolean workContinue(String anStr) {

        return errorList.size() == 0 && BetterStringUtils.isNotBlank(anStr);
    }

    public Map<String, Object> getRuleWorkResult() {
        return ruleWorkResult;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(", workStatus=").append(workStatus);
        sb.append(", ruleWorkResult=").append(ruleWorkResult);
        sb.append("\r, errorList=");
        for(String tmpStr : errorList){
            sb.append("\r\t").append(tmpStr);
        }
        sb.append("]");
        return sb.toString();
    }
}

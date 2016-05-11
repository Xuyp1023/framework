package com.betterjr.modules.rule.validator;

import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;
import java.util.*;

public class DataValidContext implements java.io.Serializable {
    private static final long serialVersionUID = 482265909230385315L;
    private static ThreadLocal<DataValidContext> validContext = new ThreadLocal();

    private final WorkRuleValidator validator;
    private final QLExpressContext context;
    private final RuleCheckResult result;
    private final RuleBusiness ruleBusin;
    private final Object value;
    private Object businValue;
    private final String message;
    private Map<String, Object> refValue = null;

    public static DataValidContext getValidContext() {

        return validContext.get();
    }

    public static void releaseValidContext() {

        validContext.set(null);
    }

    public static void addValidContext(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness anRuleBusin,
            Object anValue, Object anBusinValue, String anMessage) {
        validContext.set(new DataValidContext(anValidator, anContext, anResult, anRuleBusin, anValue, anBusinValue, anMessage));
    }

    public static void addRefValue(Map anRefValue) {
        DataValidContext dc = validContext.get();
        if (dc != null) {
            dc.refValue = anRefValue;
        }
    }

    public static void addBusinObj(Object anObj) {
        DataValidContext dc = validContext.get();
        if (dc != null) {
            dc.businValue = anObj;
        }
    }

    public DataValidContext(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness anRuleBusin,
            Object anValue, Object anBusinValue, String anMessage) {
        this.validator = anValidator;
        this.context = anContext;
        this.result = anResult;
        this.ruleBusin = anRuleBusin;
        this.value = anValue;
        this.businValue = anBusinValue;
        this.message = anMessage;
    }

    public WorkRuleValidator getValidator() {
        return this.validator;
    }

    public QLExpressContext getContext() {
        return this.context;
    }

    public RuleCheckResult getResult() {
        return this.result;
    }

    public RuleBusiness getRuleBusin() {
        return this.ruleBusin;
    }

    public Map<String, Object> getRefValue() {
        return this.refValue;
    }

    public Object getValue() {
        return this.value;
    }

    public Object getBusinValue() {
        return this.businValue;
    }

    public String getMessage() {
        return this.message;
    }

}

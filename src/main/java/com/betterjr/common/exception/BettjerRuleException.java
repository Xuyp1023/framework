package com.betterjr.common.exception;

public class BettjerRuleException extends BytterException {

    private static final long serialVersionUID = -7414620274955749491L;

    public BettjerRuleException(String anMessage) {
        super(anMessage);
    }

    public BettjerRuleException(int anCode, String message) {
        super(anCode, message);
    }

    public static RuntimeException wrap(Throwable t) {
        if (t instanceof RuntimeException) return (RuntimeException) t;
        return new BettjerRuleException(30000, "", t);
    }

    public BettjerRuleException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }
}

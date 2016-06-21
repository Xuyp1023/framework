package com.betterjr.common.exception;

public class BettjerNestedException extends BytterException {

    private static final long serialVersionUID = -7414620274955749491L;

    public BettjerNestedException(String anMessage) {
        super(anMessage);
    }

    public BettjerNestedException(int anCode, String message) {
        super(anCode, message);
    }
    
    public static RuntimeException wrap(Throwable t) {
        if (t instanceof RuntimeException) return (RuntimeException) t;
        return new BettjerNestedException(30000, "", t);
    }

    public BettjerNestedException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }
}

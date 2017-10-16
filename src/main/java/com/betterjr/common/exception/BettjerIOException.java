package com.betterjr.common.exception;

public class BettjerIOException extends BytterException {

    private static final long serialVersionUID = -7414620274955749491L;

    public BettjerIOException(String anMessage) {
        super(anMessage);
    }

    public BettjerIOException(int anCode, String message) {
        super(anCode, message);
    }

    public static RuntimeException wrap(Throwable t) {
        if (t instanceof RuntimeException) return (RuntimeException) t;
        return new BettjerIOException(30000, "", t);
    }

    public BettjerIOException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }
}

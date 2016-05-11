package com.betterjr.common.exception;

public class BytterFieldNotFoundException extends BytterException {

    private static final long serialVersionUID = -7414620274955749491L;

    public BytterFieldNotFoundException(String anMessage) {
        super(anMessage);
    }

    public BytterFieldNotFoundException(int anCode, String message) {
        super(anCode, message);
    }

    public BytterFieldNotFoundException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }
}

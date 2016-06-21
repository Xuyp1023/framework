package com.betterjr.common.exception;

public class BytterClassNotFoundException extends BytterException {

    private static final long serialVersionUID = -7414620274955749491L;

    public BytterClassNotFoundException(String anMessage) {
        super(anMessage);
    }

    public BytterClassNotFoundException(int anCode, String message) {
        super(anCode, message);
    }

    public BytterClassNotFoundException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }
}

package com.betterjr.common.exception;

public class DataTypeConvertException extends BaseException {

    private static final long serialVersionUID = -6210036892343609820L;

    public DataTypeConvertException(String msg) {
        super(msg);
    }

    public DataTypeConvertException(String msg, Throwable ex) {
        super(msg, ex);
    }

    public DataTypeConvertException() {

    }
}

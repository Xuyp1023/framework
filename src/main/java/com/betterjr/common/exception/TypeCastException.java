package com.betterjr.common.exception;

/**
 * 类型转换工具类<br>
 * 
 * @author zhoucy
 * @see java.lang.RuntimeException
 */
public class TypeCastException extends BytterException {

    private static final long serialVersionUID = 1L;

    Throwable nested;

    public TypeCastException() {
        nested = null;
    }

    public TypeCastException(String msg) {
        super(msg);
        nested = null;
    }

    public TypeCastException(String msg, Throwable nested) {
        super(msg);
        this.nested = null;
        this.nested = nested;
    }

    public TypeCastException(Throwable nested) {

        this.nested = null;
        this.nested = nested;
    }

    public String getNonNestedMessage() {
        return super.getMessage();
    }

    public Throwable getNested() {
        if (nested == null) return this;
        else return nested;
    }

}

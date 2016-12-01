package com.betterjr.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BaseException extends RuntimeException {
    /***/
    private static final long serialVersionUID = 3985761994734368220L;

    private Throwable cause;

    private String errorCode = "UNKNOW_ERROR";

    public BaseException(String anErorCode, String msg) {
        super(msg);
        this.errorCode = anErorCode;
    }

    public BaseException(String msg) {
        super(msg);
    }

    public Throwable getCause() {
        return (this.cause == this ? null : this.cause);

    }

    public BaseException(String msg, Throwable ex) {
        super(msg);
        this.cause = ex;
    }

    public BaseException() {

    }

    public BaseException(Throwable ex) {
        cause = ex;
    }

    public String getMessage() {
        if (getCause() == null) {
//            return "Error Code :".concat(this.errorCode).concat("; ").concat(super.getMessage());
            return super.getMessage();
        }
        else {
            return "Error Code :".concat(this.errorCode).concat("; ")
                    .concat(super.getMessage() + "; nested exception is " + getCause().getClass().getName() + ": " + getCause().getMessage());
        }
    }

    public void printStackTrace(PrintStream ps) {
        ps.println("Error Code :" + this.errorCode);
        if (getCause() == null) {
            super.printStackTrace(ps);
        }
        else {
            ps.println(this);
            getCause().printStackTrace(ps);
        }
    }

    public void printStackTrace(PrintWriter pw) {
        pw.println("Error Code :" + this.errorCode);
        if (getCause() == null) {
            super.printStackTrace(pw);
        }
        else {
            pw.println(this);
            getCause().printStackTrace(pw);
        }
    }

    public boolean contains(Class exClass) {
        if (exClass == null) {
            return false;
        }
        Throwable ex = this;
        while (ex != null) {
            if (exClass.isInstance(ex)) {
                return true;
            }
            if (ex instanceof BaseException) {
                ex = ((BaseException) ex).getCause();
            }
            else {
                ex = null;
            }
        }
        return false;
    }
}

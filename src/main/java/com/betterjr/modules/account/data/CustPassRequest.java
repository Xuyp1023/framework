package com.betterjr.modules.account.data;

public class CustPassRequest implements java.io.Serializable {

    private static final long serialVersionUID = -6659806153608034901L;

    private String passwd;
    private String newPasswd;
    private String okPasswd;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getNewPasswd() {
        return newPasswd;
    }

    public void setNewPasswd(String newPasswd) {
        this.newPasswd = newPasswd;
    }

    public String getOkPasswd() {
        return okPasswd;
    }

    public void setOkPasswd(String okPasswd) {
        this.okPasswd = okPasswd;
    }

}

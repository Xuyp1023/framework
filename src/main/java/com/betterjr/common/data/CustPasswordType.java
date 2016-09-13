package com.betterjr.common.data;

/**
 * 平台客户密码类型，个人登录密码，个人交易密码；机构登录密码，机构交易密码
 * 
 * @author zhoucy
 *
 */
public enum CustPasswordType {
    PERSON("0"), PERSON_TRADE("1"), ORG("6"), ORG_TRADE("7");
    private final String passType;

    CustPasswordType(String anPassType) {
        passType = anPassType;
    }

    public CustPasswordType exchangeTrade() {
        if (this == PERSON) {
            
            return PERSON_TRADE;
        }
        else if (this == ORG) {
            
            return ORG_TRADE;
        }
        else {
            return this;
        }
    }

    public String getPassType() {

        return this.passType;
    }
}

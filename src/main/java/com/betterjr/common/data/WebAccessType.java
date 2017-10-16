package com.betterjr.common.data;

/**
 * 客户访问类型定义，包括个人PC，个人PC需要交易密码，个人移动，一个移动需要密码，机构PC，机构PC需要密码，机构手机，机构手机需要密码，全部；默认是全部类型
 * @author zhoucy
 *
 */
public enum WebAccessType {
    PERSON_PC("0"), PERSON_PC_PASS("1"), PERSON_MOBILE("0"), PERSON_MOBILE_PASS("1"), ORG_PC("6"), ORG_PC_PASS(
            "7"), ORG_MOBILE("6"), ORG_MOBILE_PASS("7"), ALL("6");

    private final String passType;

    WebAccessType(String anPassType) {
        passType = anPassType;
    }

    public String getPassType() {

        return this.passType;
    }
}

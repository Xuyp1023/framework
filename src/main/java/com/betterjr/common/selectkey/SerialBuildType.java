package com.betterjr.common.selectkey;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;

public enum SerialBuildType {
    SERIAL_ALL("0", " "), SERIAL_DAY("1", "yyyyMMdd"), SERIAL_WEEK("2", "yyyyww"), SERIAL_MONTH("3",
            "yyyyMM"), SERIAL_YEAR("4", "yyyy");
    private final String value;
    private final String matchPatten;

    SerialBuildType(String anValue, String anMatchPatten) {
        value = anValue;
        matchPatten = anMatchPatten;
    }

    public String getValue() {
        return this.value;
    }

    public static SerialBuildType checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {
                for (SerialBuildType tmpSBT : SerialBuildType.values()) {
                    if (tmpSBT.value.equals(anWorkType)) {

                        return tmpSBT;
                    }
                }

                return SerialBuildType.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据不同的类型，获得模式匹配的前缀信息
     * 
     * @return
     */
    public String findMachValue() {

        return StringUtils.trim(BetterDateUtils.getDate(this.matchPatten));
    }

    public static void main(String[] args) {
        int aa = 5;
        System.out.println(StringUtils.leftPad(Integer.toString(aa), 5, "0"));
        System.out.println(BetterDateUtils.formatDate(BetterDateUtils.parseDate("20100103"), "yyyyww"));
    }
}

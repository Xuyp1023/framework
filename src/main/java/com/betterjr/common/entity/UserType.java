package com.betterjr.common.entity;

import org.apache.commons.lang3.StringUtils;

public enum UserType {
    NONE_USER, ORG_USER, OPERATOR_USER, OPERATOR_QUERY, OPERATOR_CHECKER, OPERATOR_ADUIT, OPERATOR_ADMIN, SYS_ADMIN, PERSON_USER;
    public static UserType checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return UserType.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {

        }
        return NONE_USER;
    }

    public static String[] findUserRule(UserType anType) {
        if (anType == PERSON_USER) {
            return new String[] { PERSON_USER.name() };
        }
        else if (anType == ORG_USER) {
            return new String[] { ORG_USER.name() };
        }
        else if (anType == OPERATOR_USER) {
            return new String[] { ORG_USER.name(), OPERATOR_USER.name() };
        }
        else if (anType == OPERATOR_ADMIN) {
            return new String[] { ORG_USER.name(), OPERATOR_ADMIN.name() };
        }
        return null;
    }
}

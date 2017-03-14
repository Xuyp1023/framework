// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年3月9日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sys.utils;

import java.util.Map;

import com.betterjr.common.utils.JedisUtils;

/**
 * @author liuwl
 *
 */
public final class TicketUtils {

    public static final String custTicketPrefix = "cust::ticket::";

    public static void putTicket(final String anTicket, final Map<String, String> anParam) {
        final String result = JedisUtils.setObject(custTicketPrefix + anTicket, anParam, 30 * 60);//30 分钟有效
    }

    public static Map<String, String> getToken(final String anTicket) {
        final Map<String, String> result = JedisUtils.getObject(custTicketPrefix + anTicket);
        return result;
    }

}

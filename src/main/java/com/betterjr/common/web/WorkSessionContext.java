package com.betterjr.common.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

public class WorkSessionContext {
    private static Map<String, HttpSession> workMap = new ConcurrentHashMap();;

    public static void addSession(HttpSession anSession) {
        if (anSession != null) {
            workMap.put(anSession.getId(), anSession);
        }
    }

    public static void removeSession(HttpSession anSession) {
        if (anSession != null) {
            workMap.remove(anSession.getId());
        }
    }

    public static HttpSession getSession(String anSessionId) {
        if (anSessionId == null) {

            return null;
        }

        return workMap.get(anSessionId);
    }
}

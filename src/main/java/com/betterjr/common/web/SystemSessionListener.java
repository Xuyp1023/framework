package com.betterjr.common.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SystemSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent anEvent) {
        WorkSessionContext.addSession(anEvent.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent anEvent) {
        WorkSessionContext.removeSession(anEvent.getSession());
    }

}

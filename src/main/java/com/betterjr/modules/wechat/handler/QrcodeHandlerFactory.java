// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月18日, liuwl, creation
// ============================================================================
package com.betterjr.modules.wechat.handler;

/**
 * @author liuwl
 *
 */
public final class QrcodeHandlerFactory {
    private QrcodeHandlerFactory() {
        throw new AssertionError();
    }

    private final static class SingletonClass {
        public static final QrcodeHandlerFactory instance = new QrcodeHandlerFactory();
    }

    public QrcodeHandlerFactory getInstance() {
        return SingletonClass.instance;
    }

    public static QrcodeHandler createQrcodeHandler(final int anWorkQRCode) {
        QrcodeHandler instance = null;
        if (anWorkQRCode >= 100000000 &&  anWorkQRCode <= 199999999) { //这个范围为绑定事件
            instance = new UserBindQrcodeHandler();
        }

        if (instance == null) {
            instance = new DefaultQrcodeHandler();
        }
        return instance;
    }

    public static QrcodeHandler createQrcodeHandler(final String anWorkQRCode) {
        final Integer workQRCode = Integer.valueOf(anWorkQRCode);
        return createQrcodeHandler(workQRCode);
    }
}

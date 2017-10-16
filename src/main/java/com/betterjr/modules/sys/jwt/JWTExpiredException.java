package com.betterjr.modules.sys.jwt;

public class JWTExpiredException extends JWTVerifyException {

    private static final long serialVersionUID = 6899250797022324704L;
    private long expiration;

    public JWTExpiredException(long expiration) {
        this.expiration = expiration;
    }

    public JWTExpiredException(String message, long expiration) {
        super(message);
        this.expiration = expiration;
    }

    public long getExpiration() {
        return expiration;
    };
}

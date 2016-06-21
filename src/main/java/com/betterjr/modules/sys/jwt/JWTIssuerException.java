package com.betterjr.modules.sys.jwt;

public class JWTIssuerException extends JWTVerifyException {
  
    private static final long serialVersionUID = -148519706263143279L;
    private final String issuer;

    public JWTIssuerException(String issuer) {
        this.issuer = issuer;
    }

    public JWTIssuerException(String message, String issuer) {
        super(message);
        this.issuer = issuer;
    }

    public String getIssuer() {
        return issuer;
    }
}

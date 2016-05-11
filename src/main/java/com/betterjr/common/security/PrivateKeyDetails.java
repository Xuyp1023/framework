package com.betterjr.common.security;

import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Private key details.
 *
 * @since 4.4
 */
public final class PrivateKeyDetails {

    private final String type;
    private final X509Certificate[] certChain;

    public PrivateKeyDetails(final String anType, final X509Certificate[] anCertChain) {
        super();
        this.type = anType;
        this.certChain = anCertChain;
    }

    public String getType() {
        return type;
    }

    public X509Certificate[] getCertChain() {
        return certChain;
    }

    @Override
    public String toString() {
        return type + ':' + Arrays.toString(certChain);
    }

}

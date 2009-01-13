package com.hp.it.spf.pulse.portal.component.secondarypagetype.util;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * This class is used to replace the default X509TrustManager.
 * 
 * @author <link href="huakun.gao@hp.com"> Gao, Hua-Kun </link>
 * @see javax.net.ssl.X509TrustManager
 * @version TBD
 */
public class ExX509TrustManager implements X509TrustManager {
    /**
     * Empty implement of X509TrustManager#checkClientTrusted(X509Certificate chain[], String authType)
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
     * @param chain Certificate chain
     * @param authType Authentication type
     * 
     * @throws CertificateException CertificateException
     */
    public void checkClientTrusted(X509Certificate chain[], String authType)
            throws CertificateException {

    }

    /**
     * Empty implement of X509TrustManager#checkServerTrusted(X509Certificate chain[], String authType)
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
     * @param chain Certificate chain
     * @param authType Authentication type
     * 
     * @throws CertificateException CertificateException
     */
    public void checkServerTrusted(X509Certificate chain[], String authType)
            throws CertificateException {
    }

    /**
     * Empty implement of X509TrustManager#getAcceptedIssuers()
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     * @return null
     */
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

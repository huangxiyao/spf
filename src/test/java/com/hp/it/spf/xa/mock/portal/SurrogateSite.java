package com.hp.it.spf.xa.mock.portal;

import java.util.Locale;

import com.epicentric.site.Site;

public class SurrogateSite extends Site {
    
    private Locale defaultLocale = Locale.US;
    public Locale getDefaultLocale() {
        return defaultLocale;
    }
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    private String dnsName = "TestDnsName";
    public String getDNSName() {
        return dnsName;
    }
    public void setDNSName(String dnsName) {
        this.dnsName = dnsName;
    }

}

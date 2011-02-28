package com.hp.frameworks.wpa.hpweb.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.jstl.core.Config;


/**
 * <p>
 * The LocaleSelectionFilter provides a mechanism for communicating user locale
 * information to the Java Standard Tag Library (JSTL). The JSTL is used for all
 * HPWeb layout localization and this filter can be used to select which locale
 * will be used.
 * </p>
 * 
 * <p>
 * If present, the request parameter named "locale" will be used to configure
 * the JSTL localization context. The "locale" request parameter can take one of
 * two forms: "<lang code>-<ctry code>" or "<lang code>". For example:
 * http://localhost/mysite/index.jsp?locale=es-MX.
 * </p>
 * 
 * <p>
 * If the "locale" request parameter is not specified, the filter will default
 * to using the locale value present on the ServletRequest object. The locale
 * value on the ServletRequest is typically initialized with the value of the
 * incoming Accept-Language HTTP header.
 * </p>
 */
public class LocaleSelectionFilter implements Filter
{

    public void init(FilterConfig filterConfig) { }
    
    
    public void destroy() { }


    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {
        String localeString = request.getParameter("locale");
     
        Locale userLocale;
        
        if (localeString != null)
        {
            String[] localeParts = localeString.split("-");
            
            if (localeParts.length < 2)
            {
                userLocale = new Locale(localeString);
            }
            else
            {
                userLocale = new Locale(localeParts[0], localeParts[1]);
            }
        }
        else
        {
            userLocale = request.getLocale();
        }

        Config.set(request, Config.FMT_LOCALE, userLocale);

        chain.doFilter(request, response);
    }

}

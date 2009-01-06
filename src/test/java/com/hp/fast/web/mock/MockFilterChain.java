/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/test/java/com/hp/fast/web/mock/MockFilterChain.java,v 1.1 2007/04/03 10:05:11 marcd Exp $ */

package com.hp.fast.web.mock;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author marcd
 */
public class MockFilterChain implements FilterChain {

    public void doFilter(ServletRequest arg0, ServletResponse arg1)
            throws IOException, ServletException {
    }

}

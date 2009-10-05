/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.sun.com/cddl/cddl.html or
 * at portlet-repository/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at portlet-repository/CDDLv1.0.txt. 
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 */

package com.hp.it.spf.apps.displayprofiletest.portlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class DisplayProfileTestPortlet extends GenericPortlet {

    private static final String viewPage = "/WEB-INF/jsp/displayprofiletest/view.jsp";

    protected void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {

        if (getPortletContext().getResourceAsStream(viewPage) != null) {
            try {
                // dispatch view request to view.jsp
                PortletRequestDispatcher dispatcher = getPortletContext()
                        .getRequestDispatcher(viewPage);
                dispatcher.include(request, response);
            } catch (IOException e) {
                throw new PortletException("doView exception", e);
            }
        } else {
            throw new PortletException("view.jsp missing.");
        }
    }

    public void render(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {

        super.render(request, response);

    }
}

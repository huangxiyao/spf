/*
 * CDDL HEADER START
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://www.sun.com/cddl/cddl.html and legal/CDDLv1.0.txt
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 * CDDL HEADER END
 */

package com.hp.it.spf.openportal.portletcontainer.admin.registry.database;

import java.util.Map;

import com.sun.portal.portletcontainer.context.registry.PortletRegistryContext;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

/**
 * PortletWindowPreferenceRegistryContextImpl is a concrete implementation of
 * the PortletWindowPreferenceRegistryContext interface.
 */
public class PortletWindowPreferenceRegistryContextDBImpl
		extends
		com.sun.portal.portletcontainer.admin.registry.database.PortletWindowPreferenceRegistryContextDBImpl {

	// "U1BGX0NPTkZJR19BRE1JTg==" is the base64 encoded "SPF_CONFIG_ADMIN"
	private final String defaultBase64 = "U1BGX0NPTkZJR19BRE1JTg==";

	public PortletWindowPreferenceRegistryContextDBImpl()
			throws PortletRegistryException {
		super();
	}

	public Map getPreferencesReadOnly(String portletWindowName, String userName)
			throws PortletRegistryException {
		return super.getPreferencesReadOnly(portletWindowName,
				formatDefaultUserName(userName));
	}

	public Map getPreferences(String portletWindowName, String userName)
			throws PortletRegistryException {
		return super.getPreferences(portletWindowName,
				formatDefaultUserName(userName));
	}

	public void savePreferences(String portletName, String portletWindowName,
			String userName, Map prefMap) throws PortletRegistryException {
		super.savePreferences(portletName, portletWindowName,
				formatDefaultUserName(userName), prefMap);
	}

	public void savePreferences(String portletName, String portletWindowName,
			String userName, Map prefMap, boolean readOnly)
			throws PortletRegistryException {
		super.savePreferences(portletName, portletWindowName,
				formatDefaultUserName(userName), prefMap, readOnly);
	}

	public void createPreferences(String portletName, String portletWindowName,
			String userName, Map prefMap, boolean create)
			throws PortletRegistryException {
		super.createPreferences(portletName, portletWindowName,
				formatDefaultUserName(userName), prefMap, create);
	}

	private String formatDefaultUserName(String userNmae) {
		// for config mode hack, if it is base64 encoded "SPF_CONFIG_ADMIN", let
		// it be "default"
		return defaultBase64.equals(userNmae) ? PortletRegistryContext.USER_NAME_DEFAULT
				: userNmae;
	}

}

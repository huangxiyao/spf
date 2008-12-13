/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.misc.portlet;

/**
 * <p>
 * Container class for common portlet constants which need exposure into
 * multiple portlet classes.
 * </p>
 * 
 * @author <link href="wei.teng@hp.com">Teng Wei</link>
 * @version TBD
 */
public final class Consts extends com.hp.it.spf.xa.misc.Consts {

	/**
	 * The key to get the portlet friendly id from the portlet request. This is
	 * non-standard (Vignette puts the portlet friendly ID into this attribute
	 * of the portlet request automatically).
	 */
	/*
	 * Added by CK for 1000790086 TODO - we will need to pass the portlet
	 * friendly ID over WSRP instead - so the key name for accessing that will
	 * probably change.
	 */
	public static final String KEY_PORTLET_FRIENDLY_ID = "com.vignette.portal.portlet.friendlyid";

	/**
	 * The key to get the site name (ie what Vignette calls the "site DNS name")
	 * from the portlet request. This is non-standard (Vignette puts it into
	 * this attribute automatically).
	 */
	/*
	 * Added by CK for 1000790073 TODO - we will need to pass the portal site
	 * name over WSRP instead - so the key name for accessing that will probably
	 * change.
	 */
	public static final String KEY_PORTLET_SITE_NAME = "com.vignette.portal.site.dns";

}
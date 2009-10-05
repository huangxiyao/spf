/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.misc.portlet;

/**
 * <p>
 * Container class for common portlet constants which need exposure into
 * multiple portlet classes. Currently, most of the constants provided by this
 * class are actually inherited from the SPF common utilities'
 * {@link com.hp.it.spf.xa.misc.Consts} class.
 * </p>
 * 
 * @author <link href="wei.teng@hp.com">Teng Wei</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.misc.Consts</code>
 */
public class Consts extends com.hp.it.spf.xa.misc.Consts {

	/**
	 * The name of the render parameter containing the document fragment name to
	 * use in your <code>help</code> mode, passed by a contextual-help
	 * no-script hyperlink.
	 */
	public final static String PARAM_FRAGMENT = "fragment";

}
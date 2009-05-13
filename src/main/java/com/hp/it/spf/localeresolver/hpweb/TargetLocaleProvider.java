/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import java.util.Collection;

/**
 * Provides a collection of all locales supported by a target environment, plus
 * a collection of locales allowed by the target.
 * 
 * @author Scott Jorgenson
 * @version $Revision: 2.0 $
 * @since $Revision: 2.0 $
 */
public interface TargetLocaleProvider {
	/**
	 * Returns a collection of all locales supported by a target environment.
	 * 
	 * @return set of locales or empty set.
	 */
	Collection getAllLocales();

	/**
	 * Returns a collection of locales allowed by a target environment.
	 * 
	 * @return set of locales or empty set.
	 */
	Collection getAllowedLocales();
}

/**
 * This package contains plain Java classes related to property file management,
 * usable only in Vignette portal components in the Shared Portal Framework.
 * 
 * <a name="PropertyResourceBundleManager"> <h3>PropertyResourceBundleManager</h3>
 * </a>
 * <p>
 * The portal-specific
 * {@link com.hp.it.spf.xa.properties.portal.PropertyResourceBundleManager}
 * class provides hot-reloadable cached access to property files stored either
 * on the classpath or in your current portal component's support files.
 * </p>
 * 
 * <ul>
 * <li>Property files are loaded first from the secondary support files in the
 * current Vignette portal component. If not found there, then they are loaded
 * from the current context classloader, and thus may be located anywhere inside
 * or outside the Vignette portal application, so long as any non-standard
 * location for class loading has been configured on the JVM classpath.</li>
 * <li>Property files are loaded once and then cached indefinitely until the
 * file on disk changes or is removed. The cache is then refreshed automatically
 * at the time of the next access.</li>
 * <li>The property files may be localized and tagged by locale, in the manner
 * of {@link java.util.ResourceBundle}, but that is optional.</li>
 * </ul>
 * 
 * <p>
 * Please see the class for more information. <b>Note:</b> There is also a
 * common <code>PropertyResourceBundleManager</code> only uses the classloader
 * and does not load support files. You can read about it in the SPF Common
 * Utilities Developer Guide. The portal-specific
 * <code>PropertyResourceBundleManager</code> is better for portal component
 * developers, though: it does everything the common
 * <code>PropertyResourceBundleManager</code> does, while loading from support
 * files too.
 * </p>
 */
package com.hp.it.spf.xa.properties.portal;
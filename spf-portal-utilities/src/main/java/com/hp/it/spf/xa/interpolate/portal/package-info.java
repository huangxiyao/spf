/**
 * <p>
 * This package contains plain Java classes for interpolating dynamic tokens in
 * text. The interpolation tokens supported in this package are:
 * </p>
 * <p>
 * <dl>
 * <dt><code>{AFTER:<i>date</i>}</code></dt>
 * <dd>Includes the surrounded content after the particular
 * <code><i>date</i></code>.</dd>
 * <dt><code>{AUTH:<i>types</i>}</code></dt>
 * <dd>Includes the surrounded content if the user in the current portal request
 * is using one of the listed authentication <code><i>types</i></code> (eg,
 * HPP). Otherwise suppresses the surrounded content.</dd>
 * <dt><code>{BEFORE:<i>date</i>}</code></dt>
 * <dd>Includes the surrounded content until the particular
 * <code><i>date</i></code>.</dd>
 * <dt><code>{CONTENT-URL:<i>file</i>}</code></dt>
 * <dd>Replaced with the properly-encoded URL for the secondary support file
 * named <code><i>file</i></code> in the current portal component.</dd>
 * <dt><code>{COUNTRY-CODE}</code></dt>
 * <dd>Replaced with the <a href="http://www.iso.org/iso/country_codes/iso_3166_code_lists/english_country_names_and_code_elements.htm"
 * >ISO 3166-1</a> country code for the current portal request.</dd>
 * <dt><code>{COUNTRY-CODE:<i>case</i>}</code></dt>
 * <dd>As above, forced to <code><i>lower</i></code> or
 * <code><i>upper</i></code> case (default is upper).</dd>
 * <dt><code>{COUNTRY-NAME}</code></dt>
 * <dd>Replaced with the localized name of the country in the locale for the
 * current portal request.</dd>
 * <dt><code>{DATE:<i>date</i>}</code></dt>
 * <dd>Replaced with a localized, timezone-adjusted, formatted string for the
 * particular <code><i>date</i></code>.</dd>
 * <dt><code>{EMAIL}</code></dt>
 * <dd>Replaced with the email address for the user in the current portal
 * request.</dd>
 * <dt><code>{EXIST:<i>key</i>}</code></dt>
 * <dd>Includes the surrounded content if the current portal request contains a
 * request or session parameter or attribute named <i>key</i>.</dd>
 * <dt><code>{GROUP:<i>groups</i>}</code></dt>
 * <dd>Includes the surrounded content if the user in the current portal request
 * is a member of one of the named <code><i>groups</i></code>. Otherwise
 * suppresses the surrounded content.</dd>
 * <dt><code>{HPP-LANGUAGE-CODE}</code></dt>
 * <dd>Replaced with the HP Passport language code for the current portal
 * request.</dd>
 * <dt><code>{HPP-LANGUAGE-CODE:<i>case</i>}</code></dt>
 * <dd>As above, forced to <code><i>lower</i></code> or
 * <code><i>upper</i></code> case (default is lower).</dd>
 * <dt><code>{INCLUDE:<i>key</i>}</code></dt>
 * <dd>Replaced with the value for the <code><i>key</i></code> property as found
 * in the token substitutions file (eg <code>default_includes.properties</code>
 * by default), a secondary support file in the current portal component.</dd>
 * <dt><code>{LANGUAGE-CODE}</code></dt>
 * <dd>Replaced with the <a
 * href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO
 * 639-1</a> language code for the current portal request.</dd>
 * <dt><code>{LANGUAGE-CODE:<i>case</i>}</code></dt>
 * <dd>As above, forced to <code><i>lower</i></code> or
 * <code><i>upper</i></code> case (default is lower).</dd>
 * <dt><code>{LANGUAGE-NAME}</code></dt>
 * <dd>Replaced with the localized name of the language for the current portal
 * request.</dd>
 * <dt><code>{LANGUAGE-TAG}</code></dt>
 * <dd>Replaced with the <a href="http://www.faqs.org/rfcs/rfc3066.html">RFC
 * 3066</a> language tag for the current portal request.</dd>
 * <dt><code>{LANGUAGE-TAG:<i>case</i>}</code></dt>
 * <dd>As above, forced to all <code><i>lower</i></code> or all
 * <code><i>upper</i></code> case (default is mixed case: lowercase language and
 * uppercase country).</dd>
 * <dt><code>{LOCALIZED-CONTENT-URL:<i>file</i>}</code></dt>
 * <dd>Replaced with the properly-encoded URL for the (possibly localized)
 * current portal component's secondary support file, the base name of which is
 * <code><i>file</i></code>.</dd>
 * <dt><code>{LOGGED-IN}</code></dt>
 * <dd>Includes the surrounded content if the user in the current portal request
 * is logged-in (ie, not anonymous). Otherwise suppresses the surrounded
 * content.</dd>
 * <dt><code>{LOGGED-OUT}</code></dt>
 * <dd>Includes the surrounded content if the user in the current portal request
 * is not logged-in (ie, is anonymous). Otherwise suppresses the surrounded
 * content.</dd>
 * <dt><code>{NAME}</code></dt>
 * <dd>Replaced with the full name for the user in the current portal request,
 * displayed in customary order for the locale.</dd>
 * <dt><code>{NAV-ITEM:<i>items</i>}</code></dt>
 * <dd>Includes the surrounded content if the current portal component is on a
 * page accessed via one of the named navigation <code><i>items</i></code>.
 * Otherwise suppresses the surrounded content.</dd>
 * <dt><code>{PAGE:<i>pages</i>}</code></dt>
 * <dd>Includes the surrounded content if the current portal component is on one
 * of the named portal <code><i>pages</i></code>. Otherwise suppresses the
 * surrounded content.</dd>
 * <dt><code>{REQUEST-URL}</code></dt>
 * <dd>Replaced with the complete URL for the current portal request which
 * targeted this portal component.</dd>
 * <dt><code>{REQUEST-URL:<i>spec</i>}</code></dt>
 * <dd>Replaced with the complete URL for the current portal request which
 * targeted this portal component, with scheme and port adjusted as specified.</dd>
 * <dt><code>{SITE-NAME}</code></dt>
 * <dd>Replaced with the name of the current portal site which includes this
 * portal component.</dd>
 * <dt><code>{SITE:<i>names</i>}...{/SITE}</code></dt>
 * <dd>Includes the surrounded content if the current request is for one of the
 * portal site <code><i>names</i></code>. Otherwise suppresses the surrounded
 * content.</dd>
 * <dt><code>{SITE-URL}</code></dt>
 * <dd>Replaced with the home-page URL for this portal site.</dd>
 * <dt><code>{SITE-URL:<i>spec</i>}</code></dt>
 * <dd>Replaced with the page URL for the given friendly URI at this portal
 * site. Scheme and port can also be specified.</dd>
 * <dt><code>{URL-ENCODE:<i>string</i>}</code></dt>
 * <dd>Replaced with the URL-encoded version of the given string.</dd>
 * <dt><code>{USER-PROPERTY:<i>key</i>}</code></dt>
 * <dd>Replaced with the named user property <code><i>key</i></code> in the
 * current portal request.</dd>
 * <dt><code>{VALUE:<i>key</i>}</code></dt>
 * <dd>Replaced with the value for the request or session attribute or parameter
 * named <code><i>key</i></code> as found in the current portal request.</dd>
 * </dl>
 * </p>
 * 
 * <a name="FileInterpolator"> <h3>FileInterpolator</h3> </a>
 * <p>
 * The {@link com.hp.it.spf.xa.interpolate.portal.FileInterpolator} class lets
 * you read and interpolate the content of a text file. The file must be stored
 * as a <i>secondary support file</i> in the current portal component. The file
 * may be localized; the class looks for the best-fit localized version of the
 * file when loading it. Please see the class documentation for more
 * information.
 * </p>
 * 
 * <a name="TokenParser"> <h3>TokenParser</h3> </a>
 * <p>
 * The {@link com.hp.it.spf.xa.interpolate.portal.TokenParser} provides parsing
 * methods for strings, for each of the above listed token types. This class is
 * used by the {@link com.hp.it.spf.xa.interpolate.portal.FileInterpolator} and
 * if you just use the <code>FileInterpolator</code> you will never need to use
 * the <code>TokenParser</code> directly. Please see the class documentation for
 * more information.
 * </p>
 */
package com.hp.it.spf.xa.interpolate.portal;


/**
 * {@link com.hp.it.spf.xa.dc.portal.DiagnosticContext} class present in this package alloows
 * to capture information or errors which can be useful for support team to quickly diagnose
 * the problem without the need to go immediately to the log files. The context class does not
 * replace the log files but can give a quick hint about the issue and where to look for more
 * information.
 * <p>
 * The non-error information can be saved in a free form using one of <tt>set</tt> methods.
 * The error information must be associated with the appropriate error code as defined in
 * {@link com.hp.it.spf.xa.dc.portal.ErrorCode} enumeration. Note that this enumeration should
 * be completed with additional error codes when they are discovered.
 */
package com.hp.it.spf.xa.dc.portal;
/**
 * The classes in this package provide management features which can be used with 
 * any JMX-compliant tool such as JDK-shipped JConsole.
 * <p>
 * Check {@link ManagementListener#registerMBeans(javax.management.MBeanServer)} for the list
 * of features which can be managed.
 * <p>
 * There are 2 ways to activate these features:
 * <ul>
 * <li>
 *  System property <tt>-Dspf.management.port={port number}</tt> specified in JVM startup
 *  arguments.
 * </li>
 * <li>
 *  Property file <tt>spfmanagement.properties</tt> available for the application classloader
 *  and containing property <tt>spf.management.port</tt>
 * </li>
 * </ul>
 * <p>
 * Once the port number is properly configured and the portal application started to access its
 * management features with for example JConsole the following command can be used:<br />
 * <tt>jconsole service:jmx:rmi:///jndi/rmi://{host}:{port}/spfmanagement</tt>
 */
package com.hp.it.spf.xa.management.portal;
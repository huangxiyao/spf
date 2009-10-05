/**
 * The classes in this package provide management features which can be used with 
 * any JMX-compliant tool such as JDK-shipped JConsole. In order to access these features user
 * must provide valid WebLogic Server username and password. See below for additional authentication
 * properties which can be used to further control the access.
 * <p>
 * Check {@link ManagementListener#registerMBeans(javax.management.MBeanServer)} for the list
 * of features which can be managed.
 * <p>
 * There are 2 ways to activate these features:
 * <ul>
 * <li>
 * 	System properties specified using <tt>-D{propertyName}={propertyValue}</tt> JVM startup
 *  arguments.
 * </li>
 * <li>
 *  Property file <tt>spfmanagement.properties</tt> available for the application classloader.
 * </li>
 * </ul>
 * If any of the properties is present in both locations, system property will be used. The following
 * properties are supported:
 * <ul>
 * <li>spf.management.port - available port number at which management features will be available.
 * This property is required. If not specified, management features will be disabled.</li>
 * <li>spf.management.usePlatformMBeanServer - optional; if present and equals <tt>true</tt> the
 * application MBeans will be registered in the JVM platform MBean server giving access to other
 * monitoring features of JVM; if not specified or if <tt>false</tt> dedicated MBeanServer is used.</li>
 * <li>spf.management.authentication.realmName - optional; if not present default Weblogic realm
 * will be used for authenticating users; if present, user will be authenticated in this realm.</li>
 * <li>spf.management.authentication.groupNames - optional, comma-separated list of groups;
 * if present only users belonging to this WebLogic groups will be allowed to access management
 * features; if not specified all authenticated users will be able to perform managemnt tasks.</li>
 * </ul>
 * <p>
 * Once the port number is properly configured and the portal application started to access its
 * management features with for example JConsole the following command can be used:<br />
 * <tt>jconsole service:jmx:rmi:///jndi/rmi://{host}:{port}/spfmanagement</tt>
 */
package com.hp.it.spf.xa.management.portal;
/**
 * The classes in this package provide management features which can be used with 
 * any JMX-compliant tool such as JDK-shipped JConsole.
 * <p>
 * Check {@link ManagementListener#registerMBeans(javax.management.MBeanServer)} for the list
 * of features which can be managed.
 * <p>
 * In order to active these features JVM must be started with the following argument:
 * <code>-Dspf.management.port={port number}</code>
 * It defines the port number to which RMI registry making the management features available will
 * be bound.
 * <p>
 * Once the port number is properly configured and the portal application started to access its
 * management features with for example JConsole the following command can be used:<br />
 * <tt>jconsole service:jmx:rmi:///jndi/rmi://{host}:{port}/spfmanagement</tt>
 * <p>
 * The choice of system property over a configuration file is to allow several portal instances 
 * to run on the same physical server. As each portal instance has its own dedicated registry
 * it also needs to use different port. Using configuration files would require server-specific
 * configuration file versions which would be difficult to manage. 
 */
package com.hp.it.spf.xa.management.portal;
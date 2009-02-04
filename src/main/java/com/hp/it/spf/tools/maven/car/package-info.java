/**
 * Provides classes necessary to implement the Maven CAR plugin. The CAR plugin 
 * defines a completely new packaging lifecycle that supports the creation of 
 * artifacts that conform to the Vignette Component Archive (CAR) standards.
 * 
 * <p>
 * For the most part the CAR packaging lifecycle is identical to the standard
 * WAR lifecycle except the final artifact is named with a .car extension
 * instead of a .war extension. The 'car' packaging definition assigns the
 * following life-cycle phase bindings:
 * </p>
 * 
 * <table border="1">
 * <tr>
 * <th>Life-cycle Phase</th>
 * <th>Mojo</th>
 * <th>Plugin</th>
 * </tr>
 * <tr>
 * <td>process-resources</th>
 * <td>resources</td>
 * <td>maven-resources-plugin</td>
 * </tr>
 * <tr>
 * <td>compile</th>
 * <td>compile</td>
 * <td>maven-compiler-plugin</td>
 * </tr>
 * <tr>
 * <td>process-test-resources</th>
 * <td>testResources</td>
 * <td>maven-resources-plugin</td>
 * </tr>
 * <tr>
 * <td>test-compile</th>
 * <td>testCompile</td>
 * <td>maven-compiler-plugin</td>
 * </tr>
 * <tr>
 * <td>test</th>
 * <td>test</td>
 * <td>maven-surefire-plugin</td>
 * </tr>
 * <tr>
 * <td>package</th>
 * <td>car</td>
 * <td>spf-maven-car-plugin</td>
 * </tr>
 * <tr>
 * <td>install</th>
 * <td>install</td>
 * <td>maven-install-plugin</td>
 * </tr>
 * <tr>
 * <td>deploy</th>
 * <td>deploy</td>
 * <td>maven-deploy-plugin</td>
 * </tr>
 * </table>
 * <p>
 * The <em>package</em> phase of the lifecycle is handled by the
 * {@link com.hp.it.spf.tools.maven.car.CarMojo CarMojo} class which is also
 * provided as part of this plugin.
 * </p>
 * 
 * <p>
 * For the most, the CAR plugin functions very similarly to the Maven-provided 
 * WAR plugin. The CAR plugin only supports a subset of the configuration 
 * options that are exposed by the WAR plugin, but the most significant 
 * difference between the two is that the CAR plugin generates an artifact with 
 * a <em>.car</em> extension instead of a <em>.war</em> extenstion.
 * </p>
 * 
 * <p>
 * Much like the WAR plugin, any webapp resources (things like JSP files or
 * the <em>component.xml</em> descriptor) that simply need to be included in
 * the final CAR should be placed in the <em>src/main/webapp/</em> directory
 * of the Maven project.
 * </p>
 * 
 * <h3>Usage</h3>
 * 
 * <p>
 * In order for Maven to locate the CAR plugin you need to ensure that you have
 * the appropriate plugin repository registered in either your POM or you
 * <em>settings.xml</em> file.
 * </p>
 * 
 * <p>
 * To specify the plugin repository in your POM, use a
 * <code>&lt;pluginRepositories&gt;</code> element similar to the one shown
 * below:
 * </p>
 * 
 * <pre>
 * &lt;project ...&gt;
 *   ...
 *   &lt;pluginRepositories&gt;
 *     &lt;pluginRepository&gt;
 *         &lt;id&gt;<em>enter_repository_id_here</em>&lt;/id&gt;
 *         &lt;url&gt;<em>enter_repository_url_here</em>&lt;/url&gt;
 *     &lt;/pluginRepository&gt;
 *   &lt;/pluginRepositories&gt;
 *   ...
 * &lt;/project&gt;</pre>
 * 
 * <p>
 * For information about configuring the plugin repository via the
 * <em>settings.xml</em> file instead please see the Maven <a
 * href="http://maven.apache.org/settings.html">Settings Reference</a>
 * documentation.
 * </p>
 * 
 * <p>
 * Once you've configured the appropriate plugin repository, the next step is to
 * register the CAR plugin in your POM. This is done by adding the following
 * <code>&lt;plugin&gt;</code> element to the <em>pom.xml</em> file for your
 * project:
 * </p>
 * 
 * <pre>
 * &lt;project ...&gt;
 *   ...
 *   &lt;build&gt;
 *     &lt;plugins&gt;
 *       &lt;plugin&gt;
 *         &lt;groupId&gt;com.hp.it.spf.tools&lt;/groupId&gt;
 *         &lt;artifactId&gt;spf-maven-car-plugin&lt;/artifactId&gt;
 *         &lt;extensions&gt;true&lt;/extensions&gt;
 *       &lt;/plugin&gt;
 *     &lt;/plugins&gt;
 *   &lt;/build&gt;
 *   ...
 * &lt;/project&gt;</pre>
 * 
 * <p>
 * The inclusion of the <code>&lt;extensions&gt;true&lt;/extensions&gt;</code>
 * element is very important as this is what allows the plugin to define a new
 * packaging lifecycle.
 * </p>
 * 
 * <p>
 * The final step is configure the POM to use the 'car' packaging:
 * </p>
 * 
 * <pre>
 * &lt;project ...&gt;
 *   ...
 *   &lt;packaging&gt;car&lt;/packaging&gt;
 *   ...
 * &lt;/project&gt;</pre>
 * 
 * <p>
 * Note that the <code>&lt;packaging&gt;</code> element is where you might have
 * specified a value like 'jar' or 'war' in the past projects.
 * </p>
 * 
 * <h3>Configuration</h3>
 * 
 * <p>
 * For configuration options related to the CAR plugin, see the documentation
 * for the {@link com.hp.it.spf.tools.maven.car.CarMojo CarMojo} class.
 * </p>
 * 
 * @since 1.0
 */
package com.hp.it.spf.tools.maven.car;
package com.hp.it.spf.tools.maven.car.packaging;

import java.io.File;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.jar.JarArchiver;


/**
 * Encapsulates all of the Maven project and CAR plugin configuration data that
 * may be needed by the various packaging tasks.
 * 
 * @see ICarPackagingTask
 * @author bdehamer
 * @since 1.0
 */
public interface ICarPackagingContext
{
    /**
     * Returns the Maven project object.
     *
     * @return the project
     */
    public MavenProject getProject();
    
    
    /**
     * Returns the project logger.
     *
     * @return the logger
     */
    Log getLog();
    
    
    /**
     * Returns the <code>File</code> object representing the source directory
     * for the web application resources.
     * 
     * @return the source directory for the web application resource files
     */
    public File getWebAppSourceDir();
    
    
    /**
     * Returns the <code>File</code> object representing the target directory
     * where all of the CAR resources will be assembled before they are archived.
     * 
     * @return the staging directory for the CAR resources
     */
    public File getWebAppOutputDir();
    
    
    /**
     * Returns the <code>File</code> object representing the directory
     * where the compiled Java class files to be included as part of the
     * final CAR are located.
     * 
     * @return the target directory for compiled Java class files
     */
    public File getClassesOutputDir();
    
    
    /**
     * Returns a boolean flag indicating whether or not archiving is enabled for
     * Java classes. A value of <code>true</code> indicates that Java class
     * files should be combined into a JAR before being included in the final
     * CAR. A vlaue of <code>false</code> indicates that Java classes should be
     * packaged as loose files in the <em>WEB-INF/classes/</em> directory of the
     * final CAR.
     * 
     * @return a flag indicating whether or not Java classes should be archived before
     * being included in the CAR
     */
    public boolean isClassArchivingEnabled();


    /**
     * Returns a boolean flag indicating whether or not the CAR being assembled
     * is a composite CAR. A value of <code>true</code> indicates that a
     * composite CAR is being assembled while a value of <code>false</code>
     * indicates that this is not a composite CAR.
     * 
     * @return a flag indicating whether or not a composite CAR is being
     * assembled.
     */
    public boolean isComposite();
    
    
    /**
     * Returns the <code>JarArchiver</code> to be used for archiving Java class
     * files into a JAR.
     * 
     * @see ICarPackagingContext#isClassArchivingEnabled()
     * @return the <code>JarArchiver</code>
     */
    public JarArchiver getJarArchiver();
    
    
    /**
     * Returns the archive configuration to be used when archiving Java
     * class files into a JAR.
     * 
     * @see ICarPackagingContext#isClassArchivingEnabled()
     * @return the archive configuration object
     */
    public MavenArchiveConfiguration getArchiveConfiguration();
    
    
    /**
     * Return the file name mapping pattern to be used when including
     * other artifacts as part of the final CAR.
     * 
     * @return the file name mapping pattern
     */
    public String getOutputFileNameMapping();
    
    
    /**
     * Returns the list of tokens to include when copying content
     * from the webapp source directory.
     * 
     * @return the list of tokens to include when copying webapp files
     */
    public String[] getIncludes();
    

    /**
     * Returns the list of tokens to exclude when copying content
     * from the webapp source directory.
     * 
     * @return the list of tokens to exclude when copying webapp files
     */    
    public String[] getExcludes();
}

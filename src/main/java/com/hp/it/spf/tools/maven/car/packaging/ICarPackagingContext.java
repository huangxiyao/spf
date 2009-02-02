package com.hp.it.spf.tools.maven.car.packaging;

import java.io.File;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.jar.JarArchiver;

public interface ICarPackagingContext
{
    /**
     * Returns the maven project.
     *
     * @return the project
     */
    public MavenProject getProject();
    
    /**
     * Returns the logger to use to output logging event.
     *
     * @return the logger
     */
    Log getLog();
    
    
    public File getWebAppSourceDir();
    
    
    public File getWebAppOutputDir();
    
    
    public File getClassesOutputDir();
    
    
    public boolean isClassArchivingEnabled();
    
    
    public boolean isComposite();
    
    
    public JarArchiver getJarArchiver();
    
    
    public MavenArchiveConfiguration getArchiveConfiguration();
    
    
    public String getOutputFileNameMapping();
    
    
    public String[] getIncludes();
    
    
    public String[] getExcludes();
}

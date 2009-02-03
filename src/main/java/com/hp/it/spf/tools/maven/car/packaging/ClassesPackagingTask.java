package com.hp.it.spf.tools.maven.car.packaging;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.war.util.ClassesPackager;
import org.apache.maven.plugin.war.util.PathSet;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.interpolation.InterpolationException;


/**
 * Packaging task responsible for gathering any compiled Java classes that need
 * to be included as part of the CAR. The compiled classes will either be copied
 * to the <em>WEB-INF/classes/</em> directory of the CAR, or combined into a JAR
 * and copied to the <em>WEB-INF/lib/</em> directory of the CAR. The behavior is
 * driven by the
 * {@link com.hp.it.spf.tools.maven.car.packaging.ICarPackagingContext#isClassArchivingEnabled()
 * ICarPackagingContext.isClassArchivingEnabled()} property.
 * 
 * @since 1.0
 * @author bdehamer
 */
public class ClassesPackagingTask extends AbstractCarPackagingTask
{

    // -------------------------------------------------------------- Constants
    
    
    // CAR-relative path to the classes directory
    private static final String CLASSES_PATH = "WEB-INF/classes/";
    
    
    // CAR-relative path to the lib directory
    private static final String LIB_PATH = "WEB-INF/lib/";
 
    
    // ------------------------------------------------------ Public Methods
    
    
    /**
     * Prepares any compiled Java classes for inclusion in the CAR. Classes are
     * eithered copied to the <em>WEB-INF/classes/</em> directory or JARed-up
     * and placed in the <em>WEB-INF/lib/</em> directory.
     * 
     * @param context
     * the packaging context containing the necessary configuration data
     * 
     * @throws MojoExecutionException
     * in case of any errors during the packaging process
     */
    @Override
    public void doPackaging(ICarPackagingContext context) throws MojoExecutionException
    {
        // Only need to do this packaging if there are actually class files in
        // the project.
        if (context.getClassesOutputDir().exists())
        {
            // If archiving is enabled, then assemble a JAR; otherwise,
            // simply copy the classes to the WEB-INF/classes directory
            if (context.isClassArchivingEnabled())
            {                
                this.generateJarArchive(context);                
            }
            else
            {
                final File webappClassesDirectory = new File(
                        context.getWebAppOutputDir(), CLASSES_PATH);
                
                // No need to copy classes if the source and destination
                // directory are the same
                if (!context.getClassesOutputDir().equals(webappClassesDirectory))
                {
                    this.copyClassFiles(context);
                    
                    context.getLog().info("Class files assembled");
                }
            }
        }
        else
        {
            context.getLog().info("No class files to assemble");
        }
    }

    
    // ------------------------------------------------------ Protected Methods
    
    
    /**
     * Copies compiled Java class files to the <em>WEB-INF/classes/</em>
     * subdirectory of the CAR staging directory.
     * 
     * @param context
     * the packaging context containing the necessary configuration data
     * 
     * @throws MojoExecutionException
     * in case of any errors during the packaging process
     */
    protected void copyClassFiles(ICarPackagingContext context) throws MojoExecutionException
    {
        // Get list of class files to copy
        final PathSet sources = this.getFileSet(
                context.getClassesOutputDir(), null, null);

        try
        {
            this.copyFiles(context.getClassesOutputDir(),
                    sources, context.getWebAppOutputDir(), CLASSES_PATH);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(
                    "Could not copy webapp classes["
                            + context.getClassesOutputDir()
                                    .getAbsolutePath() + "]", e);
        }
    }
    
    
    /**
     * Assembles Java class files into a JAR located in the
     * <em>WEB-INF/lib/</em> subdirectory of the CAR staging directory.
     * 
     * @param context
     * the packaging context containing the necessary configuration data
     * 
     * @throws MojoExecutionException
     * in case of any errors during the packaging process
     */
    protected void generateJarArchive(ICarPackagingContext context) throws MojoExecutionException
    {        
        try
        {            
            // Instantiate the JAR file to be generated
            String archiveName = this.getArchiveTargetName(context);
            File libDirectory = new File(context.getWebAppOutputDir(), LIB_PATH);
            File jarFile = new File(libDirectory, archiveName);
            
            // Package classes into JAR
            final ClassesPackager packager = new ClassesPackager();
         
            packager.packageClasses(
                    context.getClassesOutputDir(),
                    jarFile, 
                    context.getJarArchiver(), 
                    context.getProject(), 
                    context.getArchiveConfiguration());            
        }
        catch (InterpolationException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }        
    }    
    
    
    // -------------------------------------------------------- Private Methods
    
    
    /**
     * Returns the name of the JAR file that should be used for archiving
     * the project's class files.
     */
    private String getArchiveTargetName(ICarPackagingContext context)
            throws InterpolationException
    {
        MavenProject project = context.getProject();
        
        // We need to create a temporary artifact so that it can be passed to
        // the getArtifactTargetName method for processing
        VersionRange versionRange = 
            VersionRange.createFromVersion(project.getVersion());
        
        Artifact artifact = new DefaultArtifact(
                project.getGroupId(), 
                project.getArtifactId(),
                versionRange, 
                Artifact.SCOPE_COMPILE, 
                "jar", 
                null,
                new DefaultArtifactHandler("jar"));

        return this.getArtifactTargetlName(
                context.getOutputFileNameMapping(),
                artifact);
    }
        
}

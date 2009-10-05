package com.hp.it.spf.tools.maven.car.packaging;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.war.util.MappingUtils;
import org.apache.maven.plugin.war.util.PathSet;
import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;


/**
 * Abstract base class for implementations of the {@link ICarPackagingTask}
 * interface which provides support methods for common file copy operations.
 * 
 * @since 1.0
 * @author bdehamer
 */
public abstract class AbstractCarPackagingTask implements ICarPackagingTask
{
        
    // -------------------------------------------------------------- Constants
    
    
    // Default naming pattern to use for included dependencies
    private static final String DEFAULT_FILE_NAME_MAPPING = 
        "@{artifactId}@-@{version}@.@{extension}@";
    
        
    // Default include pattern for the DirectoryScanner
    private static final String[] DEFAULT_INCLUDES = {"**/**"};
        
    
    // ------------------------------------------------------ Public Methods
    
    
    /**
     * Process the packaging request. This will almost always result in one or
     * more files being copied to the <em>target/</em> directory of the Maven
     * project. There is nothing to return.
     * 
     * @param context
     * the packaging context containing the necessary configuration data
     * 
     * @throws MojoExecutionException
     * in case of any errors during the packaging process
     * 
     * @see ICarPackagingTask#doPackaging(ICarPackagingContext)
     */
    public abstract void doPackaging(ICarPackagingContext context) throws MojoExecutionException;
    
    
    // ------------------------------------------------------ Protected Methods

    
    /**
     * Calculates the target name for the given artifact. The default naming
     * pattern will be {artifactId}-{version}.{extension} if the
     * <tt>fileNameMapping</tt> parameter is null.
     * 
     * @param fileNameMapping
     * the file name mapping pattern to be applied
     * @param artifact
     * the artifact to be processed
     * 
     * @return the target name of the specified artifact
     * 
     * @throws InterpolationException
     * in the event that the target name cannot be resolved from the given
     * naming pattern.
     */
    protected String getArtifactTargetlName(String fileNameMapping,
            Artifact artifact) throws InterpolationException
    {
        if (fileNameMapping != null)
        {
            return MappingUtils.evaluateFileNameMapping(fileNameMapping,
                    artifact);
        }
        else
        {
            return MappingUtils.evaluateFileNameMapping(
                    DEFAULT_FILE_NAME_MAPPING, artifact);
        }
    }
    
    
    /**
     * Returns the set of files to copy from the <tt>basedir</tt> taking into
     * account the specified <tt>includes</tt> and <tt>excludes</tt>. If the
     * includes are <tt>null</tt> or empty, the default includes are used.
     * 
     * @param baseDir
     * the base directory to start from
     * @param includes
     * the includes
     * @param excludes
     * the excludes
     * 
     * @return the files to copy
     */
    protected PathSet getFileSet(File baseDir, String[] includes, String[] excludes)
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(baseDir);

        if (excludes != null)
        {
            scanner.setExcludes( excludes );
        }
        
        scanner.addDefaultExcludes();

        if (includes != null && includes.length > 0)
        {
            scanner.setIncludes(includes);
        }
        else
        {
            scanner.setIncludes(DEFAULT_INCLUDES);
        }

        scanner.scan();

        return new PathSet( scanner.getIncludedFiles() );
    }
    
    
    /**
     * Copies the files with an optional target prefix. If the structure of the
     * source directory is not the same as the root of the webapp, use the
     * <tt>targetPrefix</tt> parameter to specify in which particular directory
     * the files should be copied. Use <tt>null</tt> to copy the files with the
     * same structure.
     * 
     * @param sourceBaseDir
     * the base directory from which the <tt>sourceFilesSet</tt> will be copied
     * @param sourceFilesSet
     * the files to be copied
     * @param targetPrefix
     * the prefix to add to the target file name
     * 
     * @throws IOException
     * if an error occurred while copying the files
     */
    @SuppressWarnings("unchecked")
    protected void copyFiles(File sourceBaseDir, PathSet sourceFilesSet,
            File destinationDir, String targetPrefix) throws IOException 
    {
        Iterator iter = sourceFilesSet.iterator();

        while (iter.hasNext())
        {
            final String fileToCopyName = (String) iter.next();
            final File sourceFile = new File(sourceBaseDir, fileToCopyName);

            String destinationFileName;

            if (targetPrefix == null)
            {
                destinationFileName = fileToCopyName;
            }
            else
            {
                destinationFileName = targetPrefix + fileToCopyName;
            }

            final File destinationFile = new File(destinationDir, destinationFileName);
            
            this.copyFile(sourceFile, destinationFile);
        }
    }
    
    
    /**
     * Copy the specified file. The <tt>targetFileName</tt> is the relative path
     * according to the root of the generated artifact.
     * 
     * @param source
     * the file to copy
     * @param destination
     * the relative path according to the root of the webapp
     * 
     * @throws IOException
     * if an error occurred while copying
     */
    protected void copyFile(final File source, final File destination)
            throws IOException            
    {
                
        if (destination.lastModified() < source.lastModified())
        {
            FileUtils.copyFile(source.getCanonicalFile(), destination);

            // Preserve timestamp
            destination.setLastModified( source.lastModified() );    
        }        
    }
       
}

package com.hp.it.spf.tools.maven.car.packaging;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.interpolation.InterpolationException;


/**
 * Packaging task responsible for gathering any dependent artifacts that need to
 * be included as part of the final CAR.
 * 
 * <p>
 * The process of gathering dependencies is different depending on whether or
 * not the CAR being assembled is a composite CAR or not. For a
 * <strong>non-composite</strong> CAR (the default), the necessary dependencies
 * will be gathered and copied to the <em>WEB-INF/lib/</em> directory of the
 * CAR. For a composite CAR, the dependent CAR artifacts will all be copied to
 * the root directory of the final CAR. The specific packaging behavior is
 * driven by the
 * {@link com.hp.it.spf.tools.maven.car.packaging.ICarPackagingContext#isComposite()
 * ICarPackagingContext.isComposite()} property.
 * </p>
 * 
 * @since 1.0
 * @author bdehamer
 */
public class DependencyPackagingTask extends AbstractCarPackagingTask
{

    // -------------------------------------------------------------- Constants
    
    
    // CAR-relative path to the lib directory
    private static final String LIB_PATH = "WEB-INF/lib/";
    
    
    // ------------------------------------------------------ Protected Members
    
    
    /**
     * Artifact filter used to identify runtime dependencies.
     */
    protected final ScopeArtifactFilter filter = 
            new ScopeArtifactFilter(Artifact.SCOPE_RUNTIME);
    

    // --------------------------------------------------------- Public Methods
    
    
    /**
     * Copies project dependencies to the CAR staging directory.
     * 
     * @param context
     * the packaging context containing the necessary configuration data
     * 
     * @throws MojoExecutionException
     * in case of any errors during the packaging process
     */
    @SuppressWarnings("unchecked")
    @Override
    public void doPackaging(ICarPackagingContext context)
            throws MojoExecutionException
    {
        Iterator iter = context.getProject().getArtifacts().iterator();

        // Iterate over all project artifacts
        while (iter.hasNext())
        {
            Artifact artifact = (Artifact) iter.next();
     
            try
            {
                this.processArtifact(context, artifact);
            }
            catch (IOException e)
            {
                throw new MojoExecutionException(
                        "Failed to copy file for artifact[" + artifact
                                + "]", e);
            }
            catch (InterpolationException e)
            {
                throw new MojoExecutionException(e.getMessage(), e);
            }         
        }
    }


    // ------------------------------------------------------ Protected Methods
    
    
    /**
     * Processes a specific project dependency. Determines whether or not the
     * specified artifact needs to be included as part of the final CAR and
     * copies it to the appropriate location within the staging directory.
     * 
     * @param context
     * the current packaging context
     * @param artifact
     * the artifact to be processed
     * 
     * @throws IOException
     * when there is an error with the file copy operation
     * @throws InterpolationException
     * when there is an error calculating the target name for the artifact
     */
    protected void processArtifact(ICarPackagingContext context,
            Artifact artifact) throws IOException, InterpolationException
    {
        // Only copy non-optional, runtime artifacts
        if (!artifact.isOptional() && filter.include(artifact))
        {
            String targetFileName = this.getArtifactTargetlName(
                    context.getOutputFileNameMapping(), 
                    artifact);
            
            if ("jar".equals(artifact.getType()) && !context.isComposite())
            {
                File targetFile = new File(context.getWebAppOutputDir(),
                        LIB_PATH + targetFileName);

                this.copyFile(artifact.getFile(), targetFile);
            }
            else if ("car".equals(artifact.getType()) && context.isComposite())
            {
                File targetFile = new File(context.getWebAppOutputDir(),
                        targetFileName);
                
                this.copyFile(artifact.getFile(), targetFile);
            }
            else
            {            
                context.getLog().debug(
                        "Artifact of type[" + artifact.getType()
                                + "] is not supported, ignoring[" + artifact
                                + "]");
            }
        }
    }
       
}

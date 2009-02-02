package com.hp.it.spf.tools.maven.car.packaging;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.interpolation.InterpolationException;


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
     * Determines whether or not the specified artifact needs to be included as
     * part of the final CAR. Any non-optional JARs that marked with the RUNTIME
     * scope are copied to the WEB-INF/lib directory of the CAR.
     * 
     * @param context
     * the current packaging context
     * @param artifact
     * the artifact to be processed
     */
    protected void processArtifact(ICarPackagingContext context,
            Artifact artifact) throws InterpolationException, IOException
    {
        // Only copy non-optional, runtime JARs
        if (!artifact.isOptional() && filter.include(artifact))
        {
            String targetFileName = 
                    this.getArtifactTargetlName(context, artifact);
            
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

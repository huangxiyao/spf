package com.hp.it.spf.tools.maven.car.packaging;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.war.util.PathSet;


/**
 * Packaging task responsible for gathering any web application resources to be
 * included as part of the CAR. The resources will be copied as-is to the CAR
 * staging directory so that they can be included in the final CAR.
 * 
 * <p>
 * This task handles any non-code resources like JSP files and the component
 * descriptor (<em>component.xml</em> file) which simply need to be copied from
 * the source directory to the staging directory for the CAR. The default source
 * directory for the web application resources will be the
 * <em>src/main/webapp/</em> directory of the Maven project.
 * </p>
 * 
 * @since 1.0
 * @author bdehamer
 */
public class WebAppPackagingTask extends AbstractCarPackagingTask
{
             
    // ------------------------------------------------------ Public Methods
    
    
    /**
     * Copies web application resources from the source directory to the
     * CAR staging directory.
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
        // If there's no source directory, there's nothing to do
        if (!context.getWebAppSourceDir().exists())
        {
            context.getLog().debug(
                    "Webapp sources directory does not exist - skipping.");
        }
        else if (!context.getWebAppSourceDir().getAbsolutePath().equals(
                context.getWebAppOutputDir().getPath()))
        {
            context.getLog().info(
                    "Copying webapp resources[" + context.getWebAppSourceDir()
                            + "]");
         
            final PathSet sources = this.getFileSet(context.getWebAppSourceDir(),
                    context.getIncludes(), context.getExcludes());

            try
            {
                this.copyFiles(context.getWebAppSourceDir(), sources,
                        context.getWebAppOutputDir(), null);
            }
            catch (IOException e)
            {
                throw new MojoExecutionException(
                        "Could not copy webapp sources["
                                + context.getWebAppOutputDir().getAbsolutePath()
                                + "]", e);
            }
        }        
    }

}

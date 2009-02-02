package com.hp.it.spf.tools.maven.car.packaging;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.war.util.PathSet;


public class WebAppPackagingTask extends AbstractCarPackagingTask
{
        
     
    // ------------------------------------------------------ Public Methods
    
    
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

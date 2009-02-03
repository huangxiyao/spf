package com.hp.it.spf.tools.maven.car.packaging;

import org.apache.maven.plugin.MojoExecutionException;


/**
 * Common interface for all packaging tasks used as part of the
 * {@link com.hp.it.spf.tools.maven.car.CarMojo CarMojo}. The process of
 * assembling a complete component archive (CAR) requires a number of distinct
 * packaging tasks like copying class files, copying dependencies, etc.
 * <code>ICarPackaingTask</code> serves as the common interface for all of the
 * concrete packaging implementation classes that may be invoked as part of the
 * process of assembling the final CAR.
 * 
 * @author bdehamer
 * @since 1.0
 */
public interface ICarPackagingTask
{

    /**
     * Process the packaging request. This will almost always result in one or
     * more files being copied to the target/ directory of the Maven project.
     * There is nothing to return.
     * 
     * @param context
     * the packaging context containing the necessary configuration data
     * 
     * @throws MojoExecutionException
     * in case of any errors during the packaging process
     */
    public void doPackaging(ICarPackagingContext context) throws MojoExecutionException;

}
package com.hp.it.spf.tools.maven.car.packaging;

import org.apache.maven.plugin.MojoExecutionException;


public interface ICarPackagingTask
{

    public void doPackaging(ICarPackagingContext context) throws MojoExecutionException;

}
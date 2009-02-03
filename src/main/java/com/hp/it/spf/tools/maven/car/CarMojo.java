package com.hp.it.spf.tools.maven.car;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.ManifestException;
import org.codehaus.plexus.archiver.war.WarArchiver;
import org.codehaus.plexus.util.StringUtils;

import com.hp.it.spf.tools.maven.car.packaging.ClassesPackagingTask;
import com.hp.it.spf.tools.maven.car.packaging.DependencyPackagingTask;
import com.hp.it.spf.tools.maven.car.packaging.ICarPackagingContext;
import com.hp.it.spf.tools.maven.car.packaging.ICarPackagingTask;
import com.hp.it.spf.tools.maven.car.packaging.WebAppPackagingTask;


/**
 * A Maven plugin for the <em>package</em> life-cycle phase which will assemble
 * the necessary source files and generate a Vignette Component Archive (CAR).
 * 
 * <p>
 * This plugin performs two main tasks:
 * </p>
 * 
 * <ol>
 * <li>Assemble CAR resources</li>
 * <li>Archive CAR resources</li>
 * </ol>
 * 
 * <p>When assembling the CAR resources, there are three activities which are performed:</p>
 * 
 * <ol>
 * <li>Web application resources (JSPs, XML files, etc.) are copied to the staging directory</li>
 * <li>Java class files are copied to the staging directory (either as loose .class files copied 
 * to the <em>WEB-INF/classes/</em> directory or as a JAR file copied to the <em>WEB-INF/lib/</em>
 * directory)
 * <li>Project dependencies are copied to staging directory</li>
 * </ol>
 * 
 * <p>Once all of the necessary files have been staged, the resulting directory structure
 * is archived into a file with a <em>.car</em> extension.
 * 
 * @since 1.0
 * @author bdehamer
 * 
 * @goal car
 * @phase package
 */
public class CarMojo extends AbstractMojo
{

    // -------------------------------------------------------------- Constants
    
    
    // Empty string array used for List.toArray() call
    private static final String[] EMPTY_STRING_ARRAY = {};
    
            
    // ----------------------------------------------- Required Mojo Properties
    
    
    /**
     * The name of the generated CAR.
     *
     * @parameter expression="${project.build.finalName}"
     * @required
     */    
    private String carName;
    
    
    /**
     * The directory containing generated classes.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     * @readonly
     */
    private File classesDirectory;
    
    
    /**
     * The Jar archiver needed for archiving classes directory into jar file
     * under WEB-INF/lib.
     * 
     * @component role="org.codehaus.plexus.archiver.Archiver" role-hint="jar"
     * @required
     */
    private JarArchiver jarArchiver;

    
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    
    /**
     * The WAR archiver used to assemble the final artifact.
     * 
     * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#war}"
     * @required
     */
    private WarArchiver warArchiver;

    
    /**
     * Single directory for extra files to include in the CAR.
     * 
     * @parameter expression="${basedir}/src/main/webapp"
     * @required
     */
    private File warSourceDirectory;

    
       
    /**
     * The directory where the webapp is built.
     *
     * @parameter expression="${project.build.directory}/${project.build.finalName}"
     * @required
     */
    private File webappDirectory;
    
    
    // ----------------------------------------------- Optional Mojo Properties
          
    
    /**
     * The archive configuration to use.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
    
    
    /**
     * Whether a JAR file will be created for the classes in the webapp. Using
     * this optional configuration parameter will make the generated classes to
     * be archived into a jar file and the classes directory will then be
     * excluded from the webapp.
     * 
     * @parameter expression="${archiveClasses}" default-value="false"
     */
    private boolean archiveClasses;
    
    
    /**
     * Whether this CAR is intended to be a composite CAR containing only other
     * CAR files.
     * 
     * @parameter expression="${composite}" default-value="false"
     */
    private boolean composite;    


    /**
     * The file name mapping to use to copy libraries . If no file mapping is
     * set (default) the file is copied with its standard name.
     * 
     * @parameter
     */
    private String outputFileNameMapping;
    
    
    /**
     * The comma separated list of tokens to exclude when copying content
     * of the warSourceDirectory.
     *
     * @parameter alias="excludes"
     */
    private String warSourceExcludes;   
    
    
    /**
     * The comma separated list of tokens to include when copying content
     * of the warSourceDirectory. Default is '**'.
     *
     * @parameter alias="includes"
     */
    private String warSourceIncludes = "**";
        
    
   // ---------------------------------------------------------- Public Methods
   
      
    /**
     * Stages all of the necessary resources and archives them into a
     * <em>.car</em> file.
     */
    public void execute() 
    {
        if (this.project.getResources().size() < 1)
        {
            this.getLog().error("No resources to package");
        }
        else
        {
            try
            {
                this.doPackaging();
                this.generateCar();
            }
            catch (Exception e)
            {
                this.getLog().error("Error archiving resources", e);
            }
        }
    }


    // ------------------------------------------------------ Protected Methods
    
                             
    /**
     * Returns the list of packaging tasks necessary to completely assemble the
     * files that will archived in the CAR.
     * 
     * @return an ordered list of packaging tasks to be carried out.
     */
    protected List<ICarPackagingTask> getPackagingTasks()
    {
        List<ICarPackagingTask> tasks = new ArrayList<ICarPackagingTask>();
        
        // Only need to do the webapp and classes packaging steps if this is NOT
        // a composite CAR.
        if (!this.composite)
        {
            tasks.add(new WebAppPackagingTask());
            tasks.add(new ClassesPackagingTask());
        }
        
        tasks.add(new DependencyPackagingTask());
        
        return tasks;
    }
    
    
    /**
     * Creates the packaging context containg all the environmental data needed
     * by the packaging tasks.
     * 
     * @return the packaging context.
     */
    protected ICarPackagingContext getPackagingContext()
    {
        return new ICarPackagingContext() {

            public MavenArchiveConfiguration getArchiveConfiguration()
            {
                return CarMojo.this.archive;
            }

            public File getWebAppOutputDir()
            {
                return CarMojo.this.webappDirectory;
            }

            public File getWebAppSourceDir()
            {
                return CarMojo.this.warSourceDirectory;
            }

            public File getClassesOutputDir()
            {
                return CarMojo.this.classesDirectory;
            }

            public String[] getExcludes()
            {
                return CarMojo.this.getExcludes();
            }

            public String[] getIncludes()
            {
                return CarMojo.this.getIncludes();
            }

            public JarArchiver getJarArchiver()
            {
                return CarMojo.this.jarArchiver;
            }

            public Log getLog()
            {
                return CarMojo.this.getLog();
            }

            public MavenProject getProject()
            {
                return CarMojo.this.project;
            }

            public boolean isClassArchivingEnabled()
            {
                return CarMojo.this.archiveClasses;
            }

            public String getOutputFileNameMapping()
            {
                return CarMojo.this.outputFileNameMapping;
            }

            public boolean isComposite()
            {
                return CarMojo.this.composite;
            }            
        };
    }
    
    
    // -------------------------------------------------------- Private Methods
        

    /**
     * Executes the various packaging tasks required to assemble the files for
     * the car.
     */
    private void doPackaging() throws MojoExecutionException
    {
        this.webappDirectory.mkdirs();

        this.getLog().info("Assembling car resources");

        ICarPackagingContext context = this.getPackagingContext();
        List<ICarPackagingTask> tasks = this.getPackagingTasks();

        for (ICarPackagingTask task : tasks)
        {
            task.doPackaging(context);
        }
    }
    
    
    /**
     * Archives all resources into a .car file.
     * 
     * @throws ArchiverException
     * @throws ManifestException
     * @throws IOException
     * @throws DependencyResolutionRequiredException
     */
    private void generateCar() throws ArchiverException, ManifestException,
            IOException, DependencyResolutionRequiredException
    {
        this.getLog().info("Generating car");

        File carFile = new File(this.project.getBuild().getDirectory(),
                this.carName + ".car");

        this.warArchiver.addDirectory(this.webappDirectory);
        warArchiver.setIgnoreWebxml(false);

        MavenArchiver archiver = new MavenArchiver();
        archiver.setArchiver(this.warArchiver);
        archiver.setOutputFile(carFile);
        archiver.createArchive(this.project, this.archive);

        this.project.getArtifact().setFile(carFile);
    }

    
    /**
     * Returns a string array of the excludes to be used when assembling the
     * car.
     * 
     * @return an array of tokens to exclude
     */
    @SuppressWarnings("unchecked")
    private String[] getExcludes()
    {
        List excludeList = new ArrayList();

        if (StringUtils.isNotEmpty(this.warSourceExcludes))
        {
            excludeList.addAll(Arrays.asList(StringUtils.split(
                    this.warSourceExcludes, ",")));
        }

        return (String[]) excludeList.toArray(EMPTY_STRING_ARRAY);
    }

    
   /**
     * Returns a string array of the includes to be used when assembling the
     * car.
     * 
     * @return an array of tokens to include
     */
    private String[] getIncludes()
    {
        return StringUtils.split(
                StringUtils.defaultString(this.warSourceIncludes), ",");
    }  
          
}
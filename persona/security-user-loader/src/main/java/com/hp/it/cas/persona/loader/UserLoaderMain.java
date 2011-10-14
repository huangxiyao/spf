package com.hp.it.cas.persona.loader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.LogManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ClassUtils;

import com.hp.it.cas.xa.logging.StopWatch;

/**
 * The command line interface for the Persona user profile bulk data loader. This class parses the command line
 * options and invokes the appropriate data loader.
 *
 * @author Quintin May
 */
public class UserLoaderMain {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserLoaderMain.class);
	
	private static final char OPTION_PROPERTIES	= 'p';
	private static final char OPTION_TEMPLATE	= 't';
	private static final char OPTION_VALIDATE	= 'v';
	private static final char OPTION_FORMAT		= 'f';
	private static final char OPTION_HELP		= 'h';
	
	private static final EFileFormat DEFAULT_FILE_FORMAT = EFileFormat.XML;
	
	private static final int EXIT_OK	= 0;
	private static final int EXIT_ERROR	= 1;
	
	/**
	 * Defines the command line switches and options that are understood by the loader.
	 * @return the option definitions.
	 */
	@SuppressWarnings("static-access")
	private static Options createOptions() {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("properties").withDescription("use properties file for settings")
				.hasArg(true).withArgName("propertiesFile").create(OPTION_PROPERTIES));
		options.addOption(OptionBuilder.withLongOpt("template").withDescription("create properties file template")
				.hasArg(true).withArgName("propertiesFile").create(OPTION_TEMPLATE));
		options.addOption(OptionBuilder.withLongOpt("validate").withDescription("validate input data only, don't load")
				.hasArg(false).create(OPTION_VALIDATE));
//		options.addOption(OptionBuilder.withLongOpt("format").withDescription(
//				String.format("input file format, valid values are %s (default is %s)",
//						Arrays.asList(EFileFormat.values()), DEFAULT_FILE_FORMAT))
//				.hasArg(true).withArgName("format").create(FORMAT_OPTION));
		options.addOption(OptionBuilder.withLongOpt("help").withDescription("print this message")
				.hasArg(false).create(OPTION_HELP));
		return options;
	}

	/**
	 * Displays help text for the usage of the command line options.
	 * @param options the valid options.
	 */
	private static void showHelp(Options options) {
		new HelpFormatter()
				.printHelp(
						String.format("%s [options] [userDataFiles...]", UserLoaderMain.class.getSimpleName()),
						"Load user profile information from files specified on the command-line or from standard input if no files are specified.",
						options, null);
	}

	/**
	 * Writes a template file containing placeholders for properties that must be defined in order to load data.
	 * @param templateFile the path of the file in which to write the template data.
	 */
	private static void createTemplate(String templateFile) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(templateFile));
			writer.println("dataSourceUrl = jdbc:oracle:thin:@HOST_NAME:1521:DATABASE_NAME\n"
					+ "dataSourceUserName = \n"
					+ "dataSourcePassword = \n"
					+ "applicationPortfolioIdentifier = \n"
					+ "applicationPassword = ");
		} catch (IOException e) {
			System.err.format("Unable to create template file '%s'. %s", templateFile, e.getMessage());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * Returns the class that can load the input files based. The class is determined by command line options or defaulted.
	 * @param format the command line specified format.
	 * @return the data loader class.
	 * @throws ParseException if a format was specified but is not supported.
	 */
	private static Class<? extends IUserLoader> determineLoaderClass(String format) throws ParseException {
		Class<? extends IUserLoader> loaderClass = null;
		if (format == null) {
			loaderClass = DEFAULT_FILE_FORMAT.getLoaderClass();
		} else {
			try
			{
				loaderClass = EFileFormat.valueOf(format.toUpperCase()).getLoaderClass();
			} catch (IllegalArgumentException e) {
				throw new ParseException(String.format("'%s' is not a recognized file format. Valid values are %s.",
						format, Arrays.asList(EFileFormat.values())));
			}
		}
		return loaderClass;
	}
	
	/**
	 * Prepares the Spring application context that defines the wiring of the data loader.
	 * @param commandLine the parsed command line from which options to configure the application context are retrieved.
	 * @return the fully configured Spring application context.
	 * @throws ParseException if an invalid command line value is encountered.
	 */
	private static AbstractApplicationContext createApplicationContext(CommandLine commandLine) throws ParseException {
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		Properties internalProperties = new Properties();
		configurer.setProperties(internalProperties);

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		context.addBeanFactoryPostProcessor(configurer);
		
		// set the loader implementation class
		Class<? extends IUserLoader> loaderClass = determineLoaderClass(commandLine.getOptionValue(OPTION_FORMAT));
		internalProperties.setProperty("userLoaderClass", loaderClass.getName());
		
		// location of the application context XML configuration file
		String contextLocation = ClassUtils.convertClassNameToResourcePath(UserLoaderMain.class.getName()) + ".xml";
		context.setConfigLocation(contextLocation);

		// location of the properties that will be substituted into the application context
		String propertiesFile = commandLine.getOptionValue(OPTION_PROPERTIES);
		if (propertiesFile == null) {
			LOGGER.info("CONFIG No configuration properties file specified. Using system properties.");
		} else {
			LOGGER.info("CONFIG Reading configuration properties from {}.", propertiesFile);
			configurer.setLocation(new FileSystemResource(propertiesFile));
		}
		
		// load the context
		context.refresh();
		return context;
	}

	/**
	 * Define the logging configuration based on a predefined logging.properties file available in the JAR.
	 */
	private static void configureLogging() {
		InputStream loggingProperties = null;
		try {
			String loggingConfiguration = ClassUtils.classPackageAsResourcePath(UserLoaderMain.class) + "/logging.properties";
			loggingProperties = UserLoaderMain.class.getClassLoader().getResourceAsStream(loggingConfiguration);
			LogManager.getLogManager().readConfiguration(loggingProperties);
		} catch (Exception e) {
			System.err.format("Unable to initialize logging system: %s.", e.getMessage());
		} finally {
			if (loggingProperties != null) {
				try {
					loggingProperties.close();
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * Validate the input files. This method is selected if the OPTION_VALIDATE flag is specified.
	 * @param userLoader the loader that will perform the validation.
	 * @param files the files to process.
	 */
	private static void performValidation(IUserLoader userLoader, String...files) {
		StopWatch sw = new StopWatch().start();
		userLoader.validate(files);
		LOGGER.info(String.format("Validated %,d attributes for %,d users in %s.", userLoader
				.getAttributeLoadCount(), userLoader.getUserLoadCount(), sw));
	}
	
	/**
	 * Loads the input files. This method is selected if the OPTION_VALIDATE flag is not specified.
	 * @param userLoader the loader that will perform the load.
	 * @param files the files to process.
	 */
	private static void performLoad(IUserLoader userLoader, String...files) {
		StopWatch sw = new StopWatch().start();
		userLoader.load(files);
		LOGGER.info(String.format("Loaded %,d attributes for %,d users in %s.", userLoader
				.getAttributeLoadCount(), userLoader.getUserLoadCount(), sw));
	}
	
	/**
	 * Parse the command line and route to the proper handling code based on command line options.
	 * @param args the command line.
	 */
	public static void main(String[] args) {
		configureLogging();
		AbstractApplicationContext context = null;
		boolean error = false;
		
		try {
			Options options = createOptions();
			CommandLine commandLine = new PosixParser().parse(options, args);

			if (commandLine.hasOption(OPTION_HELP)) {
				showHelp(options);
			} else if (commandLine.hasOption(OPTION_TEMPLATE)) {
				createTemplate(commandLine.getOptionValue("t"));
			} else {
				context = createApplicationContext(commandLine);
				IUserLoader userLoader = (IUserLoader) context.getBean("userLoader");

				if (commandLine.hasOption(OPTION_VALIDATE)) {
					performValidation(userLoader, commandLine.getArgs());
				} else {
					performLoad(userLoader, commandLine.getArgs());
				}
			}
		} catch (ParseException e) {
			System.err.println("Error parsing command line: " + e.getMessage());
			error = true;
		} catch (Exception e) {
			LOGGER.error("No user data loaded.", e);
			error = true;
		} finally {
			if (context != null) {
				context.close();
			}
		}
		
		System.exit(error ? EXIT_ERROR : EXIT_OK);
	}
}

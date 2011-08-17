package com.hp.spp.portal.common.perf;

import java.io.File;

import org.apache.log4j.Logger;


/**
 * 
 * @author NBLACHIE
 * 
 */
public class PerfToolLogger {
	
	private static Logger mLog = Logger.getLogger(PerfToolLogger.class);

	
	private static Logger logSPPPerfTool = Logger.getLogger(PerfToolLogger.class);

	public static String fileSeparator = (String) System.getProperty("file.separator");

	private static final String LOG_FOLDER = fileSeparator.concat("SPP_PerfToolLog");

	private boolean noErrorInitialization = true;

	/**
	 * Constructor : configure the logger and , create the log file path
	 * ('com.hp.sesame.sesamelogConsole').
	 * 
	 */
	public PerfToolLogger() {
		try {
			mLog.debug("Start of Initialization of PerfToolLogger");
			// Get the server ID
			String serverID = System.getProperties().getProperty("server.id");
			boolean ifClusterUse = serverID != null && serverID.length() > 0;
			if (!ifClusterUse) {
				serverID = System.getProperties().getProperty("vignette.server.name");
				if (serverID == null)
					serverID = System.getProperties().getProperty("epicentric.server.name");
				ifClusterUse = serverID != null && serverID.length() > 0;
			}

			// build the sesame log path
			String PERFTOOL_LOG_PATH = System.getProperty("log_dir").concat(fileSeparator).concat(serverID).concat(LOG_FOLDER);

			// Verify if the path of log's file exist
			File mkdirFile = new File(PERFTOOL_LOG_PATH);
			mLog.debug("log to directoty : "+PERFTOOL_LOG_PATH);

			boolean exists = mkdirFile.exists();
			if (!exists) {
				// Build Folder if need
				mkdirFile.mkdirs();
			}

			PerfToolFileAppender appender = new PerfToolFileAppender(PERFTOOL_LOG_PATH);
			appender.activateOptions();
			
			
			
		//	logSPPPerfTool.setLevel(Level.DEBUG);
			logSPPPerfTool.addAppender(appender);
			logSPPPerfTool.setAdditivity(false);

			mLog.debug("End of Initialization of PerfToolLogger");

		} catch (Exception e) {
			mLog.error("Error in initialization of the logger"+e);
			noErrorInitialization = false;
		}

	}

	/**
	 * Logs a message at fatal level .
	 * 
	 * @param str
	 *            log message
	 */
	public void fatal(String str) {
		if (noErrorInitialization)
			logSPPPerfTool.fatal(str);
	}

	/**
	 * Logs a message at error level .
	 * 
	 * @param str
	 *            log message
	 */
	public void error(String str) {
		if (noErrorInitialization)
			logSPPPerfTool.error(str);
	}

	/**
	 * Logs a message at warning level .
	 * 
	 * @param str
	 *            log message
	 */
	public void warning(String str) {
		if (noErrorInitialization)
			logSPPPerfTool.warn(str);
	}

	/**
	 * Logs a message at debug level .
	 * 
	 * @param str
	 *            log message
	 */
	public void debug(String str) {
		if (noErrorInitialization)
			logSPPPerfTool.debug(str);
	}

	/**
	 * Logs a message at info level .
	 * 
	 * @param str
	 *            log message
	 */
	public void info(String str) {
		if (noErrorInitialization)
			logSPPPerfTool.info(str);
	}

}
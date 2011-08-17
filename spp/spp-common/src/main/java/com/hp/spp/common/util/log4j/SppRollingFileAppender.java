package com.hp.spp.common.util.log4j;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;

import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hp.spp.common.util.Environment;

/**
 * Rolling file appender that uses timestamp as name for the log files.
 * Even though this file appender inherits from <tt>org.apache.log4j.RollingFileApender</tt> it does
 * not rely on <<tt>Path</tt>. Instead it uses <tt>LogDirectory</tt>, <tt>LogDomain</tt> and implicit
 * server name.
 * The resulting log file path can have 1 of 2 forms:
 * <ul>
 * <li>[log dir]/[server name]/[log domain]/yyyyMMdd_HHmmssSSS.txt</li>
 * <li>[log dir]/[server name]/[log domain]_yyyyMMdd_HHmmssSSS.txt</li>
 * </ul>
 * Which form is used depends on the value of <tt>LogDomainAsDirectory</tt> flag which by default is
 * <tt>true</tt> resulting in the first path above.
 * [log dir] is either the value of <tt>LogDirectory</tt> property, or of this propery is not specified
 * the appender will attempt to get its value from <tt>log_dir</tt> system property. This system property
 * is for example set by Vignette Portal.
 * [server name] is the name of the application server instance. For WebLogic server this will be
 * the name of the managed server.
 * [log domain] is the name of the log domain to use. It serves either as a subdirectory or as a log
 * file prefix (depending on LogDomainAsDirectory property value).
 */
public class SppRollingFileAppender extends org.apache.log4j.RollingFileAppender {

	private static final String FILE_EXTENSION = ".txt";
	private static final String DATE_FORMAT_PATTERN = "yyyyMMdd_HHmmssSSS";
	private static final SimpleDateFormat BASE_FILE_NAME_FORMANT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

	/**
	 * Root directory for the log files. If not specified, the value of <tt>log_dir</tt> system property
	 * will be used. If this property is not specified it will default to the current directory.
	 */
	private String mLogDirectory;

	/**
	 * Log domain for this appender. In the resulting log file path it will be either a sub-directory
	 * or file prefix. This property is helpful when used in conjunction with <tt>log_dir</tt> system
	 * property. This property is optional.
	 */
	private String mLogDomain;

	/**
	 * Flag indicating whether <tt>LogDomain</tt> must be handled as a directory or as a file prefix.
	 */
	private boolean mLogDomainAsDirectory = true;


	public SppRollingFileAppender() {
	}

	public SppRollingFileAppender(Layout layout, String filename, boolean append) throws IOException {
		super(layout, filename, append);
	}

	public SppRollingFileAppender(Layout layout, String filename) throws IOException {
		super(layout, filename);
	}

	public String getLogDirectory() {
		return mLogDirectory;
	}

	public void setLogDirectory(String logDirectory) {
		mLogDirectory = logDirectory;
	}

	public String getLogDomain() {
		return mLogDomain;
	}

	public void setLogDomain(String logDomain) {
		mLogDomain = logDomain;
	}

	public boolean isLogDomainAsDirectory() {
		return mLogDomainAsDirectory;
	}

	public void setLogDomainAsDirectory(boolean logDomainAsDirectory) {
		mLogDomainAsDirectory = logDomainAsDirectory;
	}


	public void activateOptions() {
		if (mLogDirectory == null && System.getProperty("log_dir") != null) {
			setLogDirectory(System.getProperty("log_dir"));
		}

		setNewFile();
	}

	/**
	 * As the file name is calculated automatically this property is ignored. 
	 */
	public synchronized void setFile(String filename, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
		LogLog.error("'File' property is not supported by this appender. Use 'LogDirectory' and 'LogDomain' instead!");
	}

	private void setNewFile() {
		String filePath = null;
		try {
			filePath = calculateLogFilePath();
			super.setFile(filePath, getAppend(), getBufferedIO(), getBufferSize());
		}
		catch (IOException e) {
			LogLog.error("Exception occured when setting log file path to '" + filePath + "'", e);
		}
	}

	private String calculateLogFilePath() {
		// Generated file path will have the following structure:
		// [log directory]/[server name]/[log domain]/yyyyMMdd_HHmmssSSS.txt or
		// [log directory]/[server name]/[log domain]_yyyyMMdd_HHmmssSSS.txt

		StringBuffer actualFileName = new StringBuffer();

		// log directory
		if (mLogDirectory != null) {
			actualFileName.append(mLogDirectory).append(File.separatorChar);
		}

		// server name
		// The snippet used to determine the server id is a duplication of code from Environment
		// class. It's here to avoid a circular dependency - as Environment also performs some logging
		// during its class initilization, the logger must be initialized before this occurs.
		String serverId = System.getProperty("spp.ServerId");
		if (serverId == null) {
			serverId = System.getProperty("weblogic.Name");
		}
		actualFileName.append(serverId).append(File.separatorChar);

		// log domain
		if (mLogDomain != null) {
			if (mLogDomainAsDirectory) {
				actualFileName.append(mLogDomain).append(File.separatorChar);
			}
			else {
				actualFileName.append(mLogDomain).append('_');
			}
		}

		// file name
		actualFileName.append(BASE_FILE_NAME_FORMANT.format(new Date())).append(FILE_EXTENSION);

		// make sure that all the sub-directories exist
		File file = new File(actualFileName.toString());
		File parentFile = file.getParentFile();
		if (parentFile == null) {
			throw new IllegalStateException("Parent directory for the log file '" + file.getAbsolutePath() + "' is null!");
		}
		parentFile.mkdirs();
		return file.getAbsolutePath();
	}


	public void rollOver() {
		setNewFile();
	}

}

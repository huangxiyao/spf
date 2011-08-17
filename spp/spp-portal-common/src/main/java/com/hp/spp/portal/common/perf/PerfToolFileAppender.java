package com.hp.spp.portal.common.perf ;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.hp.spp.profile.Constants;

/**
 * PerfToolFileAppender
 *
 */
public class PerfToolFileAppender extends RollingFileAppender{
	

	/**
	 * Constructor 
	 * @param path path of the logger File
	 */
	public	PerfToolFileAppender(String path) {
		//set the file path
		this.path = path;
		//set the max backup
		this.setMaxBackupIndex(1);
		//set the fileSize
		this.setMaximumFileSize(1000*1024*1024);
        try {
			super.setFile(getFile0().getAbsolutePath(), false, false, 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Set the patern layout
		PatternLayout paternLayout = new PatternLayout();
		paternLayout.setConversionPattern("%d | %X{"+Constants.MAP_SITE+"} | %m%n");
        super.setLayout(paternLayout);
		
	}

	/**
	 * 
	 * @return get current log file path
	 */
	public String getFile()
	{
		return currentLogFile.getAbsolutePath();
	}
	
	/**
	 * 
	 * @return get current log file
	 */
	private File getFile0()
	{
		
		File logDir = new File(this.path);
		logDir.mkdirs();
		StringBuffer fileName = new StringBuffer(30);
		fileName.append(timestampFormat.format(new Date()));
		fileName.append(".txt");
		currentLogFile = new File(logDir, fileName.toString());
		return currentLogFile;
	}
	
	/**
	 * rollOver
	 */
	public void rollOver()
	{
		try
		{
			super.setFile(getFile0().getAbsolutePath(), false, bufferedIO, bufferSize);
		}
		catch(IOException ioExc)
		{
			ioExc.printStackTrace(System.out);
		}
	}
	
	/**
	 * setFile
	 */
	public synchronized void setFile(String s, boolean flag, boolean flag1, int i)
	throws IOException
	{
	}
	
	protected String name = "SimulationFileAppender";
	private static DateFormat timestampFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
	private File currentLogFile;
	private String path;
}

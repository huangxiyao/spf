package com.hp.spp.webservice.monitoring;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class MonitoringStatistics {
	
	private static final Logger mLog = Logger.getLogger(MonitoringStatistics.class);

	private static void log(String details,String statistictype) {
		
		// MonitoringStatistics must be always enabled. Change the level appropriately if this is
		// not the case.
		if (!mLog.isInfoEnabled()) {
			mLog.setLevel(Level.INFO);
		}

		mLog.info("<" + statistictype + ">" + details);
	}
	
	public static void logJVMDetails(String details){
		log(details, "JVM");
	}
	
	public static void logDataSourceDetails(String details){
		log(details, "DataSource");
	}
	
	public static void logThreadDetails(String details){
		log(details, "Thread");
	}

}

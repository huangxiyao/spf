package com.hp.spp.portal.common.perf;

public class PerfToolLogManager {

	
	private static PerfToolLogger logger = new PerfToolLogger();
	
	public static void traceUgsCall(long responseTime){
		logger.info("UGS CALL | "+Long.toString(responseTime));
	}
	
	public static void traceEserviceCall(long responseTime){
		logger.info("Eservice CALL | "+Long.toString(responseTime));
	}
	
	public static void traceUPSCall( long responseTime){
		logger.info("UPS CALL | "+Long.toString(responseTime));
	}
	

}

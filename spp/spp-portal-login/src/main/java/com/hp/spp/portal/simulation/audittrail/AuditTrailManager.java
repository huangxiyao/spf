package com.hp.spp.portal.simulation.audittrail;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;


/**
 * @author ageymond
 * Class that manage Audit trail for simulation by logging in a dedicated file
 */
public class AuditTrailManager {

	private static final Logger mLog = Logger.getLogger(AuditTrailManager.class);


	private static void log(String simulatedUserHppId, String simulatingUserHppId, String message) {
		// Simulation audit trail must be always enabled. Change the level appropriately if this is
		// not the case.
		if (!mLog.isInfoEnabled()) {
			mLog.setLevel(Level.INFO);
		}

		mLog.info(message + " : user[" + simulatingUserHppId + "] simulates user[" + simulatedUserHppId + "]");
	}

	/**
	 * This method is used when simulation starts
	 * @param simulatedHPPId
	 * @param simulatorHPPId
	 */
	public static void startSimulation(String simulatedHPPId, String simulatorHPPId){
		log(simulatedHPPId, simulatorHPPId, "START Simulation");
	}

	/**
	 * This method is used when simulation end
	 * @param simulatedHPPId
	 * @param simulatorHPPId
	 */
	public static void stopSimulation(String simulatedHPPId, String simulatorHPPId){
		log(simulatedHPPId, simulatorHPPId, "STOP Simulation");
	}

	/**
	 * This method is used when simulation end
	 * @param simulatedHPPId
	 * @param simulatorHPPId
	 */
	public static void stopSimulationByLogout(String simulatedHPPId, String simulatorHPPId){
		log(simulatedHPPId, simulatorHPPId, "STOP Simulation by LOGOUT");
	}

	/**
	 * This method is used when simulation end
	 * @param simulatedHPPId
	 * @param simulatorHPPId
	 */
	public static void keepSimulationForNextLogin(String simulatedHPPId, String simulatorHPPId){
		log(simulatedHPPId, simulatorHPPId, "KEEP Simulation during LOGOUT or the next LOGIN");
	}

	/**
	 * This method is used when session expires during a simulation session
	 * @param sessionId
	 * @param simulatedHPPId
	 * @param simulatorHPPId
	 */
	public static void sessionExpiration(String sessionId, String simulatedHPPId, String simulatorHPPId){
		log(simulatedHPPId, simulatorHPPId, "Session ["+sessionId+"] expires");
	}

	/**
	 * This method is used when simulation restarts from login
	 * @param simulatedHPPId
	 * @param simulatorHPPId
	 */
	public static void startSimulationFromLogin( String simulatedHPPId, String simulatorHPPId){
		log(simulatedHPPId, simulatorHPPId, "RESTART Simulation from login");
	}

}

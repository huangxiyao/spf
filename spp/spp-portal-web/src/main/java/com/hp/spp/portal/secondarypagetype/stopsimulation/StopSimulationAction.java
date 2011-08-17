package com.hp.spp.portal.secondarypagetype.stopsimulation;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.hp.spp.portal.simulation.StopSimulationManager;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.perf.Operation;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;

/**
 * Main Class of the Secondary Page template.STOPTSIMULATION.
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class StopSimulationAction extends BaseAction {

	/**
	 * Logger.
	 */
	private static Logger mLog = Logger.getLogger(StopSimulationAction.class);

	public PortalURI execute(PortalContext pc) throws ActionException {
		if (mLog.isDebugEnabled())
			mLog.debug("StopSimulationAction begin");

		// Call of main business class
		String redirection = null;
		TimeRecorder.getThreadInstance().recordStart(Operation.STOP_SIMULATION);
		try {
			redirection = (new StopSimulationManager(pc)).unsimulate();
			TimeRecorder.getThreadInstance().recordEnd(Operation.STOP_SIMULATION);
		}
		catch (RuntimeException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.STOP_SIMULATION, e);
			throw e;
		}

		try {
			if (mLog.isDebugEnabled())
				mLog.debug("redirection to : [" + redirection + "]");
			pc.getPortalResponse().getResponse().sendRedirect(redirection);
		} catch (IOException e) {
			String error = "error during redirect : " + e.getMessage();
			mLog.error(error);
			throw new IllegalStateException(error);
		}

		// return to home page
		return pc.getPortalURI();
	}

}

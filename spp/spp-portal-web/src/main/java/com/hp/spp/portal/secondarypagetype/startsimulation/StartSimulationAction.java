package com.hp.spp.portal.secondarypagetype.startsimulation;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.hp.spp.portal.simulation.StartSimulationManager;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.perf.Operation;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;

/**
 * Main Class of the Secondary Page template.STARTSIMULATION.
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class StartSimulationAction extends BaseAction {

	/**
	 * Logger.
	 */
	private static Logger mLog = Logger.getLogger(StartSimulationAction.class);

	public PortalURI execute(PortalContext pc) throws ActionException {
		if (mLog.isDebugEnabled())
			mLog.debug("StartSimulationAction begin");

		String redirection = null;
		TimeRecorder.getThreadInstance().recordStart(Operation.START_SIMULATION);
		try {
			// Call of main business class
			redirection = (new StartSimulationManager(pc)).simulate();
			TimeRecorder.getThreadInstance().recordEnd(Operation.START_SIMULATION);
		}
		catch (RuntimeException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.START_SIMULATION, e);
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

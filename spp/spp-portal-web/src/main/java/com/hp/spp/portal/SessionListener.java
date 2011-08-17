package com.hp.spp.portal;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.hp.spp.portal.common.helper.ProfileHelper;
import com.hp.spp.portal.common.util.CheckSession;
import com.hp.spp.portal.login.dao.LoginDAO;
import com.hp.spp.portal.login.dao.LoginDAOCacheImpl;
import com.hp.spp.portal.simulation.SimulationDAO;
import com.hp.spp.portal.simulation.SimulationDAOSQLManagerImpl;
import com.hp.spp.portal.simulation.audittrail.AuditTrailManager;
import com.hp.spp.profile.Constants;

/**
 * Cleaning when session is destroyed.
 * @author mvidal@capgemini.fr
 *
 */
public class SessionListener implements HttpSessionListener {

	/**
	 * Logger.
	 */
	private static Logger mLog = Logger.getLogger(SessionListener.class);

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		if (mLog.isDebugEnabled()) {
			mLog.debug("SPPSession created with id " + httpSessionEvent.getSession().getId());
		}
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		try {
			HttpSession session = httpSessionEvent.getSession();

			CheckSession.removeRowToCheckSession(httpSessionEvent.getSession().getId());

			// Manage simulation
			ProfileHelper profileHelper = new ProfileHelper();
			if (profileHelper.isSimulationMode(session)) {
				// audit trail
				Map simulatorProfile = profileHelper.getSimulatorProfile(session);
				Map simulatedProfile = profileHelper.getProfile(session);
				String hppIdSimulated = (String) simulatedProfile.get(Constants.MAP_HPPID);
				String hppIdSimulator = (String) simulatorProfile.get(Constants.MAP_HPPID);
				AuditTrailManager.sessionExpiration(session.getId(), hppIdSimulated,
						hppIdSimulator);

				// persistence
				String site = (String) simulatedProfile.get(Constants.MAP_SITE);
				LoginDAO loginDAO = LoginDAOCacheImpl.getInstance();
				if (!loginDAO.getPersistSimulation(site)) {
					SimulationDAO simulationDAO = SimulationDAOSQLManagerImpl.getInstance();
					simulationDAO.removeSimulation(site, hppIdSimulator, hppIdSimulated);
				}
			}
			mLog.debug("SPPSession destroyed " + session.getId());
		}
		catch (RuntimeException e) {
			mLog.error("Unexpected error occured", e);
			throw e;
		}
	}

}

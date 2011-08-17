package com.hp.spp.portal.simulation;

import org.apache.log4j.Logger;

import com.hp.spp.db.DB;

/**
 * Class to access the persistent data of simulation with SQL orders.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public final class SimulationDAOSQLManagerImpl implements SimulationDAO {

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger
			.getLogger(SimulationDAOSQLManagerImpl.class);

	/**
	 * The instance for the singleton.
	 */
	private static SimulationDAOSQLManagerImpl instance;

	// force the use of singleton
	private SimulationDAOSQLManagerImpl() {
	}

	/**
	 * Return a singleton of the DAO.
	 * 
	 * @return the instance
	 */
	public static SimulationDAOSQLManagerImpl getInstance() {
		if (null == instance) {
			instance = new SimulationDAOSQLManagerImpl();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.simulation.SimulationDAO#persistSimulation(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void persistSimulation(String site, String hppIdSimulator,
			String hppIdSimulated) {
		// we first test that the row does not exist (incoherent state of the
		// database)
		if (isSimulationExisting(site, hppIdSimulator, hppIdSimulated)) {
			mLog.warn("row already existing in SPP_SIMULATION_SESSION for site ["
				+ site + "], simulator [" + hppIdSimulator + "], simulated [" +
				hppIdSimulated + "]. Please finish the simulation session " +
				"by clicking Stop Simulation link.");
			return;
		}

		Object[] args = { site, hppIdSimulator, hppIdSimulated };
		String query = "INSERT INTO SPP_SIMULATION_SESSION "
				+ "(SITE, HPPID_SIMULATOR, HPPID_SIMULATED) VALUES (?, ?, ?)";

		int result = DB.update(query, args);
		if (result != 1) {
			mLog.error("No row inserted in SPP_SIMULATION_SESSION for site ["
					+ site + "], simulator [" + hppIdSimulator
					+ "], simulated [" + hppIdSimulated + "]");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.simulation.SimulationDAO#removeSimulation(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void removeSimulation(String site, String hppIdSimulator,
			String hppIdSimulated) {
		Object[] args = { site, hppIdSimulator, hppIdSimulated };
		String query = "DELETE FROM SPP_SIMULATION_SESSION WHERE SITE = ? "
				+ "AND HPPID_SIMULATOR = ? AND HPPID_SIMULATED = ?";

		int result = DB.update(query, args);
		if (result != 1) {
			mLog.error("No row deleted in SPP_SIMULATION_SESSION for site ["
					+ site + "], simulator [" + hppIdSimulator
					+ "], simulated [" + hppIdSimulated + "]");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.simulation.SimulationDAO#selectSimulated(java.lang.String,
	 *      java.lang.String)
	 */
	public String selectSimulated(String site, String hppIdSimulator) {
		Object[] args = { site, hppIdSimulator };
		String query = "SELECT SITE, HPPID_SIMULATOR, HPPID_SIMULATED FROM SPP_SIMULATION_SESSION "
				+ "WHERE SITE = ? AND HPPID_SIMULATOR = ?";
		Object result = DB.queryForObject(query, new SimulationSessionMapper(),
				args);
		if (result != null && result instanceof SimulationSession) {
			return ((SimulationSession) result).getHppIdSimulated();
		}
		return null;
	}

	/*
	 * Test if an simuation is persisted.
	 */
	private static boolean isSimulationExisting(String site, String hppIdSimulator, String hppIdSimulated) {
		Object[] args = { site, hppIdSimulator, hppIdSimulated };
		String query = "SELECT SITE, HPPID_SIMULATOR, HPPID_SIMULATED FROM SPP_SIMULATION_SESSION "
				+ "WHERE SITE = ? AND HPPID_SIMULATOR = ? AND HPPID_SIMULATED = ?";
		Object result = DB.queryForObject(query, new SimulationSessionMapper(),
				args);
		if (result != null && result instanceof SimulationSession) {
			return true;
		}
		return false;
	}
}

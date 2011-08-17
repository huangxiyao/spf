package com.hp.spp.portal.simulation;

/**
 * Interface which describes the different access methods to the data for the simulation.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public interface SimulationDAO {
	/**
	 * Persist the simulation of an user by an administrator for a given site.
	 * 
	 * @param site the site name
	 * @param hppIdSimulator the HPP id of the administrator
	 * @param hppIdSimulated the HPP id of the user simulated
	 */
	void persistSimulation(String site, String hppIdSimulator, String hppIdSimulated);

	/**
	 * Remove from persistenc the simulation of an user by an administrator for a given site.
	 * 
	 * @param site the site name
	 * @param hppIdSimulator the HPP id of the administrator
	 * @param hppIdSimulated the HPP id of the user simulated
	 */
	void removeSimulation(String site, String hppIdSimulator, String hppIdSimulated);

	/**
	 * Returns the hpp id of the user simulated by an administrator, null if no user.
	 * 
	 * @param site the site name
	 * @param hppIdSimulator the HPP id of the administrator
	 * @return the hpp id of the user simulated, null if no user.
	 */
	String selectSimulated(String site, String hppIdSimulator);
}

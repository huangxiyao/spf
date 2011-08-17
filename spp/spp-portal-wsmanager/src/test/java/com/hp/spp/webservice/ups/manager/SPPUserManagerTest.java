package com.hp.spp.webservice.ups.manager;

import java.util.HashMap;
import java.util.Map;

import com.hp.spp.profile.Constants;
import junit.framework.TestCase;

/**
 * Test Class for SPPSessionManager.
 *  
 * @author mvidal@capgemini.fr
 */
public class SPPUserManagerTest extends TestCase {

	/**
	 * Return null if not ups status in map.
	 * 
	 * @throws Exception
	 */
	public void testUPSStatusNotInUserProfile() throws Exception {
		Map userProfile = new HashMap();
		assertNull((new SPPUserManager()).analyzeUPSStatus(userProfile));
	}

	/**
	 * Return null if ups status null in map.
	 * 
	 * @throws Exception
	 */
	public void testUPSStatusVoid() throws Exception {
		Map userProfile = new HashMap();
		userProfile.put(Constants.MAP_STATUS, null);
		assertNull((new SPPUserManager()).analyzeUPSStatus(userProfile));
	}

	/**
	 * Return null if ups status empty in map.
	 * 
	 * @throws Exception
	 */
	public void testUPSStatusEmpty() throws Exception {
		Map userProfile = new HashMap();
		userProfile.put(Constants.MAP_STATUS, "");
		assertNull((new SPPUserManager()).analyzeUPSStatus(userProfile));
	}

	/**
	 * Return null if ups status is "titi" in map.
	 * 
	 * @throws Exception
	 */
	public void testUPSStatusNotInteger() throws Exception {
		Map userProfile = new HashMap();
		userProfile.put(Constants.MAP_STATUS, "titi");
		assertNull((new SPPUserManager()).analyzeUPSStatus(userProfile));
	}

	/**
	 * Return 2 if ups status is pending in map.
	 * 
	 * @throws Exception
	 */
	public void testUPSStatusPending() throws Exception {
		Map userProfile = new HashMap();
		userProfile.put(Constants.MAP_STATUS, String.valueOf(Constants.STATUS_UPS_PENDING));
		assertEquals(new Integer(2), (new SPPUserManager()).analyzeUPSStatus(userProfile));
	}

	/**
	 * Return 3 if ups status is inactive in map.
	 * 
	 * @throws Exception
	 */
	public void testUPSStatusInactive() throws Exception {
		Map userProfile = new HashMap();
		userProfile.put(Constants.MAP_STATUS, String.valueOf(Constants.STATUS_UPS_INACTIVE));
		assertEquals(new Integer(3), (new SPPUserManager()).analyzeUPSStatus(userProfile));
	}

	/**
	 * Return nul if ups status is active in map.
	 * 
	 * @throws Exception
	 */
	public void testUPSStatusActive() throws Exception {
		Map userProfile = new HashMap();
		userProfile.put(Constants.MAP_STATUS, String.valueOf(Constants.STATUS_UPS_ACTIVE));
		assertNull((new SPPUserManager()).analyzeUPSStatus(userProfile));
	}

	/**
	 * Return nul if ups status has a value not defined in map.
	 * 
	 * @throws Exception
	 */
	public void testUPSStatusNotDefined() throws Exception {
		Map userProfile = new HashMap();
		userProfile.put(Constants.MAP_STATUS, String.valueOf(10));
		assertNull((new SPPUserManager()).analyzeUPSStatus(userProfile));
	}

}

package com.hp.spp.portal.login.business.rule;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/*
 * Base UPS rule class
 * @ author girishsk/Adi
 */

public interface UPSRule {
	

	/**
	 * Execute the rule given parameters.
	 *
	 * @param request HttpServletRequest object
	 * @param isSimulation flag indicating if user is simulating
	 * @return The error code.
	 */

	public Integer execute(HttpServletRequest request, Map userProfile, boolean isSimulation);
}

package com.hp.spp.portal.login.business.rule;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hp.spp.profile.Constants;


/*
 * Rule that handles processing of Disclaimer form logic
 * @ author girishsk/Adi
 */


public class DisclaimerFormRule implements UPSRule{
	/**
	 * Execute the rule given parameters.
	 *
	 * @param request HttpServletRequest object
	 * @param isSimulation flag indicating if user is simulating
	 * @return The error code.
	 */

	public Integer execute(HttpServletRequest request, Map userProfile, boolean isSimulation){
		HttpSession session = request.getSession(true);
		
		if (userProfile != null){
			String mConsentFlag = (String)userProfile.get(Constants.CONSENT_FLAG);
		
			//Consent flag is no
			if("N".equalsIgnoreCase(mConsentFlag)){
				if(isSimulation){
					return new Integer(Constants.SPP_DISCLAIMER_SIMULATION_ERROR_CODE);
				}else{
					//Set the DISCLAIMER_RULE_ERROR flag, this flag should be used
					//to prevent further access to protected pages if user hacks
					//by pasting landing page URL(to be set if not simualting)
					session.setAttribute(Constants.CLEAR_USERPROFILE_FLAG, "true");
			
					return new Integer(Constants.SPP_DISCLAIMER_ERROR_CODE);
				}
			}
		}
		return null;
	}
}


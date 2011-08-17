package com.hp.spp.portal.login.business.rule;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.portal.login.business.preprocess.Localizer;

/**
 * Calls all the rule class instances polymorphically. The execution
 * stops when first rule instance returns a not null(error code) value.
 * @since SPP 4.0
*/

public class UPSRulesProcessor {
	
	/*
	 * Execute  the rules in order and return the first rule
	 * that fails computation
	 * @param request instance of HttpServletRequest
	 * @param userProfile the profile of the user
	 * @param isSimulation flag to indicate if user is simulating
	 * @return error code a non-null value indicates error
	 */
	
	public static Integer execute(HttpServletRequest request, Map userProfile, boolean isSimulation){
		
		List ruleInstanceList = RuleContainer.getInstance().getRules(getSiteIdentifier(request));
		
		if(ruleInstanceList == null || ruleInstanceList.isEmpty()){
			return null;
		}
		
		Iterator ruleListIterator = ruleInstanceList.iterator();
		
		while(ruleListIterator.hasNext()){
			//Return the first failed error code.
			Integer errorCode = ((UPSRule)ruleListIterator.next()).execute(request, userProfile, isSimulation);
			if(errorCode != null){
				return errorCode;
			}
		}
		return null;
	}
	
	private static String getSiteIdentifier(HttpServletRequest request){
		return new Localizer().getSiteIdentifier(SiteURLHelper.determineMasterSite(request));
	}	
}

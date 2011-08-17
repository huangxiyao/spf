package com.hp.spp.portal.secondarypagetype.contactHP;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hp.spp.portal.common.sql.SPPSQLManager;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;

import com.hp.spp.profile.Constants;

public class ContactHPPreDisplayAction extends BaseAction {

	private static Logger mLog = Logger.getLogger(ContactHPPreDisplayAction.class);

	public PortalURI execute(PortalContext pc) throws ActionException {
		PortalURI portalURI = null ;

        HttpServletRequest request = pc.getPortalRequest().getRequest();

        //Get the profile from the Session to get the language code
        HashMap userProfile = (HashMap) request.getSession().getAttribute(Constants.PROFILE_MAP);

		String localeLanguage = "en";
		if (userProfile.get(Constants.MAP_LANGUAGE) != null){
			localeLanguage = (String)userProfile.get(Constants.MAP_LANGUAGE);
		}
		if(mLog.isDebugEnabled()){
			mLog.debug("Language Language"+Constants.MAP_LANGUAGE);
		}
	

// 		TODO Get the good compliant LangCode for HPP
//		SesameWebServiceUtils wsUtils = SesameWebServiceUtils.getInstance() ;
//		String localeLanguage = wsUtils.getLangCode(LocaleParametersManager.getLangCode(pc));

		SPPSQLManager ssm = SPPSQLManager.getInstance() ;
		ArrayList subjectMap = getTopicList(ssm, localeLanguage) ;

		request.setAttribute("subjectMap", subjectMap) ;

		return portalURI;
	}

	/**
	 *
	 * @param ssm
	 * @param langCode
	 * @return
	 */
	public ArrayList getTopicList(SPPSQLManager ssm, String langCode) {
		ArrayList result = null ;

		if(ssm != null && langCode != null) {
			StringBuffer request = new StringBuffer("") ;
			request.append("SELECT topiccode, topiclabel ") ;
			request.append("FROM spp_contact_topics ") ;
			request.append("WHERE UPPER(languagecode) = '") ;
			request.append(langCode.toUpperCase()) ;
			request.append("' ORDER BY topiccode ") ;
			result = ssm.executeSelectQuery(request.toString()) ;
		}

		return result;
	}
}

package com.hp.spp.portal.secondarypagetype.loginpreprocess;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.hp.spp.portal.login.business.preprocess.WorkflowDeterminer;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;

/**
 * Main Class of the Secondary Page template.PRELOGIN.
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class LoginPreprocessAction extends BaseAction {

	/**
	 * Logger.
	 */
	private static Logger mLog = Logger.getLogger(LoginPreprocessAction.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vignette.portal.website.enduser.components.BaseAction#
	 * execute(com.vignette.portal.website.enduser.PortalContext)
	 */
	public PortalURI execute(PortalContext pc) throws ActionException {
		if (mLog.isDebugEnabled())
			mLog.debug("LoginPreprocessAction begin");

		// Call of main business class
		String redirection = (new WorkflowDeterminer()).processErrorWorkflow(pc
				.getPortalRequest().getRequest());
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
		return null;
	}

}

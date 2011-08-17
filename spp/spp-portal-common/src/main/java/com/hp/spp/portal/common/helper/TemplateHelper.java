package com.hp.spp.portal.common.helper;

import com.epicentric.template.Template;
import com.epicentric.template.sqltemplatemanager.SQLTemplateManager;

/**
 * Helper class for using accessing the vignette templates.
 * 
 * @author Cyril MICOUD
 * @version $Revision: 1.2 $ $Date: 2006/10/30 16:48:15 $
 */
public class TemplateHelper {

	// private static Logger mLog = Logger.getLogger(TemplateHelper.class);

	/**
	 * Empty constructor.
	 */
	public TemplateHelper() {
	}

	/**
	 * Finds the template using the friendlyId.
	 * 
	 * @param pagename
	 *            FriendlyId of the Template
	 * @return Template whose FriendlyId was passed
	 * @deprecated This method seems to access the database directly every 
	 * time it is called.
	 */
	public Template findTemplateByName(String pagename) {
		Template searchedTemplate = null;

		SQLTemplateManager templateManager = new SQLTemplateManager();
		searchedTemplate = templateManager.getTemplateByFriendlyID(pagename);

		return searchedTemplate;
	}

}

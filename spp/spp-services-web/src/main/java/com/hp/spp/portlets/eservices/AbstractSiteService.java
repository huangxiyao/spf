package com.hp.spp.portlets.eservices;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.context.support.ApplicationObjectSupport;

import com.hp.spp.common.ResourceHistoryManager;
import com.hp.spp.common.util.SppConstants;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.dao.SiteDAO;
import com.hp.spp.eservice.services.EServiceImportValidationService;
import com.hp.spp.eservice.services.MarshalService;
import com.hp.spp.eservice.services.UnmarshalService;

public abstract class AbstractSiteService extends ApplicationObjectSupport {

	private static Logger logger = Logger.getLogger(AbstractSiteService.class);

	protected UnmarshalService mUnmarshalService;

	protected MarshalService mMarshalService;
	
	protected EServiceImportValidationService mImportValidationService;

	protected ResourceHistoryManager resourceHistoryManager;

	public void setMarshalService(MarshalService marshalService) {
		this.mMarshalService = marshalService;
	}

	public void setUnmarshalService(UnmarshalService unmarshalService) {
		this.mUnmarshalService = unmarshalService;
	}

	public ResourceHistoryManager getResourceHistoryManager() {
		return resourceHistoryManager;
	}

	public void setResourceHistoryManager(ResourceHistoryManager resourceHistoryManager) {
		this.resourceHistoryManager = resourceHistoryManager;
	}

	public MarshalService getMarshalService() {
		return mMarshalService;
	}

	public UnmarshalService getUnmarshalService() {
		return mUnmarshalService;
	}

	public Site getSite(String siteName) {
		
		Site site = SiteDAO.getInstance().loadByName(siteName);
		if (site==null)
			throw new NullPointerException("There is no site with the name "+siteName);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Loaded "+site.getName()+" site");
		}
		return site;
	}

	public Site getSite(HashMap userContextKeys) {

		String	siteName = (String) userContextKeys
					.get(SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME);
		if (siteName==null)
			throw new NullPointerException("SPP Exception : The site name can not be null in method getSite(Hashmap)");
		return getSite(siteName);
	}

	public EServiceImportValidationService getImportValidationService() {
		return mImportValidationService;
	}

	public void setImportValidationService(EServiceImportValidationService importValidationService) {
		mImportValidationService = importValidationService;
	}

}

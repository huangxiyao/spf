package com.hp.spp.portlets.eservices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.spp.common.ResourceHistory;
import com.hp.spp.common.dao.ResourceHistoryDAO;
import com.hp.spp.eservice.EService;
import com.hp.spp.eservice.EServiceStatus;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.dao.EServiceDAO;

public class EServiceManager extends AbstractSiteService {

	private static Logger logger = Logger.getLogger(EServiceManager.class);

	public EService getEService(long id) {

		if (logger.isDebugEnabled()) {
			logger.debug("Trying to load eService with id [" + id + "]");
		}

		EService eservice = EServiceDAO.getInstance().load(id);

		if (logger.isDebugEnabled()) {
			logger.debug("Loaded eService with id [" + id + "], name [" + eservice.getName()
					+ "]");
		}
		return eservice;
	}

	public void updateResourceHistory(String changeOwner, String comment, String dataChange,
			Site site) throws Exception {

		byte[] backupXML = mMarshalService.marshalEServices(site);
		resourceHistoryManager.updateResourceHistory(changeOwner, comment, dataChange, site
				.getId(), backupXML, ResourceHistory.ESERVICE_MODIFICATION);
	}

	public List importXML(byte[] xmlContent, Site site) throws Exception {

		// mark each eService as new or updated
		Site uploadedSite = mUnmarshalService.unmarshalEServices(xmlContent);

		mImportValidationService.validateEServiceImport(uploadedSite, site);

		Set uploadedEServices = uploadedSite.getEServiceList();

		ArrayList statusList = new ArrayList(uploadedEServices.size());

		Set currentEServices = site.getEServiceList();

		List namesList = getNamesFromEServices(currentEServices);

		Iterator iter = uploadedEServices.iterator();
		while (iter.hasNext()) {
			EService eservice = (EService) iter.next();
			EServiceStatus status = new EServiceStatus(eservice);
			if (namesList.contains(eservice.getName())) {
				status.setExistingFlag(EServiceStatus.EXISTING_ESERVICE);
			} else {
				status.setExistingFlag(EServiceStatus.NEW_ESERVICE);
			}
			statusList.add(status);
		}

		return statusList;
	}

	private List getNamesFromEServices(Set currentEServices) {

		ArrayList namesList = new ArrayList(currentEServices.size());
		Iterator iter = currentEServices.iterator();
		while (iter.hasNext()) {
			EService eservice = (EService) iter.next();
			String name = eservice.getName();
			namesList.add(name);
		}
		return namesList;
	}

	public byte[] getEServiceDefinitions(List eServiceIds, Site site) {

		// get rid of the eservices that are not selected
		// DO NOT SAVE!!!
		Iterator iter = new ArrayList(site.getEServiceList()).iterator();
		while (iter.hasNext()) {
			EService eservice = (EService) iter.next();
			Long id = new Long(eservice.getId());
			if (!eServiceIds.contains(id)) {
				site.removeEService(eservice);
			}
		}

		byte[] xml = mMarshalService.marshalEServices(site);

		if (logger.isDebugEnabled()) {
			logger.debug("Got definitions for eServices [" + eServiceIds + "]: " + xml);
		}

		return xml;
	}

	public List getHistoryList(Site site) {
		List historyList = ResourceHistoryDAO.getInstance().getResourceHistoryByType(
				ResourceHistory.ESERVICE_MODIFICATION, site.getId());
		return historyList;
	}
}

package com.hp.spp.portlets.paramsets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.spp.common.ResourceHistory;
import com.hp.spp.common.dao.ResourceHistoryDAO;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.StandardParameterSet;
import com.hp.spp.eservice.StandardParameterSetStatus;
import com.hp.spp.eservice.dao.StandardParameterSetDAO;
import com.hp.spp.portlets.eservices.AbstractSiteService;

public class StandardParameterSetManager extends AbstractSiteService {

	private static Logger logger = Logger.getLogger(StandardParameterSetManager.class);

	public StandardParameterSet getStandardParameterSet(long id) {

		if (logger.isDebugEnabled()) {
			logger.debug("Trying to load standardParameterSet with id [" + id + "]");
		}

		StandardParameterSet paramSet = StandardParameterSetDAO.getInstance().load(id);

		if (logger.isDebugEnabled()) {
			logger.debug("Loaded standardParameterSet with id [" + id + "], name ["
					+ paramSet.getName() + "]");
		}
		return paramSet;
	}

	public void updateResourceHistory(String changeOwner, String comment, String dataChange,
			Site site) throws Exception {

		byte[] backupXML = mMarshalService.marshalStandardParameterSets(site);
		resourceHistoryManager.updateResourceHistory(changeOwner, comment, dataChange, site
				.getId(), backupXML, ResourceHistory.STANDARD_PARAM_SET_MODIFICATION);
	}

	public List importXML(byte[] xmlContent, Site site) throws Exception {

		// mark each standardParameterSet as new or updated
		Site uploadedSite = mUnmarshalService.unmarshalStandardParameterSets(xmlContent);

		mImportValidationService.validateStandardParameterSetImport(uploadedSite, site);

		Set uploadedStandardParameterSets = uploadedSite.getStandardParameterSet();

		ArrayList statusList = new ArrayList(uploadedStandardParameterSets.size());

		Set currentStandardParameterSets = site.getStandardParameterSet();

		List namesList = getNamesFromStandardParameterSets(currentStandardParameterSets);

		Iterator iter = uploadedStandardParameterSets.iterator();
		while (iter.hasNext()) {
			StandardParameterSet param = (StandardParameterSet) iter.next();
			String name = param.getName();
			StandardParameterSetStatus status = new StandardParameterSetStatus(param);
			if (namesList.contains(name)) {
				status.setExistingFlag(StandardParameterSetStatus.EXISTING_PARAM_SET);
			} else {
				status.setExistingFlag(StandardParameterSetStatus.NEW_PARAM_SET);
			}
			statusList.add(status);
		}

		return statusList;
	}

	private List getNamesFromStandardParameterSets(Set currentStandardParameterSets) {

		ArrayList namesList = new ArrayList(currentStandardParameterSets.size());
		Iterator iter = currentStandardParameterSets.iterator();
		while (iter.hasNext()) {
			StandardParameterSet param = (StandardParameterSet) iter.next();
			String name = param.getName();
			namesList.add(name);
		}
		return namesList;
	}

	public byte[] getStandardParameterSetDefinitions(List standardParameterSetIds, Site site) {

		// get rid of the params that are not selected
		// DO NOT SAVE!!!
		Iterator iter = new ArrayList(site.getStandardParameterSet()).iterator();
		while (iter.hasNext()) {
			StandardParameterSet param = (StandardParameterSet) iter.next();
			Long id = new Long(param.getId());
			if (!standardParameterSetIds.contains(id)) {
				site.removeStandardParameterSet(param);
			}
		}

		byte[] xml = mMarshalService.marshalStandardParameterSets(site);

		if (logger.isDebugEnabled()) {
			logger.debug("Got definitions for standardParameterSets ["
					+ standardParameterSetIds + "]: " + xml);
		}

		return xml;
	}

	public List getHistoryList(Site site) {
		List historyList = ResourceHistoryDAO.getInstance().getResourceHistoryByType(
				ResourceHistory.STANDARD_PARAM_SET_MODIFICATION, site.getId());
		return historyList;
	}

	public boolean referencedByEService(StandardParameterSet standardParameterSet) {
		int references = StandardParameterSetDAO.getInstance().getNumberOfReferencingEServices(
				standardParameterSet);
		return references > 0;
	}
}

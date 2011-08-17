package com.hp.spp.portlets.groups;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.hp.spp.common.ResourceHistory;
import com.hp.spp.common.dao.ResourceHistoryDAO;
import com.hp.spp.groups.Group;
import com.hp.spp.groups.Site;
import com.hp.spp.groups.SiteManager;
import com.hp.spp.groups.dao.SiteDAOCacheImpl;
import com.hp.spp.groups.exception.XmlImportException;

//public class GroupService extends ApplicationObjectSupport {
public class GroupService {

	private static Logger logger = Logger.getLogger(GroupService.class);

	/**
	 * The name of the site that will be managed
	 */
	private String siteName;

	/**
	 * the site get from the cache. the cache has been fullfilled from DB before
	 */
	private Site site;

	public Site getSite() {
		return site;
	}

	/**
	 * Initialise a new GroupService from the siteName
	 */
	public void init(String siteName) {
		this.siteName = siteName;
		site = SiteDAOCacheImpl.getInstance().loadByName(siteName);
		logger.debug("Init : The loaded site is [" + site.getId() + " : " + site.getName()
				+ "]");
	}

	public Group getGroup(long groupId) {
		Iterator it = getGroupList().iterator();
		Group group = null;
		while (it.hasNext()) {
			Group g = (Group) it.next();
			if (g.getId() == groupId) {
				group = g;
				break;
			}
		}
		return group;
	}

	public Group getGroupByName(String groupName) {
		if (groupName == null) {
			return null;
		}

		Iterator it = getGroupList().iterator();
		Group group = null;
		while (it.hasNext()) {
			Group g = (Group) it.next();
			if (groupName.equals(g.getName())) {
				group = g;
				break;
			}
		}
		return group;
	}

	public Set getGroupList() {
		return site.getGroupList();
	}

	public List importXML(Map userContext, byte[] xmlContent) throws Exception {
		SiteManager sm = new SiteManager(site);
		try {
			List list = sm.loadFromByteArray(userContext, xmlContent);
			if (logger.isDebugEnabled()) {
				logger.debug(list);
			}
			return list;
		} catch (ParserConfigurationException e) {
			logger.error("Unable load groups from XML", e);
			throw e;
		} catch (SAXException e) {
			logger.info("Unable load groups from XML: " + e.getMessage());
			throw new XmlImportException("Problem during XML File parsing. " + e.getMessage());
		} catch (IOException e) {
			logger.error("Unable load groups from XML", e);
			throw e;
		} catch (XmlImportException e) {
			logger.info("Unable load groups from XML: " + e.getMessage());
			throw e;
		}

	}

	/**
	 * Returns the change history of this Site.
	 */
	public List getHistoryList() {
		List historyList = ResourceHistoryDAO.getInstance().getResourceHistoryByType(
				ResourceHistory.USERGROUP_MODIFICATION, site.getId());
		return historyList;
	}

	public String getSiteName() {
		return siteName;
	}

}

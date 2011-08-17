package com.hp.spp.portlets.groups;

import java.util.Date;
import java.util.HashMap;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;

import com.hp.spp.common.exception.BrowserBackException;
import com.hp.spp.common.util.SppConstants;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.common.util.xml.XmlUtils;
import com.hp.spp.groups.Group;
import com.hp.spp.groups.Site;
import com.hp.spp.groups.GroupStatus;
import com.hp.spp.groups.SiteManager;
import com.hp.spp.groups.dao.GroupDAOHibernateImpl;
import com.hp.spp.groups.dao.SiteDAOCacheImpl;

/**
 * Controller import an XML file as a list of groups
 * 
 * @author MJULIENS
 * 
 */
public class GroupSaveImportFormController extends SimpleFormController implements
		InitializingBean {

	private static Logger logger = Logger.getLogger(GroupSaveImportFormController.class);

	private GroupService groupService;

	public void afterPropertiesSet() throws Exception {
//		if (this.groupService == null)
//			throw new IllegalArgumentException("A GroupService is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		String changeOwner = request.getParameter("changeOwner");

		String comment = request.getParameter("comment");

		XMLUpload xmlUpload = (XMLUpload) command;

		if (logger.isDebugEnabled()) {
			logger.debug("GroupSaveImportFormController : onSubmitAction");
			logger.debug("changeOwner : " + changeOwner);
			logger.debug("comment : " + comment);
			logger.debug(xmlUpload.getFile());
			logger.debug(xmlUpload.getUpdatedGroupMap());
		}

		Date currentDate = new Date();

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			if (logger.isDebugEnabled()) {
				logger.debug("tx created");
			}
			
			// create the string to store the changes
			StringBuffer dataChange = new StringBuffer();
			for (int i = 0; i < xmlUpload.getUpdatedGroupMap().size(); i++) {
				GroupStatus gs = (GroupStatus) xmlUpload.getUpdatedGroupMap().get(i);
				String groupName = (gs).getMGroupName();
				if (gs.getExistingFlag() == GroupStatus.EXISTING_GROUP) {
					dataChange.append(groupName + " - updated");
				} else {
					dataChange.append(groupName + " - created");
				}
				if (i != xmlUpload.getUpdatedGroupMap().size() - 1) {
					dataChange.append("<br>");
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Datachange " + dataChange.toString());
			}

			// get the name of the site from the passing parameters
			// and load it from the cache
			String siteName = null;

			HashMap UseContextKeys = (HashMap) request
					.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
			if ((UseContextKeys!=null) && (UseContextKeys
					.get(SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME) != null) ) {
				siteName = (String) UseContextKeys
						.get(SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME);
			}
			groupService = new GroupService();
			groupService.init(siteName);
			
			SiteManager sm = new SiteManager(groupService.getSite());
			sm.updateResourceHistory(changeOwner, comment, dataChange.toString());
			
			Site currenSite = groupService.getSite();
			
			// manage the upload information
			for (int i = 0; i < xmlUpload.getUpdatedGroupMap().size(); i++) {
				// for each group in the list, treat the data
				GroupStatus gs = (GroupStatus) xmlUpload.getUpdatedGroupMap().get(i);
				if(logger.isDebugEnabled()) {
					logger.debug(gs.getXmlFragment());
				}
				// if its a creation, create the group in the DB
				if (gs.getExistingFlag() == GroupStatus.NEW_GROUP) {
					if(logger.isDebugEnabled()) {
						logger.debug("create group ["+gs.getMGroupName()+"] for site ["+currenSite.getName()+"]");
					}
					// add a new group
					Group newGroup = new Group();
					newGroup.setCreationDate(currentDate);
					newGroup.setModificationDate(currentDate);
					newGroup.setName(gs.getMGroupName());
					newGroup.setSite(groupService.getSite());
					byte[] xmlFragment = XmlUtils.xmlToByteArray(gs.getXmlFragment());
					newGroup.setRules(xmlFragment);
					GroupDAOHibernateImpl.getInstance().save(newGroup);
					// add the new group to the groupService
					//groupService.getSite().getGroupList().add(newGroup);
				} else if (gs.getExistingFlag() == GroupStatus.EXISTING_GROUP) {
					if(logger.isDebugEnabled()) {
						logger.debug("update group ["+gs.getMGroupName()+"] for site ["+currenSite.getName()+"]");
					}

					// update an existing group
					Group group = GroupDAOHibernateImpl.getInstance().loadBySiteIdAndName(
							gs.getMGroupName(),currenSite.getId());
					if (group != null) {
						group.setModificationDate(currentDate);
						byte[] xmlFragment = XmlUtils.xmlToByteArray(gs.getXmlFragment());
						group.setRules(xmlFragment);
						GroupDAOHibernateImpl.getInstance().save(group);
						// update the group in the groupService
						//Group localGroup = groupService.getGroupByName(gs.getMGroupName());
						//if (localGroup != null) {
						//	localGroup.setModificationDate(currentDate);
						//}
					} else {
						logger.warn("The group with name [" + gs.getMGroupName()
								+ "] is not found in the DB");
					}
				} else {
					logger.warn("Group status incorrect [" + gs.getExistingFlag() + "]");
				}
			}

			// clear the cache
			SiteDAOCacheImpl.getInstance().removeFromCache(groupService.getSite().getName());

			tx.commit();
			if (logger.isDebugEnabled()) {
				logger.debug("tx commited");
			}

		} catch (Exception e) {
			logger.error("Error during update of groups", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					logger.error("Error during ROLLBACK of Hibernate transaction", he);
				}
				tx = null;
			}
			// Raising exception (will be caught by the framework)
			throw new ServletException(e);
		} finally {
			try {
				HibernateUtil.getSessionFactory().getCurrentSession().close();
				if (logger.isDebugEnabled()) {
					logger.debug("tx closed");
				}
			} catch (HibernateException he) {
				logger.error("Exception during CLOSE of Hibernate transaction", he);
			}
		}
		// add the confirmation message in the request
		request.getPortletSession().setAttribute("confirmationMessage",
				"The groups have been well updated", PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("action", "groups");
	}

	/*
	 * public ModelAndView onSubmitRender(ActionRequest request, ActionResponse response,
	 * Object command, BindException errors) throws Exception {
	 * 
	 * System.out.println("Import Controller : onSubmitRender");
	 * 
	 * String xmlFile = null;
	 * 
	 * xmlFile = (String) request.getParameter("file"); // xmlFile = upload.getFile();
	 * 
	 * System.out.println(xmlFile); Map map = groupService.importXML(xmlFile.getBytes());
	 * 
	 * Map model = new HashMap(); model.put("updateGroupMap", map); model.put("file", xmlFile);
	 * 
	 * return new ModelAndView("groupUpdate", "model", model); }
	 */

	/**
	 * In this controle, the command object (upload information) must be read from the session.
	 */
	protected Object formBackingObject(PortletRequest request) throws Exception {

		XMLUpload xmlUpload;
		// read from the session
		xmlUpload = (XMLUpload) request.getPortletSession().getAttribute("xmlUpload");

		if (xmlUpload==null)
		{
			logger.info("Unable to get the XML file to import, it should be due to a back in the browser");
			throw new BrowserBackException("Unable to get the content of the XML file to import, please, do not use the \"Back\" button in your browser");
		}
		
		// remove this attribute from the session
		request.getPortletSession().removeAttribute("xmlUpload");

		return xmlUpload;
	}

	/*
	 * protected void initBinder(PortletRequest request, PortletRequestDataBinder binder)
	 * throws Exception { binder.setAllowedFields(new String[] { "file" }); }
	 */

	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
			throws Exception {
		return null;
	}

	/*
	 * protected void handleInvalidSubmit(ActionRequest request, ActionResponse response)
	 * throws Exception { response.setRenderParameter("action", "groups"); }
	 */

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

}

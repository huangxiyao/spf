package com.hp.spp.portlets.groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.servlet.ServletException;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.w3c.dom.Document;

import com.hp.spp.common.exception.BrowserBackException;
import com.hp.spp.common.util.SppConstants;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.common.util.xml.XMLContent;
import com.hp.spp.common.util.xml.XmlUtils;
import com.hp.spp.groups.Group;
import com.hp.spp.groups.SiteManager;

/**
 * Controller to export the XML file stored in the history table
 * 
 * @author MJULIENS
 * 
 */
public class GroupExportFormController extends SimpleFormController implements
		InitializingBean {

	private GroupService groupService;

	public void afterPropertiesSet() throws Exception {
//		if (this.groupService == null)
//			throw new IllegalArgumentException("A GroupService is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("GroupExportFormController : onSubmitAction");
		}

		XMLContent xmlContent = (XMLContent) command;

		// get the list of groups to export
		String[] formContent = request.getParameterValues("groupId");
		if (logger.isDebugEnabled()) {
			logger.debug("The list of checkboxes is [" + formContent + "]");
		}

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

			// get the name of the site from the passing parameters
			// and load it from the cache
			String siteName = null;
			
			HashMap UseContextKeys = (HashMap) request.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
			if ((UseContextKeys!=null) && (UseContextKeys
					.get(SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME) != null) ) {
				siteName = (String) UseContextKeys
						.get(SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME);
			}
			groupService = new GroupService();
			groupService.init(siteName);
			
			// instanciate a SiteManager and get the exported XMLFile
			SiteManager sm = new SiteManager(groupService.getSite());
			// get the names of the groups according to the ids
			List groupNameList = new ArrayList();
			for (int i = 0; i < formContent.length; i++) {
				Group g = groupService.getGroup(Long.parseLong(formContent[i]));
				// check that the group has been found in the site to avoid the bug with cache or back button
				if (g==null)
				{
					logger.error("Try to export a group that does not exist in the Site");
					throw new BrowserBackException("Unable to export the groups because at least one of the selected group does no longer exist");
				}
				groupNameList.add(g.getName());
			}
			Document xmlDocument = sm.getDefinitionsOfGroups(groupNameList);

			xmlContent
					.setXmlContent(XmlUtils.xmlToByteArray(xmlDocument.getDocumentElement()));

			tx.commit();

		} catch (Exception e) {
			logger.error("Catch Exception during exporting the xml file [" + e.getMessage() + "]", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					logger.error("Exception during ROLLBACK of Hibernate transaction", he);
				}
				tx = null;
			}
			// Raising exception (will be caught by the framework)
			throw new ServletException(e);
		} finally {
			try {
				HibernateUtil.getSessionFactory().getCurrentSession().close();
			} catch (HibernateException he) {
				logger.error("Exception during CLOSE of Hibernate transaction", he);
			}
		}
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

	protected Object formBackingObject(PortletRequest request) throws Exception {

		XMLContent xmlContent;

		xmlContent = new XMLContent();

		return xmlContent;
	}

	/*
	 * protected void initBinder(PortletRequest request, PortletRequestDataBinder binder)
	 * throws Exception { binder.setAllowedFields(new String[] { "file" }); }
	 */

	/*
	 * protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse
	 * response) throws Exception { return null; }
	 */

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

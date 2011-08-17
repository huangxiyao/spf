package com.hp.spp.portlets.groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
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
import com.hp.spp.groups.Group;
import com.hp.spp.groups.SiteManager;

/**
 * Controller to delete a list of groups
 * 
 * @author MJULIENS
 * 
 */
public class GroupDeleteFormController extends SimpleFormController implements
		InitializingBean {

	private static Logger logger = Logger
			.getLogger(GroupDeleteFormController.class);

	private GroupService groupService;

	public void afterPropertiesSet() throws Exception {
		// if (this.groupService == null)
		// throw new IllegalArgumentException("A GroupService is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response,
			Object command, BindException errors) throws Exception {

		GroupList groupList = (GroupList) command;

		String[] formContent = request.getParameterValues("groupId");
		if (logger.isDebugEnabled()) {
			logger.debug("The list of checkboxes is [" + formContent + "]");
		}

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession()
					.beginTransaction();

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

			// fullfill the list of group ids to delete
			// fullfill the list of group names to delete
			List idList = new ArrayList();
			List nameList = new ArrayList();
			for (int i = 0; i < formContent.length; i++) {
				idList.add(formContent[i]);
				// get the name of th group from its Id
				Group g = groupService.getGroup(Long.parseLong(formContent[i]));
				// check that the group has been found in the site to avoid the bug with cache or back button
				if (g==null)
				{
					logger.error("Try to delete a group that does not exist in the Site");
					throw new BrowserBackException("Unable to delete the groups because at least one of the selected group does no longer exist");
				}
				nameList.add(g.getName());
			}
			
			// check that the groups can be deleted
			// this call will check that the groups that will be deleted are not referenced by other groups
			SiteManager sm = new SiteManager(groupService.getSite());
			String errorMessage = sm.checkGroupsToDelete(nameList);
			if (errorMessage != null)
			{
				logger.error(errorMessage);
				throw new Exception (errorMessage);
			}
			
			groupList.setGroupIdList(idList);
			groupList.setGroupNameList(nameList);

			if (logger.isDebugEnabled()) {
				logger.debug("delete : Id list [" + groupList.getGroupIdList()
						+ "]");
				logger.debug("delete : name list ["
						+ groupList.getGroupNameList() + "]");
			}
		} catch (Exception e) {
			logger.error("Error during display list of selected groups", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					logger.error(
							"Error during ROLLBACK of Hibernate transaction",
							he);
				}
				tx = null;
			}
			// Raising exception (will be caught by the framework)
			throw new ServletException(e);
		} finally {
			try {
				HibernateUtil.getSessionFactory().getCurrentSession().close();
			} catch (HibernateException he) {
				logger.error("Exception during CLOSE of Hibernate transaction",
						he);
			}
		}

		request.getPortletSession().setAttribute("groupList", groupList);

	}

	protected Object formBackingObject(PortletRequest request) throws Exception {

		GroupList groupList;

		groupList = new GroupList();

		return groupList;
	}

	protected ModelAndView renderInvalidSubmit(RenderRequest request,
			RenderResponse response) throws Exception {
		return null;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

}

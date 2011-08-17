package com.hp.spp.portlets.groups;

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
import com.hp.spp.groups.Group;
import com.hp.spp.groups.SiteManager;
import com.hp.spp.groups.dao.GroupDAOHibernateImpl;
import com.hp.spp.groups.dao.SiteDAOCacheImpl;

/**
 * Controller validate the delete of groups delete them in DB
 * 
 * @author MJULIENS
 * 
 */
public class GroupSaveDeleteFormController extends SimpleFormController implements
		InitializingBean {

	private GroupService groupService;

	private static Logger logger = Logger.getLogger(GroupSaveDeleteFormController.class);

	public void afterPropertiesSet() throws Exception {
//		if (this.groupService == null)
//			throw new IllegalArgumentException("A GroupService is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		String changeOwner = request.getParameter("changeOwner");

		String comment = request.getParameter("comment");

		GroupList groupList = (GroupList) command;

		if(logger.isDebugEnabled()) {
			logger.debug("GroupSaveDeleteFormController : onSubmitAction");
			logger.debug("changeOwner : " + changeOwner);
			logger.debug("comment : " + comment);
			logger.debug(groupList.getGroupNameList());
		}

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
						
			if (logger.isDebugEnabled()) {
				logger.debug("tx created");
			}
			
			// create the string to store the changes
			StringBuffer dataChange = new StringBuffer();
			for (int i = 0; i < groupList.getGroupNameList().size(); i++) {
				String groupName = (String) groupList.getGroupNameList().get(i);
				dataChange.append(groupName + " - deleted");
				if (i != groupList.getGroupNameList().size() - 1) {
					dataChange.append("<br>");
				}
			}
			if (logger.isDebugEnabled()) {
			logger.debug("Datachange " + dataChange.toString());
			}
			
//			 get the name of the site from the passing parameters
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
			
			// browse all the group names contained in the list and delete them
			for (int i = 0; i < groupList.getGroupIdList().size(); i++) {
				// delete in database
				long id = Long.parseLong((String) groupList.getGroupIdList().get(i));
				Group g = GroupDAOHibernateImpl.getInstance().load(id);
				GroupDAOHibernateImpl.getInstance().delete(g);
				// remove it from the groupService object
				/*
				Iterator it = groupService.getSite().getGroupList().iterator();
				while (it.hasNext()) {
					Group localGroup = (Group) it.next();
					if (localGroup.getId() == id) {
						groupService.getSite().getGroupList().remove(localGroup);
						break;
					}
				}
				*/

			}

			// clear the cache
			SiteDAOCacheImpl.getInstance().removeFromCache(groupService.getSite().getName());

			tx.commit();
			if (logger.isDebugEnabled()) {
				logger.debug("tx commited");
			}

		} catch (Exception e) {
			logger.error("Error during delete of groups", e);
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
				"The groups have been well deleted", PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("action", "groups");
	}

	protected ModelAndView onSubmitRender(RenderRequest arg0, RenderResponse arg1,
			Object arg2, BindException arg3) throws Exception {

		return super.onSubmitRender(arg0, arg1, arg2, arg3);
	}

	/**
	 * In this controle, the command object (upload information) must be read from the session.
	 */
	protected Object formBackingObject(PortletRequest request) throws Exception {

		GroupList groupList;
		// read from the session
		groupList = (GroupList) request.getPortletSession().getAttribute("groupList");

		if (groupList==null)
		{
			logger.info("Unable to get the list of groups to delete, it should be due to a back in the browser");
			throw new BrowserBackException("Unable to get the list of groups to delete, please, do not use the \"Back\" button in your browser");
		}
		
		// remove this attribute from the session
		request.getPortletSession().removeAttribute("groupList");

		return groupList;
	}

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

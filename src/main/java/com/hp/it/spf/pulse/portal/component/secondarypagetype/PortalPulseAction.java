/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.pulse.portal.component.secondarypagetype;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hp.it.spf.pulse.portal.component.generic.DatabaseCheckTask;
import com.hp.it.spf.pulse.portal.component.generic.HPPWebServiceCheckTask;
import com.hp.it.spf.pulse.portal.component.generic.IComponentCheckTask;
import com.hp.it.spf.pulse.portal.component.generic.PortalPulseConfig;
import com.hp.it.spf.pulse.portal.component.generic.PortalPulseConfigException;
import com.hp.it.spf.pulse.portal.component.generic.WebPageCheckTask;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.BaseAction;

/**
 * A display action for the portal pulse page. This action loads the portal
 * pulse configuration from pulse.xml (expected to be loadable from the
 * classpath) and then performs the check-tasks configured therein. It then
 * stores the loaded configuration for next time (so next time pulse.xml will
 * not need to be re-read).
 * 
 * @author <link href="hao.zhang2@hp.com"> Zhang, Hao </link>
 * @version TBD
 * @see BaseAction
 * @see LogWrapper
 * @see PortalContext
 * @see HttpServletResponse
 * @see HttpSession
 * @see List
 * @see ArrayList
 * @see String
 * @see PortalPulseConfig
 * @see IComponentCheckTask
 * @see DatabaseCheckTask
 * @see HPPWebServiceCheckTask
 * @see WebPageCheckTask
 * @see Iterator
 */
public class PortalPulseAction extends BaseAction {

	/**
	 * the log for vignette when throwing exception
	 */
	private static final LogWrapper LOG = new LogWrapper(
			PortalPulseAction.class);

	/**
	 * The execute method for this class. See the class documentation for an
	 * overview of processing steps.
	 * 
	 * @param portalContext
	 *            The portal context object
	 * @return null (continue to display pulse results page)
	 */
	public PortalURI execute(PortalContext portalContext) {

		HttpServletResponse response = portalContext.getPortalResponse()
				.getResponse();
		HttpSession session = portalContext.getPortalRequest().getSession();

		// taskList containing the monitoring tasks
		List taskList = (List) session.getAttribute("portal-pulse.tasks");
		if (taskList == null) {
			PortalPulseConfig pulseConfig = new PortalPulseConfig();

			// read configuring file
			try {
				pulseConfig.readConfig();
			} catch (PortalPulseConfigException e) {
				LOG.error(e.toString());
			}

			/*
			 * comment by ck for 1000813522 --remove the hard code tasks, both
			 * these tasks should be controlled through pulse.xml initial
			 * the monitoring tasks taskList = new ArrayList(11);
			 * IComponentCheckTask task; task = new DatabaseCheckTask();
			 * task.init(); taskList.add(task); task = new
			 * HPPWebServiceCheckTask(); task.init(); taskList.add(task); List
			 * webpageTaskList = null; webpageTaskList =
			 * healthCheckConfig.getWebPageTaskList(); int taskCount =
			 * webpageTaskList.size(); for (int i = 0; i < taskCount; i++) {
			 * task = (WebPageCheckTask)webpageTaskList.get(i); task.init();
			 * taskList.add(task); } session.setAttribute("portal-pulse.tasks",
			 * taskList);
			 */
			// added by ck for CR 1000813522
			taskList = pulseConfig.getMonitoringTaskList();
			session.setAttribute("portal-pulse.tasks", taskList);
		}

		String xSiteAvailable = "yes";

		// run the monitoring tasks
		Iterator it = taskList.iterator();
		while (it.hasNext()) {

			IComponentCheckTask task = (IComponentCheckTask) it.next();

			task.run();

			int status = task.getStatus();
			if (status == IComponentCheckTask.STATUS_PASS) {
				continue;
			} else if (status == IComponentCheckTask.STATUS_FAIL) {
				xSiteAvailable = "no";
			} else {
				xSiteAvailable = "no";
			}
		}
		response.addHeader("X-Site-Available", xSiteAvailable);

		return null; // null meaning continue to display pulse page
	}
}

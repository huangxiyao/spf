package com.hp.it.spf.pulse.portal.component.secondarypagetype.util;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.it.spf.xa.misc.portal.Utils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.vignette.portal.log.LogWrapper;

/**
 * A class configuring the web page check task from the healthcheck.xml file.
 * 
 * @author <link href="huakun.gao@hp.com"> Gao, Hua-Kun </link>
 * @author <link href="hao.zhang2@hp.com"> Zhang, Hao </link>
 * @version TBD
 * @see LogWrapper
 * @see List
 * @see InputStream
 * @see SAXBuilder
 * @see Document
 * @see Element
 * @see Iterator
 * @see Map
 * @see WebPageCheckTask
 */
public class PortalPulseConfig {

	private static final String CONFIG_FILE_NAME = "pulse.xml";
	/* added by ck for CR: 1000813522 */
	private static final String MONITORING_CHECK_TASK = "monitoring-check-task";
	private static final String HPP_WEB_SERVICE_CHECK_TASK = "HPP_WEB_SERVICE_CHECK";
	private static final String VIGNETTE_DATABASE_CHECK_TASK = "VIGNETTE_DATABASE_CHECK";
	/* added end for CR: 1000813522 */
	/**
	 * the log for vignette when throwing exception
	 */
	private static final LogWrapper LOG = new LogWrapper(
			PortalPulseConfig.class);

	/**
	 * the list containing the webpage tasks
	 */
	/*
	 * commented by ck for CR: 1000813522 private List webPageTaskList = new
	 * ArrayList();
	 */
	/*
	 * added by ck for CR: 1000813522
	 */
	private List monitoringTaskList = new ArrayList();

	/**
	 * getter
	 * 
	 * @return List
	 */
	public List getMonitoringTaskList() {
		return monitoringTaskList;
	}

	/**
	 * constructor for PortalPulseConfig
	 */
	public PortalPulseConfig() {
	}

	/**
	 * parse XML file for check health
	 * 
	 * @throws PortalPulseConfigException
	 *             PortalPulseConfigException
	 */
	public void readConfig() throws PortalPulseConfigException {
		InputStream fin = null;

		try {
			// modified by ck for QXCR1000776072
			// fin =
			// ClassLoader.getSystemClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
			fin = Utils.getResourceAsStream(CONFIG_FILE_NAME);
			// end QXCR1000776072
			if (fin == null) {
				LOG.warning("The " + CONFIG_FILE_NAME
						+ " configuration file could not be found");
				return;
			}
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(fin);
			Element root = doc.getRootElement();
			Element taskElement = null;

			Iterator it0 = root.getChildren().iterator();
			/* the following is modified by ck for CR: 1000813522 */
			while (it0.hasNext()) {
				taskElement = (Element) it0.next();
				if (MONITORING_CHECK_TASK.equals(taskElement.getName())) {
					String name = taskElement.getAttributeValue("name");
					Map params = getInitParams(taskElement);
					if (name.equalsIgnoreCase(HPP_WEB_SERVICE_CHECK_TASK)) {
						HPPWebServiceCheckTask task = new HPPWebServiceCheckTask();
						task.setParams(params); // Reserved for future use.
						task.init();
						monitoringTaskList.add(task);
					} else if (name
							.equalsIgnoreCase(VIGNETTE_DATABASE_CHECK_TASK)) {
						DatabaseCheckTask task = new DatabaseCheckTask();
						task.setParams(params); // Reserved for future use.
						task.init();
						monitoringTaskList.add(task);
					} else {
						WebPageCheckTask task = new WebPageCheckTask(name);
						task.setParams(params);
						task.init();
						monitoringTaskList.add(task);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Error while reading " + CONFIG_FILE_NAME
					+ " configuration file: " + e);
			throw new PortalPulseConfigException(e);
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}
			} catch (Exception e) {
				LOG.error("Error while closing " + CONFIG_FILE_NAME
						+ " configuration file: " + e);
			}
		}
	}

	/**
	 * get the initialized parameters from configuring resource
	 * 
	 * @param taskElement
	 * @return Map
	 */
	private Map getInitParams(Element taskElement) {
		Map ret = new HashMap();

		Iterator it = taskElement.getChildren().iterator();
		while (it.hasNext()) {
			Element initParamElement = (Element) it.next();
			String name = initParamElement.getChildText("param-name");
			String value = initParamElement.getChildText("param-value");

			if (name != null && value != null) {
				ret.put(name, value);
			} else {
				LOG.warning("Invalid element in " + CONFIG_FILE_NAME
						+ " configuration file: " + "param-name: " + name + " "
						+ "param-value: " + value);
			}
		}

		return ret;
	}
}

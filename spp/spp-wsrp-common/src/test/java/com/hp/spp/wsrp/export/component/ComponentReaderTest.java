package com.hp.spp.wsrp.export.component;

import java.util.ArrayList;
import java.util.List;

import com.hp.spp.wsrp.export.Test;
import com.hp.spp.wsrp.export.component.mapping.Component;
import com.hp.spp.wsrp.export.component.mapping.PortletInstance;

public class ComponentReaderTest extends Test {

//	public void testReadComponent() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		Component component = null ;
//		
//		xml = "<epideploy:component build-version=\"gold\" component-id=\"0045cda85f5c908efb55277786daa8f0\" component-type=\"Portlet\" description=\"Portlet that displays a list of content items.\" epi-build=\"223\" epi-version=\"7.2.1\" major-version=\"1\" minor-version=\"0\" title=\"COL Document List Financing\" xmlns:epideploy=\"http://www.epicentric.com/deployment\"><epideploy:detail><portlet-instance chrome-displayed=\"false\" friendly-id=\"colListHomePageduplicate7\" kind=\"LOCAL\" portlet-species-name=\"STANDARD_PORTLET\" published=\"false\" selectable=\"false\" server-portlet-category=\"b0a288b31e7af019abca4dc398b62e20\" service-provider-id=\"c0d5df883e5d908efb55277786daa8f0\" service-provider-name=\"JSR\" timeout=\"2147483647\" width=\"0\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
//		component = reader.readComponent(readInputStr(xml));
//		assertNotNull("Component is not null", component);
//		
//		try {
//			xml = "<epideploy:component><epideploy:detail><portlet-instance chrome-displayed=\"false\" friendly-id=\"colListHomePageduplicate7\" kind=\"LOCAL\" portlet-species-name=\"STANDARD_PORTLET\" published=\"false\" selectable=\"false\" server-portlet-category=\"b0a288b31e7af019abca4dc398b62e20\" service-provider-id=\"c0d5df883e5d908efb55277786daa8f0\" service-provider-name=\"JSR\" timeout=\"2147483647\" width=\"0\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
//			component = reader.readComponent(readInputStr(xml));
//			fail("ComponentException expected when Component attributes not found");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			xml = "<epideploy:component build-version=\"gold\" component-id=\"0045cda85f5c908efb55277786daa8f0\" component-type=\"Portlet\" description=\"Portlet that displays a list of content items.\" epi-build=\"223\" epi-version=\"7.2.1\" major-version=\"1\" minor-version=\"0\" title=\"COL Document List Financing\" xmlns:epideploy=\"http://www.epicentric.com/deployment\"></epideploy:component>" ;
//			component = reader.readComponent(readInputStr(xml));
//			fail("ComponentException expected when Component members not found");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			xml = "<test></test>" ;
//			component = reader.readComponent(readInputStr(xml));
//			fail("ComponentException expected when Component not found");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			component = reader.readComponent(null);
//			fail("ComponentException expected when Document is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadRequiredComponent() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		RequiredComponent requiredComponent = null ;
//
//		xml = "<epideploy:component><epideploy:required-component component-id=\"template0018\" component-type=\"Secondary Page Types\" major-version=\"7\" minor-version=\"2\"/></epideploy:component>" ;
//		requiredComponent = reader.readRequiredComponent(DOMUtils.selectSingleNode(readInputStr(xml), Component.DESCRIPTOR));
//		assertNotNull("RequiredComponent is not null", requiredComponent);
//		
//		xml = "<epideploy:component></epideploy:component>" ;
//		requiredComponent = reader.readRequiredComponent(DOMUtils.selectSingleNode(readInputStr(xml), Component.DESCRIPTOR));
//		assertNull("RequiredComponent is null", requiredComponent);
//		
//		try {
//			xml = "<epideploy:component><epideploy:required-component/></epideploy:component>" ;
//			requiredComponent = reader.readRequiredComponent(DOMUtils.selectSingleNode(readInputStr(xml), Component.DESCRIPTOR));
//			fail("ComponentException expected when RequiredComponent attributes not found");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			requiredComponent = reader.readRequiredComponent(null);
//			fail("ComponentException expected when Component is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadDetails() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		Details details = null ;
//
//		xml = "<epideploy:component><epideploy:detail><portlet-instance chrome-displayed=\"false\" friendly-id=\"colListHomePageduplicate7\" kind=\"LOCAL\" portlet-species-name=\"STANDARD_PORTLET\" published=\"false\" selectable=\"false\" server-portlet-category=\"b0a288b31e7af019abca4dc398b62e20\" service-provider-id=\"c0d5df883e5d908efb55277786daa8f0\" service-provider-name=\"JSR\" timeout=\"2147483647\" width=\"0\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
//		details = reader.readDetails(DOMUtils.selectSingleNode(readInputStr(xml), Component.DESCRIPTOR));
//		assertNotNull("Details is not null", details);
//		
//		xml = "<epideploy:component></epideploy:component>" ;
//		details = reader.readDetails(DOMUtils.selectSingleNode(readInputStr(xml), Component.DESCRIPTOR));
//		assertNull("Details is null", details);
//		
//		try {
//			xml = "<epideploy:component><epideploy:detail></epideploy:detail></epideploy:component>" ;
//			details = reader.readDetails(DOMUtils.selectSingleNode(readInputStr(xml), Component.DESCRIPTOR));
//			fail("ComponentException expected when Details members not found");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			details = reader.readDetails(null);
//			fail("ComponentException expected when Component is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadModuleConfig() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		ModuleConfig moduleConfig = null ;
//		
//		xml = "<epideploy:detail><module-config auto-deploy=\"true\"><descriptor-id>proxyportlet</descriptor-id><web-deployment-path>/beans/proxyportlet</web-deployment-path><locale-key>452f774b23843eabc13c7ef0b528efa0</locale-key></module-config></epideploy:detail>" ;
//		moduleConfig = reader.readModuleConfig(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//		assertNotNull("ModuleConfig is not null", moduleConfig);
//		
//		xml = "<epideploy:detail></epideploy:detail>" ;
//		moduleConfig = reader.readModuleConfig(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//		assertNull("ModuleConfig is null", moduleConfig);
//		
//		try {
//			xml = "<epideploy:detail><module-config auto-deploy=\"true\"></module-config></epideploy:detail>" ;
//			moduleConfig = reader.readModuleConfig(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//			fail("ComponentException expected when ModuleConfig members not found");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			moduleConfig = reader.readModuleConfig(null);
//			fail("ComponentException expected when Details is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadDescriptorId() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		DescriptorId descriptorId = null ;
//
//		xml = "<module-config><descriptor-id>proxyportlet</descriptor-id></module-config>" ;
//		descriptorId = reader.readDescriptorId(DOMUtils.selectSingleNode(readInputStr(xml), ModuleConfig.DESCRIPTOR));
//		assertNotNull("DescriptorId is not null", descriptorId);
//
//		xml = "<module-config><descriptor-id></descriptor-id></module-config>" ;
//		descriptorId = reader.readDescriptorId(DOMUtils.selectSingleNode(readInputStr(xml), ModuleConfig.DESCRIPTOR));
//		assertNull("DescriptorId is null", descriptorId);
//		
//		xml = "<module-config></module-config>" ;
//		descriptorId = reader.readDescriptorId(DOMUtils.selectSingleNode(readInputStr(xml), ModuleConfig.DESCRIPTOR));
//		assertNull("DescriptorId is null", descriptorId);
//		
//		try {
//			descriptorId = reader.readDescriptorId(null);
//			fail("ComponentException expected when ModuleConfig is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadWebDeploymentPath() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		WebDeploymentPath webDeploymentPath = null ;
//
//		xml = "<module-config><web-deployment-path>/beans/proxyportlet</web-deployment-path><locale-key>452f774b23843eabc13c7ef0b528efa0</locale-key></module-config>" ;
//		webDeploymentPath = reader.readWebDeploymentPath(DOMUtils.selectSingleNode(readInputStr(xml), ModuleConfig.DESCRIPTOR));
//		assertNotNull("WebDeploymentPath is not null", webDeploymentPath);
//
//		xml = "<module-config><web-deployment-path></web-deployment-path></module-config>" ;
//		webDeploymentPath = reader.readWebDeploymentPath(DOMUtils.selectSingleNode(readInputStr(xml), ModuleConfig.DESCRIPTOR));
//		assertNull("WebDeploymentPath is null", webDeploymentPath);
//		
//		xml = "<module-config></module-config>" ;
//		webDeploymentPath = reader.readWebDeploymentPath(DOMUtils.selectSingleNode(readInputStr(xml), ModuleConfig.DESCRIPTOR));
//		assertNull("WebDeploymentPath is null", webDeploymentPath);
//		
//		try {
//			webDeploymentPath = reader.readWebDeploymentPath(null);
//			fail("ComponentException expected when ModuleConfig is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadLocaleKey() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		LocaleKey localeKey = null ;
//
//		xml = "<module-config><locale-key>452f774b23843eabc13c7ef0b528efa0</locale-key></module-config>" ;
//		localeKey = reader.readLocaleKey(DOMUtils.selectSingleNode(readInputStr(xml), ModuleConfig.DESCRIPTOR));
//		assertNotNull("LocaleKey is not null", localeKey);
//
//		xml = "<module-config><locale-key></locale-key></module-config>" ;
//		localeKey = reader.readLocaleKey(DOMUtils.selectSingleNode(readInputStr(xml), ModuleConfig.DESCRIPTOR));
//		assertNull("LocaleKey is null", localeKey);
//		
//		xml = "<module-config></module-config>" ;
//		localeKey = reader.readLocaleKey(DOMUtils.selectSingleNode(readInputStr(xml), ModuleConfig.DESCRIPTOR));
//		assertNull("LocaleKey is null", localeKey);
//		
//		try {
//			localeKey = reader.readLocaleKey(null);
//			fail("ComponentException expected when ModuleConfig is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadPortletInstance() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		PortletInstance portletInstance = null ;
//
//		xml = "<epideploy:detail><portlet-instance chrome-displayed=\"false\" friendly-id=\"colListHomePageduplicate7\" kind=\"LOCAL\" portlet-species-name=\"STANDARD_PORTLET\" published=\"false\" selectable=\"false\" server-portlet-category=\"b0a288b31e7af019abca4dc398b62e20\" service-provider-id=\"c0d5df883e5d908efb55277786daa8f0\" service-provider-name=\"JSR\" timeout=\"2147483647\" width=\"0\"><properties/></portlet-instance></epideploy:detail>";
//		portletInstance = reader.readPortletInstance(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//		assertNotNull("PortletInstance is not null", portletInstance);
//		
//		xml = "<epideploy:detail></epideploy:detail>";
//		portletInstance = reader.readPortletInstance(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//		assertNull("PortletInstance is null", portletInstance);
//		
//		try {
//			xml = "<epideploy:detail><portlet-instance></portlet-instance></epideploy:detail>";
//			portletInstance = reader.readPortletInstance(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//			fail("ComponentException expected when PortletInstance attributes not found");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			portletInstance = reader.readPortletInstance(null);
//			fail("ComponentException expected when Details is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadStyleInfo() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		StyleInfo styleInfo = null ;
//
//		xml = "<epideploy:detail><style-info apply-template-header=\"true\" description=\"Logon\" friendly-id=\"sesameLogon\" id=\"dbQeSQTcUWbYUScWdTVdXRSbYWdaaYfQ\" is-system=\"false\" primary-filename=\"sesamelogin.jsp\" processing-type=\"JSP_BASED\" template-default=\"false\" template-uid=\"bcTWYVaadQfRSWZTYaZcaQRQYWdaaYfQ\" title=\"Sesame Logon\" visible=\"true\"/></epideploy:detail>";
//		styleInfo = reader.readStyleInfo(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//		assertNotNull("StyleInfo is not null", styleInfo);
//		
//		xml = "<epideploy:detail></epideploy:detail>";
//		styleInfo = reader.readStyleInfo(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//		assertNull("StyleInfo is null", styleInfo);
//		
//		try {
//			xml = "<epideploy:detail><style-info/></epideploy:detail>";
//			styleInfo = reader.readStyleInfo(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//			fail("ComponentException expected when StyleInfo attributes not found");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			styleInfo = reader.readStyleInfo(null);
//			fail("ComponentException expected when Details is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadTemplateInfo() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		TemplateInfo templateInfo = null ;
//
//		xml = "<epideploy:detail><template-info allow-guest-access=\"false\" description=\"\" friendly-id=\"hpprintable\" header-filename=\"template_header.inc\" id=\"aYSaUbdWeWfTRdXQaZZeXRQcRYZRefZa\" is-system=\"false\" system-secondary-page-type=\"false\" title=\"Sesame Printable\" type=\"templatetype_ui_element\" visible=\"true\"/></epideploy:detail>";
//		templateInfo = reader.readTemplateInfo(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//		assertNotNull("TemplateInfo is not null", templateInfo);
//		
//		xml = "<epideploy:detail></epideploy:detail>";
//		templateInfo = reader.readTemplateInfo(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//		assertNull("TemplateInfo is null", templateInfo);
//		
//		try {
//			xml = "<epideploy:detail><template-info/></epideploy:detail>";
//			templateInfo = reader.readTemplateInfo(DOMUtils.selectSingleNode(readInputStr(xml), Details.DESCRIPTOR));
//			fail("ComponentException expected when TemplateInfo attributes not found");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			templateInfo = reader.readTemplateInfo(null);
//			fail("ComponentException expected when Details is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadActions() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		Actions actions = null ;
//
//		xml = "<template-info><actions><action id=\"1\" type=\"PRE_DISPLAY\"><class>com.hp.sesame.portal.actions.AdminRegisterPreDisplayAction</class></action><action id=\"2\" type=\"PROCESS\"><class>com.hp.sesame.portal.actions.AdminRegisterProcessAction</class></action></actions></template-info>" ;
//		actions = reader.readActions(DOMUtils.selectSingleNode(readInputStr(xml), TemplateInfo.DESCRIPTOR));
//		assertNotNull("Actions is not null", actions);
//		assertFalse("Actions is not empty", actions.isEmpty());
//		assertTrue("Actions count 2 elements in the list", actions.size() == 2);
//
//		xml = "<template-info></template-info>" ;
//		actions = reader.readActions(DOMUtils.selectSingleNode(readInputStr(xml), TemplateInfo.DESCRIPTOR));
//		assertNull("Actions is null", actions);
//		
//		try {
//			xml = "<template-info><actions></actions></template-info>" ;
//			actions = reader.readActions(DOMUtils.selectSingleNode(readInputStr(xml), TemplateInfo.DESCRIPTOR));
//			fail("ComponentException expected when Actions members is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			actions = reader.readActions(null);
//			fail("ComponentException expected when StyleInfo or TemplateInfo is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadAction() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		Action action = null ;
//
//		xml = "<action id=\"2\" type=\"PROCESS\"><class>com.hp.sesame.portal.actions.AdminRegisterProcessAction</class></action>" ;
//		action = reader.readAction(DOMUtils.selectSingleNode(readInputStr(xml), Action.DESCRIPTOR));
//		assertNotNull("Action is not null", action);
//
//		try {
//			xml = "<action id=\"2\" type=\"PROCESS\"></action>" ;
//			action = reader.readAction(DOMUtils.selectSingleNode(readInputStr(xml), Action.DESCRIPTOR));
//			fail("ComponentException expected when Action members is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			xml = "<action><class>com.hp.sesame.portal.actions.AdminRegisterProcessAction</class></action>" ;
//			action = reader.readAction(DOMUtils.selectSingleNode(readInputStr(xml), Action.DESCRIPTOR));
//			fail("ComponentException expected when Action attributes is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			action = reader.readAction(null);
//			fail("ComponentException expected when Action is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadActionClass() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		ActionClass actionClass = null ;
//
//		xml = "<action><class>com.hp.sesame.portal.actions.AdminRegisterProcessAction</class></action>" ;
//		actionClass = reader.readActionClass(DOMUtils.selectSingleNode(readInputStr(xml), Action.DESCRIPTOR));
//		assertNotNull("ActionClass is not null", actionClass);
//
//		xml = "<action><class></class></action>" ;
//		actionClass = reader.readActionClass(DOMUtils.selectSingleNode(readInputStr(xml), Action.DESCRIPTOR));
//		assertNull("ActionClass is null", actionClass);
//		
//		xml = "<action></action>" ;
//		actionClass = reader.readActionClass(DOMUtils.selectSingleNode(readInputStr(xml), Action.DESCRIPTOR));
//		assertNull("ActionClass is null", actionClass);
//		
//		try {
//			actionClass = reader.readActionClass(null);
//			fail("ComponentException expected when Action is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
//
//	public void testReadGroupReference() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		GroupReference groupReference = null ;
//
//		xml = "<actions><group-reference group-id=\"INHERIT_ALL\"/></actions>" ;
//		groupReference = reader.readGroupReference(DOMUtils.selectSingleNode(readInputStr(xml), Actions.DESCRIPTOR));
//		assertNotNull("GroupReference is not null", groupReference);
//
//		xml = "<actions></actions>" ;
//		groupReference = reader.readGroupReference(DOMUtils.selectSingleNode(readInputStr(xml), Actions.DESCRIPTOR));
//		assertNull("GroupReference is null", groupReference);
//		
//		try {
//			xml = "<actions><group-reference/></actions>" ;
//			groupReference = reader.readGroupReference(DOMUtils.selectSingleNode(readInputStr(xml), Actions.DESCRIPTOR));
//			fail("ComponentException expected when GroupReference attributes is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//		
//		try {
//			groupReference = reader.readGroupReference(null);
//			fail("ComponentException expected when Actions is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}

//	public void testReadAttributes() throws Exception {
//		ComponentReader reader = new ComponentReader() ;
//		String xml = null ;
//		Node node = null ;
//		List attributes = null ;
//		Map result = null ;
//		
//		attributes = Arrays.asList(new Object[] { "att", "rib", "ute" }) ;
//		xml = "<test att=\"123\" rib=\"456\" ute=\"789\" />" ;
//		node = DOMUtils.selectSingleNode(readInputStr(xml), "test");
//		result = reader.readAttributes(node, attributes) ;
//		assertNotNull("Attributes map is not null", result) ;
//		assertFalse("Attributes map is not empty", result.isEmpty()) ;
//		assertTrue("Attributes list and map is same size", result.size() == attributes.size()) ;
//		
//		xml = "<test att=\"123\" rib=\"456\" ute=\"789\" next=\"0\" />" ;
//		node = DOMUtils.selectSingleNode(readInputStr(xml), "test");
//		result = reader.readAttributes(node, attributes) ;
//		assertNotNull("Attributes map is not null", result) ;
//		assertFalse("Attributes map is not empty", result.isEmpty()) ;
//		assertTrue("Attributes list and map is same size", result.size() == attributes.size()) ;
//		
//		attributes = Arrays.asList(new Object[] { "att", "rib", "ute", "next" }) ;
//		xml = "<test att=\"123\" rib=\"456\" ute=\"789\" />" ;
//		node = DOMUtils.selectSingleNode(readInputStr(xml), "test");
//		result = reader.readAttributes(node, attributes) ;
//		assertNotNull("Attributes map is not null", result) ;
//		assertFalse("Attributes map is not empty", result.isEmpty()) ;
//		assertFalse("Attributes list and map is not same size", result.size() == attributes.size()) ;
//
//		result = reader.readAttributes(node, new ArrayList()) ;
//		assertNotNull("Attributes map is not null", result) ;
//		assertTrue("Attributes map is empty", result.isEmpty()) ;
//
//		result = reader.readAttributes(node, null) ;
//		assertNull("Attributes map is null", result) ;
//
//		try {
//			result = reader.readAttributes(null, attributes) ;
//			fail("ComponentException expected when Node is null");
//		} catch (ComponentException e) {
//			BadComponent badComponent = e.getBadComponent();
//			assertNotNull("BadComponent is not null", badComponent);
//		}
//	}
	
	public void testRetrieveComponentsByType() throws Exception {
		ComponentReader reader = new ComponentReader() ;
		List componentList = null ;
		List result = null ;
		String xml = null ;
		Component component = null ;
		BadComponent badComponent = null ;

		// ### first test ###
		componentList = new ArrayList() ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"ModuleTypes\"><epideploy:detail><module-config auto-deploy=\"true\"><descriptor-id>abc</descriptor-id><locale-key>123</locale-key><web-deployment-path>test-folder</web-deployment-path></module-config></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"def\" component-type=\"ModuleTypes\"><epideploy:detail><module-config auto-deploy=\"true\"><descriptor-id>def</descriptor-id><locale-key>456</locale-key><web-deployment-path>test-folder</web-deployment-path></module-config></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"ghi\" component-type=\"Grids\"><epideploy:detail><style-info id=\"ghi\"/></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"jkl\" component-type=\"Styles\"><epideploy:detail><style-info id=\"jkl\"/></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"mno\" component-type=\"ModuleTypes\"><epideploy:detail><module-config auto-deploy=\"true\"><descriptor-id>mno</descriptor-id><locale-key>789</locale-key><web-deployment-path>test-folder</web-deployment-path></module-config></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		result = reader.retrieveComponentsByType(componentList, Component.MODULETYPES);
		assertNotNull("Result list is not null", result);
		assertFalse("Result list is not empty", result.isEmpty());
		assertTrue("Result list count 3 elements", result.size() == 3);
		
		result = reader.retrieveComponentsByType(componentList, Component.GRIDS);
		assertNotNull("Result list is not null", result);
		assertFalse("Result list is not empty", result.isEmpty());
		assertTrue("Result list count only 1 element", result.size() == 1);
		
		// ### second test ###
		componentList = new ArrayList() ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"ModuleTypes\"><epideploy:detail><module-config auto-deploy=\"true\"><descriptor-id>abc</descriptor-id><locale-key>123</locale-key><web-deployment-path>test-folder</web-deployment-path></module-config></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		badComponent = new BadComponent("test BadComponent") ;
		componentList.add(badComponent) ;
		
		xml = "<epideploy:component component-id=\"ghi\" component-type=\"Grids\"><epideploy:detail><style-info id=\"ghi\"/></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"jkl\" component-type=\"Styles\"><epideploy:detail><style-info id=\"jkl\"/></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"mno\" component-type=\"ModuleTypes\"><epideploy:detail><module-config auto-deploy=\"true\"><descriptor-id>mno</descriptor-id><locale-key>789</locale-key><web-deployment-path>test-folder</web-deployment-path></module-config></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		result = reader.retrieveComponentsByType(componentList, Component.MODULETYPES);
		assertNotNull("Result list is not null", result);
		assertFalse("Result list is not empty", result.isEmpty());
		assertTrue("Result list count 3 elements (2 ModuleTypes + 1 BadComponent)", result.size() == 3);
		
		// ### third test ###
		try {
			result = reader.retrieveComponentsByType(null, "");
			fail("NullPointerException expected when Component list is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}
		
		try {
			result = reader.retrieveComponentsByType(new ArrayList(), null);
			fail("NullPointerException expected when Component type is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}
		
		try {
			result = reader.retrieveComponentsByType(new ArrayList(), "for_the_test");
			fail("IllegalArgumentException expected when Component type is not valid");
		} catch (IllegalArgumentException e) {
			// this is expected
			assertTrue(true);
		}
	}

	public void testRetrievePortlets() throws Exception {
		ComponentReader reader = new ComponentReader() ;
		List componentList = null ;
		List result = null ;
		String xml = null ;
		Component component = null ;
		BadComponent badComponent = null ;

		// ### first test ###
		componentList = new ArrayList() ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"LOCAL\" service-provider-id=\"def\" service-provider-name=\"JSR\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"REMOTE\" service-provider-id=\"def\" service-provider-name=\"WSRP\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"REMOTE\" service-provider-id=\"def\" service-provider-name=\"WSRP\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"LOCAL\" service-provider-id=\"def\" service-provider-name=\"PORTALBEAN\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"REMOTE\" service-provider-id=\"def\" service-provider-name=\"WSRP\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		result = reader.retrievePortlets(componentList, PortletInstance.WSRP_PORTLET);
		assertNotNull("Result list is not null", result);
		assertFalse("Result list is not empty", result.isEmpty());
		assertTrue("Result list count 3 elements", result.size() == 3);
		
		result = reader.retrievePortlets(componentList, PortletInstance.PORTAL_BEAN);
		assertNotNull("Result list is not null", result);
		assertFalse("Result list is not empty", result.isEmpty());
		assertTrue("Result list count only 1 element", result.size() == 1);
		
		// ### second test ###
		componentList = new ArrayList() ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"LOCAL\" service-provider-id=\"def\" service-provider-name=\"JSR\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		badComponent = new BadComponent("test BadComponent") ;
		componentList.add(badComponent) ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"REMOTE\" service-provider-id=\"def\" service-provider-name=\"WSRP\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"LOCAL\" service-provider-id=\"def\" service-provider-name=\"PORTALBEAN\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"REMOTE\" service-provider-id=\"def\" service-provider-name=\"WSRP\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		
		result = reader.retrievePortlets(componentList, PortletInstance.WSRP_PORTLET);
		assertNotNull("Result list is not null", result);
		assertFalse("Result list is not empty", result.isEmpty());
		assertTrue("Result list count 3 elements (2 WSRP Portlets + 1 BadComponent)", result.size() == 3);
		
		// ### third test ###
		componentList = new ArrayList() ;
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance service-provider-id=\"def\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		result = reader.retrievePortlets(componentList, PortletInstance.WSRP_PORTLET);
		assertNotNull("Result list is not null", result);
		assertTrue("Result list contain 1 BadComponent", result.size() == 1);
		assertTrue("Return a BadComponent because Kind and/or ServiceProviderName are null", result.get(0) instanceof BadComponent);

		componentList = new ArrayList() ;
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"REMOTE\" service-provider-id=\"def\" service-provider-name=\"\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		result = reader.retrievePortlets(componentList, PortletInstance.WSRP_PORTLET);
		assertNotNull("Result list is not null", result);
		assertTrue("Result list contain 1 BadComponent", result.size() == 1);
		assertTrue("Return a BadComponent because ServiceProviderName is empty", result.get(0) instanceof BadComponent);

		componentList = new ArrayList() ;
		xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"\" service-provider-id=\"def\" service-provider-name=\"WSRP\"><properties/></portlet-instance></epideploy:detail></epideploy:component>" ;
		component = new Component(readInputStr(xml)) ;
		componentList.add(component) ;
		result = reader.retrievePortlets(componentList, PortletInstance.WSRP_PORTLET);
		assertNotNull("Result list is not null", result);
		assertTrue("Result list contain 1 BadComponent", result.size() == 1);
		assertTrue("Return a BadComponent because Kind is empty", result.get(0) instanceof BadComponent);

		// ### fourth test ###
		try {
			result = reader.retrievePortlets(null, "");
			fail("NullPointerException expected when Component list is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}
		
		try {
			result = reader.retrievePortlets(new ArrayList(), null);
			fail("NullPointerException expected when Portlet type is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}
		
		try {
			result = reader.retrievePortlets(new ArrayList(), "for_the_test");
			fail("IllegalArgumentException expected when Portlet type is not valid");
		} catch (IllegalArgumentException e) {
			// this is expected
			assertTrue(true);
		}
	}

}

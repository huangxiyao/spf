package com.hp.spp.wsrp.export.component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.hp.spp.wsrp.export.component.mapping.Component;
import com.hp.spp.wsrp.export.component.mapping.Details;
import com.hp.spp.wsrp.export.component.mapping.PortletInstance;
import com.hp.spp.wsrp.export.exception.ComponentException;
import com.hp.spp.wsrp.export.util.DOMUtils;
import com.hp.spp.wsrp.export.util.car.CarEntry;
import com.hp.spp.wsrp.export.util.car.CarFile;

public class ComponentReader {
	
	public List retrievePortlets(List components, String portletType) {
		if(components == null)
			throw new NullPointerException("No component list is found for the research");
		if(portletType == null)
			throw new NullPointerException("No portlet type is found for the research");
		else if(!PortletInstance.LIST_OF_TYPES.contains(portletType))
			throw new IllegalArgumentException("Portlet type is not valid");
		
		List portlets = new ArrayList() ;
		Component component = null ;
		
		String kind = null ;
		if(portletType.equals(PortletInstance.WSRP_PORTLET))
			kind = PortletInstance.KIND_REMOTE ;
		else
			kind = PortletInstance.KIND_LOCAL ;
		
		int i = 0 ;
		for(Iterator iterator = components.iterator(); iterator.hasNext(); ) {
			Object object = iterator.next() ;
			if(object instanceof Component) {
				component = (Component) object ;
				
				if(component == null) {
					portlets.add(new BadComponent("Component "+i+" is NULL!"));
				} else {
					Details details = component.getDetails() ;
					if(details == null) {
						portlets.add(new BadComponent("Component "+component.getComponentId()+" doesn't have any Details object!")) ;
					} else {
						PortletInstance portletInstance = details.getPortletInstance() ;
						if(portletInstance == null) {
							portlets.add(new BadComponent("Component "+component.getComponentId()+" is not a Portlet!")) ;
						} else {
							String componentKind = portletInstance.getKind() ;
							String componentProvider = portletInstance.getServiceProviderName() ;
							
							if(kind.equals(componentKind) && portletType.equals(componentProvider)) {
								portlets.add(component) ;
							} else if(componentKind == null || componentProvider == null 
									|| "".equals(componentKind) || "".equals(componentProvider)) {
								portlets.add(new BadComponent("Component "+component.getComponentId()+", Kind and ServiceProvider don't match: Kind="+componentKind+", ServiceProvider="+componentProvider+"!")) ;
							}
						}
					}
				}
			} else if(object instanceof BadComponent) {
				portlets.add(object);
			} else {
				portlets.add(new BadComponent("Component "+i+" is NULL!"));
			}
			
			i++ ;
		}
		
		return portlets;
	}

	public List retrieveComponentsByType(List components, String componentType) {
		if(components == null)
			throw new NullPointerException("No component list is found for the research");
		if(componentType == null)
			throw new NullPointerException("No component type is found for the research");
		else if(!Component.LIST_OF_TYPES.contains(componentType))
			throw new IllegalArgumentException("Component type is not valid");
		
		List specific = new ArrayList() ;
		Component component = null ;
		
		int i = 0 ;
		for(Iterator iterator = components.iterator(); iterator.hasNext(); ) {
			Object object = iterator.next() ;
			if(object instanceof Component) {
				component = (Component) object ;
				
				if(component == null) {
					// Do nothing...
				} else if(componentType.equals(component.getComponentType())) {
					specific.add(component) ;
//				} else {
//					System.out.println(component.getComponentType());
				}
			} else if(object instanceof BadComponent) {
				specific.add(object);
			} else {
				specific.add(new BadComponent("Component "+i+" is NULL!"));
			}
			
			i++ ;
		}
		
		return specific;
	}

	public Component readComponentFromCarFile(CarFile carFile, CarEntry carEntry) throws ComponentException {
		try {
			return readComponentFromStream(carFile.getInputStream(carEntry));
		} catch (IOException e) {
			throw new ComponentException("IOException generate by the CarEntry on the CarFile.", e);
		}
	}

	public Component readComponentFromStream(InputStream inputStream) throws ComponentException {
		Document doc = null;
		try {
			doc = DOMUtils.initializeDocument(inputStream);
		} catch (ParserConfigurationException e) {
			throw new ComponentException("ParserConfigurationException encountered while creating DocumentBuilder.", e);
		} catch (SAXException e) {
			throw new ComponentException("SAXException encountered while creating Document.", e);
		} catch (IOException e) {
			throw new ComponentException("IOException encountered while creating Document.", e);
		}
		return new Component(doc);
//		return readComponent(doc);
	}

//	Component readComponent(Document doc) throws ComponentException {
//		if(doc == null)
//			throw new ComponentException(new BadComponent("Document is NULL!"));
//
//		Component component = null ;
//		Node node = DOMUtils.selectSingleNode(doc, Component.DESCRIPTOR);
//
//		if(node != null) {
//			Map attributes = null ;
//			if (!Component.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, Component.ATTRIBUTES);
//			
//			Map members = null ;
//			if(!Component.MEMBERS.isEmpty()) {
//				members = new HashMap() ;
//				for(Iterator it = Component.MEMBERS.iterator(); it.hasNext(); ) {
//					String descriptor = (String) it.next() ;
//					
//					if(descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
//					} else if(RequiredComponent.DESCRIPTOR.equals(descriptor)) {
//						RequiredComponent requiredComponent = readRequiredComponent(node);
//						if(requiredComponent != null)
//							members.put(descriptor, requiredComponent);
//					} else if(Details.DESCRIPTOR.equals(descriptor)) {
//						Details details = readDetails(node);
//						if(details != null)
//							members.put(descriptor, details);
//					}
//				}
//			}
//
//			if(attributes != null && !attributes.isEmpty() 
//					&& members != null && !members.isEmpty()) {
//				component = new Component() ;
//				component.initAttributes(attributes) ;
//				component.initMembers(members) ;
//			} else {
//				throw new ComponentException(new BadComponent("Component attributes or members is NULL!"));
//			}
//		} else {
//			throw new ComponentException(new BadComponent("Root node is NULL!"));
//		}
//		
//		return component;
//	}
//
//	RequiredComponent readRequiredComponent(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("? is NULL!"));
//
//		RequiredComponent requiredComponent = null ;
//		node = DOMUtils.selectSingleNode(node, RequiredComponent.DESCRIPTOR) ;
//		
//		if(node != null) {
//			Map attributes = null ;
//			if (!RequiredComponent.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, RequiredComponent.ATTRIBUTES);
//			
//			Map members = null ;
//			if(!RequiredComponent.MEMBERS.isEmpty()) {
//				members = new HashMap() ;
//				for(Iterator it = RequiredComponent.MEMBERS.iterator(); it.hasNext(); ) {
//					String descriptor = (String) it.next() ;
//					
//					if(descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
////					} else if(Properties.DESCRIPTOR.equals(descriptor)) {
////						Properties properties = readProperties(node);
////						if(properties != null)
////							members.put(descriptor, properties);
//					}
//				}
//			}
//
//			if(attributes != null && !attributes.isEmpty() 
//					|| members != null && !members.isEmpty()) {
//				requiredComponent = new RequiredComponent() ;
//				requiredComponent.initAttributes(attributes) ;
//				requiredComponent.initMembers(members) ;
//			} else {
//				throw new ComponentException(new BadComponent("RequiredComponent attributes is NULL!"));
//			}
//		}
//		
//		return requiredComponent ;
//	}
//
//	Details readDetails(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("? is NULL!"));
//
//		Details details = null ;
//		node = DOMUtils.selectSingleNode(node, Details.DESCRIPTOR) ;
//		
//		if(node != null) {
//			Map attributes = null ;
//			if (!Details.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, Details.ATTRIBUTES);
//			
//			Map members = null ;
//			if(!Details.MEMBERS.isEmpty()) {
//				members = new HashMap() ;
//				for(Iterator it = Details.MEMBERS.iterator(); it.hasNext(); ) {
//					String descriptor = (String) it.next() ;
//					
//					if(descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
//					} else if(ModuleConfig.DESCRIPTOR.equals(descriptor)) {
//						ModuleConfig moduleConfig = readModuleConfig(node);
//						if(moduleConfig != null)
//							members.put(descriptor, moduleConfig);
//					} else if(PortletInstance.DESCRIPTOR.equals(descriptor)) {
//						PortletInstance portletInstance = readPortletInstance(node);
//						if(portletInstance != null)
//							members.put(descriptor, portletInstance);
//					} else if(StyleInfo.DESCRIPTOR.equals(descriptor)) {
//						StyleInfo styleInfo = readStyleInfo(node);
//						if(styleInfo != null)
//							members.put(descriptor, styleInfo);
//					} else if(TemplateInfo.DESCRIPTOR.equals(descriptor)) {
//						TemplateInfo templateInfo = readTemplateInfo(node);
//						if(templateInfo != null)
//							members.put(descriptor, templateInfo);
//					}
//				}
//			}
//			
//			if(members != null && (members.isEmpty() || members.size() > 1))
//				throw new ComponentException(new BadComponent(Details.DESCRIPTOR + " is not correctly build!")) ;
//
//			if(attributes != null && !attributes.isEmpty() 
//					|| members != null && !members.isEmpty()) {
//				details = new Details() ;
//				details.initAttributes(attributes) ;
//				details.initMembers(members) ;
//			}
//		}
//		
//		return details ;
//	}
//
//	ModuleConfig readModuleConfig(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("Details is NULL!"));
//
//		ModuleConfig moduleConfig = null ;
//		node = DOMUtils.selectSingleNode(node, ModuleConfig.DESCRIPTOR) ;
//		
//		if(node != null) {
//			Map attributes = null ;
//			if (!ModuleConfig.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, ModuleConfig.ATTRIBUTES);
//			
//			Map members = null ;
//			if(!ModuleConfig.MEMBERS.isEmpty()) {
//				members = new HashMap() ;
//				for(Iterator it = ModuleConfig.MEMBERS.iterator(); it.hasNext(); ) {
//					String descriptor = (String) it.next() ;
//					
//					if(descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
//					} else if(DescriptorId.DESCRIPTOR.equals(descriptor)) {
//						DescriptorId descriptorId = readDescriptorId(node);
//						if(descriptorId != null)
//							members.put(descriptor, descriptorId);
//					} else if(WebDeploymentPath.DESCRIPTOR.equals(descriptor)) {
//						WebDeploymentPath deploymentPath = readWebDeploymentPath(node);
//						if(deploymentPath != null)
//							members.put(descriptor, deploymentPath);
//					} else if(LocaleKey.DESCRIPTOR.equals(descriptor)) {
//						LocaleKey localeKey = readLocaleKey(node);
//						if(localeKey != null)
//							members.put(descriptor, localeKey);
//					}
//				}
//			}
//			
//			if(members != null && members.size() < 3)
//				throw new ComponentException(
//						new BadComponent(
//								DescriptorId.DESCRIPTOR + ", " 
//								+ WebDeploymentPath.DESCRIPTOR + " or " 
//								+ LocaleKey.DESCRIPTOR + " is missing. " 
//								+ ModuleConfig.DESCRIPTOR+" is not correctly build!")
//						) ;
//
//			if(attributes != null && !attributes.isEmpty() 
//					|| members != null && !members.isEmpty()) {
//				moduleConfig = new ModuleConfig() ;
//				moduleConfig.initAttributes(attributes) ;
//				moduleConfig.initMembers(members) ;
//			}
//		}
//		
//		return moduleConfig;
//	}
//
//	DescriptorId readDescriptorId(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("ModuleConfig is NULL!"));
//
//		DescriptorId descriptorId = null ;
//		node = DOMUtils.selectSingleNode(node, DescriptorId.DESCRIPTOR) ;
//		
//		if(node != null) {
//			String value = DOMUtils.getStringValue(node) ;
//			if(value != null && !"".equals(value)) {
//				descriptorId = new DescriptorId() ;
//				descriptorId.setValue(value) ;
//			}
//		}
//		
//		return descriptorId;
//	}
//
//	WebDeploymentPath readWebDeploymentPath(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("ModuleConfig is NULL!"));
//
//		WebDeploymentPath deploymentPath = null ;
//		node = DOMUtils.selectSingleNode(node, WebDeploymentPath.DESCRIPTOR) ;
//		
//		if(node != null) {
//			String value = DOMUtils.getStringValue(node) ;
//			if(value != null && !"".equals(value)) {
//				deploymentPath = new WebDeploymentPath() ;
//				deploymentPath.setValue(value) ;
//			}
//		}
//		
//		return deploymentPath;
//	}
//
//	LocaleKey readLocaleKey(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("ModuleConfig is NULL!"));
//
//		LocaleKey localeKey = null ;
//		node = DOMUtils.selectSingleNode(node, LocaleKey.DESCRIPTOR) ;
//		
//		if(node != null) {
//			String value = DOMUtils.getStringValue(node) ;
//			if(value != null && !"".equals(value)) {
//				localeKey = new LocaleKey() ;
//				localeKey.setValue(value) ;
//			}
//		}
//		
//		return localeKey;
//	}
//
//	PortletInstance readPortletInstance(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("Details is NULL!"));
//
//		PortletInstance portletInstance = null ;
//		node = DOMUtils.selectSingleNode(node, PortletInstance.DESCRIPTOR) ;
//		
//		if(node != null) {
//			Map attributes = null ;
//			if (!PortletInstance.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, PortletInstance.ATTRIBUTES);
//			
//			Map members = null ;
//			if(!PortletInstance.MEMBERS.isEmpty()) {
//				members = new HashMap() ;
//				for(Iterator it = PortletInstance.MEMBERS.iterator(); it.hasNext(); ) {
//					String descriptor = (String) it.next() ;
//					
//					if(descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
////					} else if(Properties.DESCRIPTOR.equals(descriptor)) {
////						Properties properties = readProperties(node);
////						if(properties != null)
////							members.put(descriptor, properties);
//					}
//				}
//			}
//
//			if(attributes != null && !attributes.isEmpty() 
//					/*|| members != null && !members.isEmpty()*/) {
//				portletInstance = new PortletInstance() ;
//				portletInstance.initAttributes(attributes) ;
//				portletInstance.initMembers(members) ;
//			} else {
//				throw new ComponentException(new BadComponent("PortletInstance attributes is NULL!"));
//			}
//		}
//
//		return portletInstance;
//	}
//
//	StyleInfo readStyleInfo(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("Details is NULL!"));
//
//		StyleInfo styleInfo = null ;
//		node = DOMUtils.selectSingleNode(node, StyleInfo.DESCRIPTOR) ;
//		
//		if(node != null) {
//			Map attributes = null ;
//			if (!StyleInfo.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, StyleInfo.ATTRIBUTES);
//			
//			Map members = null ;
//			if(!StyleInfo.MEMBERS.isEmpty()) {
//				members = new HashMap() ;
//				for(Iterator it = StyleInfo.MEMBERS.iterator(); it.hasNext(); ) {
//					String descriptor = (String) it.next() ;
//					
//					if(descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
//					} else if(Actions.DESCRIPTOR.equals(descriptor)) {
//						Actions actions = readActions(node);
//						if(actions != null)
//							members.put(descriptor, actions);
//					}
//				}
//			}
//
//			if(attributes != null && !attributes.isEmpty() 
//					/*|| members != null && !members.isEmpty()*/) {
//				styleInfo = new StyleInfo() ;
//				styleInfo.initAttributes(attributes) ;
//				styleInfo.initMembers(members) ;
//			} else {
//				throw new ComponentException(new BadComponent("StyleInfo attributes is NULL!"));
//			}
//		}
//
//		return styleInfo;
//	}
//
//	TemplateInfo readTemplateInfo(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("Details is NULL!"));
//
//		TemplateInfo templateInfo = null ;
//		node = DOMUtils.selectSingleNode(node, TemplateInfo.DESCRIPTOR) ;
//		
//		if(node != null) {
//			Map attributes = null ;
//			if (!TemplateInfo.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, TemplateInfo.ATTRIBUTES);
//
//			Map members = null ;
//			if(!TemplateInfo.MEMBERS.isEmpty()) {
//				members = new HashMap() ;
//				for(Iterator it = TemplateInfo.MEMBERS.iterator(); it.hasNext(); ) {
//					String descriptor = (String) it.next() ;
//					
//					if(descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
//					} else if(Actions.DESCRIPTOR.equals(descriptor)) {
//						Actions actions = readActions(node);
//						if(actions != null)
//							members.put(descriptor, actions);
//					}
//				}
//			}
//			
//			if(attributes != null && !attributes.isEmpty() 
//					/*|| members != null && !members.isEmpty()*/) {
//				templateInfo = new TemplateInfo() ;
//				templateInfo.initAttributes(attributes) ;
//				templateInfo.initMembers(members) ;
//			} else {
//				throw new ComponentException(new BadComponent("TemplateInfo attributes is NULL!"));
//			}
//		}
//
//		return templateInfo;
//	}
//
//	Actions readActions(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("StyleInfo or TemplateInfo is NULL!"));
//
//		Actions actions = null ;
//		node = DOMUtils.selectSingleNode(node, Actions.DESCRIPTOR) ;
//		
//		if(node != null) {
//			Map attributes = null ;
//			if(!Actions.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, Actions.ATTRIBUTES) ;
//
//			Map members = null ;
//			if (!Actions.MEMBERS.isEmpty()) {
//				members = new HashMap();
//				for (Iterator it = Actions.MEMBERS.iterator(); it.hasNext();) {
//					String descriptor = (String) it.next();
//
//					if (descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
//					} else if (Action.DESCRIPTOR.equals(descriptor)) {
//						Object object = DOMUtils.selectNodes(node, Action.DESCRIPTOR) ;
//						if(object instanceof List) {
//							List tmpList = (List) object ;
//							List actionList = null;
//							
//							Node tmpNode = null ;
//							for(Iterator itList = tmpList.iterator(); itList.hasNext(); ) {
//								tmpNode = (Node) itList.next() ;
//								
//								if(tmpNode != null) {
//									if(actionList == null)
//										actionList = new ArrayList();
//									
//									Action action = readAction(tmpNode) ;
//									if(action != null)
//										actionList.add(action) ;
//								}
//							}
//							tmpNode = null ;
//
//							if(actionList != null)
//								members.put(descriptor, actionList);
//						}
//					} else if (GroupReference.DESCRIPTOR.equals(descriptor)) {
//						GroupReference groupReference = readGroupReference(node);
//						if(groupReference != null)
//							members.put(descriptor, groupReference);
//					}
//				}
//			}
//			
//			if(attributes != null && !attributes.isEmpty() 
//					|| members != null && !members.isEmpty()) {
//				actions = new Actions() ;
//				actions.initAttributes(attributes) ;
//				actions.initMembers(members) ;
//			} else {
//				throw new ComponentException(new BadComponent("Actions attributes or members is NULL!"));
//			}
//		}
//		
//		return actions;
//	}
//
//	Action readAction(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("Action is NULL!"));
//
//		Action action = null ;
////		node = DOMUtils.selectSingleNode(node, Action.DESCRIPTOR)) ;
//		
//		if(node != null) {
//			Map attributes = null ;
//			if(!Action.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, Action.ATTRIBUTES) ;
//
//			Map members = null ;
//			if (!Action.MEMBERS.isEmpty()) {
//				members = new HashMap();
//				for (Iterator it = Action.MEMBERS.iterator(); it.hasNext();) {
//					String descriptor = (String) it.next();
//
//					if (descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
//					} else if (ActionClass.DESCRIPTOR.equals(descriptor)) {
//						ActionClass actionClass = readActionClass(node);
//						if(actionClass != null)
//							members.put(descriptor, actionClass);
//					}
//				}
//			}
//			
//			if(attributes != null && !attributes.isEmpty() 
//					&& members != null && !members.isEmpty()) {
//				action = new Action() ;
//				action.initAttributes(attributes) ;
//				action.initMembers(members) ;
//			} else {
//				throw new ComponentException(new BadComponent("Action attributes or members is NULL!"));
//			}
//		}
//		
//		return action;
//	}
//
//	ActionClass readActionClass(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("Action is NULL!"));
//
//		ActionClass actionClass = null ;
//		node = DOMUtils.selectSingleNode(node, ActionClass.DESCRIPTOR) ;
//		
//		if(node != null) {
//			String value = DOMUtils.getStringValue(node) ;
//			if(value != null && !"".equals(value)) {
//				actionClass = new ActionClass() ;
//				actionClass.setValue(value) ;
//			}
//		}
//		
//		return actionClass;
//	}
//
//	GroupReference readGroupReference(Node node) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("Actions is NULL!"));
//
//		GroupReference groupReference = null ;
//		node = DOMUtils.selectSingleNode(node, GroupReference.DESCRIPTOR) ;
//		
//		if(node != null) {
//			Map attributes = null ;
//			if(!GroupReference.ATTRIBUTES.isEmpty())
//				attributes = readAttributes(node, GroupReference.ATTRIBUTES) ;
//
//			Map members = null ;
//			if (!GroupReference.MEMBERS.isEmpty()) {
//				members = new HashMap();
//				for (Iterator it = GroupReference.MEMBERS.iterator(); it.hasNext();) {
//					String descriptor = (String) it.next();
//
//					if (descriptor == null || "".equals(descriptor)) {
//						// Do nothing...
////					} else if (Action.DESCRIPTOR.equals(descriptor)) {
////						Action action = readAction(node) ;
////						if(action != null)
////							members.put(descriptor, action);
//					}
//				}
//			}
//			
//			if(attributes != null && !attributes.isEmpty() 
//					/*|| members != null && !members.isEmpty()*/) {
//				groupReference = new GroupReference() ;
//				groupReference.initAttributes(attributes) ;
//				groupReference.initMembers(members) ;
//			} else {
//				throw new ComponentException(new BadComponent("GroupReference attributes is NULL!"));
//			}
//		}
//		
//		return groupReference;
//	}
//
//	Map readAttributes(Node node, List attributes) throws ComponentException {
//		if(node == null)
//			throw new ComponentException(new BadComponent("Cannot search attributes on a null node"));
//
//		Element element = null ;
//		if(node instanceof Element)
//			element = (Element) node ;
//			
//		Map attrMap = null ;
//		if(attributes != null) {
//			attrMap = new HashMap() ;
//		
//			for(Iterator iterator = attributes.iterator(); iterator.hasNext(); ) {
//				String attribute = (String) iterator.next() ;
//				String attributeValue = DOMUtils.getAttribute(element, attribute) ;
//				if(attributeValue != null && !"".equals(attributeValue))
//					attrMap.put(attribute, attributeValue) ;
//			}
//		}
//		
//		return attrMap;
//	}

}

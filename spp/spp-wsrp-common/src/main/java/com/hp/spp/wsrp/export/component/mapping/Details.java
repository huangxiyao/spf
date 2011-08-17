package com.hp.spp.wsrp.export.component.mapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.hp.spp.wsrp.export.component.BadComponent;
import com.hp.spp.wsrp.export.exception.ComponentException;
import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class Details extends AbstractComponentElement {

	public static final String DESCRIPTOR = "epideploy:detail" ;
	public static final boolean FINAL_NODE = false ;
	
	public static final List ATTRIBUTES = Arrays.asList(new Object[] {}) ;

	public static final List MEMBERS = Arrays.asList(
			new Object[] {
					PortletInstance.DESCRIPTOR,
					ModuleConfig.DESCRIPTOR,
					StyleInfo.DESCRIPTOR,
					TemplateInfo.DESCRIPTOR
			}) ;

	private PortletInstance mPortletInstance = null ;
	private ModuleConfig mModuleConfig = null ;
	private StyleInfo mStyleInfo = null ;
	private TemplateInfo mTemplateInfo = null ;
	
//	public Details() {
//		
//	}

	Details(Node node) throws ComponentException, BadConstructorException {
		if(node == null)
			throw new ComponentException(new BadComponent("? is NULL!"));

		node = DOMUtils.selectSingleNode(node, DESCRIPTOR) ;
		
		if(node == null) {
			throw new BadConstructorException() ;
		} else {
			Map attributes = null ;
			if (!ATTRIBUTES.isEmpty())
				attributes = readAttributes(node, ATTRIBUTES);
			
			Map members = null ;
			if(!MEMBERS.isEmpty()) {
				members = new HashMap() ;
				for(Iterator it = MEMBERS.iterator(); it.hasNext(); ) {
					String descriptor = (String) it.next() ;
					
					if(descriptor == null || "".equals(descriptor)) {
						// Do nothing...
					} else if(ModuleConfig.DESCRIPTOR.equals(descriptor)) {
						try {
							ModuleConfig moduleConfig = new ModuleConfig(node);
							members.put(descriptor, moduleConfig);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					} else if(PortletInstance.DESCRIPTOR.equals(descriptor)) {
						try {
							PortletInstance portletInstance = new PortletInstance(node);
							members.put(descriptor, portletInstance);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					} else if(StyleInfo.DESCRIPTOR.equals(descriptor)) {
						try {
							StyleInfo styleInfo = new StyleInfo(node);
							members.put(descriptor, styleInfo);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					} else if(TemplateInfo.DESCRIPTOR.equals(descriptor)) {
						try {
							TemplateInfo templateInfo = new TemplateInfo(node);
							members.put(descriptor, templateInfo);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					}
				}
			}
			
			if(members != null && (members.isEmpty() || members.size() > 1))
				throw new ComponentException(new BadComponent(Details.DESCRIPTOR + " is not correctly build!")) ;

			if(attributes != null && !attributes.isEmpty() 
					|| members != null && !members.isEmpty()) {
				initAttributes(attributes) ;
				initMembers(members) ;
			}
		}
	}

	void initAttributes(Map attributes) {

	}
	
	void initMembers(Map members) {
		if(members != null) {
			for(Iterator iterator = members.entrySet().iterator(); iterator.hasNext(); ) {
				Map.Entry param = (Map.Entry) iterator.next() ;
				String key = (String) param.getKey() ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if(PortletInstance.DESCRIPTOR.equals(key)) {
					setPortletInstance((PortletInstance) param.getValue()) ;
				} else if(ModuleConfig.DESCRIPTOR.equals(key)) {
					setModuleConfig((ModuleConfig) param.getValue()) ;
				} else if(StyleInfo.DESCRIPTOR.equals(key)) {
					setStyleInfo((StyleInfo) param.getValue()) ;
				} else if(TemplateInfo.DESCRIPTOR.equals(key)) {
					setTemplateInfo((TemplateInfo) param.getValue()) ;
				}
			}
		}
	}
	
	public PortletInstance getPortletInstance() {
		return mPortletInstance;
	}
	
	private void setPortletInstance(PortletInstance portletInstance) {
		mPortletInstance = portletInstance;
	}
	
	public StyleInfo getStyleInfo() {
		return mStyleInfo;
	}
	
	private void setStyleInfo(StyleInfo styleInfo) {
		mStyleInfo = styleInfo;
	}
	
	public TemplateInfo getTemplateInfo() {
		return mTemplateInfo;
	}
	
	private void setTemplateInfo(TemplateInfo templateInfo) {
		mTemplateInfo = templateInfo;
	}

	public ModuleConfig getModuleConfig() {
		return mModuleConfig;
	}

	private void setModuleConfig(ModuleConfig moduleConfig) {
		mModuleConfig = moduleConfig;
	}

	private String getValue() {
		return null;
	}

	public Element toElement(Document doc) {
		Element root = null;
		
		if(FINAL_NODE) {
			root = DOMUtils.createTextNode(doc, DESCRIPTOR, getValue()) ;
		} else {
			root = doc.createElement(DESCRIPTOR);
			
			for(Iterator it = ATTRIBUTES.iterator(); it.hasNext(); ) {
				// Do nothing...
			}
			
			for(Iterator it = MEMBERS.iterator(); it.hasNext(); ) {
				String key = (String) it.next() ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if(PortletInstance.DESCRIPTOR.equals(key)) {
					PortletInstance portletInstance = getPortletInstance() ;
					if(portletInstance != null)
						root.appendChild(portletInstance.toElement(doc)) ;
				} else if(ModuleConfig.DESCRIPTOR.equals(key)) {
					ModuleConfig moduleConfig = getModuleConfig() ;
					if(moduleConfig != null)
						root.appendChild(moduleConfig.toElement(doc)) ;
				} else if(StyleInfo.DESCRIPTOR.equals(key)) {
					StyleInfo styleInfo = getStyleInfo() ;
					if(styleInfo != null)
						root.appendChild(styleInfo.toElement(doc)) ;
				} else if(TemplateInfo.DESCRIPTOR.equals(key)) {
					TemplateInfo templateInfo = getTemplateInfo() ;
					if(templateInfo != null)
						root.appendChild(templateInfo.toElement(doc)) ;
				}
			}
		}
		
		return root;
	}

}

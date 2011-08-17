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

public class ModuleConfig extends AbstractComponentElement {

	public static final String DESCRIPTOR = "module-config" ;
	public static final boolean FINAL_NODE = false ;

	public static final List ATTRIBUTES = Arrays.asList(
			new Object[] {
					"auto-deploy" // boolean
			}) ;
	
	public static final List MEMBERS = Arrays.asList(
			new Object[] {
					DescriptorId.DESCRIPTOR,
					WebDeploymentPath.DESCRIPTOR,
					LocaleKey.DESCRIPTOR
			}) ;

	private boolean mAutoDeploy ;
	private DescriptorId mDescriptorId = null ;
	private WebDeploymentPath mWebDeploymentPath = null ;
	private LocaleKey mLocaleKey = null ;
	
//	public ModuleConfig() {
//		
//	}

	ModuleConfig(Node node) throws ComponentException, BadConstructorException {
		if(node == null)
			throw new ComponentException(new BadComponent("Details is NULL!"));

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
					} else if(DescriptorId.DESCRIPTOR.equals(descriptor)) {
						try {
							DescriptorId descriptorId = new DescriptorId(node);
							members.put(descriptor, descriptorId);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					} else if(WebDeploymentPath.DESCRIPTOR.equals(descriptor)) {
						try {
							WebDeploymentPath deploymentPath = new WebDeploymentPath(node);
							members.put(descriptor, deploymentPath);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					} else if(LocaleKey.DESCRIPTOR.equals(descriptor)) {
						try {
							LocaleKey localeKey = new LocaleKey(node);
							if(localeKey != null)
								members.put(descriptor, localeKey);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					}
				}
			}
			
			if(members != null && members.size() < 3)
				throw new ComponentException(
						new BadComponent(
								DescriptorId.DESCRIPTOR + ", " 
								+ WebDeploymentPath.DESCRIPTOR + " or " 
								+ LocaleKey.DESCRIPTOR + " is missing. " 
								+ ModuleConfig.DESCRIPTOR+" is not correctly build!")
						) ;

			if(attributes != null && !attributes.isEmpty() 
					|| members != null && !members.isEmpty()) {
				initAttributes(attributes) ;
				initMembers(members) ;
			}
		}
	}

	void initAttributes(Map attributes) {
		if(attributes != null) {
			for(Iterator iterator = attributes.entrySet().iterator(); iterator.hasNext(); ) {
				Map.Entry param = (Map.Entry) iterator.next() ;
				String key = (String) param.getKey() ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if("auto-deploy".equals(key)) {
					setAutoDeploy(Boolean.getBoolean((String) param.getValue())) ;
				}
			}
		}
	}
	
	void initMembers(Map members) {
		if(members != null) {
			for(Iterator iterator = members.entrySet().iterator(); iterator.hasNext(); ) {
				Map.Entry param = (Map.Entry) iterator.next() ;
				String key = (String) param.getKey() ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if(DescriptorId.DESCRIPTOR.equals(key)) {
					setDescriptorId((DescriptorId) param.getValue()) ;
				} else if(WebDeploymentPath.DESCRIPTOR.equals(key)) {
					setWebDeploymentPath((WebDeploymentPath) param.getValue()) ;
				} else if(LocaleKey.DESCRIPTOR.equals(key)) {
					setLocaleKey((LocaleKey) param.getValue()) ;
				}
			}
		}
	}

	public boolean isAutoDeploy() {
		return mAutoDeploy;
	}
	
	private void setAutoDeploy(boolean autoDeploy) {
		mAutoDeploy = autoDeploy;
	}
	
	public DescriptorId getDescriptorId() {
		return mDescriptorId;
	}
	
	private void setDescriptorId(DescriptorId descriptorId) {
		mDescriptorId = descriptorId;
	}
	
	public LocaleKey getLocaleKey() {
		return mLocaleKey;
	}
	
	private void setLocaleKey(LocaleKey localeKey) {
		mLocaleKey = localeKey;
	}
	
	public WebDeploymentPath getWebDeploymentPath() {
		return mWebDeploymentPath;
	}
	
	private void setWebDeploymentPath(WebDeploymentPath webDeploymentPath) {
		mWebDeploymentPath = webDeploymentPath;
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
				String key = (String) it.next() ;
				String value = null ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if("auto-deploy".equals(key)) {
					try {
						value = String.valueOf(isAutoDeploy()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				}
				
				if(value != null)
					root.setAttribute(key, value) ;
			}
			
			for(Iterator it = MEMBERS.iterator(); it.hasNext(); ) {
				String key = (String) it.next() ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if(DescriptorId.DESCRIPTOR.equals(key)) {
					DescriptorId descriptorId = getDescriptorId() ;
					if(descriptorId != null)
						root.appendChild(descriptorId.toElement(doc)) ;
				} else if(WebDeploymentPath.DESCRIPTOR.equals(key)) {
					WebDeploymentPath webDeploymentPath = getWebDeploymentPath() ;
					if(webDeploymentPath != null)
						root.appendChild(webDeploymentPath.toElement(doc)) ;
				} else if(LocaleKey.DESCRIPTOR.equals(key)) {
					LocaleKey localeKey = getLocaleKey() ;
					if(localeKey != null)
						root.appendChild(localeKey.toElement(doc)) ;
				}
			}
		}
		
		return root;
	}
	
}

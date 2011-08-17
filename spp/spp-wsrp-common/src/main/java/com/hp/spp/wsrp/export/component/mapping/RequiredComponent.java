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

public class RequiredComponent extends AbstractComponentElement {

	public static final String DESCRIPTOR = "epideploy:required-component" ;
	public static final boolean FINAL_NODE = false ;

	public static final List ATTRIBUTES = Arrays.asList(
			new Object[] {
					"component-id", 
					"component-type", 
					"major-version", 
					"minor-version" 
			}) ;
	
	public static final List MEMBERS = Arrays.asList(new Object[] {}) ;

	private String mComponentId = null ;
	private String mComponentType = null ;
	
	private int mMajorVersion ;
	private int mMinorVersion ;
	
//	public RequiredComponent() {
//		
//	}

	RequiredComponent(Node node) throws ComponentException, BadConstructorException {
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
					}
				}
			}

			if(attributes != null && !attributes.isEmpty() 
					|| members != null && !members.isEmpty()) {
				initAttributes(attributes) ;
				initMembers(members) ;
			} else {
				throw new ComponentException(new BadComponent("RequiredComponent attributes is NULL!"));
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
				} else if("component-id".equals(key)) {
					setComponentId((String) param.getValue()) ;
				} else if("component-type".equals(key)) {
					setComponentType((String) param.getValue()) ;
				} else if("major-version".equals(key)) {
					setMajorVersion(Integer.parseInt((String) param.getValue())) ;
				} else if("minor-version".equals(key)) {
					setMinorVersion(Integer.parseInt((String) param.getValue())) ;
				}
			}
		}
	}
	
	void initMembers(Map members) {
		
	}

	public String getComponentId() {
		return mComponentId;
	}
	
	private void setComponentId(String componentId) {
		mComponentId = componentId;
	}
	
	public String getComponentType() {
		return mComponentType;
	}
	
	private void setComponentType(String componentType) {
		mComponentType = componentType;
	}
	
	public int getMajorVersion() {
		return mMajorVersion;
	}
	
	private void setMajorVersion(int majorVersion) {
		mMajorVersion = majorVersion;
	}
	
	public int getMinorVersion() {
		return mMinorVersion;
	}
	
	private void setMinorVersion(int minorVersion) {
		mMinorVersion = minorVersion;
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
				} else if("component-id".equals(key)) {
					value = getComponentId() ;
				} else if("component-type".equals(key)) {
					value = getComponentType() ;
				} else if("major-version".equals(key)) {
					try {
						value = String.valueOf(getMajorVersion()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				} else if("minor-version".equals(key)) {
					try {
						value = String.valueOf(getMinorVersion()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				}
				
				if(value != null)
					root.setAttribute(key, value) ;
			}
			
			for(Iterator it = MEMBERS.iterator(); it.hasNext(); ) {
				// Do nothing...
			}
		}
		
		return root;
	}
	
}

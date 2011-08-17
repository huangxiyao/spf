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

public class Component extends AbstractComponentElement implements IComponentTypeConstants {
	
	public static final String DESCRIPTOR = "epideploy:component" ;
	public static final boolean FINAL_NODE = false ;
	
	public static final List ATTRIBUTES = Arrays.asList(
			new Object[] {
					"component-id", // String
					"component-type", // String
					"title", // String
					"description", // String
					"major-version", // int
					"minor-version", // int
					"build-version", // String
					"epi-version", // String
					"epi-build", // String
					"xmlns:epideploy" // String
			}) ;
	
	public static final List MEMBERS = Arrays.asList(
			new Object[] {
					RequiredComponent.DESCRIPTOR, // String
					Details.DESCRIPTOR // String
			}) ;
	
	private String mComponentId = null ;
	private String mComponentType = null ;
	
	private String mTitle = null ;
	private String mDescription = null ;

	private int mMajorVersion ;
	private int mMinorVersion ;
	private String mBuildVersion = null ;

	private String mEpiVersion = null ;
	private String mEpiBuild = null ;

	private String mNameSpace = null ;

	private RequiredComponent mRequiredComponent = null ;
	private Details mDetails = null ;
	
	public Component(Document doc) throws ComponentException {
		if(doc == null)
			throw new ComponentException(new BadComponent("Document is NULL!"));

		Node node = DOMUtils.selectSingleNode(doc, DESCRIPTOR);

		if(node == null) {
			throw new ComponentException(new BadComponent("Root node is NULL!"));
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
					} else if(RequiredComponent.DESCRIPTOR.equals(descriptor)) {
						try {
							RequiredComponent requiredComponent = new RequiredComponent(node);
							members.put(descriptor, requiredComponent);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					} else if(Details.DESCRIPTOR.equals(descriptor)) {
						try {
							Details details = new Details(node);
							members.put(descriptor, details);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					}
				}
			}

			if(attributes != null && !attributes.isEmpty() 
					&& members != null && !members.isEmpty()) {
				initAttributes(attributes) ;
				initMembers(members) ;
			} else {
				throw new ComponentException(new BadComponent("Component attributes or members is NULL!"));
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
				} else if("title".equals(key)) {
					setTitle((String) param.getValue()) ;
				} else if("description".equals(key)) {
					setDescription((String) param.getValue()) ;
				} else if("major-version".equals(key)) {
					setMajorVersion(Integer.parseInt((String) param.getValue())) ;
				} else if("minor-version".equals(key)) {
					setMinorVersion(Integer.parseInt((String) param.getValue())) ;
				} else if("build-version".equals(key)) {
					setBuildVersion((String) param.getValue()) ;
				} else if("epi-version".equals(key)) {
					setEpiVersion((String) param.getValue()) ;
				} else if("epi-build".equals(key)) {
					setEpiBuild((String) param.getValue()) ;
				} else if("xmlns:epideploy".equals(key)) {
					setNameSpace((String) param.getValue()) ;
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
				} else if(RequiredComponent.DESCRIPTOR.equals(key)) {
					setRequiredComponent((RequiredComponent) param.getValue()) ;
				} else if(Details.DESCRIPTOR.equals(key)) {
					setDetails((Details) param.getValue()) ;
				}
			}
		}
	}
	
	public String getBuildVersion() {
		return mBuildVersion;
	}
	
	private void setBuildVersion(String buildVersion) {
		mBuildVersion = buildVersion;
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
	
	public String getDescription() {
		return mDescription;
	}
	
	private void setDescription(String description) {
		mDescription = description;
	}
	
	public Details getDetails() {
		return mDetails;
	}
	
	private void setDetails(Details details) {
		mDetails = details;
	}
	
	public String getEpiBuild() {
		return mEpiBuild;
	}
	
	private void setEpiBuild(String epiBuild) {
		mEpiBuild = epiBuild;
	}
	
	public String getEpiVersion() {
		return mEpiVersion;
	}
	
	private void setEpiVersion(String epiVersion) {
		mEpiVersion = epiVersion;
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
	
	public String getNameSpace() {
		return mNameSpace;
	}
	
	private void setNameSpace(String nameSpace) {
		mNameSpace = nameSpace;
	}
	
	public RequiredComponent getRequiredComponent() {
		return mRequiredComponent;
	}
	
	private void setRequiredComponent(RequiredComponent requiredComponent) {
		mRequiredComponent = requiredComponent;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	private void setTitle(String title) {
		mTitle = title;
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
				} else if("title".equals(key)) {
					value = getTitle() ;
				} else if("description".equals(key)) {
					value = getDescription() ;
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
				} else if("build-version".equals(key)) {
					value = getBuildVersion() ;
				} else if("epi-version".equals(key)) {
					value = getEpiVersion() ;
				} else if("epi-build".equals(key)) {
					value = getEpiBuild() ;
				} else if("xmlns:epideploy".equals(key)) {
					value = getNameSpace() ;
				}
				
				if(value != null)
					root.setAttribute(key, value) ;
			}
			
			for(Iterator it = MEMBERS.iterator(); it.hasNext(); ) {
				String key = (String) it.next() ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if(RequiredComponent.DESCRIPTOR.equals(key)) {
					RequiredComponent requiredComponent = getRequiredComponent() ;
					if(requiredComponent != null)
						root.appendChild(requiredComponent.toElement(doc)) ;
				} else if(Details.DESCRIPTOR.equals(key)) {
					Details details = getDetails() ;
					if(details != null)
						root.appendChild(details.toElement(doc)) ;
				}
			}
		}
		
		return root;
	}
	
}

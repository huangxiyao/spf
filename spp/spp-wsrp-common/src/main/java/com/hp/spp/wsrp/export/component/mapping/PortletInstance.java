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

public class PortletInstance extends AbstractComponentElement implements IPortletConstants {

	public static final String DESCRIPTOR = "portlet-instance" ;
	public static final boolean FINAL_NODE = false ;

	public static final List ATTRIBUTES = Arrays.asList(
			new Object[] {
					"chrome-displayed", // boolean
					"friendly-id", // String
					"kind", // String
					"portlet-species-name", // String
					"published", // boolean
					"selectable", // boolean
					"server-portlet-category", // String
					"service-provider-id", // String
					"service-provider-name", // String
					"timeout", // int
					"width", // int
			}) ;
	
	public static final List MEMBERS = Arrays.asList(
			new Object[] {
					Properties.DESCRIPTOR 
			}) ;

	private boolean mChromeDisplayed ;
	private String mFriendlyId = null ;
	private String mKind = null ;
	private String mPortletSpeciesName = null ;
	private boolean mPublished ;
	private boolean mSelectable ;
	private String mServerPortletCategory = null ;
	private String mServiceProviderId = null ;
	private String mServiceProviderName = null ;
	private int mTimeout ;
	private int mWidth ;
	private Properties mProperties = null ;
	
//	public PortletInstance() {
//		
//	}

	PortletInstance(Node node) throws ComponentException, BadConstructorException {
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
					} else if(Properties.DESCRIPTOR.equals(descriptor)) {
						try {
							Properties properties = new Properties(node);
							members.put(descriptor, properties);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					}
				}
			}

			if(attributes != null && !attributes.isEmpty() 
					/*|| members != null && !members.isEmpty()*/) {
				initAttributes(attributes) ;
				initMembers(members) ;
			} else {
				throw new ComponentException(new BadComponent("PortletInstance attributes is NULL!"));
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
				} else if("chrome-displayed".equals(key)) {
					setChromeDisplayed(Boolean.getBoolean((String) param.getValue())) ;
				} else if("friendly-id".equals(key)) {
					setFriendlyId((String) param.getValue()) ;
				} else if("kind".equals(key)) {
					setKind((String) param.getValue()) ;
				} else if("portlet-species-name".equals(key)) {
					setPortletSpeciesName((String) param.getValue()) ;
				} else if("published".equals(key)) {
					setPublished(Boolean.getBoolean((String) param.getValue())) ;
				} else if("selectable".equals(key)) {
					setSelectable(Boolean.getBoolean((String) param.getValue())) ;
				} else if("server-portlet-category".equals(key)) {
					setServerPortletCategory((String) param.getValue()) ;
				} else if("service-provider-id".equals(key)) {
					setServiceProviderId((String) param.getValue()) ;
				} else if("service-provider-name".equals(key)) {
					setServiceProviderName((String) param.getValue()) ;
				} else if("timeout".equals(key)) {
					setTimeout(Integer.parseInt((String) param.getValue())) ;
				} else if("width".equals(key)) {
					setWidth(Integer.parseInt((String) param.getValue())) ;
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
				} else if(Properties.DESCRIPTOR.equals(key)) {
					setProperties((Properties) param.getValue()) ;
				}
			}
		}
	}

	public boolean isChromeDisplayed() {
		return mChromeDisplayed;
	}
	
	private void setChromeDisplayed(boolean chromeDisplayed) {
		mChromeDisplayed = chromeDisplayed;
	}
	
	public String getFriendlyId() {
		return mFriendlyId;
	}
	
	private void setFriendlyId(String friendlyId) {
		mFriendlyId = friendlyId;
	}
	
	public String getKind() {
		return mKind;
	}
	
	private void setKind(String kind) {
		mKind = kind;
	}
	
	public String getPortletSpeciesName() {
		return mPortletSpeciesName;
	}
	
	private void setPortletSpeciesName(String portletSpeciesName) {
		mPortletSpeciesName = portletSpeciesName;
	}
	
	public boolean isPublished() {
		return mPublished;
	}
	
	private void setPublished(boolean published) {
		mPublished = published;
	}
	
	public boolean isSelectable() {
		return mSelectable;
	}
	
	private void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}
	
	public String getServerPortletCategory() {
		return mServerPortletCategory;
	}
	
	private void setServerPortletCategory(String serverPortletCategory) {
		mServerPortletCategory = serverPortletCategory;
	}
	
	public String getServiceProviderId() {
		return mServiceProviderId;
	}
	
	private void setServiceProviderId(String serviceProviderId) {
		mServiceProviderId = serviceProviderId;
	}
	
	public String getServiceProviderName() {
		return mServiceProviderName;
	}
	
	private void setServiceProviderName(String serviceProviderName) {
		mServiceProviderName = serviceProviderName;
	}
	
	public int getTimeout() {
		return mTimeout;
	}
	
	private void setTimeout(int timeout) {
		mTimeout = timeout;
	}
	
	public int getWidth() {
		return mWidth;
	}
	
	private void setWidth(int width) {
		mWidth = width;
	}

	public Properties getProperties() {
		return mProperties;
	}

	private void setProperties(Properties properties) {
		mProperties = properties;
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
				} else if("chrome-displayed".equals(key)) {
					try {
						value = String.valueOf(isChromeDisplayed()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				} else if("friendly-id".equals(key)) {
					value = getFriendlyId() ;
				} else if("kind".equals(key)) {
					value = getKind() ;
				} else if("portlet-species-name".equals(key)) {
					value = getPortletSpeciesName() ;
				} else if("published".equals(key)) {
					try {
						value = String.valueOf(isPublished()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				} else if("selectable".equals(key)) {
					try {
						value = String.valueOf(isSelectable()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				} else if("server-portlet-category".equals(key)) {
					value = getServerPortletCategory() ;
				} else if("service-provider-id".equals(key)) {
					value = getServiceProviderId() ;
				} else if("service-provider-name".equals(key)) {
					value = getServiceProviderName() ;
				} else if("timeout".equals(key)) {
					try {
						value = String.valueOf(getTimeout()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				} else if("width".equals(key)) {
					try {
						value = String.valueOf(getWidth()) ;
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
				} else if(Properties.DESCRIPTOR.equals(key)) {
					Properties properties = getProperties() ;
					if(properties != null)
						root.appendChild(properties.toElement(doc)) ;
				}
			}
		}
		
		return root;
	}
	
}

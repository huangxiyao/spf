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

public class TemplateInfo extends AbstractComponentElement {

	public static final String DESCRIPTOR = "template-info" ;
	public static final boolean FINAL_NODE = false ;

	public static final List ATTRIBUTES = Arrays.asList(
			new Object[] {
				"allow-guest-access", // boolean
				"description", // String
				"friendly-id", // String
				"header-filename", // String
				"id", // String
				"is-system", // boolean
				"system-secondary-page-type", // boolean
				"title", // String
				"type", // String
				"visible" // boolean
			}) ;

	public static final List MEMBERS = Arrays.asList(
			new Object[] {
					Actions.DESCRIPTOR
			}) ;

	private boolean mAllowGuestAccess ;
	private String mDescription = null ;
	private String mFriendlyId = null ;
	private String mId = null ;
	private String mHeaderFilename = null ;
	private boolean mIsSystem ;
	private boolean mSystemSecondaryPageType ;
	private String mTitle = null ;
	private String mType = null ;
	private boolean mVisible ;
	private Actions mActions ;
	
//	public TemplateInfo() {
//		
//	}

	TemplateInfo(Node node) throws ComponentException, BadConstructorException {
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
					} else if(Actions.DESCRIPTOR.equals(descriptor)) {
						try {
							Actions actions = new Actions(node);
							members.put(descriptor, actions);
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
				throw new ComponentException(new BadComponent("TemplateInfo attributes is NULL!"));
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
				} else if("allow-guest-access".equals(key)) {
					setAllowGuestAccess(Boolean.getBoolean((String) param.getValue())) ;
				} else if("description".equals(key)) {
					setDescription((String) param.getValue()) ;
				} else if("friendly-id".equals(key)) {
					setFriendlyId((String) param.getValue()) ;
				} else if("header-filename".equals(key)) {
					setHeaderFilename((String) param.getValue()) ;
				} else if("id".equals(key)) {
					setId((String) param.getValue()) ;
				} else if("is-system".equals(key)) {
					setIsSystem(Boolean.getBoolean((String) param.getValue())) ;
				} else if("system-secondary-page-type".equals(key)) {
					setSystemSecondaryPageType(Boolean.getBoolean((String) param.getValue())) ;
				} else if("title".equals(key)) {
					setTitle((String) param.getValue()) ;
				} else if("type".equals(key)) {
					setType((String) param.getValue()) ;
				} else if("visible".equals(key)) {
					setVisible(Boolean.getBoolean((String) param.getValue())) ;
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
				} else if(Actions.DESCRIPTOR.equals(key)) {
					setActions((Actions) param.getValue()) ;
				}
			}
		}
	}

	public Actions getActions() {
		return mActions;
	}

	private void setActions(Actions actions) {
		mActions = actions;
	}

	public boolean isAllowGuestAccess() {
		return mAllowGuestAccess;
	}

	private void setAllowGuestAccess(boolean allowGuestAccess) {
		mAllowGuestAccess = allowGuestAccess;
	}

	public String getDescription() {
		return mDescription;
	}

	private void setDescription(String description) {
		mDescription = description;
	}

	public String getFriendlyId() {
		return mFriendlyId;
	}

	private void setFriendlyId(String friendlyId) {
		mFriendlyId = friendlyId;
	}

	public String getHeaderFilename() {
		return mHeaderFilename;
	}

	private void setHeaderFilename(String headerFilename) {
		mHeaderFilename = headerFilename;
	}

	public String getId() {
		return mId;
	}

	private void setId(String id) {
		mId = id;
	}

	public boolean isIsSystem() {
		return mIsSystem;
	}

	private void setIsSystem(boolean isSystem) {
		mIsSystem = isSystem;
	}

	public boolean isSystemSecondaryPageType() {
		return mSystemSecondaryPageType;
	}

	private void setSystemSecondaryPageType(boolean systemSecondaryPageType) {
		mSystemSecondaryPageType = systemSecondaryPageType;
	}

	public String getTitle() {
		return mTitle;
	}

	private void setTitle(String title) {
		mTitle = title;
	}

	public String getType() {
		return mType;
	}

	private void setType(String type) {
		mType = type;
	}

	public boolean isVisible() {
		return mVisible;
	}

	private void setVisible(boolean visible) {
		mVisible = visible;
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
				} else if("allow-guest-access".equals(key)) {
					try {
						value = String.valueOf(isAllowGuestAccess()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				} else if("description".equals(key)) {
					value = getDescription() ;
				} else if("friendly-id".equals(key)) {
					value = getFriendlyId() ;
				} else if("header-filename".equals(key)) {
					value = getHeaderFilename() ;
				} else if("id".equals(key)) {
					value = getId() ;
				} else if("is-system".equals(key)) {
					try {
						value = String.valueOf(isIsSystem()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				} else if("system-secondary-page-type".equals(key)) {
					try {
						value = String.valueOf(isSystemSecondaryPageType()) ;
					} catch (RuntimeException e) {
						value = null ;
					}
				} else if("title".equals(key)) {
					value = getTitle() ;
				} else if("type".equals(key)) {
					value = getType() ;
				} else if("visible".equals(key)) {
					try {
						value = String.valueOf(isVisible()) ;
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
				} else if(Actions.DESCRIPTOR.equals(key)) {
					Actions actions = getActions() ;
					if(actions != null)
						root.appendChild(actions.toElement(doc)) ;
				}
			}
		}
		
		return root;
	}
	
}

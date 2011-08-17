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

public class Action extends AbstractComponentElement implements IActionConstants {

	public static final String DESCRIPTOR = "action" ;
	public static final boolean FINAL_NODE = false ;

	public static final List ATTRIBUTES = Arrays.asList(
			new Object[] {
					"id", // String
					"type" // String
			}) ;

	public static final List MEMBERS = Arrays.asList(
			new Object[] {
					ActionClass.DESCRIPTOR
			}) ;
	
	private String mId = null ;
	private String mType = null ;
	private ActionClass mActionClass = null ;
	
//	public Action() {
//		
//	}

	Action(Node node) throws ComponentException, BadConstructorException {
		if(node == null)
			throw new ComponentException(new BadComponent("Action is NULL!"));

//		node = DOMUtils.selectSingleNode(node, DESCRIPTOR)) ;
		
		if(node == null) {
			throw new BadConstructorException() ;
		} else {
			Map attributes = null ;
			if(!ATTRIBUTES.isEmpty())
				attributes = readAttributes(node, ATTRIBUTES) ;

			Map members = null ;
			if (!MEMBERS.isEmpty()) {
				members = new HashMap();
				for (Iterator it = MEMBERS.iterator(); it.hasNext();) {
					String descriptor = (String) it.next();

					if (descriptor == null || "".equals(descriptor)) {
						// Do nothing...
					} else if (ActionClass.DESCRIPTOR.equals(descriptor)) {
						try {
							ActionClass actionClass = new ActionClass(node);
							members.put(descriptor, actionClass);
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
				throw new ComponentException(new BadComponent("Action attributes or members is NULL!"));
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
				} else if("id".equals(key)) {
					setId((String) param.getValue()) ;
				} else if("type".equals(key)) {
					setType((String) param.getValue()) ;
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
				} else if(ActionClass.DESCRIPTOR.equals(key)) {
					setActionClass((ActionClass) param.getValue()) ;
				}
			}
		}
	}
	
	public ActionClass getActionClass() {
		return mActionClass;
	}
	
	private void setActionClass(ActionClass actionClass) {
		mActionClass = actionClass;
	}
	
	public String getId() {
		return mId;
	}
	
	private void setId(String id) {
		mId = id;
	}
	
	public String getType() {
		return mType;
	}
	
	private void setType(String type) {
		mType = type;
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
				} else if("id".equals(key)) {
					value = getId() ;
				} else if("type".equals(key)) {
					value = getType() ;
				}
				
				if(value != null)
					root.setAttribute(key, value) ;
			}
			
			for(Iterator it = MEMBERS.iterator(); it.hasNext(); ) {
				String key = (String) it.next() ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if(ActionClass.DESCRIPTOR.equals(key)) {
					ActionClass actionClass = getActionClass() ;
					if(actionClass != null)
						root.appendChild(actionClass.toElement(doc)) ;
				}
			}
		}
		
		return root;
	}
	
}

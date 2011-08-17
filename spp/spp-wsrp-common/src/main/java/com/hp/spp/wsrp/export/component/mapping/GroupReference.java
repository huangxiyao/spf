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

public class GroupReference extends AbstractComponentElement {

	public static final String DESCRIPTOR = "group-reference" ;
	public static final boolean FINAL_NODE = false ;

	public static final List ATTRIBUTES = Arrays.asList(
			new Object[] {
					"group-id"
			}) ;
	
	public static final List MEMBERS = Arrays.asList(new Object[] {}) ;
	
	private String mGroupId = null ;

//	public GroupReference() {
//		
//	}

	GroupReference(Node node) throws ComponentException, BadConstructorException {
		if(node == null)
			throw new ComponentException(new BadComponent("Actions is NULL!"));

		node = DOMUtils.selectSingleNode(node, DESCRIPTOR) ;
		
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
					}
				}
			}
			
			if(attributes != null && !attributes.isEmpty() 
					/*|| members != null && !members.isEmpty()*/) {
				initAttributes(attributes) ;
				initMembers(members) ;
			} else {
				throw new ComponentException(new BadComponent("GroupReference attributes is NULL!"));
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
				} else if("group-id".equals(key)) {
					setGroupId((String) param.getValue()) ;
				}
			}
		}
	}
	
	void initMembers(Map members) {
		
	}

	public String getGroupId() {
		return mGroupId;
	}

	private void setGroupId(String groupId) {
		mGroupId = groupId;
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
				} else if("group-id".equals(key)) {
					value = getGroupId() ;
				}
				
				if(value != null)
					root.setAttribute(key, value) ;
			}
			
			for(Iterator it = MEMBERS.iterator(); it.hasNext(); ) {
//				 Do nothing...
			}
		}
		
		return root;
	}
	
}

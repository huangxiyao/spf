package com.hp.spp.wsrp.export.component.mapping;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.hp.spp.wsrp.export.component.BadComponent;
import com.hp.spp.wsrp.export.exception.ComponentException;
import com.hp.spp.wsrp.export.util.DOMUtils;

abstract class AbstractComponentElement {

	Map readAttributes(Node node, List attributes) throws ComponentException {
		if(node == null)
			throw new ComponentException(new BadComponent("Cannot search attributes on a null node"));

		Element element = null ;
		if(node instanceof Element)
			element = (Element) node ;
			
		Map attrMap = null ;
		if(attributes != null) {
			attrMap = new HashMap() ;
		
			for(Iterator iterator = attributes.iterator(); iterator.hasNext(); ) {
				String attribute = (String) iterator.next() ;
				String attributeValue = DOMUtils.getAttribute(element, attribute) ;
				if(attributeValue != null && !"".equals(attributeValue))
					attrMap.put(attribute, attributeValue) ;
			}
		}
		
		return attrMap;
	}
	
	abstract void initAttributes(Map attributes) ;
	
	abstract void initMembers(Map members) ;

	public abstract Element toElement(Document doc) ;

}

package com.hp.spp.wsrp.export.component.mapping;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.hp.spp.wsrp.export.exception.ComponentException;
import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class Properties extends AbstractComponentElement {

	public static final String DESCRIPTOR = "properties" ;
	public static final boolean FINAL_NODE = false ;

	public static final List ATTRIBUTES = Arrays.asList(new Object[] {}) ;
	
	public static final List MEMBERS = Arrays.asList(new Object[] {}) ;
	
//	public Properties() {
//		
//	}

	Properties(Node node) throws ComponentException, BadConstructorException {
		
	}

	void initAttributes(Map attributes) {

	}
	
	void initMembers(Map members) {
		
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
				// Do nothing...
			}
		}
		
		return root;
	}

}

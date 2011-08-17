package com.hp.spp.wsrp.export.component.mapping;

import java.util.Arrays;
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

public class DescriptorId extends AbstractComponentElement {

	public static final String DESCRIPTOR = "descriptor-id" ;
	public static final boolean FINAL_NODE = true ;
	
	public static final List ATTRIBUTES = Arrays.asList(new Object[] {}) ;
	public static final List MEMBERS = Arrays.asList(new Object[] {}) ;

	private String mValue = null ;

//	public DescriptorId() {
//		
//	}

	DescriptorId(Node node) throws ComponentException, BadConstructorException {
		if(node == null)
			throw new ComponentException(new BadComponent("ModuleConfig is NULL!"));

		node = DOMUtils.selectSingleNode(node, DESCRIPTOR) ;
		
		if(node == null) {
			throw new BadConstructorException() ;
		} else {
			String value = DOMUtils.getStringValue(node) ;
			if(value != null && !"".equals(value)) {
				setValue(value) ;
			}
		}
	}

	void initAttributes(Map attributes) {

	}
	
	void initMembers(Map members) {
		
	}
	
	public String getValue() {
		return mValue;
	}

	private void setValue(String value) {
		mValue = value;
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

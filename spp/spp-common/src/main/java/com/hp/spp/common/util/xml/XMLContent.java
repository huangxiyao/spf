package com.hp.spp.common.util.xml;

import java.io.Serializable;

/**
 * Represents an XML file, that will be exported to the user
 * @author MJULIENS
 *
 */
public class XMLContent implements Serializable {

    private static final long serialVersionUID = 1L;

    byte[] xmlContent;
    
	public XMLContent() {
    }
	
	public XMLContent(byte[] xmlContent) {
		this.xmlContent = xmlContent;
    }

	public byte[] getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(byte[] xmlContent) {
		this.xmlContent = xmlContent;
	}
    
}

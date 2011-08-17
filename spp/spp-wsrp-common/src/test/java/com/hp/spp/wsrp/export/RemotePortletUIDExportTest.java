package com.hp.spp.wsrp.export;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.spp.wsrp.export.component.BadComponent;
import com.hp.spp.wsrp.export.component.mapping.Component;

public class RemotePortletUIDExportTest extends Test {

	public void testCreateNewLine() throws Exception {
		RemotePortletUIDExport export = new RemotePortletUIDExport() ;
		Document doc = export.newDocument() ;
		Element uid = null ;
		
		String xml = "<epideploy:component component-id=\"abc\" component-type=\"Portlet\"><epideploy:detail><portlet-instance kind=\"LOCAL\" service-provider-id=\"def\" service-provider-name=\"PORTALBEAN\"/></epideploy:detail></epideploy:component>" ;
		
		Component component = new Component(readInputStr(xml)) ;
		uid = export.createNewLine(doc, component) ;
		assertNotNull("UID element is not null", uid);
		
		BadComponent badComponent = new BadComponent("badComponent");
		uid = export.createNewLine(doc, badComponent) ;
		assertNotNull("UID element is not null", uid);
		
		uid = export.createNewLine(doc, null) ;
		assertNull("UID element is null", uid);
	}

}

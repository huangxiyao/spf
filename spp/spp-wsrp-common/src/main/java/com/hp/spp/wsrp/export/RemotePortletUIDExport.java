package com.hp.spp.wsrp.export;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.spp.wsrp.export.carfile.CarFileReader;
import com.hp.spp.wsrp.export.component.BadComponent;
import com.hp.spp.wsrp.export.component.ComponentReader;
import com.hp.spp.wsrp.export.component.mapping.Component;
import com.hp.spp.wsrp.export.component.mapping.PortletInstance;
import com.hp.spp.wsrp.export.exception.CarFileException;
import com.hp.spp.wsrp.export.exception.CommonException;
import com.hp.spp.wsrp.export.exception.ExportException;
import com.hp.spp.wsrp.export.exception.UnsupportedMethodException;
import com.hp.spp.wsrp.export.util.DOMUtils;
import com.hp.spp.wsrp.export.util.car.CarFile;


public class RemotePortletUIDExport extends Export {

	public static final String ROOT = "wsrp-portlet-uid-list" ;
	public static final String UID = "uid" ;
	
	public Document export(CarFile carFile) throws ExportException {
		Document result;
		try {
			result = newDocument();
		} catch (CommonException e) {
			throw new ExportException(e);
		}
		Element root = result.createElement(ROOT);
		result.appendChild(root);
		
		List components = null ;
		try {
			CarFileReader reader = new CarFileReader() ;
			components = reader.getComponentList(carFile) ;
		} catch (CarFileException e) {
			createError(result, root, e.getMessage());
			return result;
		}
		
		ComponentReader reader = new ComponentReader() ;
		if(components != null && !components.isEmpty()) {
			List portletInstances = reader.retrieveComponentsByType(components, Component.PORTLET) ;
			components = null ;
			if(portletInstances != null && !portletInstances.isEmpty()) {
				List remotePortlets = reader.retrievePortlets(portletInstances, PortletInstance.WSRP_PORTLET) ;
				portletInstances = null ;
				if(remotePortlets != null && !remotePortlets.isEmpty()) {
					for(Iterator it = remotePortlets.iterator(); it.hasNext(); ) {
						Object o = it.next() ;
						root.appendChild(createNewLine(result, o)) ;
					}
					remotePortlets = null ;
				} else {
					// TODO
				}
			} else {
				// TODO
			}
		} else {
			// TODO
		}
		
		return result;
	}

	Element createNewLine(Document doc, Object o) {
		Element element = null ;
		if(o instanceof Component) {
			Component component = (Component) o ;
			element = DOMUtils.createTextNode(doc, UID, component.getDetails().getPortletInstance().getServiceProviderId());
		} else if(o instanceof BadComponent) {
			BadComponent badComponent = (BadComponent) o ;
			element = createError(doc, badComponent.getMessage()) ;
		} else {
			// TODO
		}
		
		return element ;
	}

	/**
	 * @deprecated
	 */
	public Document export(Document input) throws ExportException {
		throw new UnsupportedMethodException() ;
	}

	/**
	 * @deprecated
	 */
	public Document replace(Document documentWsrpSource, Document documentVgnSource, Document documentVgnTarget) throws ExportException {
		throw new UnsupportedMethodException() ;
	}

	/**
	 * @deprecated
	 */
	public List split(Document input) throws ExportException {
		throw new UnsupportedMethodException() ;
	}

}

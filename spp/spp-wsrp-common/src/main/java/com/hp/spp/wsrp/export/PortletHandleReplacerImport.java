package com.hp.spp.wsrp.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.exception.CommonException;
import com.hp.spp.wsrp.export.exception.ExportException;
import com.hp.spp.wsrp.export.exception.ImportException;
import com.hp.spp.wsrp.export.exception.UnsupportedMethodException;
import com.hp.spp.wsrp.export.util.DOMUtils;
import com.hp.spp.wsrp.export.util.car.CarFile;

public class PortletHandleReplacerImport extends Import {

	public Document replace(Document documentWsrpSource, Document documentVgnSource, Document documentVgnTarget) throws ImportException {
		Document result;
		try {
			result = newDocument();
		} catch (CommonException e) {
			throw new ImportException(e);
		}
		Element root = result.createElement(WsrpProducerPreferenceExport.ROOT);
		result.appendChild(root);

		Map producerInfoSource ;
		Map portletHandleSource ;
		try {
			List informationList = getInformationList(documentVgnSource) ;
			producerInfoSource = (Map) informationList.get(0) ;
			portletHandleSource = (Map) informationList.get(1) ;
		} catch (NullPointerException e) {
			createError(result, root, e.getMessage());
			return result;
		} catch (IllegalArgumentException e) {
			createError(result, root, e.getMessage());
			return result;
		}
		createInfo(result, root, "Number of portlet handle from the source file: "+portletHandleSource.size()) ;
		
		Map producerInfoTarget ;
		Map portletHandleTarget ;
		try {
			List informationList = getInformationList(documentVgnTarget) ;
			producerInfoTarget = (Map) informationList.get(0) ;
			portletHandleTarget = (Map) informationList.get(1) ;
		} catch (NullPointerException e) {
			createError(result, root, e.getMessage());
			return result;
		} catch (IllegalArgumentException e) {
			createError(result, root, e.getMessage());
			return result;
		}
		createInfo(result, root, "Number of portlet handle from the target file: "+portletHandleSource.size()) ;
		
		Map mergedHandleMap = getMergedMap(portletHandleSource, portletHandleTarget) ;
		createInfo(result, root, "Number of portlet handle (after the merge): "+mergedHandleMap.size()) ;

		com.hp.spp.wsrp.export.info.producer.ProducerInfo producerInfo ;
		List wsrpPreferenceList ;
		try {
			List informationList = getWsrpPreferenceList(documentWsrpSource) ;
			producerInfo = (com.hp.spp.wsrp.export.info.producer.ProducerInfo) informationList.get(0) ;
			wsrpPreferenceList = (List) informationList.get(1) ;
		} catch (NullPointerException e) {
			createError(result, root, e.getMessage());
			return result;
		} catch (IllegalArgumentException e) {
			createError(result, root, e.getMessage());
			return result;
		}
		createInfo(result, root, "Number of portlet with preferences: "+wsrpPreferenceList.size()) ;
		
		try {
			replaceProducerUrl(producerInfo, producerInfoSource, producerInfoTarget) ;
		} catch (NullPointerException e) {
			createError(result, root, e.getMessage());
			return result;
		} catch (IllegalArgumentException e) {
			createError(result, root, e.getMessage());
			return result;
		}
		
		root.appendChild(producerInfo.toElement(result));
		
		try {
			int nbReplaced = replaceHandle(wsrpPreferenceList, mergedHandleMap) ;
			createInfo(result, root, "Number of replaced portlet handle: "+nbReplaced) ;
		} catch (NullPointerException e) {
			createError(result, root, e.getMessage());
			return result;
		} catch (IllegalArgumentException e) {
			createError(result, root, e.getMessage());
			return result;
		}
		
		for(Iterator it = wsrpPreferenceList.iterator(); it.hasNext(); ) {
			Object object = it.next() ;
			
			if(object instanceof com.hp.spp.wsrp.export.info.producer.PortletInfo) {
				com.hp.spp.wsrp.export.info.producer.PortletInfo portletInfo = (com.hp.spp.wsrp.export.info.producer.PortletInfo) object ;
				
				if (portletInfo.hasPreferences()) {
					root.appendChild(portletInfo.toElement(result));
				}
			} else if(object instanceof com.hp.spp.wsrp.export.info.replacer.BadPortletInfo) {
				com.hp.spp.wsrp.export.info.replacer.BadPortletInfo portletInfo = (com.hp.spp.wsrp.export.info.replacer.BadPortletInfo) object ;
				
				root.appendChild(portletInfo.toElement(result));
			} else {
				root.appendChild(createError(result, ""));
			}
		}
		
		return result;
	}

	List getInformationList(Document document) {
		Map producerInfoMap = null ;
		Map portletHandleMap = null ;
		
		List producerList = getProducerList(document);
		List producerInfoList = getProducerInfoList(producerList) ;
		
		producerInfoMap = getProducerInfoMap(producerInfoList) ; ;
		portletHandleMap = getPortletHandleMap(producerInfoList) ;
		
		List informationList = new ArrayList() ;
		informationList.add(producerInfoMap) ;
		informationList.add(portletHandleMap) ;
		
		return informationList;
	}

	List getProducerList(Node node) {
		node = DOMUtils.selectSingleNode(node, RemotePortletHandleExport.ROOT) ;
		if(node == null)
			throw new NullPointerException("No WSRP portlet list found in the input document");
		List nodeList = DOMUtils.selectNodes(node, com.hp.spp.wsrp.export.info.vignette.ProducerInfo.PRODUCER) ;
		if(nodeList == null || nodeList.isEmpty())
			throw new IllegalArgumentException("No producer found in the input document");

		return nodeList;
	}

	List getProducerInfoList(List producerList) {
		if(producerList == null)
			throw new NullPointerException("Producer list is null") ;
		
		List producerInfoList = new ArrayList() ;
		for(Iterator it = producerList.iterator(); it.hasNext(); ) {
			Element producer = (Element) it.next() ;
			try {
				com.hp.spp.wsrp.export.info.vignette.ProducerInfo producerInfo = new com.hp.spp.wsrp.export.info.vignette.ProducerInfo(producer);
				
				if(producerInfo.hasPortlets()) {
					producerInfoList.add(producerInfo) ;
				}
			} catch (BadConstructorException e) {
//				throw new IllegalArgumentException(e.getMessage()) ;
			}
		}
		
		return producerInfoList;
	}

	Map getProducerInfoMap(List producerInfoList) {
		if(producerInfoList == null)
			throw new NullPointerException("ProducerInfo list is null") ;
		
		Map producerInfoMap = new TreeMap() ;
		
		for(Iterator it = producerInfoList.iterator(); it.hasNext(); ) {
			com.hp.spp.wsrp.export.info.vignette.ProducerInfo producerInfo = (com.hp.spp.wsrp.export.info.vignette.ProducerInfo) it.next() ;
			// the URL is he key of the Map because the ImportId NOT unique...
			producerInfoMap.put(producerInfo.getUrl(), producerInfo.getImportId()) ;
		}
		
		return producerInfoMap;
	}

	Map getPortletHandleMap(List producerInfoList) {
		if(producerInfoList == null)
			throw new NullPointerException("ProducerInfo list is null") ;
		
		Map portletHandleMap = new TreeMap() ;
		
		for(Iterator it = producerInfoList.iterator(); it.hasNext(); ) {
			com.hp.spp.wsrp.export.info.vignette.ProducerInfo producerInfo = (com.hp.spp.wsrp.export.info.vignette.ProducerInfo) it.next() ;
			
			Map portlets = producerInfo.getPortlets() ;
			for(Iterator it2 = portlets.values().iterator(); it2.hasNext();) {
				com.hp.spp.wsrp.export.info.vignette.PortletInfo portletInfo = (com.hp.spp.wsrp.export.info.vignette.PortletInfo) it2.next();
				portletHandleMap.put(portletInfo.getUID(), portletInfo.getHandle()) ;
			}
		}
		
		return portletHandleMap;
	}

	List getWsrpPreferenceList(Document documentWsrpSource) {
		Node node = DOMUtils.selectSingleNode(documentWsrpSource, WsrpProducerPreferenceExport.ROOT) ;
		if(node == null)
			throw new NullPointerException("No portlet preferences are found in the Document.") ;
		
		com.hp.spp.wsrp.export.info.producer.ProducerInfo producerInfo = getProducerInfo(node) ;
		
		List portletList = getPortletList(node) ;
		
		List portletInfoList = getPortletInfoList(portletList) ;
		
		List informationList = new ArrayList() ;
		informationList.add(producerInfo) ;
		informationList.add(portletInfoList) ;
		
		return informationList;
	}

	com.hp.spp.wsrp.export.info.producer.ProducerInfo getProducerInfo(Node node) {
		if(node == null) {
			throw new NullPointerException("No portlet preference are found in the Document.") ;
		}
		Node producer = DOMUtils.selectSingleNode(node, com.hp.spp.wsrp.export.info.producer.ProducerInfo.PRODUCER_INFO) ;

		com.hp.spp.wsrp.export.info.producer.ProducerInfo producerInfo = null ;
		try {
			producerInfo = new com.hp.spp.wsrp.export.info.producer.ProducerInfo((Element) producer) ;
		} catch (BadConstructorException e) {
		}
		
		if(producerInfo == null) {
			throw new IllegalArgumentException("No ProducerInfo are found in the Document.") ;
		}
		
		return producerInfo ;
	}

	List getPortletList(Node node) {
		if(node == null) {
			throw new NullPointerException("No portlet preference are found in the Document.") ;
		}
		return DOMUtils.selectNodes(node, com.hp.spp.wsrp.export.info.producer.PortletInfo.PORTLET) ;
//		List portletList = DOMUtils.selectNodes(node, com.hp.spp.wsrp.export.info.producer.PortletInfo.PORTLET) ;
//		if(portletList == null || portletList.isEmpty()) {
//			throw new IllegalArgumentException("No portlet list are found in the Document.") ;
//		}
//		return portletList;
	}

	List getPortletInfoList(List portletList) {
		if(portletList == null)
			throw new NullPointerException("No portlet list are found in the Document.") ;
		
		List portletInfoList = null ;
		for(int i = 0, len = portletList.size(); i < len; ++i) {
			Element portlet = (Element) portletList.get(i) ;
			Object portletInfo;
			try {
				portletInfo = new com.hp.spp.wsrp.export.info.producer.PortletInfo(portlet);
			} catch (BadConstructorException e) {
				portletInfo = new com.hp.spp.wsrp.export.info.replacer.BadPortletInfo("[Portlet #"+(i+1)+"] BadConstructorException: "+e.getMessage()+".") ;
			}
			
			if(portletInfo != null) {
				if(portletInfoList == null)
					portletInfoList = new ArrayList() ;
				
				portletInfoList.add(portletInfo) ;
			}
		}
		
		return portletInfoList;
	}

	void replaceProducerUrl(com.hp.spp.wsrp.export.info.producer.ProducerInfo producerInfo, Map producerInfoSource, Map producerInfoTarget) {
		String url = producerInfo.getUrl() ;
		String importId = (String) producerInfoSource.get(url) ;
		
		url = null ;
		Iterator it = producerInfoTarget.entrySet().iterator() ;
		while(it.hasNext() && url == null) {
			Map.Entry param = (Map.Entry) it.next() ;
			
			// @see getProducerInfoMap to understand why URL is the key of the Map
			if(importId.equals(param.getValue())) {
				url = (String) param.getKey() ;
			}
		}
		
		producerInfo.setUrl(url) ;
	}

	int replaceHandle(List wsrpPreferenceList, Map mergedHandleMap) {
		if(wsrpPreferenceList == null)
			throw new NullPointerException("Preference list is null.") ;
		if(mergedHandleMap == null)
			throw new NullPointerException("Handle source is null.") ;
		if(wsrpPreferenceList.isEmpty())
			throw new IllegalArgumentException("Preference list is empty.") ;
		if(mergedHandleMap.isEmpty())
			throw new IllegalArgumentException("Handle source is empty.") ;
		
		int iModified = 0 ;
		for(int i = 0, len = wsrpPreferenceList.size(); i < len; ++i) {
			Object object = wsrpPreferenceList.get(i) ;
			
			if(object instanceof com.hp.spp.wsrp.export.info.producer.PortletInfo) {
				com.hp.spp.wsrp.export.info.producer.PortletInfo portletInfo = (com.hp.spp.wsrp.export.info.producer.PortletInfo) object ;
				String handle = portletInfo.getHandle() ;
				
				handle = (String) mergedHandleMap.get(handle) ;
				if(handle == null || "".equals(handle)) {
					object = new com.hp.spp.wsrp.export.info.replacer.BadPortletInfo("[Portlet #"+(i+1)+"] handle cannot be retrieve with the key "+handle+".") ;
					wsrpPreferenceList.set(i, object) ;
				} else {
					portletInfo.setHandle(handle) ;
					iModified++ ;
				}
			}
		}
		
		return iModified ;
	}

	Map getReversedMap(Map map) {
		Map reversedMap = null ;
		
		if(map != null) {
			reversedMap = new TreeMap() ;
			
			for(Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry param = (Map.Entry) it.next() ;
				reversedMap.put(param.getValue(), param.getKey()) ;
			}
		}
			
		return reversedMap;
	}

	Map getMergedMap(Map mapSource, Map mapTarget) {
		Map mergedMap = null ;
		
		if(mapSource != null && mapTarget != null) {
			mergedMap = new TreeMap() ;
			
			if(!mapSource.isEmpty() && !mapTarget.isEmpty()) {
				mergedMap = new TreeMap() ;
				
				for(Iterator it = mapSource.entrySet().iterator(); it.hasNext(); ) {
					Map.Entry param = (Map.Entry) it.next() ;
					
					Object key = param.getKey() ;
					
					if(key != null && mapTarget.containsKey(key)) {
						Object valueSource = param.getValue() ;
						Object valueTarget = mapTarget.get(key) ;
						
						if(valueSource != null && valueTarget != null) {
							mergedMap.put(valueSource, valueTarget) ;
						}
					}
				}
			}
		}
			
		return mergedMap;
	}

	/**
	 * @deprecated
	 */
	public Document export(CarFile input) throws ExportException {
		throw new UnsupportedMethodException() ;
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
	public List split(Document input) throws ExportException {
		throw new UnsupportedMethodException() ;
	}

//	static class BadPortletInfo {
//	}

}

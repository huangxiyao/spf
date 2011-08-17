package com.hp.spp.wsrp.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.exception.CommonException;
import com.hp.spp.wsrp.export.exception.ExportException;
import com.hp.spp.wsrp.export.exception.UnsupportedMethodException;
import com.hp.spp.wsrp.export.util.DOMUtils;
import com.hp.spp.wsrp.export.util.car.CarFile;

public class ProducerSplitterExport extends Export {

	public List split(Document input) throws ExportException {
		List resultList = null ;
		
		List producerList;
		try {
			producerList = getProducerList(input);
		} catch (NullPointerException e) {
			return getErrorList(e);
		} catch (IllegalArgumentException e) {
			return getErrorList(e);
		}
		
		List producerInfoList;
		try {
			producerInfoList = getProducerInfoList(producerList) ;
		} catch (NullPointerException e) {
			return getErrorList(e);
		}
		
		List splittedProducerInfoList;
		try {
			splittedProducerInfoList = getSplittedProducerInfoList(producerInfoList) ;
		} catch (NullPointerException e) {
			return getErrorList(e);
		}
		
		try {
			resultList = getDocumentList(splittedProducerInfoList) ;
		} catch (NullPointerException e) {
			return getErrorList(e);
		}
		
		return resultList;
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

	List getSplittedProducerInfoList(List producerInfoList) {
		if(producerInfoList == null)
			throw new NullPointerException("ProducerInfo list is null") ;
		
		Map producerMap = new HashMap() ;
		List key = null ;
		for(Iterator it = producerInfoList.iterator(); it.hasNext(); ) {
			com.hp.spp.wsrp.export.info.vignette.ProducerInfo producer = (com.hp.spp.wsrp.export.info.vignette.ProducerInfo) it.next() ;
			
			if(producer != null && producer.hasPortlets()) {
				String importId = producer.getImportId() ;
				String url = producer.getUrl() ;
				
				key = new ArrayList();
				key.add(importId);
				key.add(url);
				
				com.hp.spp.wsrp.export.info.splitter.ProducerInfo producerInfo = null ;
				if(producerMap.containsKey(key)) {
					producerInfo = (com.hp.spp.wsrp.export.info.splitter.ProducerInfo) producerMap.get(key) ;
				} else {
					producerInfo = new com.hp.spp.wsrp.export.info.splitter.ProducerInfo(importId, url) ;
				}
				
				addHandles(producer, producerInfo);
				
				if(!producerMap.containsKey(key) && producerInfo.hasHandles()) {
					producerMap.put(key, producerInfo) ;
				}
			}
		}

		List splittedList = new ArrayList() ;
		for(Iterator it = producerMap.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry producer = (Map.Entry) it.next();
			com.hp.spp.wsrp.export.info.splitter.ProducerInfo producerInfo = (com.hp.spp.wsrp.export.info.splitter.ProducerInfo) producer.getValue();
			
			if(producerInfo != null) {
				splittedList.add(producerInfo) ;
			}
		}
		
		return splittedList;
	}

	void addHandles(com.hp.spp.wsrp.export.info.vignette.ProducerInfo producer, com.hp.spp.wsrp.export.info.splitter.ProducerInfo producerInfo) {
		if(producer == null || producerInfo == null)
			throw new NullPointerException("ProducerInfo is null") ;
		
		Map portletMap = producer.getPortlets();
		for(Iterator it2 = portletMap.values().iterator(); it2.hasNext(); ) {
			com.hp.spp.wsrp.export.info.vignette.PortletInfo portletInfo = (com.hp.spp.wsrp.export.info.vignette.PortletInfo) it2.next();
			String handle = (String) portletInfo.getHandle() ;
			
			if(handle != null && !"".equals(handle)) {
				producerInfo.addHandle(handle) ;
			}
		}
	}

	List getDocumentList(List splittedList) throws ExportException {
		if(splittedList == null)
			throw new NullPointerException("Splitted ProducerInfo list is null") ;
		
		List resultDocuments = new ArrayList() ;
		for(Iterator it = splittedList.iterator(); it.hasNext(); ) {
			com.hp.spp.wsrp.export.info.splitter.ProducerInfo producerInfo = (com.hp.spp.wsrp.export.info.splitter.ProducerInfo) it.next();
			
			if(producerInfo != null) {
				try {
					Document result = newDocument();
					result.appendChild(producerInfo.toElement(result));
					
					resultDocuments.add(result) ;
				} catch (DOMException e) {
					throw new ExportException(e);
				} catch (CommonException e) {
					throw new ExportException(e);
				}
			}
		}
			
		return resultDocuments;
	}

	private List getErrorList(Throwable t) throws ExportException {
		try {
			Document result = newDocument();
			result.appendChild(createError(result, t.getMessage()));
			
			List resultList = new ArrayList();
			resultList.add(result) ;
			
			return resultList;
		} catch (DOMException e) {
			throw new ExportException(e);
		} catch (CommonException e) {
			throw new ExportException(e);
		}
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
	public Document export(CarFile input) throws ExportException {
		throw new UnsupportedMethodException() ;
	}
	
	/**
	 * @deprecated
	 */
	public Document replace(Document documentWsrpSource, Document documentVgnSource, Document documentVgnTarget) throws ExportException {
		throw new UnsupportedMethodException() ;
	}

//	static class ProducerInfo {
//		
//	}

}

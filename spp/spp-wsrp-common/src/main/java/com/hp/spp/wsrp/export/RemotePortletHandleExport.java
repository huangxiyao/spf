package com.hp.spp.wsrp.export;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.hp.spp.db.DB;
import com.hp.spp.db.RowMapper;
import com.hp.spp.wsrp.export.exception.CommonException;
import com.hp.spp.wsrp.export.exception.ExportException;
import com.hp.spp.wsrp.export.exception.UnsupportedMethodException;
import com.hp.spp.wsrp.export.util.DOMUtils;
import com.hp.spp.wsrp.export.util.InputStreamUtils;
import com.hp.spp.wsrp.export.util.car.CarFile;
import com.vignette.portal.portlet.management.external.PortletPersistenceException;
import com.vignette.portal.portlet.management.external.PortletResourceNotFoundException;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.ProducerData;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.ProducerDataGetter;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.WsrpPortletApplicationManagerSpiImpl;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.WsrpPortletApplicationSpiImpl;

public class RemotePortletHandleExport extends Export {

	public static final String ROOT = "wsrp-portlet-list" ;

	public Document export(Document input) throws ExportException {
		Document result;
		try {
			result = newDocument();
		} catch (CommonException e) {
			throw new ExportException(e);
		}
		Element root = result.createElement(ROOT);
		result.appendChild(root);
		
		List remotePortletIUDs;
		try {
			remotePortletIUDs = getRemotePortletIUDList(input);
		} catch (IllegalArgumentException e) {
			createError(result, root, e.getMessage());
			return result;
		}
		
		// split portlet handle lists into sub lists
		List listOfRemotePortletIUDList = splitIntoSubLists(remotePortletIUDs, MAX_IN_LIMIT);
		
		// query the database
		List foundUIDs = new ArrayList();
		for (Iterator it = listOfRemotePortletIUDList.iterator(); it.hasNext();) {
			List subListOfHandles = (List) it.next();
			List producerInfoList = getProducerInfoList(subListOfHandles) ;
			for (Iterator it2 = producerInfoList.iterator(); it2.hasNext();) {
				com.hp.spp.wsrp.export.info.vignette.ProducerInfo info = (com.hp.spp.wsrp.export.info.vignette.ProducerInfo) it2.next();
				if (info.hasPortlets()) {
					foundUIDs.addAll(info.getPortletUIDs());
					root.appendChild(info.toElement(result));
				}
			}
		}

		// put in errors about input UIDs not found
		if (remotePortletIUDs.size() != foundUIDs.size()) {
			List handlesNotFound = new ArrayList(remotePortletIUDs);
			handlesNotFound.removeAll(foundUIDs);
			for (Iterator it = handlesNotFound.iterator(); it.hasNext();) {
				String uid = (String) it.next();
				createError(result, root, "Portlet UID not found: " + uid);
			}
		}
		
		return result;
	}

	List getRemotePortletIUDList(Node node) {
		if(node == null)
			throw new NullPointerException("Document cannot be empty!");
		node = DOMUtils.selectSingleNode(node, RemotePortletUIDExport.ROOT) ;
		if(node == null)
			throw new NullPointerException("No WSRP portlet UID list found in the input document");
		List nodeList = DOMUtils.selectNodes(node, RemotePortletUIDExport.UID) ;
		if(nodeList == null)
			throw new NullPointerException("No portlet UID found in the input document");
		
		List result = new ArrayList() ;
		for(int i = 0, len = nodeList.size(); i < len; ++i) {
			Element uid = (Element) nodeList.get(i) ;
			if (!uid.hasChildNodes() || uid.getFirstChild().getNodeValue().trim().equals("")) {
				throw new IllegalArgumentException("Portlet UID value is empty");
			}
			result.add(uid.getFirstChild().getNodeValue());
		}
		
		return result ;
	}

	List getProducerInfoList(List listOfUIDs) {
		// build the args table and the content of "IN" expression
		String[] UIDArgs = new String[listOfUIDs.size()];
		StringBuffer sb = new StringBuffer();
		for (int i = 0, len = UIDArgs.length; i < len; ++i) {
			UIDArgs[i] = (String) listOfUIDs.get(i);
			sb.append('?');
			if (i < len - 1) {
				sb.append(',');
			}
		}
		
		// here is the query
		StringBuffer sql = new StringBuffer(
				"select wpa.friendlyid as import_id, " + 
				"wpa.producer_data as url, " + 
				"wp.wsrp_portlet_uid as portlet_uid, " + 
				"wp.wsrp_portlet_handle as portlet_handle " + 
				"from " + 
				"WSRPPORTLETAPPLICATIONS wpa, " + 
				"WSRPPORTLETTYPES wpt, " + 
				"WSRPPORTLETS wp " + 
				"where 1 = 1 " + 
				"and wpa.wsrp_portlet_application_uid = wpt.wsrp_portlet_application_uid " + 
				"and wpt.wsrp_portlet_type_uid = wp.wsrp_portlet_type_uid " + 
				"and wp.wsrp_portlet_uid in (" + sb.toString() + ") " +
				"order by import_id, portlet_uid, portlet_handle ") ;
		
		// in this mapper implementation we don't use the result of the mapping but we rather
		// build this result as we read from the database. Therefore the mapper must return null
		final List result = new ArrayList();
		RowMapper mapper = new RowMapper<Object>() {
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				String url = null;
				String importId = resultSet.getString("import_id");
				try {
					url = getProducerUrlByFriendlyID(importId) ;
				} catch (Error e) {
					url = blobToString(resultSet.getBlob("url"));
				} catch (Exception e) {
					url = blobToString(resultSet.getBlob("url"));
				}
				String portletUID = resultSet.getString("portlet_uid");
				String portletHandle = resultSet.getString("portlet_handle");
				com.hp.spp.wsrp.export.info.vignette.ProducerInfo info;

				if (result.isEmpty()) {
					info = new com.hp.spp.wsrp.export.info.vignette.ProducerInfo(importId, url);
					result.add(info);
				} else {
					info = (com.hp.spp.wsrp.export.info.vignette.ProducerInfo) result.get(result.size()-1);
					if (!importId.equals(info.getImportId()) || !url.equals(info.getUrl())) {
						info = new com.hp.spp.wsrp.export.info.vignette.ProducerInfo(importId, url);
						result.add(info);
					}
				}
				
				if (portletUID != null && portletHandle != null) {
					info.addPortlet(portletUID, portletHandle);
				}

				// just return null as we create this result ourselves
				return null;
			}

			private String blobToString(Blob blob) throws SQLException {
				Object o;
				try {
					o = InputStreamUtils.getObject(blob.getBinaryStream());
				} catch (IOException e) {
					throw new SQLException("[blobToString] IOException!\n"+toTrace(e)) ;
				} catch (ClassNotFoundException e) {
					throw new SQLException("[blobToString] ClassNotFoundException!\n"+toTrace(e)) ;
				}
				if(o instanceof ProducerData) {
					ProducerDataGetter producer = new ProducerDataGetter((ProducerData) o);
					return producer.getOriginalServiceURL() ;
				}
				return null ;
			}
			
			private String getProducerUrlByFriendlyID(String friendlyID) throws PortletResourceNotFoundException, PortletPersistenceException {
				return getProducerByFriendlyID(friendlyID).getAppCreationUrl();
			}
			
			private WsrpPortletApplicationSpiImpl getProducerByFriendlyID(String friendlyID) throws PortletResourceNotFoundException, PortletPersistenceException {
				WsrpPortletApplicationManagerSpiImpl manager = WsrpPortletApplicationManagerSpiImpl.getInstance();
				return manager.getWsrpPortletApplicationByFriendlyID(friendlyID);
			}
			
			private String toTrace(Throwable t) {
				StringWriter sw = new StringWriter() ;
				PrintWriter pw = new PrintWriter(sw) ;
				t.printStackTrace(pw) ;
				return sw.toString() ;
			}
		};

		// do the query
		DB.query(sql.toString(), mapper, UIDArgs);

		return result;
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
	public List split(Document input) throws ExportException {
		throw new UnsupportedMethodException() ;
	}
	
	/**
	 * @deprecated
	 */
	public Document replace(Document documentWsrpSource, Document documentVgnSource, Document documentVgnTarget) throws ExportException {
		throw new UnsupportedMethodException() ;
	}

}

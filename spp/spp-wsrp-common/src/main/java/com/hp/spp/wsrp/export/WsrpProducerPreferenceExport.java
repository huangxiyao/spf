package com.hp.spp.wsrp.export;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hp.spp.db.DB;
import com.hp.spp.db.RowMapper;
import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.exception.CommonException;
import com.hp.spp.wsrp.export.exception.ExportException;
import com.hp.spp.wsrp.export.exception.UnsupportedMethodException;
import com.hp.spp.wsrp.export.info.producer.PortletInfo;
import com.hp.spp.wsrp.export.info.producer.ProducerInfo;
import com.hp.spp.wsrp.export.util.DOMUtils;
import com.hp.spp.wsrp.export.util.car.CarFile;

public class WsrpProducerPreferenceExport extends Export {

	public static final String ROOT = "portlet-preferences" ;

	public static final int MAX_IN_LIMIT = 100;

	public Document export(Document input) throws ExportException {
		Document result;
		try {
			result = newDocument();
		} catch (CommonException e) {
			throw new ExportException(e);
		}
		Element root = result.createElement(ROOT);
		result.appendChild(root);

		// copy producer information
		try {
			ProducerInfo producerInfo = getProducerInfo(input);
			root.appendChild(producerInfo.toElement(result));
		}
		catch (IllegalArgumentException e) {
			root.appendChild(DOMUtils.createError(result, e.getMessage()));
			return result;
		}

		// extract portlet handles from the input document
		List portletHandles;
		try {
			portletHandles = getPortletHandles(input);
		}
		catch (IllegalArgumentException e) {
			root.appendChild(DOMUtils.createError(result, e.getMessage()));
			return result;
		}

		// split portlet handle lists into sub lists
		List listOfPortletHandleLists = splitIntoSubLists(portletHandles, MAX_IN_LIMIT);

		// query the database
		List foundHandles = new ArrayList();
		for (Iterator it = listOfPortletHandleLists.iterator(); it.hasNext();) {
			List subListOfHandles = (List) it.next();
			List portletInfoList = getPortletInfoList(subListOfHandles);
			for (Iterator it2 = portletInfoList.iterator(); it2.hasNext();) {
				PortletInfo info = (PortletInfo) it2.next();
				foundHandles.add(info.getHandle());
				if (info.hasPreferences()) {
					root.appendChild(info.toElement(result));
				}
			}
		}

		// put in errors about input handles not found
		if (portletHandles.size() != foundHandles.size()) {
			List handlesNotFound = new ArrayList(portletHandles);
			handlesNotFound.removeAll(foundHandles);
			for (Iterator it = handlesNotFound.iterator(); it.hasNext();) {
				String handle = (String) it.next();
				root.appendChild(DOMUtils.createError(result, "Portlet handle not found: " + handle));
			}
		}

		return result;
	}

	List getPortletInfoList(List listOfHandles) {
		// build the args table and the content of "IN" expression
		String[] handleArgs = new String[listOfHandles.size()];
		StringBuffer sb = new StringBuffer();
		for (int i = 0, len = handleArgs.length; i < len; ++i) {
			handleArgs[i] = (String) listOfHandles.get(i);
			sb.append('?');
			if (i < len - 1) {
				sb.append(',');
			}
		}

		// here is the query
		StringBuffer sql = new StringBuffer().
				append("select a.ID as \"handle\", b.NAME as \"pref_name\", b.READ_ONLY as \"read_only\", c.VALUE as \"pref_value\" ").
				append("from WSRP_PORTLET a left outer join WSRP_PREFERENCE b on a.ID = b.PORTLET ").
				append("left outer join WSRP_PREFERENCE_VALUE c on b.ID = c.PREFERENCE ").
				append("where a.ID in (").append(sb.toString()).append(") ").
				append("order by a.ID, b.NAME, c.PREFERENCE_IDX");

		// in this mapper implementation we don't use the result of the mapping but we rather
		// build this result as we read from the database. Therefore the mapper must return null
		final List result = new ArrayList();
		RowMapper mapper = new RowMapper<Object>() {
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				String handle = resultSet.getString("handle");
				String prefName = resultSet.getString("pref_name");
				String readOnly = resultSet.getString("read_only");
				String prefValue = resultSet.getString("pref_value");
				PortletInfo info;

				if (result.isEmpty()) {
					info = new PortletInfo(handle);
					result.add(info);
				}
				else {
					info = (PortletInfo) result.get(result.size()-1);
					if (!handle.equals(info.getHandle())) {
						info = new PortletInfo(handle);
						result.add(info);
					}
				}
				if (prefName != null && prefValue != null) {
					info.addPreference(prefName, prefValue, readOnly);
				}

				// just return null as we create this result ourselves
				return null;
			}
		};

		// do the query
		DB.query(sql.toString(), mapper, handleArgs);

		return result;
	}

	List splitIntoSubLists(List list, int maxSublistSize) {
		List result = new ArrayList();
		if (list.size() <= maxSublistSize) {
			result.add(list);
		}
		else {
			int numOfSublists = list.size() / maxSublistSize;
			for (int i = 0; i < numOfSublists; ++i) {
				result.add(list.subList(i*maxSublistSize, (i+1)*maxSublistSize));
			}
			if (list.size() % maxSublistSize != 0) {
				result.add(list.subList(numOfSublists * maxSublistSize, list.size()));
			}
		}

		return result;
	}

	List getPortletHandles(Document input) {
		NodeList nl = input.getElementsByTagName("handle");
		if (nl == null || nl.getLength() == 0) {
			throw new IllegalArgumentException("No portlet handles found in the input document");
		}
		List result = new ArrayList(nl.getLength());
		for (int i = 0, len = nl.getLength(); i < len; ++i) {
			Element handle = (Element) nl.item(i);
			if (!handle.hasChildNodes() || handle.getFirstChild().getNodeValue().trim().equals("")) {
				throw new IllegalArgumentException("Portlet handle value is empty");
			}
			result.add(handle.getFirstChild().getNodeValue());
		}
		return result;
	}

	ProducerInfo getProducerInfo(Document input) {
		NodeList nl = input.getElementsByTagName("producer-info");
		if (nl == null || nl.getLength() == 0) {
			throw new IllegalArgumentException("Producer information not found");
		}
		Element producerInfoElement = (Element) nl.item(0);
		try {
			return new ProducerInfo(producerInfoElement);
		} catch (BadConstructorException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
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

	/**
	 * @deprecated
	 */
	public List split(Document input) throws ExportException {
		throw new UnsupportedMethodException() ;
	}

}

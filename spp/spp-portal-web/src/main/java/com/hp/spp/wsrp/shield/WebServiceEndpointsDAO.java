package com.hp.spp.wsrp.shield;

import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.ProducerDataGetter;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.ProducerData;
import com.hp.spp.db.DB;
import com.hp.spp.db.RowMapper;

import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.sql.ResultSet;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;

public class WebServiceEndpointsDAO implements RemoteServerDAO {
	private static final Logger mLog = Logger.getLogger(WebServiceEndpointsDAO.class);
	private RemoteServerDAO mDelegate;

	public WebServiceEndpointsDAO(RemoteServerDAO delegate) {
		mDelegate = delegate;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getDisabledServerUrls() {
		Set<String> disabledServerUrls = mDelegate.getDisabledServerUrls();
		if (disabledServerUrls == null || disabledServerUrls.isEmpty()) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Sef of disabled urls is empty");
			}
			return Collections.EMPTY_SET;
		}

		List<ProducerDataGetter> producerData = getAllRemoteServersData();

		Set<String> result = new TreeSet<String>();
		for (String wsdlUrl : disabledServerUrls) {
			result.add(wsdlUrl);
			addWebServiceEndpointUrls(result, wsdlUrl, producerData);
		}
		if (mLog.isDebugEnabled()) {
			mLog.debug("Set of disabled urls after adding web service endpoint urls: " + result);
		}

		return result;
	}

	public List<PortalRemoteServerInfo> getPortalDeclaredRemoteServers() {
		return mDelegate.getPortalDeclaredRemoteServers();
	}

	public void setUrlEnabled(String url, boolean enabled) {
		mDelegate.setUrlEnabled(url, enabled);
	}

	private List<ProducerDataGetter> getAllRemoteServersData() {
		return DB.query("select producer_data from wsrpportletapplications",
				new RowMapper<ProducerDataGetter>() {
					public ProducerDataGetter mapRow(ResultSet row, int rowNum) {
						try {
							ObjectInputStream ois = new ObjectInputStream(row.getBinaryStream(1));
							try {
								return new ProducerDataGetter((ProducerData) ois.readObject());
							}
							finally {
								ois.close();
							}
						}
						catch (Exception e) {
							throw new IllegalStateException("Error while reading producer data from database: " + e, e);
						}
					}
				});
	}

	private void addWebServiceEndpointUrls(Set<String> result, String wsdlUrl, List<ProducerDataGetter> producerData) {
		for (Iterator<ProducerDataGetter> it = producerData.iterator(); it.hasNext();) {
			ProducerDataGetter getter = it.next();
			//FIXME (slawek) - need to properly handle WSDL proxy URL
			if (wsdlUrl.equals(getter.getOriginalServiceURL())) {
				result.add(getter.getMarkupURL());
				result.add(getter.getRegistrationURL());
				result.add(getter.getServiceDescriptionURL());
				result.add(getter.getPortletManagementURL());
				return;
			}
		}
	}

}

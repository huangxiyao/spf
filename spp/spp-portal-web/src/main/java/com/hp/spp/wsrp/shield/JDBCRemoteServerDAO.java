package com.hp.spp.wsrp.shield;

import com.hp.spp.db.DB;
import com.hp.spp.db.RowMapper;
import com.hp.spp.db.DatabaseTransaction;
import com.hp.spp.db.DatabaseException;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.ProducerDataGetter;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.ProducerData;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;


/**
 * Provides database-backed implementation of {@link RemoteServerDAO}.
 */
public class JDBCRemoteServerDAO implements RemoteServerDAO {
	private static final Logger mLog = Logger.getLogger(JDBCRemoteServerDAO.class);
	private static final String SQL_GET_DISABLED_SERVERS = "select wsdl_url from spp_wsrp_disabled_server order by 1";
	private static final String SQL_GET_REGISTERED_SERVERS_INFO = "select title, producer_data from wsrpportletapplications";
	private static final String SQL_DELETE_DISABLED_SERVER = "delete from spp_wsrp_disabled_server where wsdl_url = ?";

	public Set<String> getDisabledServerUrls() {
		Set<String> result = new HashSet<String>();
		result.addAll(
				DB.query(SQL_GET_DISABLED_SERVERS,
						new RowMapper<String>() {
							public String mapRow(ResultSet row, int rowNum) throws SQLException {
								return row.getString(1);
							}
						})
		);
		if (mLog.isDebugEnabled()) {
			mLog.debug("Loaded the following set of disabled WSRP servers: " + result);
		}
		return result;
	}


	public List<PortalRemoteServerInfo> getPortalDeclaredRemoteServers() {
		List<PortalRemoteServerInfo> result = new ArrayList<PortalRemoteServerInfo>();
		result.addAll(
				DB.query(SQL_GET_REGISTERED_SERVERS_INFO,
						new RowMapper<PortalRemoteServerInfo>() {
							public PortalRemoteServerInfo mapRow(ResultSet row, int rowNum) throws SQLException {
								String title = row.getString("title");
								String url = getWsdlUrl(row.getBinaryStream("producer_data"));
								return new PortalRemoteServerInfo(title, url);
							}

						})
		);

		if (mLog.isDebugEnabled()) {
			mLog.debug("Loaded the following list of servers declared in VAP: " + result);
		}
		return result;
	}

	public void setUrlEnabled(String url, boolean enabled) {
		if (enabled) {
			DB.update(SQL_DELETE_DISABLED_SERVER, new String[] {url});
		}
		else {
			// check if it's already in the database - if yes do nothing, if not - remove it
			try {
				new DisableUrlTransaction(url).execute();
			}
			catch (Exception e) {
				throw new DatabaseException("Error when saving disabled server URL: " + e, e);
			}
		}
	}


	private String getWsdlUrl(InputStream binaryStream) {
		try {
			ObjectInputStream in = new ObjectInputStream(binaryStream);
			try {
				ProducerDataGetter getter = new ProducerDataGetter((ProducerData) in.readObject());
				return getter.getOriginalServiceURL();
			}
			finally {
				in.close();
			}
		}
		catch (Exception e) {
			throw new IllegalStateException("Unable to read producer data from BLOB: " + e, e);
		}
	}


	private class DisableUrlTransaction extends DatabaseTransaction<Object> {

		private static final String SQL_CHECK_SERVER_ALREADY_DISABLED = "select count(*) from spp_wsrp_disabled_server where wsdl_url = ?";
		private static final String SQL_DISABLE_SERVER = "insert into spp_wsrp_disabled_server(wsdl_url) values(?)";

		private String mUrl;

		public DisableUrlTransaction(String url) {
			mUrl = url;
		}

		protected Object doInTransaction() throws Exception {
			List<Integer> count =
					query(SQL_CHECK_SERVER_ALREADY_DISABLED,
							new RowMapper<Integer>() {
								public Integer mapRow(ResultSet row, int rowNum) throws SQLException {
									return new Integer(row.getInt(1));
								}
							},
							0, 1, new String[] {mUrl}, new int[] {Types.VARCHAR});
			if (count.get(0) == 0) {
				update(SQL_DISABLE_SERVER,
						new String[] {mUrl}, new int[] {Types.VARCHAR});
			}
			return 0;
		}
	}
}

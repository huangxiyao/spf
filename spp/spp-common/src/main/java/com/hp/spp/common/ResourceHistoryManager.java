package com.hp.spp.common;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.hp.spp.common.util.hibernate.HibernateUtil;

public class ResourceHistoryManager {

	private static Logger logger = Logger.getLogger(ResourceHistoryManager.class);

	/**
	 * Insert value into the resource history table This method will use an existing connection
	 * 
	 * @param connection
	 * @param owner
	 * @param comment
	 * @param dataChange
	 * @param historyType
	 * @return
	 * @throws Exception
	 */
	public void updateResourceHistory(String owner, String comment, String dataChange,
			long siteId, byte[] historyContent, int historyType) throws Exception {

		// create a backup in the database if enabled
		ResourceBundle servicesWebProp = ResourceBundle.getBundle("SPP_ServicesWeb");
		if (logger.isDebugEnabled()) {
			logger.debug("Content of the SPP_ServicesWeb.properties file is :");
			Enumeration props = servicesWebProp.getKeys();
			while (props.hasMoreElements()) {
				String key = (String) props.nextElement();
				logger.debug("[" + key + "] [" + servicesWebProp.getObject(key) + "]");
			}
		}
		if ("1".equals(servicesWebProp.getString("ENABLE_RESOURCE_HISTORY"))) {
			if (logger.isDebugEnabled()) {
				logger.debug("Resource history is enabled");
			}
			// the insertion into the resource history table can use the existing hibernate
			// connection
			// or create a new one, depending on a parameter in a config file

			Connection conn = null;
			if ("1".equals(servicesWebProp.getString("ENABLE_NATIVE_JDBC_CONNECTION"))) {
				if (logger.isDebugEnabled()) {
					logger.debug("Use native JDBC connection");
				}
				InitialContext ctx = new InitialContext();
				// TODO : get the JNDI name from the hibernate configuration
				DataSource ds = (DataSource) ctx.lookup("jdbc/SPP_PORTALDS");
				// get the connection
				conn = ds.getConnection();
				insertIntoTable(conn, owner, comment, dataChange, siteId, historyContent,
						historyType);
				conn.close();
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Use Hibernate connection");
				}
				conn = HibernateUtil.getSessionFactory().getCurrentSession().connection();
				insertIntoTable(conn, owner, comment, dataChange, siteId, historyContent,
						historyType);
				// dont close the connection, the hiberate connection will be close at the end
				// of this method
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Resource history is NOT enabled");
			}
		}
	}

	private void insertIntoTable(Connection connection, String owner, String comment,
			String dataChange, long siteId, byte[] historyContent, int modificationType) {

		if (logger.isDebugEnabled()) {
			logger.debug("Update the ressource history");
		}

		// manage the insert into the table with native SQL (to avoid the hibernate bug)
		try {
			ByteArrayInputStream baisDataChange = new ByteArrayInputStream(dataChange
					.getBytes());
			ByteArrayInputStream baisBackup = new ByteArrayInputStream(historyContent);

			// get the new sequence id
			String sqlSequence = "SELECT SPP_RESOURCE_HISTORY_ID_SEQ.nextVal FROM DUAL";
			PreparedStatement statementSequence = connection.prepareStatement(sqlSequence);

			ResultSet rs = statementSequence.executeQuery();
			long newId = 0;
			while (rs.next()) {
				newId = rs.getLong(1);
			}

			// Prepare the SQL query
			String sqlInsert = "INSERT INTO SPP_RESOURCE_HISTORY (ID, SITE_ID, OWNER, MODIFICATION_COMMENT, MODIFICATION_TYPE, CREATION_DATE, BACKUP_FILE) VALUES (?, ?, ?, ?, ?, SYSDATE, ?)";
			PreparedStatement statementInsert = connection.prepareStatement(sqlInsert);

			// insert values
			statementInsert.setLong(1, newId);
			statementInsert.setLong(2, siteId);
			statementInsert.setString(3, owner);
			statementInsert.setString(4, comment);
			statementInsert.setInt(5, modificationType);
			statementInsert.setBinaryStream(6, baisBackup, historyContent.length);

			statementInsert.executeUpdate();

			// update datachange
			String sqlUpdate = "UPDATE SPP_RESOURCE_HISTORY SET DATA_CHANGE = ? WHERE ID = ?";
			PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate);

			statementUpdate.setBinaryStream(1, baisDataChange,
					(int) dataChange.getBytes().length);
			statementUpdate.setLong(2, newId);
			statementUpdate.executeUpdate();

			baisDataChange.close();
			baisBackup.close();

		} catch (Exception e) {
			logger.error("Unable to insert the values into the resource history table", e);
		}
	}
}

package com.hp.spp.config;

import org.apache.log4j.Logger;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;
import com.hp.spp.db.DB;
import com.hp.spp.db.DatabaseTransaction;
import com.hp.spp.db.DatabaseException;

/**
 * This class will be deployed in both Vignette and on the WSRP server, and therefore it cannot rely
 * on Hibernate nor on the Vignette database pool objects.
 * <p>
 * DDL:
 * <code>
 * create table spp_config_entry (
 * name varchar2(100) not null,
 * value varchar2(4000) not null,
 * type char default 'S' not null,
 * required char default 'Y' not null,
 * read_only char default 'N' not null,
 * description varchar2(300),
 * constraint spp_config_entry_pk primary key (name)
 * )
 * </code>
 */
public class JDBCConfigEntryDAO implements ConfigEntryDAO, RowMapper<ConfigEntry> {

	private static final Logger mLog = Logger.getLogger(JDBCConfigEntryDAO.class);

	private static final String SQL_LOAD_BY_NAME = "SELECT name, value, type, read_only, required, description FROM spp_config_entry WHERE name = ?";
	private static final String SQL_LOAD_ALL = "SELECT name, value, type, read_only, required, description FROM spp_config_entry ORDER BY name";
	private static final String SQL_DELETE = "DELETE FROM spp_config_entry WHERE name = ?";
	private static final String SQL_UPDATE = "UPDATE spp_config_entry SET value = ?, type = ?, required = ?, read_only = ?, description = ? WHERE name = ?";
	private static final String SQL_INSERT = "INSERT INTO spp_config_entry (name, value, type, required, read_only, description) VALUES (?, ?, ?, ?, ?, ?)";

	private ConfigEntry createConfigEntry(ResultSet row) throws SQLException {
		String name = row.getString("name");
		String value = row.getString("value");
		String typeStr = row.getString("type");
		char type = (typeStr != null && typeStr.length() > 0 ? typeStr.charAt(0) : ConfigEntry.TYPE_STRING);
		boolean required = !"N".equals(row.getString("required"));
		boolean readOnly = "Y".equals(row.getString("read_only"));
		String desc = row.getString("description");
		return new ConfigEntry(name, value, type, required, readOnly, desc);
	}

	public ConfigEntry mapRow(ResultSet row, int rowNum) throws SQLException {
		return createConfigEntry(row);
	}

	public ConfigEntry load(String name) {
		ConfigEntry entry = DB.queryForObject(SQL_LOAD_BY_NAME, this, new String[] {name});
		if (mLog.isDebugEnabled()) {
			if (entry == null) {
				mLog.debug("Unable to load ConfigEntry '" + name + "' - entry not found");
			}
			else {
				mLog.debug("Loaded ConfigEntry: " + entry);
			}
		}
		return entry;
	}

	public void save(final ConfigEntry entry) {
		DatabaseTransaction<Object> tx = new DatabaseTransaction<Object>() {
			protected Object doInTransaction() throws Exception {
				List result =
						query(SQL_LOAD_BY_NAME, JDBCConfigEntryDAO.this, 0, 1, new String[] {entry.getName()}, null);

				boolean existingEntry = (result != null && result.size() > 0);

				if (existingEntry) {
					if (mLog.isDebugEnabled()) {
						mLog.debug("Updating ConfigEntry: " + entry);
					}
					update(SQL_UPDATE,
							new Object[] {
									entry.getValue(),
									String.valueOf(entry.getType()),
									entry.isRequired() ? "Y" : "N",
									entry.isReadOnly() ? "Y" : "N",
									entry.getDescription(),
									entry.getName()
							},
							null);
				}
				else {
					if (mLog.isDebugEnabled()) {
						mLog.debug("Creating ConfigEntry: " + entry);
					}
					update(SQL_INSERT,
							new Object[] {
									entry.getName(),
									entry.getValue(),
									String.valueOf(entry.getType()),
									entry.isRequired() ? "Y" : "N",
									entry.isReadOnly() ? "Y" : "N",
									entry.getDescription()
							},
							null);
				}
				return null;
			}
		};
		try {
			tx.execute();
		}
		catch (DatabaseException e) {
			throw e;
		}
		catch (Exception e) {
			// except DatabaseException we don't expect any other exception here
			throw new RuntimeException("Error executing save", e);
		}
	}

	public void delete(String name) {
		DB.update(SQL_DELETE, new String[] {name}, null);
	}

	public List getAllEntries() {
		if (mLog.isDebugEnabled()) {
			mLog.debug("Loading all config entries");
		}
		return DB.query(SQL_LOAD_ALL, this);
	}

}

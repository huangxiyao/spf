package com.hp.spp.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Maps each row of a database query into a map.
 * The keys in the map are case insensitive and they correspond to the names of the columns selected
 * by the query.
 */
public class RowToMapMapper implements RowMapper<Map<String, Object>> {
	private List<String> mColumnNames = null;

	public Map<String, Object> mapRow(ResultSet row, int rowNum) throws SQLException {
		List<String> columnNames = getColumnNames(row);
		Map<String, Object> rowAsMap = new CaseInsentsitiveKeysHashMap();
		for (int i = 1, columnCount = columnNames.size(); i <= columnCount; ++i) {
			rowAsMap.put(columnNames.get(i-1), (row.getObject(i) != null) ? row.getObject(i) : "");
		}
		return rowAsMap;
	}

	private List<String> getColumnNames(ResultSet rs) throws SQLException {
		if (mColumnNames == null) {
			ResultSetMetaData rsmd = rs.getMetaData();
			mColumnNames = new ArrayList<String>(rsmd.getColumnCount());
			for (int i = 1, len = rsmd.getColumnCount(); i <= len; ++i) {
				mColumnNames.add(rsmd.getColumnName(i));
			}
		}
		return mColumnNames;
	}

	public static class CaseInsentsitiveKeysHashMap extends HashMap<String, Object> {

		private String canonicalizeKey(Object key) {
			return key == null ? null : key.toString().toUpperCase();
		}

		@Override
		public boolean containsKey(Object key) {
			return super.containsKey(canonicalizeKey(key));
		}

		@Override
		public Object get(Object key) {
			return super.get(canonicalizeKey(key));
		}

		@Override
		public Object remove(Object key) {
			return super.remove(canonicalizeKey(key));
		}

		@Override
		public Object put(String key, Object value) {
			return super.put(canonicalizeKey(key), value);
		}
	}
}

package com.hp.spp.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Maps each row of the database query into a list.
 * The list elements are and their order correspond to the elements and the order of the values
 * returned by the query for each row.
 */
public class RowToListMapper implements RowMapper<List> {
	private int mColumnCount = -1;

	public List mapRow(ResultSet row, int rowNum) throws SQLException {
		int columnCount = getColumnCount(row);
		List<Object> rowAsList = new ArrayList<Object>(columnCount);
		for (int i = 1; i <= columnCount; ++i) {
			rowAsList.add((row.getObject(i) != null) ? row.getObject(i) : "");
		}
		return rowAsList;
	}

	private int getColumnCount(ResultSet rs) throws SQLException {
		if (mColumnCount == -1) {
			mColumnCount = rs.getMetaData().getColumnCount();
		}
		return mColumnCount;
	}

}

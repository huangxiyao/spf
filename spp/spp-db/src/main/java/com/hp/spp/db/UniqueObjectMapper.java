package com.hp.spp.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Retrieve uniquely the first object of the row
 */
public class UniqueObjectMapper implements RowMapper<Object> {

	public Object mapRow(ResultSet row, int rowNum) throws SQLException {
		return row.getObject(1);
	}

}

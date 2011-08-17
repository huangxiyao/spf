package com.hp.spp.portal.common.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;

public class StringLocatorMapper implements RowMapper<StringLocator> {
	public StringLocator mapRow(ResultSet row, int rowNum) throws SQLException {
		return new StringLocator(row.getString("COUNTRYCODE"), row
				.getString("LANGUAGECODE"), row.getString("STR_LOC"));
	}

}

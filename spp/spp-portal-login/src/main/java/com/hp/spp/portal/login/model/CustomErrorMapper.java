package com.hp.spp.portal.login.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;

public class CustomErrorMapper implements RowMapper<CustomError> {
	public CustomError mapRow(ResultSet row, int rowNum) throws SQLException {
		return new CustomError(row.getString("PORTAL"), row.getInt("ERROR_CODE"), row
				.getString("LOCALE"), row.getString("CUSTOM_ERROR_MESSAGE"));
	}

}

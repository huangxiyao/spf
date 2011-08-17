package com.hp.spp.portal.login.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;

public class LoginLabelMapper implements RowMapper<LoginLabel> {
	public LoginLabel mapRow(ResultSet row, int rowNum) throws SQLException {
		return new LoginLabel(row.getString("LOCALE"), row.getString("LABEL"),
				row.getString("MESSAGE"),row.getString("SITE_NAME"));
	}

}

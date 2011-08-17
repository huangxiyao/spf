package com.hp.spp.portal.common.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;

public class UserMapper implements RowMapper<User> {
	public User mapRow(ResultSet row, int rowNum) throws SQLException {
		return new User(row.getString("USER_NAME"), row.getString("LAST_NAME"),
				row.getString("COUNTRY"));
	}

}

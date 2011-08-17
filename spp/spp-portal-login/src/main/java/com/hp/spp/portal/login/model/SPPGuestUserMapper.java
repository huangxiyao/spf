package com.hp.spp.portal.login.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;

public class SPPGuestUserMapper implements RowMapper<SPPGuestUser> {
	public SPPGuestUser mapRow(ResultSet row, int rowNum) throws SQLException {
		return new SPPGuestUser(row.getString("LANGUAGE_CODE"), row.getString("COUNTRY_CODE"), row
				.getString("SITE_IDENTIFIER"), row.getString("GUEST_USER"),row.getString("PREFERRED_LANGUAGE_CODE"));
	}

}

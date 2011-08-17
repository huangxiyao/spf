package com.hp.spp.portal.common.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;

public class SiteMapper implements RowMapper<Site> {
	public Site mapRow(ResultSet row, int rowNum) throws SQLException {
		return new Site(row.getInt("ID"), row.getString("NAME"), row
				.getString("LANDING_PAGE"), row.getInt("LOCALE_IN_URL"), row
				.getString("HOME_PAGE"), row.getString("PORTLET_ID"), row
				.getString("HPAPPID"), row.getString("LOGOUT_PAGE"), row
				.getString("UPS_QUERY_ID"), row.getString("PROTOCOL"), row
				.getInt("PERSIST_SIMULATION"),
				row.getString("SITE_IDENTIFIER"), row.getInt("ACCESS_SITE"),
				row.getString("ACCESS_CODE"), row.getString("STOP_SIMULATION_PAGE"));
	}

}

package com.hp.spp.portal.login.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;

public class WorkflowMapper implements RowMapper<Workflow> {
	public Workflow mapRow(ResultSet row, int rowNum) throws SQLException {
		return new Workflow(row.getString("PORTAL"), row
				.getLong("ERROR_CODE"), row.getString("TARGET_URL"), 
				row.getInt("DISPLAY_MESSAGE"));
	}

}

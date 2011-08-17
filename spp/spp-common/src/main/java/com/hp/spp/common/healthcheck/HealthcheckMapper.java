package com.hp.spp.common.healthcheck;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;
import com.hp.spp.common.healthcheck.HealthcheckServerInfo;

public class HealthcheckMapper implements RowMapper<HealthcheckServerInfo> {
	public HealthcheckServerInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		boolean outOfRotationFlag = "Y".equals(row.getString("OUT_OF_ROTATION_FLAG")) ? true : false;
		return new HealthcheckServerInfo(
				row.getString("SERVER_NAME"),
				row.getString("APPLICATION_NAME"),
				row.getString("SERVER_TYPE"),
				row.getString("SITE_NAME"),
				outOfRotationFlag);	
	}
}
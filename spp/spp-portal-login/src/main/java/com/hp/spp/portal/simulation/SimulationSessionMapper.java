package com.hp.spp.portal.simulation;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;

public class SimulationSessionMapper implements RowMapper<SimulationSession> {
	public SimulationSession mapRow(ResultSet row, int rowNum) throws SQLException {
		return new SimulationSession(row.getString("SITE"), row
				.getString("HPPID_SIMULATOR"), row.getString("HPPID_SIMULATED"));
	}

}

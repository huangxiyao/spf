package com.hp.spp.portal.login.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.spp.db.RowMapper;

public class UPSRuleMapper implements RowMapper<UPSRule> {
	public UPSRule mapRow(ResultSet row, int rowNum) throws SQLException {
		return new UPSRule(row.getString("RULE_TYPE"), row.getString("RULE_CLASSES"), 
				row.getString("SITE_IDENTIFIER"));
	}
}

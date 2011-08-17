package com.hp.spp.filters.access;

import com.hp.spp.db.RowMapper;
import com.hp.spp.db.DB;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUrlAccessRuleDAO implements UrlAccessRuleDAO, RowMapper<UrlAccessRule> {

	public List findUrlAccessRules(String ruleSetName) {
		return DB.query("select url_pattern, access_type, data from spp_url_access_rule where rule_set = ? order by ord",
				this, new String[] {ruleSetName});
	}

	public UrlAccessRule mapRow(ResultSet row, int rowNum) throws SQLException {
		String access = row.getString("access_type").toLowerCase();
		String pattern = row.getString("url_pattern");
		if ("allow".equals(access)) {
			return new AllowRule(pattern);
		}
		else if ("deny".equals(access)) {
			return new DenyRule(pattern);
		}
		else if ("ldap".equals(access)) {
			return new LdapRule(pattern);
		}
		else if ("ldapacl".equals(access)) {
			return new LdapAclRule(pattern, row.getString("data"));
		}
		else if ("simldap".equals(access)) {
			return new SimulationLdapRule(pattern);
		}
		else {
			throw new IllegalArgumentException("Don't know how to handle access type: " + row.getString("access_type"));
		}
	}

}

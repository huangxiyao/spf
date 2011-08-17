package com.hp.spp.filters.access;

import java.util.List;

public interface UrlAccessRuleDAO {
	List findUrlAccessRules(String ruleSetName);
}

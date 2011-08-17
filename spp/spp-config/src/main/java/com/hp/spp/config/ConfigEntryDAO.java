package com.hp.spp.config;

import java.util.List;

public interface ConfigEntryDAO {
	ConfigEntry load(String name);
	void save(ConfigEntry entry);
	void delete(String name);
	List getAllEntries();
}

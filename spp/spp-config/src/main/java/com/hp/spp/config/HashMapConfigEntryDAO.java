package com.hp.spp.config;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

public class HashMapConfigEntryDAO implements ConfigEntryDAO {
	private Map mMap = new TreeMap();

	public ConfigEntry load(String name) {
		return (ConfigEntry) mMap.get(name);
	}

	public void save(ConfigEntry entry) {
		mMap.put(entry.getName(), entry);
	}

	public void delete(String name) {
		mMap.remove(name);
	}

	public List getAllEntries() {
		return new ArrayList(mMap.values());
	}
}

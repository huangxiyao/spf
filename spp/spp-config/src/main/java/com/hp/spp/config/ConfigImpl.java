package com.hp.spp.config;

import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ConfigImpl {
	private ConfigEntryDAO mDAO;

	ConfigImpl(ConfigEntryDAO dao) {
		mDAO = dao;
	}

	/**
	 * Loads entry throwing exception if the entry cannot be found.
	 */
	private ConfigEntry loadEntry(String name) throws ConfigException {
		ConfigEntry entry = mDAO.load(name);
		if (entry == null) {
			throw new ConfigException("Config entry '" + name + "' not found!");
		}
		return entry;
	}

	/**
	 * Gets the value of the entry having given name. If the entry cannot be found, an exception is thrown.
	 * @param name name of the entry
	 * @return entry value
	 * @throws ConfigException If entry with such a name cannot be found
	 */
	public String getValue(String name) throws ConfigException {
		return loadEntry(name).getValue();
	}

	/**
	 * Gets the value of the entry having given name or the <tt>defaultValue</tt> if the entry could
	 * not be found.
	 * @param name name of the entry to query
	 * @param defaultValue default value used if entry doesn't exist
	 * @return entry value or given default value if entry doesn't exist
	 */
	public String getValue(String name, String defaultValue) {
		ConfigEntry entry = mDAO.load(name);
		if (entry == null || entry.getValue() == null) {
			return defaultValue;
		}
		else {
			return entry.getValue();
		}
	}

	/**
	 * Gets the integer value of the entry having given name. The type of the entry doesn't have to
	 * be {@link ConfigEntry#TYPE_INT}. If the value cannot be converted to integer 0 will be returned.
	 * @param name name of the entry to query
	 * @return value of the entry with the given name
	 * @throws ConfigException If entry with the given name cannot be found
	 */
	public int getIntValue(String name) throws ConfigException {
		return loadEntry(name).getIntValue();
	}

	/**
	 * Gets the integer value of the entry having given <tt>name</tt> or the <tt>defaultValue</tt>
	 * if the entry could not be found or its type is not {@link ConfigEntry#TYPE_INT}.
	 * @param name name of the entry
	 * @param defaultValue default value used if entry doesn't exist
	 * @return entry integer value or default value if the entry doesn't exist
	 */
	public int getIntValue(String name, int defaultValue) {
		ConfigEntry entry = mDAO.load(name);
		if (entry == null || (entry.getType() != ConfigEntry.TYPE_INT)) {
			return defaultValue;
		}
		else {
			return entry.getIntValue();
		}
	}

	/**
	 * Gets the boolean value of the entry having given <tt>name</tt>. The type of the entry doesn't
	 * have to be {@link ConfigEntry#TYPE_BOOLEAN}.
	 * @param name name of the entry
	 * @return entry boolean value
	 * @throws ConfigException If entry with such a <tt>name</tt> could not be found
	 * @see Boolean#valueOf(String)
	 */
	public boolean getBooleanValue(String name) throws ConfigException {
		return loadEntry(name).getBooleanValue();
	}

	/**
	 * Gets the boolean value of the entry having given <tt>name</tt> or the <tt>defaultValue</tt>
	 * if the entry could not be found or its type is not {@link ConfigEntry#TYPE_BOOLEAN}.
	 * @param name name of the entry
	 * @param defaultValue default value used if entry doesn't exist
	 * @return entry boolean value of default value if entry doesn't exist
	 */
	public boolean getBooleanValue(String name, boolean defaultValue) {
		ConfigEntry entry = mDAO.load(name);
		if (entry == null || (entry.getType() != ConfigEntry.TYPE_BOOLEAN)) {
			return defaultValue;
		}
		else {
			return entry.getBooleanValue();
		}
	}

	/**
	 * @return All config entries. The default DAO implementation will order them ascending by name.
	 */
	public Iterator getEntries() {
	   return Collections.unmodifiableList(mDAO.getAllEntries()).iterator();
	}

	/**
	 * Returns the list of entries maching the name. Use '*' as a wildcard character
	 * @param namePattern pattern that can contain '*' to mark any character set
	 * @return iterator with entries matching the pattern
	 */
	public Iterator getEntries(String namePattern) {
	   Pattern pattern = convertToPattern(namePattern);
	   Iterator allEntries = mDAO.getAllEntries().iterator();
	   List filteredEntries = new ArrayList();
	   while (allEntries.hasNext()) {
		  ConfigEntry entry = (ConfigEntry) allEntries.next();
		  if (pattern.matcher(entry.getName()).matches()) {
			 filteredEntries.add(entry);
		  }
	   }
	   return Collections.unmodifiableList(filteredEntries).iterator();
	}

	private Pattern convertToPattern(String namePattern) {
	   String regex = namePattern.replaceAll("\\.", "\\.").replaceAll("\\*", ".*");
	   return Pattern.compile(regex);
	}

	/**
	 * @param entryName name of the entry to retrieve
	 * @return config entry if the given <tt>name</tt> or <tt>null</tt> if entry cannot be found
	 */
	public ConfigEntry get(String entryName) {
	   return mDAO.load(entryName);
	}

	/**
	 * Saves (creates or updates) the entry.
	 * @param entry entry to save
	 * @throws ConfigException If attempting to update read-only entry.
	 */
	public void set(ConfigEntry entry) throws ConfigException {
	   ConfigEntry oldEntry = mDAO.load(entry.getName());
	   if (oldEntry != null && oldEntry.isReadOnly()) {
		  throw new ConfigException("Cannot update read-only entry: " + entry.getName());
	   }
	   else {
		  mDAO.save(entry);
	   }
	}

}

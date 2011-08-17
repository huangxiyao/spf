package com.hp.spp.config;

public class ConfigEntry implements Comparable {
	public static final char TYPE_STRING = 'S';
	public static final char TYPE_INT = 'I';
	public static final char TYPE_BOOLEAN = 'B';

	private static boolean DEFAULT_READONLY = false;
	private static boolean DEFAULT_REQUIRED = true;

	/**
	 * Name of the config entry. Name is the entry key and therefore it is unique in the whole
	 * configuration.
	 */
	private String mName;

	/**
	 * String value of the config entry.
	 */
	private String mValue = null;

	/**
	 * Integer value of the config entry or 0 if a String or Boolean-typed value cannot be converted
	 * to integer.
	 */
	private int mIntValue = 0;

	/**
	 * Boolean value of the config entry.
	 * @see Boolean#valueOf(String) used for from-string convertion
	 */
	private boolean mBooleanValue = false;

	/**
	 * Flag showing whether the config value must be specified, i.e. the value cannot be null.
	 * This is field is part of config entry metadata that can be used in the UI allowing to
	 * edit configuration.
	 */
	private boolean mRequired = true;

	/**
	 * Flag indicating whether the value is read only, i.e. it cannot be updated through the API
	 * once it was created. In such a case the updates can be done directly in the database.
	 * This is field is part of config entry metadata that can be used in the UI allowing to
	 * edit configuration.
	 */
	private boolean mReadOnly = false;

	/**
	 * Config entry type. Can be one for <tt>TYPE_xxx</tt> constants.
	 * This is field is part of config entry metadata that can be used in the UI allowing to
	 * edit configuration.
	 */
	private char mType = TYPE_STRING;

	/**
	 * Config entry description. Should contain the purpose of the config entry and if it allows only
	 * specific values, those values should be listed here.
	 * This is field is part of config entry metadata that can be used in the UI allowing to
	 * edit configuration.
	 */
	private String mDescription;

	
	public ConfigEntry(String name, String value, char type, boolean required, boolean readOnly, String description) {
		if (name == null || name.trim().equals("")) {
			throw new IllegalArgumentException("ConfigEntry name cannot be null nor empty!");
		}
		if (required && value == null) {
			throw new IllegalArgumentException("Required value cannot be null!");
		}
		if (type != TYPE_STRING && type != TYPE_INT && type != TYPE_BOOLEAN) {
			throw new IllegalArgumentException("Illegal type: " + type);
		}

		mName = name;
		mRequired = required;
		mReadOnly = readOnly;
		mType = type;
		mDescription = description;

		if (value != null) {
			mValue = value;
			try {
				mIntValue = Integer.parseInt(value);
			}
			catch (NumberFormatException e) {
				mIntValue = 0;
			}
			mBooleanValue = Boolean.valueOf(value).booleanValue();
		}
	}

	public ConfigEntry(String name, String value, String description) {
		this(name, value, TYPE_STRING, DEFAULT_REQUIRED, DEFAULT_READONLY, description);
	}

	public ConfigEntry(String name, int intValue, boolean required, boolean readOnly, String description) {
		mName = name;
		mIntValue = intValue;
		mRequired = required;
		mReadOnly = readOnly;
		mType = TYPE_INT;
		mValue = Integer.toString(intValue);
		mBooleanValue = Boolean.valueOf(mValue).booleanValue();
		mDescription = description;
	}

	public ConfigEntry(String name, int intValue, String description) {
		this(name, intValue, DEFAULT_REQUIRED, DEFAULT_READONLY, description);
	}

	public ConfigEntry(String name, boolean booleanValue, boolean required, boolean readOnly, String description) {
		mName = name;
		mBooleanValue = booleanValue;
		mRequired = required;
		mReadOnly = readOnly;
		mType = TYPE_BOOLEAN;
		mValue = Boolean.toString(booleanValue);
		mIntValue = 0;
		mDescription = description;
	}

	public ConfigEntry(String name, boolean booleanValue, String description) {
		this(name, booleanValue, DEFAULT_REQUIRED, DEFAULT_READONLY, description);
	}

	public String getName() {
		return mName;
	}

	public boolean isRequired() {
		return mRequired;
	}

	public boolean isReadOnly() {
		return mReadOnly;
	}

	public char getType() {
		return mType;
	}

	public String getValue() {
		return mValue;
	}

	public int getIntValue() {
		return mIntValue;
	}

	public boolean getBooleanValue() {
		return mBooleanValue;
	}

	public String getDescription() {
		return mDescription;
	}

	public String toString() {
		return
				"ConfigEntry={name=" + mName + ",value=" + mValue + ",type=" + mType +
						",required=" + mRequired + ",readOnly=" + mReadOnly + "}";
	}

	public int compareTo(Object o) {
		return mName.compareTo(((ConfigEntry) o).mName);
	}
}

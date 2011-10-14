package com.hp.it.cas.persona.uav.service;


/**
 * The user identifier types understood by Persona.
 *
 * @author Quintin May
 */
public enum EUserIdentifierType {
	/**
	 * An HP Passport user identifier.
	 */
	EXTERNAL_USER("EXT", "HP Passport"),
	
	/**
	 * An HP employee number.
	 */
	EMPLOYEE("EMP", "HP EID"),
	
	/**
	 * An HP employee's Simplified Email Address (j.doe@hp.com)
	 */
	EMPLOYEE_SIMPLIFIED_EMAIL_ADDRESS("EMP_SEA", "HP Email");
	
	private final String userIdentifierTypeCode;
	private final String userIdentifierName;
	
	private EUserIdentifierType(String userIdentifierTypeCode, String userIdentifierName) {
		this.userIdentifierTypeCode = userIdentifierTypeCode;
		this.userIdentifierName = userIdentifierName;
	}
	
	/**
	 * An internal code used to serialize a user identifier.
	 * @return the unique code that represents this user identifier type in a data store.
	 */
	public String getUserIdentifierTypeCode() {
		return userIdentifierTypeCode;
	}
	
	/**
	 * Returns a name for this user identifier type appropriate for use in a user interface.
	 * @return a human friendly name.
	 */
	public String getUserIdentifierName() {
		return userIdentifierName;
	}
	
	/**
	 * Returns the enum constant of this type with the specified type code.
	 * @param userIdentifierTypeCode the type code of the constant to return.
	 * @return the enum constant of this type with the user identifier type.
	 * @throws IllegalArgumentException if the user identifier type does not represent an enum type.
	 */
	public static EUserIdentifierType valueOfUserIdentifierTypeCode(String userIdentifierTypeCode) {
		EUserIdentifierType result = null;
		
		EUserIdentifierType[] values = values();		
		for (int i = 0, ii = values.length; i < ii && result == null; ++i) {
			if (values[i].userIdentifierTypeCode.equals(userIdentifierTypeCode)) {
				result = values[i];
			}
		}
		
		if (result == null) {
			StringBuilder sb = new StringBuilder();
			String delimiter = "";
			for (EUserIdentifierType type : values()) {
				sb.append(delimiter).append(type.getUserIdentifierTypeCode());
				delimiter = ", ";
			}
			throw new IllegalArgumentException(String.format("User identifier type code '%s' is invalid. Valid type codes are %s.", userIdentifierTypeCode, sb));
		}
		
		return result;
	}
}

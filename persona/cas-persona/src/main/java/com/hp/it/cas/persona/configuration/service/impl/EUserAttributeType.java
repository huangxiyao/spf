package com.hp.it.cas.persona.configuration.service.impl;

enum EUserAttributeType {
	SIMPLE("SMPL"),
	COMPOUND("CMPND");
	
	private final String userAttributeTypeCode;
	
	private EUserAttributeType(String userAttributeTypeCode) {
		this.userAttributeTypeCode = userAttributeTypeCode;
	}
	
	String getUserAttributeTypeCode() {
		return userAttributeTypeCode;
	}
	
	static EUserAttributeType valueOfUserAttributeTypeCode(String userAttributeTypeCode) {
		EUserAttributeType result = null;
		
		EUserAttributeType[] values = values();		
		for (int i = 0, ii = values.length; i < ii && result == null; ++i) {
			if (values[i].userAttributeTypeCode.equals(userAttributeTypeCode)) {
				result = values[i];
			}
		}
		
		return result;
	}
}

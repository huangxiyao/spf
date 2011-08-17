package com.hp.spp.groups.expression;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents an equality.
 * 
 * @author bmollard
 * 
 */
public class Equality implements Expression {

	/**
	 * Name of the attribute.
	 */
	private String mAttributeName;

	/**
	 * Value of the attribute.
	 */
	private String mAttributeValue;

	/**
	 * constructor.
	 */
	public Equality() {
		//Call this to intialize empty attributes
		this.setAttributeValue(mAttributeValue);	
	}

	public String getAttributeName() {
		return mAttributeName;
	}

	public void setAttributeName(String attributeName) {
		mAttributeName = attributeName;
	}

	public String getAttributeValue() {
		return mAttributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		//Convert null attribute values into empty strings since castor generates
		// empty values as null
		mAttributeValue = (attributeValue == null) ? "": attributeValue;
	}

	/**
	 * Return true if the user context correpsonds to this aquality.
	 * 
	 * @return true if OK for the user context
	 * @param userContext user Context
	 * @param siteName name of the site
	 */
	public boolean isTrue(Map userContext, String siteName) {
		/*
		 * List values = (List) userContext.get(mAttributeName); if (values == null) { throw
		 * new IllegalArgumentException("Attribute '" + mAttributeName + "' not found in the
		 * user context!"); } return values.isEmpty() && mAttributeValue == null ||
		 * mAttributeValue.equals(values.get(0));
		 */
		boolean isTrue = false;
		
		if (!userContext.containsKey(mAttributeName)){
			throw new IllegalArgumentException("Attribute '" + mAttributeName
					+ "' not found in the user context!");
		} else{
				String value = (String) userContext.get(mAttributeName);
				//Treat null values in profile as valid strings
				value = (value ==  null? "": value);
				// the value can be a single value or a list of values,
				// separated by semi-colons
				String[] valueList = value.split(";");
				for (int i = 0; i < valueList.length; i++) {
					if ((mAttributeValue != null) 
						&& (mAttributeValue.equals(valueList[i]))) {
						isTrue = true;
						break;
				}
			}
		}
		return isTrue;
	}

	public void getInGroupNames(ArrayList list) {
		// nothing to do in this case
	}
}

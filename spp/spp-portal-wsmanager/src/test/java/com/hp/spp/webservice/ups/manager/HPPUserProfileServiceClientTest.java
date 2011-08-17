package com.hp.spp.webservice.ups.manager;

import junit.framework.TestCase;
import com.hp.spp.webservice.ups.client.AttributeGroup;
import com.hp.spp.webservice.ups.client.Attribute;

public class HPPUserProfileServiceClientTest extends TestCase {

	/**
	 * This method tests if the language returned is null with a PathInfo null or "".
	 *
	 * @throws Exception
	 */
	public void testGetAttributeGroups() throws Exception {
		String[] attributeNames = {"UserId", "ProfileId"};
		String[] attributeValues = {"sppuser", "12345"};
		AttributeGroup[] attributeGroups = new HPPUserProfileServiceClient().getAttributeGroups(attributeNames, attributeValues);
		Attribute[] attributes = attributeGroups[0].getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			Attribute attribute  = attributes[i];
			System.out.println(attribute.getAttributeId());
			assertEquals(attributeNames[i], attribute.getAttributeId());
			System.out.println(attribute.getAttributeValues(0));
			assertEquals(attributeValues[i], attribute.getAttributeValues(0));
		}
	}


}

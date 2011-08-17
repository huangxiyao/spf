package com.hp.spp.wsrp.export;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hp.spp.db.DB;
import com.hp.spp.wsrp.export.helper.ProducerTestHelper;
import com.hp.spp.wsrp.export.info.producer.PortletInfo;
import com.hp.spp.wsrp.export.info.producer.PreferenceInfo;
import com.hp.spp.wsrp.export.info.producer.ProducerInfo;

public class WsrpProducerPreferenceExportTest extends Test {

	protected void setUp() throws Exception {
		ProducerTestHelper.setupTestDataSource();
		ProducerTestHelper.setupTestDatabase();
		setupTestData();
	}

	public void testGetProducerInfo() throws Exception {
		WsrpProducerPreferenceExport export = new WsrpProducerPreferenceExport();
		ProducerInfo producerInfo;

		String xml = "<portlet-handle-list><producer-info><url>http://abc</url><import-id>abc</import-id></producer-info></portlet-handle-list>";
		producerInfo = export.getProducerInfo(readInputStr(xml));
		assertNotNull("Producer info is not null", producerInfo);
		assertEquals("Producer url", "http://abc", producerInfo.getUrl());
		assertEquals("Producer import id", "abc", producerInfo.getImportId());

		try {
			producerInfo = export.getProducerInfo(readInputStr("<portlet-handle-list></portlet-handle-list>"));
			fail("IllegalArgumentException expected when producer info not found");
		}
		catch (IllegalArgumentException e) {
			// this is expected
			assertTrue(true);
		}

		try {
			producerInfo = export.getProducerInfo(readInputStr("<portlet-handle-list><producer-info><url>http://abc</url></producer-info></portlet-handle-list>"));
			fail("IllegalArgumentException expected when uncomplete producer info found");
		}
		catch (IllegalArgumentException e) {
			// this is expected
			assertTrue(true);
		}
	}

	public void testCopyProducerInfo() throws Exception {
		WsrpProducerPreferenceExport export = new WsrpProducerPreferenceExport();

		String xml = "<portlet-handle-list><producer-info><url>http://abc</url><import-id>abc</import-id></producer-info></portlet-handle-list>";
		Document doc = export.export(readInputStr(xml));
		NodeList nl = doc.getElementsByTagName("producer-info");
		assertTrue("Producer info found in the result document", nl != null && nl.getLength() == 1);
	}

	public void testGetPortletHandles() throws Exception {
		WsrpProducerPreferenceExport export = new WsrpProducerPreferenceExport();
		List portletHandles;

		String xml = "<portlet-handle-list><handle>abc</handle><handle>def</handle></portlet-handle-list>";
		portletHandles = export.getPortletHandles(readInputStr(xml));
		assertNotNull("Returned list is not null", portletHandles);
		assertEquals("Returned list has 2 elements", 2, portletHandles.size());
		assertEquals("Returned list content", Arrays.asList(new String[]{"abc", "def"}), portletHandles);

		try {
			portletHandles = export.getPortletHandles(readInputStr("<portlet-handle-list></portlet-handle-list>"));
			fail("IllegalArgumentException expected when no portlet handles found in the input");
		}
		catch (IllegalArgumentException e) {
			// expected when no handles present in the input
			assertTrue(true);
		}

		try {
			portletHandles = export.getPortletHandles(readInputStr("<portlet-handle-list><handle>abc</handle><handle>\n</handle></portlet-handle-list>"));
			fail("IllegalArgumentException expected when empty portlet handle found");
		}
		catch (IllegalArgumentException e) {
			// expected
			assertTrue(true);
		}
	}

	public void testSplitIntoSubLists() throws Exception {
		WsrpProducerPreferenceExport export = new WsrpProducerPreferenceExport();

		List list = Arrays.asList(new String[] {"1", "2", "3", "4", "5"});
		List subLists = export.splitIntoSubLists(list, 2);
		assertEquals("Number of sublists after the split", 3, subLists.size());
		assertEquals("Sublist 1", Arrays.asList(new String[] {"1", "2"}), subLists.get(0));
		assertEquals("Sublist 2", Arrays.asList(new String[] {"3", "4"}), subLists.get(1));
		assertEquals("Sublist 3", Arrays.asList(new String[] {"5"}), subLists.get(2));

		list = Arrays.asList(new String[] {"1", "2", "3", "4"});
		subLists = export.splitIntoSubLists(list, 2);
		assertEquals("Number of sublists after the split", 2, subLists.size());
		assertEquals("Sublist 1", Arrays.asList(new String[] {"1", "2"}), subLists.get(0));
		assertEquals("Sublist 2", Arrays.asList(new String[] {"3", "4"}), subLists.get(1));
	}

	public void testGetPortletInfoList() throws Exception {
		setUp();

		WsrpProducerPreferenceExport export = new WsrpProducerPreferenceExport();
		List portletInfoList = export.getPortletInfoList(Arrays.asList(new String[] {"1", "2", "non_existing"}));
		assertNotNull("Portlet info list is not null", portletInfoList);
		assertEquals("Two portlets returned", 2, portletInfoList.size());

		// check the single value preference
		PortletInfo info = (PortletInfo) portletInfoList.get(0);
		assertEquals("Handle 1", "1", info.getHandle());
		Map preferences = info.getPreferences();
		List expectedValueList = Arrays.asList(new String[] {"single value"});
		assertTrue("single_value_pref found", preferences.containsKey("single_value_pref"));
		assertEquals("preference value", expectedValueList, ((PreferenceInfo) preferences.get("single_value_pref")).getValues());
		assertEquals("preference read-only flag", "false", ((PreferenceInfo) preferences.get("single_value_pref")).getReadOnly());

		// check the multi-value preference
		expectedValueList = Arrays.asList(new String[] {"value 1", "value 2"});
		assertEquals("Multi-value preference values", expectedValueList,  ((PreferenceInfo)preferences.get("multi_value_pref")).getValues());
		assertEquals("Multi-value preference read-only flag", "true", ((PreferenceInfo) preferences.get("multi_value_pref")).getReadOnly());

		// check the portlet without preferences
		info = (PortletInfo) portletInfoList.get(1);
		assertEquals("Handle 2", "2", info.getHandle());
		assertTrue("Portlet has no preferences", !info.hasPreferences());
	}

	public void testPortletHandleNotFoundError() throws Exception {
		setUp();

		String xml = "<portlet-handle-list><producer-info><url>http://abc</url><import-id>abc</import-id><handle>non_existing</handle></producer-info></portlet-handle-list>";
		WsrpProducerPreferenceExport export = new WsrpProducerPreferenceExport();
		Document result = export.export(readInputStr(xml));
		NodeList errors = result.getElementsByTagName("error");
		assertEquals("Number of errors found", 1, errors.getLength());
		Element error = (Element) errors.item(0);
		String errorMsg = error.getFirstChild().getNodeValue();

		assertTrue("Handle listed in the error message", errorMsg.indexOf("non_existing") > 0);
	}

	private void setupTestData() {
		// create portlet with 2 preferences
		DB.update("insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values ('1', 'TEST.1', 'a.b', 'TEST')");
		// create single-value preference
		DB.update("insert into WSRP_PREFERENCE (ID, NAME, READ_ONLY, PORTLET) values ('1', 'single_value_pref', 'false', '1')");
		DB.update("insert into WSRP_PREFERENCE_VALUE (PREFERENCE, PREFERENCE_IDX, VALUE) values ('1', '1', 'single value')");
		// create multi-value preference
		DB.update("insert into WSRP_PREFERENCE (ID, NAME, READ_ONLY, PORTLET) values ('2', 'multi_value_pref', 'true', '1')");
		DB.update("insert into WSRP_PREFERENCE_VALUE (PREFERENCE, PREFERENCE_IDX, VALUE) values ('2', '1', 'value 1')");
		DB.update("insert into WSRP_PREFERENCE_VALUE (PREFERENCE, PREFERENCE_IDX, VALUE) values ('2', '2', 'value 2')");

		// create portlet without preferences
		DB.update("insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values ('2', 'TEST.2', 'a.b', 'TEST')");
	}

}

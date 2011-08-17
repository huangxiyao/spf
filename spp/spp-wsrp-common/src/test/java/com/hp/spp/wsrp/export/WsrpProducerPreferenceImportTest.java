package com.hp.spp.wsrp.export;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.hp.spp.db.DB;
import com.hp.spp.db.RowToMapMapper;
import com.hp.spp.wsrp.export.helper.ProducerTestHelper;
import com.hp.spp.wsrp.export.info.producer.PortletInfo;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class WsrpProducerPreferenceImportTest extends Test {

	protected void setUp() throws Exception {
		ProducerTestHelper.setupTestDataSource();
		ProducerTestHelper.setupTestDatabase();
		setupTestData();
	}

	public void testImportTransactionPortletNotFound() throws Exception {
		PortletInfo portletInfo = new PortletInfo("non_existing_handle");
		WsrpProducerPreferenceImport.ImportTransaction tx = new WsrpProducerPreferenceImport.ImportTransaction(portletInfo);
		String result = (String) tx.execute();
		assertNotNull("Result is not null if error occured", result);
	}

	public void testImportTransactionPreferencesUpdated() throws Exception {
		PortletInfo portletInfo = new PortletInfo("1");
		portletInfo.addPreference("single_value_pref", "single value - updated", "false");
		portletInfo.addPreference("multi_value_pref", "value 1 - udpated", "true");
		portletInfo.addPreference("multi_value_pref", "value 2 - udpated", "true");
		portletInfo.addPreference("multi_value_pref", "value 3", "true");
		portletInfo.addPreference("another_pref", "another value", "false");

		WsrpProducerPreferenceImport.ImportTransaction tx = new WsrpProducerPreferenceImport.ImportTransaction(portletInfo);
		String result = (String) tx.execute();
		assertNull("Result is null upon successful import", result);

		List dbContent =
				DB.query("select a.NAME as \"n\", b.VALUE as \"v\", a.READ_ONLY as \"ro\" " +
						"from WSRP_PREFERENCE a left outer join WSRP_PREFERENCE_VALUE b on a.ID = b.PREFERENCE " +
						"where a.PORTLET = ? " +
						"order by a.NAME, b.PREFERENCE_IDX",
				new RowToMapMapper(), new String[] {portletInfo.getHandle()});
		assertEquals("Selected 5 rows", 5, dbContent.size());
		// preferences are sorted alphabetically in the query result
		Iterator it = dbContent.iterator();
		assertPreference((Map) it.next(), "another_pref", "another value", "false");
		assertPreference((Map) it.next(), "multi_value_pref", "value 1 - udpated", "true");
		assertPreference((Map) it.next(), "multi_value_pref", "value 2 - udpated", "true");
		assertPreference((Map) it.next(), "multi_value_pref", "value 3", "true");
		assertPreference((Map) it.next(), "single_value_pref", "single value - updated", "false");
	}

	public void testCreateExportInput() throws Exception {
		String xml =
			"<portlet-preferences>" +
					"<producer-info><url>http://abc</url><import-id>abc</import-id></producer-info>" +
					"<portlet><handle>abc</handle><preference><name>a</name><read-only>false</read-only><value>1</value></preference></portlet>" +
					"<portlet><handle>def</handle><preference><name>a</name><read-only>false</read-only><value>1</value></preference></portlet>" +
					"<portlet><handle>ghi</handle><preference><name>a</name><read-only>false</read-only><value>1</value></preference></portlet>" +
			"</portlet-preferences>";
		Document importInput = DOMUtils.initializeDocument(new StringReader(xml));
		Document result = new WsrpProducerPreferenceImport().createExportInput(importInput);
		NodeList producerInfo = result.getElementsByTagName("producer-info");
		assertTrue("producer-info present in the result", producerInfo != null && producerInfo.getLength() == 1);
		NodeList handles = result.getElementsByTagName("handle");
		assertTrue("handle elements present", handles != null && handles.getLength() > 0);
		assertEquals("Number of handles selected", 3, handles.getLength());
		assertEquals("Handle 1", "abc", DOMUtils.getStringValue(handles.item(0)));
		assertEquals("Handle 2", "def", DOMUtils.getStringValue(handles.item(1)));
		assertEquals("Handle 3", "ghi", DOMUtils.getStringValue(handles.item(2)));
	}

	private void assertPreference(Map row, String name, String value, String readOnly) {
		assertEquals(name, row.get("n"));
		assertEquals(value, row.get("v"));
		assertEquals(readOnly, row.get("ro"));
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

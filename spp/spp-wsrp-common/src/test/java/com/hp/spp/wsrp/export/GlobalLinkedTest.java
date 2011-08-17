package com.hp.spp.wsrp.export;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hsqldb.jdbc.jdbcBlob;
import org.w3c.dom.Document;

import com.hp.spp.db.DB;
import com.hp.spp.wsrp.export.helper.ProducerTestHelper;
import com.hp.spp.wsrp.export.helper.TestHelper;
import com.hp.spp.wsrp.export.helper.VignetteTestHelper;

public class GlobalLinkedTest extends Test {

	protected void setUp() throws Exception {
		TestHelper.setupTestDataSource();
		VignetteTestHelper.setupTestDatabase();
		ProducerTestHelper.setupTestDatabase();
		setupTestData();
	}

	public void testGlobalLinkedProcess() throws Exception {
		setUp() ;

		Export export = null ;
		String xml = null ;
		StringReader reader = null ;
		StringWriter writer = null ;
		List list = null ;
		
		xml = "<wsrp-portlet-uid-list><uid>abc</uid><uid>def</uid><uid>ghi</uid><uid>jkl</uid><uid>mno</uid><uid>pqr</uid><uid>stu</uid><uid>vwx</uid><uid>yza</uid><uid>bcd</uid><uid>efg</uid><uid>hij</uid><uid>klm</uid><uid>nop</uid><uid>qrs</uid><uid>tuv</uid><uid>wxy</uid><uid>zab</uid><uid>cde</uid><uid>fgh</uid><uid>ijk</uid><uid>lmn</uid><uid>opq</uid><uid>rst</uid><uid>uvw</uid><uid>xyz</uid></wsrp-portlet-uid-list>";
		
		export = new RemotePortletHandleExport() ;
		reader = new StringReader(xml) ;
		writer = new StringWriter() ;
		assertNotNull("Reader from the xml is not null", reader) ;
		export.export(reader, writer) ;
		
		assertNotNull("Writer result is not null", writer) ;
		xml = writer.toString() ;
		assertNotNull("XML from Writer is not null", xml) ;
		assertFalse("XML from Writer is not an empty String", "".equals(xml)) ;
		
		export = new ProducerSplitterExport() ;
		reader = new StringReader(xml) ;
		list = new ArrayList() ;
		export.split(reader, list) ;
		
		assertNotNull("Splitted Producer is not null", list) ;
		assertFalse("Splitted Producer is not empty", list.isEmpty()) ;
		
		List preferences = new ArrayList() ;
		export = new WsrpProducerPreferenceExport() ;
		for(Iterator it = list.iterator(); it.hasNext(); ) {
			writer = new StringWriter() ;
			Document doc = (Document) it.next() ;
			assertNotNull("Document is not null", doc) ;
			export.write(doc, writer) ;
			
			assertNotNull("Writer result is not null", writer) ;
			xml = writer.toString() ;
			assertNotNull("XML from Writer is not null", xml) ;
			assertFalse("XML from Writer is not an empty String", "".equals(xml)) ;
			
			reader = new StringReader(xml) ;
			writer = new StringWriter() ;
			assertNotNull("Reader from the xml is not null", reader) ;
			export.export(reader, writer) ;
			
			assertNotNull("Writer result is not null", writer) ;
			xml = writer.toString() ;
			assertNotNull("XML from Writer is not null", xml) ;
			assertFalse("XML from Writer is not an empty String", "".equals(xml)) ;
			
			preferences.add(xml) ;
		}
		
		assertNotNull("Preferences list is not null", preferences) ;
		assertFalse("Preferences list is not empty", preferences.isEmpty()) ;
		assertTrue("Preferences list and Producer list are equals", preferences.size() == list.size()) ;
	}

	private void setupTestData() {
		try {
			byte[] bytes = new byte[] { -84, -19, 0, 5, 115, 114, 0, 89, 99, 111, 
					109, 46, 118, 105, 103, 110, 101, 116, 116, 101, 
					46, 112, 111, 114, 116, 97, 108, 46, 112, 111, 
					114, 116, 108, 101, 116, 46, 109, 97, 110, 97, 
					103, 101, 109, 101, 110, 116, 46, 105, 110, 116, 
					101, 114, 110, 97, 108, 46, 105, 109, 112, 108, 
					101, 109, 101, 110, 116, 97, 116, 105, 111, 110, 
					46, 112, 114, 111, 118, 105, 100, 101, 114, 46, 
					119, 115, 114, 112, 46, 80, 114, 111, 100, 117, 
					99, 101, 114, 68, 97, 116, 97, 0, 0, 0, 
					0, 0, 0, 0, 1, 2, 0, 6, 76, 0, 
					9, 109, 97, 114, 107, 117, 112, 85, 82, 76, 
					116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 
					97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 
					59, 76, 0, 18, 111, 114, 105, 103, 105, 110, 
					97, 108, 83, 101, 114, 118, 105, 99, 101, 85, 
					82, 76, 113, 0, 126, 0, 1, 76, 0, 20, 
					112, 111, 114, 116, 108, 101, 116, 77, 97, 110, 
					97, 103, 101, 109, 101, 110, 116, 85, 82, 76, 
					113, 0, 126, 0, 1, 91, 0, 17, 114, 101, 
					103, 105, 115, 116, 114, 97, 116, 105, 111, 110, 
					83, 116, 97, 116, 101, 116, 0, 2, 91, 66, 
					76, 0, 15, 114, 101, 103, 105, 115, 116, 114, 
					97, 116, 105, 111, 110, 85, 82, 76, 113, 0, 
					126, 0, 1, 76, 0, 21, 115, 101, 114, 118, 
					105, 99, 101, 68, 101, 115, 99, 114, 105, 112, 
					116, 105, 111, 110, 85, 82, 76, 113, 0, 126, 
					0, 1, 120, 112, 116, 0, 83, 104, 116, 116, 
					112, 58, 47, 47, 103, 51, 117, 48, 52, 56, 
					56, 46, 104, 111, 117, 115, 116, 111, 110, 46, 
					104, 112, 46, 99, 111, 109, 58, 50, 55, 48, 
					48, 53, 47, 115, 112, 112, 45, 115, 101, 114, 
					118, 105, 99, 101, 115, 45, 119, 101, 98, 47, 
					87, 83, 82, 80, 52, 74, 80, 114, 111, 100, 
					117, 99, 101, 114, 47, 87, 83, 82, 80, 66, 
					97, 115, 101, 83, 101, 114, 118, 105, 99, 101, 
					116, 0, 71, 104, 116, 116, 112, 58, 47, 47, 
					103, 51, 117, 48, 52, 56, 56, 46, 104, 111, 
					117, 115, 116, 111, 110, 46, 104, 112, 46, 99, 
					111, 109, 58, 50, 55, 48, 48, 53, 47, 115, 
					112, 112, 45, 115, 101, 114, 118, 105, 99, 101, 
					115, 45, 119, 101, 98, 47, 119, 115, 100, 108, 
					47, 119, 115, 114, 112, 95, 119, 115, 100, 108, 
					46, 106, 115, 112, 116, 0, 96, 104, 116, 116, 
					112, 58, 47, 47, 103, 51, 117, 48, 52, 56, 
					56, 46, 104, 111, 117, 115, 116, 111, 110, 46, 
					104, 112, 46, 99, 111, 109, 58, 50, 55, 48, 
					48, 53, 47, 115, 112, 112, 45, 115, 101, 114, 
					118, 105, 99, 101, 115, 45, 119, 101, 98, 47, 
					87, 83, 82, 80, 52, 74, 80, 114, 111, 100, 
					117, 99, 101, 114, 47, 87, 83, 82, 80, 80, 
					111, 114, 116, 108, 101, 116, 77, 97, 110, 97, 
					103, 101, 109, 101, 110, 116, 83, 101, 114, 118, 
					105, 99, 101, 112, 116, 0, 91, 104, 116, 116, 
					112, 58, 47, 47, 103, 51, 117, 48, 52, 56, 
					56, 46, 104, 111, 117, 115, 116, 111, 110, 46, 
					104, 112, 46, 99, 111, 109, 58, 50, 55, 48, 
					48, 53, 47, 115, 112, 112, 45, 115, 101, 114, 
					118, 105, 99, 101, 115, 45, 119, 101, 98, 47, 
					87, 83, 82, 80, 52, 74, 80, 114, 111, 100, 
					117, 99, 101, 114, 47, 87, 83, 82, 80, 82, 
					101, 103, 105, 115, 116, 114, 97, 116, 105, 111, 
					110, 83, 101, 114, 118, 105, 99, 101, 116, 0, 
					97, 104, 116, 116, 112, 58, 47, 47, 103, 51, 
					117, 48, 52, 56, 56, 46, 104, 111, 117, 115, 
					116, 111, 110, 46, 104, 112, 46, 99, 111, 109, 
					58, 50, 55, 48, 48, 53, 47, 115, 112, 112, 
					45, 115, 101, 114, 118, 105, 99, 101, 115, 45, 
					119, 101, 98, 47, 87, 83, 82, 80, 52, 74, 
					80, 114, 111, 100, 117, 99, 101, 114, 47, 87, 
					83, 82, 80, 83, 101, 114, 118, 105, 99, 101, 
					68, 101, 115, 99, 114, 105, 112, 116, 105, 111, 
					110, 83, 101, 114, 118, 105, 99, 101 } ;
			Blob blob = new jdbcBlob(bytes) ;
			Object[] args = new Object[1] ;
			args[0] = blob ;
			int[] argSqlTypes = new int[1] ;
			argSqlTypes[0] = Types.BLOB;
			
			// create portlet application
			DB.update("insert into WSRPPORTLETAPPLICATIONS (WSRP_PORTLET_APPLICATION_UID, TITLE, REGISTRATION_HANDLE, PRODUCER_TYPE, DESCRIPTION, FRIENDLYID, COOKIE_INIT, CREATED_DATE, PRODUCER_DATA) values ('abc', 'TEST_TITLE', '123', 2, null, 'TEST_IMPORTID', 'perGroup', '2006-12-18 08:19:28', ?)", args, argSqlTypes);
			// create portlet type
			DB.update("insert into WSRPPORTLETTYPES (WSRP_PORTLET_TYPE_UID, WSRP_PORTLET_APPLICATION_UID, WSRP_PORTLET_HANDLE, WSRP_PARENT_HANDLE, NAME, SECURE, VISIBLE, CREATED_DATE) values ('def', 'abc', '456', 'Vignette Safe Value', '456', '0', '0', '2006-12-18 08:19:28')");
			// create portlet instance
			DB.update("insert into WSRPPORTLETS (WSRP_PORTLET_UID, WSRP_PORTLET_TYPE_UID, WSRP_PORTLET_HANDLE, WSRP_PORTLET_STATE, VERSION) values ('ghi', 'def', '789', null, 1)");

			// create portlet with 2 preferences
			DB.update("insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values ('789', 'TEST.1', 'a.b', 'TEST')");
			// create single-value preference
			DB.update("insert into WSRP_PREFERENCE (ID, NAME, READ_ONLY, PORTLET) values ('1', 'single_value_pref', 'f', '789')");
			DB.update("insert into WSRP_PREFERENCE_VALUE (PREFERENCE, PREFERENCE_IDX, VALUE) values ('1', '1', 'single value')");
			// create multi-value preference
			DB.update("insert into WSRP_PREFERENCE (ID, NAME, READ_ONLY, PORTLET) values ('2', 'multi_value_pref', 'f', '789')");
			DB.update("insert into WSRP_PREFERENCE_VALUE (PREFERENCE, PREFERENCE_IDX, VALUE) values ('2', '1', 'value 1')");
			DB.update("insert into WSRP_PREFERENCE_VALUE (PREFERENCE, PREFERENCE_IDX, VALUE) values ('2', '2', 'value 2')");

			// create portlet without preferences
			DB.update("insert into WSRP_PORTLET (ID, PORTLET_ID, DEFINITION_ID, APPLICATION) values ('2', 'TEST.2', 'a.b', 'TEST')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

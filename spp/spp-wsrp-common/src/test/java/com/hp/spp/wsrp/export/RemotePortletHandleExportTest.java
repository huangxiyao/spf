package com.hp.spp.wsrp.export;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.hsqldb.jdbc.jdbcBlob;
import org.w3c.dom.Document;

import com.hp.spp.db.DB;
import com.hp.spp.wsrp.export.helper.VignetteTestHelper;

public class RemotePortletHandleExportTest extends Test {

	protected void setUp() throws Exception {
		VignetteTestHelper.setupTestDataSource();
		VignetteTestHelper.setupTestDatabase();
		setupTestData();
	}

	public void testGetRemotePortletIUDList() throws Exception {
		RemotePortletHandleExport export = new RemotePortletHandleExport();
		List list = null ;

		String xml = "<wsrp-portlet-uid-list><uid>6c238b4775b874761fcea8107a5d88f0</uid><uid>ad74bab5f2b4a4761fcea8107a5d88f0</uid><uid>15638756a0989a8e4f90458d7a5d88f0</uid><uid>57b4a93f4d9209081fcea8107a5d88f0</uid><uid>8136917276c78e824f90458d7a5d88f0</uid><uid>90f45fd0d6cd5d98e3ba28107a5d88f0</uid><uid>6514948e384eaff71fcea8107a5d88f0</uid><uid>97ede219895ec8081fcea8107a5d88f0</uid><uid>f460ea7d50715601d22024107a5d88f0</uid><uid>77240702f464a9c84f90458d7a5d88f0</uid><uid>125fdd8eed30d1081fcea8107a5d88f0</uid><uid>810dfb715ee56601d22024107a5d88f0</uid><uid>fce4841363a030b74f90458d7a5d88f0</uid><uid>578b0e3711ab2b01d22024107a5d88f0</uid><uid>f5c5f4320baee301d22024107a5d88f0</uid><uid>8f882ea16dc25301d22024107a5d88f0</uid><uid>cd41919a3ce0ad7449fe0e107a5d88f0</uid><uid>b863aa24ad38e201d22024107a5d88f0</uid><uid>af4678afc11acf7f4f90458d7a5d88f0</uid><uid>de9e27073f61b01d4f90458d7a5d88f0</uid><uid>b559eed5e7ac04761fcea8107a5d88f0</uid><uid>dd223a3e35349a081fcea8107a5d88f0</uid><uid>d8a0874d6c54ccf71fcea8107a5d88f0</uid><uid>ccff248c164a8df71fcea8107a5d88f0</uid><uid>a47b714ff14d57394f90458d7a5d88f0</uid><uid>4a3347641237def71fcea8107a5d88f0</uid></wsrp-portlet-uid-list>";
		list = export.getRemotePortletIUDList(readInputStr(xml));
		assertNotNull("Portlet list is not null", list);
		assertFalse("Portlet list is not empty", list.isEmpty());
		assertEquals("Producer import id", "6c238b4775b874761fcea8107a5d88f0", list.get(0));

		try {
			list = export.getRemotePortletIUDList(null);
			fail("NullPointerException expected when portlet list not found");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}

		try {
			list = export.getRemotePortletIUDList(readInputStr("<wsrp-portlet-uid-list></wsrp-portlet-uid-list>"));
			fail("NullPointerException expected when portlet list not found");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}

		try {
			list = export.getRemotePortletIUDList(readInputStr("<wsrp-portlet-uid-list><uid></uid></wsrp-portlet-uid-list>"));
			fail("IllegalArgumentException expected when portlet list not found");
		} catch (IllegalArgumentException e) {
			// this is expected
			assertTrue(true);
		}
	}

	public void testGetProducerInfoList() throws Exception {
		setUp() ;

		RemotePortletHandleExport export = new RemotePortletHandleExport();
		List list = null ;

		String xml = "<wsrp-portlet-uid-list><uid>6c238b4775b874761fcea8107a5d88f0</uid><uid>ad74bab5f2b4a4761fcea8107a5d88f0</uid><uid>15638756a0989a8e4f90458d7a5d88f0</uid><uid>57b4a93f4d9209081fcea8107a5d88f0</uid><uid>8136917276c78e824f90458d7a5d88f0</uid><uid>90f45fd0d6cd5d98e3ba28107a5d88f0</uid><uid>6514948e384eaff71fcea8107a5d88f0</uid><uid>97ede219895ec8081fcea8107a5d88f0</uid><uid>f460ea7d50715601d22024107a5d88f0</uid><uid>77240702f464a9c84f90458d7a5d88f0</uid><uid>125fdd8eed30d1081fcea8107a5d88f0</uid>   <uid>1d25b783f48d4a492660bb10f8039e01</uid>   <uid>810dfb715ee56601d22024107a5d88f0</uid><uid>fce4841363a030b74f90458d7a5d88f0</uid><uid>578b0e3711ab2b01d22024107a5d88f0</uid><uid>f5c5f4320baee301d22024107a5d88f0</uid><uid>8f882ea16dc25301d22024107a5d88f0</uid><uid>cd41919a3ce0ad7449fe0e107a5d88f0</uid><uid>b863aa24ad38e201d22024107a5d88f0</uid><uid>af4678afc11acf7f4f90458d7a5d88f0</uid><uid>de9e27073f61b01d4f90458d7a5d88f0</uid><uid>b559eed5e7ac04761fcea8107a5d88f0</uid><uid>dd223a3e35349a081fcea8107a5d88f0</uid><uid>d8a0874d6c54ccf71fcea8107a5d88f0</uid><uid>ccff248c164a8df71fcea8107a5d88f0</uid><uid>a47b714ff14d57394f90458d7a5d88f0</uid><uid>4a3347641237def71fcea8107a5d88f0</uid></wsrp-portlet-uid-list>";
		list = export.getRemotePortletIUDList(readInputStr(xml));
		list = export.getProducerInfoList(list);
		assertNotNull("ProducerInfo list is not null", list);
		assertFalse("ProducerInfo list is not empty", list.isEmpty());
		
		com.hp.spp.wsrp.export.info.vignette.ProducerInfo producerInfo = (com.hp.spp.wsrp.export.info.vignette.ProducerInfo) list.get(0);
		assertNotNull("ProducerInfo is not null", producerInfo);
		
		assertNotNull("ImportId is not null", producerInfo.getImportId());
		assertFalse("ImportId is not empty", "".equals(producerInfo.getImportId()));
		assertNotNull("Url is not null", producerInfo.getUrl());
		assertFalse("Url is not empty", "".equals(producerInfo.getUrl()));
		assertNotNull("PortletUID list is not null", producerInfo.getPortletUIDs());
		assertTrue("Portlet list is not empty", producerInfo.hasPortlets());
		assertNotNull("Portlet list is not null", producerInfo.getPortlets());
	}
	
	public void testExport() throws Exception {
		setUp() ;

		RemotePortletHandleExport export = new RemotePortletHandleExport();
		Document doc = null ;

		String xml = "<wsrp-portlet-uid-list><uid>6c238b4775b874761fcea8107a5d88f0</uid><uid>ad74bab5f2b4a4761fcea8107a5d88f0</uid><uid>15638756a0989a8e4f90458d7a5d88f0</uid><uid>57b4a93f4d9209081fcea8107a5d88f0</uid><uid>8136917276c78e824f90458d7a5d88f0</uid><uid>90f45fd0d6cd5d98e3ba28107a5d88f0</uid><uid>6514948e384eaff71fcea8107a5d88f0</uid><uid>97ede219895ec8081fcea8107a5d88f0</uid><uid>f460ea7d50715601d22024107a5d88f0</uid><uid>77240702f464a9c84f90458d7a5d88f0</uid><uid>125fdd8eed30d1081fcea8107a5d88f0</uid>   <uid>1d25b783f48d4a492660bb10f8039e01</uid>   <uid>810dfb715ee56601d22024107a5d88f0</uid><uid>fce4841363a030b74f90458d7a5d88f0</uid><uid>578b0e3711ab2b01d22024107a5d88f0</uid><uid>f5c5f4320baee301d22024107a5d88f0</uid><uid>8f882ea16dc25301d22024107a5d88f0</uid><uid>cd41919a3ce0ad7449fe0e107a5d88f0</uid><uid>b863aa24ad38e201d22024107a5d88f0</uid><uid>af4678afc11acf7f4f90458d7a5d88f0</uid><uid>de9e27073f61b01d4f90458d7a5d88f0</uid><uid>b559eed5e7ac04761fcea8107a5d88f0</uid><uid>dd223a3e35349a081fcea8107a5d88f0</uid><uid>d8a0874d6c54ccf71fcea8107a5d88f0</uid><uid>ccff248c164a8df71fcea8107a5d88f0</uid><uid>a47b714ff14d57394f90458d7a5d88f0</uid><uid>4a3347641237def71fcea8107a5d88f0</uid></wsrp-portlet-uid-list>";
		doc = export.export(readInputStr(xml)) ;
		assertNotNull("Document is not null", doc);
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
			DB.update("insert into WSRPPORTLETAPPLICATIONS (WSRP_PORTLET_APPLICATION_UID, TITLE, REGISTRATION_HANDLE, PRODUCER_TYPE, DESCRIPTION, FRIENDLYID, COOKIE_INIT, CREATED_DATE, PRODUCER_DATA) values ('1ce670fb38204a492660bb10f8039e01', 'GPP dev ecomm server', '16.138.40.158_1166429806324_0', 2, null, 'GPP dev ecomm server', 'perGroup', '2006-12-18 08:19:28', ?)", args, argSqlTypes);
			// create portlet type
			DB.update("insert into WSRPPORTLETTYPES (WSRP_PORTLET_TYPE_UID, WSRP_PORTLET_APPLICATION_UID, WSRP_PORTLET_HANDLE, WSRP_PARENT_HANDLE, NAME, SECURE, VISIBLE, CREATED_DATE) values ('6e18d76a0b7d4a492660bb10f8039e01', '1ce670fb38204a492660bb10f8039e01', '4.100', 'Vignette Safe Value', '4.100', '0', '0', '2006-12-18 08:19:28')");
			// create portlet instance
			DB.update("insert into WSRPPORTLETS (WSRP_PORTLET_UID, WSRP_PORTLET_TYPE_UID, WSRP_PORTLET_HANDLE, WSRP_PORTLET_STATE, VERSION) values ('1d25b783f48d4a492660bb10f8039e01', '6e18d76a0b7d4a492660bb10f8039e01', '4.16.138.40.158_1166429858277_1', null, 1)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

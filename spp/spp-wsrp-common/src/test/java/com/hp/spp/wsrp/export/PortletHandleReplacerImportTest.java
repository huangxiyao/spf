package com.hp.spp.wsrp.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.spp.wsrp.export.util.DOMUtils;

public class PortletHandleReplacerImportTest extends Test {

	public void testGetReversedMap() throws Exception {
		PortletHandleReplacerImport imPort = new PortletHandleReplacerImport();
		Map source = null;
		Map result = null;

		source = new HashMap();
		source.put("abc", "123");
		source.put("def", "456");
		source.put("ghi", "789");

		result = new HashMap();
		result.put("123", "abc");
		result.put("456", "def");
		result.put("789", "ghi");

		assertFalse("Source map and result map are different", source.equals(result));
		source = imPort.getReversedMap(source);
		assertNotNull("Source map is not null", source);
		assertFalse("Source map is not empty", source.isEmpty());
		assertTrue("Source map and result map are now equal", source.equals(result));

		source = new HashMap();
		assertTrue("Source map is empty", source.isEmpty());
		assertFalse("Source map and result map are different", source.equals(result));
		result = imPort.getReversedMap(source);
		assertNotNull("Result map is not null", result);
		assertTrue("Result map is not empty", result.isEmpty());
		assertTrue("Source map and result map are now equal", source.equals(result));

		source = null;
		assertNull("Source map is null", source);
		assertFalse("Source map and result map are different", equals(source, result));
		result = imPort.getReversedMap(source);
		assertNull("Result map is null", result);
		assertTrue("Source map and result map are now equal", equals(source, result));
	}

	public void testGetMergedMap() throws Exception {
		PortletHandleReplacerImport imPort = new PortletHandleReplacerImport();
		Map map1 = null;
		Map map2 = null;
		Map result = null;
		Map merged = null;

		map1 = new HashMap();
		map1.put("abc", "123");
		map1.put("def", "456");
		map1.put("ghi", "789");

		map2 = new HashMap();
		map2.put("abc", "ZYX");
		map2.put("def", "WVU");
		map2.put("ghi", "TSR");

		result = new HashMap();
		result.put("123", "ZYX");
		result.put("456", "WVU");
		result.put("789", "TSR");

		merged = imPort.getMergedMap(map1, map2);
		assertNotNull("", merged);
		assertFalse("", merged.isEmpty());
		assertTrue("", merged.equals(result));

		map1.put("jkl", "012");
		map1.put("pqr", "345");

		map2.put("mno", "QPO");
		map2.put("pqr", "NML");

		result.put("345", "NML");

		merged = imPort.getMergedMap(map1, map2);
		assertNotNull("", merged);
		assertFalse("", merged.isEmpty());
		assertTrue("", merged.equals(result));

		map1 = new HashMap();
		assertTrue("", map1.isEmpty());
		merged = imPort.getMergedMap(map1, map2);
		assertNotNull("", merged);
		assertTrue("", merged.isEmpty());
		assertTrue("", merged.equals(map1));

		map2 = null;
		assertNull("", map2);
		merged = imPort.getMergedMap(map1, map2);
		assertNull("", merged);
	}

	public void testGetProducerList() throws Exception {
		PortletHandleReplacerImport imPort = new PortletHandleReplacerImport();
		List list = null;

		String xml = "<wsrp-portlet-list><producer><import-id>SOMEIMPORT</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet></producer></wsrp-portlet-list>";
		list = imPort.getProducerList(readInputStr(xml));
		assertNotNull("Producer list is not null", list);
		assertFalse("Producer list is not empty", list.isEmpty());

		try {
			list = imPort.getProducerList(null);
			fail("NullPointerException expected when Producer list is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}

		try {
			list = imPort.getProducerList(readInputStr("<wsrp-portlet-list></wsrp-portlet-list>"));
			fail("IllegalArgumentException expected when Producer list not found");
		} catch (IllegalArgumentException e) {
			// this is expected
			assertTrue(true);
		}
	}

	public void testGetPortletList() throws Exception {
		PortletHandleReplacerImport imPort = new PortletHandleReplacerImport();
		List list = null;

		String xml = "<producer><import-id>SOMEIMPORT</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer>";
		list = imPort.getPortletList(DOMUtils.selectSingleNode(readInputStr(xml), com.hp.spp.wsrp.export.info.vignette.ProducerInfo.PRODUCER));
		assertNotNull("Portlet list is not null", list);
		assertFalse("Portlet list is not empty", list.isEmpty());

		xml = "<producer><import-id>SOMEIMPORT</import-id><url>http://abc</url><portlet></portlet></producer>";
		list = imPort.getPortletList(DOMUtils.selectSingleNode(readInputStr(xml), com.hp.spp.wsrp.export.info.vignette.ProducerInfo.PRODUCER));
		assertNotNull("Portlet list is not null", list);
		assertFalse("Portlet list is not empty", list.isEmpty());

		xml = "<producer></producer>";
		list = imPort.getPortletList(DOMUtils.selectSingleNode(readInputStr(xml), com.hp.spp.wsrp.export.info.vignette.ProducerInfo.PRODUCER));
		assertNull("Portlet list is null", list);
	}

	public void testGetProducerInfoList() throws Exception {
		PortletHandleReplacerImport imPort = new PortletHandleReplacerImport();
		List list = null;

		String xml = "<wsrp-portlet-list><producer><import-id>SOMEIMPORT</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet></producer></wsrp-portlet-list>";
		list = imPort.getProducerList(readInputStr(xml));
		list = imPort.getProducerInfoList(list);
		assertNotNull("ProducerInfo list is not null", list);
		assertFalse("ProducerInfo list is not empty", list.isEmpty());

		xml = "<wsrp-portlet-list><producer><import-id></import-id><url></url></producer></wsrp-portlet-list>";
		list = imPort.getProducerList(readInputStr(xml));
		list = imPort.getProducerInfoList(list);
		assertNotNull("ProducerInfo list is not null", list);
		assertTrue("ProducerInfo list is empty", list.isEmpty());

		try {
			list = imPort.getProducerInfoList(null);
			fail("NullPointerException expected when ProducerInfo list is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}
	}

	public void testGetPreferenceList() throws Exception {
	}

	public void testGetWsrpPreferenceList() throws Exception {
	}

	public void testGetPortletHandleMapByList() throws Exception {
	}

	public void testGetPortletHandleMapByDocument() throws Exception {
	}

	public void testReplaceHandle() throws Exception {
	}

	public void testReplace() throws Exception {
	}

	private boolean equals(Object o1, Object o2) {
		if (o1 == null && o2 == null)
			return true;
		else if (o1 == null)
			return false;
		else if (o2 == null)
			return false;
		else
			return o1.equals(o2);
	}

}

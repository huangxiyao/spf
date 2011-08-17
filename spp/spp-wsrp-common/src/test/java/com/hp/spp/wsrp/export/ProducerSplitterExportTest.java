package com.hp.spp.wsrp.export;

import java.util.List;

public class ProducerSplitterExportTest extends Test {

	public void testGetProducerList() throws Exception {
		ProducerSplitterExport export = new ProducerSplitterExport();
		List list = null;

		String xml = "<wsrp-portlet-list><producer><import-id>SOMEIMPORT</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet></producer></wsrp-portlet-list>";
		list = export.getProducerList(readInputStr(xml));
		assertNotNull("Producer list is not null", list);
		assertFalse("Producer list is not empty", list.isEmpty());

		try {
			list = export.getProducerList(null);
			fail("NullPointerException expected when Producer list is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}

		try {
			list = export.getProducerList(readInputStr("<wsrp-portlet-list></wsrp-portlet-list>"));
			fail("IllegalArgumentException expected when Producer list not found");
		} catch (IllegalArgumentException e) {
			// this is expected
			assertTrue(true);
		}
	}

	public void testGetProducerInfoList() throws Exception {
		ProducerSplitterExport export = new ProducerSplitterExport();
		List list = null;

		String xml = "<wsrp-portlet-list><producer><import-id>SOMEIMPORT</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet></producer></wsrp-portlet-list>";
		list = export.getProducerList(readInputStr(xml));
		list = export.getProducerInfoList(list);
		assertNotNull("ProducerInfo list is not null", list);
		assertFalse("ProducerInfo list is not empty", list.isEmpty());

		xml = "<wsrp-portlet-list><producer><import-id></import-id><url></url></producer></wsrp-portlet-list>";
		list = export.getProducerList(readInputStr(xml));
		list = export.getProducerInfoList(list);
		assertNotNull("ProducerInfo list is not null", list);
		assertTrue("ProducerInfo list is empty", list.isEmpty());

		try {
			list = export.getProducerInfoList(null);
			fail("NullPointerException expected when ProducerInfo list is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}
	}

	public void testGetSplittedProducerInfoList() throws Exception {
		ProducerSplitterExport export = new ProducerSplitterExport();
		List list = null;

		String xml = "<wsrp-portlet-list><producer><import-id>Producer1</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer><producer><import-id>Producer2</import-id><url>http://def</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer><producer><import-id>Producer1</import-id><url>http://abc</url><portlet><uid>ghi</uid><handle>789</handle></portlet><portlet><uid>jkl</uid><handle>012</handle></portlet></producer><producer><import-id>Producer3</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer><producer><import-id>Producer2</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer></wsrp-portlet-list>";
		list = export.getProducerList(readInputStr(xml));
		list = export.getProducerInfoList(list);
		list = export.getSplittedProducerInfoList(list);
		assertNotNull("SplittedProducerInfo list is not null", list);
		assertFalse("SplittedProducerInfo list is not empty", list.isEmpty());

		try {
			list = export.getSplittedProducerInfoList(null);
			fail("NullPointerException expected when SplittedProducerInfo list is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}
	}

	public void testGetDocumentList() throws Exception {
		ProducerSplitterExport export = new ProducerSplitterExport();
		List list = null;

		String xml = "<wsrp-portlet-list><producer><import-id>Producer1</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer><producer><import-id>Producer2</import-id><url>http://def</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer><producer><import-id>Producer1</import-id><url>http://abc</url><portlet><uid>ghi</uid><handle>789</handle></portlet><portlet><uid>jkl</uid><handle>012</handle></portlet></producer><producer><import-id>Producer3</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer><producer><import-id>Producer2</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer></wsrp-portlet-list>";
		list = export.getProducerList(readInputStr(xml));
		list = export.getProducerInfoList(list);
		list = export.getSplittedProducerInfoList(list);
		list = export.getDocumentList(list);
		assertNotNull("Document list is not null", list);
		assertFalse("Document list is not empty", list.isEmpty());

		try {
			list = export.getDocumentList(null);
			fail("NullPointerException expected when Document list is null");
		} catch (NullPointerException e) {
			// this is expected
			assertTrue(true);
		}
	}

	public void testSplit() throws Exception {
		ProducerSplitterExport export = new ProducerSplitterExport();
		List list = null;

		String xml = "<wsrp-portlet-list><producer><import-id>Producer1</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer><producer><import-id>Producer2</import-id><url>http://def</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer><producer><import-id>Producer1</import-id><url>http://abc</url><portlet><uid>ghi</uid><handle>789</handle></portlet><portlet><uid>jkl</uid><handle>012</handle></portlet></producer><producer><import-id>Producer3</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer><producer><import-id>Producer2</import-id><url>http://abc</url><portlet><uid>abc</uid><handle>123</handle></portlet><portlet><uid>def</uid><handle>456</handle></portlet></producer></wsrp-portlet-list>";
		list = export.split(readInputStr(xml));
		assertNotNull("Splited list is not null", list);
		assertFalse("Splited list is not empty", list.isEmpty());

		list = export.split(null);
		assertNotNull("Splited list is not null", list);
	}

}

package com.hp.spp.wsrp.export.helper;

import com.hp.spp.db.DB;

public class ProducerTestHelper extends TestHelper {

	private ProducerTestHelper() {}

	public static void setupTestDatabase() throws Exception {
		DB.update("drop table WSRP_PREFERENCE_VALUE if exists", null);
		DB.update("drop table WSRP_PREFERENCE if exists", null);
		DB.update("drop table WSRP_PORTLET if exists", null);
		DB.update("create table WSRP_PORTLET (\n" +
				"        ID varchar(50) not null,\n" +
				"        PORTLET_ID varchar(50) not null,\n" +
				"        DEFINITION_ID varchar(50) not null,\n" +
				"        APPLICATION varchar(50) not null,\n" +
				"        PARENT_HANDLE varchar(50),\n" +
				"        primary key (ID)\n" +
				"    )", null);
		DB.update("create table WSRP_PREFERENCE (\n" +
				"        ID varchar(100) not null,\n" +
				"        NAME varchar(50) not null,\n" +
				"        READ_ONLY varchar(5) not null,\n" +
				"        PORTLET varchar(50) not null,\n" +
				"        primary key (ID)\n" +
				"    )", null);
		DB.update("create table WSRP_PREFERENCE_VALUE (\n" +
				"        PREFERENCE varchar(100) not null,\n" +
				"        PREFERENCE_IDX integer not null,\n" +
				"        VALUE varchar(50),\n" +
				"        primary key (PREFERENCE, PREFERENCE_IDX)\n" +
				"    )", null);
	}

}

package com.hp.spp.wsrp.export.helper;

import com.hp.spp.db.DB;

public class VignetteTestHelper extends TestHelper {

	private VignetteTestHelper() {}

	public static void setupTestDatabase() throws Exception {
		DB.update("drop table WSRPPORTLETAPPLICATIONS if exists", null);
		DB.update("drop table WSRPPORTLETTYPES if exists", null);
		DB.update("drop table WSRPPORTLETS if exists", null);
		DB.update("create table WSRPPORTLETAPPLICATIONS (\n" +
				"        WSRP_PORTLET_APPLICATION_UID varchar(192) not null,\n" +
				"        TITLE varchar(765) not null,\n" +
				"        REGISTRATION_HANDLE varchar(765) ,\n" +
				"        PRODUCER_TYPE integer not null,\n" +
				"        DESCRIPTION varchar(765) ,\n" +
				"        FRIENDLYID varchar(765) ,\n" +
				"        COOKIE_INIT varchar(192) ,\n" +
				"        CREATED_DATE date not null,\n" +
				"        PRODUCER_DATA binary not null,\n" +
				"        primary key (WSRP_PORTLET_APPLICATION_UID)\n" +
				"    )", null);
		DB.update("create table WSRPPORTLETTYPES (\n" +
				"        WSRP_PORTLET_TYPE_UID varchar(192) not null,\n" +
				"        WSRP_PORTLET_APPLICATION_UID varchar(192) not null,\n" +
				"        WSRP_PORTLET_HANDLE varchar(765) not null,\n" +
				"        WSRP_PARENT_HANDLE varchar(765) ,\n" +
				"        NAME varchar(765) not null,\n" +
				"        SECURE char(3) not null,\n" +
				"        VISIBLE char(3) not null,\n" +
				"        CREATED_DATE date not null,\n" +
				"        primary key (WSRP_PORTLET_TYPE_UID)\n" +
				"    )", null);
		DB.update("create table WSRPPORTLETS (\n" +
				"        WSRP_PORTLET_UID varchar(192) NOT NULL,\n" +
				"        WSRP_PORTLET_TYPE_UID varchar(192) NOT NULL,\n" +
				"        WSRP_PORTLET_HANDLE varchar(765) NOT NULL,\n" +
				"        WSRP_PORTLET_STATE binary ,\n" +
				"        VERSION integer DEFAULT 0 NOT NULL,\n" +
				"        primary key (WSRP_PORTLET_UID)\n" +
				"    )", null);
	}

}

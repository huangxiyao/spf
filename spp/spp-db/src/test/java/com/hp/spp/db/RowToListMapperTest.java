package com.hp.spp.db;

import java.util.List;

public class RowToListMapperTest extends TestFixture {

	public void testRowToListMapper() throws Exception {
		List row = DB.queryForObject("select id, name from test where name = 't1'", new RowToListMapper());
		assertEquals("Number of elements in the row", 2, row.size());
		assertEquals("2nd element is the name", "t1", row.get(1));
	}
}

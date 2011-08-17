package com.hp.spp.db;

import java.util.Map;

public class RowToMapMapperTest extends TestFixture {

	public void testRowToMapMapper() throws Exception {
		Map<String, Object> row = DB.queryForObject("select id, name from test where name = 't1'", new RowToMapMapper());
		assertEquals("Number of elements in the row", 2, row.size());
		assertNotNull("id element is not null", row.get("id"));
		assertNotNull("name element is not null", row.get("name"));
		assertEquals("name element value", "t1", row.get("name"));

	}
}

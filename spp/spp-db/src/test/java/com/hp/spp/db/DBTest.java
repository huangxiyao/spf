package com.hp.spp.db;


import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class DBTest extends TestFixture {

    public void testSimpleQuery() throws Exception {
        List result = DB.query("select * from test", TOM);
        assertNotNull("List is not null", result);
        assertTrue("List is not empty", !result.isEmpty());
        assertEquals("Selected complete list", 5, result.size());
    }

    public void testQueryWithArgumentsWithoutTypes() throws Exception {
        List<TestObject> result = DB.query("select * from test where name = ?", TOM, new Object[] {"t2"});
        assertTrue("Only one element selected", 1 == result.size());
        assertEquals("Selected object name", "t2", result.get(0).getName());
    }

    public void testQueryWithArgumentsAndTypes() throws Exception {
        List<TestObject> result = DB.query("select * from test where name in (?, ?) order by name", TOM,
                        new Object[] {"t2", "t3"}, new int[] {Types.VARCHAR, Types.VARCHAR});
        assertEquals("Number of selected elements", 2, result.size());
        assertEquals("First selected object", "t2", result.get(0).getName());
        assertEquals("Second selected object", "t3", result.get(1).getName());
    }

    public void testQueryWithTypeInteger() throws Exception {
        List<TestObject> result = DB.query("select * from test where id = ? ", TOM,
                        new Object[] {"17"}, new int[] {Types.INTEGER});
        assertEquals("Number of selected elements", 1, result.size());
        assertEquals("Selected object id", 17, result.get(0).getId());
        assertEquals("Selected object name", "t5", result.get(0).getName());
    }

    public void testQuerySubset() throws Exception {
        List<TestObject> result = DB.query("select * from test order by name", TOM, 1, 2);
        assertEquals("Number of selected elements", 2, result.size());
        assertEquals("First selected object", "t2", result.get(0).getName());
        assertEquals("Second selected object", "t3", result.get(1).getName());
    }

    public void testDatabaseExceptionThrownOnSQLException() throws Exception {
        try {
            DB.update("update test set name = ? where name = ?", new Object[] {null, "t3"});
            fail("DatabaseException expected");
        }
        catch (DatabaseException e) {
            assertEquals("Wrapped exception", SQLException.class, e.getCause().getClass());
        }
    }

    public void testQueryForObject() throws Exception {
        TestObject result = DB.queryForObject("select * from test where name = ?", TOM, new Object[] {"t2"}, new int[] {Types.VARCHAR});
        assertEquals("name is 't2'", "t2", result.getName());
    }

}

package com.hp.spp.db;


import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Types;

public class DatabaseTransactionTest extends TestFixture {

    public void testSimpleQuery() throws Exception {
        DatabaseTransaction<List<TestObject>> tx = new DatabaseTransaction<List<TestObject>>(mDataSource) {
            protected List<TestObject> doInTransaction() throws SQLException {
                return query("select * from test", TOM, 0, 0, null, null);
            }
        };
        List<TestObject> result = tx.execute();
        assertNotNull("List is not null", result);
        assertTrue("List is not empty", !result.isEmpty());
        assertEquals("Selected complete list", 5, result.size());
    }

    public void testQueryWithArgumentsWithoutTypes() throws Exception {
        DatabaseTransaction<List<TestObject>> tx = new DatabaseTransaction<List<TestObject>>(mDataSource) {
            protected List<TestObject> doInTransaction() throws SQLException {
                return query("select * from test where name = ?", TOM, 0, 0, new Object[] {"t2"}, null);
            }
        };
        List<TestObject> result = tx.execute();
        assertTrue("Only one element selected", 1 == result.size());
//        assertTrue("Created instance of TestObject", result.get(0) instanceof TestObject);
        assertEquals("Selected object name", "t2", result.get(0).getName());
    }

    public void testQueryWithArgumentsAndTypes() throws Exception {
        DatabaseTransaction<List<TestObject>> tx = new DatabaseTransaction<List<TestObject>>(mDataSource) {
            protected List<TestObject> doInTransaction() throws SQLException {
                return query("select * from test where name in (?, ?) order by name", TOM, 0, 0,
                        new Object[] {"t2", "t3"}, new int[] {Types.VARCHAR, Types.VARCHAR});
            }
        };
        List<TestObject> result = tx.execute();
        assertEquals("Number of selected elements", 2, result.size());
        assertEquals("First selected object", "t2", result.get(0).getName());
        assertEquals("Second selected object", "t3", result.get(1).getName());
    }

    public void testQuerySubset() throws Exception {
        DatabaseTransaction<List<TestObject>> tx = new DatabaseTransaction<List<TestObject>>(mDataSource) {
            protected List<TestObject> doInTransaction() throws SQLException {
                return query("select * from test order by name", TOM, 1, 2, null, null);
            }
        };

        List<TestObject> result = tx.execute();
        assertEquals("Number of selected elements", 2, result.size());
        assertEquals("First selected object", "t2", result.get(0).getName());
        assertEquals("Second selected object", "t3", result.get(1).getName());
    }

    public void testUpdateWithArgumentsWithoutTypes() throws Exception {
        final List<TestObject> result = new ArrayList<TestObject>();
        DatabaseTransaction<Integer> tx = new DatabaseTransaction<Integer>(mDataSource) {
            protected Integer doInTransaction() throws SQLException {
                int id = query("select * from test where name = ?", TOM, 0, 0, new Object[] {"t2"}, null).get(0).getId();
                int updatedCount = update("update test set name = ? where name = ?", new Object[] {"t2-updated", "t2"}, null);
                result.addAll(query("select * from test where id = ?", TOM, 0, 0, new Object[] {new Integer(id)}, null));
                return new Integer(updatedCount);
            }
        };
        Integer updatedCount = (Integer) tx.execute();
        assertEquals("Number of updated rows", 1, updatedCount.intValue());
        assertEquals("Updated value", "t2-updated",  result.get(0).getName());
    }

    public void testDatabaseExceptionThrownOnSQLException() throws Exception {
        try {
            DatabaseTransaction tx = new DatabaseTransaction(mDataSource) {
                protected Object doInTransaction() throws SQLException {
                    // try to insert null to non-nullable column
                    update("update test set name = ? where name = ?", new Object[] {null, "t3"}, null);
                    return null;
                }
            };
            tx.execute();
            fail("DatabaseException expected");
        }
        catch (DatabaseException e) {
            assertEquals("Wrapped exception", SQLException.class, e.getCause().getClass());
        }
    }

    public void testTransaction() throws Exception {
        try {
            DatabaseTransaction tx = new DatabaseTransaction(mDataSource) {
                protected Object doInTransaction() throws SQLException {
                    update("update test set name = ? where name = ?", new Object[] {"t2-updated", "t2"}, null);
                    // try to insert null to non-nullable column
                    update("update test set name = ? where name = ?", new Object[] {null, "t3"}, null);
                    return null;
                }
            };
            tx.execute();
            fail("No DatabaseException thrown?");
        }
        catch (DatabaseException e) {
            // we expected this here
        }

        DatabaseTransaction<List<TestObject>> tx2 = new DatabaseTransaction<List<TestObject>>(mDataSource) {
            protected List<TestObject> doInTransaction() throws SQLException {
                return query("select * from test where name = ?", TOM, 0, 0, new Object[] {"t2"}, null);
            }
        };
        List<TestObject> result = tx2.execute();
        assertTrue("Row update was rolled back therefore the name column has the old value", result.size() == 1);
    }
}

package com.hp.spp.db;

import java.util.List;
import java.sql.SQLException;

public class DB {
    private DB() { }

    /**
     * Utility method wrapping {@link DatabaseTransaction#query(String, RowMapper, int, int, Object[], int[])}. See
     * the method description for the semantics of parameters and the returned value.
     */
    public static <E> List<E> query(final String sql, final RowMapper<E> mapper, final int startAt, final int maxResults, final Object[] args, final int[] argSqlTypes) {
        DatabaseTransaction<List<E>> tx = new DatabaseTransaction<List<E>>(DataSourceHolder.getDataSource()) {
            protected List<E> doInTransaction() throws SQLException {
                return query(sql, mapper, startAt, maxResults, args, argSqlTypes);
            }
        };
        try {
            return tx.execute();
        } catch (Exception e) {
            if (e instanceof DatabaseException) {
                throw (DatabaseException) e;
            }
            else {
                throw new DatabaseException("Unexpected exception while executing database query", e);
            }
        }
    }

    public static <E> List<E> query(String sql, RowMapper<E> mapper, int startAt, int maxResults, Object[] args) {
        return query(sql, mapper, startAt, maxResults, args, null);
    }

    public static <E> List<E> query(String sql, RowMapper<E> mapper, int startAt, int maxResults) {
        return query(sql, mapper, startAt, maxResults, null, null);
    }

    public static <E> List<E> query(String sql, RowMapper<E> mapper, Object[] args, int[] argSqlTypes) {
        return query(sql, mapper, 0, 0, args, argSqlTypes);
    }

    public static <E> List<E> query(String sql, RowMapper<E> mapper, Object[] args) {
        return query(sql, mapper, 0, 0, args, null);
    }

    public static <E> List<E> query(String sql, RowMapper<E> mapper) {
        return query(sql, mapper, 0, 0, null, null);
    }

    /**
     * Executes a query that returns a single row. The method wraps {@link DatabaseTransaction#query(String, RowMapper, int, int, Object[], int[])}
     * using <tt>1</tt> as number of requested results.
     * @return result of using the <tt>mapper</tt> on the selected row or <tt>null</tt> if the query selected no row.
     */
    public static <E> E queryForObject(String sql, RowMapper<E> mapper, Object[] args, int[] argSqlTypes) {
        List<E> result = query(sql, mapper, 0, 1, args, argSqlTypes);
        return (result.isEmpty() ? null : result.get(0));
    }

    public static <E> E queryForObject(String sql, RowMapper<E> mapper, Object[] args) {
        return queryForObject(sql, mapper, args, null);
    }

    public static <E> E queryForObject(String sql, RowMapper<E> mapper) {
        return queryForObject(sql, mapper, null, null);
    }

    /**
     * Utility method wrapping {@link DatabaseTransaction#update(String, Object[], int[])}. See the method description
     * for the semantics of the parameters and the return value.
     */
    public static int update(final String sql, final Object[] args, final int[] argSqlTypes) {
        DatabaseTransaction tx = new DatabaseTransaction(DataSourceHolder.getDataSource()) {
            protected Object doInTransaction() throws SQLException {
                return new Integer(update(sql, args, argSqlTypes));
            }
        };
        try {
            return ((Integer) tx.execute()).intValue();
        } catch (Exception e) {
            if (e instanceof DatabaseException) {
                throw (DatabaseException) e;
            }
            else {
                throw new DatabaseException("Unexpected exception when executing database update", e);
            }
        }
    }

    public static int update(String sql, Object[] args) {
        return update(sql, args, null);
    }

	public static int update(String sql) {
		return update(sql, null, null);
	}
}

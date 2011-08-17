package com.hp.spp.db;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

/**
 * Class representing a set of database operations that should run in the same transaction. The implementing classes
 * must overwrite {@link #doInTransaction()} where they can call {@link #query(String, RowMapper, int, int, Object[], int[])}
 * and {@link #update(String, Object[], int[])} methods to perform database operations.
 * For single query and update operation that don't have to be grouped into transaction use rather the utility
 * methods in {@link DB}.
 */
public abstract class DatabaseTransaction<T> {

    private static final Logger mLog = Logger.getLogger(DatabaseTransaction.class);
    private static final Logger mSqlLog = Logger.getLogger("com.hp.spp.db.SQL");

    private DataSource mDataSource;
    private Connection mConnection;

    protected DatabaseTransaction(DataSource mDataSource) {
        this.mDataSource = mDataSource;
    }

    protected DatabaseTransaction() {
        this(DataSourceHolder.getDataSource());
    }

    protected abstract T doInTransaction() throws Exception;


    public T execute() throws Exception {
        try {
            T result = doInTransaction();
            getTransactionConnection().commit();
            return result;
        }
        catch (SQLException e) {
            rollbackCurrentTransaction();
            throw new DatabaseException(e);
        }
        catch (Exception e) {
            rollbackCurrentTransaction();
            throw e;
        }
        finally {
            if (mConnection != null) {
                close(mConnection);
                mConnection = null;
            }
        }
    }

    private void rollbackCurrentTransaction() {
        try {
            getTransactionConnection().rollback();
        }
        catch (SQLException e2) {
            mLog.error("Error rolling back transaction", e2);
        }
    }

    protected Connection getTransactionConnection() throws SQLException {
        if (mConnection != null) {
            return mConnection;
        }
        mConnection = mDataSource.getConnection();
        if (mConnection.getAutoCommit()) {
            try {
                mConnection.setAutoCommit(false);
            } catch (SQLException e) {
                mLog.error("Unable to set Auto Commit to false", e);
                close(mConnection);
                throw e;
            }
        }
        return mConnection;
    }

    protected void close(Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        }
        catch (Exception e) {
            mLog.error("Error closing connection", e);
        }
    }

    protected void close(Statement stmt) {
        try {
            stmt.close();
        }
        catch (Exception e) {
            mLog.error("Error closing statement", e);
        }
    }

    protected void close(ResultSet rs) {
        try {
            rs.close();
        }
        catch (Exception e) {
            mLog.error("Error closing result set", e);
        }
    }

    private void checkArgs(Object[] args, int[] argSqlTypes) {
        if (argSqlTypes == null) {
            return;
        }
        if ((args == null || args.length == 0) && argSqlTypes.length > 0) {
            throw new IllegalArgumentException("No arguments specified but types provided?");
        }
        if (args != null && args.length != argSqlTypes.length) {
            throw new IllegalArgumentException("Number of specified arguments was " + args.length + " but number of types was " + argSqlTypes.length);
        }
    }

    private void debugSql(String sql, Object[] args) {
        StringBuffer sb = new StringBuffer("SQL=[");
        sb.append(sql);
        sb.append(']');
        if (args != null && args.length > 0) {
            sb.append(" ARGS=[");
            for (int i = 0, len = args.length; i < len; ++i) {
                sb.append(args[i]);
                if (i < len - 1) {
                    sb.append(", ");
                }
            }
            sb.append(']');
        }
        mSqlLog.debug(sb);
    }

    /**
     * Performs database query.
     * @param sql query sql statement; it can contain '?' position parameters same as for {@link PreparedStatement}
     * @param mapper row mapper used to map {@link ResultSet} rows into objects; if the mapper returns <tt>null</tt>
     * the object is not added to the resulting list
     * @param startAt 0-based index to which the cursor in the result set is moved
     * @param maxResults maximum number of results returned by this query; if the value is less than 1, all results will
     * be returned
     * @param args query arguments; use <tt>null</tt> if this query does not need any arguments
     * @param argSqlTypes values from {@link Types} class representing the types of the arguments provided in <tt>args</tt>;
     * if <tt>null</tt> is used the mapping will be taken from {@link SqlTypeMap#getType(Class)}
     * @return a list of objects returned by <tt>mapper</tt>. If no object was selected by the query, empty list is returned
     * @throws SQLException If a database error occurs
     */
    protected <E> List<E> query(String sql, RowMapper<E> mapper, int startAt, int maxResults, Object[] args, int[] argSqlTypes) throws SQLException {
        checkArgs(args, argSqlTypes);
        if (mSqlLog.isDebugEnabled()) {
            debugSql(sql, args);
        }

        int resultSetType = (startAt > 0 ? ResultSet.TYPE_SCROLL_INSENSITIVE : ResultSet.TYPE_FORWARD_ONLY);
        int resultCount = (maxResults < 1 ? Integer.MAX_VALUE : maxResults);

        boolean hasArguments = (args != null && args.length > 0);
        List<E> result = new ArrayList<E>();
        ResultSet rs = null;
        Statement stmt = null;
        try {
            if (hasArguments) {
                PreparedStatement pstmt = getTransactionConnection().prepareStatement(sql, resultSetType, ResultSet.CONCUR_READ_ONLY);
                stmt = pstmt;
                setArguments(pstmt, args, argSqlTypes);
                rs = pstmt.executeQuery();
            }
            else {
                stmt = getTransactionConnection().createStatement(resultSetType, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery(sql);
            }

            try {
                if (startAt > 0 && rs.absolute(startAt+1) || rs.next()) {
                    int pos = 0;
                    do {
                        E obj = mapper.mapRow(rs, pos);
                        if (obj != null) {
                            result.add(obj);
                        }
                        pos++;
                    } while (rs.next() && result.size() < resultCount);
                }
            }
            finally {
                if (rs != null) {
                    close(rs);
                }
            }
            return result;
        }
        finally {
            if (stmt != null) {
                close(stmt);
            }
        }
    }

    private void setArguments(PreparedStatement pstmt, Object[] args, int[] argSqlTypes) throws SQLException {
        for (int i = 0, len = args.length; i < len; ++i) {
            if (args[i] != null) {
                int targetSqlType = (argSqlTypes != null ? argSqlTypes[i] : SqlTypeMap.getType(args[i].getClass()));
                if(targetSqlType != Types.BLOB) {
                	pstmt.setObject(i + 1, args[i], targetSqlType);
                } else {
                	// Add to JUnit test with hsqldb
                	// Without this line, hsqldb throw an SQLException
                	// with a message "Type Conversion not supported" 
                	pstmt.setBlob(i + 1, (Blob) args[i]);
                }
            }
            else {
                pstmt.setNull(i + 1, (argSqlTypes != null ? argSqlTypes[i] : Types.VARCHAR));
            }
        }
    }

    /**
     * Peforms database update.
     * @param sql update sql statement; it can contain '?' position parameters same as for {@link PreparedStatement}
     * @param args sql statement arguments; use <tt>null</tt> if this update does not need any arguments
     * @param argSqlTypes values from {@link Types} class representing the types of the arguments provided in <tt>args</tt>;
     * if <tt>null</tt> is used the mapping will be taken from {@link SqlTypeMap#getType(Class)}
     * @return number of updated rows
     * @throws SQLException If a database error occurs
     */
    protected int update(String sql, Object[] args, int[] argSqlTypes) throws SQLException {
        checkArgs(args, argSqlTypes);
        if (mSqlLog.isDebugEnabled()) {
            debugSql(sql, args);
        }

		boolean hasArguments = (args != null && args.length > 0);
		Statement stmt = null;
		int result;
        try {
			if (hasArguments) {
				PreparedStatement pstmt = getTransactionConnection().prepareStatement(sql);
				stmt = pstmt;
				setArguments(pstmt, args, argSqlTypes);
				result = pstmt.executeUpdate();
			}
			else {
				stmt = getTransactionConnection().createStatement();
				result = stmt.executeUpdate(sql);
			}

			return result;
		}
        finally {
			if (stmt != null) {
				close(stmt);
			}
		}
    }
}

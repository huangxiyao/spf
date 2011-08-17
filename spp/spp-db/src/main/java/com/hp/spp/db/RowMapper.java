package com.hp.spp.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a single query result into an object.
 * For example if the query is for a person, the mapper could look like this:
 * <pre>
 * class PersonMapper implements RowMapper<Person> {
 *   Person mapRow(ResultSet row, int rowNum) throws SQLException {
 *     return new Person(row.getString("first_name"), row.getString("last_name"));
 *   }
 * }
 * </pre>
 * If the value returned by <code>mapRow</code> method is different from <tt>null</tt>, it will be
 * added to the list returned by different <tt>query</tt> methods in {@link DB} and {@link DatabaseTransaction}
 * classes. If the returned value is <tt>null</tt> it will not be added to the result.
 * Note that the implementations should not call <tt>ResultSet.next</tt> or any other methods that
 * move the cursor in the result set - this will create unexpected results. 
 */
public interface RowMapper<E> {
    E mapRow(ResultSet row, int rowNum) throws SQLException;
}

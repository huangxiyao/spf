package com.hp.spp.config;

import junit.framework.Assert;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.Context;
import java.util.Hashtable;
import java.util.Iterator;

import com.hp.spp.db.DataSourceHolder;
import org.apache.log4j.BasicConfigurator;

/**
 * This class is not regular JUnit test as it makes connections to the database. Its pupose is
 * to perform ad-hoc testing.
 */
public class JDBCConfigEntryDAOTest {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		try {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			if (args.length == 3) {
				env.put(Context.PROVIDER_URL, args[0]);
				env.put(Context.SECURITY_PRINCIPAL, args[1]);
				env.put(Context.SECURITY_CREDENTIALS, args[2]);
			}
			else {
				env.put(Context.PROVIDER_URL, "t3://localhost:27005");
				env.put(Context.SECURITY_PRINCIPAL, "weblogic");
				env.put(Context.SECURITY_CREDENTIALS, "weblogic");
			}
			Object obj = new InitialContext(env).lookup("jdbc/SPP_PORTALDS");
			DataSource ds = (DataSource) obj;
			DataSourceHolder.setTestDataSource(ds);
			JDBCConfigEntryDAO dao = new JDBCConfigEntryDAO();
//			JDBCConfigEntryDAO dao = new JDBCConfigEntryDAO(ds);

			ConfigEntry entry = dao.load(name("TEST_NON_EXISTING_ENTRY"));
			Assert.assertNull("Null returned for non-existing entry", entry);

			ConfigEntry entry2 = new ConfigEntry(name("TEST_CONFIG_ENTRY"), 3, false, true, "some description");
			dao.save(entry2);

			ConfigEntry entry3 = dao.load(entry2.getName());
			Assert.assertNotNull("Saved entry successfully loaded", entry3);
			Assert.assertEquals("Saved entry value", 3, entry3.getIntValue());
			Assert.assertEquals("Saved entry required flag", false, entry3.isRequired());
			Assert.assertEquals("Saved entry readOnly flag", true, entry3.isReadOnly());
			Assert.assertEquals("Saved entry type is int", ConfigEntry.TYPE_INT, entry3.getType());
			Assert.assertEquals("Saved entry description", "some description", entry3.getDescription());

			dao.save(new ConfigEntry(entry3.getName(), true, true, false, "updated description"));
			ConfigEntry entry4 = dao.load(entry3.getName());
			Assert.assertNotNull("Updated entry successfully loaded", entry4);
			Assert.assertEquals("Updated entry value", true, entry4.getBooleanValue());
			Assert.assertEquals("Updated entry required flag", true, entry4.isRequired());
			Assert.assertEquals("Updated entry readOnly flag", false, entry4.isReadOnly());
			Assert.assertEquals("Updated entry type is boolean", ConfigEntry.TYPE_BOOLEAN, entry4.getType());
			Assert.assertEquals("Updated entry description", "updated description", entry4.getDescription());

			Iterator it = dao.getAllEntries().iterator();
			boolean found = false;
			while (it.hasNext() && !found) {
				ConfigEntry entry5 = (ConfigEntry) it.next();
				if (entry5.getName().equals(entry4.getName())) {
					found = true;
				}
			}
			Assert.assertTrue("Iterator returned saved entry", found);

			dao.delete(entry2.getName());
			Assert.assertNull("Entry deleted", dao.load(entry2.getName()));

			it = dao.getAllEntries().iterator();
			found = false;
			while (it.hasNext() && !found) {
				ConfigEntry entry6 = (ConfigEntry) it.next();
				if (entry6.getName().equals(entry4.getName())) {
					found = true;
				}
			}
			Assert.assertFalse("Iterator did not returned deleted entry", found);

			System.out.println("ALL TESTS SUCCESSFUL");
		}
		catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	static String name(String name) {
		return "JDBCConfigEntryDAOTest." + name;
	}
}

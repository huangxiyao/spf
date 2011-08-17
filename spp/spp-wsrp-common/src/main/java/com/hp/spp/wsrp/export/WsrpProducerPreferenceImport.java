package com.hp.spp.wsrp.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hp.spp.db.DatabaseTransaction;
import com.hp.spp.db.RowMapper;
import com.hp.spp.wsrp.export.exception.CommonException;
import com.hp.spp.wsrp.export.exception.ImportException;
import com.hp.spp.wsrp.export.exception.UnsupportedMethodException;
import com.hp.spp.wsrp.export.info.producer.PortletInfo;
import com.hp.spp.wsrp.export.info.producer.PreferenceInfo;
import com.hp.spp.wsrp.export.util.DOMUtils;
import com.hp.frameworks.wpa.wsrp4j.services.portletentityregistry.RegistryCache;

public class WsrpProducerPreferenceImport extends Import {

	public void importPreferences(Reader input, Writer output) throws ImportException {
		try {
			Document inputDocument = initializeDocument(input);
			Document resultDocument = importPreferences(inputDocument);

			write(resultDocument, output);
		} catch (CommonException e) {
			throw new ImportException(e);
		}
	}

	public Document importPreferences(Document input) throws ImportException {
		// first export the existing portlet preferences so we have a copy in case the import fails
		Document exportResult;
		try {
			Document exportInput = createExportInput(input);
			exportResult = new WsrpProducerPreferenceExport().export(exportInput);
		}
		catch (Exception e) {
			throw new ImportException("Error occured while exporting the current preference values", e);
		}

		try {
			doImport(input, exportResult);
			// flush the cache so the changes in DB are also visible in the memory
			RegistryCache.getInstance().flushEntry(RegistryCache.PORTLET_REGISTRY);
		}
		catch (Exception e) {
			// inject the error as the first element of the document
			createInternalError(exportResult, toString(e));
		}

		// return the saved export; it may contain an error information
		return exportResult;
	}

	private void createInternalError(Document exportResult, String msg) {
		exportResult.getDocumentElement().insertBefore(
				DOMUtils.createError(exportResult, msg),
				exportResult.getDocumentElement().getFirstChild());
	}

	protected void doImport(Document input, Document output) {
		// within a database transaction: iterate over portlets and for each portlet,
		// first remove its preferences and values and then insert new preferences and new values

		NodeList portlets = input.getElementsByTagName("portlet");
		if (portlets == null || portlets.getLength() == 0) {
			createInternalError(output, "No portlet found in the input file!");
		}
		else {
			for (int i = 0, len = portlets.getLength(); i < len; ++i) {
				try {
					PortletInfo portletInfo = new PortletInfo((Element) portlets.item(i));
					ImportTransaction transaction = new ImportTransaction(portletInfo);

					String error = (String) transaction.execute();
					if (error != null) {
						createInternalError(output, error);
					}
				}
				catch (Exception e) {
					createInternalError(output, toString(e));
				}
			}
		}
	}

	protected Document createExportInput(Document input) throws ParserConfigurationException, TransformerException, IOException {
		Document result = DOMUtils.newDocument();
		DOMSource src = new DOMSource(input);
		DOMResult res = new DOMResult(result);
		InputStream is = getClass().getResourceAsStream("/com/hp/spp/wsrp/export/importInputToExportInput.xsl");
		try {
			StreamSource xsl = new StreamSource(is);
			Transformer transformer = DOMUtils.newTransformer(xsl);
			transformer.transform(src, res);
		}
		finally {
			is.close();
		}
		return result;
	}

	private String toString(Throwable e) {
		StringWriter sw = new StringWriter();
		sw.write(e.toString());
		sw.write("\n");
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static class ImportTransaction extends DatabaseTransaction {
		private PortletInfo mPortletInfo;

		public ImportTransaction(PortletInfo portletInfo) {
			mPortletInfo = portletInfo;
		}

		protected Object doInTransaction() throws Exception {
			RowMapper<String> singleColumnRow = new RowMapper<String>() {
				public String mapRow(ResultSet resultSet, int i) throws SQLException {
					return resultSet.getString(1);
				}
			};
			String[] portletHandleArg = {mPortletInfo.getHandle()};
			// check if the portlet exists
			List portlets = query("select count(*) from wsrp_portlet where id = ?",
					singleColumnRow, 0, 1, portletHandleArg, null);
			int existingCount = Integer.parseInt((String) portlets.get(0));
			if (existingCount == 0) {
				return "Portlet with such a handle not found: " + mPortletInfo.getHandle();
			}

			// get the IDs of the preferences
			List prefIds = query("select ID from WSRP_PREFERENCE where PORTLET = ?",
					singleColumnRow, 0, 0, portletHandleArg, null);

			if(!prefIds.isEmpty()) {
				String portletInClause = createInClause(prefIds.size());

				// delete the old preference values
				update("delete from WSRP_PREFERENCE_VALUE where PREFERENCE in (" + portletInClause + ")",
						prefIds.toArray(new String[0]), null);

				// delete old prefernces
				update("delete from WSRP_PREFERENCE where ID in (" + portletInClause + ")",
						prefIds.toArray(new String[0]), null);
			}

			// create preference with values
			Collection preferences = mPortletInfo.getPreferences().values();
			for (Iterator it = preferences.iterator(); it.hasNext();) {
				PreferenceInfo pref = (PreferenceInfo) it.next();
				// create new preference
				String prefId = mPortletInfo.getHandle() + "." + pref.getName();
				update("insert into WSRP_PREFERENCE (ID, NAME, READ_ONLY, PORTLET) values (?, ?, ?, ?)",
						new String[] {prefId, pref.getName(), pref.getReadOnly(), mPortletInfo.getHandle()},
						null);
				// create new preference values
				List values = pref.getValues();
				for (int idx = 1, len = values.size(); idx <= len; ++idx) {
					update("insert into WSRP_PREFERENCE_VALUE (PREFERENCE, PREFERENCE_IDX, VALUE) values (?, ?, ?)",
						new String[] {prefId, String.valueOf(idx), (String) values.get(idx-1)},
						null);
				}
			}

			return null;
		}

		private String createInClause(int argCount) {
			StringBuffer inClause = new StringBuffer();
			for (int i = 0; i < argCount; ++i) {
				inClause.append('?');
				if (i < argCount - 1) {
					inClause.append(',');
				}
			}
			return inClause.toString();
		}
	}

	/**
	 * @deprecated
	 */
	Document replace(Document documentWsrpSource, Document documentVgnSource, Document documentVgnTarget) throws ImportException {
		throw new UnsupportedMethodException() ;
	}

}

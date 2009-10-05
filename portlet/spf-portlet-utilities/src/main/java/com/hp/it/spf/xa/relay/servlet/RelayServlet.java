/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.relay.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;

/**
 * <p>
 * Servlet for handling download requests for files from the <i>portlet resource
 * bundle folder</i> on the local system. If you are not using the portlet
 * resource bundle folder, you do not need this servlet.
 * </p>
 * <p>
 * The relay servlet is the means by which portlets in SPF can give clients
 * access to file content which is external to the portlet application (for
 * example, images). These external files are maintained outside of the portlet
 * application, in the resource bundle folder, for content administration
 * purposes (such as localization administration). When a portlet needs to
 * display one of those files to the user, it uses the SPF portlet framework's
 * {@link com.hp.it.spf.xa.i18n.portlet.I18nUtility#getLocalizedFileURL(javax.portlet.PortletRequest, javax.portlet.PortletResponse, String)}
 * (or a JSP tag like <code>&lt;spf-i18n-portlet:localizedFileURL&gt;</code>)
 * to generate a URL for downloading those files, and presents that URL in the
 * response to the user (for example, as the <code>SRC</code> attribute in an
 * <code>&lt;IMG&gt;</code> tag). That URL points to this relay servlet, so
 * when the client then opens that URL, it is this servlet which is invoked.
 * </p>
 * <p>
 * Therefore the operation of the relay servlet is as follows:
 * <ol>
 * <li>Get the filename to download from the URL.</li>
 * <li>Security-check the filename to make sure it is permitted to be
 * downloaded. This uses the <code>init_relay.properties</code> file if it
 * exists; otherwise it uses the <code>init_relay_default.properties</code>
 * file.</li>
 * <li>Open the file and stream it into the response.</li>
 * </ol>
 * </p>
 * 
 * @author <link href="kuang.cheng@hp.com"> Cheng Kuang </link>
 * @author <link href="scott.jorgenson@hp.com"> Scott Jorgenson </link>
 * @version TBD
 */
public class RelayServlet extends HttpServlet {

	/**
	 * Buffer size to use when relaying the file.
	 */
	private static final int MEM_BUFFER_SIZE = 4086;

	/**
	 * The name of the relay servlet configuration file. (The .properties
	 * extension is assumed by the
	 * {@link com.hp.it.spf.xa.properties.PropertyResourceBundleManager}.)
	 */
	private static final String RELAY_SERVLET_INIT_FILE = "init_relay";

	/**
	 * The name of the default relay servlet configuration file. (The
	 * .properties extension is assumed by the
	 * {@link com.hp.it.spf.xa.properties.PropertyResourceBundleManager}.)
	 */
	private static final String RELAY_SERVLET_DEFAULT_INIT_FILE = "init_relay_default";

	/**
	 * The key name prefix for the content type property in relay servlet
	 * configuration file.
	 */
	private static final String RELAY_SERVLET_INIT_KEY_CONTENTTYPE_FOR = "contentType";

	private String portletBundleDir;

	private String fileContentType;

	private long fileSize;

	private static final long serialVersionUID = -2022901825644909910L;

	/**
	 * Implements the HTTP <code>GET</code> method (the only method supported
	 * by the relay servlet). The filename to download is in the additional path
	 * information of the request, by definition. We validate the existence and
	 * permissibility of the file, and if those checks pass we process the file
	 * by streaming it back to the client. If the checks fail to pass, we send a
	 * file-not-found error back to the client (even if the file was found but
	 * was not permitted for download).
	 * 
	 * @param request
	 *            The HTTP request.
	 * @param response
	 *            The HTTP response.
	 * @throws ServletException
	 *             Some exception.
	 * @throws IOException
	 *             Some exception.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Filename to download comes from the additional path.
		String fileName = request.getPathInfo();
		try {
			if (!checkRequirements(fileName)) {
				sendError(response, HttpServletResponse.SC_NOT_FOUND,
						"The file: " + fileName + " was not found.");
				return;
			}
		} catch (Exception e) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND, "The file: "
					+ fileName + " was not found.");
			return;
		}
		try {
			processFile(fileName, response);
		} catch (Exception e) {
			// If there is an error during file streaming, do not send an error
			// back to the client -- it is too late for that.
			return;
		}

	}

	/**
	 * Open the given file and stream its content into the given response.
	 * 
	 * @param fileName
	 *            The file, relative to the portlet resource bundle folder.
	 * @param response
	 *            The response.
	 * @throws IOException
	 *             IO Exception
	 */
	private void processFile(String fileName, HttpServletResponse response)
			throws IOException {
		File file = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ServletOutputStream sos = null;
		boolean writeFlag = false;
		try {
			file = new File(makeFullPath(portletBundleDir, fileName));
			fileSize = file.length();
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			sos = response.getOutputStream();
			byte[] buffer = new byte[RelayServlet.MEM_BUFFER_SIZE];
			int bytesRead = bis.read(buffer, 0, buffer.length);
			// Set response status before there is any data written to client,
			// because this is the last opportunity to do so.
			sendSuccess(response);
			writeFlag = true;
			while (true) {
				if (bytesRead < 0) {
					break;
				}
				sos.write(buffer, 0, bytesRead);
				bytesRead = bis.read(buffer, 0, buffer.length);
			}

		} catch (IOException e) {
			// If no data was written to client, send error to response.
			if (!writeFlag) {
				sendError(response,
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"An internal error occured on the server. Please try again later.");
			}
			throw e;
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (Exception e2) {
					fis = null;
				}
			}
			if (null != bis) {
				try {
					bis.close();
				} catch (Exception e3) {
					bis = null;
				}
			}
			if (null != sos) {
				try {
					sos.flush();
					sos.close();
				} catch (Exception e4) {
					sos = null;
				}
			}
		}
	}

	/**
	 * Check that the file exists within the portlet resource bundle directory,
	 * and is a permissible file to download. Permitted files are identified by
	 * extension, configured in <code>init_relay.properties</code> (or
	 * <code>init_relay_default.properties</code> by default).
	 * 
	 * @param fileName
	 *            File name to check.
	 * @return True if OK do download, otherwise false.
	 */
	private boolean checkRequirements(String fileName) {
		// Check that the filename is not blank and does not include ".."
		// references (to avoid hacking outside of the portlet resource bundle
		// folder).
		if (!checkFileName(fileName)) {
			return false;
		}
		// Load the init_relay.properties into cache.
		getConfig(fileName);
		// Check whether the file extension name matches the
		// listed file extensions in initialization properties.
		if (!checkFileExtension(fileContentType)) {
			return false;
		}
		// Check whether the file exists.
		if (!checkFileExist(fileName, portletBundleDir)) {
			return false;
		}
		return true;
	}

	/**
	 * Check that the requested filename is not blank and does not include
	 * <code>..</code> references. This is to protecte against attempts to
	 * reference files outside of the portlet resource bundle folder.
	 * 
	 * @param fileName
	 *            File name to check.
	 * @return True if OK, otherwise false.
	 */
	private boolean checkFileName(String fileName) {
		if (null == fileName)
			return false;
		fileName = fileName.trim();
		if ("".equals(fileName))
			return false;
		if (fileName.indexOf("..") != -1)
			return false;
		return true;
	}

	/**
	 * Check that the requested file extension matches one of the permitted file
	 * extensions configured in <code>init_relay.properties</code> (or
	 * <code>init_relay_default.properties</code> by default). This is done by
	 * comparing the given file extension with the class attribute, which was
	 * previously loaded from the configuration file (such that it is non-null
	 * if permitted, and null if not permitted).
	 * 
	 * @param fileContentType
	 *            The file extension.
	 * @return True if OK, otherwise false.
	 */
	private boolean checkFileExtension(String fileContentType) {
		// Empty means this file type is not permitted for download.
		if (null == fileContentType || "".equals(fileContentType)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Return true if the file exists at the given path, and false otherwise.
	 * 
	 * @param fileName
	 *            The file name to check.
	 * @param path
	 *            The file path to check.
	 * @return True if file exists at that path, otherwise false.
	 */
	private boolean checkFileExist(String fileName, String path) {
		File f = new File(makeFullPath(path, fileName));
		if (f.exists()) {
			f = null;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return the file extension from the given filename, including the "." at
	 * the beginning of the extension. For example, given
	 * <code>html/picture.jpg</code> this returns <code>.jpg</code>.
	 * 
	 * @param fileName
	 *            The requested file name.
	 * @return The file extension (defined as whatever comes after the last "."
	 *         in the file name - or empty string if none).
	 */
	private String getFileExt(String fileName) {
		int i = fileName.lastIndexOf(".");
		if (i > -1)
			return fileName.substring(i);
		else
			return "";
	}

	/**
	 * Load portlet resource bundle folder path, and content type to use for
	 * this file extension, into the respective class attributes. The portlet
	 * resource bundle folder is taken from the
	 * <code>i18n_portlet_config.properties</code>, and the content type
	 * information is taken from the <code>init_relay.properties</code> (or
	 * <code>init_relay_default.properties</code> if the former does not
	 * exist). To read these files, we use
	 * {@link com.hp.it.spf.xa.properties.PropertyResourceBundleManager} which
	 * provides a hot-reloadable caching capability. If for some reason the
	 * files are not found, we quietly assume null for this information.
	 */
	private void getConfig(String fileName) {
		ResourceBundle portletI18nConfig = PropertyResourceBundleManager
				.getBundle(I18nUtility.PORTLET_I18N_CONFIG_FILE);
		portletBundleDir = getProperty(
				I18nUtility.PORTLET_I18N_CONFIG_PROP_BUNDLE_DIR,
				portletI18nConfig);
		if (portletBundleDir == null) {
			portletBundleDir = I18nUtility.BUNDLE_DIR_DEFAULT;
		}
		ResourceBundle relayServletConfig = PropertyResourceBundleManager
				.getBundle(RELAY_SERVLET_INIT_FILE);
		if (relayServletConfig == null) {
			relayServletConfig = PropertyResourceBundleManager
					.getBundle(RELAY_SERVLET_DEFAULT_INIT_FILE);
		}
		String fileExt = getFileExt(fileName);
		fileContentType = getProperty(RELAY_SERVLET_INIT_KEY_CONTENTTYPE_FOR
				+ fileExt, relayServletConfig);
	}

	/**
	 * Wrapper method to ignore exceptions when getting properties from the
	 * resource bundle.
	 * 
	 * @param key
	 *            A property key.
	 * @param rb
	 *            A resource bundle.
	 * @return The property value (if resource bundle was not found, or property
	 *         was not found, then null is returned).
	 */
	private String getProperty(String key, ResourceBundle rb) {
		try {
			return rb.getString(key);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Send HTTP success message back to the client. In the response, we mark it
	 * is cacheable and expiring in 1 year. We also set the content type from
	 * the information mapped in <code>init_relay.properties</code> or
	 * <code>init_relay_default.properties</code> (previously cached into the
	 * class attributes), and we set the content length from the file size (also
	 * previously set into the class attributes).
	 * 
	 * @param response
	 *            The HTTP response.
	 */
	private void sendSuccess(HttpServletResponse response) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		response.setContentType(fileContentType);
		response.setContentLength((int) fileSize);
		response.addHeader("Cache-Control", "public");
		response.addHeader("Expires", cal.getTime().toString());
		response.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Send an HTTP error back to the client.
	 * 
	 * @param response
	 *            The HTTP response.
	 * @param int
	 *            The HTTP error number.
	 * @param String
	 *            The error message to include in the response.
	 */
	private void sendError(HttpServletResponse response, int errNum,
			String errMsg) throws IOException {
		response.setContentType("text/html");
		String msgString = "<html>\n<head>\n<title>Error: " + errNum
				+ "</title>\n</head>\n<body>\n" + errMsg
				+ "\n</body>\n</html>\n";
		response.setContentLength(msgString.length());
		response.sendError(errNum, msgString);
	}

	/**
	 * Return a full path for the given path and file.
	 * 
	 * @param path
	 *            The path name.
	 * @param file
	 *            The file name.
	 * @return The fully-qualified pathname.
	 */
	private String makeFullPath(String path, String file) {
		String fullPath = path + "/" + file;
		fullPath.replaceAll("/+", "/");
		return fullPath;
	}

}

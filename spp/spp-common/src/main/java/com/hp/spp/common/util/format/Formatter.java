package com.hp.spp.common.util.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utils class to format elements.
 * @author MJULIENS
 *
 */
public class Formatter {
	
	/**
	 * Format a date into String without hours.
	 * @param date date to format
	 * @return formatted date
	 */
	public String dateToString(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(date);
	}
	
	/**
	 * Format a date into String with hours.
	 * @param date date to format
	 * @return formatted date
	 */
	public String dateToStringWithHours(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		return sdf.format(date);
	}
	
	public static String clobToString(Clob cl) throws IOException, SQLException 
    {
      if (cl == null) 
        return  "";
          
      StringBuffer strOut = new StringBuffer();
      String aux;
            
	// We access to stream, as this way we don't have to use the CLOB.length() which is slower...
	BufferedReader br = new BufferedReader(cl.getCharacterStream());

      while ((aux=br.readLine())!=null)
             strOut.append(aux);

      return strOut.toString();
    }

	/**
	 * Convert a blob into a byte[].
	 * Return an empty array if the blob is null
	 * @param blob
	 * @return
	 * @throws SQLException
	 */
	public static byte[] blobToByteArray(Blob blob) throws SQLException
	{
		byte[] octets;
		if (blob != null)
		{	
			int length = (int) blob.length();
			octets = blob.getBytes(1, length);
		}
		else
		{
			octets = new byte[0];
		}
		return octets;
	}

	public String encodeXmlForHtml(byte[] xmlBytes) {
		String xml = new String(xmlBytes);
		xml = xml.replaceAll("&amp;", "&amp;amp;");
		xml = xml.replaceAll("&gt;", "&amp;gt;");
		xml = xml.replaceAll("&lt;", "&amp;lt;");
		xml = xml.replaceAll("&apos;", "&amp;apos;");
		xml = xml.replaceAll("&quot;", "&amp;quot;");
		return Entities.encode(xml);
	}

	/**
	 * Escapes the ampersands that are not part of an entity.
	 * For example 'this & that' => 'this &amp; that',
	 * but '&quot;this&quot; & &quot;that&quot;' => 
	 * '&quot;this&quot; & &quot;that&quot;', i.e. the ampersands in the
	 * &quot; are not escaped.
	 * <p>Based on following regex replacement: &(?![#0-9a-zA-Z]+;)
	 * @param input
	 * @return
	 */
	public String escapeAmpersand(String input)	{
		String regex = "&(?![#0-9a-zA-Z]+;)";
		return input.replaceAll(regex, "&amp;");
	}
	
}

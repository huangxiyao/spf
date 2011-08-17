package com.hp.spp.portal.reports;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Class to generate the HTML code for the HTML reports
 * @author MJULIENS
 *
 */
public class HTMLReportsFormatter {

	
	/**
	 * Format as an HTML table from a list of labels and a matrix of Data (ArrayList of ArrayLists)
	 */
	public String format(ArrayList labelList, ArrayList dataList)
	{
		
		String html = new String("");
		if (dataList != null && dataList.size()>0)
		{
			html += "<p>"+dataList.size()+" results found.</p>";
			
			html += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n";
			// header
			html += "<tr>\n";
			for (int i=0; i<labelList.size(); i++)
			{
				html += "<td>"+labelList.get(i)+"</td>\n";
			}
			html += "</tr>\n";
			
			// content
			for (int j=0; j<dataList.size(); j++)
			{
				ArrayList row = (ArrayList) dataList.get(j);
				html += "<tr>\n";
				for (int i=0; i<row.size(); i++)
				{
					Object value = row.get(i);
					String formattedValue = new String();
					if (value instanceof Date)
					{
						SimpleDateFormat sdf = new SimpleDateFormat(ReportsConstants.DATE_FORMAT);
						formattedValue = sdf.format((Date) value);
					}
					else
					{
						formattedValue = value.toString();
					}
					html += "<td>"+formattedValue+"</td>\n";
				}
				html += "</tr>\n";
			}
			
			html += "</table>\n";
		}
		else
		{
			html += "<p>No results found !</p>";
		}
		return html;
	}
}




<%@ page language="java" contentType="application/vnd.ms-excel"
    pageEncoding="ISO-8859-1"%><%
    
    
    // get the list of data to display
    java.util.ArrayList dataList = (java.util.ArrayList) request.getSession().getAttribute(com.hp.spp.portal.reports.ReportsConstants.SESSION_KEY_DATALIST);
    // get a list of coma separeted labels
    java.util.ArrayList labelList = (java.util.ArrayList) request.getSession().getAttribute(com.hp.spp.portal.reports.ReportsConstants.SESSION_KEY_LABELLIST);    
    // get the report name
    String reportName = (String) request.getSession().getAttribute(com.hp.spp.portal.reports.ReportsConstants.SESSION_KEY_REPORT_NAME);    
    response.setHeader("Content-Disposition","attachment; filename=" + reportName + ".xls" );

    
    // remove the values from the session
    request.getSession().removeAttribute(com.hp.spp.portal.reports.ReportsConstants.SESSION_KEY_DATALIST);
    request.getSession().removeAttribute(com.hp.spp.portal.reports.ReportsConstants.SESSION_KEY_LABELLIST);
    request.getSession().removeAttribute(com.hp.spp.portal.reports.ReportsConstants.SESSION_KEY_REPORT_NAME);
    
    if ((labelList!=null) && (dataList!=null) )
    {
		
		String csv = new String("");
		
		if (dataList != null && dataList.size()>0)
		{
			for (int i=0; i<labelList.size(); i++)
			{
				csv += labelList.get(i) + com.hp.spp.portal.reports.ReportsConstants.CSV_SEPARATOR;
			}
			// remove the last separator
			if (csv.length()>0)
			{
				csv = csv.substring(0, csv.length() - com.hp.spp.portal.reports.ReportsConstants.CSV_SEPARATOR.length());
			}
			// add the new line
			csv += "\n";
			
			// content
			for (int j=0; j<dataList.size(); j++)
			{
				java.util.ArrayList row = (java.util.ArrayList) dataList.get(j);
				for (int i=0; i<row.size(); i++)
				{
					Object value = row.get(i);
					String formattedValue = new String();
					if (value instanceof java.sql.Date)
					{
						java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(com.hp.spp.portal.reports.ReportsConstants.DATE_FORMAT);
						formattedValue = sdf.format((java.util.Date) value);
					}
					else
					{
						formattedValue = value.toString();
					}
					csv += formattedValue + com.hp.spp.portal.reports.ReportsConstants.CSV_SEPARATOR;
				}
				// remove the last separator
				if (csv.length()>0)
				{
					csv = csv.substring(0, csv.length() - com.hp.spp.portal.reports.ReportsConstants.CSV_SEPARATOR.length());
				}
				// add the new line
				csv += "\n";
			}
			// remove the last \n
			if (csv.length()>0)
			{
				csv = csv.substring(0, csv.length()-1);
			}
		}
		else
		{
			csv += "No results found !";
		}
		out.print(csv);	
    }
    else
    {
    	out.print("Do not use \"Refresh\" button, close and retry");	
    }
%>
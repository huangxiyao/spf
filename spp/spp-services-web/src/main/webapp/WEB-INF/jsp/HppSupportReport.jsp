<!-- HppSupportReport.jsp -->
<%/*--
	@(#)HppSupportReport.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        ShivaShanker B            29-Oct-2006          Created

	Note :
	
--*/%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page import="
	java.util.Iterator,
	java.util.List,	
	java.util.ArrayList,
	java.util.Set,
	java.util.TreeMap,
	java.util.Map,
	java.util.HashMap,	
	javax.servlet.jsp.JspWriter,
	java.io.IOException,
	com.hp.spp.portlets.reports.hppreport.command.HPPReport,
	com.hp.spp.portlets.reports.hppreport.model.HPPReportModel,
	javax.portlet.*"
	
        contentType="text/html; charset=UTF-8"
             
%>

<%!
       
        /*
	   This method generates html table code fragment for the map.	
	*/
	private void writeMapHtml(JspWriter out, Object obj, String tableTitle) throws IOException {	       
	        if(obj!=null){
			out.println("<table width='100%'>");
			if(tableTitle!=null){
				out.println("<tr bgcolor='#0066FF'>");
					out.println("<td colspan='2' style='color: #ffffff;font-weight: bold;text-align: left;'>");			
						out.println(tableTitle);					
					out.println("</td>");
				out.println("</tr>");
			}	
			if (obj instanceof Map) {
				Map map = (Map) obj;
				for (Iterator it = map.entrySet().iterator(); it.hasNext();) {

					Map.Entry entry = (Map.Entry) it.next();
					out.println("<tr>");	
						out.println("<td>" + entry.getKey() + "</td>");
						out.println("<td>");
						writeObjectHtml(out, entry.getValue());
						out.println("</td>");				
					out.println("</tr>");
				}
			}
			out.println("</table>");
		}	
	}
	
	/*
	  This is a helper method to the writeMapHtml
	*/
	private void writeObjectHtml(JspWriter out, Object obj) throws IOException {
		if (obj instanceof Map) {		        
			writeMapHtml(out,obj,null);
		}
		else if (obj instanceof List) {
			writeListHtml(out, (List) obj);
		}
		else {
			out.println(obj);
		}
	}
	
	/*	  
	  This method iterates through the list and prints the values in single line .
	*/ 
	private void writeListHtml(JspWriter out, List list) throws IOException{
		out.println("<div>");
		for (Iterator it = list.iterator(); it.hasNext();) {
			Object obj = it.next();
			writeObjectHtml(out, obj);
		}
		out.println("</div>");
	}
	
	/* 
	  This method generates the rows for 
	  -  groups and its role or
	  -  groups and add/remove buttons.
	*/
	private void displayHPPassportGroups(String displayFlag,
		HPPReportModel aHPPReportModel,	JspWriter out) throws IOException{		
		Map groupRoleMap = aHPPReportModel.getHppGroupRoleMap();
		
		List localHPPGroups = aHPPReportModel.getLocalHPPGroups();
		Iterator localHPPGroupsItr = localHPPGroups.iterator();		
		
		//Display Hpp users groups
		int groupRoleMapSize = 0;
		if(groupRoleMap!=null && groupRoleMap.size()>0 ){
			groupRoleMapSize  = groupRoleMap.size();
		}		
		if(displayFlag.equals("printGroupsAndRoles")){
			out.println("<tr align='left' valign='top'>"); 
				out.println("<td colspan='5'>This user belongs to "+ groupRoleMapSize +" group(s)");	     
				out.println("</td>");	      
			out.println("</tr>");
			if(groupRoleMapSize >0 ){
				Iterator groupRoleMapItr =  groupRoleMap.keySet().iterator(); 
				while(groupRoleMapItr.hasNext()){
					String title = (String) groupRoleMapItr.next();
					String groupRole = (String) groupRoleMap.get(title);
					out.println("<tr align='left' valign='top'>"); 
						out.println("<td width='15%'>Group name</td>");
						out.println("<td width='14%'>"+ title +"</td>");
						out.println("<td width='10%'>&nbsp;</td>");
						out.println("<td width='12%'>Role name</td>");
						out.println("<td width='56%'>"+ groupRole +"</td>");
					out.println("</tr>");

				}
			}	
		
			
		}
		
		//Display HPP groups and buttons
		if(displayFlag.equals("printGroupsAndButtons")){
			String groupTitle = null;
			String groupRole = null;
			while(localHPPGroupsItr.hasNext()){
				groupTitle = (String) localHPPGroupsItr.next();
				// This variable will be true if group name of the local hpp group
				// exist in the user group, It is useful in making add/remove enable or disable
				// if both group names matches it is assumed that user can only remove groups
				//  (can not perform add)
				// else user can only add groups(can not perform remove)
				boolean groupExists= false;
				if(groupRoleMapSize >0){
					groupRole = (String)groupRoleMap.get(groupTitle);
					if(groupRoleMap.containsKey(groupTitle)){
						groupExists = true;
					}else{
						groupExists = false;
					}

				}    
				out.println("<tr align='left' valign='top'>"); 
					out.println("<td width='15%' height='16'>Group </td>"); 
					out.println("<td width='14%' height='16'>"+ groupTitle +"</td>"); 
					out.println("<td colspan='3' align='left' height='16'>"); 
					if(groupExists){
						out.println("<input type='submit' disabled name='addUser' value='Add user to group' onClick=addUserToGroup("+"'"+groupTitle+"'"+")>");
					}else{
						out.println("<input type='submit' name='addUser' value='Add user to group' onClick=addUserToGroup("+"'"+groupTitle+"'"+")>");
					}	
					out.println("</td>"); 
				out.println("</tr>"); 		    
					out.println("<tr align='left' valign='top'> "); 
					out.println("<td width='8%'>&nbsp;</td>"); 
					out.println("<td width='14%'>&nbsp;</td>"); 
					out.println("<td colspan='3'>"); 
					if(!groupExists){
						out.println("<input type='submit' disabled name='removeUser' value='Remove user from group' onClick=removeUserFromGroup("+"'"+groupTitle+"'"+")>"); 
					}else{
						out.println("<input type='submit' name='removeUser' value='Remove user from group' onClick=removeUserFromGroup("+"'"+groupTitle+"'"+")>"); 
					}	
					out.println("</td>"); 
				out.println("</tr>");   
					out.println("<tr align='left' valign='top'>"); 
					out.println("<td width='8%'>&nbsp;</td>"); 
					out.println("<td width='14%'>&nbsp;</td>"); 
					out.println("<td colspan='3'>&nbsp; </td>"); 
				out.println("</tr>");		
			
			}//while
		}//if	
		
	}//displayHPPassportGroups
%>

<portlet:defineObjects/>
<!-- Define variables to hold the action url -->
<portlet:actionURL var="hppReport">
	<portlet:param name="action" value="hppReport" />
</portlet:actionURL>
<portlet:actionURL var="addHppReport">
	<portlet:param name="action" value="addGroup" />
</portlet:actionURL>
<portlet:actionURL var="removeHppReport">
	<portlet:param name="action" value="removeGroup" />
</portlet:actionURL>
<script type="text/javascript">
<!--

	function addUserToGroup(groupTitle)
	{	
		
		document.forms["passportGroupsForm"].groupCommand.value = groupTitle;	
		document.forms["passportGroupsForm"].action='<%=addHppReport%>';
		document.forms["passportGroupsForm"].submit();
			
	}
	
	function removeUserFromGroup(groupTitle)
	{
						
		document.forms["passportGroupsForm"].groupCommand.value = groupTitle;		
		document.forms["passportGroupsForm"].action='<%=removeHppReport%>';
		document.forms["passportGroupsForm"].submit();
			
	}
// -->	
</script>



<!-- Search table ----->
<form name="searchForm" method="post" action="<%=hppReport%>">
  <table width="100%" border="0" cellspacing="2" cellpadding="0">
    <tr align="left" valign="top" bgcolor="#0066FF"> 
      <td colspan="4" style="color: #ffffff;font-weight: bold;text-align: left;">Search Criteria</td>
    </tr>
    <tr align="left" valign="top"> 
      <td colspan="4">Please ensure that you have selected the correct 'search 
        by' criteria, see red asterik<font color="#FF0000"> *</font></td>
    </tr>
    <tr align="left" valign="top"> 
      <td width="11%" height="7">Criteria</td>
      <td width="17%" height="7">       
      	<spring:bind path="hppReport.searchEntityCommand.entityIdentifier">
        	<input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
        	<font color="red"><c:out value="${status.errorMessage}"/></font>
        </spring:bind>
      </td>
      <td colspan="2" height="7">&nbsp;</td>
    </tr>
    <tr align="left" valign="top"> 
      <td width="11%" height="3">Search by <font color="#FF0000">*</font></td>
      <td width="17%" height="3"> 
      
      <spring:bind path="hppReport.searchEntityCommand.entityType">
        <input type="radio" name="<c:out value="${status.expression}"/>" value="1" <c:if test='${status.value == "1"}'>checked</c:if>>
       </spring:bind>  
        User ID</td>
      <td width="19%" height="3">
        <spring:bind path="hppReport.searchEntityCommand.entityType">
          <input type="radio" name="<c:out value="${status.expression}"/>"value="2" <c:if test='${status.value == "2"}'>checked</c:if>>
        </spring:bind>   
        HP Passport ID</td>
      <td width="53%" height="3"> 
       <spring:bind path="hppReport.searchEntityCommand.entityType">
          <input type="radio" name="<c:out value="${status.expression}"/>" value="3" <c:if test='${status.value == "3"}'>checked</c:if>>
        </spring:bind> 
        Email Address</td>
    </tr>
    <tr align="left" valign="top"> 
      <td width="11%" height="7">&nbsp;</td>
      <td width="17%" height="7">&nbsp;</td>
      <td width="19%" height="7">&nbsp;</td>
      <td width="53%" height="7">&nbsp;</td>
    </tr>
    <tr align="left" valign="top"> 
      <td width="11%" height="7">&nbsp;</td>
      <td width="17%" height="7">&nbsp;</td>
      <td width="19%" height="7">&nbsp;</td>
      <td width="53%" height="7"> 
        <div align="right"> 
          <input type="submit" name="Search" value="Search">          
        </div>       
      </td>
    </tr>
  </table>  
  
</form>

<!-- HPP User information table ----->
<%      
	HPPReport aHPPReport = null;
	HPPReportModel aHPPReportModel = null;		
	aHPPReport = (HPPReport)request.getAttribute("reportModel");		 
	if(aHPPReport!=null){
		aHPPReportModel = aHPPReport.getHppReportModel();
		if(aHPPReportModel!=null && !aHPPReportModel.isInvalidSearch()){
			Map userProfile  = aHPPReportModel.getUserProfileMap();		
			try{					
				writeMapHtml(out,userProfile,"HPP User Information");					
			}catch(IOException ioex){
				ioex.printStackTrace();	
			}
		}	
	}
%>

<!-- HP Passport groups table ----->

<%        
  if(aHPPReportModel!=null && !aHPPReportModel.isInvalidSearch()){   
 	List localHPPGroups = aHPPReportModel.getLocalHPPGroups();
 	if(localHPPGroups!=null && localHPPGroups.size() > 0 ) 	{ %> 
 	
		<form name="passportGroupsForm" method="post">
		  <table width="100%" border="0" cellspacing="2" cellpadding="0">
		    <tr align="left" valign="top" bgcolor="#0066FF">  
		      <td colspan="5" style="color: #ffffff;font-weight: bold;text-align: left;">HP Passport groups</td>
		    </tr>		    		     
		    <% displayHPPassportGroups("printGroupsAndRoles",aHPPReportModel,out); %>		   
		    <tr align="left" valign="top"> 
		      <td width="8%" height="16">&nbsp;</td>
		      <td width="14%" height="16">&nbsp;</td>
		      <td colspan="3" align="left" height="16">&nbsp;</td>
		    </tr>
		    <% displayHPPassportGroups("printGroupsAndButtons",aHPPReportModel,out); %>
		  </table>		  
		   <spring:bind path="hppReport.groupCommand">
		   	<input type="hidden" name="groupCommand" value="">;		  
		   </spring:bind>
		  <input type="hidden" name="actionFrom" value="">		   
		</form>
<%} 	}%>

 
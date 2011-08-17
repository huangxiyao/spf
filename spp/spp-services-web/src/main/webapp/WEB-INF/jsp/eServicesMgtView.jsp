<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:actionURL var="actionEServiceImportUrl">
	<portlet:param name="action" value="importEService" />
</portlet:actionURL>

<portlet:actionURL var="actionEServiceDeleteUrl">
	<portlet:param name="action" value="deleteEService" />
</portlet:actionURL>

<portlet:actionURL var="actionEServiceExportUrl">
	<portlet:param name="action" value="exportEService" />
</portlet:actionURL>

<portlet:renderURL var="historyUrl">
	<portlet:param name="action" value="eServiceHistory" />
</portlet:renderURL>

<style type="text/css">
.sppActiveTab
{
	background-color: #FF6600;
}

.sppInactiveTab
{
	background-color: #999999;
}

.sppSectionHeader
{
	border-style: solid;
	border-width: 0px; 
	border-bottom-width: 5px;
	border-color: #FF6600;
}

.sppSectionHeaderBelowTab
{
	border-style: solid;
	border-width: 0px; 
	border-bottom-width: 5px;
	border-top-width: 2px;
	border-color: #FF6600;
}
</style>

<script type="text/javascript">

function disableButtons(disable)
{
		document.forms["deleteExport"].deleteButton.disabled=disable;
		document.forms["deleteExport"].exportButton.disabled=disable;
		document.forms["importEService"].importButton.disabled=disable;
}

function deleteEService()
{
	if (!checkOneChecked())
	{
		alert("Please select at least one eService to delete");
	}
	else
	{
		document.forms["deleteExport"].action='<c:out value="${actionEServiceDeleteUrl}" escapeXml="false"/>';
		disableButtons(true);
		document.forms["deleteExport"].submit();
	}
}

function exportEService()
{
	if (!checkOneChecked())
	{
		alert("Please select at least one eService to export");
	}
	else
	{
		document.forms["deleteExport"].action='<c:out value="${actionEServiceExportUrl}" escapeXml="false"/>';
		disableButtons(true);
		document.forms["deleteExport"].submit();
	}
}

function isArray(obj)
{
	if (obj == null)
	{
		return false;
	}
	else
	{
		return(typeof(obj.length)=="undefined")?false:true;
	}
}

function checkOneChecked()
{
	var totalChecked = 0;
	if (isArray(document.forms["deleteExport"].eServiceId))
	{
		for ( i=0; i < document.forms["deleteExport"].eServiceId.length; i++ ) 
		{
	    	if ( document.forms["deleteExport"].eServiceId[i].checked == true ) {
	    	  	totalChecked++;
			}
		}
	}
	else
	{
		if ( (document.forms["deleteExport"].eServiceId != null) && (document.forms["deleteExport"].eServiceId.checked == true) )
		{
			totalChecked = 1;
		}
	}	
	if (totalChecked==0)
	{
		return false;
	}
	else
	{
		return true;
	}	
}

function selectAll(bool)
{
        var inputs = document.getElementsByTagName("input");
        for (var cpt = 0 ; cpt < inputs.length ; cpt++)
        {
                if ((inputs[cpt].type == "checkbox") && (inputs[cpt].name == "eServiceId"))
                        inputs[cpt].checked = bool;
        }
}

function importXML()
{
	if (document.forms["importEService"].file.value=="")
	{
		alert("Please enter an XML Content");
	}
	else
	{
		disableButtons(true);
		document.forms["importEService"].submit();
	}
}

</script>

<portlet:renderURL var="standardParameterSetsUrl">
	<portlet:param name="action" value="standardParameterSets" />
</portlet:renderURL>

<portlet:defineObjects />

<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<table cellpadding="20" align="left">
				<tr>
					<td class="sppActiveTab" align="center">
						<div class="colorFFFFFFbld">EServices</div>
					</td>
					<td class="sppInactiveTab" align="center">
						<a class="colorFFFFFFbld" href="<c:out value='${standardParameterSetsUrl}' escapeXml='false'/>">Parameter Sets</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			
<p class="sppSectionHeaderBelowTab">
<br>
<c:if test="${not empty confirmationMessage}">
	<font color="blue"><c:out value="${confirmationMessage}" /></font>
	<c:set var="confirmationMessage" value="" scope="session"/>
	<br>
</c:if>
<br>
<b>Import eServices definitions:</b>
</p> 

<p><i>Please copy the XML content you would like to upload, paste it in
the textarea below, and press the "Import eService Definition" button.</i></p>

		</td>
	</tr>
	<tr>
		<td>
			<form method="POST" name="importEService"
				action="<c:out value='${actionEServiceImportUrl}' escapeXml='false'/>">
				
				<textarea name="file" cols="70" rows="15"></textarea> 
				<br>
				<br>
				<input type="button" name="importButton" value="Import eService Definition" onclick="importXML()" class="secButton">
			</form>
		
		<p>
		</p>
		</td>
	</tr>
	<tr>
		<td>

<p class="sppSectionHeader">
<b>Export/Delete eService definitions :</b>
</p>
<p><i>Please select the eService(s) you would like to export or delete, and
press the "Export eService definition" or "Delete eService definition" button.</i></p>

		</td>
	</tr>
	<tr>
		<td>
			<form name="deleteExport" method="POST">
			<br>
			<a onclick="selectAll(true);">Select all</a> / <a onclick="selectAll(false);">Unselect all</a> 
			<br>
			<table border="1" cellspacing="0">
				<tr>
					<td>&nbsp;</td>
					<td width="250">Name</td>
					<td>Create Date <br>
					<i>(mm/dd/yyyy)</i></td>
					<td>Last update <br>
					<i>(mm/dd/yyyy)</i></td>
				</tr>
				
				<c:forEach items="${eServiceList}" var="eService">
					<tr>
						<td>
							<input type="checkbox" name="eServiceId" value="<c:out value='${eService.id}'/>">
						</td>
						<td><c:out value='${eService.name}'/></td>
						<td>
							<fmt:formatDate value="${eService.creationDate}" pattern="MM/dd/yyyy"/>
						</td>
						<td>
							<fmt:formatDate value="${eService.lastModificationDate}" pattern="MM/dd/yyyy"/>
						</td>
					</tr>	
				</c:forEach>
			
			</table>
		
			<br>
			<input type="button" name="exportButton" value="Export eService definition" onclick="exportEService()" class="secButton"> 
			<input type="button" name="deleteButton" value="Delete eService definition" onclick="deleteEService()" class="secButton">
			
			</form>
		</td>
	</tr>
	<tr>
		<td>

<p class="sppSectionHeader">
<b>View eService definition change history :</b>
</p>
<br>
		</td>
	</tr>
	<tr>
		<td>
<i>
<a href="<c:out value='${historyUrl}' escapeXml='false'/>">Access eService definition change
history and associated configuration backups.</a>
</i> 
<br>
		</td>
	</tr>
</table>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:actionURL var="actionStandardParameterSetImportUrl">
	<portlet:param name="action" value="importStandardParameterSet" />
</portlet:actionURL>

<portlet:actionURL var="actionStandardParameterSetDeleteUrl">
	<portlet:param name="action" value="deleteStandardParameterSet" />
</portlet:actionURL>

<portlet:actionURL var="actionStandardParameterSetExportUrl">
	<portlet:param name="action" value="exportStandardParameterSet" />
</portlet:actionURL>

<portlet:renderURL var="historyUrl">
	<portlet:param name="action" value="standardParameterSetHistory" />
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
		document.forms["importStandardParameterSet"].importButton.disabled=disable;
}

function deleteStandardParameterSet()
{
	if (!checkOneChecked())
	{
		alert("Please select at least one parameter set to delete");
	}
	else
	{
		document.forms["deleteExport"].action='<c:out value="${actionStandardParameterSetDeleteUrl}" escapeXml="false"/>';
		disableButtons(true);
		document.forms["deleteExport"].submit();
	}
}

function exportStandardParameterSet()
{
	if (!checkOneChecked())
	{
		alert("Please select at least one parameter set to export");
	}
	else
	{
		document.forms["deleteExport"].action='<c:out value="${actionStandardParameterSetExportUrl}" escapeXml="false"/>';
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
	if (isArray(document.forms["deleteExport"].standardParameterSetId))
	{
		for ( i=0; i < document.forms["deleteExport"].standardParameterSetId.length; i++ ) 
		{
	    	if ( document.forms["deleteExport"].standardParameterSetId[i].checked == true ) {
	    	  	totalChecked++;
			}
		}
	}
	else
	{
		if ( (document.forms["deleteExport"].standardParameterSetId != null) && (document.forms["deleteExport"].standardParameterSetId.checked == true) )
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
	        if ((inputs[cpt].type == "checkbox") && (inputs[cpt].name == "standardParameterSetId"))
	                inputs[cpt].checked = bool;
	}
}

function importXML()
{
	if (document.forms["importStandardParameterSet"].file.value=="")
	{
		alert("Please enter an XML Content");
	}
	else
	{
		disableButtons(true);
		document.forms["importStandardParameterSet"].submit();
	}
}

</script>

<portlet:renderURL var="eServicesUrl">
	<portlet:param name="action" value="eservices" />
</portlet:renderURL>

<portlet:defineObjects />

<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<table cellpadding="20" align="left">
				<tr>
					<td class="sppInactiveTab" align="center">
						<a class="colorFFFFFFbld" href="<c:out value='${eServicesUrl}' escapeXml='false'/>">EServices</a>
					</td>
					<td class="sppActiveTab" align="center">
						<div class="colorFFFFFFbld">Parameter Sets</div>
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
<b>Import parameter set definitions:</b>
</p> 

<p><i>Please copy the XML content you would like to upload, paste it in
the textarea below, and press the "Import standardParameterSet Definition" button.</i></p>

		</td>
	</tr>
	<tr>
		<td>
			<form method="POST" name="importStandardParameterSet"
				action="<c:out value='${actionStandardParameterSetImportUrl}' escapeXml='false'/>">
				
				<textarea name="file" cols="70" rows="15"></textarea> 
				<br>
				<br>
				<input type="button" name="importButton" value="Import parameter set definition" onclick="importXML()" class="secButton">
			
			</form>

			<p>
			</p>
		</td>
	</tr>
	<tr>
		<td>

<p class="sppSectionHeader">
<b>Export/Delete parameter set definitions :</b>
</p>
<p><i>Please select the parameter set(s) you would like to export or delete, and
press the "Export parameter set definition" or "Delete parameter set definition" button.</i></p>

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
				
				<c:forEach items="${standardParameterSetList}" var="standardParameterSet">
					<tr>
						<td>
							<input type="checkbox" name="standardParameterSetId" value="<c:out value='${standardParameterSet.id}'/>">
						</td>
						<td><c:out value='${standardParameterSet.name}'/></td>
						<td>
							<fmt:formatDate value="${standardParameterSet.creationDate}" pattern="MM/dd/yyyy"/>
						</td>
						<td>
							<fmt:formatDate value="${standardParameterSet.lastModificationDate}" pattern="MM/dd/yyyy"/>
						</td>
					</tr>	
				</c:forEach>
			
			</table>
			<br>
			<input type="button" name="exportButton" value="Export parameter set definition" onclick="exportStandardParameterSet()" class="secButton"> 
			<input type="button" name="deleteButton" value="Delete parameter set definition" onclick="deleteStandardParameterSet()" class="secButton">
			
			</form>
		</td>
	</tr>
	<tr>
		<td>
<p class="sppSectionHeader">
<b>View parameter set definition change history :</b>
</p>
<br>
		</td>
	</tr>
	<tr>
		<td>
<i>
<a href="<c:out value='${historyUrl}' escapeXml='false'/>">Access parameter set definition change
history and associated configuration backups.</a>
</i> 
<br>
		</td>
	</tr>
</table>
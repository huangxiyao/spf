<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" />

	<xsl:template match="/portlet-preferences">
		<portlet-handle-list>
			<xsl:copy-of select="producer-info" />
			<xsl:apply-templates select="portlet/handle" />
		</portlet-handle-list>
	</xsl:template>

	<xsl:template match="handle">
		<handle><xsl:value-of select="." /></handle>
	</xsl:template>

</xsl:stylesheet>
<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="xml"/>
   <xsl:param select="''" name="indent-increment"/>
   
   <xsl:template match="*" name="noname">
      <xsl:param select="'&#xA;'" name="indent"/>
      <xsl:value-of select="$indent"/>
      <xsl:copy>
         <xsl:copy-of select="@*"/>
		<xsl:apply-templates>
            <xsl:with-param select="concat($indent, $indent-increment)" name="indent"/>
         </xsl:apply-templates>
      </xsl:copy>
   </xsl:template>
   
   <xsl:template match="comment()|processing-instruction()">
      <xsl:copy/>
   </xsl:template>
   
   <xsl:template match="text()[normalize-space(.)='']"/>
</xsl:stylesheet>

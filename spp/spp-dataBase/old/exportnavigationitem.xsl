<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml" indent="yes"/>

	<xsl:template match="export">
	SET DEFINE OFF <br/>
	SET ESCAPE \;<br/>

delete from spp_eservice_parameter;<br/>
delete from spp_eservice;<br/>
delete from SPP_STANDARD_PARAMETERSET;<br/>
<br/>

PROMPT STANDARD  PARAMETER SET<br/>
insert into SPP_STANDARD_PARAMETERSET (ID, SITE_ID ,NAME) select SPP_STANDARD_PARAMETERSET_SEQ.nextval,id,'smartportal' from SPP_site where name='smartportal';<br/>


Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PartnerPhone','PartnerPhone');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PrimaryChannelSegment','PrimaryChannelSegment');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PhysCity','PhysCity');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PhysAdLine1','PhysAdLine1');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PhysAdLine2','PhysAdLine2');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PhysAdLine3','PhysAdLine3');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PartnerName','PartnerName');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PartnerFax','PartnerFax');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PartnerProId','PartnerProId');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PhysPostalCode','PhysPostalCode');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PhysCountry','PhysCountry');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'Email','Email');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'FirstName','FirstName');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'HpInternalUser','HpInternalUser');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PreferredLanguageCode','PreferredLanguageCode');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'LastLoginDate','LastLoginDate');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'LastName','LastName');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'CountryCode','CountryCode');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'IsPartnerAdmin','IsPartnerAdmin');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'HPPUserId','HPPUserId');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'Tier','Tier');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'HPOrg','HPOrg');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PartnerProIdHQ','PartnerProIdHQ');<br/>
Insert into SPP_ESERVICE_PARAMETER (ID,STANDARD_PARAMETERSET_ID ,NAME , EXPRESSION) values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_STANDARD_PARAMETERSET_SEQ.currval,'PortalSessionId','${PortalSessionId}');<br/>


<br/>
			<br/>
			<xsl:apply-templates select="eService"/>
	</xsl:template>


	<xsl:template match="eService">
			PROMPT EService : <xsl:value-of select="title"/><br/>
			Insert into SPP_ESERVICE (ID,SITE_ID,STANDARDPARAMETERSET_ID,NAME,METHOD,PRODUCTION_URL,TEST_URL,CREATION_DATE,LAST_MODIFICATION_DATE,IS_NEW_WINDOW,SECURITY_MODE ) 
			values (SPP_ESERVICE_ID_SEQ.nextval,
					1,
					<xsl:choose>
  						<xsl:when test="stdParameter">
    						SPP_STANDARD_PARAMETERSET_SEQ.currval,
  						</xsl:when>
  						<xsl:otherwise>
  							null,
  						</xsl:otherwise>
					</xsl:choose>
					'<xsl:value-of select="name"/>',
					'<xsl:value-of select="method"/>',
					'<xsl:value-of select="productionUrl"/>',
					'<xsl:value-of select="testUrl"/>',
					sysdate,
					sysdate,0,0
			);
			<br/>
			
			
				 <xsl:for-each select="parameter/name">
			 	Insert into SPP_ESERVICE_PARAMETER (ID,ESERVICE_ID ,NAME , EXPRESSION)
				 values (SPP_ESERVICE_PARAMETER_ID_SEQ.nextval,SPP_ESERVICE_ID_SEQ.currval,'<xsl:value-of select="."/>','<xsl:value-of select="."/>');
			 	<br/>
     
     		 </xsl:for-each>
     		 <br/>
	</xsl:template>

</xsl:stylesheet>

<%-----------------------------------------------------------------------------
	cleansheet_footer.jsp
	
	STYLE: Cleansheet Footer
	STYLE TYPE: Footer
	USES HEADER FILE: Yes

	Displays the privacy link, terms link, and optionally, the sales and
	service terms link, feedback link, and site map link.
	For German locale only, the imprint link is displayed.
	The Omniture metrics collection script will be inserted.

-----------------------------------------------------------------------------%>

<%-----------------------------------------------------------------------------
	Imports
-----------------------------------------------------------------------------%>

<%@ page import="com.hp.frameworks.wpa.portal.hpweb.Utils" %>

<%-----------------------------------------------------------------------------
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cscore" uri="http://frameworks.hp.com/wpa/cleansheet-core" %>

<%----------------------------------------------------------------------------- 
	Style Arguments
-----------------------------------------------------------------------------%>

<%-- boolean widePage --%>
<c:set var="widePageArg" value="${widePage}" />


<%-----------------------------------------------------------------------------
	Variables
-----------------------------------------------------------------------------%>

<%
/*
String privacyUrlDef = Utils.getI18nValue(i18nID, "hpweb.privacyUrl",
			portalContext);
String imprintUrlDef = Utils.getI18nValue(i18nID, "hpweb.imprintUrl",
			portalContext);
String termsUrlDef = Utils.getI18nValue(i18nID, "hpweb.termsUrl",
			portalContext);
String saleTermsUrlDef = Utils.getI18nValue(i18nID, "hpweb.saleTermsUrl",
			portalContext);
String feedbackUrlDef = Utils.getI18nValue(i18nID, "hpweb.feedbackUrl",
			portalContext);
String feedbackTextDef = Utils.getI18nValue(i18nID, "hpweb.feedbackText",
			portalContext);
String metricsUrlDef = Utils.getI18nValue(i18nID, "hpweb.metricsUrl",
			portalContext);
String siteMapUrlDef = Utils.getI18nValue(i18nID, "hpweb.siteMapUrl",
			portalContext);
*/


%>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />
		

<%-----------------------------------------------------------------------------
	Template
-----------------------------------------------------------------------------%>
 <!-- BEGIN FOOTER AREA -->
      <div class="footer" >
 <!-- BEGIN FOOTER RIGHT NAV TOP ROW -->
        <div class="right top">
          <ul>
            <li>
              <span class="no_printable">
                <a class="hand" id="fnr_l1_p1_trigger" tabindex="190" title="About HP">About HP</a>

                <span class="pipe_chr_solid"> </span>
              </span>
            </li>
            <li>
              <span class="no_printable">
                <a class="link_metrics" href="http://welcome.hp.com/country/us/en/support_task.html" name="us_en_home_footer_l2_corp_fixed_support-and-drivers" tabindex="192" title="Support &amp; Drivers">Support &amp; Drivers</a>
                <span class="pipe_chr_solid"> </span>

              </span>
            </li>
            <li>
              <span class="no_printable">
                <a class="link_metrics" href="http://www.hpl.hp.com" name="us_en_home_footer_l3_corp_fixed_hp-labs" tabindex="194" title="HP Labs">HP Labs</a>
                <span class="pipe_chr_solid"> </span>
              </span>
            </li>

            <li>
              <span class="no_printable">
                <a class="hand" id="fnr_l1_p4_trigger" tabindex="196" title="Resources">Resources</a>
                <span class="pipe_chr_solid"> </span>
              </span>
            </li>
            <li>
              <span class="no_printable">

                <a class="hand" id="fnr_l1_p5_trigger" tabindex="198" title="Customer Support">Customer Support</a>
              </span>
            </li>
          </ul>
        </div>
        <!-- END FOOTER RIGHT NAV TOP ROW -->
        <!-- BEGIN FOOTER RIGHT NAV MIDDLE ROW -->
        <div class="right middle" style="display:block;">

          <ul>
            <li>
              <span class="no_printable">
                <a class="link_metrics" href="http://welcome.hp.com/country/us/en/sitemap.html" name="us_en_home_footer_l1_corp_fixed_site-map" tabindex="200" title="Site Map">Site Map</a>
                <span class="pipe_chr_solid"> </span>
              </span>
            </li>
            <li>

              <span class="no_printable">
                <a class="link_metrics" href="http://h41105.www4.hp.com/m/us/en/index.xsl" name="us_en_home_footer_l2_corp_fixed_hp-mobile" tabindex="202" title="HP Mobile">HP Mobile</a>
                <span class="pipe_chr_solid"> </span>
              </span>
            </li>
            <li>
              <span class="no_printable">
                <a class="link_metrics" href="http://welcome.hp.com/country/us/en/contact_us.html" name="us_en_home_footer_l3_corp_fixed_contact-hp-/-customer-service" tabindex="204" title="Contact HP / Customer Service">Contact HP / Customer Service</a>

                <span class="pipe_chr_solid"> </span>
              </span>
            </li>
            <li>
              <span class="no_printable">
                <a class="link_metrics" href="http://www.hp.com/hpinfo/newsroom/recalls/index.html" name="us_en_home_footer_l4_corp_fixed_recalls_replacements" tabindex="206" title="Recalls &amp; Replacements">Recalls &amp; Replacements</a>
              </span>

            </li>
          </ul>
        </div>
        <!-- END FOOTER RIGHT NAV MIDDLE ROW -->
        <!-- BEGIN FOOTER RIGHT NAV BOTTOM ROW -->
        <div class="right bottom" style="display:block;">
          <ul>
            <li>
              <span class="no_printable">

                <a class="link_metrics" href="http://welcome.hp.com/country/us/en/privacy.html" name="us_en_home_footer_l1_corp_fixed_privacy-statement" tabindex="208" title="Privacy Statement">Privacy Statement</a>
                <span class="pipe_chr_solid"> </span>
              </span>
            </li>
            <li>
              <span class="no_printable">
                <a class="link_metrics" href="http://welcome.hp.com/country/us/en/termsofuse.html" name="us_en_home_footer_l2_corp_fixed_term-of-use" tabindex="210" title="Terms of Use">Terms of Use</a>

                <span class="pipe_chr_solid"> </span>
              </span>
            </li>
            <li class="copyright">© 2010 Hewlett-Packard Development Company, L.P.</li>
          </ul>
        </div>
        <!-- END FOOTER RIGHT NAV BOTTOM ROW -->
        <div class="left cselector" rel="180">
          <div class="footerleftimages">
            <a class="map png hand" tabindex="180" title="map">
              <span class="screenReading hidden">Select another location/language: opens popup layer</span>
            </a>
            <div class="flag" title="flag"> </div>
          </div>
          <div class="footerlefttext">

            <a class="country" tabindex="180" title="United States">United States</a>
          </div>
        </div>
        <!-- END FOOTER LEFT NAV -->
      </div>
      <!-- END FOOTER AREA -->
	

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

			<div class="footer" id="footer">
				<div class="right top">
					<ul>
						<li>
							<span class="no_printable">
								<!--<a title="About HP" class="hand" id="fnr_l1_p1_trigger" tabindex="190">About HP</a>-->
								<a title="About HP" class="hand link_metrics" id="fnr_l1_p1_trigger" tabindex="190" name="insert metrics id">About HP</a>
							</span>
							<span class="pipe_chr_solid"> </span>
						</li>
						<li>
							<span class="no_printable">
								<!--<a title="Support and Drivers" class="hand link_metrics" tabindex="192">Support &amp; Drivers</a>-->
								<a title="Support and Drivers" class="hand link_metrics" tabindex="192" name="insert metrics id here">Support &amp; Drivers</a>
							</span>
							<span class="pipe_chr_solid"> </span>
						</li>
						<li>
							<span class="no_printable">
								<a title="HP Labs" href="http://www.hpl.hp.com" name="ie_en_home_footer_l2_corp_fixed_hp-labs" tabindex="194" class="link_metrics">HP Labs</a>
							</span>
							<span class="pipe_chr_solid"> </span>
						</li>
						<li>
							<span class="no_printable">
								<!--<a title="Resources" class="hand" id="fnr_l1_p3_trigger" tabindex="196">Resources</a>-->
								<a title="Resources" class="hand link_metrics" name="insert metrics id here" id="fnr_l1_p3_trigger" tabindex="196">Resources</a>
							</span>
							<span class="pipe_chr_solid"> </span>
						</li>
						<li>
							<span class="no_printable">
								<!--<a title="Preferred Partners" class="hand" id="fnr_l1_p4_trigger" tabindex="198">Preferred Partners</a>-->
								<a title="Preferred Partners" class="hand link_metrics" name="insert id metrics here" id="fnr_l1_p4_trigger" tabindex="198">Preferred Partners</a>
							</span>
						</li>
					</ul>
				</div>
				<div class="right middle" style="display:block;">
					<ul>
						<li>
							<span class="no_printable">
								<a title="Site Map" href="http://www8.hp.com/ie/en/sitemap.html" name="ie_en_home_footer_l5_corp_fixed_site-map" tabindex="200" class="link_metrics">Site Map</a>
							</span>
							<span class="pipe_chr_solid"> </span>
						</li>
						<li>
							<span class="no_printable">
								<a title="HP Mobile" href="http://h41105.www4.hp.com/m/ie/en/index.xsl" name="ie_en_home_footer_l6_corp_fixed_hp-mobile" tabindex="200" class="link_metrics">HP Mobile</a>
							</span>
							<span class="pipe_chr_solid"> </span>
						</li>
						<li>
							<span class="no_printable">
								<a title="Contact HP and Customer Service" href="/country/ie/en/cs/contact-hp/contact.html" name="ie_en_home_footer_l7_corp_fixed_all-hp-contacts" tabindex="200" class="link_metrics">Contact HP / Customer Service</a>
							</span>
							<span class="pipe_chr_solid"> </span>
						</li>
						<li>
							<span class="no_printable">
								<a title="Personal Data Rights Notice" href="http://www8.hp.com/ie/en/personal-data-rights.html" name="ie_en_home_footer_l2_corp_fixed_personal-data-rights-notice" tabindex="200" class="link_metrics">Personal Data Rights Notice</a>
							</span>
						</li>
					</ul>
				</div>
				<div class="right bottom" style="display:block;">
					<ul>
						<li>
							<span class="no_printable">
								<a title="Privacy Statement" href="http://www8.hp.com/ie/en/privacy/privacy.html" name="ie_en_home_footer_l1_corp_fixed_privacy-statement" tabindex="200" class="link_metrics">Privacy Statement</a>
							</span>
							<span class="pipe_chr_solid"> </span>
						</li>
						<li>
							<span class="no_printable">
								<a title="Terms of use" href="http://www8.hp.com/ie/en/privacy/terms-of-use.html" name="ie_en_home_footer_l3_corp_fixed_term-of-use" tabindex="200" class="link_metrics">Terms of Use</a>
							</span>
							<span class="pipe_chr_solid"> </span>
						</li>
						<li class="copyright">© 2010 Hewlett-Packard Development Company, L.P.</li>
					</ul>
				</div>
				<!-- End new code footer link changes -->
				<div class="left cselector" rel="180">
					<div class="footerleftimages">
						<a title="map" class="map png hand" tabindex="180">
							<span class="screenReading hidden">Select another location/language: opens popup layer</span>
						</a>
						<!--<div title="flag" class="flag" style="background:transparent url(i/footer/flags/united_states.png) no-repeat center center;"> </div>-->
						<div title="flag" class="flag"> </div>
					</div>
					<div class="footerlefttext">
						<a title="United States" class="country" tabindex="180">United States</a>
					</div>
					<!-- Begin new code -->
					<!-- Optional for US only -->
                   <div class="footerlefttext">
						<div class="dotted_left"></div>
					</div>
					<div class="footerlefttext">
						<!--<a title="United States HP.com" class="country" tabindex="182">United States HP.com</a>-->
						<a title="United States HP.com" class="country link_metrics" name="insert metrics id here" tabindex="182">United States HP.com</a>
					</div>
					<!-- End new code -->
				</div>
		</div>
	

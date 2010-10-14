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
      <div class="footer" id="footer">
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
        <!-- BEGIN COUNTRY SELECTOR -->
        <div class="hidden" id="americas" title="Americas">
          <div class="continentheader">Americas</div>
          <div class="firstcolumn">

            <a class="link_metrics" href="http://welcome.hp.com/country/lamerica_nsc_cnt_amer/es/welcome.html" name="us_en_home_country-selector_l1_corp_fixed_america-central" tabindex="180" title="América Central">América Central</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ar/es/welcome.html" name="us_en_home_country-selector_l2_corp_fixed_argentina" tabindex="180" title="Argentina">Argentina</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/bo/es/welcome.html" name="us_en_home_country-selector_l3_corp_fixed_bolivia" tabindex="180" title="Bolivia">Bolivia</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/br/pt/welcome.html" name="us_en_home_country-selector_l4_corp_fixed_brasil" tabindex="180" title="Brasil">Brasil</a>

            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ca/en/welcome.html" name="us_en_home_country-selector_l5_corp_fixed_canada" tabindex="180" title="Canada">Canada</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ca/fr/welcome.html" name="us_en_home_country-selector_l6_corp_fixed_canada---french" tabindex="180" title="Canada - Français">Canada - Français</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/lamerica_nsc_carib/en/welcome.html" name="us_en_home_country-selector_l7_corp_fixed_caribbean" tabindex="180" title="Caribbean">Caribbean</a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/cl/es/welcome.html" name="us_en_home_country-selector_l8_corp_fixed_chile" tabindex="180" title="Chile">Chile</a>
            <br />
          </div>
          <div class="secondcolumn">
            <a class="link_metrics" href="http://welcome.hp.com/country/co/es/welcome.html" name="us_en_home_country-selector_l9_corp_fixed_colombia" tabindex="180" title="Colombia">Colombia</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ec/es/welcome.html" name="us_en_home_country-selector_l10_corp_fixed_ecuador" tabindex="180" title="Ecuador">Ecuador</a>

            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/mx/es/welcome.html" name="us_en_home_country-selector_l11_corp_fixed_mexico" tabindex="180" title="México">México</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/py/es/welcome.html" name="us_en_home_country-selector_l12_corp_fixed_paraguay" tabindex="180" title="Paraguay">Paraguay</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/pe/es/welcome.html" name="us_en_home_country-selector_l13_corp_fixed_peru" tabindex="180" title="Perú">Perú</a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/pr/es/welcome.html" name="us_en_home_country-selector_l14_corp_fixed_puerto-rico" tabindex="180" title="Puerto Rico">Puerto Rico</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/us/en/welcome.html" name="us_en_home_country-selector_l15_corp_fixed_united-states" tabindex="180" title="United States">United States</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/uy/es/welcome.html" name="us_en_home_country-selector_l16_corp_fixed_uruguay" tabindex="180" title="Uruguay">Uruguay</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ve/es/welcome.html" name="us_en_home_country-selector_l17_corp_fixed_venezuela" tabindex="180" title="Venezuela">Venezuela</a>

            <br />
          </div>
        </div>
        
        <div class="hidden" id="asia" title="Asia and Oceania">
          <div class="continentheader">Asia and Oceania</div>
          <div class="firstcolumn">
            <a class="link_metrics" href="http://welcome.hp.com/country/au/en/welcome.html" name="us_en_home_country-selector_l1_corp_fixed_australia" tabindex="180" title="Australia">Australia</a>
            <br />

            <a class="link_metrics" href="http://h20533.www2.hp.com/country/bd/en/welcome.html" name="us_en_home_country-selector_l2_corp_fixed_bangladesh" tabindex="180" title="Bangladesh">Bangladesh</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/cn/zh/welcome.html" name="us_en_home_country-selector_l3_corp_fixed_china" tabindex="180" title="中国">中国</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/hk/en/welcome.html" name="us_en_home_country-selector_l4_corp_fixed_hong-kong" tabindex="180" title="Hong Kong">Hong Kong</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/hk/zh/welcome.html" name="us_en_home_country-selector_l5_corp_fixed_hong-kong---trad-chinese" tabindex="180" title="香港 - 中文">香港 - 中文</a>

            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/in/en/welcome.html" name="us_en_home_country-selector_l6_corp_fixed_india" tabindex="180" title="India">India</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/id/en/welcome.html" name="us_en_home_country-selector_l7_corp_fixed_indonesia" tabindex="180" title="Indonesia">Indonesia</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/jp/ja/welcome.html" name="us_en_home_country-selector_l8_corp_fixed_japan" tabindex="180" title="日本">日本</a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/kr/ko/welcome.html" name="us_en_home_country-selector_l9_corp_fixed_korea" tabindex="180" title="한국">한국</a>
            <br />
          </div>
          <div class="secondcolumn">
            <a class="link_metrics" href="http://welcome.hp.com/country/my/en/welcome.html" name="us_en_home_country-selector_l10_corp_fixed_malaysia" tabindex="180" title="Malaysia">Malaysia</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/nz/en/welcome.html" name="us_en_home_country-selector_l11_corp_fixed_new-zealand" tabindex="180" title="New Zealand">New Zealand</a>

            <br />
            <a class="link_metrics" href="http://www.hp.com/pk" name="us_en_home_country-selector_l12_corp_fixed_pakistan" tabindex="180" title="Pakistan">Pakistan</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ph/en/welcome.html" name="us_en_home_country-selector_l13_corp_fixed_philippines" tabindex="180" title="Philippines">Philippines</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/sg/en/welcome.html" name="us_en_home_country-selector_l14_corp_fixed_singapore" tabindex="180" title="Singapore">Singapore</a>
            <br />

            <a class="link_metrics" href="http://h20533.www2.hp.com/country/lk/en/welcome.html" name="us_en_home_country-selector_l15_corp_fixed_sri-lanka" tabindex="180" title="Sri Lanka">Sri Lanka</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/tw/zh/welcome.html" name="us_en_home_country-selector_l16_corp_fixed_taiwan" tabindex="180" title="台灣">台灣</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/th/en/welcome.html" name="us_en_home_country-selector_l17_corp_fixed_thailand" tabindex="180" title="Thailand">Thailand</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/vn/en/welcome.html" name="us_en_home_country-selector_l18_corp_fixed_vietnam" tabindex="180" title="Vietnam">Vietnam</a>

            <br />
          </div>
        </div>
        <div class="hidden" id="europe" title="Europe, Middle East and Africa">
          <div class="continentheader">Europe, Middle East and Africa</div>
          <div class="firstcolumn">
            <a class="link_metrics" href="http://welcome.hp.com/country/emea_africa/en/welcome.html" name="us_en_home_country-selector_l1_corp_fixed_africa" tabindex="180" title="Africa">Africa</a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/emea_africa/fr/welcome.html" name="us_en_home_country-selector_l2_corp_fixed_africa---french" tabindex="180" title="Afrique">Afrique</a>
            <br />
            <a class="link_metrics" href="http://www.hp.com/al" name="us_en_home_country-selector_l3_corp_fixed_albania" tabindex="180" title="Albania">Albania</a>
            <br />
            <a class="link_metrics" href="http://www.hp.az" name="us_en_home_country-selector_l4_corp_fixed_azerbaijan" tabindex="180" title="Azərbaycan">Azərbaycan</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/by/ru/welcome.html" name="us_en_home_country-selector_l5_corp_fixed_belarus" tabindex="180" title="Беларусь">Беларусь</a>

            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/be/nl/welcome.html" name="us_en_home_country-selector_l6_corp_fixed_belgium---dutch" tabindex="180" title="België">België</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/be/fr/welcome.html" name="us_en_home_country-selector_l7_corp_fixed_belgium---french" tabindex="180" title="Belgique">Belgique</a>
            <br />
            <a class="link_metrics" href="http://www.hp.com/ba" name="us_en_home_country-selector_l8_corp_fixed_bosnia---herzegovina" tabindex="180" title="Bosna i Hercegovina">Bosna i Hercegovina</a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/bg/bg/welcome.html" name="us_en_home_country-selector_l9_corp_fixed_bulgaria" tabindex="180" title="България">България</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/cz/cs/welcome.html" name="us_en_home_country-selector_l10_corp_fixed_czech-republic" tabindex="180" title="Česká republika">Česká republika</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/rs/sr/welcome.html" name="us_en_home_country-selector_l11_corp_fixed_montenegro" tabindex="180" title="Crna Gora">Crna Gora</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/dk/da/welcome.html" name="us_en_home_country-selector_l12_corp_fixed_denmark" tabindex="180" title="Danmark">Danmark</a>

            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/de/de/welcome.html" name="us_en_home_country-selector_l13_corp_fixed_germany" tabindex="180" title="Deutschland">Deutschland</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ee/et/welcome.html" name="us_en_home_country-selector_l14_corp_fixed_estonia" tabindex="180" title="Eesti">Eesti</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/gr/el/welcome.html" name="us_en_home_country-selector_l15_corp_fixed_greece" tabindex="180" title="Ελλάδα">Ελλάδα</a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/es/es/welcome.html" name="us_en_home_country-selector_l16_corp_fixed_spain" tabindex="180" title="España">España</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/fr/fr/welcome.html" name="us_en_home_country-selector_l17_corp_fixed_france" tabindex="180" title="France">France</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/hr/hr/welcome.html" name="us_en_home_country-selector_l18_corp_fixed_croatia" tabindex="180" title="Hrvatska">Hrvatska</a>
            <br />
            <a class="link_metrics" href="http://www.hp.is" name="us_en_home_country-selector_l19_corp_fixed_iceland" tabindex="180" title="Iceland">Iceland</a>

            <br />
            <a class="link_metrics" href="http://www8.hp.com/ie/en/home.html" name="us_en_home_country-selector_l20_corp_fixed_ireland" tabindex="180" title="Ireland">Ireland</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/it/it/welcome.html" name="us_en_home_country-selector_l21_corp_fixed_italy" tabindex="180" title="Italia">Italia</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/kz/ru/welcome.html" name="us_en_home_country-selector_l22_corp_fixed_kazakhstan" tabindex="180" title="Казахстан">Казахстан</a>
            <br />

            <a class="link_metrics" href="http://www.hp.co.ke/" name="us_en_home_country-selector_l23_corp_fixed_kenya" tabindex="180" title="Kenya">Kenya</a>
            <br />
            <a class="link_metrics" href="http://h20158.www2.hp.com/gms/ks/sq/" name="us_en_home_country-selector_l24_corp_fixed_kosovo" tabindex="180" title="Kosovo">Kosovo</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/gr/el/welcome.html" name="us_en_home_country-selector_l25_corp_fixed_cyprus" tabindex="180" title="Κύπρος">Κύπρος</a>
            <br />
            <a class="link_metrics" href="http://h20158.www2.hp.com/gms/ge/ge/" name="us_en_home_country-selector_l26_corp_fixed_georgia" tabindex="180" title="საქართველო">საქართველო</a>

            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/il/he/welcome.html" name="us_en_home_country-selector_l27_corp_fixed_israel" tabindex="180" title=" ישראל"> ישראל</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/lv/lv/welcome.html" name="us_en_home_country-selector_l28_corp_fixed_latvia" tabindex="180" title="Latvija">Latvija</a>
            <br />
          </div>
          <div class="secondcolumn">

            <a class="link_metrics" href="http://welcome.hp.com/country/lt/lt/welcome.html" name="us_en_home_country-selector_l29_corp_fixed_lithuania" tabindex="180" title="Lietuva">Lietuva</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/be/fr/welcome.html" name="us_en_home_country-selector_l30_corp_fixed_luxembourg" tabindex="180" title="Luxembourg">Luxembourg</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/hu/hu/welcome.html" name="us_en_home_country-selector_l31_corp_fixed_hungary" tabindex="180" title="Magyarország">Magyarország</a>
            <br />
            <a class="link_metrics" href="http://www.hp.com/mk" name="us_en_home_country-selector_l32_corp_fixed_macedonia" tabindex="180" title="Македонија">Македонија</a>

            <br />
            <a class="link_metrics" href="http://www.hp.com/mt" name="us_en_home_country-selector_l33_corp_fixed_malta" tabindex="180" title="Malta">Malta</a>
            <br />
            <a class="link_metrics" href="http://www.hp.ma" name="us_en_home_country-selector_l34_corp_fixed_morocco" tabindex="180" title="Maroc">Maroc</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/emea_middle_east/en/welcome.html" name="us_en_home_country-selector_l35_corp_fixed_middle-east---english" tabindex="180" title="Middle East">Middle East</a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/emea_middle_east/ar/welcome.html" name="us_en_home_country-selector_l36_corp_fixed_middle-east---arabic" tabindex="180" title="الشرق الأوسط ">الشرق الأوسط </a>
            <br />
            <a class="link_metrics" href="http://www.hp.md" name="us_en_home_country-selector_l37_corp_fixed_moldova" tabindex="180" title="Moldova">Moldova</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/nl/nl/welcome.html" name="us_en_home_country-selector_l38_corp_fixed_netherlands" tabindex="180" title="Nederland">Nederland</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/no/no/welcome.html" name="us_en_home_country-selector_l39_corp_fixed_norway" tabindex="180" title="Norge">Norge</a>

            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/at/de/welcome.html" name="us_en_home_country-selector_l40_corp_fixed_austria" tabindex="180" title="Österreich">Österreich</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ru/ru/welcome.html" name="us_en_home_country-selector_l41_corp_fixed_russia" tabindex="180" title="Россия">Россия</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/pl/pl/welcome.html" name="us_en_home_country-selector_l42_corp_fixed_poland" tabindex="180" title="Polska">Polska</a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/pt/pt/welcome.html" name="us_en_home_country-selector_l43_corp_fixed_portugal" tabindex="180" title="Portugal">Portugal</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ro/ro/welcome.html" name="us_en_home_country-selector_l44_corp_fixed_romania" tabindex="180" title="România">România</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/emea_middle_east/en/welcome.html" name="us_en_home_country-selector_l45_corp_fixed_saudia-arabia" tabindex="180" title="Saudi Arabia">Saudi Arabia</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ch/de/welcome.html" name="us_en_home_country-selector_l46_corp_fixed_switzerland---german" tabindex="180" title="Schweiz">Schweiz</a>

            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/sk/sk/welcome.html" name="us_en_home_country-selector_l47_corp_fixed_slovakia" tabindex="180" title="Slovensko">Slovensko</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/si/sl/welcome.html" name="us_en_home_country-selector_l48_corp_fixed_slovenia" tabindex="180" title="Slovenija">Slovenija</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/za/en/welcome.html" name="us_en_home_country-selector_l49_corp_fixed_south-africa" tabindex="180" title="South Africa">South Africa</a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/rs/sr/welcome.html" name="us_en_home_country-selector_l50_corp_fixed_serbia" tabindex="180" title="Srbija">Srbija</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ch/fr/welcome.html" name="us_en_home_country-selector_l51_corp_fixed_switzerland---french" tabindex="180" title="Suisse">Suisse</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/fi/fi/welcome.html" name="us_en_home_country-selector_l52_corp_fixed_finland" tabindex="180" title="Suomi">Suomi</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/se/sv/welcome.html" name="us_en_home_country-selector_l53_corp_fixed_sweden" tabindex="180" title="Sverige">Sverige</a>

            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/tr/tr/welcome.html" name="us_en_home_country-selector_l54_corp_fixed_turkey" tabindex="180" title="Türkiye">Türkiye</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/ua/ru/welcome.html" name="us_en_home_country-selector_l55_corp_fixed_ukraine" tabindex="180" title="Украина">Украина</a>
            <br />
            <a class="link_metrics" href="http://welcome.hp.com/country/emea_middle_east/ar/welcome.html" name="us_en_home_country-selector_l56_corp_fixed_united-arab-emirates" tabindex="180" title="دولة الإمارات العربية المتحدة ">دولة الإمارات العربية المتحدة </a>
            <br />

            <a class="link_metrics" href="http://welcome.hp.com/country/uk/en/welcome.html" name="us_en_home_country-selector_l57_corp_fixed_united-kingdom" tabindex="180" title="United Kingdom">United Kingdom</a>
            <br />
          </div>
        </div>
       
        <!-- END  COUNTRY SELECTOR -->
      </div>
      <!-- END FOOTER AREA -->
	

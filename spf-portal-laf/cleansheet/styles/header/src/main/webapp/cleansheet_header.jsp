<%-----------------------------------------------------------------------------
	cleansheet_header.jsp
	
	STYLE: Cleansheet Header - Only stretch logo, enhanced layout is supported.
	STYLE TYPE: Header
	USES HEADER FILE: Yes

	The header displays itself in two parts.   This is done to allow  the 
	horizontal navigation to display between the upper portion 
	(hp-wide global buttons) and the lower portion (title and breadcrumbs).
	
	'cleansheet_header_split' request attribute set here to indicate to the
	grid component and here that the lower portion remains to be displayed.
	
-----------------------------------------------------------------------------%>


<%----------------------------------------------------------------------------- 
	Imports
-----------------------------------------------------------------------------%>

<jsp:directive.page import="javax.servlet.http.HttpServletRequest" />
<jsp:directive.page import="javax.servlet.http.HttpSession" />

<jsp:directive.page import="com.vignette.portal.website.enduser.PortalContext" />
<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="java.util.ArrayList" />
<jsp:directive.page import="java.util.Iterator" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemUtils" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemNode" />
<jsp:directive.page import="com.hp.frameworks.wpa.hpweb.MenuItem" />
<jsp:directive.page import="com.hp.frameworks.wpa.portal.hpweb.Utils" />


<%----------------------------------------------------------------------------- 
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cscore" uri="http://frameworks.hp.com/wpa/cleansheet-core" %>

<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />
		
<div class="header" id="header">
				<div class="left" >
					<div class="pad">
						<a title="HP.com home" class="logo png link_metrics" href="http://www.hp.com" name="ie_en_home__header_corp_fixed_homepage" tabindex="1">
							<span class="hidden">HP.com </span>
						</a>
						<ul class="main_nav">
							<li>
								<a title="Products and Services" class="hnl_l1_p1 hand" id="hnl_l1_p1_trigger" tabindex="2">Products &amp;<br/> Services</a>
							</li>
							<li>
								<a title="Explore and Create" class="hnl_l1_p2 hand" id="hnl_l1_p2_trigger" tabindex="3">Explore &amp;<br/> Create</a>
							</li>
						</ul>						
						<div class="hnl_l2_p1_menu" id="hnl_l2_p1_inner">
							<div id="hnl_l2_p1_accordion">
								<ul>
									<li>
										<a title="Home and Home Office" class="hnl_l2_p1_toggler green_arrow hand" tabindex="2">Home &amp; Home Office</a>
										<div class="hnl_l2_p1_link" id="hnl_hho">
											<ul>
												<li>
													<a title="Laptops and Tablet PCs" class="hho link_metrics" href="http://welcome.hp.com/country/ie/en/hho/notebooks_tabletpcs.html" name="ie_en_home_hho-product_l1_hho_fixed_laptops---tablet-pcs" tabindex="2">Laptops - Tablet PCs</a>
												</li>
												<li>
													<a title="Desktops" class="hho link_metrics" href="http://welcome.hp.com/country/ie/en/hho/desktops.html" name="ie_en_home_hho-product_l2_hho_fixed_desktops---all-in-one-pcs" tabindex="2">Desktops - All-in-one PCs</a>
												</li>
												<li>
													<a title="Monitors" class="hho link_metrics" href="http://h10010.www1.hp.com/wwpc/ie/en/ho/WF02a/382087-382087-3284302.html" name="ie_en_home_hho-product_l3_hho_fixed_monitors" tabindex="2">Monitors</a>
												</li>
												<li>
													<a title="Accessories" class="hho link_metrics" href="http://h41112.www4.hp.com/promo/attachfinder/ie/en/?jumpid=ex_r10104_ie/en/smb/psg/smartfinder-mu-xx-aw-/chev/20091030" name="ie_en_home_hho-product_l4_hho_fixed_accessories---parts" tabindex="2">Accessories </a>
												</li>
												<li>
													<a title="Printers" class="hho link_metrics" href="http://welcome.hp.com/country/ie/en/hho/printing_multifunction.html" name="ie_en_home_hho-product_l5_hho_fixed_printers---all-in-ones" tabindex="2">Printers - All-in-ones</a>
												</li>
												<li>
													<a title="Ink Toner and Paper" class="hho link_metrics" href="http://welcome.hp.com/country/ie/en/laser-toner-ink-cartridges.html" name="ie_en_home_hho-product_l6_hho_fixed_ink---toner---paper" tabindex="2">Ink - Toner - Paper</a>
												</li>
												<li>
													<a title="Handhelds and calculators" class="hho link_metrics" href="http://welcome.hp.com/country/ie/en/hho/handheld.html" name="ie_en_home_hho-product_l7_hho_fixed_handhelds---calculators" tabindex="2">Handhelds - Calculators</a>
												</li>
												<li>
													<a title="Scanners" class="hho link_metrics" href="http://www8.hp.com/ie/en/products/scanners/index.html" name="ie_en_home_hho-product_l8_hho_fixed_scanners---fax" tabindex="2">Scanners</a>
												</li>
												<li>
													<a title="Consumer Products" class="hho link_metrics" href="http://h41257.www4.hp.com/cda/hho/display/main/hho_home.jsp?zn=hho&amp;cp=1247_4033_4" name="ie_en_home_hho-product_l9_hho_fixed_all-consumer-products" tabindex="2">All Consumer Products</a>
												</li>
												<li>
													<a title="Photo Printing Services" class="hho link_metrics" href="http://www.snapfish.ie/hpcomhomepage_ie" name="ie_en_home_hho-product_l10_hho_fixed_photo-printing-services" tabindex="2">Photo Printing Services</a>
												</li>
												<li>
													<a title="Latest Product Offers" class="hho link_metrics" href="http://h41112.www4.hp.com/price_cat_rss/index.php?cc=ie&amp;ll=en&amp;segment=hho" name="ie_en_home_hho-product_l11_hho_fixed_latest-product-offers" tabindex="2">Latest Product Offers</a>
												</li>
											</ul>
										</div>
									</li>
									<li>
										<a title="Small and Medium Business" class="hnl_l2_p1_toggler green_arrow hand" tabindex="2">Small &amp; Medium Business</a>
										<div class="hnl_l2_p1_link gone" id="hnl_smb">
											<ul>
												<li>
													<a title="Laptops and Tablet PCs" class="smb link_metrics" href="http://h41111.www4.hp.com/notebooks/ie/en/" name="ie_en_home_smb-product_l1_smb_fixed_laptops---tablet-pcs" tabindex="2">Laptops - Tablet PCs</a>
												</li>
												<li>
													<a title="Desktops and Workstatios" class="smb link_metrics" href="http://welcome.hp.com/country/ie/en/smb/desktops.html" name="ie_en_home_smb-product_l2_smb_fixed_desktops---workstations" tabindex="2">Desktops - Workstations</a>
												</li>
												<li>
													<a title="Accessories" class="smb link_metrics" href="http://h41112.www4.hp.com/promo/attachfinder/ie/en/?jumpid=ex_r10104_ie/en/smb/psg/smartfinder-mu-xx-aw-/chev/20091030" name="ie_en_home_smb-product_l3_smb_fixed_accessories---parts" tabindex="2">Accessories </a>
												</li>
												<li>
													<a title="Printers Multifunction" class="smb link_metrics" href="http://welcome.hp.com/country/ie/en/smb/printing_multifunction.html" name="ie_en_home_smb-product_l4_smb_fixed_printers---multifunction" tabindex="2">Printers - Multifunction</a>
												</li>
												<li>
													<a title="Ink Toner and Paper" class="smb link_metrics" href="http://welcome.hp.com/country/ie/en/laser-toner-ink-cartridges.html" name="ie_en_home_smb-product_l5_smb_fixed_ink---toner---paper" tabindex="2">Ink - Toner - Paper</a>
												</li>
												<li>
													<a title="Servers and Blades" class="smb link_metrics" href="http://welcome.hp.com/country/ie/en/smb/servers.html" name="ie_en_home_smb-product_l6_smb_fixed_servers---blades" tabindex="2">Servers - Blades</a>
												</li>
												<li>
													<a title="Storage" class="smb link_metrics" href="http://welcome.hp.com/country/ie/en/smb/storage.html" name="ie_en_home_smb-product_l7_smb_fixed_storage" tabindex="2">Storage</a>
												</li>
												<li>
													<a title="Networking" class="smb link_metrics" href="http://h40060.www4.hp.com/procurve/ie/en/sbn/index.html" name="ie_en_home_smb-product_l8_smb_fixed_networking" tabindex="2">Networking</a>
												</li>
												<li>
													<a title="Services" class="smb link_metrics" href="http://h20219.www2.hp.com/services/w1/en/warranty/carepack-overview.html" name="ie_en_home_smb-product_l9_smb_fixed_services" tabindex="2">Services</a>
												</li>
												<li>
													<a title="Handhelds and Calculators" class="smb link_metrics" href="http://h41111.www4.hp.com/handhelds/ie/en/" name="ie_en_home_smb-product_l10_smb_fixed_handhelds---calculators" tabindex="2">Handhelds - Calculators</a>
												</li>
												<li>
													<a title="Scanners" class="smb link_metrics" href="http://www8.hp.com/ie/en/products/scanners/index.html" name="ie_en_home_smb-product_l11_smb_fixed_scanners---fax" tabindex="2">Scanners</a>
												</li>
												<li>
													<a title="Small and Medium Business" class="smb link_metrics" href="http://h41320.www4.hp.com/cda/hpsmb/display/main/smb_home.jsp?zn=hpsmb&amp;cp=1_4000_4" name="ie_en_home_smb-product_l12_smb_fixed_all-small---medium-business-products" tabindex="2">All Small - Medium Business Products</a>
												</li>
												<li>
													<a title="Latest Product Offers" class="smb link_metrics" href="http://h41112.www4.hp.com/price_cat_rss/index.php?cc=ie&amp;ll=en&amp;segment=smb" name="ie_en_home_smb-product_l13_smb_fixed_latest-product-offers" tabindex="2">Latest Product Offers</a>
												</li>
												<li>
													<a title="Point of Sale" class="smb link_metrics" href="http://h41112.www4.hp.com/pos/ie/en/index.html" name="ie_en_home_smb-product_l14_smb_fixed_point-of-sale-solutions" tabindex="2">Point of Sale</a>
												</li>
											</ul>
										</div>
									</li>
									<li>
										<a title="Large Enterprise Business" class="hnl_l2_p1_toggler green_arrow hand" tabindex="2">Large Enterprise Business</a>
										<div class="hnl_l2_p1_link gone" id="hnl_leb">
											<ul>
												<li>
													<a title="Solutions" class="leb link_metrics" href="http://h71028.www7.hp.com/enterprise/w1/en/solutions/large-enterprise-business-solutions.html" name="ie_en_home_leb-product_l1_leb_fixed_solutions" tabindex="2">Solutions</a>
												</li>
												<li>
													<a title="Services" class="leb link_metrics" href="http://h20219.www2.hp.com/services/w1/en/business-it-services.html" name="ie_en_home_leb-product_l2_leb_fixed_hp-services" tabindex="2">Services</a>
												</li>
												<li>
													<a title="Servers" class="leb link_metrics" href="http://h71028.www7.hp.com/enterprise/cache/418226-0-0-105-121.html" name="ie_en_home_leb-product_l3_leb_fixed_servers" tabindex="2">Servers</a>
												</li>
												<li>
													<a title="Blades" class="leb link_metrics" href="http://h71028.www7.hp.com/enterprise/cache/80316-0-0-105-121.html" name="ie_en_home_leb-product_l4_leb_fixed_blades" tabindex="2">Blades</a>
												</li>
												<li>
													<a title="Storage" class="leb link_metrics" href="http://h71028.www7.hp.com/enterprise/cache/418227-0-0-105-121.html" name="ie_en_home_leb-product_l5_leb_fixed_storage" tabindex="2">Storage</a>
												</li>
												<li>
													<a title="Networking Solutions" class="leb link_metrics" href="http://h40060.www4.hp.com/procurve/ie/en/index.html" name="ie_en_home_leb-product_l6_leb_fixed_networking-solutions" tabindex="2">Networking Solutions</a>
												</li>
												<li>
													<a title="Software" class="leb link_metrics" href="http://welcome.hp.com/country/ie/en/leb/software/software.html" name="ie_en_home_leb-product_l7_leb_fixed_software" tabindex="2">Software</a>
												</li>
												<li>
													<a title="Printing Multifunction" class="leb link_metrics" href="http://h41320.www4.hp.com/cda/mwec/display/main/ipge_home.jsp?zn=hpsmb&amp;cp=5659-5660_4100_4" name="ie_en_home_leb-product_l8_leb_fixed_printing---multifunction" tabindex="2">Printing - Multifunction</a>
												</li>
												<li>
													<a title="Ink Toner and Paper" class="leb link_metrics" href="http://welcome.hp.com/country/ie/en/laser-toner-ink-cartridges.html" name="ie_en_home_leb-product_l9_leb_fixed_ink---toner---paper" tabindex="2">Ink - Toner - Paper</a>
												</li>
												<li>
													<a title="Laptops and Tablet PCs" class="leb link_metrics" href="http://welcome.hp.com/country/ie/en/leb/notebooks_tabletpcs.html" name="ie_en_home_leb-product_l10_leb_fixed_laptops---tablet-pcs" tabindex="2">Laptops - Tablet PCs</a>
												</li>
												<li>
													<a title="Desktops and Workstations" class="leb link_metrics" href="http://welcome.hp.com/country/ie/en/leb/desktops.html" name="ie_en_home_leb-product_l11_leb_fixed_desktops---workstations" tabindex="2">Desktops - Workstations</a>
												</li>
												<li>
													<a title="Video Collaboration" class="leb link_metrics" href="http://www.hp.com/halo/index.html" name="ie_en_home_leb-product_l12_leb_fixed_video-collaboration" tabindex="2">Video Collaboration</a>
												</li>
												<li>
													<a class="leb link_metrics" href="http://h71028.www7.hp.com/enterprise/w1/en/leb-index.html" name="ie_en_home_leb-product_l13_leb_fixed_all-large-enterprise-products---services" tabindex="2" title="Large Enterprise">All Large Enterprise Products - Services</a>
												</li>
											</ul>
										</div>
									</li>
									<li>
										<a title="Government, Health and Education" class="hnl_l2_p1_toggler green_arrow hand" tabindex="2" >Government, Health &amp; Education</a>
										<div class="hnl_l2_p1_link gone" id="hnl_ghe">
											<ul>
												<li>
													<a title="Education" class="ghe link_metrics" href="http://h20247.www2.hp.com/PublicSector/cache/83505-0-0-105-121.html" name="ie_en_home_ghe-product_l1_ghe_fixed_education" tabindex="2">Education</a>
												</li>
												<li>
													<a title="Health" class="ghe link_metrics" href="http://h20247.www2.hp.com/PublicSector/cache/83502-0-0-105-121.html" name="ie_en_home_ghe-product_l2_ghe_fixed_health---life-sciences" tabindex="2">Health - Life Sciences</a>
												</li>
												<li>
													<a title="Public Sector" class="ghe link_metrics" href="http://h20247.www2.hp.com/PublicSector/cache/83374-0-0-105-121.html" name="ie_en_home_ghe-product_l3_ghe_fixed_public-sector" tabindex="2">Public Sector</a>
												</li>
												<li>
													<a title="Public Sector, Health and Education" class="ghe link_metrics" href="http://h20247.www2.hp.com/publicsector/cache/424075-0-0-105-121.html" name="ie_en_home_ghe-product_l4_ghe_fixed_all-public-sector---health---education" tabindex="2">All Public Sector - Health - Education</a>
												</li>
											</ul>
										</div>
									</li>
									<li>
										<a title="Graphic Arts" class="hnl_l2_p1_toggler green_arrow hand" tabindex="2">Graphic Arts</a>
										<div class="hnl_l2_p1_link gone" id="hnl_ga">
											<ul>
												<li>
													<a title="Indigo Digital Presses" class="ga link_metrics" href="http://h10088.www1.hp.com/cda/gap/display/main/gap_home.jsp?zn=gap&amp;cp=1-247-251_4011_4" name="ie_en_home_ga-product_l1_ga_fixed_indigo-digital-presses" tabindex="2">Indigo Digital Presses</a>
												</li>
												<li>
													<a title="Scitex Industrial Printers" class="ga link_metrics" href="http://h10088.www1.hp.com/cda/gap/display/main/gap_home.jsp?zn=gap&amp;cp=1-247-269_4011_4" name="ie_en_home_ga-product_l2_ga_fixed_scitex-industrial-printers" tabindex="2">Scitex Industrial Printers</a>
												</li>
												<li>
													<a title="Designjet Printers" class="ga link_metrics" href="http://h10088.www1.hp.com/cda/gap/display/main/gap_home.jsp?zn=gap&amp;cp=1-247-261_4011_4" name="ie_en_home_ga-product_l3_ga_fixed_designjet-printers" tabindex="2">Designjet Printers</a>
												</li>
												<li>
													<a title="Specialty Printing Systems" class="ga link_metrics" href="http://h10088.www1.hp.com/cda/gap/display/main/gap_home.jsp?zn=gap&amp;cp=1-247-260_4011_4" name="ie_en_home_ga-product_l4_ga_fixed_specialty-printing-systems" tabindex="2">Specialty Printing Systems</a>
												</li>
												<li>
													<a title="Ink Supplies" class="ga link_metrics" href="http://h10088.www1.hp.com/cda/gap/display/main/gap_home.jsp?zn=gap&amp;cp=1-254_4011_4" name="ie_en_home_ga-product_l5_ga_fixed_ink---supplies" tabindex="2">Ink - Supplies</a>
												</li>
												<li>
													<a title="All Graphic Arts Products" class="ga link_metrics" href="http://h10088.www1.hp.com/cda/gap/display/main/gap_home.jsp?zn=gap&amp;cp=1_4011_4__&amp;vertical=ga-gw" name="ie_en_home_ga-product_l6_ga_fixed_all-graphic-arts-products" tabindex="2">All Graphic Arts Products</a>
												</li>
											</ul>
										</div>
									</li>
									<li>
									   <a title="Support &amp; Drivers" class="green_arrow link_metrics" href="http://welcome.hp.com/country/ie/en/support.html" tabindex="2">Support &amp; Drivers</a>
									</li>
								</ul>
							</div>
							<a title="Closing popup" class="lastitemMenu" tabindex="2"></a>
						</div>
						<div class="hnl_l2_p2_menu" id="hnl_l2_p2_inner">
							<div id="hnl_l2_p2_accordion">
								<ul>
									<li>
										<a title="Home" class="hnl_l2_p2_toggler green_arrow hand" tabindex="3" >At Home</a>
										<div class="hnl_l2_p2_link" id="hnl_ath">
					                      <ul>
					
					                        <li>
					                          <a class="hho link_metrics" href="http://www.hp.com/hho/hp_create" name="us_en_home_hho-at-home_l1_hho_fixed_free-printing-projects" tabindex="3" title="Free printing projects">Free printing projects</a>
					                        </li>
					                        <li>
					                          <a class="hho link_metrics" href="http://www.hp.com/united-states/consumer/everyday-computing/index.html" name="us_en_home_hho-at-home_l2_hho_fixed_pc-ideas,-guides-and-how-tos" tabindex="3" title="PC ideas, guides and how-to's">PC ideas, guides and how-to's</a>
					                        </li>
					                        <li>
					                          <a class="hho link_metrics" href="http://www1.snapfish.com/welcomenpnu" name="us_en_home_hho-at-home_l3_hho_fixed_share-print-photos-online" tabindex="3" title="Share &amp; print photos online">Share &amp; print photos online</a>
					
					                        </li>
					                        <li>
					                          <a class="hho link_metrics" href="http://h71036.www7.hp.com/hho/cache/252121-0-0-225-121.html" name="us_en_home_hho-at-home_l4_hho_fixed_home-office-printing-tips" tabindex="3" title="Home office printing tips">Home office printing tips</a>
					                        </li>
					                        <li>
					                          <a class="hho link_metrics" href="http://www.hp.com/united-states/consumer/digital_photography/home.html" name="us_en_home_hho-at-home_l5_hho_fixed_photo-printing-tips-ideas" tabindex="3" title="Photo printing tips &amp; ideas">Photo printing tips &amp; ideas</a>
					                        </li>
					
					                        <li>
					                          <a class="hho link_metrics" href="http://h30187.www3.hp.com/?tab=atHome&amp;mcid=hho" name="us_en_home_hho-at-home_l6_hho_fixed_free-online-classes" tabindex="3" title="Free online classes">Free online classes</a>
					                        </li>
					                        <li>
					                          <a class="hho link_metrics" href="http://www.hp.com/global/us/en/consumer/digital_photography/free/software/printapp_index.html" name="us_en_home_hho-at-home_l7_hho_fixed_free-printing-software" tabindex="3" title="Free printing software">Free printing software</a>
					                        </li>
					                        <li>
					                          <a class="hho link_metrics" href="http://h71036.www7.hp.com/hho/cache/597818-0-0-225-121.html" name="us_en_home_hho-at-home_l8_hho_fixed_exclusive-extras-for-your-pc" tabindex="3" title="Exclusive extras for your PC">Exclusive extras for your PC</a>
					
					                        </li>
					                      </ul>
										</div>
									</li>
									<li>
										<a title="Work" class="hnl_l2_p2_toggler green_arrow hand" tabindex="3">At Work</a>
										<div class="hnl_l2_p2_link gone" id="hnl_atw">
											<ul>
												<li>
													<a title="Questions and answers" class="smb link_metrics" href="http://h41112.www4.hp.com/promo/obc/ie/en/" name="ie_en_home_smb-at-work_l1_smb_fixed_busans_psg" tabindex="3">Got questions? We have answers</a>
												</li>
												<li>
													<a title="HP Laptop Expertise Center" class="smb link_metrics" href="http://h41111.www4.hp.com/notebooks/ie/en/index.html" name="ie_en_home_smb-at-work_l2_smb_fixed_laptops-expertise-centre" tabindex="3">HP Laptops Expertise Center</a>
												</li>
												<li>
													<a title="HP Desktop Expertise Center" class="smb link_metrics" href="http://h41111.www4.hp.com/new_desktops/ie/en/index.html" name="ie_en_home_smb-at-work_l3_smb_fixed_desktop-expertise-centre" tabindex="3">HP Desktop Expertise Center</a>
												</li>
												<li>
													<a title="HP Workstations Expertise Center" class="smb link_metrics" href="http://h41111.www4.hp.com/new_workstations/ie/en/index.html" name="ie_en_home_smb-at-work_l4_smb_fixed_workstations-expertise-centre" tabindex="3">HP Workstations Expertise Center</a>
												</li>
												<li>
													<a title="HP Point of Sale Solutions" class="smb link_metrics" href="http://h41112.www4.hp.com/pos/ie/en/index.html" name="ie_en_home_smb-at-work_l5_smb_fixed_point-of-sale-solutions" tabindex="3">HP Point of Sale (POS) Solutions</a>
												</li>
												<li>
													<a title="HP Monitor Expertise Center" class="smb link_metrics" href="http://h41112.www4.hp.com/monitors/ie/en/index.html" name="ie_en_home_smb-at-work_l6_smb_fixed_monitors-expertise-centre" tabindex="3">HP Monitor Expertise Center</a>
												</li>
												<li>
													<a title="HP Handhelds Expertise Center" class="smb link_metrics" href="http://h41111.www4.hp.com/handhelds/ie/en/use.html" name="ie_en_home_smb-at-work_l7_smb_fixed_handhelds-expertise-centre" tabindex="3">HP Handhelds Expertise Center</a>
												</li>
												<li>
													<a title="HP Thin Client Home" class="smb link_metrics" href="http://h41320.www4.hp.com/cda/mwec/display/main/hpthinclients_content.jsp?zn=hpsmb&amp;cp=4661-4662-4663_4088_4__" name="ie_en_home_smb-at-work_l8_smb_fixed_thin-clients-home" tabindex="3">HP Thin Client Home</a>
												</li>
												<li>
													<a title="Total Care" class="smb link_metrics" href="http://h41320.www4.hp.com/cda/smbtc/display/main/smbtc_content.jsp?zn=hpsmb&amp;cp=1231-1232_4028_4__" name="ie_en_home_smb-at-work_l9_smb_fixed_total-care" tabindex="3">Total Care</a>
												</li>
												<li>
													<a title="Free online classes" class="smb link_metrics" href="http://h41348.www4.hp.com/?cc=ie&amp;mcid=explore" name="ie_en_home_smb-at-work_l10_smb_fixed_free-online-classes" tabindex="3">Free online classes</a>
												</li>
												<li>
													<a title="Printing Imaging expertise centre" class="smb link_metrics" href="http://h41320.www4.hp.com/cda/piec/display/main/hppiec_content.jsp?zn=hpsmb&amp;cp=1347-1348_4038_4__" name="ie_en_home_smb-at-work_l11_smb_fixed_printing---imaging-expertise-centre" tabindex="3">Printing - Imaging Expertise Centre</a>
												</li>
												<li>
													<a title="Server Storage Expertise Centre" class="smb link_metrics" href="http://h20384.www2.hp.com/serverstorage/w1/en/solutions/midsize-business-center.html"  name="ie_en_home_smb-at-work_l12_smb_fixed_server---storage-expertise-centre" tabindex="3">Server - Storage Expertise Centre</a>
												</li>
											</ul>
										</div>
									</li>
								</ul>
							</div>
					        <a title="Closing popup" class="lastitemMenu" tabindex="3"></a>
					     </div>
				    </div>				
				</div>
				<div class="right">
					<div class="nav_txt">
						<a title="Support and Drivers" class="support_txt link_metrics" href="http://welcome.hp.com/country/ie/en/support.html" name="ie_en_home_l1_header_corp_fixed_support &amp; drivers" tabindex="10">SUPPORT &amp; DRIVERS</a>
						<span class="pipe_chr"> </span>
					</div>					
					<div class="search">
						<form action="http://www.hp.com/search/" method="get" class="zeroMargin" onsubmit="try{trackMetrics('linkClick',{type:'link', id:'ie_en_home__header_corp_fixed_search'});} catch(err) {};return validateSearch();" name="searchHP" id="searchHP">
							<input type="hidden" name="cc" value="ie"/>
							<input type="hidden" name="lang" value="en"/>
							<input type="hidden" name="charset" value="utf-8"/>
							<input type="hidden" name="qp" value="url:h41131.www4.hp.com/ie/en url:NEW URL GOES HERE"/>
							<input type="hidden" name="hpn" value="Newsroom home"/>
							<input type="hidden" name="hpa" value="http://www.hp.com/country/ie/en/contact_us.html"/>
							<input type="hidden" name="hps" value="Newsroom home"/>
							<input type="hidden" name="hpr" value="http://h41131.www4.hp.com/ie/en/"/>
							<div class="input">
								<input id="searchBox" value="SEARCH HP.COM" title="Enter search criteria here to find content on HP.com" type="text" name="qt" size="30" maxlength="100" onfocus="clearSearch(this,searchTxt);" onblur="restoreSearch(this);" tabindex="11"/>
								<input class="searchSubmit" type="submit" name="search" value="" title="Begin your search" tabindex="11"/>
							</div>
						</form>
					</div>
					<div class="pad">
						<ul class="nav_buttons js_header_buttons">
							<li>
								<a class="connect hand" id="connect_trigger" tabindex="21" title="Connect with HP">
									<span class="hidden">Connect with HP</span>
								</a>
							</li>
							<li>
								<a class="community"  id="community_trigger" onclick="return false;" tabindex="22" title="Online Communities">
									<span class="hidden">Online Communities</span>
									<span class="hidden">Opens simulated dialog</span>
								</a>
							</li>
						</ul>
					</div>
					<div id="connectContainerInnerHP">
						<div id="connect_text">Connect with HP</div>
						<div id="connectInformationHP">
							<div id="header_rightnav_accordion">
								<div class="lefty">
									<div class="linksWidth">
										<div class="connectImage">
											<img alt="Email us" height="12" src="http://www8.hp.com/us/en/images/email_us_tcm245-167814.png" width="17"/>
										</div>
										<div class="lefty">
											<a title="Email us" onclick="return false;" href="javascript:void(0);" class="togglerrn hand" tabindex="21">Email us </a>
										</div>
									</div>
									<div class="connect_sub_links">
										<div class="elementrn">
											<ul>
												<li>
													<a title="Questions before you buy" href="http://welcome.hp.com/country/ie/en/contact/email_2.html" id="email-questions" name="ie_en_home_header_l2_corp_fixed_email-questions" tabindex="21" class="link_metrics">Questions before you buy</a>
												</li>
												<li>
													<a title="Technical support after you buy" href="http://h20180.www2.hp.com/apps/Nav?h_pagetype=s-005&amp;h_lang=en&amp;h_cc=ie&amp;h_product=top&amp;h_client=S-A-R861-2" id="email-tech-support" name="ie_en_home_header_l3_corp_fixed_email-tech-support" tabindex="21" class="link_metrics">Technical support after you buy</a>
												</li>
												<li>
													<a title="Other questions and feedback" href="http://welcome.hp.com/country/ie/en/contact/email_3.html" id="email-other-questions" name="ie_en_home_header_l4_corp_fixed_email-other-questions" tabindex="21" class="link_metrics">Other questions / feedback</a>
												</li>
												<li>
													<a title="Feedback to webmaster" href="http://h41111.www4.hp.com/contacthp/webmaster/ie/en/feedback.php?refurl=?refurl=" id="email-feedback" name="ie_en_home_header_l5_corp_fixed_email-feedback" tabindex="21" class="link_metrics">Feedback to Webmaster</a>
												</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="connect_border"> </div>
								<div class="lefty">
									<div class="linksWidth">
										<div class="connectImage">
											<img alt="Call us" height="20" src="http://www8.hp.com/us/en/images/call_us_tcm245-167809.png" width="15"/>
										</div>
										<div class="lefty">
											<a title="Call us" onclick="return false;" href="javascript:void(0);" class="togglerrn hand" tabindex="21">Call us </a>
										</div>
									</div>
									<div class="connect_sub_links">
										<div class="elementrn">
											<ul>
												<li>
													<a title="Questions before you buy" href="http://welcome.hp.com/country/ie/en/contact/phone_assist2.html" id="call-us-questions" name="ie_en_home_header_l6_corp_fixed_call-us-questions" tabindex="21" class="link_metrics">Questions before you buy</a>
												</li>
												<li>
													<a title="Technical support after you buy" href="http://welcome.hp.com/country/ie/en/contact/phone_assist.html" id="call-us-tech-support" name="ie_en_home_header_l7_corp_fixed_call-us-tech-support" tabindex="21" class="link_metrics">Technical support after you buy</a>
												</li>
												<li>
													<a title="Other questions and feedback" href="http://welcome.hp.com/country/ie/en/contact/phone_assist3.html" id="call-us-other-questions" name="ie_en_home_header_l8_corp_fixed_call-us-other-questions" tabindex="21" class="link_metrics">Other questions / feedback</a>
												</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<div class="connect_border"> </div>
							<div>
								<div class="connectImage">
									<img alt="HP office locations" height="18" src="http://www8.hp.com/us/en/images/office_location_tcm245-167819.png" width="17"/>
								</div>
								<div class="lefty">
									<a title="HP office locations" href="http://welcome.hp.com/country/ie/en/contact/office_locs.html" id="hp-office-locations" name="ie_en_home_header_l9_corp_fixed_hp-office-locations" tabindex="21" class="link_metrics">HP office locations</a>
								</div>
							</div>
							<div class="connect_border"> </div>
							<div>
								<div class="leftyContact">
									<a title="All HP contacts" href="/country/ie/en/cs/contact-hp/contact.html" id="lastitemNav" name="ie_en_home_header_l10_corp_fixed_all-hp-contacts" tabindex="21" class="link_metrics">All HP Contacts</a>
								</div>
							</div>
						</div>
						<a title="Closing popup" class="lastitemMenu" tabindex="21"></a>
					</div>
				</div>
			</div>	

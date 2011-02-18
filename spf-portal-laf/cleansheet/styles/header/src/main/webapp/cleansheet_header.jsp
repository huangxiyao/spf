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
								<a class="community png" href="javascript:void(0);" id="community_trigger" onclick="return false;" tabindex="22" title="Online Communities">
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

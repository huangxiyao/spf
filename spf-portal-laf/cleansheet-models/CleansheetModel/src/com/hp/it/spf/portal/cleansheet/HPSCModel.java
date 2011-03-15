package com.hp.it.spf.portal.cleansheet;

import java.util.List;
import java.util.SortedSet;

/**
 * Site Help is a link that appears on the right portion of the menu bar.  If this value is populated it will appear on the menu bar, if the URL or linkText is null it will not appear.  The link should take the user to a page that offers help related to the current site.
 */

public class HPSCModel {

	/**
	 * The fully-qualified URL to the User's profile page.  This URL will be used in conjunction with the &quot;Edit your profile&quot; hyperlink rendered at the top of the page.  The &quot;Edit your profile&quot; hyperlink is only rendered when the &quot;signInUrl&quot; and &quot;username&quot; properties both contain non-null values. <p>Note: For HPP users this will link to the HPP edit profile page, for Enterprise Directory users this should link to DirectoryWorks.</p>
	 */

	private String profileURL;

	/**
	 * The fully-qualified URL to the HP Passport registration page.  This URL will be used in conjunction with the &quot;Register&quot; hyperlink rendered at the top of the page.  The &quot;Register&quot; hyperlink is only rendered when the &quot;signInUrl&quot; property contains a non-null value and &quot;username&quot; property is empty (indicating that the user is not logged-in). <p>Note: This is only relevant for HPP users, this link will not be shown for Enterprise Directory users.</p>
	 */

	private String HPPRegistrationURL;

	/**
	 * <p>The fully-qualified URL to the authentication provider login page.  This URL will be used in conjunction with the &quot;Sign-in&quot; link rendered at the top of the page.  The value for this property is also used to indicate whether or not the page should render any of the other authentication related links.  See profileURL, HPPRegistrationURL and signOutURL for more details.</p> <p></p>
	 */

	private String signInURL;

	/**
	 * <p>The fully-qualified URL to the sign-out page for the user.  This URL will be used in conjunction with the &quot;Sign-out&quot; hyperlink rendered at the top of the page.  The &quot;Sign-out&quot; hyperlink is only rendered when the &quot;signInUrl&quot; and &quot;username&quot; properties both contain non-null values.</p>
	 */

	private String signOutURL;

	/**
	 * <p>If specified, the username will be rendered as part of the welcome message at the top left of the page.  A non-empty value for the username property indicates that the user is logged-in and will determine whether the sign-in/register links are displayed or whether the sign-out/profile links are displayed.</p>
	 */

	private String username;

	/**
	 * <p>The value of this property will be used as the URL to the Omniture metrics collection script that will be inserted at the bottom of the page.  If no value is supplied for this property, the URL to the default Omniture metrics collection script for the current locale will be used.</p>
	 */

	private String metricURL;

	/**
	 * This is an ordered list of menu items used to populate the &quot;Connect with HP&quot; drop down icon in the upper right of the UI.  The list will be pre-populated with global hp.com values.  Application teams may choose to add a site specific MenuItem to the list or to replace the entire list with site specific values.  Valid values are id, linkedtext, url, alt, and imageURL.  Other values in MenuItem will be ignored.  Note that images appear in the menu dropdown and therefore must be small, it is recommended to keep these images at 20x20 pixels or smaller.
	 */

	private List<MenuItem> contactHP;

	/**
	 * ?????????? - how does this get populated?  I.e. how is it aware of the &quot;Latest...&quot; <p>This is an ordered list of menu items used to populate the &quot;Online Communities&quot; drop down icon in the upper right of the UI.  The list will be pre-populated with global hp.com values.  Application teams may choose to add a site specific MenuItem to the list or to replace the entire list with site specific values.  Note that images appear in the menu dropdown and therefore must be small, it is recommended to keep these images at 85x85 pixels.</p>
	 */

	private List<MenuItem> communities;

	/**
	 * This is an optional image of a [ + ] and [ - ] floating in the bottom right of screen.  When the user clicks on this link they will be taken to the feedback url given by this String.
	 */

	private String feedback;

	/**
	 * List of services where page can be shared.  This is an ordered list of MenuItem's.  The relevant values are id, linkText, url, alt, and imageURL.  Other attributes of MenuItem will be ignored. Note that images appear in the menu dropdown and therefore must be small, it is recommended to keep these images at 20x20 pixels or smaller. <p>Clicking the Share button will expand a menu of sharing options. The exact list of options is determined by the BU and should be optimized locally by featuring only the most popular sharing options per country. The list is ordered by popularity, in descending order (for instance, in the U.S. the first share icon would be Facebook). Clicking any of these links will close the Share menu and open a new browser tab/window with the corresponding sharing page for that particular service. Users are sharing the current page, so the page URL is forwarded to the sharing page of the service. The “Email” option opens a pop-up window.</p> <p>The recommendation is to have 2 - 6 sharing tools. However, the Share menu can accommodate up to 20 sharing tools, divided into 2 columns of 10. For instance, if there are 8 options, they will all display in a single column; if there are 12 options, the first column will include 10 and the second column will include the remaining 2.</p>
	 */

	private List<MenuItem> share;

	/**
	 * <p>List of ways a users can subscribe to more information.  This is an ordered list of MenuItem's.  The relevant values are id, linkText, url, alt, and imageURL.  Other attributes of MenuItem will be ignored.  Note that images appear in the menu dropdown and therefore must be small, it is recommended to keep these images at 20x20 pixels or smaller.</p> <p>Clicking the Subscribe button will expand a menu containing 2 options:</p> <ul> <li>E-Newsletter: Clicking this button will collapse the menu and open a new browser tab/window with HP’s current newsletter sign-up page called “Subscriber’s Choice”</li> <li>RSS Feeds: Clicking this button will collapse the menu and open a new browser tab/window with HP’s current “RSS Feed Signup Page”</li> </ul> <p>Unlike the Share menu, Subscribe is not contextual to the current page content, however it may include contextual options in the future.</p>
	 */

	private List<MenuItem> subscribe;

	/**
	 * This should be the URL of the current page given in a printable view.
	 */

	private String printableURL;

	/**
	 * This is a ordered List of Menu items that will be used to create the footer at the bottom of the page.  Please note the following rules in relation to footer MenuItems. <ul> <li>There can only be two level of footerLinks</li> <li>The first level is text only, images will be ignored.</li> <li>Each MenuItem at the first level will be separated by a | (pipe) character.</li> <li>There can be up to three rows of footerLinks, although not all three lines are required.  Links will be populated from the top down in the space provided.</li> <li>Second level Footer items (aka Footer Flyups) are optional.  When they do appear they may include a reference to a 15x15 pixel image.</li> </ul> <p>The footer is pervasive throughout the site and provides information about country and language as well as links to general HP sites such as Privacy Statement, Terms &amp; Conditions, etc.  Below is more general information about standard hp.com footer.</p> <p><a href="https://h10014.www1.hp.com/hpweb/standards/visualdesign/footer.asp">https://h10014.www1.hp.com/hpweb/standards/visualdesign/footer.asp</a></p> <p></p> <p></p> <p></p>
	 */

	private List<MenuItem> footerLinks;

	private MenuItem siteHelp;

	/**
	 * WORK IN PROGRESS <p>This is a Set of LocaleLinks that will be used to create the Locale Selector on the site.  The Locales will be sorted automatically based on the Region and localized Locale name.</p>
	 */

	private SortedSet<CountryLanguageLink> localSelector;

	/**
	 * Determines whether to display the Locale selector.  If the value is true, the Locale selector will be available and the user will be able to change Locales using the Locale selector.  If the value is false the the current Locale will be indicated, but the user will not have the ability to change Locales. <p>default value: true</p>
	 */

	private boolean displayLocaleSelector;

	/**
	 * This is the name of the portal site listed in the Page Header just below the HP logo.
	 */

	private String siteTitle;

	/**
	 * This will be the localized page title for the specific page the user is currently visiting.  It is displayed just below the horizontal menu.
	 */

	private String pageTitle;

	/**
	 * <p>The value of this property will be rendered as the tagline text immediately beneath the page title. This is an optional page element. If no value is specified, no tagline will be rendered.</p> <p></p>
	 */

	private String tagline;

	/**
	 * <p>The value of this property will be rendered as the browser window title which is displayed in the title bar of the user's browser. If no value is specified for this property the Vignette JSP custom tag <code>&lt;vgn-portal:pageContentTitle /&gt;</code> will be used to render the window title.</p>
	 */

	private String windowTitle;

	/**
	 * <p>????? - Need more definition on how these will work.</p> <p>Breadcrumbs are used to represent the current page's location in the site hierarchy and are displayed immediately above the page header. Breadcrumbs are optional page element.</p>
	 */
	
	private List<MenuItem> breadcrumbs;

	
	/**
	 * This property determines whether there will be a 10px gutter space between portlets on the page.  If the value is set to true there will be a 10 pixel space between portlets.  If this value is set to false, then adjacent portlets line up with no separation between them.  Note: this applies to the space between portlets only, the outer edge spacing will be controlled by portletMarginEnabled. <p>default: true</p>
	 */
	
	private boolean portletGutterEnabled;

	
	/**
	 * ???? - What do we allow to be chosen? <ul> <li>on some pages the gutterspace is off entirely</li> <li>on other pages it is white</li> <li>on some pages it is black</li> <li>on some pages it is dark gray (same color as horizontal nav)</li> <li>finally on other pages it is light gray</li> <li>default to pink ;) that way it will get updated quickly when wrong.</li> </ul>
	 */
	
	private String portletGutterStyle;

	
	/**
	 * This property determines whether there will be a margin on the <span style='font-size:11.0pt;font-family:"Calibri","sans-serif"; mso-ascii-theme-font:minor-latin;mso-fareast-font-family:SimSun;mso-fareast-theme-font: minor-fareast;mso-hansi-theme-font:minor-latin;mso-bidi-font-family:"Times New Roman"; mso-bidi-theme-font:minor-bidi;mso-ansi-language:EN-US;mso-fareast-language: ZH-CN;mso-bidi-language:AR-SA'>OUTSIDE EDGE of the portlet page-display area: not just left/right edges, but top/bottom as well.<span style="mso-spacerun:yes">  </span></span>If this value is set to false, then there will be no margin on the outside edge of the portlets.. <p>default: true</p>
	 */
	
	private boolean portletMarginEnabled;

	
	/**
	 * <p>???? - What do we allow to be chosen?</p> <ul> <li>sometimes the L/R margins are different color than the gutterspace,</li> <li> sometimes they are the same color as the gutterspace,</li> <li>sometimes they exist when otherwise there is NO gutterspace</li> <li>sometimes gutterspace may exist with NO margins (in principle at least; not shown in UI mockups but I think you should design for this case)</li> </ul>
	 */
	
	private String portletMarginStyle;


}

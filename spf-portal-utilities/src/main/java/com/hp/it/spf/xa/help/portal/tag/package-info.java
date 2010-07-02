/**
 * <p>
 * This package contains JSP tag classes and tag documentation for online help
 * in Vignette portal components in Shared Portal Framework. Please see the
 * class documentation for more information on these classes.
 * </p>
 * <blockquote>
 * <p>
 * <b>Note:</b> SPF portal developers should never need to use the Java classes
 * in this package directly. Use the respective JSP tags instead. For your
 * reference, the tag classes and their respective JSP tags are listed below:
 * </p>
 * 
 * <dl>
 * <dt>{@link com.hp.it.spf.xa.help.portal.tag.ClassicContextualHelpTag} - Tag
 * class for <a href="#classicContextualHelp">
 * <code>&lt;spf-help-portal:classicContextualHelp&gt;</code></a></dt>
 * <dt>{@link com.hp.it.spf.xa.help.portal.tag.ClassicGlobalHelpTag} - Tag class
 * for <a href="#classicGlobalHelp">
 * <code>&lt;spf-help-portal:classicGlobalHelp&gt;</code></a></dt>
 * </dl>
 * </blockquote>
 * 
 * <blockquote> <b>Note:</b> The JSP tags here generate "standalone" online help
 * links, where the link encloses an image or whole message. To "inject" online
 * help links into the middle of a message, see the discussion in
 * {@link com.hp.it.spf.xa.i18n.portal.tag}. </blockquote>
 * 
 * <hr>
 * <a name="TLD">
 * <h3>spf-help-portal.tld</h3> </a>
 * <p>
 * The TLD file is <code>spf-help-portal.tld</code> and it is contained inside
 * the SPF portal utilities JAR, in the <code>META-INF/</code> folder. This JAR
 * is provided in the SPF Vignette environment. The TLD file's URI is
 * <code>/spf-help-portal.tld</code> so that is the URI you should refer to it
 * with, in your component's JSP's. Although you can use whatever JSP tag prefix
 * you like, the documentation here will use the prefix
 * <code>spf-help-portal</code>.
 * </p>
 * <hr>
 * <a name="classicContextualHelp">
 * <h3>
 * &lt;spf-help-portal:classicContextualHelp&gt;</h3> </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-help-portal&quot; uri=&quot;/spf-help-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-help-portal:classicContextualHelp
 * 	anchor=&quot;&lt;link-text&gt;&quot;
 * 	anchorKey=&quot;&lt;link-message-key&gt;&quot;
 * 	anchorImg=&quot;&lt;link-image-URL&gt;&quot;
 * 	anchorImgKey=&quot;&lt;link-image-message-key&gt;&quot;
 * 	anchorImgAlt=&quot;&lt;link-image-alt-text&gt;&quot;
 * 	anchorImgAltKey=&quot;&lt;link-image-alt-message-key&gt;&quot;
 * 	escape=&quot;&lt;true-or-false&gt;&quot;
 * 	filterSpan=&quot;&lt;true-or-false&gt;&quot;
 * 	title=&quot;&lt;help-title&gt;&quot;
 * 	titleKey=&quot;&lt;help-title-message-key&gt;&quot;
 * 	content=&quot;&lt;help-content&gt;&quot;
 * 	contentKey=&quot;&lt;help-content-message-key&gt;&quot;
 * 	noScriptHref=&quot;&lt;uri&gt;&quot;
 * 	width=&quot;&lt;pixels&gt;&quot;
 * 	borderStyle=&quot;&lt;inline-style&gt;&quot;
 * 	borderClass=&quot;&lt;css-classname&gt;&quot;
 * 	titleStyle=&quot;&lt;inline-style&gt;&quot;
 * 	titleClass=&quot;&lt;css-classname&gt;&quot;
 * 	contentStyle=&quot;&lt;inline-style&gt;&quot;
 * 	contentClass=&quot;&lt;css-classname&gt;&quot;
 * 	/&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * You use this tag to generate a "classic" contextual help link and its
 * associated dynamic overlay. The link can be a whole text string or image. The
 * result is a "standalone" contextual help in your Vignette portal component's
 * rendering. If you need to "inject" contextual help into a surrounding message
 * instead, use the
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag
 * instead (see the discussion in {@link com.hp.it.spf.xa.i18n.portal.tag}). (If
 * you don't know what is meant by "contextual help" or the "classic" rendition,
 * or "standalone" or "inject", see the discussion in the SPF Portal Utilities
 * Developer's Guide.)
 * </p>
 * 
 * <p>
 * For example, let's say your Vignette component needs to link an image like
 * this <img src="../doc-files/key.gif"> so that, when clicked, it reveals a
 * classic contextual help overlay about Web security. Using standalone
 * contextual help, you can have the following in your component's message
 * properties:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * key.help.title=Service Portal and your security
 * key.help.content=Internet security is important to us...
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * For the image, you will need its URL and optionally some alt text for it. If
 * the image is already hosted on a Web server elsewhere, you can just use that
 * URL. Or if you want to host the image out of your portal component, store the
 * image as a <i>secondary support file</i> into your component, and you will be
 * able to then generate a URL for it through the tag. (In the case of localized
 * images, store all of them properly tagged by locale: eg
 * <code>filename.gif</code> for the base image, <code>filename_zh.gif</code>
 * for the default Chinese image, <code>filename_zh_HK.gif</code> for the Hong
 * Kong Chinese image, etc.) As for the image's alt text, that can be a message
 * property in your component's resource bundle like above:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * key.help.alt=Click for more information
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * In this example, we will assume that the image (named <code>key.gif</code>)
 * has been stored into the component as a secondary support file. Then use the
 * following in your JSP to express this image with the appropriate contextual
 * help markup (text, HTML, CSS and JavaScript) attached to it. Note: this
 * demonstration expresses the contextual help layer using the built-in
 * "default" style; see the discussion on tag attributes for how to override
 * that and provide your own style.
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-help-portal&quot; uri=&quot;/spf-help-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-help-portal:classicContextualHelp
 * 	anchorImgKey=&quot;key.gif&quot; anchorImgAltKey=&quot;key.help.alt&quot;
 * 	titleKey=&quot;key.help.title&quot; contentKey=&quot;key.help.content&quot;
 * 	/&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * This JSP tag renders the image with your alt text like this: <img
 * src="../doc-files/key.gif" alt="Click for more information"> where both the
 * image and alt text are properly localized for the user. And when the user
 * clicks on the image, the contextual help layer would appear, looking like
 * this:
 * </p>
 * 
 * <img src="../doc-files/contextualHelp.jpg">
 * 
 * <p>
 * If the browser was unscripted, though, clicking on the link would open your
 * portal site's global help secondary page. If your portal site does not have a
 * global help secondary page, then the link would be inoperable (ie just the
 * image would display, with no contextual-help link around it at all).
 * </p>
 * 
 * <p>
 * Here are the tag attributes:
 * </p>
 * 
 * <dl>
 * <dt><code>anchor="<i>link-text</i>"</code><br>
 * <code>anchorKey="<i>link-message-key</i>"</code></dt>
 * <dd>
 * <p>
 * If your link is a text link, use one of these 2 attributes to specify it.
 * With <code>anchor</code> you pass the actual text string for the link. With
 * <code>anchorKey</code> you pass a message key for a message in your portal
 * component's resource bundle. If for some reason you pass both, then
 * <code>anchor</code> will take precedence.
 * </p>
 * <p>
 * If your link is an image, use <code>anchorImg</code> or
 * <code>anchorImgKey</code> instead (see below). You must use one of these 4
 * attributes or there will be an error.
 * </p>
 * </dd>
 * 
 * <dt><code>anchorImg="<i>link-image-URL</i>"</code><br>
 * <code>anchorImgKey="<i>link-image-message-key</i>"</code></dt>
 * <dd>
 * <p>
 * If your link is an image, use one of these 2 attributes to provide the URL
 * for it. With <code>anchorImg</code> you pass the actual URL for the link
 * image. This can be an HTTP or HTTPS URL pointing at any image anywhere.
 * </p>
 * <p>
 * With <code>anchorImgKey</code> you pass the filename of a <i>secondary
 * support file</i> in your portal component, which is the image to use. This
 * can be a base filename for a localized bundle of images, in which case the
 * best-candidate localized image URL will be automatically expressed given the
 * user's current locale. The localized image files should all be stored as
 * secondary support files in the component, and tagged with locale as per the
 * Java standard for {@link java.util.ResourceBundle}): eg
 * <code>filename.gif</code> for the base file (this is also the filename you
 * would pass to the <code>anchorImgKey</code> tag attribute),
 * <code>filename_fr.gif</code> for the default French version of the image,
 * <code>filename_fr_CA.gif</code> for the Canadian French version of the image,
 * etc.
 * </p>
 * <p>
 * If for some reason you provide both <code>anchorImg</code> and
 * <code>anchorImgKey</code> the former will take priority. If you need to link
 * text, not an image, then use <code>anchor</code> or <code>anchorKey</code>
 * instead of these attributes. You must use one of these 4 attributes or the
 * tag will yield an error.
 * </p>
 * <p>
 * To optionally provide alt text for the image, use <code>anchorImgAlt</code>
 * or <code>anchorImgAltKey</code> (see below).
 * </p>
 * </dd>
 * 
 * <dt><code>anchorImgAlt="<i>link-image-alt-text</i>"</code><br>
 * <code>anchorImgAltKey="<i>link-image-alt-message-key</i>"</code></dt>
 * <dd>
 * <p>
 * Use one of these attributes to provide alt text for a linked image. (With a
 * text link, these attributes have no function.) With <code>anchorImgAlt</code>
 * you pass the actual text string to use for the alt. With
 * <code>anchorImgAltKey</code> you provide the keyname for a message property
 * in your component's resource bundle. If for some reason you provide both
 * attributes, <code>anchorImgAlt</code> will take priority.
 * </p>
 * </dd>
 * 
 * <dt><code>title="<i>help-title</i>"</code><br>
 * <code>titleKey="<i>help-title-message-key</i>"</code></dt>
 * <dd>
 * <p>
 * Use one of these attributes to provide the title for the contextual help (it
 * is an error if you do not use one of these with a non-null, non-blank value;
 * if you use both, the <code>title</code> attribute takes precedence). The
 * <code>title</code> attribute provides the literal string to use for the title
 * (so it is your responsibility to have already generated that string,
 * localized as needed). The <code>titleKey</code> is a message key for the
 * title - the tag looks inside your current component's message resource bundle
 * to retrieve the title.
 * </p>
 * </dd>
 * 
 * <dt><code>content="<i>help-content</i>"</code><br>
 * <code>titleKey="<i>help-content-message-key</i>"</code></dt>
 * <dd>
 * <p>
 * Use one of these attributes to provide the title for the contextual help (it
 * is an error if you do not use one of these with a non-null, non-blank value;
 * if you use both, the <code>content</code> attribute takes precedence). The
 * <code>content</code> attribute provides the literal string to use for the
 * main help content (so it is your responsibility to have already generated
 * that string, localized as needed). The <code>contentKey</code> is a message
 * key for that - the tag looks inside your current component's message resource
 * bundle to retrieve the main help content.
 * </p>
 * </dd>
 * 
 * <dt><code>noScriptHref="<i>uri</i>"</code></dt>
 * <dd>
 * <p>
 * The <code>noScriptHref</code> attribute gives an alternative URL to offer the
 * browser to use in case JavaScript is disabled. For a script-enabled browser,
 * this attribute has no effect.
 * </p>
 * 
 * <p>
 * Because the classic rendition of contextual help requires JavaScript, it will
 * not work if JavaScript is not enabled in the browser. So by default, the
 * classic rendition of contextual help includes a no-script URL pointing to
 * your portal site's global help secondary page (if your portal site provides
 * one - if not, then there is no no-script URL provided by default). But you
 * can specify any other URL to be used in the no-script case instead, via this
 * attribute. If your portal site has a global help secondary page, you can even
 * set this attribute to a document fragment like
 * <code>noScriptHref="#go_here"</code> so that, in the no-script case, the
 * user's browser jumps to that part of your global help secondary page content.
 * </p>
 * </dd>
 * 
 * <dt><code>width="<i>pixels</i>"</code></dt>
 * <dd>
 * <p>
 * The classic rendition of contextual help is a DHTML popup table with a
 * 300-pixel width by default. You can override that width with this attribute.
 * </p>
 * </dd>
 * 
 * <dt><code>borderStyle="<i>inline-style</i>"</code><br>
 * <code>borderClass="<i>css-classname</i>"</code></dt>
 * <dd>
 * <p>
 * The classic rendition of contextual help is a DHTML popup table with a thin
 * solid black border by default. You can use either of these attributes to
 * style the border differently.
 * </p>
 * 
 * <p>
 * The <code>borderStyle</code> attribute works like the <code>style</code>
 * attribute of most HTML tags - it lets you provide a set of CSS properties
 * inline with your tag. The <code>borderClass</code> attribute works like the
 * <code>class</code> attribute of most HTML tags - it lets you refer to a CSS
 * class defined elsewhere in your JSP or in an included CSS sheet. Typically
 * you would use only one or the other, though the tag will let you provide
 * both. If you do use both, then CSS properties defined in
 * <code>borderStyle</code> override the same properties defined in the
 * <code>borderClass</code> class, as usual with CSS. Similarly, CSS properties
 * defined in either override any defined elsewhere on the page for tables.
 * </p>
 * 
 * <p>
 * The CSS properties you can define are the ones which are relevant to table
 * borders (ie CSS properties for the HTML <code>&lt;TABLE&gt;</code> tag). For
 * example, here is what the classic contextual help popup looks like with
 * <code>borderStyle="border-color:blue;border-width:3px;border-style:solid"</code>
 * :
 * </p>
 * 
 * <img src="../doc-files/contextualHelpBlueBorder.jpg">
 * 
 * <p>
 * You can set <code>borderStyle=""</code> to cancel the default border style.
 * </p>
 * </dd>
 * 
 * <dt><code>titleStyle="<i>inline-style</i>"</code><br>
 * <code>titleClass="<i>css-classname</i>"</code></dt>
 * <dd>
 * <p>
 * The classic rendition of contextual help is a DHTML popup table where the top
 * row contains the title for the contextual help. By default, that title is
 * displayed using bold white font on a blue background. You can use either of
 * these attributes to style the title differently.
 * </p>
 * 
 * <p>
 * The <code>titleStyle</code> attribute works like the <code>style</code>
 * attribute of most HTML tags - it lets you provide a set of CSS properties
 * inline with your tag. The <code>titleClass</code> attribute works like the
 * <code>class</code> attribute of most HTML tags - it lets you refer to a CSS
 * class defined elsewhere in your JSP or in an included CSS sheet. Typically
 * you would use only one or the other, though the tag will let you provide
 * both. If you do use both, then CSS properties defined in
 * <code>titleStyle</code> override the same properties defined in the
 * <code>titleClass</code> class, as usual with CSS. Similarly, CSS properties
 * defined in either override any defined elsewhere on the page for tables.
 * </p>
 * 
 * <p>
 * The CSS properties you can define are the ones which are relevant to table
 * cells (ie CSS properties for the HTML <code>&lt;TD&gt;</code> tag). For
 * example, here is what the classic contextual help popup looks like with
 * 
 * <code>titleStyle="background-color:gray;font-style:italic;color:black;font-weight:bold"</code>
 * :
 * </p>
 * 
 * <img src="../doc-files/contextualHelpGrayTitle.jpg">
 * 
 * <p>
 * You can set <code>titleStyle=""</code> to cancel the default title style.
 * </p>
 * </dd>
 * 
 * <dt><code>contentStyle="<i>inline-style</i>"</code><br>
 * <code>contentClass="<i>css-classname</i>"</code></dt>
 * <dd>
 * <p>
 * The classic rendition of contextual help is a DHTML popup table where the
 * second row contains the main content for the contextual help. By default,
 * that content is displayed using normal black font on a white background. You
 * can use either of these attributes to style the main help content
 * differently.
 * </p>
 * 
 * <p>
 * The <code>contentStyle</code> attribute works like the <code>style</code>
 * attribute of most HTML tags - it lets you provide a set of CSS properties
 * inline with your tag. The <code>contentClass</code> attribute works like the
 * <code>class</code> attribute of most HTML tags - it lets you refer to a CSS
 * class defined elsewhere in your JSP or in an included CSS sheet. Typically
 * you would use only one or the other, though the tag will let you provide
 * both. If you do use both, then CSS properties defined in
 * <code>contentStyle</code> override the same properties defined in the
 * <code>contentClass</code> class, as usual with CSS. Similarly, CSS properties
 * defined in either override any defined elsewhere on the page for tables.
 * </p>
 * 
 * <p>
 * The CSS properties you can define are the ones which are relevant to table
 * cells (ie CSS properties for the HTML <code>&lt;TD&gt;</code> tag). See the
 * above example (for styling the title) for the general idea. You can set
 * <code>contentStyle=""</code> to cancel the default style.
 * </p>
 * </dd>
 * 
 * <dt><code>escape="<i>true-or-false</i>"</code></dt>
 * <dd>
 * <p>
 * The <code>escape</code> attribute is used as a switch to control the
 * HTML-escaping policy for all message text in this contextual help instance.
 * By default, the expressed message strings for link, title, image alt, and
 * content do not escape any HTML special characters they may contain, such as
 * <code>&lt;</code>. This lets you put HTML markup in your messages and have it
 * render as such in the browser. If you need the HTML special characters to be
 * converted into their corresponding character entities, so that they display
 * literally in the browser instead, use the <code>escape="true"</code>
 * attribute on the tag.
 * </p>
 * </dd>
 * 
 * <dt><code>filterSpan="<i>true-or-false</i>"</code></dt>
 * <dd>
 * <p>
 * Similarly, the <code>filterSpan</code> attribute indicates whether to remove
 * any <code>&lt;SPAN&gt;</code> tags which Vignette may have inserted. Vignette
 * does this automatically, to help assistive devices such as readers. Normally
 * the <code>&lt;SPAN&gt;</code> tags are invisible, but some browsers in some
 * contexts (eg the HTML <code>&lt;TITLE&gt;</code> tag) will display these
 * erroneously. The <code>filterSpan="true"</code> attribute setting lets you
 * suppress those <code>&lt;SPAN&gt;</code> tags. By default they are not
 * suppressed.
 * </p>
 * </dd>
 * 
 * </dl>
 * 
 * <p>
 * <b>Note:</b> The classic rendition of contextual help utilizes an "X" image
 * on which the user can click to close the window. This is a GIF image which
 * you can supply in your portal component. You can localize the image if
 * desired. Name the base file for the image <code>btn_close.gif</code> and put
 * it (as a secondary support file) in the <code>WEB-INF/</code> folder of your
 * component. Localized versions of the image should be tagged by locale and
 * placed in the same location with the same basename.
 * </p>
 * <p>
 * You don't have to supply the close button. SPF provides a default
 * (unlocalized) one in the <code>images/</code> folder of the Vignette portal
 * Web application, and this is what will be used if you don't provide one. </
 * <p>
 * In either case, you can provide some ALT text for the close button if
 * desired. Just define a message in your component's resource bundle named
 * <code>contextualHelp.close.alt</code>. If you do not, a blank ALT text will
 * be used by default.
 * </p>
 * 
 * <hr>
 * <a name="classicGlobalHelpParam">
 * <h3>
 * &lt;spf-help-portal:classicGlobalHelp&gt;</h3> </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-help-portal&quot; uri=&quot;/spf-help-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-help-portal:classicGlobalHelp
 * 	anchor=&quot;&lt;link-text&gt;&quot;
 * 	anchorKey=&quot;&lt;link-message-key&gt;&quot;
 * 	anchorImg=&quot;&lt;link-image-URL&gt;&quot;
 * 	anchorImgKey=&quot;&lt;link-image-message-key&gt;&quot;
 * 	anchorImgAlt=&quot;&lt;link-image-alt-text&gt;&quot;
 * 	anchorImgAltKey=&quot;&lt;link-image-alt-message-key&gt;&quot;
 * 	escape=&quot;&lt;true-or-false&gt;&quot;
 * 	filterSpan=&quot;&lt;true-or-false&gt;&quot;
 * 	fragment=&quot;&lt;fragment-name&gt;&quot;
 * 	windowFeatures=&quot;&lt;popup-window-features&gt;&quot;
 * 	/&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * You use this tag to generate a "classic" global help link pointing to your
 * portal site's <i>global help secondary page</i> in a child (popup) window of
 * your specification. The link can be a whole text string or an image. The
 * result is a "standalone" global help link in your Vignette portal component's
 * rendering. If you need to "inject" a global help link into a surrounding
 * message instead, use the
 * <code>&lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</code> tag instead
 * (see the discussion in {@link com.hp.it.spf.xa.i18n.portal.tag}). If you
 * don't know what is meant by "global help" or the "classic" rendition, or
 * "standalone" or "inject", see the discussion in the SPF Portal Utilities
 * Developer's Guide.
 * </p>
 * <blockquote>
 * <p>
 * <b>Note:</b> Unlike
 * <code>&lt;spf-help-portal:classicContextualHelp&gt;</code>, which emits both
 * the hyperlink and the DHTML for your help overlay - see above - this tag only
 * emits a hyperlink, which points to your site's presumed global help secondary
 * page in the "classic" manner (ie a child popup browser window, with window
 * features set to your specifications: eg buttonless, no address bar, etc).
 * Your portal site must therefore implement a global help secondary page
 * instance for this to point to.
 * </p>
 * <ul>
 * <li>It must be an instance of the SPF-provided global help secondary page
 * type (which you will find in Vignette server console, named <b>Shared Portal
 * Framework - Global Help Secondary Page</b> with component ID
 * <code>spf-global-help-secondarypage</code>).</li>
 * <li>It must use the template friendly ID <code>PUBLIC_SPF_GLOBAL_HELP</code>
 * as with all global help secondary page instances.</li>
 * <li>Its primary JSP should express your sitewide help content, typically as
 * suggested by the SPF Global Help Developer's Guide (though if you want to
 * express your content a different way, that is fine).</li>
 * <li>Your global help secondary page must be shared to your portal site, by
 * the Vignette server administrator.</li>
 * <li>And it must be configured as the secondary page to use for that secondary
 * page type; your site's grid and theme should also be applied to it. You do
 * this in your Vignette site console, in <b>Site Settings</b> &gt;
 * <b>Appearance</b> &gt; <b>Secondary Pages</b>.</li>
 * <li>For more information, see the SPF Global Help Developer's Guide.</li>
 * </p> </blockquote>
 * <p>
 * For example, let's say you need to link this image <img
 * src="../doc-files/more_info.gif"> (which may be localized) to your site's
 * global help secondary page rendered within a classic-style popup window.
 * First you will need the image URL and optionally some alt text for it. If the
 * image is hosted on a Web server elsewhere, you can just use that URL. Or if
 * you want to host the image our of your portal component, store the image as a
 * <i>secondary support file</i> into your component, and you will then be able
 * to generate a URL for it through the tag. (In the case of localized images
 * such as this one, store all of the localized images, properly tagged by
 * locale: eg <code>filename.gif</code> for the base image,
 * <code>filename_zh.gif</code> for the default Chinese image,
 * <code>filename_zh_HK.gif</code> for the Hong Kong Chinese image, etc.) As for
 * the image's alt text, that can be a message property in your component's
 * resource bundle:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * key=More information
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Then use the following in your JSP:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-help-portal&quot; uri=&quot;/spf-help-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-help-portal:classicGlobalHelp
 * 	anchorImgKey=&quot;more_info.gif&quot; anchorImgAltKey=&quot;key&quot;
 * 	fragment=&quot;#profile&quot;
 * 	windowFeatures=&quot;width=974;height=610;menubar=no;status=no;toolbar=no;scrollbars=yes&quot;/&gt;
 * 	/&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Note this assumes the existence of a document fragment, <code>#profile</code>
 * , inside your global help content. This will cause the popup to jump to that
 * position in the the global help secondary page when the popup opens. This
 * example also shows how you can pass in window feature specifications to size
 * the window and enable or disable its features as desired. (The syntax for
 * window features is just that of the JavaScript <code>window.open()</code>
 * method - see any JavaScript documentation for more information.)
 * </p>
 * <p>
 * The end result is that the image and alt text will be rendered to the user
 * like this <img src="../doc-files/more_info.gif" alt="More information">
 * properly localized for the user's current locale based on the translations
 * available among your message properties and <code>more_info.gif</code>
 * secondary support files. And when the user clicks the linked image, the
 * following happens:
 * </p>
 * <ol>
 * <li>A child browser window (popup window) will open.</li>
 * <li>The popup window will conform to the specified window features (in this
 * case: 974 by 410 pixels, with no buttons or bars except scrollbars).</li>
 * <li>The popup window open your site's global help secondary page instance.</li>
 * <li>Furthermore it will jump to the <code>#profile</code> section of the
 * content in that page (by default it would just go to the top of the page).</li>
 * <li>For example, the popup window could look like this (note that all of the
 * content inside the window comes from your site's global help secondary page
 * and so will vary based on your secondary page content, grid and theme).</li>
 * </ol>
 * <img src="../doc-files/globalHelp.jpg">
 * <p>
 * The global help popup requires JavaScript to function. In an unscripted
 * browser, clicking the "managing your profile" link would open the same global
 * help secondary page, but in the same browser window instead of a popup.
 * </p>
 * 
 * <p>
 * Here are the tag attributes:
 * </p>
 * 
 * <dl>
 * <dt><code>anchor="<i>link-text</i>"</code><br>
 * <code>anchorKey="<i>link-message-key</i>"</code></dt>
 * <dd>
 * <p>
 * If your link is a text link, use one of these 2 attributes to specify it.
 * With <code>anchor</code> you pass the actual text string for the link. With
 * <code>anchorKey</code> you pass a message key for a message in your portal
 * component's resource bundle. If for some reason you pass both, then
 * <code>anchor</code> will take precedence.
 * </p>
 * <p>
 * If your link is an image, use <code>anchorImg</code> or
 * <code>anchorImgKey</code> instead (see below). You must use one of these 4
 * attributes or there will be an error.
 * </p>
 * </dd>
 * 
 * <dt><code>anchorImg="<i>link-image-URL</i>"</code><br>
 * <code>anchorImgKey="<i>link-image-message-key</i>"</code></dt>
 * <dd>
 * <p>
 * If your link is an image, use one of these 2 attributes to provide the URL
 * for it. With <code>anchorImg</code> you pass the actual URL for the link
 * image. This can be an HTTP or HTTPS URL pointing at any image anywhere.
 * </p>
 * <p>
 * With <code>anchorImgKey</code> you pass the filename of a <i>secondary
 * support file</i> in your portal component, which is the image to use. This
 * can be a base filename for a localized bundle of images, in which case the
 * best-candidate localized image URL will be automatically expressed given the
 * user's current locale. The localized image files should all be stored as
 * secondary support files in the component, and tagged with locale as per the
 * Java standard for {@link java.util.ResourceBundle}): eg
 * <code>filename.gif</code> for the base file (this is also the filename you
 * would pass to the <code>anchorImgKey</code> tag attribute),
 * <code>filename_fr.gif</code> for the default French version of the image,
 * <code>filename_fr_CA.gif</code> for the Canadian French version of the image,
 * etc.
 * </p>
 * <p>
 * If for some reason you provide both <code>anchorImg</code> and
 * <code>anchorImgKey</code> the former will take priority. If you need to link
 * text, not an image, then use <code>anchor</code> or <code>anchorKey</code>
 * instead of these attributes. You must use one of these 4 attributes or the
 * tag will yield an error.
 * </p>
 * <p>
 * To optionally provide alt text for the image, use <code>anchorImgAlt</code>
 * or <code>anchorImgAltKey</code> (see below).
 * </p>
 * </dd>
 * 
 * <dt><code>anchorImgAlt="<i>link-image-alt-text</i>"</code><br>
 * <code>anchorImgAltKey="<i>link-image-alt-message-key</i>"</code></dt>
 * <dd>
 * <p>
 * Use one of these attributes to provide alt text for a linked image. (With a
 * text link, these attributes have no function.) With <code>anchorImgAlt</code>
 * you pass the actual text string to use for the alt. With
 * <code>anchorImgAltKey</code> you provide the keyname for a message property
 * in your component's resource bundle. If for some reason you provide both
 * attributes, <code>anchorImgAlt</code> will take priority.
 * </p>
 * </dd>
 * 
 * <dt><code>fragment="<i>fragment-name</i>"</code></dt>
 * <dd>
 * <p>
 * The name of a document anchor (
 * <code>&lt;a name="<i>fragment-name</i>"&gt;</code>) within the global help
 * content, to which the client browser should jump. This is optional.
 * </p>
 * </dd>
 * <dt><code>windowFeatures="<i>popup-window-features</i>"</code></dt>
 * <dd>
 * <p>
 * A string of JavaScript <code>window.open()</code> feature specifications to
 * be used in the popup window in place of the default features. This is
 * optional. The default features are:
 * <ul>
 * <li>410 pixels by 610 pixels (width by height)</li>
 * <li>no menu bar</li>
 * <li>no tool bar</li>
 * <li>no status bar</li>
 * <li>no scrollbars</li>
 * <li>no buttons</li>
 * <li>resizeable</li>
 * </ul>
 * </p>
 * </dd>
 * 
 * <dt><code>escape="<i>true-or-false</i>"</code></dt>
 * <dd>
 * <p>
 * The <code>escape</code> attribute is used as a switch to control the
 * HTML-escaping policy for the link and image alt text. By default, the
 * expressed message strings do not escape any HTML special characters they may
 * contain, such as <code>&lt;</code>. This lets you put HTML markup in your
 * messages and have it render as such in the browser. If you need the HTML
 * special characters to be converted into their corresponding character
 * entities, so that they display literally in the browser instead, use the
 * <code>escape="true"</code> attribute on the tag.
 * </p>
 * </dd>
 * 
 * <dt><code>filterSpan="<i>true-or-false</i>"</code></dt>
 * <dd>
 * <p>
 * Similarly, the <code>filterSpan</code> attribute indicates whether to remove
 * any <code>&lt;SPAN&gt;</code> tags which Vignette may have inserted. Vignette
 * does this automatically, to help assistive devices such as readers. Normally
 * the <code>&lt;SPAN&gt;</code> tags are invisible, but some browsers in some
 * contexts (eg the HTML <code>&lt;TITLE&gt;</code> tag) will display these
 * erroneously. The <code>filterSpan="true"</code> attribute setting lets you
 * suppress those <code>&lt;SPAN&gt;</code> tags. By default they are not
 * suppressed.
 * </p>
 * </dd>
 * 
 * </dl>
 * 
 */
package com.hp.it.spf.xa.help.portal.tag;


/**
 * <p>
 * This package contains JSP tag classes and tag documentation for
 * internationalizing Vignette portal components in Shared Portal Framework.
 * Please see the class documentation for more information on these classes.
 * </p>
 * <blockquote>
 * <p>
 * <b>Note:</b> SPF portal developers should never need to use the Java classes
 * in this package directly. Use the respective JSP tags instead. For your
 * reference, the tag classes and their respective JSP tags are listed below:
 * </p>
 * 
 * <dl>
 * <dt>{@link com.hp.it.spf.xa.i18n.portal.tag.MessageTag} - Tag class for <a
 * href="#message"><code>&lt;spf-i18n-portal:i18nValue&gt;</code></a></dt>
 * <dt>{@link com.hp.it.spf.xa.i18n.portal.tag.ParamTag} - Tag class for <a
 * href="#param"><code>&lt;spf-i18n-portal:i18nParam&gt;</code></a></dt>
 * <dt>{@link com.hp.it.spf.xa.i18n.portal.tag.ClassicContextualHelpParamTag} -
 * Tag class for <a href="#classicContextualHelpParam">
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code></a></dt>
 * <dt>{@link com.hp.it.spf.xa.i18n.portal.tag.ClassicGlobalHelpParamTag} - Tag
 * class for <a href="#classicGlobalHelpParam">
 * <code>&lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</code></a></dt>
 * <dt>{@link com.hp.it.spf.xa.i18n.portal.tag.LocalizedFileURLTag} - Tag class
 * for <a href="#localizedFileURL">
 * <code>&lt;spf-i18n-portal:localizedFileURL&gt;</code></a></dt>
 * <dt>{@link com.hp.it.spf.xa.i18n.portal.tag.ClassicLocaleIndicatorTag} - Tag
 * class for <a href="#indicator">
 * <code>&lt;spf-i18n-portal:classicLocaleIndicator&gt;</code></a></dt>
 * <dt>{@link com.hp.it.spf.xa.i18n.portal.tag.ClassicLocaleSelectorTag} - Tag
 * class for <a href="#selector">
 * <code>&lt;spf-i18n-portal:classicLocaleSelector&gt;</code></a></dt>
 * </dl>
 * </blockquote>
 * 
 * <hr>
 * <a name="TLD">
 * <h3>spf-i18n-portal.tld</h3> </a>
 * <p>
 * The TLD file is <code>spf-i18n-portal.tld</code> and it is contained inside
 * the SPF portal utilities JAR, in the <code>META-INF/</code> folder. This JAR
 * is provided in the SPF Vignette environment. The TLD file's URI is
 * <code>/spf-i18n-portal.tld</code> so that is the URI you should refer to it
 * with, in your component's JSP's. Although you can use whatever JSP tag prefix
 * you like, the documentation here will use the prefix
 * <code>spf-i18n-portal</code>.
 * </p>
 * 
 * <hr>
 * <a name="message">
 * <h3>&lt;spf-i18n-portal:i18nValue&gt;</h3> </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:i18nValue
 * 	stringID=&quot;&lt;component-id&gt;&quot;
 * 	key=&quot;&lt;message-key&gt;&quot;
 * 	defaultValue=&quot;&lt;default-value&gt;&quot;
 * 	escape=&quot;&lt;true-or-false&gt;&quot;
 * 	filterSpan=&quot;&lt;true-or-false&gt;&quot;
 * 	/&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * This tag expresses the message for the given key from an SPF Vignette portal
 * component's resource bundle, localized to best-fit the user's locale.
 * </p>
 * 
 * <p>
 * By default the tag checks the current component's resource bundle, but given
 * a different <i>component-id</i> it will look in that component's bundle
 * instead. All of the message resources in the bundle are searched for the
 * given <i>message-key</i>. The message property files comprising the bundle
 * should (except for the base file) be tagged by locale according to the
 * Java-standard for {@link java.util.ResourceBundle}. And as is standard for
 * SPF Vignette, the basename of the bundle should match the component ID and be
 * stored in the <code>/WEB-INF/i18n/</code> folder of the component. So, for a
 * component whose component ID is <code>myComponent</code>, message property
 * files such as <code>myComponent.properties</code> (base),
 * <code>myComponent_fr.properties</code> (default French), and
 * <code>myComponent_zh_CN.properties</code> (China Chinese) should be stored
 * within <code>/WEB-INF/i18n/</code> of that component in order to be found by
 * this tag.
 * </p>
 * 
 * <p>
 * Here are the tag attributes:
 * </p>
 * 
 * <dl>
 * <dt><code>stringID="<i>component-id</i>"</code></dt>
 * <dd>
 * <p>
 * The <code>stringID</code> attribute is used to specify an overriding
 * component ID for the bundle to search. By default, the component ID of the
 * currently-executing component is assumed.
 * </p>
 * </dd>
 * <dt><code>key="<i>message-key</i>"</code></dt>
 * <dd>
 * <p>
 * The <code>key</code> attribute is used to specify the message to retrieve. It
 * is required.
 * </p>
 * </dd>
 * <dt><code>defaultValue="<i>default-value</i>"</code></dt>
 * <dd>
 * <p>
 * The <code>defaultValue</code> attribute is used to specify a default value to
 * express, in case the given message key is not found. This is an optional
 * attribute; the message key itself is expressed if no explicit (ie non-null
 * and non-blank) <code>defaultValue</code> attribute is provided.
 * </p>
 * </dd>
 * <dt><code>escape="<i>true-or-false</i>"</code></dt>
 * <dd>
 * <p>
 * The <code>escape</code> attribute is used as a switch to control the
 * HTML-escaping policy for the returned message. By default, the expressed
 * message string does not escape any HTML special characters it may contain,
 * such as <code>&lt;</code>. This lets you put HTML markup in your messages and
 * have it render as such in the browser. If you need the HTML special
 * characters to be converted into their corresponding character entities, so
 * that they display literally in the browser instead, use the
 * <code>escape="true"</code> attribute on the tag.
 * </p>
 * </dd>
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
 * </dl>
 * 
 * <p>
 * Note that <code>&lt;spf-i18n-portal:i18nValue&gt;</code> may also contain the
 * following SPF tags:
 * </p>
 * 
 * <dl>
 * <dt><a href="#param"><code>&lt;spf-i18n-portal:i18nParam&gt;</code></a></dt>
 * <dd>
 * <p>
 * If your message value includes general string parameters as per the Java
 * standard for {@link java.text.MessageFormat}, use the
 * <code>&lt;spf-i18n-portal:i18nParam&gt;</code> tag inside the
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag body, in order, for each
 * such parameter. See the <a href="#param">
 * <code>&lt;spf-i18n-portal:i18nParam&gt;</code></a> tag documentation below.
 * </p>
 * </dd>
 * 
 * <dt><a href="#classicContextualHelpParam">
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code></a></dt>
 * <dd>
 * <p>
 * If your message value includes the special markup for contextual-help
 * injection (<code>&lt;contextual_help&gt;...&lt;/contextual_help&gt;</code>),
 * use the <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code>
 * tag inside the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag body, in
 * order, for each such markup pair. See the <a
 * href="#classicContextualHelpParam">
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code></a> tag
 * documentation below.
 * </p>
 * </dd>
 * 
 * <dt><a href="#classicGlobalHelpParam">
 * <code>&lt;spf-i18n-portal:classicGlobalHelpParam&gt;</code></a></dt>
 * <dd>
 * <p>
 * If your message value includes the special markup for global-help injection (
 * <code>&lt;global_help&gt;...&lt;/global_help&gt;</code>), use the
 * <code>&lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</code> tag inside
 * the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag body, in order, for
 * each such markup pair. See the <a href="#classicGlobalHelpParam">
 * <code>&lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</code></a> tag
 * documentation below.
 * </p>
 * </dd>
 * </dl>
 * 
 * <hr>
 * <a name="param">
 * <h3>&lt;spf-i18n-portal:i18nParam&gt;</h3> </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:i18nParam value=&quot;&lt;string&gt;&quot; /&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * You use this tag inside the <a href="#message">
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code></a> tag body, to pass a string
 * parameter into any {@link java.text.MessageFormat} placeholders in the
 * message value. For example, if your message in your current component's
 * resource bundle is like this:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * key=Your name is {0} and your age is {1}.
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Then use the following in your JSP to express the fully-interpolated message
 * value from the current component's resource bundle (where <code>name</code>
 * contains the user name, and <code>age</code> contains the age):
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:i18nValue key=&quot;key&quot;&gt;
 * 	&lt;spf-i18n-portal:i18nParam value=&quot;&lt;%= name %&gt;&quot; /&gt;
 * 	&lt;spf-i18n-portal:i18nParam value=&quot;&lt;%= age %&gt;&quot; /&gt;
 * &lt;/spf-i18n-portal:i18nValue&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Here are the tag attributes:
 * </p>
 * 
 * <dl>
 * <dt><code>value="<i>string</i>"</code></dt>
 * <dd>
 * <p>
 * The string to substitute for this position in the message.
 * </p>
 * </dd>
 * </dl>
 * 
 * <p>
 * A single message may contain multiple {@link java.text.MessageFormat}
 * parameter placeholders, labeled <code>{0}</code>, <code>{1}</code>, etc as
 * shown in the above example. You just provide one
 * <code>&lt;spf-i18n-portal:i18nParam&gt;</code> tag for each one, in order.
 * Any extra tags you provide go unused. Conversely, any placeholders in the
 * message, for which you did not provide a
 * <code>&lt;spf-i18n-portal:i18nParam&gt;</code>, remain.
 * </p>
 * 
 * <hr>
 * <a name="classicContextualHelpParam">
 * <h3>
 * &lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</h3> </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:i18nClassicContextualHelpParam
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
 * You use this tag inside the <a href="#message">
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code></a> tag body, to inject the
 * "classic" rendition of contextual help into the message value. (If you don't
 * know what is meant by "contextual help" or the "classic" rendition or
 * "inject", see the discussion in the SPF Portal Utilities Developer's Guide.)
 * </p>
 * 
 * <p>
 * For example, let's say you need to produce a UI message like this (where the
 * word "secure" links to some classic contextual help not shown yet):
 * </p>
 * 
 * <img src="../doc-files/contextualHelpLink.jpg">
 * 
 * <p>
 * Using contextual help injection, you can have the following in your
 * component's message properties:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * key=Your sign-in is &lt;contextual_help&gt;secure&lt;/contextual_help&gt;
 * key.help.title=Service Portal and your security
 * key.help.content=Internet security is important to us...
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Then use the following in your JSP to express this message with the
 * appropriate contextual help markup (text, HTML, CSS and JavaScript) injected
 * into it. Note: this demonstration expresses the contextual help using the
 * built-in "default" style; see the discussion on tag attributes for how to
 * override that and provide your own style.
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:i18nValue key=&quot;key&quot;&gt;
 * 	&lt;spf-i18n-portal:i18nClassicContextualHelpParam titleKey=&quot;key.help.title&quot; contentKey=&quot;key.help.content&quot; /&gt;
 * &lt;/spf-i18n-portal:i18nValue&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * So then, when the user clicked on the "secure" hyperlink, it would look like
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
 * to retrieve the title (using the key itself as the default, if the message is
 * not found). The message lookup proceeds as described <a
 * href="#message">above</a>.
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
 * bundle to retrieve the main help content (using the key itself as the
 * default, if the message is not found). The message lookup proceeds as
 * described <a href="#message">above</a>.
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
 * </dl>
 * 
 * <p>
 * A single message may contain multiple occurrences of
 * <code>&lt;contextual_help&gt;...&lt;/contextual_help&gt;</code> markup. You
 * just provide one
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag for
 * each one, in order. Any extra tags you provide go unused. Conversely, any
 * <code>&lt;contextual_help&gt;...&lt;/contextual_help&gt;</code> markup in the
 * message, for which you did not provide a
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code>, is
 * removed (leaving the contents behind).
 * </p>
 * <blockquote>
 * <p>
 * <b>Note:</b> Due to current limitations, you cannot inject both contextual
 * help and global help into the same message.
 * </p>
 * </blockquote>
 * 
 * <p>
 * Injecting contextual help into a message is useful because it lets you embed
 * contextual help in the middle of a message (ie in the middle of a phrase or
 * sentence), without having to break the message apart and risk making it
 * untranslatable. Note that if you do have contextual help to wrap around a
 * whole message (or around some other element, like an image), then you may be
 * more interested in using the standalone contextual help approach, instead of
 * contextual help injection. For example,
 * <code>&lt;spf-help-portal:classicContextualHelp&gt;</code> is an SPF JSP tag
 * for expressing standalone contextual help in the "classic" manner. See the
 * documentation.
 * </p>
 * 
 * <blockquote>
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
 * </blockquote>
 * 
 * <hr>
 * <a name="classicGlobalHelpParam">
 * <h3>
 * &lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</h3> </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:i18nClassicGlobalHelpParam
 * 	fragment=&quot;&lt;fragment-name&gt;&quot;
 * 	windowFeatures=&quot;&lt;popup-window-features&gt;&quot;
 * 	/&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * You use this tag inside the <a href="#message">
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code></a> tag body, to inject a
 * hyperlink into the message value which points to the "classic" rendition of
 * the global help secondary page. If you don't know what is meant by
 * "global help" or the "classic" rendition or "inject", see the discussion in
 * the SPF Portal Utilities Developer's Guide.
 * </p>
 * <blockquote>
 * <p>
 * <b>Note:</b> Your portal site must implement a global help secondary page
 * instance (template friendly ID <code>PUBLIC_SPF_GLOBAL_HELP</code>) for this
 * to work.
 * </p>
 * </blockquote>
 * <p>
 * For example, let's say you need to produce a UI message in your portal
 * component looking like this (where "browser requirements" is a hyperlink
 * pointing to the global help secondary page rendered within a classic-style
 * popup window):
 * </p>
 * <blockquote>
 * <code>Read about <font color="blue"><u>browser requirements</u></font> for this site</code>
 * </blockquote>
 * <p>
 * Using global help injection, you can have the following in your component's
 * message properties:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * key=Read about &lt;global_help&gt;browser requirements&lt;/global_help&gt; for this site
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Then use the following in your JSP to express this message with the
 * appropriate markup (text, HTML, CSS, and JavaScript) injected into it for
 * rendering the classic-style global help popup when the "browser requirements"
 * link is clicked.
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:i18nValue key=&quot;key&quot;&gt;
 * 	&lt;spf-i18n-portal:i18nClassicGlobalHelpParam fragment=&quot;browser_reqs&quot; /&gt;
 * &lt;/spf-i18n-portal:i18nValue&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Note this example assumes use of the default, "classic" style window features
 * for the popup. It also assumes the existence of a document fragment,
 * <code>#brower_reqs</code>, which the global help secondary page will jump to
 * when the popup opens. Thus global help content can contain a section about
 * browser requirements marked with that fragment name, such that clicking the
 * "browser requirements" link in the message will open the popup, fill it with
 * the global help content, and jump to that portion of the content.
 * </p>
 * <p>
 * The global help popup requires JavaScript to function. In an unscripted
 * browser, clicking the "browser requirements" link would open the same global
 * help secondary page, but in the same browser window.
 * </p>
 * 
 * <p>
 * Here are the tag attributes:
 * </p>
 * 
 * <dl>
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
 * </p>
 * <ul>
 * <li>410 pixels by 610 pixels (width by height)</li>
 * <li>no menu bar</li>
 * <li>no tool bar</li>
 * <li>no status bar</li>
 * <li>resizeable</li>
 * </ul>
 * </p></dd>
 * </dl>
 * 
 * <p>
 * A single message may contain multiple occurrences of
 * <code>&lt;global_help&gt;...&lt;/global_help&gt;</code> markup. You just
 * provide one <code>&lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</code>
 * tag for each one, in order. Any extra tags you provide go unused. Conversely,
 * any <code>&lt;global_help&gt;...&lt;/global_help&gt;</code> markup in the
 * message, for which you did not provide a
 * <code>&lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</code>, is removed
 * (leaving the contents behind).
 * </p>
 * <blockquote>
 * <p>
 * <b>Note:</b> Due to current limitations, you cannot inject both contextual
 * help and global help into the same message.
 * </p>
 * </blockquote>
 * 
 * <p>
 * Injecting "classic"-style global help into a message is useful because it
 * lets you embed a global help popup in the middle of a message (ie in the
 * middle of a phrase or sentence), without having to break the message apart
 * and risk making it untranslatable. Note that if you wish to wrap a global
 * help popup hyperlink around a whole message (or around some other element,
 * like an image), then you may be more interested in using the standalone
 * global help approach, instead of global help injection. For example,
 * <code>&lt;spf-help-portal:classicGlobalHelp&gt;</code> is an SPF JSP tag for
 * expressing standalone global help in the "classic" popup manner. See the
 * documentation.
 * </p>
 * 
 * <hr>
 * <a name="localizedFileURL">
 * <h3>&lt;spf-i18n-portal:localizedFileURL&gt;</h3>
 * </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:localizedFileURL
 * 	file=&quot;&lt;base-file&gt;&quot;
 * 	fileKey=&quot;&lt;localized-file-message-key&gt;&quot;
 * 	/&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * You use this tag to express a URL for a supporting static resource file
 * (image, HTML, PDF, etc), where the URL is automatically adjusted to point to
 * the best-fit localized version of that resource. The resulting URL is
 * properly encoded and ready to present to the user's browser, such as in the
 * <code>SRC</code> attribute of an HTML <code>&lt;IMG&gt;</code> tag.
 * </p>
 * 
 * <p>
 * The resource file itself must be a secondary support file inside the current
 * portal component. As a secondary support file, it must be in the
 * <code>WEB-INF/</code> folder of the component. The file may be any static
 * resource, such as an image, video, PDF, etc. It may be a single file, or a
 * bundle of localized files sharing the same basename. In the latter case,
 * except for the base file itself, each file in the bundle should be tagged by
 * locale according to the Java-standard for {@link java.util.ResourceBundle}.
 * </p>
 * 
 * <p>
 * For example, say you have a bundle of <code>picture.jpg</code> files
 * (including the base file, <code>picture.jpg</code>, and some localized copies
 * of it: <code>picture_es_MX.jpg</code>, <code>picture_zh_CN.jpg</code>, etc).
 * Imagine you have put them into the <code>WEB-INF/</code> folder of your
 * portal component. Then to display the best-fit version of the image for the
 * user's locale in the browser, use the following in your JSP:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;img src='&lt;spf-i18n-portal:localizedFileURL file=&quot;picture.jpg&quot; /&gt;'&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Here are the tag attributes:
 * </p>
 * 
 * <dl>
 * <dt><code>file="<i>base-file</i>"</code><br>
 * <code>fileKey="<i>localized-file-message-key</i>"</code></dt>
 * <dd>
 * <p>
 * Use one of these attributes to identity the file for which to build the URL.
 * The <code>file</code> attribute gives the base filename to look for
 * (including any subfolder relative to the <code>WEB-INF/</code> of your
 * component). If no bundle for that base file is found, then a URL pointing to
 * the base file inside your component will be built and returned anyway. (This
 * will generate a 404 error when opened by the browser, so that during testing,
 * any missing resources will be obvious.)
 * <p>
 * The <code>fileKey</code> attribute works differently: with that attribute,
 * you give the key for a message property whose value is the actual localized
 * file name. (This message lookup proceeds as described <a
 * href="#message">above</a>. If the message is not found, this tag just
 * expresses an empty string.) Once the value is retrieved, it is treated as the
 * name of the actual localized file to use (including any subfolder relative to
 * your component's <code>WEB-INF/</code> folder). As above, if the file is not
 * found, then a URL pointing to it inside your component will be built and
 * returned anyway. (This will generate a 404 error when opened by the browser,
 * so that during testing, any missing resources will be obvious.)
 * </p>
 * 
 * <p>
 * Using <code>fileKey</code> requires you and the translators to setup all the
 * values for this message key, in all the localized versions of the message
 * properties file, to point to the proper filename. This can be rather
 * error-prone, so we recommend using the <code>file</code> attribute instead.
 * </p>
 * 
 * <p>
 * You must provide one of these attributes, or there will be an error. If you
 * provide both, the <code>file</code> attribute takes precedence.
 * </p>
 * </dd>
 * </dl>
 * 
 * <hr>
 * <a name="indicator">
 * <h3>&lt;spf-i18n-portal:classicLocaleIndicator&gt;</h3>
 * </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:classicLocaleIndicator
 *    order=&quot;&lt;spec&gt;&quot;
 *    displayInLocale=&quot;&lt;language-tag&gt;&quot;
 *    /&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Use this tag to express a "classic" HPWeb-compliant locale indicator string.
 * This is just a simple text string displaying the user's current resolved
 * locale, localized for that locale. See <a href=
 * "https://h10014.www1.hp.com/hpweb/newstandards/definitions/country.html"
 * >https://h10014.www1.hp.com/hpweb/newstandards/definitions/country.html</a>
 * for a description of this standard.
 * </p>
 * <p>
 * For example, when the current resolved locale is <code>de-DE</code> (German
 * for Germany), the tag expresses the string <code>Deutschland-Deutsch</code>
 * by default, which complies with the HPWeb standard. Using the attributes, you
 * can reverse the name display order and/or alter the localization of the
 * emitted string; for example, the following expresses
 * <code>German-Germany</code>:
 * </p>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:classicLocaleIndicator
 *    order=&quot;language-country&quot;
 *    displayInLocale=&quot;en&quot;
 *    /&gt;
 * </pre>
 * 
 * Here are the tag attributes: </p>
 * 
 * <dl>
 * <dt><code>order="<i>spec</i>"</code></dt>
 * <dd>
 * <p>
 * By default the expression of a full (ie country-specific) locale places the
 * country name first, and language name second. Use
 * <code>order="language-country"</code> to reverse this order.
 * <code>order="country-language"</code> explicitly yields the default behavior.
 * </p>
 * </dd>
 * <dt><code>displayInLocale="<i>language-tag</i>"</code></dt>
 * <dd>
 * <p>
 * By default the locale name is expressed in its own native language (ie, the
 * language of the current locale). However you can specify an alternate
 * language explicitly, by passing its language tag with this attribute. The
 * language tag must be an RFC 3066 format tag - for example,
 * <code>displayInLocale="en"</code> for English,
 * <code>displayInLocale="en-US"</code> for US English, etc.
 * <code>displayInLocale="current"</code> is an explicit way of yielding the
 * default behavior.
 * </p>
 * </dd>
 * </dl>
 * 
 * <hr>
 * <a name="selector">
 * <h3>&lt;spf-i18n-portal:classicLocaleSelector&gt;</h3>
 * </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:classicLocaleSelector
 *    label=&quot;&lt;string&gt;&quot;
 *    labelKey=&quot;&lt;message-key&gt;&quot;
 *    order=&quot;&lt;spec&gt;&quot;
 *    displayInLocale=&quot;&lt;language-tag&gt;&quot;
 *    sortInLocale=&quot;&lt;language-tag&gt;&quot;
 *    labelStyle=&quot;&lt;inline-style&gt;&quot;
 *    labelClass=&quot;&lt;css-classname&gt;&quot;
 *    listStyle=&quot;&lt;inline-style&gt;&quot;
 *    listClass=&quot;&lt;css-classname&gt;&quot;
 *    escape=&quot;&lt;true-or-false&gt;&quot;
 *    filterSpan=&quot;&lt;true-or-false&gt;&quot;
 *    /&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Use this tag to express a "classic" HPWeb-compliant locale selector form,
 * including the label, the pull-down menu of available locales, and a form
 * action which points to the SPF-provided secondary page process action for
 * persisting a locale selection in the SPF-standard manner. See <a href=
 * "https://h10014.www1.hp.com/hpweb/newstandards/definitions/country.html"
 * >https://h10014.www1.hp.com/hpweb/newstandards/definitions/country.html</a>
 * for a description of the look-and-feel standard.
 * </p>
 * <p>
 * The list of available locales used to populate the selector is taken from
 * {@link com.hp.it.spf.xa.i18n.portal.I18nUtility#getAvailableLocales(javax.servlet.http.HttpServletRequest)}
 * . Basically this is the default locale for your portal site, plus any
 * additional locales which you listed for your portal site in the
 * <code>site_locale_support.properties</code> file. This properties file is
 * typically stored on the portal server's filesystem in the global resources
 * directory (eg <code>/opt/sasuapps/sp/global_resources/</code> in production),
 * though it can be stored in any directory listed in the portal server's
 * classpath. The format of the file is:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * &lt;site-name&gt; = &lt;language-tag&gt;, &lt;language-tag&gt;, ...
 * </pre>
 * 
 * </blockquote>
 * <p>
 * The <i>site-name</i> is your Vignette "site DNS name", and each
 * <i>language-tag</i> is an RFC 3066 style string representing an ISO 639-1
 * language code and, optionally, an ISO 3166-1 country code as well. For
 * example:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * my_site = zh-CN, ja, fr
 * </pre>
 * 
 * </blockquote>
 * <p>
 * indicating that China Chinese, Japanese and French are the additional,
 * non-default locales for the <code>my_site</code> portal site. (As for the
 * default locale, that is configured through the Vignette site console in the
 * usual manner. Vignette does not natively support the concept of different
 * sets of locales for different portal sites, and thus
 * <code>site_locale_support.properties</code> is an SPF-provided extension to
 * the native Vignette capability.) These 3, together with the site default
 * locale, would be the options listed in the locale selector expressed by this
 * tag.
 * </p>
 * <blockquote>
 * <p>
 * <b>Note:</b> Only locales registered with Vignette at the server level are
 * recognized in <code>site_locale_support.properties</code> - any others are
 * ignored. Duplicate locales in <code>site_locale_support.properties</code> are
 * ignored too. You do not have to list the default locale in
 * <code>site_locale_support.properties</code> as the tag will automatically get
 * that from Vignette; if you list it in the property file anyway, it will be
 * recognized as duplicate and ignored. Note that changes to
 * <code>site_locale_support.properties</code> take effect in a running portal
 * environment within 15 minutes, with no restart required.
 * </p>
 * </blockquote>
 * <p>
 * For example, consider the case where the site default locale (in Vignette
 * site console) for <code>my_site</code> is US English, and
 * <code>site_locale_support.properties</code> contains the following:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * my_site = zh-CN, zh-TW, zh-HK, ja-JP, ko-KR
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Also imagine that your component's message resource bundle contains a message
 * like this (in the base file):
 * </p>
 * <blockquote>
 * 
 * <pre>
 * localeSelector.label = Language:
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Now imagine that we want to generate a locale selector using the above
 * message string (localized for the user's current locale) as the label for the
 * pull-down widget. We also want each locale named in the pull-down list to be
 * rendered in its own native language, but we want them to be sorted in order
 * of the current locale. (In reality, you would probably want the options to be
 * <i>either</i> both localized and sorted in the current locale, <i>or</i> each
 * localized in its own language and then all sorted together cross-locale. So
 * this is not necessarily a realistic example, but helps to demonstrate use of
 * the tag.) The following JSP code in your component JSP would satisfy these
 * requirements:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-i18n-portal&quot; uri=&quot;/spf-i18n-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-i18n-portal:classicLocaleSelector
 *    labelKey=&quot;localeSelector.label&quot;
 *    displayInLocale=&quot;various&quot;
 *    sortInLocale=&quot;current&quot;
 *    /&gt;
 * </pre>
 * 
 * </blockquote>
 * <p>
 * and the resulting locale selector would render as follows (this is when the
 * user's current locale is US English; the label string and sort order would
 * differ in other locales, though each displayed locale name in the select list
 * would continue to be rendered in its own language):
 * </p>
 * <img src="../doc-files/classicLocaleSelector.jpg">
 * <p>
 * Note that the current locale is pre-selected in the option list, as
 * illustrated above with US-English in the example. Note also that submit image
 * points at the companion <i>locale selector secondary page</i> assumed to
 * exist in your portal site. This Vignette secondary page is a process-only
 * action which, when the user clicks on the submit image:
 * </p>
 * <ol>
 * <li>collects the user selection from the form,</li>
 * <li>sets the HP.com-standard <code>lang</code> cookie (and <code>cc</code>
 * cookie as well, in the case of a country-specific locale),</li>
 * <li>uses HP Passport Web Services to write the language back into the current
 * user's profile in the HPP database (only if the user is currently logged-in
 * to HPP - if for some reason this writeback fails, an error is logged to the
 * Vignette log but no error is presented in the UI), and</li>
 * <li>redirects to the same page URL used by the user to access the page in
 * which you rendered the selector widget in the first place.</li>
 * </ol>
 * <p>
 * The name of the secondary page is <b>Shared Portal Framework - Locale
 * Selector Secondary Page</b> (component ID
 * <code>spf-locale-selector-secondarypage</code>) and it is an instance of a
 * secondary page type of the same name (component ID
 * <code>spf-locale-selector-secondarypagetype</code>). SPF provides these and
 * they exist in every SPF Vignette portal environment, but they need to have
 * been configured to work properly:
 * </p>
 * <ol>
 * <li>The SPF locale selector secondary page must have been configured as the
 * default secondary page to use for the SPF locale selector secondary page
 * type, in the Vignette server console.</li>
 * <li>In addition, the secondary page must have been shared to your portal site
 * by the Vignette server administrator.</li>
 * <li>And lastly, in Vignette site console for your site, you should have again
 * configured the SPF locale selector secondary page as the instance to use for
 * the secondary page type. Also you should have applied your site's grid and
 * theme to it. (You do these steps in <b>Site Settings &gt; Appearance &gt;
 * Secondary Pages</b>.)</li>
 * </ol>
 * <p>
 * <p>
 * This tag is just a wrapper around the
 * {@link com.hp.it.spf.xa.i18n.portal.ClassicLocaleSelectorProvider} Java
 * class. Most functionality of the Java API can be performed via this tag. The
 * tag attributes are:
 * </p>
 * <dl>
 * <dt><code>label="<i>string</i>"</code><br>
 * <code>labelKey="<i>message-key</i>"</code></dt>
 * <dd>
 * <p>
 * Use one of these attributes to provide a label for the pull-down menu. The
 * <code>label</code> attribute lets you provide the label string directly.
 * Alternatively, the <code>labelKey</code> attribute lets you provide the key
 * for a message property containing the label string. This key is used to
 * lookup the label string from your component's message resource bundle as
 * described <a href="#message">above</a>.
 * </p>
 * <p>
 * The HPWeb "classic" standard for a locale selector always is supposed to
 * include a label of some kind. Thus it is an error if a non-blank label string
 * is not provided to the tag through one or the other of these attributes (eg
 * you do not provide either attribute, or you provide the <code>labelKey</code>
 * attribute but your message is not found or blank). If for some reason you
 * provide both attributes, the <code>label</code> attribute takes precedence.
 * </p>
 * 
 * <blockquote>
 * <p>
 * <b>Tip 1:</b> Your label can contain HTML markup if desired (but see tip 2
 * below - and for applying CSS to your label, see the <code>labelStyle</code>
 * and/or <code>labelClass</code> attributes instead). Conversely, if you need
 * to use HTML-sensitive characters in your label string/message (like the &lt;
 * character) and do not wish that to be interpreted by the browser as HTML
 * markup, then you need to use the <code>escape="true"</code> attribute as well
 * - see below.
 * </p>
 * <p>
 * <b>Tip 2:</b> <font color="red"><b>Coming soon:</b></font> For accessibility
 * purposes, your label is not only expressed by this JSP tag - it is also
 * marked explicitly as a label for the selector widget, using the HTML-standard
 * <code>&lt;label for="..."&gt;</code> tag. So choose a label which will make
 * sense for accessibility purposes as well as for visible display.
 * </p>
 * <p>
 * <b>Tip 3:</b> If you need to render a classic locale selector without a
 * visible label, use
 * {@link com.hp.it.spf.xa.i18n.portal.ClassicLocaleSelectorProvider} directly.
 * It lets you do that although the tag does not.
 * </p>
 * </blockquote>
 * 
 * <dt><code>order="<i>spec</i>"</code></dt>
 * <dd>
 * <p>
 * By default, each full (ie country-specific) locale in the selector list will
 * be displayed country name first, and language name second (as per the HPWeb
 * "classic" standard). This is also the order used for sorting. Use
 * <code>order="language-country"</code> to reverse this order.
 * <code>order="country-language"</code> explicitly yields the default behavior.
 * Regardless, the sort order of locales in the list is always ascending.
 * </p>
 * </dd>
 * 
 * <dt><code>displayInLocale="<i>language-tag</i>"</code></dt>
 * <dd>
 * <p>
 * By default, each locale in the selector list is expressed in the language of
 * the current locale (as per the HPWeb "classic" standard). However you can
 * specify an alternate language explicitly, by passing its language tag with
 * this attribute. The language tag must be an RFC 3066 format tag - for
 * example, <code>displayInLocale="en"</code> for English,
 * <code>displayInLocale="en-US"</code> for US English, etc.
 * <code>displayInLocale="various"</code> is a special value requesting that
 * each locale be expressed in its own native language.
 * <code>displayInLocale="current"</code> is another special value, which just
 * yields the default behavior.
 * </p>
 * </dd>
 * 
 * <dt><code>sortInLocale="<i>language-tag</i>"</code></dt>
 * <dd>
 * <p>
 * Locale options in the selector list are sorted by locale name, where the
 * locale name is localized according to this attribute. By default, the current
 * locale is used for sorting (as per the HPWeb "classic" standard). However you
 * can specify an alternate language explicitly, by passing its language tag
 * with this attribute. The language tag must be an RFC 3066 format tag - for
 * example, <code>sortInLocale="en"</code> for English,
 * <code>sortInLocale="en-US"</code> for US English, etc.
 * <code>sortInLocale="current"</code> is another special value, which just
 * yields the default behavior. Regardless, the sort order of locales is always
 * ascending.
 * </p>
 * <p>
 * <font color="red"><b>Coming soon:</b></font>
 * <code>sortInLocale="various"</code> is a special value requesting that each
 * locale be localized into its own native language for purposes of sorting (the
 * resulting sort thus transcends any one locale, and is conducted in the
 * Unicode character collating sequence).
 * </p>
 * </dd>
 * 
 * <dt><code>labelStyle="<i>inline-style</i>"</code><br>
 * <code>labelClass="<i>css-classname</i>"</code></dt>
 * <dd>
 * <p>
 * By default, no explicit CSS styling is applied to the label you provide. You
 * can use either of these attributes to provide an explicit style instead.
 * </p>
 * <p>
 * The <code>labelStyle</code> attribute works like the <code>style</code>
 * attribute of most HTML tags - it lets you provide a set of CSS properties
 * inline with your tag. The <code>labelClass</code> attribute works like the
 * <code>class</code> attribute of most HTML tags - it lets you refer to a CSS
 * class defined elsewhere in your JSP or in an included CSS sheet. Typically
 * you would use only one or the other, though the tag will let you provide
 * both. If you do use both, then CSS properties defined in
 * <code>labelStyle</code> override the same properties defined in the
 * <code>labelClass</code> class, as usual with CSS. Similarly, CSS properties
 * defined in either override any defined elsewhere on the page so far as the
 * label is concerned.
 * </p>
 * </dd>
 * 
 * <dt><code>listStyle="<i>inline-style</i>"</code><br>
 * <code>listClass="<i>css-classname</i>"</code></dt>
 * <dd>
 * <p>
 * The HPWeb "classic" locale selector is basically an HTML &lt;select&gt;
 * widget.By default, no explicit CSS styling is applied to that &lt;select&gt;
 * widget. You can use either of these attributes to provide an explicit style
 * instead.
 * </p>
 * 
 * <p>
 * The <code>listStyle</code> attribute works like the <code>style</code>
 * attribute of most HTML tags - it lets you provide a set of CSS properties
 * inline with your tag. The <code>listClass</code> attribute works like the
 * <code>class</code> attribute of most HTML tags - it lets you refer to a CSS
 * class defined elsewhere in your JSP or in an included CSS sheet. Typically
 * you would use only one or the other, though the tag will let you provide
 * both. If you do use both, then CSS properties defined in
 * <code>listStyle</code> override the same properties defined in the
 * <code>listClass</code> class, as usual with CSS. Similarly, CSS properties
 * defined in either override any defined elsewhere on the page for tables.
 * </p>
 * 
 * <p>
 * The CSS properties you can define are the ones which are relevant to
 * &lt;select&gt; widgets. You can set <code>listStyle=""</code> to cancel any
 * default style.
 * </p>
 * </dd>
 * 
 * <dt><code>escape="<i>true-or-false</i>"</code></dt>
 * <dd>
 * <p>
 * By default, your label content is expressed as-is, which means that any
 * HTML-sensitive characters in it (like &;lt; or &gt; or etc) are treated as
 * HTML. If you want them to display literally instead, use
 * <code>escape="true"</code>. The default behavior can explicitly be invoked
 * using <code>escape="false"</code>.
 * </p>
 * </dd>
 * 
 * <dt><code>filterSpan="<i>true-or-false</i>"</code></dt>
 * <dd>
 * <p>
 * Similarly, the <code>filterSpan</code> attribute indicates whether to remove
 * any <code>&lt;SPAN&gt;</code> tags which Vignette may have inserted. Vignette
 * does this automatically, to help assistive devices such as readers. Normally
 * the <code>&lt;SPAN&gt;</code> tags are invisible, but just in case this
 * causes display problems in your portal site, the
 * <code>filterSpan="true"</code> attribute setting lets you suppress those
 * <code>&lt;SPAN&gt;</code> tags. By default they are not suppressed.
 * </p>
 * </dd>
 * </dl>
 * 
 */
package com.hp.it.spf.xa.i18n.portal.tag;


/**
 * <p>
 * This package contains JSP tag classes and tag documentation for interpolating
 * file content in Vignette portal components in Shared Portal Framework.
 * </p>
 * 
 * <blockquote>
 * <p>
 * <b>Note:</b> SPF portal component developers should never need to use the
 * Java classes in this package directly. Use the respective JSP tags instead.
 * For your reference, the tag classes and their respective JSP tags are listed
 * below:
 * </p>
 * 
 * <dl>
 * <dt>{@link com.hp.it.spf.xa.interpolate.portal.tag.FileInterpolatorTag} - Tag
 * class for <a href="#interpolate">
 * <code>&lt;spf-file-portal:interpolate&gt;</code></a></dt>
 * </dl>
 * </blockquote>
 * 
 * <a name="TLD"> <h3>spf-file-portal.tld</h3> </a>
 * <p>
 * The TLD file is <code>spf-file-portal.tld</code> and it is contained inside
 * the SPF portal utilities JAR, in the <code>META-INF/</code> folder. This JAR
 * is provided in the SPF Vignette environment. The TLD file's URI is
 * <code>/spf-file-portal.tld</code> so that is the URI you should refer to it
 * with, in your component's JSP's. Although you can use whatever JSP tag prefix
 * you like, the documentation here will use the prefix
 * <code>spf-file-portal</code>.
 * </p>
 * 
 * <a name="interpolate"> <h3>&lt;spf-file-portal:interpolate&gt;</h3> </a>
 * <p>
 * Usage:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-file-portal&quot; uri=&quot;/spf-file-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-file-portal:interpolate
 * 	file=&quot;&lt;i&gt;base-file&lt;/i&gt;&quot;
 * 	includeFile=&quot;&lt;i&gt;token-substitutions-file&lt;/i&gt;&quot;
 * 	/&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * You use this tag to interpolate a (possibly localized) file, which may
 * include some of the special markup tokens supported by
 * {@link com.hp.it.spf.xa.interpolate.portal.FileInterpolator}, and express the
 * interpolated content.
 * </p>
 * 
 * <p>
 * The file to be interpolated must be a <i>secondary support file</i> found in
 * your current Vignette portal component. As with all secondary support files,
 * it must reside in the <code>WEB-INF/</code> folder in your component archive
 * (CAR). The file may be any static text (not binary) file: HTML, JavaScript,
 * plain text, etc. The file encoding <b>must</b> be UTF-8.
 * </p>
 * 
 * <p>
 * The file to be interpolated may be part of a resource bundle of localized
 * versions of that file; all the localized files in the bundle should share the
 * same base filename, be UTF-8 encoded, and (except for the base file itself)
 * be tagged by locale according to the Java-standard for
 * {@link java.util.ResourceBundle}. All of these files must be placed as
 * secondary support files in the current component as well.
 * </p>
 * 
 * <p>
 * For example, say you have a bundle of <code>text.html</code> files (including
 * the base file, <code>text.html</code>, and some localized copies of it:
 * <code>text_zh_TW.html</code>, <code>text_zh_CN.html</code>, etc). Imagine you
 * have put them into your component's <code>WEB-INF/</code> as secondary
 * support files. Then to display in the browser the interpolated content of the
 * best-fit (for the current locale) file, use the following in your component
 * JSP:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * &lt;%@ taglib prefix=&quot;spf-file-portal&quot; uri=&quot;/spf-file-portal.tld&quot; %&gt;
 * ...
 * &lt;spf-file-portal:interpolate file=&quot;text.html&quot; /&gt;
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Here are the tag attributes:
 * </p>
 * 
 * <dl>
 * <dt><code>file="<i>base-file</i>"</code></dt>
 * <dd>
 * <p>
 * The <code>base-file</code> attribute gives the base filename to look for
 * among the secondary support files in your component's <code>WEB-INF</code>
 * folder. Since Vignette defines that secondary support files are all contained
 * within that folder (no sub-folder), there should be no path in your
 * <i>base-file</i>, just the filename.
 * </p>
 * <p>
 * When you give the <code>base-file</code> attribute, the tag will search the
 * component's secondary support files, looking for the best-fit file for the
 * current locale, in the manner of {@link java.util.ResourceBundle}. If no
 * bundle for that base file is found, then an empty string will be expressed.
 * </p>
 * <p>
 * The <code>base-file</code> attribute is required, and there will be an error
 * if you do not provide it.
 * </p>
 * </dd>
 * <dt><code>includeFile="<i>token-substitutions-file</i>"</code></dt>
 * <dd>
 * <p>
 * The <code>includeFile</code> attribute is optional. It gives the name of a
 * token substitutions property file to use when processing any special
 * <code>{INCLUDE:<i>key</i>}</code> tokens in the file content. By default, a
 * token substitutions propery file named
 * <code>default_includess.properties</code> will be assumed. You do not need to
 * specify this attribute unless you are using <code>{INCLUDE:<i>key</i>}</code>
 * tokens in your file content.
 * </p>
 * 
 * <p>
 * If you do have a token substitutions file, put it into your current
 * component's <code>WEB-INF</code> folder as a secondary support file. As
 * defined by Vignette, there should be no path in the
 * <i>token-substitutions-file</i> since secondary support files always live
 * directly in that folder (no sub-folder).
 * </p>
 * 
 * <p>
 * A template for the token substitutions file, also named
 * <code>default_includes.properties</code>, is provided in the SPF common
 * utilities JAR. The format is just name-value pairs, where values may
 * reference other tokens in turn.
 * </p>
 * </dd>
 * </dl>
 */
package com.hp.it.spf.xa.interpolate.portal.tag;


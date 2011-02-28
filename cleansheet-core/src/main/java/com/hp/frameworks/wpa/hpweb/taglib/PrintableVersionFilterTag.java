package com.hp.frameworks.wpa.hpweb.taglib;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.oro.text.perl.Perl5Util;


/**
 * The PrintableVersionFilterTag tag filters its body content in accordance with
 * the HPWeb printable version rules. The body content of this tag is evaluated
 * and then a number of substitutions are performed to do the following before
 * it is written to the page.
 * <ul>
 * <li>&lt;input type="image"&gt; tags are replaced with plain &lt;img&gt; tags</li>
 * <li>All white fonts switched to black</li>
 * <li>All HPWeb theme colors are removed</li>
 * <li>All links are disabled</li>
 * <li>All blue text is changed to black</li>
 * <li>All Compaq theme colors are removed</li>
 * <li>All background colors are removed</li>
 * </ul>
 */
public class PrintableVersionFilterTag extends SimpleTagSupport
{

	// ------------------------------------------------------ Protected Members    
    
    
    /**
     * Reference to the regular expression engine.
     */
    protected Perl5Util regEx = new Perl5Util();
        
    
	// --------------------------------------------------------- Public Methods    
    
    
    public void doTag() throws JspException, IOException
    {        
        JspFragment bodyFragment = this.getJspBody();
     
        // Convert body fragment to string
        StringWriter bodyBuffer = new StringWriter();
        bodyFragment.invoke(bodyBuffer);
        String body = bodyBuffer.toString();
        
        // Retrieve JspWriter and write filtered output
        PageContext pageContext = (PageContext) this.getJspContext();
        JspWriter out = pageContext.getOut();
        
        out.print(this.filterContent(body));        
    }

    
	// --------------------------------------------------------- Public Methods  

    
    protected String filterContent(String body)
    {
        // HP.COM CGI - ExciseBlueText
        body = this.substitute(body, "s/class=[\"']?color003366[\"']\\s?/class=\"color000000\"/ig");
        body = this.substitute(body, "s/class=[\"']?color003366bld[\"']?/class=\"bold\"/ig");
        
        
        // HP.COM CGI - ExciseCompaqText
        // Replace red text with black
        body = this.substitute(body, "s/class=[\"']?color990000[\"']\\s?/class=\"color000000\"/ig");
        
        // Replace red bold with bold
        body = this.substitute(body, "s/class=[\"']?color990000bld[\"']?/class=\"bold\"/ig");
        
        // Replace red search class with underline
        body = this.substitute(body, "s/class=[\"']?cpqsrchopt[\"']?/class=\"udrline\"/ig");
        
        // Remove red link text 
        body = this.substitute(body, "s/class=[\"']?cpqRed[\"']\\s?//ig");
        
        // Replace red bold link class with bold
        body = this.substitute(body, "s/class=[\"']?cpqRedbold[\"']?/class=\"bold\"/ig");
        
        // Replace red breadcrumb with underline
        body = this.substitute(body, "s/class=[\"']?cpqBreadcrumb[\"']\\s?/class=\"udrline\"/ig");
        
        // Remove compaq white text
        body = this.substitute(body, "s/class=[\"']?cpqNavWhite[\"']\\s?//ig");
        
        // Replace red underline with underline
        body = this.substitute(body, "s/class=[\"']?cpqUdrline[\"']\\s?/class=\"udrline\"/ig");
        
        // Replace red underline bold with underline bold
        body = this.substitute(body, "s/class=[\"']?cpqUdrlinebold[\"']\\s?/class=\"udrlinebold\"/ig");                        
        
        
        // HP.COM CGI - ExciseLinks
        // Remove anything clickable
        body = this.substitute(body, "s/href/nohref/ig");
        body = this.substitute(body, "s/onclick/noonclick/ig");
        
        // Replace submit buttons with regular buttons
        body = this.substitute(body, "s/type=[\"']?submit[\"']?/type=\"button\"/ig");
        

        // HP.COM CGI - ExciseTheme
        // Replace themebodylink with underline
        body = this.substitute(body, "s/class=[\"']?themebodylink[\"']?/class=\"udrline\"/ig");
        
        // Remove themebody
        body = this.substitute(body, "s/class=[\"']?themebody[\"']?//ig");
        
        // Replace themeheaderlink with bold
        body = this.substitute(body, "s/class=[\"']?themeheaderlink[\"']?/class=\"bold\"/ig");
        
        // Replace themeheader with bold
        body = this.substitute(body, "s/class=[\"']?themeheader[\"']?/class=\"bold\"/ig");
        
        // Replace theme with colorFFFFFFbg
        body = this.substitute(body, "s/class=[\"']?theme[\"']?/class=\"colorFFFFFFbg\"/ig");
        
        // Remove color666666bg
        body = this.substitute(body, "s/class=[\"']?color666666bg[\"']?//ig");       
        
        // Replace leveld with bold
        body = this.substitute(body, "s/class=[\"']?leveld[\"']?/class=\"bold\"/ig");
                

        // HP.COM CGI - ReplaceInputImageWithImage
        body = this.substitute(body, "s/<INPUT\\s+[^>]*type=['\"]?image['\"]?[^>]*src=(\\S+)[^>]*>/<IMG src=$1 border=\"0\">/xgsi");
        body = this.substitute(body, "s/<INPUT\\s+[^>]*src=(\\S+)[^>]*type=['\"]?image['\"]?[^>]*>/<IMG src=$1 border=\"0\">/xgsi");
        
        
        // HP.COM CGI - ExciseBackgroundColors
        // Remove all background colors
        body = this.substitute(body, "s/bgcolor/nobgcolor/ig");
        
        // Hide corner images on tabs
        body = this.substitute(body, "s/\\w+tab_right_on.gif/s.gif/g");
        body = this.substitute(body, "s/\\w+tab_left_on.gif/s.gif/g");
        body = this.substitute(body, "s/\\w+tab_right_off.gif/s.gif/g");
        body = this.substitute(body, "s/\\w+tab_left_off.gif/s.gif/g");
        
        // Replace black background class with white
        body = this.substitute(body, "s/class=[\"']?color000000bg[\"']?/class=\"colorFFFFFFbg\"/ig");
        
        // Reinstate grey background E6E6E6
        body = this.substitute(body, "s/nobgcolor=[\"']?#E6E6E6[\"']?/bgcolor=\"#E6E6E6\"/ig");
        
        // Reinstate grey background E7E7E7
        body = this.substitute(body, "s/nobgcolor=[\"']?#E7E7E7[\"']?/bgcolor=\"#E7E7E7\"/ig");      
        
        // Reinstate table borders
        body = this.substitute(body, "s/nobgcolor=[\"']?#999999[\"']?/bgcolor=\"#999999\"/ig");

        // Reinstate grey background CCCCCC
        body = this.substitute(body, "s/nobgcolor=[\"']?#CCCCCC[\"']?/bgcolor=\"#CCCCCC\"/ig");
        
        // Reinstate white background FFFFFF
        body = this.substitute(body, "s/nobgcolor=[\"']?#FFFFFF[\"']?/bgcolor=\"#FFFFFF\"/ig");
        
        // Remove background colors for style-defined colors
        body = this.substitute(body, "s/background-color:#e7e7e7/nobackground-color:#e7e7e7/ig");
        body = this.substitute(body, "s/background-color:#f0f0f0/nobackground-color:#f0f0f0/ig");
        
        
        // HP.COM CGI - ExciseWhiteFonts
        // Remove any standalone white font tags
        body = this.substitute(body, "s/<font\\s+color=\"#ffffff\">/<font color=\"#333399\">/gi");
        body = this.substitute(body, "s/<font\\s+color=#ffffff>/<font color=\"#000000\">/gi");
        body = this.substitute(body, "s/style=(.*)?color:#ffffff(.*)?>/style=$1color:#000000$2>/gi");
        
        // Replace white text with black text
        body = this.substitute(body, "s/class=[\"']?colorFFFFFF[\"']\\s?/class=\"color000000\"/ig");
        
        // Replace white bold class with bold
        body = this.substitute(body, "s/class=[\"']?colorFFFFFFbld[\"']?/class=\"bold\"/ig");
        body = this.substitute(body, "s/class=[\"']?whitebld[\"']?/class=\"bold\"/ig");
        
        // Replace colorFFFFFFsml class with black text
        body = this.substitute(body, "s/class=[\"']?colorFFFFFFsml[\"']?/class=\"color000000\"/ig");        
        
        return body;
    }
    
    
    /**
     * Applies the given regex substitution pattern to the input string.
     */
    protected String substitute(String input, String pattern)
    {
        return this.regEx.substitute(pattern, input);
    }    
}

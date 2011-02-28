package com.hp.frameworks.wpa.hpweb.taglib;


import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;


public class TabTag extends SimpleTagSupport
{
    
	// ------------------------------------------------------ Protected Members
    
    private static final String TABS_KEY = "com.hp.frameworks.wpa.hpweb.TABS";


    /**
     * The title string of the tab.
     */
    protected String title;


    /**
     * The URL to navigate to when the tab title is clicked.
     */
    protected String url;

    
    /**
     * The alt text to use on conjunction with the tab title.
     */
    protected String alt;
    

    /**
     * The body content of the tab.
     */
    protected String body;

    
	// --------------------------------------------------------- Public Methods
    
    public String getTitle()
    {
        return this.title;
    }
    
    
    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getUrl()
    {
        return this.url;
    }
    
    
    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getAlt()
    {
        return this.alt;
    }
    
    
    public void setAlt(String alt)
    {
        this.alt = alt;
    }
    
    
    public String getBody()
    {
        return this.body;
    }


    public void doTag() throws JspException, IOException
    {
        java.io.Writer writer = new java.io.StringWriter();
        
        if (this.getJspBody() != null)
        {
            this.getJspBody().invoke(writer);
        }

        this.body = writer.toString();
                
        List tabList = (List) this.getJspContext().getAttribute(TABS_KEY,
                PageContext.REQUEST_SCOPE);
        tabList.add(this);
    }
    
}

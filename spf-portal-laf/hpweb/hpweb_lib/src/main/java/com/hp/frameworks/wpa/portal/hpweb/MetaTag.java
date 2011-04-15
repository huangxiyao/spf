package com.hp.frameworks.wpa.portal.hpweb;

/**
 * This class represents HTML Meta tag.
 *
 * Comparing to the HTML tag it allows to specify <code>code</code> and one of
 * <code>name</code>, <code>http-equiv</code>, etc... but it does not support several of these
 * attributes as there are currently no use cases for that, and this would allow to keep
 * JSP pages in which they are used simpler.
 * <p>
 * Using this class the following meta tag can be built:
 * &lt;meta {type}="{typeValue}" content="{content}"&gt;
 * where values in curly braces reference the class attributes.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class MetaTag
{
    private String type;
    private String typeValue;
    private String content;

    public MetaTag() {}

    public MetaTag(String type, String typeValue, String content)
    {
        this.type = type;
        this.typeValue = typeValue;
        this.content = content;
    }

    /**
     * Returns the type of meta tag; if following HTML spec this would usually be one of: name,
     * http-equiv, scheme
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type of of meta tag.
     * @param type meta tag type; if following HTML spec this should be one of: name, http-equiv,
     * scheme.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Returns value specific for the tag {@link #getType()}, e.g. for type=name typeValue
     * could be author, description, keywords, etc...
     */
    public String getTypeValue()
    {
        return typeValue;
    }

    /**
     * Sets the value corresponding to the type of this tag.
     * @param typeValue value of the type, e.g. for type=http-equiv this could be content-type.
     *
     * @see #setType(String)
     */
    public void setTypeValue(String typeValue)
    {
        this.typeValue = typeValue;
    }

    /**
     * Returns the content of the meta tag
     */
    public String getContent()
    {
        return content;
    }

    /**
     * Sets the content of the meta tag.
     * @param content meta tag content
     */
    public void setContent(String content)
    {
        this.content = content;
    }
}

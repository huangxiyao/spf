package com.hp.frameworks.wpa.hpweb.taglib.functions;

import java.awt.ComponentOrientation;
import java.text.MessageFormat;
import java.util.Locale;

import javax.servlet.jsp.jstl.fmt.LocalizationContext;

/**
 * Container class for HPWeb-specific JSTL functions.
 */
public class Functions
{

    /**
     * Round the given number to nearest whole integer value.
     */
    public static long round(double value)
    {        
        return Math.round(value);
    }
    
        
    /**
     * Returns a flag indicating whether or not the locale used by the given
     * localization context uses a left-to-right writing system.
     */
    public static boolean isLocaleLeftToRight(LocalizationContext locCtxt)
    {
        boolean result = true;
        
        Locale locale = locCtxt.getLocale();
        
        if (locale != null)
        {
            ComponentOrientation co = ComponentOrientation.getOrientation(locale);
            
            if (!co.isLeftToRight())
            {
                result = false;
            }
        }
        
        return result;
    }
    
    
    /**
     * Returns the result of doing a MessageFormat.format against the supplied
     * pattern and param values.
     */
    public static String format(String pattern, String param)
    {
        return MessageFormat.format(pattern, new String[] { param });
    }
    
}

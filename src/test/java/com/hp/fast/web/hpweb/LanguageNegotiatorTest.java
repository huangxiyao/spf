package com.hp.fast.web.hpweb;

import java.util.ArrayList;
import java.util.Locale;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.hp.fast.web.http.NoAcceptableLanguageException;
import com.hp.fast.web.mock.MockLocaleProviderFactory;

import junit.framework.TestCase;

/**
 * Test the actual language negotiator we are using.
 * @author marc derosa
 */
public class LanguageNegotiatorTest extends TestCase {
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockLocaleProviderFactory localeProviderFactory;

    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        
        ArrayList supportedLocales = new ArrayList();
        supportedLocales.add(new Locale("de"));
        supportedLocales.add(new Locale("fr", "ca"));
        localeProviderFactory = new MockLocaleProviderFactory();
        localeProviderFactory.setLocales(supportedLocales);
    }
    
    /**
     * The accept-language header can contain multiple locales we want to make
     * sure that a locale is selected according to the priority in the 
     * accept-language header.
     *
     */
    public void testCorrectlyResolvesMultipleLocales() {
        LanguageNegotiator ln = new LanguageNegotiator();
        ln.setTargetLocaleProviderFactory(this.localeProviderFactory);
        request.addPreferredLocale(Locale.GERMAN);
        request.addPreferredLocale(Locale.CANADA_FRENCH);
        request.addPreferredLocale(Locale.JAPANESE);
        
        try {
            ln.negotiate(request, response);
            assertEquals(Locale.CANADA_FRENCH, ln.negotiatedValue(request));
            assertEquals(Locale.CANADA_FRENCH, response.getLocale());
            
        } catch (NoAcceptableLanguageException e) {
            fail("should have selected fr-cn");
        }
    }

    public void testAcceptLanguageMatchesProvidedLanguage()
            throws NoAcceptableLanguageException {
        LanguageNegotiator ln = new LanguageNegotiator();
        ln.setTargetLocaleProviderFactory(this.localeProviderFactory);
        request.addPreferredLocale(Locale.GERMAN);
        ln.negotiate(request, response);

        assertEquals(Locale.GERMAN, ln.negotiatedValue(request));
        assertEquals(Locale.GERMAN, response.getLocale());
    }

    public void testAcceptLanguageCountryMatchesProvidedLanguageCountry()
            throws NoAcceptableLanguageException {
        this.localeProviderFactory.addLocale(new Locale("fr"));
        LanguageNegotiator ln = new LanguageNegotiator();
        ln.setTargetLocaleProviderFactory(this.localeProviderFactory);
        
        request.addPreferredLocale(Locale.CANADA_FRENCH);
        ln.negotiate(request, response);

        assertEquals(Locale.CANADA_FRENCH, ln.negotiatedValue(request));
        assertEquals(Locale.CANADA_FRENCH, response.getLocale());
    }

    // msd: first step to make this compliant
    public void testAcceptLanguageMatchesProvidedLanguageCountry()
            throws NoAcceptableLanguageException {
        try {           
            LanguageNegotiator ln = new LanguageNegotiator();
            ln.setTargetLocaleProviderFactory(this.localeProviderFactory);
            
            request.addPreferredLocale(Locale.FRENCH);
            ln.negotiate(request, response);
            fail("we should not resolve a locale");
        } catch (NoAcceptableLanguageException e) {
            assertTrue(true);
        }
    }

    public void testAcceptLanguageCountryMatchesProvidedLanguage()
            throws NoAcceptableLanguageException {
        ArrayList supportedLocales = new ArrayList();
        supportedLocales.add(new Locale("fr"));
        supportedLocales.add(new Locale("de"));
        MockLocaleProviderFactory provider = new MockLocaleProviderFactory();
        provider.setLocales(supportedLocales);
        
        LanguageNegotiator ln = new LanguageNegotiator();
        ln.setTargetLocaleProviderFactory(provider);
        request.addPreferredLocale(Locale.CANADA_FRENCH);
        ln.negotiate(request, response);

        assertEquals(Locale.FRENCH, ln.negotiatedValue(request));
        assertEquals(Locale.FRENCH, response.getLocale());
    }

    public void testNoMatch() {
        ArrayList supportedLocales = new ArrayList();
        supportedLocales.add(new Locale("fr"));
        MockLocaleProviderFactory provider = new MockLocaleProviderFactory();
        provider.setLocales(supportedLocales);
        LanguageNegotiator ln = new LanguageNegotiator();
        ln.setTargetLocaleProviderFactory(provider);
        
        request.addPreferredLocale(Locale.GERMAN);
        try {
            ln.negotiate(request, response);
            fail("NoAcceptableLanguageException expected.");
        } catch (NoAcceptableLanguageException ex) {
            assertTrue(true);
        }

        assertNull(ln.negotiatedValue(request));
        assertEquals(new MockHttpServletResponse().getLocale(), response
                .getLocale());
    }

}

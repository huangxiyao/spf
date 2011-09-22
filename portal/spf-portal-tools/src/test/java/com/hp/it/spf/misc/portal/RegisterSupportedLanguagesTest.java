package com.hp.it.spf.misc.portal;

import org.junit.Test;

import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class RegisterSupportedLanguagesTest
{

    private RegisterSupportedLanguagesStub testee = new RegisterSupportedLanguagesStub();

    @Test
    public void allValueShouldBeIgnored() {
        Properties data = new Properties();
        data.put("spf", "ALL");

        testee.registerSupportedLanguages(data);

        assertThat(testee.registeredLanguages.isEmpty(), is(true));
    }

    @Test
    public void allSitesLocalesAreMergedIntoSingleSet() {
        Properties data = new Properties();
        data.put("spf", "ALL");
        data.put("hpsc", "en-US, fr-FR");
        data.put("cpc", "en-US, de");

        testee.registerSupportedLanguages(data);

        Set<Locale> expectedLanguages = new HashSet<Locale>() {{
            add(new Locale("en", "US"));
            add(new Locale("fr", "FR"));
            add(new Locale("de"));
        }};

        assertThat(testee.registeredLanguages, is(expectedLanguages));
    }

    private class RegisterSupportedLanguagesStub extends RegisterSupportedLanguages {
        private Set<Locale> registeredLanguages;

        @Override
        public void registerSupportedLanguages(Set<Locale> languages)
        {
            registeredLanguages = languages;
        }
    }
}


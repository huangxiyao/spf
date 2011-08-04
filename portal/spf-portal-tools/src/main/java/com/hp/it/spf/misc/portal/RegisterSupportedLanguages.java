package com.hp.it.spf.misc.portal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.epicentric.i18n.locale.LocaleManager;
import com.hp.it.spf.xa.i18n.I18nUtility;


/**
 * Utility class used to register, in Vignette Portal, support languages described in Properties
 * file. Vignette Portal uses term language to designate locale supported by the portal. This class
 * also uses the same naming convention.
 *
 * <p>The Properties file containing registration data looks as follows:</p>
 * <pre>
 * # HP Support Center Site
 * hpsc = ja-JP, en-US, es-BO, es-PE, es-CL, en-PH
 * 
 * # Carepack Central Site
 * cpc = ar-SA,zh-TW
 * </pre>
 *
 * @author Xu, Ke-Jun (Daniel,HPIT-GADSC) (ke-jun.xu@hp.com)
 */
public class RegisterSupportedLanguages
{

    public static void main(String[] args)
    {
        if (args.length == 0) {
            System.out.printf("java [options] %s %s%n", RegisterSupportedLanguages.class.getName(),
                    "path/to/site_locale_support.properties");
            System.exit(0);
        }

        File supportedLanguageDefinitionFile = new File(args[0]);
        if (!supportedLanguageDefinitionFile.isFile()) {
            System.err.printf("Data file not found: %s%n", supportedLanguageDefinitionFile.getAbsolutePath());
            System.exit(1);
        }

        Properties supportedLanguageDefinitionsData = null;
        try {
            supportedLanguageDefinitionsData = loadProperties(supportedLanguageDefinitionFile);
        }
        catch (Exception e) {
            System.err.printf("Error occurred while reading properties data file '%s': %s%n",
                    supportedLanguageDefinitionFile.getAbsolutePath(), e);
            System.exit(2);
        }

        try {
            RegisterSupportedLanguages registerTool = new RegisterSupportedLanguages() {
                // override registerSupportLanguage method so we can print diagnostic information
                // without tying the actual method implementation to use System.out which is ok
                // for command line usage as here.

                @Override
                public void registerSupportedLanguages(Set<Locale> languages)
                {
                    super.registerSupportedLanguages(languages);
                    System.out.println("Registered supported languages: " + languages);
                }

            };
            registerTool.registerSupportedLanguages(supportedLanguageDefinitionsData);
        }
        catch (Exception e) {
            System.err.printf("Error occurred while registering default languages: %s%n", e);
            e.printStackTrace();
            System.exit(3);
        }
    }

    private static Properties loadProperties(File supportedLanguageDefinitionFile) throws IOException
    {
        Properties supportedLanguageDefinitionsData = new Properties();
        FileInputStream in = new FileInputStream(supportedLanguageDefinitionFile);
        try {
            supportedLanguageDefinitionsData.load(in);
        }
        finally {
            in.close();
        }
        return supportedLanguageDefinitionsData;
    }

    public void registerSupportedLanguages(Properties supportedLanguageDefinitionsData)
    {
        Set<Locale> allLanguages = extractCombinedSetOfLanguagesForAllSites(supportedLanguageDefinitionsData);
        registerSupportedLanguages(allLanguages);
    }

    private Set<Locale> extractCombinedSetOfLanguagesForAllSites(Properties supportedLanguageDefinitionsData)
    {
        Set<Locale> allLanguages = new TreeSet<Locale>();

        Enumeration siteNames = supportedLanguageDefinitionsData.propertyNames();
        while (siteNames.hasMoreElements()) {
            String siteName = (String) siteNames.nextElement();
            String siteSupportedLanguages = ((String) supportedLanguageDefinitionsData.get(siteName)).trim();

            Set<Locale> siteSupportedLanguagesAsLocales = extractLanguages(siteSupportedLanguages);
            allLanguages.addAll(siteSupportedLanguagesAsLocales);
        }

        return allLanguages;
    }

    private Set<Locale> extractLanguages(String commaSeparatedLanguages)
    {
        if ("ALL".equalsIgnoreCase(commaSeparatedLanguages)) {
            // let's ignore ALL value
            return Collections.emptySet();
        }

        Set<Locale> result = new TreeSet<Locale>();

        // ',' as support languages delimiter
        String[] languages = commaSeparatedLanguages.split(",");

        for (String aSupportLanguage : languages) {
            Locale locale = I18nUtility.languageTagToLocale(aSupportLanguage);
            if (locale != null) {
                result.add(locale);
            }
        }

        return result;
    }

    public void registerSupportedLanguages(Set<Locale> languages) {
        LocaleManager lm = LocaleManager.getInstance();

        for (Locale locale : languages) {
            lm.addRegisteredLocale(locale);
        }
    }

}

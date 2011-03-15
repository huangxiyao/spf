package com.hp.it.spf.portal.cleansheet;

import java.util.Locale;

public class CountryLanguageLink {

	
	/**
	 * ??? is this a Country selector, language selector or locale selector?  It's confusing as only countries are listed in the selection. <p>ISO 639 language code.  Must include both the language and Country as part of the Locale.  (ie. en-US and fr-CA)</p>
	 */
	
	private Locale locale;

	/**
	 * A Locale must exist in one of three Regions, this must be &quot;Americas,&quot; &quot;EMEA,&quot; or &quot;AsiaPac.&quot;
	 */
	
	private Region region;

	
	/**
	 * Localized Country name displayed to user. <p>Note: All Countries are listed in the language that user will experience when they select it.  For instance Spain is listed as España and Vietnam is listed as Vietnam.  While the Vietnamese page may be in English the content is customized for Vietnamese customers.  </p> <p>Generally the list will display only the country.  For Countries with more than one language, the non-English languages will also include the language name.  For instance &quot;Canada&quot; and &quot;Canada - Français&quot;</p>
	 */
	
	private String countryLanguageName;

	
	/**
	 * This is the URL pointing to the appropriate localized page for the Country/Language selected.
	 */
	
	private String countryLanguageURL;
}

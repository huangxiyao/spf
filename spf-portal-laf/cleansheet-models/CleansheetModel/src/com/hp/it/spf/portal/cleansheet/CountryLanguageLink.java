package com.hp.it.spf.portal.cleansheet;

import java.util.Locale;

public class CountryLanguageLink {

	private Locale locale;

	/**
	 * A Locale must exist in one of three Regions, this must be &quot;Americas,&quot; &quot;EMEA,&quot; or &quot;AsiaPac.&quot;
	 */
	
	private Region region;

	private String countryLanguageName;

	private String countryLanguageURL;
}

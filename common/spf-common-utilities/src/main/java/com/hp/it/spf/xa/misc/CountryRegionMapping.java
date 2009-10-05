package com.hp.it.spf.xa.misc;

import java.util.Map;
import java.util.HashMap;

/**
 * 
 * A Country-Region Mapping class for Shared Portal Framework.
 * 
 * @author <link href="huakun.gao@hp.com">Gao, Hua-Kun</link>
 * @author <link href="ye.liu@hp.com">Liu, Ye</link>
 * @version TBD
 * @see others
 */
public class CountryRegionMapping {
    private static Map country_region_map = new HashMap(101);
    public static final String APJ = "APJ";
    public static final String AMERICAS = "Americas";
    public static final String EMEA = "EMEA";
    
    static {
        country_region_map.put("AF", APJ); // AFGHANISTAN
        country_region_map.put("AX", EMEA); // ALAND ISLANDS
        country_region_map.put("AL", EMEA); // ALBANIA
        country_region_map.put("DZ", EMEA); // ALGERIA
        country_region_map.put("AS", APJ); // AMERICAN SAMOA
        country_region_map.put("AD", EMEA); // ANDORRA
        country_region_map.put("AO", EMEA); // ANGOLA
        country_region_map.put("AI", AMERICAS); // ANGUILLA
        country_region_map.put("AQ", APJ); // ANTARCTICA
        country_region_map.put("AG", AMERICAS); // ANTIGUA AND BARBUDA
        country_region_map.put("AR", AMERICAS); // ARGENTINA
        country_region_map.put("AM", APJ); // ARMENIA
        country_region_map.put("AW", AMERICAS); // ARUBA
        country_region_map.put("AU", APJ); // AUSTRALIA
        country_region_map.put("AT", EMEA); // AUSTRIA
        country_region_map.put("AZ", APJ); // AZERBAIJAN
        country_region_map.put("BS", AMERICAS); // BAHAMAS
        country_region_map.put("BH", APJ); // BAHRAIN
        country_region_map.put("BD", APJ); // BANGLADESH
        country_region_map.put("BB", AMERICAS); // BARBADOS
        country_region_map.put("BY", EMEA); // BELARUS
        country_region_map.put("BE", EMEA); // BELGIUM
        country_region_map.put("BZ", AMERICAS); // BELIZE
        country_region_map.put("BJ", EMEA); // BENIN
        country_region_map.put("BM", AMERICAS); // BERMUDA
        country_region_map.put("BT", APJ); // BHUTAN
        country_region_map.put("BO", AMERICAS); // BOLIVIA
        country_region_map.put("BA", EMEA); // BOSNIA AND HERZEGOVINA
        country_region_map.put("BW", EMEA); // BOTSWANA
        country_region_map.put("BV", APJ); // BOUVET ISLAND
        country_region_map.put("BR", AMERICAS); // BRAZIL
        country_region_map.put("IO", APJ); // BRITISH INDIAN OCEAN TERRITORY
        country_region_map.put("BN", APJ); // BRUNEI DARUSSALAM
        country_region_map.put("BG", EMEA); // BULGARIA
        country_region_map.put("BF", EMEA); // BURKINA FASO
        country_region_map.put("BI", EMEA); // BURUNDI
        country_region_map.put("KH", APJ); // CAMBODIA
        country_region_map.put("CM", EMEA); // CAMEROON
        country_region_map.put("CA", AMERICAS); // CANADA
        country_region_map.put("CV", EMEA); // CAPE VERDE
        country_region_map.put("KY", AMERICAS); // CAYMAN ISLANDS
        country_region_map.put("CF", EMEA); // CENTRAL AFRICAN REPUBLIC
        country_region_map.put("TD", EMEA); // CHAD
        country_region_map.put("CL", AMERICAS); // CHILE
        country_region_map.put("CN", APJ); // CHINA
        country_region_map.put("CX", APJ); // CHRISTMAS ISLAND
        country_region_map.put("CC", APJ); // COCOS (KEELING) ISLANDS
        country_region_map.put("CO", AMERICAS); // COLOMBIA
        country_region_map.put("KM", EMEA); // COMOROS
        country_region_map.put("CG", EMEA); // CONGO
        country_region_map.put("CD", EMEA); // CONGO, THE DEMOCRATIC REPUBLIC OF THE
        country_region_map.put("CK", APJ); // COOK ISLANDS
        country_region_map.put("CR", AMERICAS); // COSTA RICA
        country_region_map.put("CI", EMEA); // C?TE D'IVOIRE
        country_region_map.put("HR", EMEA); // CROATIA
        country_region_map.put("CU", AMERICAS); // CUBA
        country_region_map.put("CY", APJ); // CYPRUS
        country_region_map.put("CZ", EMEA); // CZECH REPUBLIC
        country_region_map.put("DK", EMEA); // DENMARK
        country_region_map.put("DJ", EMEA); // DJIBOUTI
        country_region_map.put("DM", AMERICAS); // DOMINICA
        country_region_map.put("DO", AMERICAS); // DOMINICAN REPUBLIC
        country_region_map.put("EC", AMERICAS); // ECUADOR
        country_region_map.put("EG", EMEA); // EGYPT
        country_region_map.put("SV", AMERICAS); // EL SALVADOR
        country_region_map.put("GQ", EMEA); // EQUATORIAL GUINEA
        country_region_map.put("ER", EMEA); // ERITREA
        country_region_map.put("EE", EMEA); // ESTONIA
        country_region_map.put("ET", EMEA); // ETHIOPIA
        country_region_map.put("FK", AMERICAS); // FALKLAND ISLANDS (MALVINAS)
        country_region_map.put("FO", EMEA); // FAROE ISLANDS
        country_region_map.put("FJ", APJ); // FIJI
        country_region_map.put("FI", EMEA); // FINLAND
        country_region_map.put("FR", EMEA); // FRANCE
        country_region_map.put("GF", AMERICAS); // FRENCH GUIANA
        country_region_map.put("PF", APJ); // FRENCH POLYNESIA
        country_region_map.put("TF", APJ); // FRENCH SOUTHERN TERRITORIES
        country_region_map.put("GA", EMEA); // GABON
        country_region_map.put("GM", EMEA); // GAMBIA
        country_region_map.put("GE", APJ); // GEORGIA
        country_region_map.put("DE", EMEA); // GERMANY
        country_region_map.put("GH", EMEA); // GHANA
        country_region_map.put("GI", EMEA); // GIBRALTAR
        country_region_map.put("GR", EMEA); // GREECE
        country_region_map.put("GL", AMERICAS); // GREENLAND
        country_region_map.put("GD", AMERICAS); // GRENADA
        country_region_map.put("GP", AMERICAS); // GUADELOUPE
        country_region_map.put("GU", APJ); // GUAM
        country_region_map.put("GT", AMERICAS); // GUATEMALA
        country_region_map.put("GG", EMEA); // GUERNSEY
        country_region_map.put("GN", EMEA); // GUINEA
        country_region_map.put("GW", EMEA); // GUINEA-BISSAU
        country_region_map.put("GY", AMERICAS); // GUYANA
        country_region_map.put("HT", AMERICAS); // HAITI
        country_region_map.put("HM", APJ); // HEARD ISLAND AND MCDONALD ISLANDS
        country_region_map.put("VA", EMEA); // HOLY SEE (VATICAN CITY STATE)
        country_region_map.put("HN", AMERICAS); // HONDURAS
        country_region_map.put("HK", APJ); // HONG KONG
        country_region_map.put("HU", EMEA); // HUNGARY
        country_region_map.put("IS", EMEA); // ICELAND
        country_region_map.put("IN", APJ); // INDIA
        country_region_map.put("ID", APJ); // INDONESIA
        country_region_map.put("IR", APJ); // IRAN, ISLAMIC REPUBLIC OF
        country_region_map.put("IQ", APJ); // IRAQ
        country_region_map.put("IE", EMEA); // IRELAND
        country_region_map.put("IM", EMEA); // ISLE OF MAN
        country_region_map.put("IL", APJ); // ISRAEL
        country_region_map.put("IT", EMEA); // ITALY
        country_region_map.put("JM", AMERICAS); // JAMAICA
        country_region_map.put("JP", APJ); // JAPAN
        country_region_map.put("JE", EMEA); // JERSEY
        country_region_map.put("JO", APJ); // JORDAN
        country_region_map.put("KZ", APJ); // KAZAKHSTAN
        country_region_map.put("KE", EMEA); // KENYA
        country_region_map.put("KI", APJ); // KIRIBATI
        country_region_map.put("KP", APJ); // KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF
        country_region_map.put("KR", APJ); // KOREA, REPUBLIC OF
        country_region_map.put("KW", APJ); // KUWAIT
        country_region_map.put("KG", APJ); // KYRGYZSTAN
        country_region_map.put("LA", APJ); // LAO PEOPLE'S DEMOCRATIC REPUBLIC
        country_region_map.put("LV", EMEA); // LATVIA
        country_region_map.put("LB", APJ); // LEBANON
        country_region_map.put("LS", EMEA); // LESOTHO
        country_region_map.put("LR", EMEA); // LIBERIA
        country_region_map.put("LY", EMEA); // LIBYAN ARAB JAMAHIRIYA
        country_region_map.put("LI", EMEA); // LIECHTENSTEIN
        country_region_map.put("LT", EMEA); // LITHUANIA
        country_region_map.put("LU", EMEA); // LUXEMBOURG
        country_region_map.put("MO", APJ); // MACAO
        country_region_map.put("MK", EMEA); // MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF
        country_region_map.put("MG", EMEA); // MADAGASCAR
        country_region_map.put("MW", EMEA); // MALAWI
        country_region_map.put("MY", APJ); // MALAYSIA
        country_region_map.put("MV", APJ); // MALDIVES
        country_region_map.put("ML", EMEA); // MALI
        country_region_map.put("MT", EMEA); // MALTA
        country_region_map.put("MH", APJ); // MARSHALL ISLANDS
        country_region_map.put("MQ", AMERICAS); // MARTINIQUE
        country_region_map.put("MR", EMEA); // MAURITANIA
        country_region_map.put("MU", EMEA); // MAURITIUS
        country_region_map.put("YT", EMEA); // MAYOTTE
        country_region_map.put("MX", AMERICAS); // MEXICO
        country_region_map.put("FM", APJ); // MICRONESIA, FEDERATED STATES OF
        country_region_map.put("MD", EMEA); // MOLDOVA, REPUBLIC OF
        country_region_map.put("MC", EMEA); // MONACO
        country_region_map.put("MN", APJ); // MONGOLIA
        country_region_map.put("ME", EMEA); // MONTENEGRO
        country_region_map.put("MS", AMERICAS); // MONTSERRAT
        country_region_map.put("MA", EMEA); // MOROCCO
        country_region_map.put("MZ", EMEA); // MOZAMBIQUE
        country_region_map.put("MM", APJ); // MYANMAR
        country_region_map.put("NA", EMEA); // NAMIBIA
        country_region_map.put("NR", APJ); // NAURU
        country_region_map.put("NP", APJ); // NEPAL
        country_region_map.put("NL", EMEA); // NETHERLANDS
        country_region_map.put("AN", AMERICAS); // NETHERLANDS ANTILLES
        country_region_map.put("NC", APJ); // NEW CALEDONIA
        country_region_map.put("NZ", APJ); // NEW ZEALAND
        country_region_map.put("NI", AMERICAS); // NICARAGUA
        country_region_map.put("NE", EMEA); // NIGER
        country_region_map.put("NG", EMEA); // NIGERIA
        country_region_map.put("NU", APJ); // NIUE
        country_region_map.put("NF", APJ); // NORFOLK ISLAND
        country_region_map.put("MP", APJ); // NORTHERN MARIANA ISLANDS
        country_region_map.put("NO", EMEA); // NORWAY
        country_region_map.put("OM", APJ); // OMAN
        country_region_map.put("PK", APJ); // PAKISTAN
        country_region_map.put("PW", APJ); // PALAU
        country_region_map.put("PS", APJ); // PALESTINIAN TERRITORY, OCCUPIED
        country_region_map.put("PA", AMERICAS); // PANAMA
        country_region_map.put("PG", APJ); // PAPUA NEW GUINEA
        country_region_map.put("PY", AMERICAS); // PARAGUAY
        country_region_map.put("PE", AMERICAS); // PERU
        country_region_map.put("PH", APJ); // PHILIPPINES
        country_region_map.put("PN", APJ); // PITCAIRN
        country_region_map.put("PL", EMEA); // POLAND
        country_region_map.put("PT", EMEA); // PORTUGAL
        country_region_map.put("PR", AMERICAS); // PUERTO RICO
        country_region_map.put("QA", APJ); // QATAR
        country_region_map.put("RE", EMEA); // ReUNION
        country_region_map.put("RO", EMEA); // ROMANIA
        country_region_map.put("RU", EMEA); // RUSSIAN FEDERATION
        country_region_map.put("RW", EMEA); // RWANDA
        country_region_map.put("SH", EMEA); // SAINT HELENA
        country_region_map.put("KN", AMERICAS); // SAINT KITTS AND NEVIS
        country_region_map.put("LC", AMERICAS); // SAINT LUCIA
        country_region_map.put("PM", AMERICAS); // SAINT PIERRE AND MIQUELON
        country_region_map.put("VC", AMERICAS); // SAINT VINCENT AND THE GRENADINES
        country_region_map.put("WS", APJ); // SAMOA
        country_region_map.put("SM", EMEA); // SAN MARINO
        country_region_map.put("ST", EMEA); // SAO TOME AND PRINCIPE
        country_region_map.put("SA", APJ); // SAUDI ARABIA
        country_region_map.put("SN", EMEA); // SENEGAL
        country_region_map.put("RS", EMEA); // SERBIA
        country_region_map.put("SC", EMEA); // SEYCHELLES
        country_region_map.put("SL", EMEA); // SIERRA LEONE
        country_region_map.put("SG", APJ); // SINGAPORE
        country_region_map.put("SK", EMEA); // SLOVAKIA
        country_region_map.put("SI", EMEA); // SLOVENIA
        country_region_map.put("SB", APJ); // SOLOMON ISLANDS
        country_region_map.put("SO", EMEA); // SOMALIA
        country_region_map.put("ZA", EMEA); // SOUTH AFRICA
        country_region_map.put("GS", APJ); // SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS
        country_region_map.put("ES", EMEA); // SPAIN
        country_region_map.put("LK", APJ); // SRI LANKA
        country_region_map.put("SD", EMEA); // SUDAN
        country_region_map.put("SR", AMERICAS); // SURINAME
        country_region_map.put("SJ", EMEA); // SVALBARD AND JAN MAYEN
        country_region_map.put("SZ", EMEA); // SWAZILAND
        country_region_map.put("SE", EMEA); // SWEDEN
        country_region_map.put("CH", EMEA); // SWITZERLAND
        country_region_map.put("SY", APJ); // SYRIAN ARAB REPUBLIC
        country_region_map.put("TW", APJ); // TAIWAN, PROVINCE OF CHINA
        country_region_map.put("TJ", APJ); // TAJIKISTAN
        country_region_map.put("TZ", EMEA); // TANZANIA, UNITED REPUBLIC OF
        country_region_map.put("TH", APJ); // THAILAND
        country_region_map.put("TL", APJ); // TIMOR-LESTE
        country_region_map.put("TG", EMEA); // TOGO
        country_region_map.put("TK", APJ); // TOKELAU
        country_region_map.put("TO", APJ); // TONGA
        country_region_map.put("TT", AMERICAS); // TRINIDAD AND TOBAGO
        country_region_map.put("TN", EMEA); // TUNISIA
        country_region_map.put("TR", APJ); // TURKEY
        country_region_map.put("TM", APJ); // TURKMENISTAN
        country_region_map.put("TC", AMERICAS); // TURKS AND CAICOS ISLANDS
        country_region_map.put("TV", APJ); // TUVALU
        country_region_map.put("UG", EMEA); // UGANDA
        country_region_map.put("UA", EMEA); // UKRAINE
        country_region_map.put("AE", APJ); // UNITED ARAB EMIRATES
        country_region_map.put("GB", EMEA); // UNITED KINGDOM
        country_region_map.put("US", AMERICAS); // UNITED STATES
        country_region_map.put("UM", APJ); // UNITED STATES MINOR OUTLYING ISLANDS
        country_region_map.put("UY", AMERICAS); // URUGUAY
        country_region_map.put("UZ", APJ); // UZBEKISTAN
        country_region_map.put("VU", APJ); // VANUATU
        country_region_map.put("VE", AMERICAS); // Vatican City State see HOLY SEE VENEZUELA
        country_region_map.put("VN", APJ); // VIET NAM
        country_region_map.put("VG", AMERICAS); // VIRGIN ISLANDS, BRITISH
        country_region_map.put("VI", AMERICAS); // VIRGIN ISLANDS, U.S.
        country_region_map.put("WF", APJ); // WALLIS AND FUTUNA
        country_region_map.put("EH", EMEA); // WESTERN SAHARA
        country_region_map.put("YE", APJ); // YEMEN
        country_region_map.put("ZM", EMEA); // Zaire see CONGO, THE DEMOCRATIC REPUBLIC OF THE ZAMBIA
        country_region_map.put("ZW", EMEA); // ZIMBABWE
    }
    
    
    /**
     * get region from country_code
     *
     * @param country_code ISO 3166 Codes (Countries) 
     * @return region, the value is one of "AMERICAS", "APJ" and "EMEA", if can not find the match region, NULL will be returned
     */
    public static String getRegionFromCountryCode(String country_code) {
        return (String)country_region_map.get(country_code.toUpperCase());
    }
}

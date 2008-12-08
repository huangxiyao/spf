/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: RMDataType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class RMDataType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class RMDataType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _userId
     */
    private java.lang.String _userId;

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;

    /**
     * Field _title
     */
    private java.lang.String _title;

    /**
     * Field _firstName
     */
    private java.lang.String _firstName;

    /**
     * Field _middleName
     */
    private java.lang.String _middleName;

    /**
     * Field _lastName
     */
    private java.lang.String _lastName;

    /**
     * Field _email
     */
    private java.lang.String _email;

    /**
     * Field _segmentName
     */
    private java.lang.String _segmentName;

    /**
     * Field _residentCountryCode
     */
    private java.lang.String _residentCountryCode;

    /**
     * Field _langCode
     */
    private java.lang.String _langCode;

    /**
     * Field _localizationCode
     */
    private java.lang.String _localizationCode;

    /**
     * Field _contactPrefEmail
     */
    private java.lang.String _contactPrefEmail;

    /**
     * Field _contactPrefTelephone
     */
    private java.lang.String _contactPrefTelephone;

    /**
     * Field _contactPrefPost
     */
    private java.lang.String _contactPrefPost;

    /**
     * Field _securityLevel
     */
    private java.lang.String _securityLevel;

    /**
     * Field _homeAddressLine1
     */
    private java.lang.String _homeAddressLine1;

    /**
     * Field _homeAddressLine2
     */
    private java.lang.String _homeAddressLine2;

    /**
     * Field _homeCity
     */
    private java.lang.String _homeCity;

    /**
     * Field _homeDistrict
     */
    private java.lang.String _homeDistrict;

    /**
     * Field _homeState
     */
    private java.lang.String _homeState;

    /**
     * Field _homePostalCode
     */
    private java.lang.String _homePostalCode;

    /**
     * Field _homeCountryCode
     */
    private java.lang.String _homeCountryCode;

    /**
     * Field _homePOBox
     */
    private java.lang.String _homePOBox;

    /**
     * Field _homeTelephoneCountryCode
     */
    private java.lang.String _homeTelephoneCountryCode;

    /**
     * Field _homeTelephoneCityCode
     */
    private java.lang.String _homeTelephoneCityCode;

    /**
     * Field _homeTelephoneNumber
     */
    private java.lang.String _homeTelephoneNumber;

    /**
     * Field _creationAppId
     */
    private java.lang.String _creationAppId;

    /**
     * Field _profileCreationDate
     */
    private java.lang.String _profileCreationDate;

    /**
     * Field _profileLastUpdatedApplicationId
     */
    private java.lang.String _profileLastUpdatedApplicationId;

    /**
     * Field _profileLastUpdatedDate
     */
    private java.lang.String _profileLastUpdatedDate;

    /**
     * Field _profileLastUpdatedProfileId
     */
    private java.lang.String _profileLastUpdatedProfileId;

    /**
     * Field _cidNumber
     */
    private java.lang.String _cidNumber;

    /**
     * Field _migrationExceptionFlag
     */
    private java.lang.String _migrationExceptionFlag;

    /**
     * Field _busCompanyName
     */
    private java.lang.String _busCompanyName;

    /**
     * Field _busBuildingName
     */
    private java.lang.String _busBuildingName;

    /**
     * Field _busMailStop
     */
    private java.lang.String _busMailStop;

    /**
     * Field _busAddressLine1
     */
    private java.lang.String _busAddressLine1;

    /**
     * Field _busAddressLine2
     */
    private java.lang.String _busAddressLine2;

    /**
     * Field _busCity
     */
    private java.lang.String _busCity;

    /**
     * Field _busDistrict
     */
    private java.lang.String _busDistrict;

    /**
     * Field _busState
     */
    private java.lang.String _busState;

    /**
     * Field _busPostalCode
     */
    private java.lang.String _busPostalCode;

    /**
     * Field _busCountryCode
     */
    private java.lang.String _busCountryCode;

    /**
     * Field _busAddressLocalizationCode
     */
    private java.lang.String _busAddressLocalizationCode;

    /**
     * Field _busTimezoneCode
     */
    private java.lang.String _busTimezoneCode;

    /**
     * Field _busTelephoneCountryCode
     */
    private java.lang.String _busTelephoneCountryCode;

    /**
     * Field _busTelephoneCityCode
     */
    private java.lang.String _busTelephoneCityCode;

    /**
     * Field _busTelephoneNumber
     */
    private java.lang.String _busTelephoneNumber;

    /**
     * Field _busTelephoneExtension
     */
    private java.lang.String _busTelephoneExtension;

    /**
     * Field _busFaxCountryCode
     */
    private java.lang.String _busFaxCountryCode;

    /**
     * Field _busFaxCityCode
     */
    private java.lang.String _busFaxCityCode;

    /**
     * Field _busFaxNumber
     */
    private java.lang.String _busFaxNumber;

    /**
     * Field _busFaxExtension
     */
    private java.lang.String _busFaxExtension;


      //----------------/
     //- Constructors -/
    //----------------/

    public RMDataType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.RMDataType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'busAddressLine1'.
     * 
     * @return String
     * @return the value of field 'busAddressLine1'.
     */
    public java.lang.String getBusAddressLine1()
    {
        return this._busAddressLine1;
    } //-- java.lang.String getBusAddressLine1() 

    /**
     * Returns the value of field 'busAddressLine2'.
     * 
     * @return String
     * @return the value of field 'busAddressLine2'.
     */
    public java.lang.String getBusAddressLine2()
    {
        return this._busAddressLine2;
    } //-- java.lang.String getBusAddressLine2() 

    /**
     * Returns the value of field 'busAddressLocalizationCode'.
     * 
     * @return String
     * @return the value of field 'busAddressLocalizationCode'.
     */
    public java.lang.String getBusAddressLocalizationCode()
    {
        return this._busAddressLocalizationCode;
    } //-- java.lang.String getBusAddressLocalizationCode() 

    /**
     * Returns the value of field 'busBuildingName'.
     * 
     * @return String
     * @return the value of field 'busBuildingName'.
     */
    public java.lang.String getBusBuildingName()
    {
        return this._busBuildingName;
    } //-- java.lang.String getBusBuildingName() 

    /**
     * Returns the value of field 'busCity'.
     * 
     * @return String
     * @return the value of field 'busCity'.
     */
    public java.lang.String getBusCity()
    {
        return this._busCity;
    } //-- java.lang.String getBusCity() 

    /**
     * Returns the value of field 'busCompanyName'.
     * 
     * @return String
     * @return the value of field 'busCompanyName'.
     */
    public java.lang.String getBusCompanyName()
    {
        return this._busCompanyName;
    } //-- java.lang.String getBusCompanyName() 

    /**
     * Returns the value of field 'busCountryCode'.
     * 
     * @return String
     * @return the value of field 'busCountryCode'.
     */
    public java.lang.String getBusCountryCode()
    {
        return this._busCountryCode;
    } //-- java.lang.String getBusCountryCode() 

    /**
     * Returns the value of field 'busDistrict'.
     * 
     * @return String
     * @return the value of field 'busDistrict'.
     */
    public java.lang.String getBusDistrict()
    {
        return this._busDistrict;
    } //-- java.lang.String getBusDistrict() 

    /**
     * Returns the value of field 'busFaxCityCode'.
     * 
     * @return String
     * @return the value of field 'busFaxCityCode'.
     */
    public java.lang.String getBusFaxCityCode()
    {
        return this._busFaxCityCode;
    } //-- java.lang.String getBusFaxCityCode() 

    /**
     * Returns the value of field 'busFaxCountryCode'.
     * 
     * @return String
     * @return the value of field 'busFaxCountryCode'.
     */
    public java.lang.String getBusFaxCountryCode()
    {
        return this._busFaxCountryCode;
    } //-- java.lang.String getBusFaxCountryCode() 

    /**
     * Returns the value of field 'busFaxExtension'.
     * 
     * @return String
     * @return the value of field 'busFaxExtension'.
     */
    public java.lang.String getBusFaxExtension()
    {
        return this._busFaxExtension;
    } //-- java.lang.String getBusFaxExtension() 

    /**
     * Returns the value of field 'busFaxNumber'.
     * 
     * @return String
     * @return the value of field 'busFaxNumber'.
     */
    public java.lang.String getBusFaxNumber()
    {
        return this._busFaxNumber;
    } //-- java.lang.String getBusFaxNumber() 

    /**
     * Returns the value of field 'busMailStop'.
     * 
     * @return String
     * @return the value of field 'busMailStop'.
     */
    public java.lang.String getBusMailStop()
    {
        return this._busMailStop;
    } //-- java.lang.String getBusMailStop() 

    /**
     * Returns the value of field 'busPostalCode'.
     * 
     * @return String
     * @return the value of field 'busPostalCode'.
     */
    public java.lang.String getBusPostalCode()
    {
        return this._busPostalCode;
    } //-- java.lang.String getBusPostalCode() 

    /**
     * Returns the value of field 'busState'.
     * 
     * @return String
     * @return the value of field 'busState'.
     */
    public java.lang.String getBusState()
    {
        return this._busState;
    } //-- java.lang.String getBusState() 

    /**
     * Returns the value of field 'busTelephoneCityCode'.
     * 
     * @return String
     * @return the value of field 'busTelephoneCityCode'.
     */
    public java.lang.String getBusTelephoneCityCode()
    {
        return this._busTelephoneCityCode;
    } //-- java.lang.String getBusTelephoneCityCode() 

    /**
     * Returns the value of field 'busTelephoneCountryCode'.
     * 
     * @return String
     * @return the value of field 'busTelephoneCountryCode'.
     */
    public java.lang.String getBusTelephoneCountryCode()
    {
        return this._busTelephoneCountryCode;
    } //-- java.lang.String getBusTelephoneCountryCode() 

    /**
     * Returns the value of field 'busTelephoneExtension'.
     * 
     * @return String
     * @return the value of field 'busTelephoneExtension'.
     */
    public java.lang.String getBusTelephoneExtension()
    {
        return this._busTelephoneExtension;
    } //-- java.lang.String getBusTelephoneExtension() 

    /**
     * Returns the value of field 'busTelephoneNumber'.
     * 
     * @return String
     * @return the value of field 'busTelephoneNumber'.
     */
    public java.lang.String getBusTelephoneNumber()
    {
        return this._busTelephoneNumber;
    } //-- java.lang.String getBusTelephoneNumber() 

    /**
     * Returns the value of field 'busTimezoneCode'.
     * 
     * @return String
     * @return the value of field 'busTimezoneCode'.
     */
    public java.lang.String getBusTimezoneCode()
    {
        return this._busTimezoneCode;
    } //-- java.lang.String getBusTimezoneCode() 

    /**
     * Returns the value of field 'cidNumber'.
     * 
     * @return String
     * @return the value of field 'cidNumber'.
     */
    public java.lang.String getCidNumber()
    {
        return this._cidNumber;
    } //-- java.lang.String getCidNumber() 

    /**
     * Returns the value of field 'contactPrefEmail'.
     * 
     * @return String
     * @return the value of field 'contactPrefEmail'.
     */
    public java.lang.String getContactPrefEmail()
    {
        return this._contactPrefEmail;
    } //-- java.lang.String getContactPrefEmail() 

    /**
     * Returns the value of field 'contactPrefPost'.
     * 
     * @return String
     * @return the value of field 'contactPrefPost'.
     */
    public java.lang.String getContactPrefPost()
    {
        return this._contactPrefPost;
    } //-- java.lang.String getContactPrefPost() 

    /**
     * Returns the value of field 'contactPrefTelephone'.
     * 
     * @return String
     * @return the value of field 'contactPrefTelephone'.
     */
    public java.lang.String getContactPrefTelephone()
    {
        return this._contactPrefTelephone;
    } //-- java.lang.String getContactPrefTelephone() 

    /**
     * Returns the value of field 'creationAppId'.
     * 
     * @return String
     * @return the value of field 'creationAppId'.
     */
    public java.lang.String getCreationAppId()
    {
        return this._creationAppId;
    } //-- java.lang.String getCreationAppId() 

    /**
     * Returns the value of field 'email'.
     * 
     * @return String
     * @return the value of field 'email'.
     */
    public java.lang.String getEmail()
    {
        return this._email;
    } //-- java.lang.String getEmail() 

    /**
     * Returns the value of field 'firstName'.
     * 
     * @return String
     * @return the value of field 'firstName'.
     */
    public java.lang.String getFirstName()
    {
        return this._firstName;
    } //-- java.lang.String getFirstName() 

    /**
     * Returns the value of field 'homeAddressLine1'.
     * 
     * @return String
     * @return the value of field 'homeAddressLine1'.
     */
    public java.lang.String getHomeAddressLine1()
    {
        return this._homeAddressLine1;
    } //-- java.lang.String getHomeAddressLine1() 

    /**
     * Returns the value of field 'homeAddressLine2'.
     * 
     * @return String
     * @return the value of field 'homeAddressLine2'.
     */
    public java.lang.String getHomeAddressLine2()
    {
        return this._homeAddressLine2;
    } //-- java.lang.String getHomeAddressLine2() 

    /**
     * Returns the value of field 'homeCity'.
     * 
     * @return String
     * @return the value of field 'homeCity'.
     */
    public java.lang.String getHomeCity()
    {
        return this._homeCity;
    } //-- java.lang.String getHomeCity() 

    /**
     * Returns the value of field 'homeCountryCode'.
     * 
     * @return String
     * @return the value of field 'homeCountryCode'.
     */
    public java.lang.String getHomeCountryCode()
    {
        return this._homeCountryCode;
    } //-- java.lang.String getHomeCountryCode() 

    /**
     * Returns the value of field 'homeDistrict'.
     * 
     * @return String
     * @return the value of field 'homeDistrict'.
     */
    public java.lang.String getHomeDistrict()
    {
        return this._homeDistrict;
    } //-- java.lang.String getHomeDistrict() 

    /**
     * Returns the value of field 'homePOBox'.
     * 
     * @return String
     * @return the value of field 'homePOBox'.
     */
    public java.lang.String getHomePOBox()
    {
        return this._homePOBox;
    } //-- java.lang.String getHomePOBox() 

    /**
     * Returns the value of field 'homePostalCode'.
     * 
     * @return String
     * @return the value of field 'homePostalCode'.
     */
    public java.lang.String getHomePostalCode()
    {
        return this._homePostalCode;
    } //-- java.lang.String getHomePostalCode() 

    /**
     * Returns the value of field 'homeState'.
     * 
     * @return String
     * @return the value of field 'homeState'.
     */
    public java.lang.String getHomeState()
    {
        return this._homeState;
    } //-- java.lang.String getHomeState() 

    /**
     * Returns the value of field 'homeTelephoneCityCode'.
     * 
     * @return String
     * @return the value of field 'homeTelephoneCityCode'.
     */
    public java.lang.String getHomeTelephoneCityCode()
    {
        return this._homeTelephoneCityCode;
    } //-- java.lang.String getHomeTelephoneCityCode() 

    /**
     * Returns the value of field 'homeTelephoneCountryCode'.
     * 
     * @return String
     * @return the value of field 'homeTelephoneCountryCode'.
     */
    public java.lang.String getHomeTelephoneCountryCode()
    {
        return this._homeTelephoneCountryCode;
    } //-- java.lang.String getHomeTelephoneCountryCode() 

    /**
     * Returns the value of field 'homeTelephoneNumber'.
     * 
     * @return String
     * @return the value of field 'homeTelephoneNumber'.
     */
    public java.lang.String getHomeTelephoneNumber()
    {
        return this._homeTelephoneNumber;
    } //-- java.lang.String getHomeTelephoneNumber() 

    /**
     * Returns the value of field 'langCode'.
     * 
     * @return String
     * @return the value of field 'langCode'.
     */
    public java.lang.String getLangCode()
    {
        return this._langCode;
    } //-- java.lang.String getLangCode() 

    /**
     * Returns the value of field 'lastName'.
     * 
     * @return String
     * @return the value of field 'lastName'.
     */
    public java.lang.String getLastName()
    {
        return this._lastName;
    } //-- java.lang.String getLastName() 

    /**
     * Returns the value of field 'localizationCode'.
     * 
     * @return String
     * @return the value of field 'localizationCode'.
     */
    public java.lang.String getLocalizationCode()
    {
        return this._localizationCode;
    } //-- java.lang.String getLocalizationCode() 

    /**
     * Returns the value of field 'middleName'.
     * 
     * @return String
     * @return the value of field 'middleName'.
     */
    public java.lang.String getMiddleName()
    {
        return this._middleName;
    } //-- java.lang.String getMiddleName() 

    /**
     * Returns the value of field 'migrationExceptionFlag'.
     * 
     * @return String
     * @return the value of field 'migrationExceptionFlag'.
     */
    public java.lang.String getMigrationExceptionFlag()
    {
        return this._migrationExceptionFlag;
    } //-- java.lang.String getMigrationExceptionFlag() 

    /**
     * Returns the value of field 'profileCreationDate'.
     * 
     * @return String
     * @return the value of field 'profileCreationDate'.
     */
    public java.lang.String getProfileCreationDate()
    {
        return this._profileCreationDate;
    } //-- java.lang.String getProfileCreationDate() 

    /**
     * Returns the value of field 'profileId'.
     * 
     * @return String
     * @return the value of field 'profileId'.
     */
    public java.lang.String getProfileId()
    {
        return this._profileId;
    } //-- java.lang.String getProfileId() 

    /**
     * Returns the value of field
     * 'profileLastUpdatedApplicationId'.
     * 
     * @return String
     * @return the value of field 'profileLastUpdatedApplicationId'.
     */
    public java.lang.String getProfileLastUpdatedApplicationId()
    {
        return this._profileLastUpdatedApplicationId;
    } //-- java.lang.String getProfileLastUpdatedApplicationId() 

    /**
     * Returns the value of field 'profileLastUpdatedDate'.
     * 
     * @return String
     * @return the value of field 'profileLastUpdatedDate'.
     */
    public java.lang.String getProfileLastUpdatedDate()
    {
        return this._profileLastUpdatedDate;
    } //-- java.lang.String getProfileLastUpdatedDate() 

    /**
     * Returns the value of field 'profileLastUpdatedProfileId'.
     * 
     * @return String
     * @return the value of field 'profileLastUpdatedProfileId'.
     */
    public java.lang.String getProfileLastUpdatedProfileId()
    {
        return this._profileLastUpdatedProfileId;
    } //-- java.lang.String getProfileLastUpdatedProfileId() 

    /**
     * Returns the value of field 'residentCountryCode'.
     * 
     * @return String
     * @return the value of field 'residentCountryCode'.
     */
    public java.lang.String getResidentCountryCode()
    {
        return this._residentCountryCode;
    } //-- java.lang.String getResidentCountryCode() 

    /**
     * Returns the value of field 'securityLevel'.
     * 
     * @return String
     * @return the value of field 'securityLevel'.
     */
    public java.lang.String getSecurityLevel()
    {
        return this._securityLevel;
    } //-- java.lang.String getSecurityLevel() 

    /**
     * Returns the value of field 'segmentName'.
     * 
     * @return String
     * @return the value of field 'segmentName'.
     */
    public java.lang.String getSegmentName()
    {
        return this._segmentName;
    } //-- java.lang.String getSegmentName() 

    /**
     * Returns the value of field 'title'.
     * 
     * @return String
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
     * Returns the value of field 'userId'.
     * 
     * @return String
     * @return the value of field 'userId'.
     */
    public java.lang.String getUserId()
    {
        return this._userId;
    } //-- java.lang.String getUserId() 

    /**
     * Sets the value of field 'busAddressLine1'.
     * 
     * @param busAddressLine1 the value of field 'busAddressLine1'.
     */
    public void setBusAddressLine1(java.lang.String busAddressLine1)
    {
        this._busAddressLine1 = busAddressLine1;
    } //-- void setBusAddressLine1(java.lang.String) 

    /**
     * Sets the value of field 'busAddressLine2'.
     * 
     * @param busAddressLine2 the value of field 'busAddressLine2'.
     */
    public void setBusAddressLine2(java.lang.String busAddressLine2)
    {
        this._busAddressLine2 = busAddressLine2;
    } //-- void setBusAddressLine2(java.lang.String) 

    /**
     * Sets the value of field 'busAddressLocalizationCode'.
     * 
     * @param busAddressLocalizationCode the value of field
     * 'busAddressLocalizationCode'.
     */
    public void setBusAddressLocalizationCode(java.lang.String busAddressLocalizationCode)
    {
        this._busAddressLocalizationCode = busAddressLocalizationCode;
    } //-- void setBusAddressLocalizationCode(java.lang.String) 

    /**
     * Sets the value of field 'busBuildingName'.
     * 
     * @param busBuildingName the value of field 'busBuildingName'.
     */
    public void setBusBuildingName(java.lang.String busBuildingName)
    {
        this._busBuildingName = busBuildingName;
    } //-- void setBusBuildingName(java.lang.String) 

    /**
     * Sets the value of field 'busCity'.
     * 
     * @param busCity the value of field 'busCity'.
     */
    public void setBusCity(java.lang.String busCity)
    {
        this._busCity = busCity;
    } //-- void setBusCity(java.lang.String) 

    /**
     * Sets the value of field 'busCompanyName'.
     * 
     * @param busCompanyName the value of field 'busCompanyName'.
     */
    public void setBusCompanyName(java.lang.String busCompanyName)
    {
        this._busCompanyName = busCompanyName;
    } //-- void setBusCompanyName(java.lang.String) 

    /**
     * Sets the value of field 'busCountryCode'.
     * 
     * @param busCountryCode the value of field 'busCountryCode'.
     */
    public void setBusCountryCode(java.lang.String busCountryCode)
    {
        this._busCountryCode = busCountryCode;
    } //-- void setBusCountryCode(java.lang.String) 

    /**
     * Sets the value of field 'busDistrict'.
     * 
     * @param busDistrict the value of field 'busDistrict'.
     */
    public void setBusDistrict(java.lang.String busDistrict)
    {
        this._busDistrict = busDistrict;
    } //-- void setBusDistrict(java.lang.String) 

    /**
     * Sets the value of field 'busFaxCityCode'.
     * 
     * @param busFaxCityCode the value of field 'busFaxCityCode'.
     */
    public void setBusFaxCityCode(java.lang.String busFaxCityCode)
    {
        this._busFaxCityCode = busFaxCityCode;
    } //-- void setBusFaxCityCode(java.lang.String) 

    /**
     * Sets the value of field 'busFaxCountryCode'.
     * 
     * @param busFaxCountryCode the value of field
     * 'busFaxCountryCode'.
     */
    public void setBusFaxCountryCode(java.lang.String busFaxCountryCode)
    {
        this._busFaxCountryCode = busFaxCountryCode;
    } //-- void setBusFaxCountryCode(java.lang.String) 

    /**
     * Sets the value of field 'busFaxExtension'.
     * 
     * @param busFaxExtension the value of field 'busFaxExtension'.
     */
    public void setBusFaxExtension(java.lang.String busFaxExtension)
    {
        this._busFaxExtension = busFaxExtension;
    } //-- void setBusFaxExtension(java.lang.String) 

    /**
     * Sets the value of field 'busFaxNumber'.
     * 
     * @param busFaxNumber the value of field 'busFaxNumber'.
     */
    public void setBusFaxNumber(java.lang.String busFaxNumber)
    {
        this._busFaxNumber = busFaxNumber;
    } //-- void setBusFaxNumber(java.lang.String) 

    /**
     * Sets the value of field 'busMailStop'.
     * 
     * @param busMailStop the value of field 'busMailStop'.
     */
    public void setBusMailStop(java.lang.String busMailStop)
    {
        this._busMailStop = busMailStop;
    } //-- void setBusMailStop(java.lang.String) 

    /**
     * Sets the value of field 'busPostalCode'.
     * 
     * @param busPostalCode the value of field 'busPostalCode'.
     */
    public void setBusPostalCode(java.lang.String busPostalCode)
    {
        this._busPostalCode = busPostalCode;
    } //-- void setBusPostalCode(java.lang.String) 

    /**
     * Sets the value of field 'busState'.
     * 
     * @param busState the value of field 'busState'.
     */
    public void setBusState(java.lang.String busState)
    {
        this._busState = busState;
    } //-- void setBusState(java.lang.String) 

    /**
     * Sets the value of field 'busTelephoneCityCode'.
     * 
     * @param busTelephoneCityCode the value of field
     * 'busTelephoneCityCode'.
     */
    public void setBusTelephoneCityCode(java.lang.String busTelephoneCityCode)
    {
        this._busTelephoneCityCode = busTelephoneCityCode;
    } //-- void setBusTelephoneCityCode(java.lang.String) 

    /**
     * Sets the value of field 'busTelephoneCountryCode'.
     * 
     * @param busTelephoneCountryCode the value of field
     * 'busTelephoneCountryCode'.
     */
    public void setBusTelephoneCountryCode(java.lang.String busTelephoneCountryCode)
    {
        this._busTelephoneCountryCode = busTelephoneCountryCode;
    } //-- void setBusTelephoneCountryCode(java.lang.String) 

    /**
     * Sets the value of field 'busTelephoneExtension'.
     * 
     * @param busTelephoneExtension the value of field
     * 'busTelephoneExtension'.
     */
    public void setBusTelephoneExtension(java.lang.String busTelephoneExtension)
    {
        this._busTelephoneExtension = busTelephoneExtension;
    } //-- void setBusTelephoneExtension(java.lang.String) 

    /**
     * Sets the value of field 'busTelephoneNumber'.
     * 
     * @param busTelephoneNumber the value of field
     * 'busTelephoneNumber'.
     */
    public void setBusTelephoneNumber(java.lang.String busTelephoneNumber)
    {
        this._busTelephoneNumber = busTelephoneNumber;
    } //-- void setBusTelephoneNumber(java.lang.String) 

    /**
     * Sets the value of field 'busTimezoneCode'.
     * 
     * @param busTimezoneCode the value of field 'busTimezoneCode'.
     */
    public void setBusTimezoneCode(java.lang.String busTimezoneCode)
    {
        this._busTimezoneCode = busTimezoneCode;
    } //-- void setBusTimezoneCode(java.lang.String) 

    /**
     * Sets the value of field 'cidNumber'.
     * 
     * @param cidNumber the value of field 'cidNumber'.
     */
    public void setCidNumber(java.lang.String cidNumber)
    {
        this._cidNumber = cidNumber;
    } //-- void setCidNumber(java.lang.String) 

    /**
     * Sets the value of field 'contactPrefEmail'.
     * 
     * @param contactPrefEmail the value of field 'contactPrefEmail'
     */
    public void setContactPrefEmail(java.lang.String contactPrefEmail)
    {
        this._contactPrefEmail = contactPrefEmail;
    } //-- void setContactPrefEmail(java.lang.String) 

    /**
     * Sets the value of field 'contactPrefPost'.
     * 
     * @param contactPrefPost the value of field 'contactPrefPost'.
     */
    public void setContactPrefPost(java.lang.String contactPrefPost)
    {
        this._contactPrefPost = contactPrefPost;
    } //-- void setContactPrefPost(java.lang.String) 

    /**
     * Sets the value of field 'contactPrefTelephone'.
     * 
     * @param contactPrefTelephone the value of field
     * 'contactPrefTelephone'.
     */
    public void setContactPrefTelephone(java.lang.String contactPrefTelephone)
    {
        this._contactPrefTelephone = contactPrefTelephone;
    } //-- void setContactPrefTelephone(java.lang.String) 

    /**
     * Sets the value of field 'creationAppId'.
     * 
     * @param creationAppId the value of field 'creationAppId'.
     */
    public void setCreationAppId(java.lang.String creationAppId)
    {
        this._creationAppId = creationAppId;
    } //-- void setCreationAppId(java.lang.String) 

    /**
     * Sets the value of field 'email'.
     * 
     * @param email the value of field 'email'.
     */
    public void setEmail(java.lang.String email)
    {
        this._email = email;
    } //-- void setEmail(java.lang.String) 

    /**
     * Sets the value of field 'firstName'.
     * 
     * @param firstName the value of field 'firstName'.
     */
    public void setFirstName(java.lang.String firstName)
    {
        this._firstName = firstName;
    } //-- void setFirstName(java.lang.String) 

    /**
     * Sets the value of field 'homeAddressLine1'.
     * 
     * @param homeAddressLine1 the value of field 'homeAddressLine1'
     */
    public void setHomeAddressLine1(java.lang.String homeAddressLine1)
    {
        this._homeAddressLine1 = homeAddressLine1;
    } //-- void setHomeAddressLine1(java.lang.String) 

    /**
     * Sets the value of field 'homeAddressLine2'.
     * 
     * @param homeAddressLine2 the value of field 'homeAddressLine2'
     */
    public void setHomeAddressLine2(java.lang.String homeAddressLine2)
    {
        this._homeAddressLine2 = homeAddressLine2;
    } //-- void setHomeAddressLine2(java.lang.String) 

    /**
     * Sets the value of field 'homeCity'.
     * 
     * @param homeCity the value of field 'homeCity'.
     */
    public void setHomeCity(java.lang.String homeCity)
    {
        this._homeCity = homeCity;
    } //-- void setHomeCity(java.lang.String) 

    /**
     * Sets the value of field 'homeCountryCode'.
     * 
     * @param homeCountryCode the value of field 'homeCountryCode'.
     */
    public void setHomeCountryCode(java.lang.String homeCountryCode)
    {
        this._homeCountryCode = homeCountryCode;
    } //-- void setHomeCountryCode(java.lang.String) 

    /**
     * Sets the value of field 'homeDistrict'.
     * 
     * @param homeDistrict the value of field 'homeDistrict'.
     */
    public void setHomeDistrict(java.lang.String homeDistrict)
    {
        this._homeDistrict = homeDistrict;
    } //-- void setHomeDistrict(java.lang.String) 

    /**
     * Sets the value of field 'homePOBox'.
     * 
     * @param homePOBox the value of field 'homePOBox'.
     */
    public void setHomePOBox(java.lang.String homePOBox)
    {
        this._homePOBox = homePOBox;
    } //-- void setHomePOBox(java.lang.String) 

    /**
     * Sets the value of field 'homePostalCode'.
     * 
     * @param homePostalCode the value of field 'homePostalCode'.
     */
    public void setHomePostalCode(java.lang.String homePostalCode)
    {
        this._homePostalCode = homePostalCode;
    } //-- void setHomePostalCode(java.lang.String) 

    /**
     * Sets the value of field 'homeState'.
     * 
     * @param homeState the value of field 'homeState'.
     */
    public void setHomeState(java.lang.String homeState)
    {
        this._homeState = homeState;
    } //-- void setHomeState(java.lang.String) 

    /**
     * Sets the value of field 'homeTelephoneCityCode'.
     * 
     * @param homeTelephoneCityCode the value of field
     * 'homeTelephoneCityCode'.
     */
    public void setHomeTelephoneCityCode(java.lang.String homeTelephoneCityCode)
    {
        this._homeTelephoneCityCode = homeTelephoneCityCode;
    } //-- void setHomeTelephoneCityCode(java.lang.String) 

    /**
     * Sets the value of field 'homeTelephoneCountryCode'.
     * 
     * @param homeTelephoneCountryCode the value of field
     * 'homeTelephoneCountryCode'.
     */
    public void setHomeTelephoneCountryCode(java.lang.String homeTelephoneCountryCode)
    {
        this._homeTelephoneCountryCode = homeTelephoneCountryCode;
    } //-- void setHomeTelephoneCountryCode(java.lang.String) 

    /**
     * Sets the value of field 'homeTelephoneNumber'.
     * 
     * @param homeTelephoneNumber the value of field
     * 'homeTelephoneNumber'.
     */
    public void setHomeTelephoneNumber(java.lang.String homeTelephoneNumber)
    {
        this._homeTelephoneNumber = homeTelephoneNumber;
    } //-- void setHomeTelephoneNumber(java.lang.String) 

    /**
     * Sets the value of field 'langCode'.
     * 
     * @param langCode the value of field 'langCode'.
     */
    public void setLangCode(java.lang.String langCode)
    {
        this._langCode = langCode;
    } //-- void setLangCode(java.lang.String) 

    /**
     * Sets the value of field 'lastName'.
     * 
     * @param lastName the value of field 'lastName'.
     */
    public void setLastName(java.lang.String lastName)
    {
        this._lastName = lastName;
    } //-- void setLastName(java.lang.String) 

    /**
     * Sets the value of field 'localizationCode'.
     * 
     * @param localizationCode the value of field 'localizationCode'
     */
    public void setLocalizationCode(java.lang.String localizationCode)
    {
        this._localizationCode = localizationCode;
    } //-- void setLocalizationCode(java.lang.String) 

    /**
     * Sets the value of field 'middleName'.
     * 
     * @param middleName the value of field 'middleName'.
     */
    public void setMiddleName(java.lang.String middleName)
    {
        this._middleName = middleName;
    } //-- void setMiddleName(java.lang.String) 

    /**
     * Sets the value of field 'migrationExceptionFlag'.
     * 
     * @param migrationExceptionFlag the value of field
     * 'migrationExceptionFlag'.
     */
    public void setMigrationExceptionFlag(java.lang.String migrationExceptionFlag)
    {
        this._migrationExceptionFlag = migrationExceptionFlag;
    } //-- void setMigrationExceptionFlag(java.lang.String) 

    /**
     * Sets the value of field 'profileCreationDate'.
     * 
     * @param profileCreationDate the value of field
     * 'profileCreationDate'.
     */
    public void setProfileCreationDate(java.lang.String profileCreationDate)
    {
        this._profileCreationDate = profileCreationDate;
    } //-- void setProfileCreationDate(java.lang.String) 

    /**
     * Sets the value of field 'profileId'.
     * 
     * @param profileId the value of field 'profileId'.
     */
    public void setProfileId(java.lang.String profileId)
    {
        this._profileId = profileId;
    } //-- void setProfileId(java.lang.String) 

    /**
     * Sets the value of field 'profileLastUpdatedApplicationId'.
     * 
     * @param profileLastUpdatedApplicationId the value of field
     * 'profileLastUpdatedApplicationId'.
     */
    public void setProfileLastUpdatedApplicationId(java.lang.String profileLastUpdatedApplicationId)
    {
        this._profileLastUpdatedApplicationId = profileLastUpdatedApplicationId;
    } //-- void setProfileLastUpdatedApplicationId(java.lang.String) 

    /**
     * Sets the value of field 'profileLastUpdatedDate'.
     * 
     * @param profileLastUpdatedDate the value of field
     * 'profileLastUpdatedDate'.
     */
    public void setProfileLastUpdatedDate(java.lang.String profileLastUpdatedDate)
    {
        this._profileLastUpdatedDate = profileLastUpdatedDate;
    } //-- void setProfileLastUpdatedDate(java.lang.String) 

    /**
     * Sets the value of field 'profileLastUpdatedProfileId'.
     * 
     * @param profileLastUpdatedProfileId the value of field
     * 'profileLastUpdatedProfileId'.
     */
    public void setProfileLastUpdatedProfileId(java.lang.String profileLastUpdatedProfileId)
    {
        this._profileLastUpdatedProfileId = profileLastUpdatedProfileId;
    } //-- void setProfileLastUpdatedProfileId(java.lang.String) 

    /**
     * Sets the value of field 'residentCountryCode'.
     * 
     * @param residentCountryCode the value of field
     * 'residentCountryCode'.
     */
    public void setResidentCountryCode(java.lang.String residentCountryCode)
    {
        this._residentCountryCode = residentCountryCode;
    } //-- void setResidentCountryCode(java.lang.String) 

    /**
     * Sets the value of field 'securityLevel'.
     * 
     * @param securityLevel the value of field 'securityLevel'.
     */
    public void setSecurityLevel(java.lang.String securityLevel)
    {
        this._securityLevel = securityLevel;
    } //-- void setSecurityLevel(java.lang.String) 

    /**
     * Sets the value of field 'segmentName'.
     * 
     * @param segmentName the value of field 'segmentName'.
     */
    public void setSegmentName(java.lang.String segmentName)
    {
        this._segmentName = segmentName;
    } //-- void setSegmentName(java.lang.String) 

    /**
     * Sets the value of field 'title'.
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Sets the value of field 'userId'.
     * 
     * @param userId the value of field 'userId'.
     */
    public void setUserId(java.lang.String userId)
    {
        this._userId = userId;
    } //-- void setUserId(java.lang.String) 

}

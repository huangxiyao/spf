/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: PrivateDataType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class PrivateDataType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class PrivateDataType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profilePrivate
     */
    private com.hp.globalops.hppcbl.webservice.ProfilePrivate _profilePrivate;

    /**
     * Field _profileExtended
     */
    private com.hp.globalops.hppcbl.webservice.ProfileExtended _profileExtended;

    /**
     * Field _systemData
     */
    private com.hp.globalops.hppcbl.webservice.SystemData _systemData;


      //----------------/
     //- Constructors -/
    //----------------/

    public PrivateDataType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.PrivateDataType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'profileExtended'.
     * 
     * @return ProfileExtended
     * @return the value of field 'profileExtended'.
     */
    public com.hp.globalops.hppcbl.webservice.ProfileExtended getProfileExtended()
    {
        return this._profileExtended;
    } //-- com.hp.globalops.hppcbl.webservice.ProfileExtended getProfileExtended() 

    /**
     * Returns the value of field 'profilePrivate'.
     * 
     * @return ProfilePrivate
     * @return the value of field 'profilePrivate'.
     */
    public com.hp.globalops.hppcbl.webservice.ProfilePrivate getProfilePrivate()
    {
        return this._profilePrivate;
    } //-- com.hp.globalops.hppcbl.webservice.ProfilePrivate getProfilePrivate() 

    /**
     * Returns the value of field 'systemData'.
     * 
     * @return SystemData
     * @return the value of field 'systemData'.
     */
    public com.hp.globalops.hppcbl.webservice.SystemData getSystemData()
    {
        return this._systemData;
    } //-- com.hp.globalops.hppcbl.webservice.SystemData getSystemData() 

    /**
     * Sets the value of field 'profileExtended'.
     * 
     * @param profileExtended the value of field 'profileExtended'.
     */
    public void setProfileExtended(com.hp.globalops.hppcbl.webservice.ProfileExtended profileExtended)
    {
        this._profileExtended = profileExtended;
    } //-- void setProfileExtended(com.hp.globalops.hppcbl.webservice.ProfileExtended) 

    /**
     * Sets the value of field 'profilePrivate'.
     * 
     * @param profilePrivate the value of field 'profilePrivate'.
     */
    public void setProfilePrivate(com.hp.globalops.hppcbl.webservice.ProfilePrivate profilePrivate)
    {
        this._profilePrivate = profilePrivate;
    } //-- void setProfilePrivate(com.hp.globalops.hppcbl.webservice.ProfilePrivate) 

    /**
     * Sets the value of field 'systemData'.
     * 
     * @param systemData the value of field 'systemData'.
     */
    public void setSystemData(com.hp.globalops.hppcbl.webservice.SystemData systemData)
    {
        this._systemData = systemData;
    } //-- void setSystemData(com.hp.globalops.hppcbl.webservice.SystemData) 

}

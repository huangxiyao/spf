/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: ProfileIdResultType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class ProfileIdResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class ProfileIdResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;

    /**
     * Field _userId
     */
    private java.lang.String _userId;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProfileIdResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.ProfileIdResultType()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Sets the value of field 'profileId'.
     * 
     * @param profileId the value of field 'profileId'.
     */
    public void setProfileId(java.lang.String profileId)
    {
        this._profileId = profileId;
    } //-- void setProfileId(java.lang.String) 

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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: AdminResetPasswordResultType.java,v 1.2 2007/09/29 03:05:05 liuliyey Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class AdminResetPasswordResultType.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/09/29 03:05:05 $
 */
public class AdminResetPasswordResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;
    
    private String _guid;
    
    private String _url;


      //----------------/
     //- Constructors -/
    //----------------/

    public String getGuid() {
        return _guid;
    }


    public void setGuid(String guid) {
        this._guid = guid;
    }


    public String getUrl() {
        return _url;
    }


    public void setUrl(String url) {
        this._url = url;
    }


    public AdminResetPasswordResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.AdminResetPasswordResultType()


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
     * Sets the value of field 'profileId'.
     * 
     * @param profileId the value of field 'profileId'.
     */
    public void setProfileId(java.lang.String profileId)
    {
        this._profileId = profileId;
    } //-- void setProfileId(java.lang.String) 

}

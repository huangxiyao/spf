/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: ValidatePinResultType.java,v 1.1 2006/09/08 14:32:03 geymonda Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class ValidatePinResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/09/08 14:32:03 $
 */
public class ValidatePinResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;


      //----------------/
     //- Constructors -/
    //----------------/

    public ValidatePinResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.ValidatePinResultType()


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

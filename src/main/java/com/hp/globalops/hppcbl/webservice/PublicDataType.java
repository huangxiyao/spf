/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: PublicDataType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class PublicDataType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class PublicDataType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profilePublic
     */
    private com.hp.globalops.hppcbl.webservice.ProfilePublic _profilePublic;


      //----------------/
     //- Constructors -/
    //----------------/

    public PublicDataType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.PublicDataType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'profilePublic'.
     * 
     * @return ProfilePublic
     * @return the value of field 'profilePublic'.
     */
    public com.hp.globalops.hppcbl.webservice.ProfilePublic getProfilePublic()
    {
        return this._profilePublic;
    } //-- com.hp.globalops.hppcbl.webservice.ProfilePublic getProfilePublic() 

    /**
     * Sets the value of field 'profilePublic'.
     * 
     * @param profilePublic the value of field 'profilePublic'.
     */
    public void setProfilePublic(com.hp.globalops.hppcbl.webservice.ProfilePublic profilePublic)
    {
        this._profilePublic = profilePublic;
    } //-- void setProfilePublic(com.hp.globalops.hppcbl.webservice.ProfilePublic) 

}

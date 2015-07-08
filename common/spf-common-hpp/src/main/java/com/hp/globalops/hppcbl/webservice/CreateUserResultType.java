/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: CreateUserResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class CreateUserResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class CreateUserResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;

    /**
     * Field _requiresEV
     */
    private java.lang.String _requiresEV;


      //----------------/
     //- Constructors -/
    //----------------/

    public CreateUserResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.CreateUserResultType()


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
     * Returns the value of field 'requiresEV'.
     * 
     * @return String
     * @return the value of field 'requiresEV'.
     */
    public java.lang.String getRequiresEV()
    {
        return this._requiresEV;
    } //-- java.lang.String getRequiresEV() 

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
     * Sets the value of field 'requiresEV'.
     * 
     * @param requiresEV the value of field 'requiresEV'.
     */
    public void setRequiresEV(java.lang.String requiresEV)
    {
        this._requiresEV = requiresEV;
    } //-- void setRequiresEV(java.lang.String) 

}

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: IsMemberOfGroupResultType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class IsMemberOfGroupResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class IsMemberOfGroupResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _isMember
     */
    private boolean _isMember;

    /**
     * keeps track of state for field: _isMember
     */
    private boolean _has_isMember;

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;


      //----------------/
     //- Constructors -/
    //----------------/

    public IsMemberOfGroupResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.IsMemberOfGroupResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIsMember
     * 
     */
    public void deleteIsMember()
    {
        this._has_isMember= false;
    } //-- void deleteIsMember() 

    /**
     * Returns the value of field 'isMember'.
     * 
     * @return boolean
     * @return the value of field 'isMember'.
     */
    public boolean getIsMember()
    {
        return this._isMember;
    } //-- boolean getIsMember() 

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
     * Method hasIsMember
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasIsMember()
    {
        return this._has_isMember;
    } //-- boolean hasIsMember() 

    /**
     * Sets the value of field 'isMember'.
     * 
     * @param isMember the value of field 'isMember'.
     */
    public void setIsMember(boolean isMember)
    {
        this._isMember = isMember;
        this._has_isMember = true;
    } //-- void setIsMember(boolean) 

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

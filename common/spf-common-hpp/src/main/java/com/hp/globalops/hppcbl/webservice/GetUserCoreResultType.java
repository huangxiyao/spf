/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetUserCoreResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetUserCoreResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetUserCoreResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profileIdentity
     */
    private com.hp.globalops.hppcbl.webservice.ProfileIdentity _profileIdentity;

    /**
     * Field _profileCore
     */
    private com.hp.globalops.hppcbl.webservice.ProfileCore _profileCore;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetUserCoreResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetUserCoreResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'profileCore'.
     * 
     * @return ProfileCore
     * @return the value of field 'profileCore'.
     */
    public com.hp.globalops.hppcbl.webservice.ProfileCore getProfileCore()
    {
        return this._profileCore;
    } //-- com.hp.globalops.hppcbl.webservice.ProfileCore getProfileCore() 

    /**
     * Returns the value of field 'profileIdentity'.
     * 
     * @return ProfileIdentity
     * @return the value of field 'profileIdentity'.
     */
    public com.hp.globalops.hppcbl.webservice.ProfileIdentity getProfileIdentity()
    {
        return this._profileIdentity;
    } //-- com.hp.globalops.hppcbl.webservice.ProfileIdentity getProfileIdentity() 

    /**
     * Sets the value of field 'profileCore'.
     * 
     * @param profileCore the value of field 'profileCore'.
     */
    public void setProfileCore(com.hp.globalops.hppcbl.webservice.ProfileCore profileCore)
    {
        this._profileCore = profileCore;
    } //-- void setProfileCore(com.hp.globalops.hppcbl.webservice.ProfileCore) 

    /**
     * Sets the value of field 'profileIdentity'.
     * 
     * @param profileIdentity the value of field 'profileIdentity'.
     */
    public void setProfileIdentity(com.hp.globalops.hppcbl.webservice.ProfileIdentity profileIdentity)
    {
        this._profileIdentity = profileIdentity;
    } //-- void setProfileIdentity(com.hp.globalops.hppcbl.webservice.ProfileIdentity) 

}

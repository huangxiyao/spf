/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetGUIDExpirationRequestType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetGUIDExpirationRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetGUIDExpirationRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _guid
     */
    private java.lang.String _guid;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetGUIDExpirationRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetGUIDExpirationRequestType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'guid'.
     * 
     * @return String
     * @return the value of field 'guid'.
     */
    public java.lang.String getGuid()
    {
        return this._guid;
    } //-- java.lang.String getGuid() 

    /**
     * Sets the value of field 'guid'.
     * 
     * @param guid the value of field 'guid'.
     */
    public void setGuid(java.lang.String guid)
    {
        this._guid = guid;
    } //-- void setGuid(java.lang.String) 

}

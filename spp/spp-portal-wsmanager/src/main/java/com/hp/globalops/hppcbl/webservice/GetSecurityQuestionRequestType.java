/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetSecurityQuestionRequestType.java,v 1.1 2006/09/08 14:32:07 geymonda Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetSecurityQuestionRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/09/08 14:32:07 $
 */
public class GetSecurityQuestionRequestType implements java.io.Serializable {


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

    public GetSecurityQuestionRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetSecurityQuestionRequestType()


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

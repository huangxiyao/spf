/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: ValidateSessionTokenResultType.java,v 1.1 2006/09/08 14:31:48 geymonda Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class ValidateSessionTokenResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/09/08 14:31:48 $
 */
public class ValidateSessionTokenResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _userId
     */
    private java.lang.String _userId;


      //----------------/
     //- Constructors -/
    //----------------/

    public ValidateSessionTokenResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.ValidateSessionTokenResultType()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Sets the value of field 'userId'.
     * 
     * @param userId the value of field 'userId'.
     */
    public void setUserId(java.lang.String userId)
    {
        this._userId = userId;
    } //-- void setUserId(java.lang.String) 

}

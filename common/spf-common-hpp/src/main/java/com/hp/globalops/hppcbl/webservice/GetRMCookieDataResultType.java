/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetRMCookieDataResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetRMCookieDataResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetRMCookieDataResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _rMDataField
     */
    private java.lang.String _rMDataField;

    /**
     * Field _rMExpiryTimestamp
     */
    private java.lang.String _rMExpiryTimestamp;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetRMCookieDataResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetRMCookieDataResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'rMDataField'.
     * 
     * @return String
     * @return the value of field 'rMDataField'.
     */
    public java.lang.String getRMDataField()
    {
        return this._rMDataField;
    } //-- java.lang.String getRMDataField() 

    /**
     * Returns the value of field 'rMExpiryTimestamp'.
     * 
     * @return String
     * @return the value of field 'rMExpiryTimestamp'.
     */
    public java.lang.String getRMExpiryTimestamp()
    {
        return this._rMExpiryTimestamp;
    } //-- java.lang.String getRMExpiryTimestamp() 

    /**
     * Sets the value of field 'rMDataField'.
     * 
     * @param rMDataField the value of field 'rMDataField'.
     */
    public void setRMDataField(java.lang.String rMDataField)
    {
        this._rMDataField = rMDataField;
    } //-- void setRMDataField(java.lang.String) 

    /**
     * Sets the value of field 'rMExpiryTimestamp'.
     * 
     * @param rMExpiryTimestamp the value of field
     * 'rMExpiryTimestamp'.
     */
    public void setRMExpiryTimestamp(java.lang.String rMExpiryTimestamp)
    {
        this._rMExpiryTimestamp = rMExpiryTimestamp;
    } //-- void setRMExpiryTimestamp(java.lang.String) 

}

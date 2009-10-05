/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetRMDataResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetRMDataResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetRMDataResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _rMData
     */
    private com.hp.globalops.hppcbl.webservice.RMData _rMData;

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

    public GetRMDataResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetRMDataResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'rMData'.
     * 
     * @return RMData
     * @return the value of field 'rMData'.
     */
    public com.hp.globalops.hppcbl.webservice.RMData getRMData()
    {
        return this._rMData;
    } //-- com.hp.globalops.hppcbl.webservice.RMData getRMData() 

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
     * Sets the value of field 'rMData'.
     * 
     * @param rMData the value of field 'rMData'.
     */
    public void setRMData(com.hp.globalops.hppcbl.webservice.RMData rMData)
    {
        this._rMData = rMData;
    } //-- void setRMData(com.hp.globalops.hppcbl.webservice.RMData) 

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

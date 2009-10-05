/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetRMDataRequestType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetRMDataRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetRMDataRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _rMdataField
     */
    private java.lang.String _rMdataField;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetRMDataRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetRMDataRequestType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'rMdataField'.
     * 
     * @return String
     * @return the value of field 'rMdataField'.
     */
    public java.lang.String getRMdataField()
    {
        return this._rMdataField;
    } //-- java.lang.String getRMdataField() 

    /**
     * Sets the value of field 'rMdataField'.
     * 
     * @param rMdataField the value of field 'rMdataField'.
     */
    public void setRMdataField(java.lang.String rMdataField)
    {
        this._rMdataField = rMdataField;
    } //-- void setRMdataField(java.lang.String) 

}

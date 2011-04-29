/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: ModifiedDataType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class ModifiedDataType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class ModifiedDataType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fieldName
     */
    private java.lang.String _fieldName;

    /**
     * Field _beforeValue
     */
    private java.lang.String _beforeValue;

    /**
     * Field _afterValue
     */
    private java.lang.String _afterValue;


      //----------------/
     //- Constructors -/
    //----------------/

    public ModifiedDataType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.ModifiedDataType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'afterValue'.
     * 
     * @return String
     * @return the value of field 'afterValue'.
     */
    public java.lang.String getAfterValue()
    {
        return this._afterValue;
    } //-- java.lang.String getAfterValue() 

    /**
     * Returns the value of field 'beforeValue'.
     * 
     * @return String
     * @return the value of field 'beforeValue'.
     */
    public java.lang.String getBeforeValue()
    {
        return this._beforeValue;
    } //-- java.lang.String getBeforeValue() 

    /**
     * Returns the value of field 'fieldName'.
     * 
     * @return String
     * @return the value of field 'fieldName'.
     */
    public java.lang.String getFieldName()
    {
        return this._fieldName;
    } //-- java.lang.String getFieldName() 

    /**
     * Sets the value of field 'afterValue'.
     * 
     * @param afterValue the value of field 'afterValue'.
     */
    public void setAfterValue(java.lang.String afterValue)
    {
        this._afterValue = afterValue;
    } //-- void setAfterValue(java.lang.String) 

    /**
     * Sets the value of field 'beforeValue'.
     * 
     * @param beforeValue the value of field 'beforeValue'.
     */
    public void setBeforeValue(java.lang.String beforeValue)
    {
        this._beforeValue = beforeValue;
    } //-- void setBeforeValue(java.lang.String) 

    /**
     * Sets the value of field 'fieldName'.
     * 
     * @param fieldName the value of field 'fieldName'.
     */
    public void setFieldName(java.lang.String fieldName)
    {
        this._fieldName = fieldName;
    } //-- void setFieldName(java.lang.String) 

}

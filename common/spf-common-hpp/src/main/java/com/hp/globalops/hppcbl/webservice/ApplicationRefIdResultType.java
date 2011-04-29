/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: ApplicationRefIdResultType.java,v 1.1 2006/11/08 07:34:24 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class ApplicationRefIdResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:24 $
 */
public class ApplicationRefIdResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _applicationRefId
     */
    private java.lang.String _applicationRefId;

    /**
     * Field _userId
     */
    private java.lang.String _userId;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationRefIdResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.ApplicationRefIdResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'applicationRefId'.
     * 
     * @return String
     * @return the value of field 'applicationRefId'.
     */
    public java.lang.String getApplicationRefId()
    {
        return this._applicationRefId;
    } //-- java.lang.String getApplicationRefId() 

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
     * Sets the value of field 'applicationRefId'.
     * 
     * @param applicationRefId the value of field 'applicationRefId'
     */
    public void setApplicationRefId(java.lang.String applicationRefId)
    {
        this._applicationRefId = applicationRefId;
    } //-- void setApplicationRefId(java.lang.String) 

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

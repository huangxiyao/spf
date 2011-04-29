/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: AdminViewUserResultTypeChoice.java,v 1.1 2006/11/08 07:34:24 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class AdminViewUserResultTypeChoice.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:24 $
 */
public class AdminViewUserResultTypeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _publicData
     */
    private com.hp.globalops.hppcbl.webservice.PublicData _publicData;

    /**
     * Field _privateData
     */
    private com.hp.globalops.hppcbl.webservice.PrivateData _privateData;


      //----------------/
     //- Constructors -/
    //----------------/

    public AdminViewUserResultTypeChoice() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.AdminViewUserResultTypeChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'privateData'.
     * 
     * @return PrivateData
     * @return the value of field 'privateData'.
     */
    public com.hp.globalops.hppcbl.webservice.PrivateData getPrivateData()
    {
        return this._privateData;
    } //-- com.hp.globalops.hppcbl.webservice.PrivateData getPrivateData() 

    /**
     * Returns the value of field 'publicData'.
     * 
     * @return PublicData
     * @return the value of field 'publicData'.
     */
    public com.hp.globalops.hppcbl.webservice.PublicData getPublicData()
    {
        return this._publicData;
    } //-- com.hp.globalops.hppcbl.webservice.PublicData getPublicData() 

    /**
     * Sets the value of field 'privateData'.
     * 
     * @param privateData the value of field 'privateData'.
     */
    public void setPrivateData(com.hp.globalops.hppcbl.webservice.PrivateData privateData)
    {
        this._privateData = privateData;
    } //-- void setPrivateData(com.hp.globalops.hppcbl.webservice.PrivateData) 

    /**
     * Sets the value of field 'publicData'.
     * 
     * @param publicData the value of field 'publicData'.
     */
    public void setPublicData(com.hp.globalops.hppcbl.webservice.PublicData publicData)
    {
        this._publicData = publicData;
    } //-- void setPublicData(com.hp.globalops.hppcbl.webservice.PublicData) 

}

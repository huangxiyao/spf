/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetSecurityQuestionResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetSecurityQuestionResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetSecurityQuestionResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _securityQuestion
     */
    private java.lang.String _securityQuestion;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetSecurityQuestionResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetSecurityQuestionResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'securityQuestion'.
     * 
     * @return String
     * @return the value of field 'securityQuestion'.
     */
    public java.lang.String getSecurityQuestion()
    {
        return this._securityQuestion;
    } //-- java.lang.String getSecurityQuestion() 

    /**
     * Sets the value of field 'securityQuestion'.
     * 
     * @param securityQuestion the value of field 'securityQuestion'
     */
    public void setSecurityQuestion(java.lang.String securityQuestion)
    {
        this._securityQuestion = securityQuestion;
    } //-- void setSecurityQuestion(java.lang.String) 

}

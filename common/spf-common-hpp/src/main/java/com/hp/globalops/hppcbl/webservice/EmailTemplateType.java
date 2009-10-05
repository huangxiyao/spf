/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: EmailTemplateType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class EmailTemplateType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class EmailTemplateType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _templateType
     */
    private java.lang.String _templateType;

    /**
     * Field _body
     */
    private java.lang.String _body;

    /**
     * Field _subject
     */
    private java.lang.String _subject;

    /**
     * Field _fromAddress
     */
    private java.lang.String _fromAddress;

    /**
     * Field _replyTo
     */
    private java.lang.String _replyTo;

    /**
     * Field _toAddressList
     */
    private java.util.Vector _toAddressList;

    /**
     * Field _ccAddressList
     */
    private java.util.Vector _ccAddressList;

    /**
     * Field _bccAddressList
     */
    private java.util.Vector _bccAddressList;

    /**
     * Field _isHTML
     */
    private boolean _isHTML;

    /**
     * keeps track of state for field: _isHTML
     */
    private boolean _has_isHTML;

    /**
     * Field _encoding
     */
    private java.lang.String _encoding;


      //----------------/
     //- Constructors -/
    //----------------/

    public EmailTemplateType() 
     {
        super();
        _toAddressList = new Vector();
        _ccAddressList = new Vector();
        _bccAddressList = new Vector();
    } //-- com.hp.globalops.hppcbl.webservice.EmailTemplateType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addBccAddress
     * 
     * 
     * 
     * @param vBccAddress
     */
    public void addBccAddress(java.lang.String vBccAddress)
        throws java.lang.IndexOutOfBoundsException
    {
        _bccAddressList.addElement(vBccAddress);
    } //-- void addBccAddress(java.lang.String) 

    /**
     * Method addBccAddress
     * 
     * 
     * 
     * @param index
     * @param vBccAddress
     */
    public void addBccAddress(int index, java.lang.String vBccAddress)
        throws java.lang.IndexOutOfBoundsException
    {
        _bccAddressList.insertElementAt(vBccAddress, index);
    } //-- void addBccAddress(int, java.lang.String) 

    /**
     * Method addCcAddress
     * 
     * 
     * 
     * @param vCcAddress
     */
    public void addCcAddress(java.lang.String vCcAddress)
        throws java.lang.IndexOutOfBoundsException
    {
        _ccAddressList.addElement(vCcAddress);
    } //-- void addCcAddress(java.lang.String) 

    /**
     * Method addCcAddress
     * 
     * 
     * 
     * @param index
     * @param vCcAddress
     */
    public void addCcAddress(int index, java.lang.String vCcAddress)
        throws java.lang.IndexOutOfBoundsException
    {
        _ccAddressList.insertElementAt(vCcAddress, index);
    } //-- void addCcAddress(int, java.lang.String) 

    /**
     * Method addToAddress
     * 
     * 
     * 
     * @param vToAddress
     */
    public void addToAddress(java.lang.String vToAddress)
        throws java.lang.IndexOutOfBoundsException
    {
        _toAddressList.addElement(vToAddress);
    } //-- void addToAddress(java.lang.String) 

    /**
     * Method addToAddress
     * 
     * 
     * 
     * @param index
     * @param vToAddress
     */
    public void addToAddress(int index, java.lang.String vToAddress)
        throws java.lang.IndexOutOfBoundsException
    {
        _toAddressList.insertElementAt(vToAddress, index);
    } //-- void addToAddress(int, java.lang.String) 

    /**
     * Method deleteIsHTML
     * 
     */
    public void deleteIsHTML()
    {
        this._has_isHTML= false;
    } //-- void deleteIsHTML() 

    /**
     * Method enumerateBccAddress
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateBccAddress()
    {
        return _bccAddressList.elements();
    } //-- java.util.Enumeration enumerateBccAddress() 

    /**
     * Method enumerateCcAddress
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateCcAddress()
    {
        return _ccAddressList.elements();
    } //-- java.util.Enumeration enumerateCcAddress() 

    /**
     * Method enumerateToAddress
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateToAddress()
    {
        return _toAddressList.elements();
    } //-- java.util.Enumeration enumerateToAddress() 

    /**
     * Method getBccAddress
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getBccAddress(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _bccAddressList.size())) {
            throw new IndexOutOfBoundsException("getBccAddress: Index value '"+index+"' not in range [0.."+_bccAddressList.size()+ "]");
        }
        
        return (String)_bccAddressList.elementAt(index);
    } //-- java.lang.String getBccAddress(int) 

    /**
     * Method getBccAddress
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getBccAddress()
    {
        int size = _bccAddressList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_bccAddressList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getBccAddress() 

    /**
     * Method getBccAddressCount
     * 
     * 
     * 
     * @return int
     */
    public int getBccAddressCount()
    {
        return _bccAddressList.size();
    } //-- int getBccAddressCount() 

    /**
     * Returns the value of field 'body'.
     * 
     * @return String
     * @return the value of field 'body'.
     */
    public java.lang.String getBody()
    {
        return this._body;
    } //-- java.lang.String getBody() 

    /**
     * Method getCcAddress
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getCcAddress(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _ccAddressList.size())) {
            throw new IndexOutOfBoundsException("getCcAddress: Index value '"+index+"' not in range [0.."+_ccAddressList.size()+ "]");
        }
        
        return (String)_ccAddressList.elementAt(index);
    } //-- java.lang.String getCcAddress(int) 

    /**
     * Method getCcAddress
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getCcAddress()
    {
        int size = _ccAddressList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_ccAddressList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getCcAddress() 

    /**
     * Method getCcAddressCount
     * 
     * 
     * 
     * @return int
     */
    public int getCcAddressCount()
    {
        return _ccAddressList.size();
    } //-- int getCcAddressCount() 

    /**
     * Returns the value of field 'encoding'.
     * 
     * @return String
     * @return the value of field 'encoding'.
     */
    public java.lang.String getEncoding()
    {
        return this._encoding;
    } //-- java.lang.String getEncoding() 

    /**
     * Returns the value of field 'fromAddress'.
     * 
     * @return String
     * @return the value of field 'fromAddress'.
     */
    public java.lang.String getFromAddress()
    {
        return this._fromAddress;
    } //-- java.lang.String getFromAddress() 

    /**
     * Returns the value of field 'isHTML'.
     * 
     * @return boolean
     * @return the value of field 'isHTML'.
     */
    public boolean getIsHTML()
    {
        return this._isHTML;
    } //-- boolean getIsHTML() 

    /**
     * Returns the value of field 'replyTo'.
     * 
     * @return String
     * @return the value of field 'replyTo'.
     */
    public java.lang.String getReplyTo()
    {
        return this._replyTo;
    } //-- java.lang.String getReplyTo() 

    /**
     * Returns the value of field 'subject'.
     * 
     * @return String
     * @return the value of field 'subject'.
     */
    public java.lang.String getSubject()
    {
        return this._subject;
    } //-- java.lang.String getSubject() 

    /**
     * Returns the value of field 'templateType'.
     * 
     * @return String
     * @return the value of field 'templateType'.
     */
    public java.lang.String getTemplateType()
    {
        return this._templateType;
    } //-- java.lang.String getTemplateType() 

    /**
     * Method getToAddress
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getToAddress(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _toAddressList.size())) {
            throw new IndexOutOfBoundsException("getToAddress: Index value '"+index+"' not in range [0.."+_toAddressList.size()+ "]");
        }
        
        return (String)_toAddressList.elementAt(index);
    } //-- java.lang.String getToAddress(int) 

    /**
     * Method getToAddress
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getToAddress()
    {
        int size = _toAddressList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_toAddressList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getToAddress() 

    /**
     * Method getToAddressCount
     * 
     * 
     * 
     * @return int
     */
    public int getToAddressCount()
    {
        return _toAddressList.size();
    } //-- int getToAddressCount() 

    /**
     * Method hasIsHTML
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasIsHTML()
    {
        return this._has_isHTML;
    } //-- boolean hasIsHTML() 

    /**
     * Method removeAllBccAddress
     * 
     */
    public void removeAllBccAddress()
    {
        _bccAddressList.removeAllElements();
    } //-- void removeAllBccAddress() 

    /**
     * Method removeAllCcAddress
     * 
     */
    public void removeAllCcAddress()
    {
        _ccAddressList.removeAllElements();
    } //-- void removeAllCcAddress() 

    /**
     * Method removeAllToAddress
     * 
     */
    public void removeAllToAddress()
    {
        _toAddressList.removeAllElements();
    } //-- void removeAllToAddress() 

    /**
     * Method removeBccAddress
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String removeBccAddress(int index)
    {
        java.lang.Object obj = _bccAddressList.elementAt(index);
        _bccAddressList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeBccAddress(int) 

    /**
     * Method removeCcAddress
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String removeCcAddress(int index)
    {
        java.lang.Object obj = _ccAddressList.elementAt(index);
        _ccAddressList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeCcAddress(int) 

    /**
     * Method removeToAddress
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String removeToAddress(int index)
    {
        java.lang.Object obj = _toAddressList.elementAt(index);
        _toAddressList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeToAddress(int) 

    /**
     * Method setBccAddress
     * 
     * 
     * 
     * @param index
     * @param vBccAddress
     */
    public void setBccAddress(int index, java.lang.String vBccAddress)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _bccAddressList.size())) {
            throw new IndexOutOfBoundsException("setBccAddress: Index value '"+index+"' not in range [0.."+_bccAddressList.size()+ "]");
        }
        _bccAddressList.setElementAt(vBccAddress, index);
    } //-- void setBccAddress(int, java.lang.String) 

    /**
     * Method setBccAddress
     * 
     * 
     * 
     * @param bccAddressArray
     */
    public void setBccAddress(java.lang.String[] bccAddressArray)
    {
        //-- copy array
        _bccAddressList.removeAllElements();
        for (int i = 0; i < bccAddressArray.length; i++) {
            _bccAddressList.addElement(bccAddressArray[i]);
        }
    } //-- void setBccAddress(java.lang.String) 

    /**
     * Sets the value of field 'body'.
     * 
     * @param body the value of field 'body'.
     */
    public void setBody(java.lang.String body)
    {
        this._body = body;
    } //-- void setBody(java.lang.String) 

    /**
     * Method setCcAddress
     * 
     * 
     * 
     * @param index
     * @param vCcAddress
     */
    public void setCcAddress(int index, java.lang.String vCcAddress)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _ccAddressList.size())) {
            throw new IndexOutOfBoundsException("setCcAddress: Index value '"+index+"' not in range [0.."+_ccAddressList.size()+ "]");
        }
        _ccAddressList.setElementAt(vCcAddress, index);
    } //-- void setCcAddress(int, java.lang.String) 

    /**
     * Method setCcAddress
     * 
     * 
     * 
     * @param ccAddressArray
     */
    public void setCcAddress(java.lang.String[] ccAddressArray)
    {
        //-- copy array
        _ccAddressList.removeAllElements();
        for (int i = 0; i < ccAddressArray.length; i++) {
            _ccAddressList.addElement(ccAddressArray[i]);
        }
    } //-- void setCcAddress(java.lang.String) 

    /**
     * Sets the value of field 'encoding'.
     * 
     * @param encoding the value of field 'encoding'.
     */
    public void setEncoding(java.lang.String encoding)
    {
        this._encoding = encoding;
    } //-- void setEncoding(java.lang.String) 

    /**
     * Sets the value of field 'fromAddress'.
     * 
     * @param fromAddress the value of field 'fromAddress'.
     */
    public void setFromAddress(java.lang.String fromAddress)
    {
        this._fromAddress = fromAddress;
    } //-- void setFromAddress(java.lang.String) 

    /**
     * Sets the value of field 'isHTML'.
     * 
     * @param isHTML the value of field 'isHTML'.
     */
    public void setIsHTML(boolean isHTML)
    {
        this._isHTML = isHTML;
        this._has_isHTML = true;
    } //-- void setIsHTML(boolean) 

    /**
     * Sets the value of field 'replyTo'.
     * 
     * @param replyTo the value of field 'replyTo'.
     */
    public void setReplyTo(java.lang.String replyTo)
    {
        this._replyTo = replyTo;
    } //-- void setReplyTo(java.lang.String) 

    /**
     * Sets the value of field 'subject'.
     * 
     * @param subject the value of field 'subject'.
     */
    public void setSubject(java.lang.String subject)
    {
        this._subject = subject;
    } //-- void setSubject(java.lang.String) 

    /**
     * Sets the value of field 'templateType'.
     * 
     * @param templateType the value of field 'templateType'.
     */
    public void setTemplateType(java.lang.String templateType)
    {
        this._templateType = templateType;
    } //-- void setTemplateType(java.lang.String) 

    /**
     * Method setToAddress
     * 
     * 
     * 
     * @param index
     * @param vToAddress
     */
    public void setToAddress(int index, java.lang.String vToAddress)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _toAddressList.size())) {
            throw new IndexOutOfBoundsException("setToAddress: Index value '"+index+"' not in range [0.."+_toAddressList.size()+ "]");
        }
        _toAddressList.setElementAt(vToAddress, index);
    } //-- void setToAddress(int, java.lang.String) 

    /**
     * Method setToAddress
     * 
     * 
     * 
     * @param toAddressArray
     */
    public void setToAddress(java.lang.String[] toAddressArray)
    {
        //-- copy array
        _toAddressList.removeAllElements();
        for (int i = 0; i < toAddressArray.length; i++) {
            _toAddressList.addElement(toAddressArray[i]);
        }
    } //-- void setToAddress(java.lang.String) 

}

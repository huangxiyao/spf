/* 
  * CDDL HEADER START
  * The contents of this file are subject to the terms
  * of the Common Development and Distribution License 
  * (the License). You may not use this file except in
  * compliance with the License.
  *
  * You can obtain a copy of the License at
  * http://www.sun.com/cddl/cddl.html and legal/CDDLv1.0.txt
  * See the License for the specific language governing
  * permission and limitations under the License.
  *
  * When distributing Covered Code, include this CDDL 
  * Header Notice in each file and include the License file  
  * at legal/CDDLv1.0.txt.                                                           
  * If applicable, add the following below the CDDL Header,
  * with the fields enclosed by brackets [] replaced by
  * your own identifying information: 
  * "Portions Copyrighted [year] [name of copyright owner]"
  *
  * Copyright 2009 Sun Microsystems Inc. All Rights Reserved
  * CDDL HEADER END
 */

package com.sun.portal.wsrp.producer.markup.impl;

import com.sun.portal.wsrp.common.stubs.v2.*;

import com.sun.portal.wsrp.producer.ProducerException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.List;

import javax.mail.MessagingException;

import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import javax.servlet.ServletInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * The <code>ProducerRequestWrapper</code> is for wrapping the original
 * HttpServletRequest before passing it to the PortletContainer.
 * A wrapper is needed to make data in UploadContext available to 
 * getInputStream(), getReader(), getContextType() and getContentLength().
 **/
public class ProducerRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest _request;
    private String _mimeType;
    private byte[] _data;
    private int _length = 0;
    private boolean _gotReader = false;
    private boolean _gotStream = false;
    //private ProviderContext _pc = null;

    public ProducerRequestWrapper( HttpServletRequest request,
                                   InteractionParams interactionParams )
        throws ProducerException
    {
        super( request );
        _request = request;        
        UploadContext[] uploadContexts = (UploadContext []) interactionParams.
                getUploadContexts().toArray(new UploadContext[0]);
        NamedString[] formParams = (NamedString[])interactionParams.
                getFormParameters().toArray(new NamedString[0]);

        MimeMultipart parts = aggregateUploadData( uploadContexts, formParams );
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        try {
            parts.writeTo( byteOutStream );
        } catch (IOException ioe) {
            throw new ProducerException(
                 "ProducerRequestWrapper.ProducerRequestWrapper() ", ioe );
        } catch (MessagingException me) {
            throw new ProducerException(
                 "ProducerRequestWrapper.ProducerRequestWrapper() ", me);
        }
        _data = byteOutStream.toByteArray();
        _mimeType = parts.getContentType();
        _length = _data.length;
    }

    public ServletInputStream getInputStream()
        throws IOException
    {
        if( _gotReader ) {
            throw new IllegalStateException();
        }
        if( _length == 0 ) {
            throw new IOException();
        }
        _gotStream = true;
        return new ProducerServletInputStream( _data );
    }

    public int getContentLength() {
        return _length;
    }

    public String getContentType() {
        return _mimeType;
    }

    public BufferedReader getReader()
        throws IOException
    {
        if( _gotStream ) {
            throw new IllegalStateException();
        }
        if( _length == 0 ) {
            throw new IOException();
        }
        _gotReader = true;
        ByteArrayInputStream inputStream =
               new ByteArrayInputStream( _data );
        InputStreamReader streamReader =
               new InputStreamReader( inputStream );
        return new BufferedReader( streamReader );
    }

    /**
     * Aggregate the uploadContext array into one MimeMultipart.
     **/
    private MimeMultipart aggregateUploadData( UploadContext[] uploadContexts, NamedString[] formParams )
        throws ProducerException
    {

        MimeMultipart parts = new MimeMultipart();

        // aggregate the upload context information into the multipart request

        if( uploadContexts != null ) {
            for( int i = 0; i < uploadContexts.length; i++ ) {
                UploadContext uploadContext = uploadContexts[i];

                InternetHeaders headers = new InternetHeaders();
                headers.addHeader( "Content-Type", uploadContext.getMimeType() );
    
                List attributes = uploadContext.getMimeAttributes();
                if( attributes != null && attributes.size() > 0 ) {
                    for( int j = 0; j < attributes.size(); j++ ) {
                        NamedString attribute = (NamedString)attributes.get(j);
                        try {
                            headers.addHeader( attribute.getName(), new String(attribute.getValue().getBytes(_request.getCharacterEncoding()), "iso-8859-1") );
                        } catch (UnsupportedEncodingException ex) {
                            // it should never happen
                            throw new ProducerException("ProducerRequestWrapper.aggregateUploadData() ", ex);
                        }
                    }
                }

                try {
                    MimeBodyPart mimeBodyPart =
                          new MimeBodyPart( headers, uploadContext.getUploadData() );
                    parts.addBodyPart( mimeBodyPart );
                } catch( MessagingException me ) {
                    throw new ProducerException(
                         "ProducerRequestWrapper.aggregateUploadData() ", me);
                } 
            }
         }

        // aggregate the form parameters into the multipart request

        if( formParams != null ) {
            for( int i = 0; i < formParams.length; i++ ) {
                NamedString formParam = formParams[i];
                InternetHeaders headers = new InternetHeaders();

                StringBuffer sb = new StringBuffer( "form-data; name=\"" );
                sb.append( formParam.getName() );
                sb.append( "\"" );

                headers.addHeader( "Content-Disposition", sb.toString() );

                try {
                	// The fix made by Liu, Ye (ye.liu@hp.com)
                	// pass encoding to the getBytes method below
                    MimeBodyPart mimeBodyPart =
                      new MimeBodyPart( headers, formParam.getValue().getBytes(_request.getCharacterEncoding()) );
                    parts.addBodyPart( mimeBodyPart );
                } catch( MessagingException me ) {
                    throw new ProducerException(
                     "ProducerRequestWrapper.aggregateUploadData() ", me);
                } catch (UnsupportedEncodingException e) {
                	throw new ProducerException(
                            "ProducerRequestWrapper.aggregateUploadData() ", e);
        		}
            }
        }
        return parts;
    }
}


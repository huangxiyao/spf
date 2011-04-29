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

import com.sun.portal.container.*;

import com.sun.portal.wsrp.common.Util;
import com.sun.portal.wsrp.common.WSRPLogger;
import com.sun.portal.wsrp.common.WSRPSpecKeys;

import com.sun.portal.wsrp.common.stubs.v2.*;

import java.net.URLEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerChannelURL implements ChannelURL {

    private Map<String, String[]> _params = null;
    private ChannelURLType _action;
    private ChannelMode _newChannelMode;
    private ChannelState _newWindowState;
    private boolean _secure = false;
    private Templates _templates = null;
    private Map _baseMap = null;
    private boolean _requiresConsumerRewriting = false;
    private String cacheLevel;
    private String resourceID;
	private List<String> publicParamIdentifiers;
	
    private static Logger logger = WSRPLogger.getLogger (
			ProducerChannelURL.class, "logmessages");
	
    public ProducerChannelURL( Templates templates, Map baseParamMap,
			List<String> publicParamIdentifierList) {
        _templates = templates;
        _baseMap = baseParamMap;
		publicParamIdentifiers = publicParamIdentifierList;
    }

    public void setChannelMode(ChannelMode newChannelMode) {
        _newChannelMode = newChannelMode;
    }

    public void setWindowState(ChannelState newWindowState) {
        _newWindowState = newWindowState;
    }

    public void setURLType(ChannelURLType type) {
        _action = type;
    }

    public void setParameter(String name, String value) {
		//Special handling for null value
		//A parameter value of null indicates that this parameter 
		//should be removed. 
		
		String[] values = null;
		
		if(value != null) {
			values = new String[1];
			values[0] = value;
		}
        setParameter( name, values );
    }

    public void setParameter(String name, String[] values) {
        if (_params == null) {
            _params = new HashMap<String, String[]>();
        }
        _params.put(name, values);
    }

    public void setParameters(Map<String, String[]> parametersMap) {
        _params = parametersMap;
    }

    public void setSecure(boolean secure) {
        _secure = secure;
    }

    public ChannelState getWindowState() {
        return _newWindowState;
    }

    public ChannelMode getChannelMode() {
        return _newChannelMode;
    }

    public ChannelURLType getURLType() {
        return _action;
    }

    public Map<String, String[]> getParameters() {
        return _params;
    }

    public boolean isSecure () {
        return _secure;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public void setCacheLevel(String cacheLevel) {
        this.cacheLevel = cacheLevel;
    }

    public String getCacheLevel() {
        return this.cacheLevel;
    }

    public String toString() {

        String mode = null;
        String state = null;
        String privateParamStr = null;
        String publicParamStr = null;
        String template = null;

        // Determine whether to do producer rewrite or consumer rewrite.
        if( _templates != null ) {
            if( _action != null ) {
                if(  _action.equals( ChannelURLType.ACTION ) ) {
                    if( _secure ) {
                        template = _templates.getSecureBlockingActionTemplate();
                    } else {
                        template = _templates.getBlockingActionTemplate();
                    }
                } else if ( _action.equals( ChannelURLType.RENDER ) ) {
                    if( _secure ) {
                        template = _templates.getSecureRenderTemplate();
                    } else {
                        template = _templates.getRenderTemplate();
                    }
                } else if ( _action.equals( ChannelURLType.RESOURCE ) ) {
                    if( _secure ) {
                        template = _templates.getSecureResourceTemplate();
                    } else {
                        template = _templates.getResourceTemplate();
                    } 
                } else {
                	if (logger.isLoggable(Level.SEVERE))
						logger.log(Level.SEVERE, "PSWS_CSPWPMI0003", _action);
		    
                    throw new RuntimeException( 
							"ProducerChannelURL.toString(): unknown URL type " 
								+ _action );
                }
            } else {
            	if (logger.isLoggable(Level.SEVERE))
					logger.log(Level.SEVERE, "PSWS_CSPWPMI0004");
                throw new RuntimeException( "ProducerChannelURL.toString(): null type." );
            }
        }

        // Get the channel mode and window state in WSRP form
        if( _newChannelMode != null ) {
            mode = WSRPToContainerMap.mapChannelModeToWSRP( _newChannelMode );
            if( mode == null ) {
                mode = _newChannelMode.toString();
            }
        }

        if( _newWindowState != null ) {
            state = WSRPToContainerMap.mapWindowStateToWSRP( _newWindowState );
            if( state == null ) {
                state = _newWindowState.toString();
            }
        }

        Map privateParams = getPrivateParameterMap();
        privateParamStr = Util.getStringFromMap( privateParams );
		privateParamStr = Util.doURLEncode( privateParamStr );
        
        Map publicParams = getPublicParameterMap();
        publicParamStr = Util.getNavigationalValuesStringFromMap( publicParams );
		publicParamStr = Util.doURLEncode( publicParamStr );
        
			
		// If we can a template here, that means we can do producer rewrite
        if( template == null ) {
            // Comsumer rewrite

            _requiresConsumerRewriting = true;

            StringBuffer urlsb = new StringBuffer( WSRPSpecKeys.URL_REWRITE_START );
            urlsb.append( WSRPSpecKeys.URL_TYPE );
            urlsb.append( "=" );

            if( _action.equals( ChannelURLType.ACTION ) ) {
                urlsb.append( WSRPSpecKeys.URL_TYPE_BLOCKING_ACTION );
                if( privateParams != null && !privateParams.isEmpty()) {
                    urlsb.append( "&" );
                    urlsb.append( WSRPSpecKeys.INTERACTION_STATE );
                    urlsb.append( "=" );
                    urlsb.append( privateParamStr );
                }
            } else if ( _action.equals( ChannelURLType.RENDER ) ) {
                urlsb.append( WSRPSpecKeys.URL_TYPE_RENDER );
                if( privateParams != null && !privateParams.isEmpty()) {
                    urlsb.append( "&" );
                    urlsb.append( WSRPSpecKeys.NAVIGATIONAL_STATE );
                    urlsb.append( "=" );
                    urlsb.append( privateParamStr );
                }
            }else if ( _action.equals( ChannelURLType.RESOURCE ) ) {
                //TODO: what about resource id and resourceCacheLevel ??
                urlsb.append( WSRPSpecKeys.URL_TYPE_RESOURCE );
                if( privateParams != null && !privateParams.isEmpty()) {
                    urlsb.append( "&" );
                    urlsb.append( WSRPSpecKeys.RESOURCE_STATE );
                    urlsb.append( "=" );
                    urlsb.append( privateParamStr );
                }
                // add wsrp-resourceID and wsrp-resourceCacheability parameters for consumer 
                // rewritten resource URLs (text/plain)
                // this fix is made by Slawek
                if ( resourceID != null ) {
                	urlsb.append( '&' );
                	urlsb.append( WSRPSpecKeys.GENERIC_RESOURCE_ID );
                	urlsb.append( '=' );
                	urlsb.append( URLEncoder.encode(resourceID) );
                }
                if ( cacheLevel != null ) {
                	urlsb.append( '&' );
                	urlsb.append( WSRPSpecKeys.GENERIC_RESOURCE_CACHE_LEVEL );
                	urlsb.append( '=' );
                	urlsb.append( cacheLevel );
                }
            }

            if( _newChannelMode != null ) {
                urlsb.append( "&" );
                urlsb.append( WSRPSpecKeys.MODE );
                urlsb.append( "=" );
                urlsb.append( mode );
            }

            if( _newWindowState != null ) {
                urlsb.append( "&" );
                urlsb.append( WSRPSpecKeys.WINDOW_STATE );
                urlsb.append( "=" );
                urlsb.append( state );
            }

            if( _secure ) {
                urlsb.append( "&" );
                urlsb.append( WSRPSpecKeys.SECURE_URL );
                urlsb.append( "=" );
                urlsb.append( WSRPSpecKeys.SECURE_URL_TRUE );
            }

            urlsb.append( WSRPSpecKeys.URL_REWRITE_END );
            return urlsb.toString();
			
        } else {
            //Producer rewriting

            _requiresConsumerRewriting = false;

            Map paramMap = new HashMap();
            paramMap.putAll( _baseMap );
            paramMap.put( WSRPSpecKeys.MODE, mode );
            paramMap.put( WSRPSpecKeys.WINDOW_STATE, state );
            
			if( _action.equals( ChannelURLType.ACTION ) ) {
				
                paramMap.put( WSRPSpecKeys.INTERACTION_STATE, privateParamStr );
                paramMap.put( WSRPSpecKeys.NAVIGATIONAL_VALUES, publicParamStr );				
                paramMap.put( 
						WSRPSpecKeys.URL_TYPE, 
						WSRPSpecKeys.URL_TYPE_BLOCKING_ACTION);
				
            }else if( _action.equals( ChannelURLType.RESOURCE ) ) {
				
                paramMap.put( WSRPSpecKeys.RESOURCE_STATE, privateParamStr );
                paramMap.put( WSRPSpecKeys.URL_TYPE, WSRPSpecKeys.URL_TYPE_RESOURCE);
				if(resourceID != null){
					paramMap.put( WSRPSpecKeys.GENERIC_RESOURCE_ID, 
						URLEncoder.encode(resourceID));
				}
                paramMap.put( WSRPSpecKeys.GENERIC_RESOURCE_CACHE_LEVEL, cacheLevel);
				
            }else {
				
                paramMap.put( WSRPSpecKeys.NAVIGATIONAL_STATE, privateParamStr );
                paramMap.put( WSRPSpecKeys.NAVIGATIONAL_VALUES, publicParamStr );				
                paramMap.put( WSRPSpecKeys.URL_TYPE, WSRPSpecKeys.URL_TYPE_RENDER );
				
            }
			
            ProducerRewriter pr = new ProducerRewriter(  );
            return pr.rewrite( template, paramMap );
			
        }
    }

    /**
     * Determines whether consumer rewriting is required for this
     * channel URL.
	 *
	 * @return boolean
	 */
    public boolean requiresConsumerRewriting( ) {
        return _requiresConsumerRewriting;
    }

    public void setProperty(String name, String value) {
        //TODO
    }

    public void addProperty(String name, String value) {
        //TODO
    }

    public Map<String, List<String>> getProperties() {
        //TODO
        return Collections.emptyMap();
    }
	
	private Map<String, String[]> getPublicParameterMap(){
		Map publicParamMap = new HashMap();
		//Return empty map, if there is no public param for the portlet.
		if(publicParamIdentifiers.isEmpty()){
			return publicParamMap;
		}
		
		if(_params != null && _params.size() > 0){
			for (Map.Entry<String, String[]> entry : _params.entrySet()){
				if(publicParamIdentifiers.contains(entry.getKey())){
					publicParamMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		return publicParamMap;		
	}
	
	private Map<String, String[]> getPrivateParameterMap(){
		
		//Return param map, if there is no public param for the portlet.
		if(publicParamIdentifiers.isEmpty()){
			return _params;
		}
		
		Map privateParamMap = new HashMap();
		if(_params != null && _params.size() > 0){
			for (Map.Entry<String, String[]> entry : _params.entrySet()){
				if(!publicParamIdentifiers.contains(entry.getKey())){
					privateParamMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		return privateParamMap;		
		
	}
	
}

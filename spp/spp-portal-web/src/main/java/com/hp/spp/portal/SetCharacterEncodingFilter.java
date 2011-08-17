package com.hp.spp.portal;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hp.spp.portal.characterencoding.EncodingResponseWrapper;
	
		/*
		* Copyright 2004 The Apache Software Foundation
		*
		* Licensed under the Apache License, Version 2.0 (the "License");
		* you may not use this file except in compliance with the License.
		* You may obtain a copy of the License at
		*
		*     http://www.apache.org/licenses/LICENSE-2.0
		*
		* Unless required by applicable law or agreed to in writing, software
		* distributed under the License is distributed on an "AS IS" BASIS,
		* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		* See the License for the specific language governing permissions and
		* limitations under the License.
		*/


		
		/**
		 * <p>Example filter that sets the character encoding to be used in parsing the
		 * incoming request, either unconditionally or only if the client did not
		 * specify a character encoding.  Configuration of this filter is based on
		 * the following initialization parameters:</p>
		 * <ul>
		 * <li><strong>encoding</strong> - The character encoding to be configured
		 *     for this request, either conditionally or unconditionally based on
		 *     the <code>ignore</code> initialization parameter.  This parameter
		 *     is required, so there is no default.</li>
		 * <li><strong>ignore</strong> - If set to "true", any character encoding
		 *     specified by the client is ignored, and the value returned by the
		 *     <code>selectEncoding()</code> method is set.  If set to "false,
		 *     <code>selectEncoding()</code> is called <strong>only</strong> if the
		 *     client has not already specified an encoding.  By default, this
		 *     parameter is set to "true".</li>
		 * </ul>
		 *
		 * <p>Although this filter can be used unchanged, it is also easy to
		 * subclass it and make the <code>selectEncoding()</code> method more
		 * intelligent about what encoding to choose, based on characteristics of
		 * the incoming request (such as the values of the <code>Accept-Language</code>
		 * and <code>User-Agent</code> headers, or a value stashed in the current
		 * user's session.</p>
		 *
		 * @author Craig McClanahan
		 * @version $Revision: 1.1 $ $Date: 2006/09/27 07:52:48 $
		 */
		public class SetCharacterEncodingFilter implements Filter {
			
			
		    // ----------------------------------------------------- Instance Variables

			private static Logger mLog = Logger.getLogger(SetCharacterEncodingFilter.class);

		    /**
		     * The default character encoding to set for requests that pass through
		     * this filter.
		     */
		    protected String mEncoding = null;


		    /**
		     * The filter configuration object we are associated with.  If this value
		     * is null, this filter instance is not currently configured.
		     */
		    protected FilterConfig mFilterConfig = null;


		    /**
		     * Should a character encoding specified by the client be ignored?
		     */
		    protected boolean mIgnore = true;


		    // --------------------------------------------------------- Public Methods


		    /**
		     * Take this filter out of service.
		     */
		    public void destroy() {	
		        this.mEncoding = null;
		        this.mFilterConfig = null;
		    }


		    /**
		     * Select and set (if specified) the character encoding to be used to
		     * interpret request parameters for this request.
		     *
		     * @param request The servlet request we are processing
		     * @param result The servlet response we are creating
		     * @param chain The filter chain we are processing
		     *
		     * @exception IOException if an input/output error occurs
		     * @exception ServletException if a servlet error occurs
		     */
		    public void doFilter(ServletRequest request, ServletResponse response,
		                         FilterChain chain)
			throws IOException, ServletException {
		    	
		    	HttpServletRequest mRequest= (HttpServletRequest)request;
		    	
//				if (ApplicationParameters.IS_DEBUG_ENABLED) {
//		        logger.logDebug(null, " Encoding : doFilter ");
//				}
		        // Conditionally select and set the character encoding to be used
		    	
		        if (mIgnore || (request.getCharacterEncoding() == null)) {
		            String encoding = selectEncoding(request);
			    	if (encoding != null){
		            request.setCharacterEncoding(encoding);
		            }
		        }
		 
		        if (mLog.isDebugEnabled()) {
		        	StringBuilder sb = new StringBuilder("\nsetContentType: ").append(response.getCharacterEncoding());
					sb.append("\nrequest: ").append(mRequest.getRequestURI());
					sb.append("\ninclude: ").append(mRequest.getAttribute("javax.servlet.include.request_uri"));
					sb.append("\nforward: ").append(mRequest.getAttribute("javax.servlet.forward.request_uri"));
					sb.append("\n_spp_charset: ").append(request.getParameter("_spp_charset"));
					mLog.debug(sb);
		        }
		        
//		      	Pass control on to the next filter
//				response wrapper is used for handling Eservice Encoding 
//				response.setContentType(request,response) was not able to server the purpose

//				in case there is parameter present in the request,then change the content type otherwise pass as it is
		        if (request.getParameter("_spp_charset")!=null){
		        	EncodingResponseWrapper wrapper=new EncodingResponseWrapper((HttpServletResponse)response, request.getParameter("_spp_charset"));
		        	wrapper.setContentType("text/html; charset="+request.getParameter("_spp_charset"));
		        	chain.doFilter(request,wrapper);
	            }else{

				// FIX ME - Pranav 
				// Hard Coding the UTF-8 Character Set in case there is no contentType found
				// Due to the caching junk characters were coming on public page
	             	EncodingResponseWrapper wrapper1=new EncodingResponseWrapper((HttpServletResponse)response, mEncoding);
		        	wrapper1.setContentType("text/html; charset=" + mEncoding);
		           	chain.doFilter(request,wrapper1);
	            }
	     
		    }


		    /**
		     * Place this filter into service.
		     *
		     * @param filterConfig The filter configuration object
		     */
		    public void init(FilterConfig filterConfig) throws ServletException {

			
				this.mFilterConfig = filterConfig;
		        this.mEncoding = filterConfig.getInitParameter("encoding");
		        String value  = filterConfig.getInitParameter("ignore");

				mLog.debug("Encoding : init ["+this.mEncoding +"]");
				
		        if (value == null) {
		            this.mIgnore = true;
				}
		        else if (value.equalsIgnoreCase("true")) {
		            this.mIgnore = true;
				}
		        else if (value.equalsIgnoreCase("yes")){
		            this.mIgnore = true;
				}
		        else{
		            this.mIgnore = false;
				}

		    }


		    // ------------------------------------------------------ Protected Methods


		    /**
		     * Select an appropriate character encoding to be used, based on the
		     * characteristics of the current request and/or filter initialization
		     * parameters.  If no character encoding should be set, return
		     * <code>null</code>.
		     * <p>
		     * The default implementation unconditionally returns the value configured
		     * by the <strong>encoding</strong> initialization parameter for this
		     * filter.
		     *
		     * @param request The servlet request we are processing
		     */
		    protected String selectEncoding(ServletRequest request) {
		        return (this.mEncoding);
		    }


		}


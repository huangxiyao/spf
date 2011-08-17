package com.hp.spp.portal.characterencoding;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;

public class EncodingResponseWrapper extends HttpServletResponseWrapper {
	private static final Logger mLog = Logger.getLogger(EncodingResponseWrapper.class);

	private String mEserviceEncoding;

	public EncodingResponseWrapper(HttpServletResponse response, String encoding) {
		super(response);
		mEserviceEncoding = encoding;
		if (mLog.isDebugEnabled()) {
			mLog.debug("Encoding received in EncodingResponseWrapper Class : "+ encoding);
		}
	}

	public void setContentType(String contentType) {
		
		if(mEserviceEncoding!=null){
			super.setContentType("text/html; charset="+mEserviceEncoding);
		}
		else{
			// Hard Coded to fix the junk character being displayed on public page
			// Due to caching
			super.setContentType("text/html; charset=UTF-8");
		}
	}
	
}

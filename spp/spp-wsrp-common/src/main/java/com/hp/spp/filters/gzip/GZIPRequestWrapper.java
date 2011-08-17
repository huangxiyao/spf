package com.hp.spp.filters.gzip;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GZIPRequestWrapper extends HttpServletRequestWrapper {
	private ServletInputStream mInputStream;
	private BufferedReader mReader;

	public GZIPRequestWrapper(HttpServletRequest req) throws IOException
	{
		super(req);
		mInputStream = new GZIPRequestStream(req);
		String characterEncoding = req.getCharacterEncoding();
		if (characterEncoding == null) {
			characterEncoding = "UTF-8";
		}
		mReader = new BufferedReader(new InputStreamReader(mInputStream, characterEncoding));
	}

	public ServletInputStream getInputStream() throws IOException
	{
		return mInputStream;
	}

	public BufferedReader getReader() throws IOException
	{
		return mReader;
	}

}

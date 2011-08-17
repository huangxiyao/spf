package com.hp.spp.filters.gzip;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.PrintWriter;
import java.io.IOException;

public class GZIPResponseWrapper extends HttpServletResponseWrapper {
	private ServletOutputStream mOutputStream;
	private PrintWriter mWriter;

	public GZIPResponseWrapper(HttpServletResponse response)
	{
		super(response);
	}

	public ServletOutputStream getOutputStream() throws IOException
	{
		if(mOutputStream == null) {
			mOutputStream = new GZIPResponseStream((HttpServletResponse)getResponse());
		}
		return mOutputStream;
	}

	public PrintWriter getWriter()
		throws IOException
	{
		if(mWriter == null) {
			mWriter = new PrintWriter(getOutputStream());
		}
		return mWriter;
	}
}

package com.hp.spp.filters.gzip;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZIPResponseStream extends ServletOutputStream {
	private GZIPOutputStream mOutputStream;

	public GZIPResponseStream(HttpServletResponse response) throws IOException
	{
		mOutputStream = new GZIPOutputStream(response.getOutputStream());
		response.addHeader("Content-Encoding", "gzip");
	}

	public void write(int b) throws IOException
	{
		mOutputStream.write(b);
	}

	public void write(byte b[]) throws IOException
	{
		mOutputStream.write(b);
	}

	public void write(byte b[], int off, int len) throws IOException
	{
		mOutputStream.write(b, off, len);
	}

	public void close() throws IOException
	{
		finish();
		mOutputStream.close();
	}

	public void flush() throws IOException
	{
		mOutputStream.flush();
	}

	public void finish() throws IOException
	{
		mOutputStream.finish();
	}
}

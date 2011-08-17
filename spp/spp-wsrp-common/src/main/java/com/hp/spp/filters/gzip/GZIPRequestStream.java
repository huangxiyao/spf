package com.hp.spp.filters.gzip;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GZIPRequestStream extends ServletInputStream {
	private GZIPInputStream mInputStream;

	public GZIPRequestStream(HttpServletRequest request) throws IOException
	{
		ServletInputStream inputStream = request.getInputStream();
		mInputStream = new GZIPInputStream(inputStream);
	}

	public int read() throws IOException
	{
		return mInputStream.read();
	}

	public int read(byte b[]) throws IOException
	{
		return mInputStream.read(b);
	}

	public int read(byte b[], int off, int len) throws IOException
	{
		return mInputStream.read(b, off, len);
	}

	public void close() throws IOException
	{
		mInputStream.close();
	}

}

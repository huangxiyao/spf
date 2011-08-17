package com.hp.spp.wsrp.export.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Reader;

public class InputStreamUtils {

	public static File getFile(InputStream in) throws IOException {
		if (in == null)
			throw new IOException("Null InputStream to read");
		OutputStream out = null;
		File file;
		try {
			File tmpFile = File.createTempFile("default", ".tmp");
			out = new BufferedOutputStream(new FileOutputStream(tmpFile));
			byte byteBuffer[] = new byte[1024];
			int len;
			while ((len = in.read(byteBuffer)) > -1) {
				out.write(byteBuffer, 0, len);
				out.flush();
			}
			file = tmpFile;
			tmpFile = null;
		} finally {
			if (out != null)
				out.close();
		}
		return file;
	}

	public static Object getObject(InputStream in) throws IOException, ClassNotFoundException {
		if (in == null)
			throw new NullPointerException("Null InputStream to read");
		
		ObjectInputStream ois = new ObjectInputStream(in) ;
		Object object = ois.readObject();
		
		return object ;
	}

	public static Reader getReader(InputStream inputStream) {
		return new BufferedReader(new InputStreamReader(inputStream));
	}

}

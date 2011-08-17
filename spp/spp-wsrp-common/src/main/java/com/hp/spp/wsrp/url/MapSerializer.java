package com.hp.spp.wsrp.url;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Helper to serialize the parameter map of the portlet url. See
 * org.apache.wsrp4j.producer.util.MapSerializer to have the original sourcecode.
 * 
 * @author mathieu.vidal@hp.com
 */
public class MapSerializer {

	/**
	 * Serialize the parameters.
	 * 
	 * @param parameters - the parameters in Map form
	 * @return the parameters serialized
	 * @throws IOException - if the creation or manipulation of the outputStream is going wrong
	 */
	public static byte[] serialize(Map parameters) throws IOException {
		byte[] result = null;

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bytes);
		out.writeObject(parameters);
		out.flush();
		result = bytes.toByteArray();
		out.close();
		return result;
	}
}

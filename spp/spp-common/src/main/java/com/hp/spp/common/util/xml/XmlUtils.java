package com.hp.spp.common.util.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Element;

public class XmlUtils {

	public static byte[] xmlToByteArray(Element element) throws IOException {
		// transformation du XmlDocument
		OutputFormat of = new OutputFormat("XML", "ISO-8859-1", true);
		of.setLineWidth(0);
		// transforme le document xml en un byte[]
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		XMLSerializer serializer = new XMLSerializer(byteStream, of);

		serializer.serialize(element);
		byte[] xmlByte = byteStream.toByteArray();

		// Close the byteStream
		byteStream.close();
		return xmlByte;
	}
}

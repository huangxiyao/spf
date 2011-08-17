package com.hp.spp.eservice.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.services.MarshalService;

public class CastorMarshalServiceImpl implements MarshalService {

	private static Logger logger = Logger.getLogger(CastorMarshalServiceImpl.class);

	public byte[] marshalEServices(Site site) {
		return marshalSite(site, "EServiceCastorMapping.xml");
	}

	private byte[] marshalSite(Site site, String mappingFile) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(out);
		Mapping map = new Mapping();
		try {
			URL u = getClass().getClassLoader().getResource(mappingFile);
			map.loadMapping(u);
			Marshaller marshaller = new Marshaller(writer);
			marshaller.setMapping(map);
			try {
				marshaller.marshal(site);
			} catch (MarshalException e) {
				logger.error("Could not marshal site", e);
			} catch (ValidationException e) {
				logger.error("Validation error marshalling site", e);
			}
		} catch (IOException e) {
			logger.error("IO exception marshalling site", e);
		} catch (MappingException e) {
			logger.error("Mapping error marshalling site. "
					+ "Mapping file: " + mappingFile, e);
		}

		return out.toByteArray();
	}

	public byte[] marshalStandardParameterSets(Site site) {
		return marshalSite(site, "StandardParameterSetCastorMapping.xml");
	}

}

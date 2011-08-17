package com.hp.spp.wsrp.export.carfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.hp.spp.wsrp.export.component.BadComponent;
import com.hp.spp.wsrp.export.component.ComponentReader;
import com.hp.spp.wsrp.export.component.mapping.Component;
import com.hp.spp.wsrp.export.exception.CarFileException;
import com.hp.spp.wsrp.export.exception.ComponentException;
import com.hp.spp.wsrp.export.util.InputStreamUtils;
import com.hp.spp.wsrp.export.util.car.CarEntry;
import com.hp.spp.wsrp.export.util.car.CarFile;

public class CarFileReader {
	
	private ComponentReader mComponentReader = null ;
	
	public CarFileReader() {
		mComponentReader = new ComponentReader() ;
	}

	public List getComponentList(CarFile carFile) throws CarFileException {
		return recursiveCarFileExplorer(carFile, new ArrayList(), "component.xml");
	}
	
	public List getSerializedObject(CarFile carFile) throws CarFileException {
		return recursiveCarFileExplorer(carFile, new ArrayList(), ".ser");
	}
	
	private List recursiveCarFileExplorer(CarFile carFile, List components, String searchCriteria) throws CarFileException {
		if (carFile == null)
			throw new NullPointerException("CarFile cannot be null");
		if (components == null)
			components = new ArrayList();

		try {
			Enumeration entries = carFile.entries();
			CarEntry entry;

			while (entries.hasMoreElements()) {
				entry = (CarEntry) entries.nextElement();

				if (entry.isDirectory()) {
					continue; // Do nothing...
				} else if (entry.getName().toLowerCase().endsWith(".car")) {
					File file = InputStreamUtils.getFile(carFile.getInputStream(entry));
					CarFile newCarFile = new CarFile(file);
					recursiveCarFileExplorer(newCarFile, components, searchCriteria);
					newCarFile.close();
					file.delete();
				} else if (entry.getName().toLowerCase().endsWith(searchCriteria)) {
					InputStream stream = carFile.getInputStream(entry);
					Object readFile = null ;
					if(searchCriteria.endsWith(".xml")) {
						readFile = readComponent(stream) ;
					} else if(searchCriteria.endsWith(".ser")) {
						readFile = readSerializedObject(stream) ;
					}
					if(readFile != null)
						components.add(readFile) ;
				}
			}
		} catch (IOException e) {
			throw new CarFileException(e) ;
		}
		
		return components ;
	}

	private Object readComponent(InputStream stream) throws CarFileException {
		Component component = null ;
		try {
			component = mComponentReader.readComponentFromStream(stream);
		} catch (ComponentException e) {
			BadComponent badComponent = e.getBadComponent() ;
			if(badComponent != null) {
				return badComponent ;
			}
			throw new CarFileException("ComponentException encountered while retrieving Component from Stream.", e) ;
		}
		return component;
	}

	private Object readSerializedObject(InputStream stream) throws CarFileException {
		try {
			return InputStreamUtils.getObject(stream) ;
		} catch (IOException e) {
			throw new CarFileException("IOException encountered while retrieving Object from Stream.", e);
		} catch (ClassNotFoundException e) {
			throw new CarFileException("ClassNotFoundException encountered while retrieving Object from Stream.", e);
		}
	}

}

package com.hp.spp.wsrp.export.util.car;

import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;

public class CarEntry extends JarEntry {

	public CarEntry(String name) {
		super(name);
	}

	public CarEntry(JarEntry je) {
		super(je);
	}

	public CarEntry(ZipEntry ze) {
		super(ze);
	}

	public CarEntry(CarEntry je) {
		this((JarEntry)je);
	}

}

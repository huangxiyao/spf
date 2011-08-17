package com.hp.spp.wsrp.export.util.car;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class CarFile extends JarFile {

	public CarFile(File file, boolean verify, int mode) throws IOException {
		super(file, verify, mode);
	}

	public CarFile(File file, boolean verify) throws IOException {
		super(file, verify);
	}

	public CarFile(File file) throws IOException {
		super(file);
	}

	public CarFile(String name, boolean verify) throws IOException {
		super(name, verify);
	}

	public CarFile(String name) throws IOException {
		super(name);
	}

	public CarEntry getCarEntry(String name) {
		return (CarEntry) getJarEntry(name);
	}

	public ZipEntry getEntry(String name) {
		ZipEntry ze = super.getEntry(name);
		if (ze != null) {
		    return new CarFileEntry(ze);
		}
		return null;
	}

	public Enumeration entries() {
		final Enumeration enumeration = super.entries();
		return new Enumeration() {
		    public boolean hasMoreElements() {
		    	return enumeration.hasMoreElements();
		    }
		    public Object nextElement() {
			    JarEntry je = (JarEntry)enumeration.nextElement();
				return new CarFileEntry(je);
		    }
		};
	}

    private class CarFileEntry extends CarEntry {
    	CarFileEntry(ZipEntry ze) {
    	    super(ze);
    	}
    	CarFileEntry(JarEntry je) {
    	    super(je);
    	}
    }
}

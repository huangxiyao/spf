package com.hp.spp.wsrp.export.util.car;

import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class CarOutputStream extends JarOutputStream {

	public CarOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	public CarOutputStream(OutputStream out, Manifest man) throws IOException {
		super(out, man);
	}

}

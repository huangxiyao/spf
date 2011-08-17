package com.hp.spp.wsrp.export.util.car;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarInputStream;

public class CarInputStream extends JarInputStream {

	public CarInputStream(InputStream in) throws IOException {
		super(in);
	}

	public CarInputStream(InputStream in, boolean verify) throws IOException {
		super(in, verify);
	}

}

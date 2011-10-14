// $Header$
package com.hp.it.cas.persona.loader;

import com.hp.it.cas.persona.loader.xml.XmlUserLoader;

/**
 * Input file formats supported by the Persona bulk data loader. This enumeration provides a mapping from the file input
 * type specified on a command line to the {@link IUserLoader} implementation that can process that file type.
 *
 * @author Quintin May
 */
enum EFileFormat {

	XML(XmlUserLoader.class);
	
	private final Class<? extends IUserLoader> loaderClass;
	
	EFileFormat(Class<? extends IUserLoader> loaderClass) {
		this.loaderClass = loaderClass;
	}
	
	Class<? extends IUserLoader> getLoaderClass() {
		return loaderClass;
	}
}
package com.hp.spp.wsrp.export;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

import org.w3c.dom.Document;

import com.hp.spp.wsrp.export.exception.CommonException;
import com.hp.spp.wsrp.export.exception.ExportException;
import com.hp.spp.wsrp.export.util.car.CarFile;

public abstract class Export extends Common {

	public void export(CarFile input, Writer output) throws ExportException {
		try {
			Document resultDocument = export(input) ; 
				
			write(resultDocument, output);
		} catch (CommonException e) {
			throw new ExportException(e);
		}
	}
	
	public void export(Reader input, Writer output) throws ExportException {
		try {
			Document inputDocument = initializeDocument(input);
			Document resultDocument = export(inputDocument);

			write(resultDocument, output);
		} catch (CommonException e) {
			throw new ExportException(e);
		}
	}

	public void split(Reader input, List outputs) throws ExportException {
		try {
			if(outputs == null)
				throw new NullPointerException("") ;
			
			Document inputDocument = initializeDocument(input);
			List resultDocuments = split(inputDocument) ;
			
			if(resultDocuments != null)
				outputs.addAll(resultDocuments) ;
		} catch (CommonException e) {
			throw new ExportException(e);
		} 
	}
	
	abstract Document export(CarFile input) throws ExportException ;

	abstract Document export(Document input) throws ExportException ;
	
	abstract List split(Document input) throws ExportException ;
	
}

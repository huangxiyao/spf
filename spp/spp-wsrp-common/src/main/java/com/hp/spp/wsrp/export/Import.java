package com.hp.spp.wsrp.export;

import java.io.Reader;
import java.io.Writer;

import org.w3c.dom.Document;

import com.hp.spp.wsrp.export.exception.CommonException;
import com.hp.spp.wsrp.export.exception.ImportException;

public abstract class Import extends Common {

	public void replace(Reader wsrpSource, Reader vgnSource, Reader vgnTarget, Writer wsrpTarget) throws ImportException {
		try {
			Document documentWsrpSource = initializeDocument(wsrpSource) ;
			Document documentVgnSource = initializeDocument(vgnSource) ;
			Document documentVgnTarget = initializeDocument(vgnTarget) ;

			Document documentWsrpTarget = replace(documentWsrpSource, documentVgnSource, documentVgnTarget) ;
			
			write(documentWsrpTarget, wsrpTarget) ;
		} catch (CommonException e) {
			throw new ImportException(e);
		}
	}
	
	abstract Document replace(Document documentWsrpSource, Document documentVgnSource, Document documentVgnTarget) throws ImportException  ;
	
}

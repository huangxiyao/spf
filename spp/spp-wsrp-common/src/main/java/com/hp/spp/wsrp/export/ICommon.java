package com.hp.spp.wsrp.export;

import java.io.Writer;

import org.w3c.dom.Document;

import com.hp.spp.wsrp.export.exception.CommonException;

public interface ICommon {

	public static final int MAX_IN_LIMIT = 100;

	abstract void write(Document document, Writer output) throws CommonException ;

}

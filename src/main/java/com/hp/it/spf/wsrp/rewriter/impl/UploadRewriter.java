package com.hp.it.spf.wsrp.rewriter.impl;

import com.hp.it.spf.wsrp.rewriter.IRewriter;
import org.apache.axis.MessageContext;

/**
 * //FIXME (slawek) - to be implemented
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class UploadRewriter implements IRewriter {
	public boolean shouldApply(MessageContext messageContext) {
		return false;
	}

	public void rewrite(Object data) {
	}
}

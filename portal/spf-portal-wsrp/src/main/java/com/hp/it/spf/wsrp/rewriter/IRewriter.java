package com.hp.it.spf.wsrp.rewriter;

import org.apache.axis.MessageContext;

/**
 * WSRP request or response rewriter interface. The objects of this type are registered in
 * {@link com.hp.it.spf.wsrp.rewriter.WsrpRewriterHandler} as either request or reponse rewriters.
 * The rewriter should be invoked only if its {@link #shouldApply(org.apache.axis.MessageContext)} method
 * returns true. The actual changes to the WSRP object are peformed in {@link #rewrite(Object)}
 * where the object is either request or response WSRP type specific for the given operation.
 * Note that whether the rewriter is used for requests or reponses depends on which rewriters list
 * it was registered in {@link com.hp.it.spf.wsrp.rewriter.WsrpRewriterHandler}.
 * <p/>
 * The implementations of this type should be thread-safe and not store any state. They are created
 * only once and invoked on each WSRP web service call.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public interface IRewriter {

	/**
	 * @param messageContext web service call message context
	 * @return <tt>true</tt> if the rewriter should be invoked for this web service call
	 */
	boolean shouldApply(MessageContext messageContext);

	/**
	 * Rewrites the WSRP request or reponse object. The rewriting consists of changing the content
	 * of the object. The example of data passed in could be <tt>oasis.names.tc.wsrp.v2.types.GetMarkup</tt>
	 * for getMarkup request or <tt>oasis.names.tc.wsrp.v2.types.ServiceDescription</tt> for
	 * getServiceDescription response.
	 *
	 * @param data WSRP request or reponse object
	 */
	void rewrite(Object data);
}

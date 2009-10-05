/**
 * Contains the WSRP request/response rewrting framework implemented as an Axis handler
 * {@link com.hp.it.spf.wsrp.rewriter.WsrpRewriterHandler} which is inovked on each web service
 * call and which, in turns, invokes the appropriate implementations of
 * {@link com.hp.it.spf.wsrp.rewriter.IRewriter} interface.
 */
package com.hp.it.spf.wsrp.rewriter;
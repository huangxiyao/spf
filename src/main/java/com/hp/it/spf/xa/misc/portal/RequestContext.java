package com.hp.it.spf.xa.misc.portal;

import com.hp.it.spf.xa.log.portal.TimeRecorder;

/**
 * The objects of this class are thread-scoped global variables which hold other objects
 * which also need similar scope (i.e. thread-scoped global). While not very elegant, this approach
 * allows to call these objects as if they were available through static context. The particularity
 * of this class is that its objects are bound to a thread-local variable. This means that each
 * thread has its own copy of the object.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class RequestContext {

	/**
	 * Attribute used to bind the request context instance to the HttpServletRequest
	 */
	public static final String REQUEST_KEY = RequestContext.class.getName();

	private static final ThreadLocal<RequestContext> mInstance =
			new ThreadLocal<RequestContext>();

//	private DiagnosticContext mDiagnosticContext;
	private TimeRecorder mTimeRecorder;

	private RequestContext() {
		mTimeRecorder = new TimeRecorder();
//		mDiagnosticContext = new DiagnosticContext();
	}

	/**
	 * This method returns the <tt>RequestContext</tt> instance tied to the current thread. It
	 * should be called only in main request thread. In order to use this instance in any other thread
	 * (e.g. WSRP) it must be bound to some context that the main request thread shares with these
	 * additional threads.
	 * Note that {@link #resetThreadInstance()} must be called prior to the first call to any other
	 * call to the request context for a given HttpServletRequest.
	 * @return RequestContext instance that tied to the current thread.
	 */
	public static RequestContext getThreadInstance() {
		RequestContext threadInstance = mInstance.get();
		if (threadInstance == null) {
			threadInstance = new RequestContext();
			mInstance.set(threadInstance);
		}
		return threadInstance;
	}

	/**
	 * Resets the instance of <tt>RequestContext</tt> associated with this thread.
	 */
	public static void resetThreadInstance() {
		mInstance.set(null);
	}

//	public DiagnosticContext getDiagnosticContext() {
//		return mDiagnosticContext;
//	}

	public TimeRecorder getTimeRecorder() {
		return mTimeRecorder;
	}
}

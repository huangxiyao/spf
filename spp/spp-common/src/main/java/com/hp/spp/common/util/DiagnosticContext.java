package com.hp.spp.common.util;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.io.Serializable;

/**
 This class is used to put all the diagnostic information that may help people quickly 
 diagnose problems. The getMessage() return the error message as a String object 
 and we can put it as HTML comment at the footer area in the page. This way people 
 from GPP support could already do the first level of diagnosis. 

 @author Dae Cho

 */

public class DiagnosticContext implements Serializable {

	private static Logger mLog = Logger.getLogger(DiagnosticContext.class);

	private static final ThreadLocal<DiagnosticContext> mInstance = new ThreadLocal<DiagnosticContext>();

	private Map<String, String> mDiagnosticMap = Collections.synchronizedMap(new HashMap<String, String>());

	/**
	 * This method returns the DiagnosticContext instance tied to the current thread. 
	 */
	protected DiagnosticContext() {
	}

	public static DiagnosticContext getThreadInstance() {
		if (mLog.isDebugEnabled()) {
			mLog.debug("In DiagnosticContext getThreadInstance ");
		}

		DiagnosticContext instance = mInstance.get();
		if (instance == null) {
			instance = new DiagnosticContext();
			mInstance.set(instance);
		}
		return instance;
	}

	/**
	This method checks the mDiagnoticMap object is empty or not     
	 */

	public boolean isEmpty() {
		return mDiagnosticMap.isEmpty();
	}

	/**
	This method delete all message from the mDiagnosticMap object     
	 */

	public void clear() {
		mDiagnosticMap.clear();
	}

	public void copyFrom(DiagnosticContext diagnosticContext) {
//    	mDiagnosticMap = diagnosticContext.mDiagnosticMap; 
		mDiagnosticMap.clear();
		mDiagnosticMap.putAll(diagnosticContext.mDiagnosticMap);
	}

	public DiagnosticContext clone() {
		DiagnosticContext newDC = new DiagnosticContext();
		newDC.mDiagnosticMap.putAll(mDiagnosticMap);
		return newDC;
	}

	/**
	This method adds an error message into the mDiagnosticMap object with component name)     
	 */

	public void add(String componentName, String errorMessage) {
		  mDiagnosticMap.put(componentName, errorMessage);
	}

	/**
	The getErrorMessage() return all of the error messages from mDiagnosticMap 
	object.  We can put this output string as HTML comment at the footer area in the page.     
	 */

	public String getErrorMessage()
	{
	   StringBuilder retVal = new StringBuilder().append('\n');

	   if (mDiagnosticMap.size() != 0) {
			Iterator it = mDiagnosticMap.keySet().iterator();
			   while (it.hasNext()) {
				   String key = (String) it.next();
				   String val = (String) mDiagnosticMap.get(key);
				   retVal.append(key).append(":: ").append(val).append("\n");
			   }
	   }

		  return retVal.toString();
	}

}
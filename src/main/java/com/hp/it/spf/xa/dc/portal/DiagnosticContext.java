package com.hp.it.spf.xa.dc.portal;

import java.util.Map;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.io.Serializable;

/**
 * This class is used to put all the diagnostic information that may help people quickly
 * diagnose problems. The getMessage() return the error message as a String object
 * and we can put it as HTML comment at the footer area in the page. This way people
 * from GPP support could already do the first level of diagnosis.
 *
 * @author Dae Cho
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class DiagnosticContext implements Serializable {

	private Map<String, DataCallback> mDiagnosticMap = Collections.synchronizedMap(new LinkedHashMap<String, DataCallback>());
	private Map<ErrorCode, String> mErrors = Collections.synchronizedMap(new LinkedHashMap<ErrorCode, String>());

	public DiagnosticContext() {
	}

	/**
	 * @return <tt>true</tt> if no diagnostic information has been captured in this context.
	 */
	public boolean isEmpty() {
		return mDiagnosticMap.isEmpty() && mErrors.isEmpty();
	}

	/**
	 * @return <tt>true</tt> if this diagnostic context contains errors.
	 */
	public boolean hasErrors() {
		return !mErrors.isEmpty();
	}

	/**
	 * Copies the data from the give <tt>diagnosticContext</tt>.
	 *
	 * @param diagnosticContext source of the data to copy from
	 */
	public void copyFrom(DiagnosticContext diagnosticContext) {
		mDiagnosticMap.putAll(diagnosticContext.mDiagnosticMap);
		mErrors.putAll(diagnosticContext.mErrors);
	}

	/**
	 * @return a new diagnostic context containg the same error as this context; other data is
	 * cloned
	 */
	public DiagnosticContext cloneErrorsOnly() {
		DiagnosticContext newDC = new DiagnosticContext();
		newDC.mErrors.putAll(mErrors);
		return newDC;
	}

	/**
	 * Sets data for the given component in this diagnostic context.
	 *
	 * @param componentName name of the compnont for which message will be set
	 * @param data		  message to set for this component
	 */
	public void set(String componentName, String data) {
		mDiagnosticMap.put(componentName, new DataHolder(data));
	}

	/**
	 * Sets the data callback for the given component in this diagnostic context.
	 * The callback is a way to defer when the data should be retrieved to the time when this
	 * context will be serialized through one of <tt>getDataAsString</tt>. This is needed
	 * as the filter initializing the diagnostic context is called as one of the first filters
	 * and some data may be available only once other fiters or the actual servlet handling
	 * the request executes
	 * .
	 * @param componentName name of the component for which message will be set
	 * @param callback callback used to retrieve the data when this context is serialized
	 */
	public void set(String componentName, DataCallback callback) {
		mDiagnosticMap.put(componentName, callback);
	}

	/**
	 * Sets the error data for the given component in this diagnostic context.
	 * @param code error code
	 * @param errorData error data for component
	 */
	public void setError(ErrorCode code, String errorData) {
		mErrors.put(code, errorData);
	}


	/**
	 * Serializes the component data captured by this context to string.
	 * @param componentSeparator separator between component-serialized data
	 * @param dataSeparator separator between component name and its data
	 * @param htmlEscape if <tt>true</tt> in the result all occurences of '<' will be replaced with '['
	 * and all occurrences of '>' will be replaced with ']'
	 * @return string representing the information hold by this diagnostic context
	 */
	public String getDataAsString(String componentSeparator, String dataSeparator, boolean htmlEscape) {
		StringBuilder result = new StringBuilder();
		for (Iterator<Map.Entry<String, DataCallback>> it = mDiagnosticMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, DataCallback> component = it.next();
			String componentName = component.getKey();
			String componentData = component.getValue().getData();

			result.append(componentName);
			result.append(dataSeparator);
			result.append(componentData);
			if (it.hasNext()) {
				result.append(componentSeparator);
			}
		}

		if (!mDiagnosticMap.isEmpty() && !mErrors.isEmpty()) {
			result.append(componentSeparator);
		}

		for (Iterator<Map.Entry<ErrorCode, String>> it = mErrors.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<ErrorCode, String> error = it.next();
			ErrorCode errorCode = error.getKey();
			String errorData = error.getValue();

			result.append("ERROR").append(dataSeparator);
			result.append(errorCode);
			if (errorData != null) {
				result.append(dataSeparator);
				result.append(errorData);
			}
			if (it.hasNext()) {
				result.append(componentSeparator);
			}
		}

		if (htmlEscape) {
			for (int i = 0, len = result.length(); i < len; ++i) {
				if (result.charAt(i) == '<') {
					result.setCharAt(i, '[');
				}
				else if (result.charAt(i) == '>') {
					result.setCharAt(i, ']');
				}
			}
		}
		return result.toString();
	}


	/**
	 * @return string representing the information hold by this diagnostic context.
	 * @see #getDataAsString(String, String, boolean) 
	 */
	public String getDataAsString() {
		return getDataAsString("\n", ":: ", true);
	}

	@Override
	public String toString() {
		return getDataAsString(";", "=", false);
	}

	/**
	 * Callback implementation which simply holds the constructor-given value.
	 */
	private static class DataHolder implements DataCallback {
		private String mData;

		private DataHolder(String data) {
			mData = data;
		}

		public String getData() {
			return mData;
		}
	}
}
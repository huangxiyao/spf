package com.hp.it.spf.xa.log.portal;

class OperationData {
	protected Operation mOperation;
	protected long mStartTimeStamp;
	protected Object mAdditionalData;
	protected long mEndTimeStamp;
	protected boolean mError;
	protected Object mErrorAdditionalData;

	/**
	 * Creates new instance of OperationData object. This constructor is package private as it should
	 * be invoked only by {@link TimeRecorder} class.
	 * @param operation {@link Operation} for which this data is created
	 * @param additionalData additional data associated with this operation
	 */
	OperationData(Operation operation, Object additionalData) {
		mOperation = operation;
		mAdditionalData = additionalData;
		mStartTimeStamp = System.currentTimeMillis();
		mEndTimeStamp = -1;
		mError = false;
		mErrorAdditionalData = null;
	}

	/**
	 * Marks this operation as finished. This method is package private as it should only be invoked
	 * by {@link TimeRecorder} class and not directly by the client, time recording class.
	 */
	void finish() {
		mEndTimeStamp = System.currentTimeMillis();
	}

	/**
	 * Makrs the operation as error. This method is package private as it should only be invoked by
	 * {@link TimeRecorder} class and not directly by the client, time recording class.
	 * @param additionalData additional data associated with this operation
	 */
	void error(Object additionalData) {
		mEndTimeStamp = System.currentTimeMillis();
		mError = true;
		mErrorAdditionalData = additionalData;
	}

	public Operation getOperation() {
		return mOperation;
	}

	public long getStartTimeStamp() {
		return mStartTimeStamp;
	}

	public Object getAdditionalData() {
		return mAdditionalData;
	}

	public long getEndTimeStamp() {
		return mEndTimeStamp;
	}

	public Object getErrorAdditionalData() {
		return mErrorAdditionalData;
	}

	public long getExecutionTime() {
		long result = mEndTimeStamp - mStartTimeStamp;
		if (result >= 0) {
			return result;
		}
		else {
			return -1;
		}
	}

	public boolean isError() {
		return mError;
	}

	public boolean isFinished() {
		return mEndTimeStamp >= 0;
	}

	public String toString() {
		return writeOperationData(new StringBuffer()).toString();
	}

	/**
	 * Writes the operation data to the provided <tt>sb</tt> string buffer and returns it.
	 * The data has the following format:
	 * OperationName[AdditionalData]:Status[AdditionalErrorData]=ExecutionTime
	 *
	 * The AdditionalData and AdditionalErrorData are optional.
	 * @param sb string build into which this operation data is written
	 * @return string builder which was passed as the parameter
	 */
	StringBuffer writeOperationData(StringBuffer sb) {
		sb.append(mOperation.name());
		if (mAdditionalData != null) {
			sb.append('[').append(mAdditionalData).append(']');
		}
		sb.append(':');
		if (isFinished()) {
			if (isError()) {
				sb.append("ERROR");
				if (mErrorAdditionalData != null) {
					sb.append('[').append(mErrorAdditionalData).append(']');
				}
			}
			else {
				sb.append("OK");
			}
		}
		else {
			sb.append("unknown");
		}
		sb.append('=').append(getExecutionTime());
		return sb;
	}
}

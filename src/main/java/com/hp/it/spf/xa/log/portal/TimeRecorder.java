package com.hp.it.spf.xa.log.portal;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is used to record, in a structured way, the execution time information for different
 * operations occuring during the request.
 *
 * These operations must be surrounded with {@link #recordStart(Operation, Object)} or
 * {@link #recordStart(Operation)}, {@link #recordEnd(Operation)}, {@link #recordError(Operation, Object)}.
 * The time data will be captured only if the appropriate loggers associated with the operations
 * (created automatically) are enabled in DEBUG mode. For example for operation such as "UPS"
 * time recorder will automatically create a logger "TIME.com.hp.it.spf.xa.log.portal.TimeRecorder.UPS".
 *
 * All the time metrics are logged as a signle message using {@link #logRecordedData()}
 */
public class TimeRecorder {

	/**
	 * Attribute name used to bind the time recorder instance to the HttpServletRequest.
	 */
	public static final String REQUEST_KEY = TimeRecorder.class.getName();

	private static final Logger mTLog = Logger.getLogger("TIME." + TimeRecorder.class.getName());

	private static Map<Operation, Logger> mOperationLoggers = Collections.synchronizedMap(new HashMap<Operation, Logger>());
	private static final ThreadLocal<TimeRecorder> mInstance = new ThreadLocal<TimeRecorder>();

	private Thread mMainThread;
	private Map<Thread, List<OperationData>> mThreadOperations = Collections.synchronizedMap(new HashMap<Thread, List<OperationData>>());

	protected TimeRecorder() {
		mMainThread = Thread.currentThread();
	}

	/**
	 * This method returns the <tt>TimeRecorder</tt> instance tied to the current thread. It
	 * should be called only in main request thread. In order to use this instance in any other thread
	 * (e.g. WSRP) it must be bound to some context that the main request thread shares with these
	 * additional threads.
	 * Note that {@link #resetThreadInstance()} must be called prior to the first call to any other
	 * call to time recorder for a given request.
	 * @return TimeRecorder instance that is tied to the current thread.
	 */
	public static TimeRecorder getThreadInstance() {
		TimeRecorder instance = mInstance.get();
		if (instance == null) {
			instance = new TimeRecorder();
			mInstance.set(instance);
		}
		return instance;
	}

	/**
	 * Resets the instance of <tt>TimeRecorder</tt> associated to the current thread.
	 */
	public static void resetThreadInstance() {
		mInstance.set(null);
	}

	/**
	 * Records the start of the operation.
	 * @param operation operation that is starting
	 * @param addtionalData optional additional data. Use <tt>null</tt> if you don't want any data being
	 * logged for this operation. This can be used for example to log request URI when capturing
	 * total request execution time.
	 *
	 * @return operation data created. This value should not be used for end or error events - use
	 * {@link #recordEnd(Operation)} and {@link #recordEnd(Operation)} instead.
	 */
	public OperationData recordStart(Operation operation, Object addtionalData) {
		if (isOperationRecordingEnabled(operation)) {
			removeOperationForThread(operation, Thread.currentThread());
			OperationData data = new OperationData(operation, addtionalData);
			addOperationForThread(data, Thread.currentThread());
			return data;
		}
		return null;
	}

	/**
	 * Calls {@link #recordStart(Operation, Object)} with <tt>null</tt> additional data.
	 * @param operation operation that is starting
	 * @return operation data created. This value should not be used for end or error events - use
	 */
	public OperationData recordStart(Operation operation) {
		return recordStart(operation, null);
	}

	/**
	 * Records the end of the operation.
	 * @param operation operation that is ending. It must be the same value as the one used in
	 * the corresponding <tt>recordStart</tt> call.
	 * @return data associated with this operation. This value is used mainly for testing.
	 */
	public OperationData recordEnd(Operation operation) {
		if (isOperationRecordingEnabled(operation)) {
			OperationData data = findOperationDataForThread(operation, Thread.currentThread());
			if (data != null && !data.isFinished()) {
				data.finish();
			}
			return data;
		}
		return null;
	}

	/**
	 * Records the operation error. Operation which ends with error is also considered as finished.
	 * @param operation operation that has error. Must be the same as the one used in the corresponding
	 * <tt>recordStart</tt> call.
	 * @param additionalData additional data. This can be used to provide exception message. Note that the
	 * corresponding stack trace won't be printed and requires a separate log message.
	 * @return data associated with this operation. This value is used mainly for testing.
	 */
	public OperationData recordError(Operation operation, Object additionalData) {
		if (isOperationRecordingEnabled(operation)) {
			OperationData data = findOperationDataForThread(operation, Thread.currentThread());
			if (data != null && !data.isFinished()) {
				data.error(additionalData);
			}
			return data;
		}
		return null;
	}

	/**
	 * Creates a string represenation of the recorded operations since the last call to {@link #resetThreadInstance()}.
	 *
	 * @see OperationData#writeOperationData(StringBuffer) for the format of the data logged for a
	 * single operation.
	 *
	 * @return string represenation of the recorded data
	 */
	public String getRecordedDataAsString() {
		StringBuffer sb = new StringBuffer();
		List<OperationData> operations = getThreadOperationList(mMainThread);
		for (Iterator<OperationData> it = operations.iterator(); it.hasNext();) {
			OperationData data = it.next();
			data.writeOperationData(sb);
			if (it.hasNext()) {
				sb.append(',');
			}
		}

		Thread thread = findLongestNotMainRunningThread();
		if (thread != null) {
			operations = getThreadOperationList(thread);
			if (operations != null && !operations.isEmpty()) {
				sb.append(',');
				for (Iterator<OperationData> it = operations.iterator(); it.hasNext();) {
					OperationData data = it.next();
					data.writeOperationData(sb);
					if (it.hasNext()) {
						sb.append(',');
					}
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Writes a message to the log files containing all the operations info.
	 */
	public void logRecordedData() {
		if (mTLog.isDebugEnabled()) {
			mTLog.debug(getRecordedDataAsString());
		}
	}

	/**
	 * @param operation operation
	 * @return true if the operation time recording is enabled.
	 */
	public static boolean isOperationRecordingEnabled(Operation operation) {
		Logger l = mOperationLoggers.get(operation);
		if (l == null) {
			l = Logger.getLogger("TIME." + TimeRecorder.class.getName() + "." + operation.name());
			mOperationLoggers.put(operation, l);
		}
		return l.isDebugEnabled();
	}

	private List<OperationData> getThreadOperationList(Thread thread) {
		List<OperationData> transactions = mThreadOperations.get(thread);
		if (transactions == null) {
			transactions = new ArrayList<OperationData>();
			mThreadOperations.put(thread, transactions);
		}
		return transactions;
	}

	private void removeOperationForThread(Operation operation, Thread thread) {
		for (Iterator<OperationData> it = getThreadOperationList(thread).iterator(); it.hasNext();) {
			OperationData operationData = it.next();
			if (operation.equals(operationData.getOperation())) {
				it.remove();
			}
		}
	}

	private void addOperationForThread(OperationData operationData, Thread thread) {
		getThreadOperationList(thread).add(operationData);
	}

	private OperationData findOperationDataForThread(Operation operation, Thread thread) {
		for (Iterator<OperationData> it = getThreadOperationList(thread).iterator(); it.hasNext();) {
			OperationData operationData = it.next();
			if (operation.equals(operationData.getOperation())) {
				return operationData;
			}
		}
		return null;
	}

	private Thread findLongestNotMainRunningThread() {
		// calculate total time for the threads that are not the main thread
		Thread longestRunningNotMainThread = null;
		long max = 0;
		for (Iterator<Map.Entry<Thread, List<OperationData>>> it = mThreadOperations.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Thread, List<OperationData>> entry = it.next();
			if (entry.getKey() != mMainThread) {
				long totalExecTime = 0;
				for (Iterator<OperationData> it2 = entry.getValue().iterator(); it2.hasNext();) {
					OperationData operationData = it2.next();
					if (operationData.isFinished()) {
						totalExecTime += operationData.getExecutionTime();
					}
				}

				if (totalExecTime > max) {
					max = totalExecTime;
					longestRunningNotMainThread = entry.getKey();
				}
			}
		}
		return longestRunningNotMainThread;
	}

	private OperationData getOperationData(Operation operation) {
		OperationData data = findOperationDataForThread(operation, mMainThread);
		if (data != null) {
			return data;
		}
		else {
			Thread thread = findLongestNotMainRunningThread();
			if (thread != null) {
				data = findOperationDataForThread(operation, thread);
				if (data != null) {
					return data;
				}
			}
		}
		return null;
	}

	
	private List<OperationData> getOperationDataList() {
		List<OperationData> result = new ArrayList<OperationData>(getThreadOperationList(mMainThread));
		Thread thread = findLongestNotMainRunningThread();
		if (thread != null) {
			result.addAll(getThreadOperationList(thread));
		}
		return result;
	}
}

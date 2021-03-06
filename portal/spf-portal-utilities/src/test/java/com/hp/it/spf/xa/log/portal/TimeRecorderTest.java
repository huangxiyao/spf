package com.hp.it.spf.xa.log.portal;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class TimeRecorderTest extends TestCase
{
	@Override
	protected void setUp() throws Exception
	{
		BasicConfigurator.configure();
		// Force the level to DEBUG for TimeRecorder loggers otherwise it will result in a
		// NullPointerException thrown in the tests as recordStart would return null if
		// loggers are not at debug level.
		Logger.getLogger("TIME." + TimeRecorder.class.getName()).setLevel(Level.DEBUG);
	}

	public void testSingleThreadRecording() throws Exception {
		TimeRecorder recorder = new TimeRecorder();
		OperationData requestData = recorder.recordStart(Operation.REQUEST, "/portal/site/abc");
		OperationData profileData = recorder.recordStart(Operation.PROFILE_CALL);
		long profileStart = System.currentTimeMillis();
		Thread.sleep(50);
		long profileEnd = System.currentTimeMillis();
		recorder.recordEnd(Operation.PROFILE_CALL);
		OperationData groupsData = recorder.recordStart(Operation.GROUPS_CALL);
		long groupsStart = System.currentTimeMillis();
		Thread.sleep(50);
		long groupsEnd = System.currentTimeMillis();
		recorder.recordError(Operation.GROUPS_CALL, "Something terrible happened");
		recorder.recordEnd(Operation.REQUEST);

		assertTrue("Request finished", requestData.isFinished());
		assertFalse("Request error", requestData.isError());
		assertTrue("Request time is not zero", requestData.getExecutionTime() > 0);
		assertTrue("Request time is longer or equal to its component times",
				requestData.getExecutionTime() >= profileData.getExecutionTime() + groupsData.getExecutionTime());

		assertTrue("Profile call finished", profileData.isFinished());
		assertFalse("Profile call error", profileData.isError());
		assertTrue("Profile call time", profileData.getExecutionTime() >= (profileEnd-profileStart));

		assertTrue("Groups call finished", groupsData.isFinished());
		assertTrue("Groups call error", groupsData.isError());
		assertTrue("Groups call time", groupsData.getExecutionTime() >= (groupsEnd-groupsStart));
	}

	public void testMultiThreadRecording() throws Exception {
		final TimeRecorder recorder = new TimeRecorder();
		recorder.recordStart(Operation.REQUEST, "/portal/site/def");
		recorder.recordStart(Operation.PROFILE_CALL);
		long profileStart = System.currentTimeMillis();
		Thread.sleep(50);
		long profileEnd = System.currentTimeMillis();
		recorder.recordEnd(Operation.PROFILE_CALL);
		final long[][] wsrp = {{0,0},{0,0}};
		List<Thread> threads = Arrays.asList(
				new Thread() {
					public void run()
					{
						recorder.recordStart(Operation.WSRP_CALL, "portlet1");
						try {
							wsrp[0][0] = System.currentTimeMillis();
							Thread.sleep(50);
							wsrp[0][1] = System.currentTimeMillis();
						}
						catch (InterruptedException e) {
							throw new IllegalStateException(e);
						}
						recorder.recordEnd(Operation.WSRP_CALL);
					}
				},
				new Thread() {
					public void run()
					{
						recorder.recordStart(Operation.WSRP_CALL, "portlet2");
						try {
							wsrp[1][0] = System.currentTimeMillis();
							Thread.sleep(100);
							wsrp[1][1] = System.currentTimeMillis();
						}
						catch (InterruptedException e) {
							throw new IllegalStateException(e);
						}
						recorder.recordEnd(Operation.WSRP_CALL);
					}
				}
		);
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
		recorder.recordEnd(Operation.REQUEST);


		Map<Operation, List<OperationData>> resultMap = convert(recorder.getRecordedData());

		assertTrue("Profile call captured", resultMap.containsKey(Operation.PROFILE_CALL));
		assertTrue("Profile call time",
				resultMap.get(Operation.PROFILE_CALL).get(0).getExecutionTime() >= (profileEnd-profileStart));
		assertTrue("Both WSRP calls captured",
				resultMap.containsKey(Operation.WSRP_CALL)
						&& resultMap.get(Operation.WSRP_CALL).size() == 2);
		assertTrue("Request time captured", resultMap.containsKey(Operation.REQUEST));
		assertTrue("Request time",
				resultMap.get(Operation.REQUEST).get(0).getExecutionTime() >= ((profileEnd-profileStart) +
						(Math.max(wsrp[1][1]-wsrp[1][0], wsrp[0][1]-wsrp[0][0]))));
	}

	private Map<Operation, List<OperationData>> convert(List<OperationData> data)
	{
		Map<Operation, List<OperationData>> result = new HashMap<Operation, List<OperationData>>();
		for (OperationData operationData : data) {
			if (result.containsKey(operationData.getOperation())) {
				result.get(operationData.getOperation()).add(operationData);
			}
			else {
				List<OperationData> l = new ArrayList<OperationData>(2);
				l.add(operationData);
				result.put(operationData.getOperation(), l);
			}
		}
		return result;
	}
}

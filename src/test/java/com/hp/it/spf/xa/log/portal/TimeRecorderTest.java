package com.hp.it.spf.xa.log.portal;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;

import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class TimeRecorderTest extends TestCase
{
	@Override
	protected void setUp() throws Exception
	{
		BasicConfigurator.configure();
	}

	public void testSingleThreadRecording() throws Exception {
		TimeRecorder recorder = new TimeRecorder();
		OperationData requestData = recorder.recordStart(Operation.REQUEST, "/portal/site/abc");
		OperationData profileData = recorder.recordStart(Operation.PROFILE_CALL);
		Thread.sleep(50);
		recorder.recordEnd(Operation.PROFILE_CALL);
		OperationData groupsData = recorder.recordStart(Operation.GROUPS_CALL);
		Thread.sleep(50);
		recorder.recordError(Operation.GROUPS_CALL, "Something terrible happened");
		recorder.recordEnd(Operation.REQUEST);

		assertTrue("Request finished", requestData.isFinished());
		assertFalse("Request error", requestData.isError());
		assertTrue("Request time is not zero", requestData.getExecutionTime() > 0);
		assertTrue("Request time is longer or equal to its component times",
				requestData.getExecutionTime() >= profileData.getExecutionTime() + groupsData.getExecutionTime());

		assertTrue("Profile call finished", profileData.isFinished());
		assertFalse("Profile call error", profileData.isError());
		assertTrue("Profile call time >=50", profileData.getExecutionTime() >= 50);

		assertTrue("Groups call finished", groupsData.isFinished());
		assertTrue("Groups call error", groupsData.isError());
		assertTrue("Groups call time >=50", groupsData.getExecutionTime() >= 50);
	}

	public void testMultiThreadRecording() throws Exception {
		final TimeRecorder recorder = new TimeRecorder();
		recorder.recordStart(Operation.REQUEST, "/portal/site/def");
		recorder.recordStart(Operation.PROFILE_CALL);
		Thread.sleep(50);
		recorder.recordEnd(Operation.PROFILE_CALL);
		List<Thread> threads = Arrays.asList(
				new Thread() {
					public void run()
					{
						recorder.recordStart(Operation.WSRP_CALL, "portlet1");
						try {
							Thread.sleep(50);
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
							Thread.sleep(100);
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


		Map<Operation, OperationData> resultMap = convert(recorder.getRecordedData());

		assertTrue("Profile call time",
				resultMap.get(Operation.PROFILE_CALL).getExecutionTime() >= 50);
		assertTrue("Only longest WSRP call time recorded",
				resultMap.get(Operation.WSRP_CALL).getExecutionTime() >= 100);
		assertTrue("Request time",
				resultMap.get(Operation.REQUEST).getExecutionTime() >= 150);
	}

	private Map<Operation, OperationData> convert(List<OperationData> data)
	{
		Map<Operation, OperationData> result = new HashMap<Operation, OperationData>();
		for (OperationData operationData : data) {
			if (result.containsKey(operationData.getOperation())) {
				fail("Only single entry expected for each operation: " + operationData.getOperation());
			}
			result.put(operationData.getOperation(), operationData);
		}
		return result;
	}
}

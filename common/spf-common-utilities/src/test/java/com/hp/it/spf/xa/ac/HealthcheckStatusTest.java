package com.hp.it.spf.xa.ac;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ye Liu (ye.liu@hp.com)
 */
public class HealthcheckStatusTest {
	@Test
	public void testHealthcheckStatusEquals() {
		OpenStatus open = new OpenStatus();
		//wait for 1 second to create other instances
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		OpenStatus open2 = new OpenStatus();
		DownStatus down = new DownStatus();
		ClosedStatus closed = new ClosedStatus();

		// compare based on the class type
		assertEquals("open status doesn't equal to null", open.equals(null), false);
		assertEquals("open status equals to itself", open.equals(open), true);
		assertEquals("open status doesn't equal to down status", open.equals(down), false);
		assertEquals("open status doesn't equal to closed status", open.equals(closed), false);
		assertEquals("down status doesn't equal to closed status", down.equals(closed), false);

		// compare based on the content
		assertEquals("an empty open status doesn't equal to another empty open status because the creation timestamps are different", open.equals(open2), false);

		open.setOpenSignSource("OpenSignSource");
		open.setPortalPulseSource("PortalPulseSource");

		open2.setOpenSignSource("OpenSignSource");
		open2.setPortalPulseSource("PortalPulseSource");
		assertEquals("one open status doesn't equal to another open status event the content is same because the creation timestamps are different", open.equals(open2), false);

		open2.setPortalPulseSource("PortalPulseSource2");
		assertEquals("one open status doesn't equal to another open status because the portal pulse source is different", open.equals(open2), false);
	}
}

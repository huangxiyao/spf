package com.hp.it.spf.wsrp.rewriter.impl;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import oasis.names.tc.wsrp.v1.types.RegistrationData;

import java.util.Arrays;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class ConsumerAgentRewriterTest
{
	@Test(expected = IllegalArgumentException.class)
	public void testRewriteWithNullParameter() {
		new ConsumerAgentRewriter().rewrite(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRewriteWithIncorrectParameterType() {
		new ConsumerAgentRewriter().rewrite(new Object());
	}

	@Test
	public void testRewriteWithShortConsumerAgentName() {
		RegistrationData registrationData = new RegistrationData();
		registrationData.setConsumerAgent("short name");
		new ConsumerAgentRewriter().rewrite(registrationData);

		assertThat("Consumer agent value not changed",
				registrationData.getConsumerAgent(), is("short name"));
	}

	@Test
	public void testRewriteWithLongConsumerAgentName() {
		RegistrationData registrationData = new RegistrationData();
		char[] ooo = new char[100];
		Arrays.fill(ooo, 'o');
		registrationData.setConsumerAgent("l" + new String(ooo) + "ng name");

		new ConsumerAgentRewriter().rewrite(registrationData);

		assertThat("Size of long agent name does not exceed limit",
				registrationData.getConsumerAgent().length(),
				is(ConsumerAgentRewriter.CONSUMER_AGENT_MAX_SIZE));
	}
}

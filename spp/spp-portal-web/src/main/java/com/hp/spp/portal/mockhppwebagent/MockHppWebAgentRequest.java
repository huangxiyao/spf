package com.hp.spp.portal.mockhppwebagent;

import com.hp.spp.profile.Constants;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Collections;
import java.util.Arrays;

public class MockHppWebAgentRequest extends HttpServletRequestWrapper {
	private String mUsername;
	private String mProfileId;

	public MockHppWebAgentRequest(HttpServletRequest request, String username, String hppId) {
		super(request);
		mUsername = username;
		mProfileId = hppId;
	}

	public String getHeader(String name) {
		if (Constants.SM_USERDN.equalsIgnoreCase(name)) {
			return mUsername;
		}
		else if (Constants.SM_UNIVERSALID.equalsIgnoreCase(name)) {
			return mProfileId;
		}
		else {
			return super.getHeader(name);
		}
	}

	public Enumeration getHeaders(String name) {
		if (Constants.SM_USERDN.equalsIgnoreCase(name)) {
			return Collections.enumeration(Arrays.asList(mUsername));
		}
		else if (Constants.SM_UNIVERSALID.equalsIgnoreCase(name)) {
			return Collections.enumeration(Arrays.asList(mProfileId));
		}
		else {
			return super.getHeaders(name);
		}
	}

	public Enumeration getHeaderNames() {
		if (super.getHeader(Constants.SM_USERDN) != null) {
			return super.getHeaderNames();
		}
		else {
			return new CompositeEnumeration(
					super.getHeaderNames(),
					Collections.enumeration(Arrays.asList(mUsername, mProfileId))
			);
		}
	}


	public class CompositeEnumeration implements Enumeration {

		private Enumeration mEnumeration1;
		private Enumeration mEnumeration2;

		public CompositeEnumeration(Enumeration enumeration1, Enumeration enumeration2) {
			mEnumeration1 = enumeration1;
			mEnumeration2 = enumeration2;
		}

		public boolean hasMoreElements() {
			return mEnumeration1.hasMoreElements() || mEnumeration2.hasMoreElements();
		}

		public Object nextElement() {
			if (mEnumeration1.hasMoreElements()) {
				return mEnumeration1.nextElement();
			}
			else {
				return mEnumeration2.nextElement();
			}
		}
	}
}

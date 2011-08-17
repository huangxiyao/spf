package com.hp.spp.portal.webconnector;

import java.io.Serializable;

public class SessionAccess implements Serializable {
	private long mTimeout;
	private long mLastAccess;

	public SessionAccess(long timeoutInMillis) {
		mTimeout = timeoutInMillis;
	}

	public boolean isExpired(long time) {
		return mLastAccess + mTimeout < time;
	}

	public void expire() {
		mLastAccess = Long.MIN_VALUE;
	}

	public long getTimeout() {
		return mTimeout;
	}

	public long getLastAccess() {
		return mLastAccess;
	}

	public void setLastAccess(long lastAccess) {
		mLastAccess = lastAccess;
	}

	public String toString() {
		return "EServiceInterpolator SessionAccess: timeout=" + mTimeout + ",lastAccess=" + mLastAccess;
	}

}

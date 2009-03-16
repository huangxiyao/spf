package com.hp.it.spf.xa.management.portal;

import com.vignette.portal.log.LogWrapper;

import java.util.Map;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class NullLogWrapper extends LogWrapper
{
	public NullLogWrapper(Class aClass)
			throws IllegalArgumentException
	{
		super(aClass);
	}

	@Override public void debug(Object message, Map map, Throwable throwable) {}
	@Override public void debug(Object message, Map map) {}
	@Override public void debug(Object message, Throwable throwable) { }
	@Override public void debug(Object message) { }
	@Override public void debug(Throwable error) { }
	@Override public void error(Object message, Map map, Throwable throwable) { }
	@Override public void error(Object message, Map map) { }
	@Override public void error(Object message, Throwable throwable) { }
	@Override public void error(Object message) { }
	@Override public void error(Throwable error) { }
	@Override public void fatal(Object message, Map map, Throwable throwable) { }
	@Override public void fatal(Object message, Map map) { }
	@Override public void fatal(Object message, Throwable throwable) { }
	@Override public void fatal(Object message) { }
	@Override public void fatal(Throwable error) { }
	@Override public void info(Object message, Map map, Throwable throwable) { }
	@Override public void info(Object message, Map map) { }
	@Override public void info(Object message, Throwable throwable) { }
	@Override public void info(Object message) { }
	@Override public void info(Throwable error) { }
	@Override public void warning(Object message, Map map, Throwable throwable) { }
	@Override public void warning(Object message, Map map) { }
	@Override public void warning(Object message, Throwable throwable) { }
	@Override public void warning(Object message) { }
	@Override public void warning(Throwable error) { }
	@Override public boolean willLogAtLevel(int level) { return false; }
	
}

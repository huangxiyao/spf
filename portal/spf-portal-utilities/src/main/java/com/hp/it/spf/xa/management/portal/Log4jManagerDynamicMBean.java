package com.hp.it.spf.xa.management.portal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import com.vignette.portal.log.LogConfiguration;

/**
 * MBean allowing to view and change Log4J loggers level.
 * The use of DynamicMBean allows to provide more meta data information than with simple MBean
 * which should ease the use of that object in JMX consoles.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 *
 */
public class Log4jManagerDynamicMBean  implements DynamicMBean {

	/**
	 * Contains the information about the management interface of this MBean.
	 */
	private MBeanInfo mInfo;


	public Log4jManagerDynamicMBean() {
		mInfo = new MBeanInfo(
				getClass().getName(),
				"This MBean allows to view and change Log4j loggers' levels",
				new MBeanAttributeInfo[] {
					new MBeanAttributeInfo(
							"AllLoggersLevels", 
							String[].class.getName(),
							"Array of logger names along with their effective levels",
							true, false, false)
				},
				new MBeanConstructorInfo[] {
					new MBeanConstructorInfo(
							"Creates Log4jManager",
							Log4jManagerDynamicMBean.class.getConstructors()[0])
				},
				new MBeanOperationInfo[] {
					new MBeanOperationInfo(
							"getLoggersLevels",
							"Retrieves the loggers, based on the given logger name pattern, along with their effective levels",
							new MBeanParameterInfo[] {
									new MBeanParameterInfo(
											"loggerNamePattern",
											String.class.getName(),
											"Regular expression used to select loggers based on their names")
									
							},
							String[].class.getName(),
							MBeanOperationInfo.INFO),
					new MBeanOperationInfo(
							"getLoggersForLevel",
							"Retrieves the loggers based on the given level",
							new MBeanParameterInfo[] {
									new MBeanParameterInfo(
											"level",
											String.class.getName(),
											"Logging level; can be one of FATAL, ERROR, WARN, INFO, DEBUG")
							},
							String[].class.getName(),
							MBeanOperationInfo.INFO),
					new MBeanOperationInfo(
							"setLoggersLevels",
							"Sets the specified level for loggers based on the given logger name pattern",
							new MBeanParameterInfo[] {
									new MBeanParameterInfo(
											"loggerNamePattern",
											String.class.getName(),
											"Regular expression used to select loggers based on their names"),
									new MBeanParameterInfo(
											"level",
											String.class.getName(),
											"Logging level; can be one of FATAL, ERROR, WARN, INFO, DEBUG")
							},
							String[].class.getName(),
							MBeanOperationInfo.ACTION_INFO)
				},
				null);
	}
	
	/**
	 * Returns the value of the attribute. 
	 * Currently on a single attribute called <tt>AllLogersLevels</tt> is supported.
	 * @see #getAllLoggersLevels() for more details on the value of AllLoggersLevels attribute.
	 */
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		if ("AllLoggersLevels".equals(attribute)) {
			return getAllLoggersLevels().toArray();
		}
		throw new AttributeNotFoundException("Attribute not found: " + attribute);
	}

	/**
	 * Returns the list of attributes. Currently only a single attribute called <tt>AllLoggersLevels</tt>
	 * is supported.
	 */
	public AttributeList getAttributes(String[] attributes) {
		AttributeList result = new AttributeList();
		for (String attribute : attributes) {
			try {
				result.add(new Attribute(attribute, getAttribute(attribute)));
			} catch (Exception e) {
				throw new IllegalArgumentException("Unable to retieve attribute: " + attribute);
			} 
		}
		return result;
	}

	/**
	 * Returns the information about this MBean.
	 */
	public MBeanInfo getMBeanInfo() {
		return mInfo;
	}

	/**
	 * Invokes this MBean operations.
	 * Currently the following operations are supported: {@link #getLoggersLevels(String) getLoggerLevels},
	 * {@link #setLoggersLevels(String, String) setLoggerLevels}.
	 */
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException 
	{
		if ("getLoggersLevels".equals(actionName)) {
			return getLoggersLevels((String) params[0]).toArray();
		}
		else if ("setLoggersLevels".equals(actionName)) {
			return setLoggersLevels((String) params[0], (String)params[1]).toArray();
		}
		else if ("getLoggersForLevel".equals(actionName)) {
			return getLoggersForLevel((String) params[0]).toArray();
		}
		return null;
	}

	/**
	 * This method does nothing as this MBean has not writable attributes.
	 */
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
	}

	/**
	 * This method does nothing as this MBean has not writable attributes.
	 */
	public AttributeList setAttributes(AttributeList attributes) {
		return null;
	}

	/**
	 * @return the logger names along with their levels for all the loggers currently present 
	 * in the application.
	 * @see #getLoggersLevels(String) for more information about the format of the result
	 */
	public List<String> getAllLoggersLevels() {
		return getLoggersLevels(null);
	}

	/**
	 * Returns the logger names along with their levels for the loggers whose names match
	 * the given <tt>loggerNamePattern</tt>.
	 * 
	 * @param loggerNamePattern regular expression which should be present in the logger name
	 * @return an alphabetically sorted list of string with the following format 
	 * <tt>{logger name}: {level}</tt> for the loggers with names containing the given pattern.
	 */
	public List<String> getLoggersLevels(String loggerNamePattern) {
		Map<String, String> loggerLevels = new TreeMap<String, String>();

		for (Logger logger : findLoggers(loggerNamePattern)) {
			loggerLevels.put(logger.getName(), logger.getEffectiveLevel().toString());
		}

		List<String> result = new ArrayList<String>(loggerLevels.size());
		for (Map.Entry<String, String> loggerLevel : loggerLevels.entrySet()) {
			result.add(loggerLevel.getKey() + ": " + loggerLevel.getValue());
		}
		return result;
	}

	/**
	 * Returns the logger names with the given level.
	 * @param level logging level
	 * @return an alphabetically sorted list of logger names having given level.
	 */
	public List<String> getLoggersForLevel(String level) {
		Level logLevel = Level.toLevel(level, null);
		if (logLevel == null) {
			return Collections.emptyList();
		}
		List<String> result = new ArrayList<String>();
		for (Logger logger : findLoggers(null)) {
			if (logger.getEffectiveLevel().equals(logLevel)) {
				result.add(logger.getName());
			}
		}
		Collections.sort(result);
		return result;
	}

	/**
	 * Sets the specified level for the logger selected with the given pattern and returns the list
	 * of loggers and their effective levels that this operation applied to. This method does 
	 * nothing if the level does not correspond to one of Log4j supported values.
	 * 
	 * @param loggerNamePattern regular expression which should be present in the logger name
	 * @param level logging level
	 * @return the list of matching logger for which the level change was applied or 0-size array
	 * if the level was incorrect or no logger was found for the pattern. 
	 */
	public List<String> setLoggersLevels(String loggerNamePattern, String level) {
		Level logLevel = Level.toLevel(level, null);
		if (logLevel == null) {
			return Collections.emptyList();
		}

		// first handle plain log4j loggers
		for (Logger logger : findLoggers(loggerNamePattern)) {
			logger.setLevel(logLevel);
			// Vignette is caching log levels so we need to have special handling for its
			// logging subsystems
			setPortalSubsystemLevel(logLevel, logger);
		}

		return getLoggersLevels(loggerNamePattern);
	}

	/**
	 * Sets Vignette logging subsystem level. This method is required as Vignette is caching
	 * logging levels and do not check them using Log4J facitlities. It changes the level
	 * of the logging subsystem which results in the appropriate cache refresh.
	 * <p>
	 * The method is package protected for testing purpose.
	 * @param level logging level
	 * @param logger logger for whose subsystem's level will be changed.
	 */
	/*private*/ void setPortalSubsystemLevel(Level level, Logger logger)
	{
		int subsystemLevel = toPortalLevel(level);
		if (subsystemLevel == -1) {
			return;
		}

		String subsystem = getSubsystem(logger.getName());
		if (subsystem != null) {
			LogConfiguration.getInstance().setLevel(subsystemLevel, subsystem);
		}
	}

	/**
	 * @param loggerName logger name
	 * @return Vignette logging subsystem name; if the logger name corresponds to a class name,
	 * the class' package name will be retured; otherwise the logger name will be returned.
	 */
	private String getSubsystem(String loggerName)
	{
		int pos = loggerName.lastIndexOf('.');
		if (pos == -1 || pos == loggerName.length()-1) {
			return null;
		}
		if (Character.isUpperCase(loggerName.charAt(pos+1))) {
			return loggerName.substring(0, pos);
		}
		return loggerName;
	}

	/**
	 * Converts the Log4J level to portal logging level
	 * @param logLevel Log$j logging level
	 * @return corresponding portal logging level or <tt>-1</tt> if the given level is not one
	 * of DEBUG, INFO, WARN, ERROR, FATAL.
	 */
	private int toPortalLevel(Level logLevel)
	{
		if (Level.DEBUG.equals(logLevel)) {
			return LogConfiguration.DEBUG;
		}
		else if (Level.INFO.equals(logLevel)) {
			return LogConfiguration.INFO;
		}
		else if (Level.ERROR.equals(logLevel)) {
			return LogConfiguration.ERROR;
		}
		else if (Level.FATAL.equals(logLevel)) {
			return LogConfiguration.FATAL;
		}
		else if (Level.WARN.equals(logLevel)) {
			return LogConfiguration.WARNING;
		}
		return -1;
	}

	/**
	 * Finds loggers corresponding to the given pattern.
	 * @param loggerNamePattern regular expression which should be present in the logger name
	 * @return list of loggers whose names contain the given pattern.
	 */
	@SuppressWarnings("unchecked")
	private List<Logger> findLoggers(String loggerNamePattern) {
		Pattern pattern =
			(loggerNamePattern == null) ? 
					null : 
					Pattern.compile(loggerNamePattern);
		List<Logger> result = new ArrayList<Logger>();
		
		LoggerRepository loggerRepository = LogManager.getLoggerRepository();
		for (Enumeration<Logger> loggers = loggerRepository.getCurrentLoggers(); loggers.hasMoreElements();) {
			Logger logger = loggers.nextElement();
			if (pattern == null) {
				result.add(logger);
			}
			else {
				Matcher matcher = pattern.matcher(logger.getName());
				if (matcher.find()) {
					result.add(logger);
				}
			}
		}
		
		return result;
	}

}

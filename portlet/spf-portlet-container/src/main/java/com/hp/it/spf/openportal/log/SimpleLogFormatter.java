package com.hp.it.spf.openportal.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.Formatter;

/**
 * SimpleLogFormatter was created in order to add the Diagnostic ID to openportal logs.
 * Note that this class, which extends a class from Java Logging framework, must be present in
 * the system classpath. Otherwise JDK's {@link java.util.logging.LogManager} will not be able
 * to load it. That's why it is part of this module which is included in system classpath.
 * 
 * @author pranav
 */
public class SimpleLogFormatter extends Formatter
{
	private static ThreadLocal<String> diagnosticID = new ThreadLocal<String>();
	private String lineSeparator = System.getProperty("line.separator");
	
	public static void setDiagnosticId(String id){
		if(id != null){
			diagnosticID.set(id);	
		} else {
			diagnosticID.remove();
		}
	}	
	
	public static String getDiagnosticId(){
		return diagnosticID.get();
	}

	@Override
	public String format(LogRecord record) {
		
		StringBuilder sb = new StringBuilder();    
		sb.delete(0, sb.capacity());
		sb.append(new Date(record.getMillis()));
		sb.append(" [");
		if(getDiagnosticId() != null) {
			sb.append(getDiagnosticId());
		}
		sb.append("] ")
		.append(record.getLevel().getLocalizedName())
		.append(" ")
		.append(formatMessage(record))   
		.append(lineSeparator);
		
		if (record.getThrown() != null) {            
			try {                
				StringWriter sw = new StringWriter();                
				PrintWriter pw = new PrintWriter(sw);                
				record.getThrown().printStackTrace(pw);                
				pw.close();                
				sb.append(sw.toString());            
			} catch (Exception ex) {                
				// ignore - we're just printing the exception stack trace for the error that
				// occurred before
			}       
		}       
		return sb.toString();		
	}
}

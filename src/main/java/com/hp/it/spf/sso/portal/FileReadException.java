package com.hp.it.spf.sso.portal;

/**
 * This is essenitally a RuntimeException wrapper for IOException which may be thrown when
 * reading the input file. It is needed as reading occurs {@link InputFile} which is
 * an implementation of {@link java.util.Iterator} whose method do not allow throwing checked
 * exceptions.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class FileReadException extends RuntimeException
{
	public FileReadException()
	{
	}

	public FileReadException(String message)
	{
		super(message);
	}

	public FileReadException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FileReadException(Throwable cause)
	{
		super(cause);
	}
}

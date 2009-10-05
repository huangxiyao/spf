package com.hp.it.spf.sso.portal;

/**
 * This is essenitally a RuntimeException wrapper for IOException which may be thrown when
 * reading the input file. It is needed as reading occurs {@link AnonUsersInputFile} which is
 * an implementation of {@link java.util.Iterator} whose method do not allow throwing checked
 * exceptions.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class AnonUsersFileReadException extends RuntimeException
{
	public AnonUsersFileReadException()
	{
	}

	public AnonUsersFileReadException(String message)
	{
		super(message);
	}

	public AnonUsersFileReadException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AnonUsersFileReadException(Throwable cause)
	{
		super(cause);
	}
}

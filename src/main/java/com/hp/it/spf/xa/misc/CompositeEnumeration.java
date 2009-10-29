package com.hp.it.spf.xa.misc;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class CompositeEnumeration<T> implements Enumeration<T>
{

	private Enumeration<T> e1;
	private Enumeration<T> e2;

	/**
	 * @param e1
	 * @param e2
	 */
	public CompositeEnumeration(Enumeration<T> e1, Enumeration<T> e2)
	{
		this.e1 = e1;
		this.e2 = e2;
	}

	public boolean hasMoreElements()
	{
		return e1.hasMoreElements() || e2.hasMoreElements();
	}

	public T nextElement()
	{
		if (e1.hasMoreElements()) {
			return e1.nextElement();
		}
		else if (e2.hasMoreElements()) {
			return e2.nextElement();
		}
		else {
			throw new NoSuchElementException();
		}
	}

}

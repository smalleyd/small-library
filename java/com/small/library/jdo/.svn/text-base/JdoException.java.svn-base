package com.small.library.jdo;

/***************************************************************************************
*
*	Exception class that represents the errors propagated within the JDO engine.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 12/13/2004
*
***************************************************************************************/

public class JdoException extends RuntimeException
{
	/** Constructs an exception with a message. */
	public JdoException(String message) { super(message); }

	/** Constructs an exception with a root cause. */
	public JdoException(Throwable exception) { super(exception); }

	/** Get the root cause. */
	public Throwable getCause()
	{
		Throwable cause = super.getCause();

		if (null == cause)
			return this;

		return cause;
	}
}


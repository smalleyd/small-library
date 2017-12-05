package com.small.library.generator;

/*************************************************************************************
*
*	Class that represents exceptions specific to generation of code. The
*	accessor <CODE>getRootCause</CODE> can contain a more specific <I>Exception</I>
*	object if one exists.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 12/24/2000
*
*************************************************************************************/

public class GeneratorException extends Exception
{
	private static final long serialVersionUID = 1L;

	public GeneratorException(final String message, final Exception cause) { super(message, cause); }
	public GeneratorException(final String message) { super(message); }
	public GeneratorException(final Exception cause) { super(cause); }
}

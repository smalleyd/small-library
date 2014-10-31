package com.small.library.util;

/*****************************************************************************************
*
*	Generic exception for object pool errors.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 8/26/2000
*
*****************************************************************************************/

public class PoolException extends Exception
{
	/********************************************************************************
	*
	*	Constants
	*
	********************************************************************************/

	/** Constant - default message. */
	public static final String DEFAULT_MESSAGE = "General pool error";

	/********************************************************************************
	*
	*	Constructors/Destructor
	*
	********************************************************************************/

	/** Constructor - default. */
	public PoolException() { this(DEFAULT_MESSAGE); }

	/** Constructor - accepts a string message. */
	public PoolException(String strMessage) { super(strMessage); }

	/** Constructor - accepts another exception object. */
	public PoolException(Exception pEx)
	{ super(pEx.getMessage()); m_RootCause = pEx; }

	/********************************************************************************
	*
	*	Accessor methods
	*
	********************************************************************************/

	/** Accessor - gets a reference to the root exception object. */
	public Exception getRootCause() { return m_RootCause; }

	/********************************************************************************
	*
	*	Member variables
	*
	********************************************************************************/

	/** Member variable - reference to the root exception object. */
	private Exception m_RootCause = null;
}

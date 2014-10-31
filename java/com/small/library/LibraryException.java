package com.small.library;

/************************************************************************************
*
*	Base exception class for all exception classes within this and underlying
*	packages.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 5/31/2002
*
************************************************************************************/

public class LibraryException extends Exception
{
	/****************************************************************************
	*
	*	Constructors
	*
	****************************************************************************/

	/** Constructor - constructs an object populated with a message.
		@param strMessage Exception message.
	*/
	public LibraryException(String strMessage)
	{
		super(strMessage);
	}

	/** Constructor - constructs an object populated with a Root Cause.
		@param pRootCause Original exception wrapped by this exception
			instance
	*/
	public LibraryException(Exception pRootCause)
	{
		super(pRootCause.getMessage());
		m_RootCause = pRootCause;
	}

	/****************************************************************************
	*
	*	Accessor methods
	*
	****************************************************************************/

	/** Accessor method - gets the Root Cause exception. */
	public Exception getRootCause() { return m_RootCause; }

	/** Accessor method - returns either the Root Cause exception, if non-null,
	    or return <CODE>this</CODE>.
	*/
	public Exception toException()
	{
		return ((m_RootCause != null) ? m_RootCause : this);
	}

	/****************************************************************************
	*
	*	Member variables
	*
	****************************************************************************/

	/** Member variable - reference to the Root Cause exception. */
	private Exception m_RootCause = null;
}

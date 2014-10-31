package com.small.library.data;

/*********************************************************************************
*
*	Exception class thrown from the <I>DataSource</I> during initialization.
*	The exception primarily indicates that the driver specified during
*	initialization is invalid.
*
*	@author David Small
*	@version 1.1.0.0
*	@date 4/15/2002
*
**********************************************************************************/

public class DataSourceException extends Exception
{
	/**************************************************************************
	*
	*	Constructors
	*
	**************************************************************************/

	/** Constructor - accepts a <I>String</I> error message.
		@param strMessage Error message.
	*/
	public DataSourceException(String strMessage)
	{ this(strMessage, null); }

	/** Constructor - accepts an <I>Exception</I> object.
		@param pRootCause Originating error message.
	*/
	public DataSourceException(Exception pRootCause)
	{ this(pRootCause.getMessage(), pRootCause); }

	/** Constructor - accepts a <I>String</I> error message and an <I>Exception</I> object.
		@param strMessage Error message.
		@param pRootCause Originating error message.
	*/
	public DataSourceException(String strMessage, Exception pRootCause)
	{ super(strMessage); m_RootCause = pRootCause; }

	/**************************************************************************
	*
	*	Accessor methods
	*
	**************************************************************************/

	/** Accessor method - gets the Root Cause property. */
	public Exception getRootCause() { return m_RootCause; }

	/** Output method - returns either <CODE>this</CODE> or the Root Cause
	    exception. If the Root Cause exception is <CODE>null</CODE>, the
	    method returns <CODE>this</CODE>; otherwise it returns the Root Cause
	    exception.
	*/
	public Exception toException()
	{
		if (null != m_RootCause)
			return m_RootCause;

		return this;
	}

	/**************************************************************************
	*
	*	Member variables
	*
	**************************************************************************/

	/** Member variable - contains the originating exception object. */
	private Exception m_RootCause = null;
}
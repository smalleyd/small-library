package com.small.library.atg.meta;

/*************************************************************************************
*
*	Exception class that represents a parsing or processing exception
*	thrown from an ATG Dynamo Meta object. Usually, thrown during
*	construction of a meta object.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/8/2002
*
*************************************************************************************/

public class DynamoMetaException extends Exception
{
	/****************************************************************************
	*
	*	Constructors
	*
	****************************************************************************/

	/** Constructor - constructs a populated object.
		@param strMessage Exception's message.
	*/
	public DynamoMetaException(String strMessage) { super(strMessage); }

	/** Constructor - constructs a populated object.
		@param rootCause Exception's root cause.
	*/
	public DynamoMetaException(Exception rootCause)
	{
		super(rootCause.getMessage());

		m_RootCause = rootCause;
	}

	/****************************************************************************
	*
	*	Accessor methods
	*
	****************************************************************************/

	/** Member variable - gets the root cause exception that this
	    instance wraps. Used by the calling application to get more
	    information about the nature of the exception.
	*/
	public Exception getRootCause() { return m_RootCause; }

	/** Member variable - gets the root cause exception or a reference
	    to this object if the root cause is <CODE>null</CODE>.
	*/
	public Exception toException()
	{
		if (null != m_RootCause)
			return m_RootCause;

		return this;
	}

	/****************************************************************************
	*
	*	Member variables
	*
	****************************************************************************/

	/** Member variable - reference to the root cause exception that this
	    instance wraps. Used by the calling application to get more
	    information about the nature of the exception.
	*/
	private Exception m_RootCause = null;
}

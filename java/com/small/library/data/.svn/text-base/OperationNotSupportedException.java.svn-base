package com.small.library.data;

import java.sql.SQLException;

/************************************************************************************
*
*	Subclass of the SQLException class that represents an exception
*	thrown because the underlying driver or database does not support
*	the operation requested.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 5/31/2002
*
************************************************************************************/

public class OperationNotSupportedException extends SQLException
{
	/****************************************************************************
	*
	*	Constructors
	*
	****************************************************************************/

	/** Constructor - constructs an object populated with a Root Cause.
		@param pRootCause Original exception wrapped by this exception
			instance
	*/
	public OperationNotSupportedException(SQLException pRootCause)
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
	public SQLException getRootCause() { return m_RootCause; }

	/****************************************************************************
	*
	*	Member variables
	*
	****************************************************************************/

	/** Member variable - reference to the Root Cause exception. */
	private SQLException m_RootCause = null;

	/****************************************************************************
	*
	*	Helper methods
	*
	****************************************************************************/

	/** Helper method - determines if the SQL Exception was thrown due
	    to an operation that is not supported by the driver or database.
	*/
	public static boolean isUnsupportedOperation(SQLException pEx)
	{
		return "IM".equals(pEx.getSQLState().substring(0, 2));
	}
}

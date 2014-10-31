package com.small.library.data;

import java.sql.*;

/***************************************************************************************
*
*	JDBC error message builder. Accepts a SQLException object for input.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Error implements java.io.Serializable
{
	/******************************************************************************
	*
	* Constructors/Destructor
	*
	******************************************************************************/

	/** Constructs a Error object based on a JDBC SQLException object.
		@param ex The JDBC SQLException object with the database error information.
		@param strLineBreak The line break characters used in the message.
	*/
	Error(SQLException ex, String strLineBreak)
	{ create(ex, strLineBreak); }

	private void create(SQLException ex,
		String strLineBreak)
	{
		m_strValue = "State: " + ex.getSQLState() + strLineBreak +
			"Native Code: " + ex.getErrorCode() + strLineBreak +
			"Message: " + ex.getMessage();
	}

	/** Returns a reference to the formatted SQL exception message. */
	public String toString() { return get(); }

	/******************************************************************************
	*
	* Accessor methods
	*
	******************************************************************************/

	/** Returns a reference to the formatted SQL exception message. */
	public String get() { return m_strValue; }

	/******************************************************************************
	*
	* Private member variables
	*
	******************************************************************************/

	private String m_strValue = null;
}

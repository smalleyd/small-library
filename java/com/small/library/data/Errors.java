package com.small.library.data;

import java.sql.*;
import java.util.*;

/***************************************************************************************
*
*	Contains & creates a collection of Error objects built from a single
*	SQLException object.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Errors extends Exception implements java.io.Serializable
{
	/******************************************************************************
	*
	* Public constants
	*
	******************************************************************************/

	/** Default line break characters. */
	public static final String DEFAULT_LINE_BREAK = "\n";

	/******************************************************************************
	*
	* Constructors/Destructor
	*
	******************************************************************************/

	/** Constructs the object based on a SQLException object. It can use the
	    the default line break character to build the message.
		param ex The SQLException with the error message or messages.
		param strLineBreak The line break character used in the message.
	*/
	public Errors(SQLException ex, String strLineBreak) { create(ex, strLineBreak); }

	/** Constructs the object based on a SQLException object. It uses the
	    the default line break character to build the message.
		param ex The SQLException with the error message or messages.
	*/
	public Errors(SQLException ex) { this(ex, DEFAULT_LINE_BREAK); }

	/** Constructs the object based on a prepared string message.
		@param strMessage Prepare string message.
	*/
	public Errors(String strMessage) { m_strMessage = strMessage; }

	/******************************************************************************
	*
	* Implementation of the Exception object.
	*
	******************************************************************************/

	/** Returns a string representation of the SQL exception. */
	public String getMessage()
	{
		if (null != m_strMessage) return m_strMessage;

		if ((null == m_Data) || (0 == m_Data.size()))
			return null;

		String m_strMessage = "";

		for (int i = 0; i < size(); i++)
		{
			if (0 < i)
				m_strMessage = m_strMessage + m_strLineBreak + m_strLineBreak;

			m_strMessage = m_strMessage + item(i).get();
		}

		return m_strMessage;
	}

	/** Returns a reference to the formatted SQL exception message. */
	public String toString() { return getMessage(); }

	/** Creates the collection of Error objects. */
	private void create(SQLException ex, String strLineBreak)
	{
		// Destroy the old data vector, and create a fresh vector.
		m_Data = new Vector();

		if (null == strLineBreak) strLineBreak = DEFAULT_LINE_BREAK;

		do
		{
			m_Data.addElement(new Error(ex, strLineBreak));
		} while (null != (ex = ex.getNextException()));

		// Clear the string version of the SQL exception.
		m_strMessage = null;
		m_strLineBreak = strLineBreak;
	}

	/******************************************************************************
	*
	* Accessor methods
	*
	******************************************************************************/

	/** Returns the number of Error objects contained in this collection. */
	public int size() { return m_Data.size(); }

	/** Returns the Error object specified by "nItem".
		@param nItem Zero based index of the Error object location in the collection.
	*/
	public Error item(int nItem) { return (Error) m_Data.elementAt(nItem); }

	/******************************************************************************
	*
	* Private member variables
	*
	******************************************************************************/

	/** Vector of Error objects. */
	private Vector m_Data = null;

	/** Contains a reference to a string version of the error message
	    after the first time it is created. */
	private String m_strMessage = null;

	private String m_strLineBreak = null;
}

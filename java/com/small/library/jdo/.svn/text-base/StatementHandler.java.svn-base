package com.small.library.jdo;

import java.sql.*;
import java.util.List;

/****************************************************************************
*
*	JDO class that performs the preparation of a JDBC Statement.
*	The single method returns a <I>PreparedStatement</I> object
*	or a derivative - i.e. <I>CallableStatement</I>. Used primarily
*	for retrieving data.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 12/16/2004
*
****************************************************************************/

public abstract class StatementHandler
{
	/** Abstract method - prepares the SQL statement and binds any
	    necessary parameters.
		@param connection JDBC Connection.
	*/
	public abstract PreparedStatement prepare(Connection connection)
		throws JdoException;

	/** Helper method - appends WHERE clause segments and stashes the value
	    for later binding to the statement. Ignores null parameters.
	*/
	protected void appendWhereSegment(StringBuffer where, List parameters,
		String segment, Object value)
			throws JdoException
	{
		if (null == value)
			return;

		if (0 < where.length())
			where.append(" AND");

		where.append(segment);
		parameters.add(value);
	}

	/** Helper method - prepares the statement based on SELECT, FROM, WHERE, and ORDER
	    clauses. In addition binds the parameters in the List.
	*/
	protected PreparedStatement prepare(Connection connection, String selectFrom,
		StringBuffer where, String otherClauses, List parameters)
			throws JdoException
	{
		if (null != where)
			selectFrom+= where.toString();

		if (null != otherClauses)
			selectFrom+= otherClauses;

		PreparedStatement stmt = null;

		try
		{
			stmt = connection.prepareStatement(selectFrom);

			int size = parameters.size();
			for (int i = 0; i < size; i++)
				stmt.setObject(i + 1, parameters.get(i));

			return stmt;
		}

		catch (SQLException ex)
		{
			try { if (null != stmt) stmt.close(); }
			catch (SQLException e) { /** No need to handle further. **/ }

			throw new JdoException(ex);
		}
	}
}


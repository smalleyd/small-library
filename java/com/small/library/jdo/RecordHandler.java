package com.small.library.jdo;

import java.sql.*;

/***************************************************************************************
*
*	Class that handles the binding parameters and the retrieving of result set
*	data for a single record.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 12/13/2004
*
***************************************************************************************/

public abstract class RecordHandler
{
	/******************************************************************************
	*
	* Constructors/Destructors
	*
	******************************************************************************/

	/** Constructor - default. */
	public RecordHandler() {}

	/******************************************************************************
	*
	* Data access and manipulation methods
	*
	******************************************************************************/

	/** Accepts a JDBC resultset object and retrieves the current rowset's
	    values. Do not call the <I>next</I> of the ResultSet. Handled by
	    the caller.
		@param rs JDBC ResultSet object.
		@param value DataValue object to populate with the current
			ResultSet record.
	*/
	public abstract void fetch(ResultSet rs, DataValue value)
		throws SQLException;

	/** Sets the parameters of the PreparedStatement with the DataValue
	    object for a SQL UPDATE. Do not execute the PreparedStatement.
		@param stmt JDBC PreparedStatement object.
		@param value DataValue object.
	*/
	public abstract void update(PreparedStatement stmt, DataValue value)
		throws SQLException;

	/** Sets the parameters of the PreparedStatement with the DataValue
	    object for a SQL INSERT. Do not execute the PreparedStatement.
		@param stmt JDBC PreparedStatement object.
		@param value DataValue object.
	*/
	public abstract void insert(PreparedStatement stmt, DataValue value)
		throws SQLException;

	/** Sets the parameters of the PreparedStatement with the DataValue
	    object for a SQL DELETE. Do not execute the PreparedStatement.
		@param stmt JDBC PreparedStatement object.
		@param value DataValue object.
	*/
	public abstract void delete(PreparedStatement stmt, DataValue value)
		throws SQLException;

	/******************************************************************************
	*
	* Parameter setting helper methods
	*
	******************************************************************************/

	/** Sets an <I>Integer</I> parameter. If <I>value</I> is zero (0), then
	    setNull is called instead.
		@param stmt A reference to a JDBC PreparedStatement object.
		@param param The order in the parameter list to bind the <I>int</I>.
		@param value The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement stmt, int param, Integer value)
		throws SQLException
	{
		if (null == value)
			stmt.setNull(param, java.sql.Types.INTEGER);
		else
			stmt.setInt(param, value.intValue());
	}

	/** Sets a <I>Short</I> parameter. If <I>value</I> is zero (0), then
	    setNull is called instead.
		@param stmt A reference to a JDBC PreparedStatement object.
		@param param The order in the parameter list to bind the <I>short</I>.
		@param value The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement stmt, int param, Short value)
		throws SQLException
	{
		if (null == value)
			stmt.setNull(param, java.sql.Types.SMALLINT);
		else
			stmt.setShort(param, value.shortValue());
	}

	/** Sets a <I>Byte</I> parameter. If <I>value</I> is zero (0), then
	    setNull is called instead.
		@param stmt A reference to a JDBC PreparedStatement object.
		@param param The order in the parameter list to bind the <I>byte</I>.
		@param value The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement stmt, int param, Byte value)
		throws SQLException
	{
		if (null == value)
			stmt.setNull(param, java.sql.Types.TINYINT);
		else
			stmt.setByte(param, value.byteValue());
	}

	/** Sets a <I>String</I> parameter. If <I>strValue</I> is NULL, then
	    setNull is called instead.
		@param stmt A reference to a JDBC PreparedStatement object.
		@param param The order in the parameter list to bind the <I>String</I>.
		@param strValue The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement stmt, int param, String strValue)
		throws SQLException
	{ setParam(stmt, param, strValue, java.sql.Types.VARCHAR); }

	/** Sets a <I>String</I> parameter. If <I>strValue</I> is NULL, then
	    setNull is called instead.
		@param stmt A reference to a JDBC PreparedStatement object.
		@param param The order in the parameter list to bind the <I>String</I>.
		@param strValue The value to bind to the parameter.
		@param paramType The java.sql.Types that represents the parameter.
	*/
	protected void setParam(PreparedStatement stmt, int param, String strValue,
		int paramType) throws SQLException
	{
		if (null == strValue)
			stmt.setNull(param, paramType);
		else
			stmt.setString(param, strValue);
	}

	/** Sets a <I>Timestamp</I> parameter. If <I>value</I> is NULL, then
	    setNull is called instead.
		@param stmt A reference to the JDBC PreparedStatement object.
		@param param The order in the parameter list to bind the <I>Timestamp</I>.
		@param value The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement stmt, int param, Timestamp value)
		throws SQLException
	{
		if (null == value)
			stmt.setNull(param, java.sql.Types.TIMESTAMP);
		else
			stmt.setTimestamp(param, value);
	}

	/** Sets a <I>Date</I> parameter. If <I>value</I> is NULL, then
	    setNull is called instead.
		@param stmt A reference to the JDBC PreparedStatement object.
		@param param The order in the parameter list to bind the <I>Timestamp</I>.
		@param value The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement stmt, int param, Date value)
		throws SQLException
	{
		if (null == value)
			stmt.setNull(param, java.sql.Types.DATE);
		else
			stmt.setDate(param, value);
	}
}

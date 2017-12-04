package com.small.library.metadata;

import java.io.Serializable;
import java.sql.*;

/***************************************************************************************
*
*	Data collection that describes the columns (fields) of a table. The data
*	collection also accepts the schema name in case the being requested exists
*	in more than one schema.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Column implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final String name;
	public final int dataType;
	public final String typeName;
	public final int size;
	public final int decimalDigits;
	public final int radix;
	public final boolean nullable;
	public final String remarks;
	public final boolean autoIncrement;

	public Column(final ResultSet rs) throws SQLException
	{
		name = rs.getString(4);
		dataType = rs.getInt(5);
		typeName = rs.getString(6);
		size = rs.getInt(7);
		decimalDigits = rs.getInt(9);
		radix = rs.getInt(10);
		nullable = (DatabaseMetaData.columnNullable == rs.getInt(11));
		remarks = rs.getString(12);
		autoIncrement = "YES".equals(rs.getString(23));
	}
}

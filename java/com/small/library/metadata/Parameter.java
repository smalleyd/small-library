package com.small.library.metadata;

import java.io.Serializable;
import java.sql.*;

/***************************************************************************************
*
*	Data collection that describes the parameters of a procedure.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Parameter implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final String name;
	public final short type;
	public final short dataType;
	public final String typeName;
	public final int precision;
	public final int length;
	public final short scale;
	public final short radix;
	public final boolean nullable;
	public final String remarks;

	public Parameter(final ResultSet rs) throws SQLException
	{
		name = rs.getString(4);
		type = rs.getShort(5);
		dataType = rs.getShort(6);
		typeName = rs.getString(7);
		precision = rs.getInt(8);
		length = rs.getInt(9);
		scale = rs.getShort(10);
		radix = rs.getShort(11);
		nullable = (DatabaseMetaData.procedureNullable == rs.getShort(12));
		remarks = rs.getString(13);
	}
}

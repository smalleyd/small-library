package com.small.library.metadata;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/***************************************************************************************
*
*	Value object that represents the JDBC metadata type info.
*
*	@author smalleyd
*	@version 2.0.2
*	@date 9/21/2017
*
***************************************************************************************/

public class TypeInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final String name;
	public final short type;
	public final int precision;
	public final String literalPrefix;
	public final String literalSuffix;
	public final String createParams;
	public final short nullable;
	public final boolean caseSensitive;
	public final short searchable;
	public final boolean unsigned;
	public final boolean fixedPrecision;
	public final boolean autoIncrement;
	public final String localName;
	public final short scaleMin;
	public final short scaleMax;
	public final int radix;

	public TypeInfo(final ResultSet rs) throws SQLException
	{
		name = rs.getString(1);
		type = rs.getShort(2);
		precision = rs.getInt(3);
		literalPrefix = rs.getString(4);
		literalSuffix = rs.getString(5);
		createParams = rs.getString(6);
		nullable = rs.getShort(7);
		caseSensitive = rs.getBoolean(8);
		searchable = rs.getShort(9);
		unsigned = rs.getBoolean(10);
		fixedPrecision = rs.getBoolean(11);
		autoIncrement = rs.getBoolean(12);
		localName = rs.getString(13);
		scaleMin = rs.getShort(14);
		scaleMax = rs.getShort(15);
		radix = rs.getInt(18);
	}
}

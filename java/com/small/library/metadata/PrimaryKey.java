package com.small.library.metadata;

import java.io.Serializable;
import java.sql.*;

/***************************************************************************************
*
*	Data collection that describes the primary key of a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class PrimaryKey implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final String key;
	public final String name;

	public PrimaryKey(final ResultSet rs) throws SQLException
	{
		key = rs.getString(4);
		name = rs.getString(6);
	}

	@Override
	public String toString()
	{
		return new StringBuilder("{ name: ").append("name")
			.append(", key: ").append(key)
			.append(" }").toString();
	}
}

package com.small.library.metadata;

import java.io.Serializable;
import java.sql.*;

/***************************************************************************************
*
*	Data collection that describes the stored procedures of a data source.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Procedure implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final String catalog;
	public final String schema;
	public final String name;
	public final String remarks;
	public final int type;

	public Procedure(final ResultSet rs) throws SQLException
	{
		catalog = rs.getString(1);
		schema = rs.getString(2);
		name = rs.getString(3);
		remarks = rs.getString(7);
		type = rs.getInt(8);
	}
}

package com.small.library.metadata;

import java.io.Serializable;
import java.sql.*;
import java.util.List;
import java.util.LinkedList;

/***************************************************************************************
*
*	Parent data record class that supports caching of the exported or imported
*	foreign key information for a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class ForeignKey implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final String name;
	public final String pkName;
	public final String pkTable;
	public final String fkTable;
	public final short updateRule;
	public final short deleteRule;
	public final short deferrability;

	public final List<Key> pks = new LinkedList<>();
	public final List<Key> fks = new LinkedList<>();

	public ForeignKey(final ResultSet rs) throws SQLException
	{
		pkTable = rs.getString(3);
		fkTable = rs.getString(7);
		updateRule = rs.getShort(10);
		deleteRule = rs.getShort(11);
		name = rs.getString(12);
		pkName = rs.getString(13);
		deferrability = rs.getShort(14);
	}

	public void addKey(final ResultSet rs) throws SQLException
	{
		final short order = rs.getShort(9);
		pks.add(new Key(order, rs.getString(4)));
		fks.add(new Key(order, rs.getString(8)));
	}
}

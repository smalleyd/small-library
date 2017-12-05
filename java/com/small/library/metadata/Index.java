package com.small.library.metadata;

import java.io.Serializable;
import java.sql.*;
import java.util.List;
import java.util.LinkedList;

/***************************************************************************************
*
*	Data collection that describes the indexes and statistics of a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Index implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final boolean unique;
	public final String name;
	public final short type;
	public final int cardinality;
	public final int pages;
	public final String filterCondition;
	public final List<Key> keys = new LinkedList<>();

	public Index(final ResultSet rs) throws SQLException
	{
		unique = (!rs.getBoolean(4));
		name = rs.getString(6);
		type = rs.getShort(7);
		cardinality = rs.getInt(11);
		pages = rs.getInt(12);
		filterCondition = rs.getString(13);

		addKey(rs);
	}

	public void addKey(final ResultSet rs) throws SQLException
	{
		keys.add(new Key(rs.getShort(8), rs.getString(9), rs.getString(10)));
	}
}

package com.small.library.metadata;

import java.io.Serializable;

/***************************************************************************************
*
*	Generic class for defining a collection of index key columns.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Key implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final String ASCENDING = "A";

	public final short order;
	public final String name;
	public final String sort;

	public Key(final short order, final String name)
	{
		this(order, name, ASCENDING);
	}

	public Key(final short order, final String name, final String sort)
	{
		this.order = order;
		this.name = name;
		this.sort = (null != sort) ? (sort.startsWith(ASCENDING) ? "ASC" : "DESC") : null;
	}

	@Override
	public String toString() { return name; }
}

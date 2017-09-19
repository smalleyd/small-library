package com.small.library.html;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/*********************************************************************************
*
*	Class that represents a collection of HTML table row elements, <I>TableRow</I>.
*	This is a zero-based collection.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/29/2001
*
*********************************************************************************/

public class TableRows extends Element
{
	private final List<TableRow> rows;

	public TableRows() { super(null); rows = new ArrayList<>(); }

	public TableRows(final TableRow... rows)
	{
		super(null);

		this.rows = Arrays.asList(rows);
	}

	public TableRows(final List<TableRow> rows)
	{
		super(null);

		this.rows = new ArrayList<>(rows);
	}

	/** Action method - creates the HTML table row element and underlying
	    table cell elements.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public void create(final Writer writer) throws IOException
	{
		for (final TableRow r : rows) r.create(writer);
	}

	/** Mutator method - adds a <I>TableRow</I> object to the collection.
		@param row New <I>TableRow</I> object to add to
			the collection.
	*/
	public void add(final TableRow row) { rows.add(row); }

	/** Mutator method - adds an array of <I>TableRow</I> objects to the collection.
		@param rows An array of <I>TableRow</I> objects.
	*/
	public void add(final TableRow... rows)
	{
		for (final TableRow i : rows)
			add(i);
	}

	/** Mutator method - adds an <I>List</I> of <I>TableRow</I> objects
	    to the collection.
		@param rows An <I>List</I> of <I>TableRow</I> objects.
	*/
	public void add(List<TableRow> rows)
	{
		rows.addAll(rows);
	}

	/** Mutator method - clears the collection of <I>TableRow</I> objects. */
	public void clear() { rows.clear(); }

	/** Mutator method - removes a <I>TableRow</I> object from the collection
	    at the specified index.
		@param nIndex Index of the <I>TableRow</I> object to remove.
	*/
	public void remove(int nIndex) { rows.remove(nIndex); }
}

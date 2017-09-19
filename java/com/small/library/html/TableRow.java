package com.small.library.html;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/*********************************************************************************
*
*	Class that represents the HTML table row element. This class is a container
*	class for a collection of <I>TableCell</I> objects. This is a zero-based
*	collection.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/29/2001
*
*********************************************************************************/

public class TableRow extends TagElement
{
	public static final String TAG = "tr";

	private final List<TableCell> cells;

	public TableRow() { super(null); cells = new ArrayList<>(); }

	public TableRow(final TableCell... cells)
	{
		super(null);

		this.cells = Arrays.asList(cells);
	}

	public TableRow(final List<TableCell> cells)
	{
		super(null);

		this.cells = new ArrayList<>(cells);
	}

	/** Action method - creates the HTML table row element and underlying
	    table cell elements.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public void create(final Writer writer) throws IOException
	{
		writeTag(writer, TAG);

		for (final TableCell c : cells) c.create(writer);

		writeTagClosing(writer, TAG);
	}

	/** Mutator method - adds a <I>TableCell</I> object to the collection.
		@param pTableCell New <I>TableCell</I> object to add to
			the collection.
	*/
	public void add(TableCell cell) { cells.add(cell); }

	/** Mutator method - adds an array of <I>TableCell</I> objects to the collection.
		@param cells An array of <I>TableCell</I> objects.
	*/
	public void add(TableCell... cells)
	{
		for (final TableCell i : cells)
			add(i);
	}

	/** Mutator method - adds an <I>List</I> of <I>TableCell</I> objects
	    to the collection.
		@param cells An <I>List</I> of <I>TableCell</I> objects.
	*/
	public void add(List<TableCell> cells)
	{
		cells.addAll(cells);
	}

	/** Mutator method - clears the collection of <I>TableCell</I> objects. */
	public void clear() { cells.clear(); }

	/** Mutator method - removes a <I>TableCell</I> object from the collection
	    at the specified index.
		@param i Index of the <I>TableCell</I> object to remove.
	*/
	public void remove(int i) { cells.remove(i); }
}

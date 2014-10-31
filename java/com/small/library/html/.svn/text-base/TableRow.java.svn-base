package com.small.library.html;

import java.io.IOException;
import java.io.Writer;
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
	/*************************************************************************
	*
	*	Constants
	*
	*************************************************************************/

	/** Constant - element's tag name. */
	public static final String TAG = "TR";

	/*************************************************************************
	*
	*	Constructors
	*
	*************************************************************************/

	/** Constructor - constructs an empty object.
	*/
	public TableRow() { super(null); m_TableCells = new ArrayList(); }

	/** Constructor - constructs a populated object.
		@param pTableCells An array of <I>TableCell</I> objects.
	*/
	public TableRow(TableCell[] pTableCells)
	{
		super(null);

		m_TableCells = new ArrayList(pTableCells.length);

		add(pTableCells);
	}

	/** Constructor - constructs a populated object.
		@param pTableCells An <I>List</I> of <I>TableCell</I> objects.
	*/
	public TableRow(List pTableCells)
	{
		super(null);

		m_TableCells = new ArrayList(pTableCells);
	}

	/*************************************************************************
	*
	*	Required methods: Element
	*
	*************************************************************************/

	/** Action method - creates the HTML table row element and underlying
	    table cell elements.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		writeTag(pWriter, TAG);

		int nSize = m_TableCells.size();

		for (int i = 0; i < nSize; i++)
			((TableCell) m_TableCells.get(i)).create(pWriter);

		writeTagClosing(pWriter, TAG);
	}

	/*************************************************************************
	*
	*	Accessor methods
	*
	*************************************************************************/

	/** Accessor method - gets the number of <I>TableCell</I> objects in the
	    collection.
	*/
	public int size() { return m_TableCells.size(); }

	/** Accessor method - gets a <I>TableCell</I> object in the collection
	    by index.
		@param nIndex Index of the <I>TableCell</I> object.
	*/
	public TableCell get(int nIndex) { return (TableCell) m_TableCells.get(nIndex); }

	/*************************************************************************
	*
	*	Mutator methods
	*
	*************************************************************************/

	/** Mutator method - adds a <I>TableCell</I> object to the collection.
		@param pTableCell New <I>TableCell</I> object to add to
			the collection.
	*/
	public void add(TableCell pTableCell) { m_TableCells.add(pTableCell); }

	/** Mutator method - adds an array of <I>TableCell</I> objects to the collection.
		@param pTableCells An array of <I>TableCell</I> objects.
	*/
	public void add(TableCell[] pTableCells)
	{
		for (int i = 0; i < pTableCells.length; i++)
			add(pTableCells[i]);
	}

	/** Mutator method - adds an <I>List</I> of <I>TableCell</I> objects
	    to the collection.
		@param pTableCells An <I>List</I> of <I>TableCell</I> objects.
	*/
	public void add(List pTableCells)
	{
		m_TableCells.add(pTableCells);
	}

	/** Mutator method - clears the collection of <I>TableCell</I> objects. */
	public void clear() { m_TableCells.clear(); }

	/** Mutator method - removes a <I>TableCell</I> object from the collection
	    at the specified index.
		@param nIndex Index of the <I>TableCell</I> object to remove.
	*/
	public void remove(int nIndex) { m_TableCells.remove(nIndex); }

	/*************************************************************************
	*
	*	Member variables
	*
	*************************************************************************/

	/** Member variable - contains reference to list of <I>TableCell</I> objects. */
	private ArrayList m_TableCells = null;
}

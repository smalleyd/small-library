package com.small.library.html;

import java.io.IOException;
import java.io.Writer;
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
	/*************************************************************************
	*
	*	Constructors
	*
	*************************************************************************/

	/** Constructor - constructs an empty object.
	*/
	public TableRows() { super(null); m_TableRows = new ArrayList(); }

	/** Constructor - constructs a populated object.
		@param pTableRows An array of <I>TableRow</I> objects.
	*/
	public TableRows(TableRow[] pTableRows)
	{
		super(null);

		m_TableRows = new ArrayList(pTableRows.length);

		add(pTableRows);
	}

	/** Constructor - constructs a populated object.
		@param pTableRows An <I>List</I> of <I>TableRow</I> objects.
	*/
	public TableRows(List pTableRows)
	{
		super(null);

		m_TableRows = new ArrayList(pTableRows);
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
		int nSize = m_TableRows.size();

		for (int i = 0; i < nSize; i++)
			((TableRow) m_TableRows.get(i)).create(pWriter);
	}

	/*************************************************************************
	*
	*	Accessor methods
	*
	*************************************************************************/

	/** Accessor method - gets the number of <I>TableRow</I> objects in the
	    collection.
	*/
	public int size() { return m_TableRows.size(); }

	/** Accessor method - gets a <I>TableRow</I> object in the collection
	    by index.
		@param nIndex Index of the <I>TableRow</I> object.
	*/
	public TableRow get(int nIndex) { return (TableRow) m_TableRows.get(nIndex); }

	/*************************************************************************
	*
	*	Mutator methods
	*
	*************************************************************************/

	/** Mutator method - adds a <I>TableRow</I> object to the collection.
		@param pTableRow New <I>TableRow</I> object to add to
			the collection.
	*/
	public void add(TableRow pTableRow) { m_TableRows.add(pTableRow); }

	/** Mutator method - adds an array of <I>TableRow</I> objects to the collection.
		@param pTableRows An array of <I>TableRow</I> objects.
	*/
	public void add(TableRow[] pTableRows)
	{
		for (int i = 0; i < pTableRows.length; i++)
			add(pTableRows[i]);
	}

	/** Mutator method - adds an <I>List</I> of <I>TableRow</I> objects
	    to the collection.
		@param pTableRows An <I>List</I> of <I>TableRow</I> objects.
	*/
	public void add(List pTableRows)
	{
		m_TableRows.add(pTableRows);
	}

	/** Mutator method - clears the collection of <I>TableRow</I> objects. */
	public void clear() { m_TableRows.clear(); }

	/** Mutator method - removes a <I>TableRow</I> object from the collection
	    at the specified index.
		@param nIndex Index of the <I>TableRow</I> object to remove.
	*/
	public void remove(int nIndex) { m_TableRows.remove(nIndex); }

	/*************************************************************************
	*
	*	Member variables
	*
	*************************************************************************/

	/** Member variable - contains reference to list of <I>TableRow</I> objects. */
	private List m_TableRows = null;
}

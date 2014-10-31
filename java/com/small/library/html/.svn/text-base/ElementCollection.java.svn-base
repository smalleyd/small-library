package com.small.library.html;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/************************************************************************************
*
*	Class that represents a collection of HTML elements. The <CODE>create</CODE>
*	method iterates through the collection and executes each <I>Element</I>'s
*	<CODE>create</CODE> method in turn. The collection is iterated through in
*	the order elements are added.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 11/7/2001
*
************************************************************************************/

public class ElementCollection extends Element
{
	/****************************************************************************
	*
	*	Constructors
	*
	****************************************************************************/

	/** Constructor - constructs an empty object. */
	public ElementCollection() { m_Elements = new ArrayList(); }

	/** Constructor - constructs an empty object with a specified capacity.
		@param nCapacity Anticipated capacity of the collection.
	*/
	public ElementCollection(int nCapacity)
	{
		m_Elements = new ArrayList(nCapacity);
	}

	/** Constructor - constructs a populated object.
		@param pNewValues <I>Element</I> objects to add.
	*/
	public ElementCollection(Element[] pNewValues)
	{
		m_Elements = new ArrayList(pNewValues.length);

		add(pNewValues);
	}

	/** Constructor - constructs a populated object.
		@param pNewValues <I>Element</I> objects to add.
	*/
	public ElementCollection(List pNewValues)
	{
		m_Elements = new ArrayList(pNewValues.size());

		add(pNewValues);
	}

	/****************************************************************************
	*
	*	Required methods: Element
	*
	****************************************************************************/

	/** Action method - creates the collection of HTML elements.
	*/
	public void create(Writer pWriter) throws IOException
	{
		int nSize = m_Elements.size();

		for (int i = 0; i < nSize; i++)
			get(i).create(pWriter);
	}

	/****************************************************************************
	*
	*	Accessor methods
	*
	****************************************************************************/

	/** Accessor method - gets the size of the collection. */
	public int size() { return m_Elements.size(); }

	/** Accessor method - gets the <I>Element</I> at the specified index.
		@param nIndex The specified index of the <I>Element</I> to retrieve.
	*/
	public Element get(int nIndex) { return (Element) m_Elements.get(nIndex); }

	/****************************************************************************
	*
	*	Mutator methods
	*
	****************************************************************************/

	/** Mutator method - adds a single <I>Element</I> object to the collection.
		@param pNewValue <I>Element</I> object to add.
	*/
	public void add(Element pNewValue) { m_Elements.add(pNewValue); }

	/** Mutator method - adds an array of <I>Element</I> objects to the collection.
		@param pNewValues <I>Element</I> objects to add.
	*/
	public void add(Element[] pNewValues)
	{
		for (int i = 0; i < pNewValues.length; i++)
			add(pNewValues[i]);
	}

	/** Mutator method - adds a <I>List</I> of <I>Element</I> objects to
	    the collection.
		@param pNewValues <I>Element</I> objects to add.
	*/
	public void add(List pNewValues) { m_Elements.addAll(pNewValues); }

	/** Mutator method - clears the collection of <I>Element</I> objects. */
	public void clear() { m_Elements.clear(); }

	/** Mutator method - removes a single <I>Element</I> object from the collection
	    at the specified index.
		@param nIndex The specified index of the <I>Element</I> to remove.
	*/
	public void remove(int nIndex) { m_Elements.remove(nIndex); }

	/****************************************************************************
	*
	*	Member variables
	*
	****************************************************************************/

	/** Member variable - contains a reference to the collection of <I>Element</I>
	    objects.
	*/
	private List m_Elements = null;
}

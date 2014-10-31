package com.small.library.html;

import java.util.ArrayList;

/***************************************************************************************
*
*	Implementation of a list of items to be displayed in comboboxes, listboxes, ... .
*	This class helps build adhoc lists.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public class List implements IList
{
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - Allocates a list with an unknown size. */
	public List() { m_Data = new ArrayList(); }

	/** Constructor - Allocates a list with a known initial size. */
	public List(int nCount) { m_Data = new ArrayList(nCount); }

	/** Constructor - Allocates a list based on the contents of another list. */
	public List(IList pList) { m_Data = new ArrayList(pList.getListCount()); add(pList); }

	/** Constructor - Allocates a list and initializes it with a single list item. */
	public List(IListItem pListItem) { m_Data = new ArrayList(); add(pListItem); }

	/** Constructor - Allocates a list based on the contents of an <I>ArrayList</I> object. */
	public List(ArrayList pList) { m_Data = new ArrayList(pList); }

	/******************************************************************************
	*
	*	Required methods: IList
	*
	******************************************************************************/

	/** Accessor method - gets the number of items in the list. */
	public int getListCount() { return getCount(); }

	/** Accessor method - gets the <I>IListItem</I> object at the specified zero-based
	    index.
	*/
	public IListItem getListItem(int nIndex)
	{
		// DO NOT go through "get" because the object may only support
		// IListItem, not ListItem.
		return (IListItem) m_Data.get(nIndex);
	}

	/******************************************************************************
	*
	*	Action methods
	*
	******************************************************************************/

	/** Action method - adds an individual list item to the list. */
	public void add(IListItem pListItem) { m_Data.add(pListItem); }

	/** Action method - adds the contents of another list object to the current list. */
	public void add(IList pList)
	{
		for (int i = 0; i < pList.getListCount(); i++)
			add(pList.getListItem(i));
	}

	/** Action method - adds the contents of an <I>ArrayList</I> object to the
	    current list.
	*/
	public void add(ArrayList pList)
	{
		for (int i = 0; i < pList.size(); i++)
			m_Data.add(pList.get(i));
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the number of list items in the list. */
	public int getCount() { return m_Data.size(); }

	/** Accessor method - gets the <I>ListItem</I> object at the specified zero-based
	    index.
	*/
	public ListItem get(int nIndex) { return (ListItem) m_Data.get(nIndex); }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the list items in the list. */
	private ArrayList m_Data = null;
}

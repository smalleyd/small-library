package com.small.library.html;

/***************************************************************************************
*
*	Interface for lists of items to be displayed in comboboxes, listboxes, ... .
*	The list implementation should be zero-based.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public interface IList
{
	/** Accessor method - gets the number of items in the list. */
	public int getListCount();

	/** Accessor method - gets the <I>ListItem</I> object at the specified
	    zero-based index.
		@param nIndex Index of the <I>ListItem</I> object to retrieve.
	*/
	public IListItem getListItem(int nIndex);
}

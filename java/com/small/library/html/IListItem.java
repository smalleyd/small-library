package com.small.library.html;

/***************************************************************************************
*
*	Interface for individudal items to be displayed in comboboxes, listboxes, ... .
*	Works in conjunction with the <I>IList</I> interface.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public interface IListItem
{
	/** Accessor method - gets the list item's identifier in the list. */
	public String getListItemID();

	/** Accessor method - gets the list item's display description. */
	public String getListItemDesc();
}

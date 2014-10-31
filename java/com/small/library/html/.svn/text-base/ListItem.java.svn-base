package com.small.library.html;

/***************************************************************************************
*
*	Implementation of individual items to be displayed in comboboxes, listboxes, ... .
*	Works in conjunction with the <I>List</I> class.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public class ListItem implements IListItem
{
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - creates an empty list item. */
	public ListItem() {}

	/** Constructor - creates a populate list item.
		@param strID Identifier of the list item.
		@param strDesc Display description of the list item.
	 */
	public ListItem(String strID, String strDesc)
	{ setID(strID); setDesc(strDesc); }

	/******************************************************************************
	*
	*	Required methods: IListItem
	*
	******************************************************************************/

	/** Accessor method - gets the identity of the list item. */
	public String getListItemID() { return getID(); }

	/** Accessor method - gets the display description of the list item. */
	public String getListItemDesc() { return getDesc(); }

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the identity of the list item. */
	public String getID() { return m_strID; }

	/** Accessor method - gets the display description of the list item. */
	public String getDesc() { return m_strDesc; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the identity of the list item. */
	public void setID(String strValue) { m_strID = strValue; }

	/** Mutator method - sets the display description of the list item. */
	public void setDesc(String strValue) { m_strDesc = strValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the identity of the list item. */
	private String m_strID = null;

	/** Member variable - contains the display description of the list item. */
	private String m_strDesc = null;
}

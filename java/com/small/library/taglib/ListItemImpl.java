package com.small.library.taglib;

/***********************************************************************************
*
*	Simple <I>ListItem</I> implementation.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 12/30/2002
*
************************************************************************************/

public class ListItemImpl implements ListItem
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/** Constructor - sets the state with default values. */
	public ListItemImpl()
	{
		id = null;
		desc = null;
	}

	/** Constructor - sets the state with the supplied values.
		@param id ID and description value of the list item.
	*/
	public ListItemImpl(String id)
	{
		this.id = this.desc = id;
	}

	/** Constructor - sets the state with the supplied values.
		@param id ID value of the list item.
		@param desc Description of the list item.
	*/
	public ListItemImpl(String id, String desc)
	{
		this.id = id;
		this.desc = desc;
	}

	/** Accessor method - gets the ID value of the list item. */
	public String getListItemId() { return id; }

	/** Accessor method - gets the description of the list item. */
	public String getListItemDesc() { return desc; }

	/** Accessor method - String representation. */
	public String toString() { return desc; }

	/** Member variable - contains the ID value of the list item. Public for serializers. */
	public String id;

	/** Member variable - contains the description of the list item. Public for serializers. */
	public String desc;
}


package com.small.library.taglib;

import java.util.*;

import javax.servlet.jsp.JspTagException;

/**********************************************************************
*
*	Base tag class for form elements that provide a list of selectable
*	items. The collection of items must implement the <I>ListItem</I>
*	interface.
*
*	Examples include comboboxes, listboxes, and multiple checkboxes.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/26/2002
*
***********************************************************************/

public abstract class SelectionTag extends FormElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*******************************************************************
	*
	*	Constants
	*
	*******************************************************************/

	/*******************************************************************
	*
	*	PageElement methods
	*
	*******************************************************************/

	/** PddTagSupport method - gets the tag name.
		@return <CODE>null</CODE> as no single tag defines this
			element.
	*/
	public String getTagName() { return null; }

	/*******************************************************************
	*
	*	Helper methods
	*
	*******************************************************************/

	/** Helper method - converts an array of <I>String</I> objects to
	    a <I>Set</I>.
	*/
	private static Set<String> toSet(String[] values)
	{
		if ((null == values) || (0 == values.length))
			return new HashSet<String>(0);

		Set<String> set = new HashSet<String>(values.length);
		
		for (int i = 0; i < values.length; i++)
			set.add(values[i]);
		
		return set;
	}

	/*******************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************/

	/** Accessor method - gets the collection of selectable items. */
	public Collection<ListItem> getOptions() { return options; }

	/** Accessor method - gets the <I>Set</I> of selected items. */
	public Set<String> getSelectedItems()
		throws JspTagException
	{
		// If POST, check for selectedItems in the request.
		if (isPost())
			return toSet(getHttpServletRequest().getParameterValues(name));

		// Make sure something is returned.
		if (null == selectedItems)
			return (selectedItems = new HashSet<String>(0));

		return selectedItems;
	}

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the collection of selectable items. */
	public void setOptions(Collection<ListItem> newValue) { options = newValue; }

	/** Mutator method - sets the <I>Set</I> of selected items.
		@param newValue A <I>Set</I> of values.
	*/
	public void setSelectedItems(Set<String> newValue) { selectedItems = newValue; }

	/** Mutator method - sets the <I>Set</I> of selected items.
		@param newValue A <I>Collection</I> of values.
	*/
	public void setSelectedItems(Collection<String> newValue)
	{
		selectedItems = new HashSet<String>(newValue);
	}

	/** Mutator method - sets the <I>Set</I> of selected items.
		@param newValue An array of <I>String</I> values.
	*/
	public void setSelectedItems(String[] newValues)
	{
		selectedItems = toSet(newValues);
	}

	/** Mutator method - sets the <I>Set</I> of selected items.
		@param newValue A single <I>String</I> value.
	*/
	public void setSelectedItem(String newValue)
	{
		selectedItems = new HashSet<String>(1);

		if (null != newValue)
			selectedItems.add(newValue);
	}

	/** Mutator method - sets the <I>Set</I> of selected items.
		@param newValue A single <I>Object</I> value.
	*/
	public void setSelectedItem(Object newValue)
	{
		if (null == newValue)
			setSelectedItem((String) null);
		else
			setSelectedItem(newValue.toString());
	}

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the collection of selectable items. */
	protected Collection<ListItem> options = null;

	/** Member variable - contains the set of selected items. */
	protected Set<String> selectedItems = null;
}

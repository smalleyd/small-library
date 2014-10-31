package com.small.library.taglib;

import java.util.ArrayList;
import java.util.Collection;

/**********************************************************************
*
*	Tag class that renders a pair of radio buttons of the True/False
*	nature.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 12/10/2002
*
***********************************************************************/

public class BooleanButtonsTag extends RadioButtonsTag
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*******************************************************************
	*
	*	Constants
	*
	********************************************************************/

	/** Constant - option value of true. */
	public static final String VALUE_TRUE = "Y";

	/** Constant - option value of false. */
	public static final String VALUE_FALSE = "N";

	/*******************************************************************
	*
	*	Static members
	*
	********************************************************************/

	/** Static member - contains the static list of yes/no options. */
	private static Collection<ListItem> yesNoOptions = null;

	/*******************************************************************
	*
	*	Static constructors
	*
	********************************************************************/

	/** Static constructor - initializes the static options list. */
	static
	{
		yesNoOptions = new ArrayList<ListItem>(2);
		yesNoOptions.add(new ListItemImpl(VALUE_TRUE, "Yes"));
		yesNoOptions.add(new ListItemImpl(VALUE_FALSE, "No"));
	}

	/*******************************************************************
	*
	*	Constructors
	*
	********************************************************************/

	/** Constructor - sets the default values. */
	public BooleanButtonsTag()
	{
		super(" ", false);
	}

	/*******************************************************************
	*
	*	Accessor methods
	*
	********************************************************************/

	/** Accessor method - gets the list options for the radio buttons. */
	public Collection<ListItem> getOptions() { return yesNoOptions; }
}

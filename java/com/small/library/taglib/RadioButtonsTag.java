package com.small.library.taglib;

/***********************************************************************
*
*	Radio buttons tag class that renders the HTML for a form radio button
*	list. Uses the DHTML "div" tag to display a scrollable region within
*	a page for the list of options.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/27/2002
*
***********************************************************************/

public class RadioButtonsTag extends OptionButtonsElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - input type attribute. */
	public static final String TYPE = "radio";

	/*****************************************************************
	*
	*	Constructors
	*
	*****************************************************************/

	/** Constructor - sets the default values. */
	public RadioButtonsTag()
	{
		super();
	}

	/** Constructor - accepts the default values.
			@param delimiter The delimiter for the option buttons.
			@param hasBorder Indicates whether the option buttons group
				has a border.
	*/
	protected RadioButtonsTag(String delimiter, boolean hasBorder)
	{
		super(delimiter, hasBorder);
	}

	/*******************************************************************
	*
	*	OptionButtonsElement methods
	*
	*******************************************************************/

	/** OptionButtonsElement method - gets the Input tag type. */
	public String getType() { return TYPE; }
}

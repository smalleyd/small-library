package com.small.library.taglib;

/***********************************************************************
*
*	Checkbox tag class that renders the HTML for a form checkbox
*	list. Uses the DHTML "div" tag to display a scrollable region within
*	a page for the list of options.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/27/2002
*
***********************************************************************/

public class CheckBoxesTag extends OptionButtonsElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - input type attribute. */
	public static final String TYPE = "checkbox";

	/*******************************************************************
	*
	*	OptionButtonsElement methods
	*
	*******************************************************************/

	/** OptionButtonsElement method - gets the Input tag type. */
	public String getType() { return TYPE; }
}

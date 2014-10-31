package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;

/***********************************************************************
*
*	Checkbox tag class that renders the HTML for a single form checkbox
*	with a yes/no indicator.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/26/2002
*
************************************************************************/

public class CheckBoxTag extends InputTag
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*******************************************************************
	*
	*	Constants
	*
	*******************************************************************/

	/** Constant - input tag type. */
	public static final String TYPE = "checkbox";

	/** Constant - permanent checkbox value used to indicate whether
	    the box has been checked or otherwise.
	*/
	public static final String VALUE = "Y";

	/*******************************************************************
	*
	*	Constructor methods
	*
	*******************************************************************/

	/** Constructor - sets the member variables to default values. */
	public CheckBoxTag() { super(VALUE); }

	/*******************************************************************
	*
	*	InputTag methods
	*
	*******************************************************************/

	/** InputTag method - gets the type of input tag. */
	public String getType() { return TYPE; }

	/*******************************************************************
	*
	*	TagSupport methods
	*
	*******************************************************************/

	/** TagSupport method - handles the start of the tag. */
	public int doStartTag()
		throws JspTagException
	{
		try
		{
			startTag();
			outputProperty(PROPERTY_ON_CLICK, onClick);
			outputUnaryProperty(PROPERTY_CHECKED, isChecked());

			pageContext.getOut().print(">");
		}

		catch (IOException ex)
		{
			throw new JspTagException(ex.getMessage());
		}

		return SKIP_BODY;
	}

	/*******************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************/

	/** Accessor method - gets the checked property of the Checkbox. */
	public boolean isChecked()
		throws JspTagException
	{
		// Determine if the checked value should come from the
		// HTTP POST or the supplied value.
		// If the checkbox is selected, a value will be returned.
		if (isPost())
		{
			String value = super.getValue();

			return ((null != value) &&
				    (0 < value.length())) ? true : false;
		}

		return isChecked;
	}

	/** Accessor method - gets the click event of the Checkbox. */
	public String getOnClick() { return onClick; }

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the checked proeprty of the Checkbox. */
	public void setChecked(boolean newValue) { isChecked = newValue; }

	/** Mutator method - sets the click event of the Checkbox. */
	public void setOnClick(String newValue) { onClick = newValue; }

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the checked property of the Checkbox. */
	private boolean isChecked = false;

	/** Member variable - contains the click event of the Checkbox. */
	private String onClick = null;
}

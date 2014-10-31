package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;

/***********************************************************************
*
*	Hidden value tag class that renders the HTML for a form hidden control.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/26/2002
*
***********************************************************************/

public class HiddenTag extends InputTag
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*******************************************************************
	*
	*	Constants
	*
	*******************************************************************/

	/** Constant - input tag type. */
	public static final String TYPE = "hidden";

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

			pageContext.getOut().print(">");
		}

		catch (IOException ex) { throw new JspTagException(ex.getMessage()); }

		return SKIP_BODY;
	}

	/******************************************************************
	*
	*	Mutator methods
	*
	******************************************************************/

	/** Mutator method - sets a boolean value. */
	public void setValue(boolean newValue)
	{
		if (newValue)
			setValue((String) "1");
		else
			setValue((String) null);
	}
}

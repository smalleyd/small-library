package com.small.library.taglib;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

/*********************************************************************
*
*	Tag class the renders a validation message on the page. The
*	validation message is cached in a request attribute during
*	the controller servlet's validation phase.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 12/19/2002
*
*********************************************************************/

public class PageMessageTag extends PageElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - tag name. */
	public static final String TAG_NAME = "span";

	/** Constant - CSS Class for page messages. */
	public static final String CSS_CLASS = "errormessage";

	/*******************************************************************
	*
	*	PageElement methods
	*
	*******************************************************************/

	/** PageElement method - gets the tag name. */
	public String getTagName() { return TAG_NAME; }

	/*******************************************************************
	*
	*	TagSupport methods
	*
	*******************************************************************/

	/** TagSupport method - handles the start of the tag. */
	public int doStartTag()
		throws JspTagException
	{
		// Get the message to display.
		String message = getValidationMessage();

		// Does a message exist?
		if (null == message)
			return SKIP_BODY;

		// Get the writer.
		JspWriter out = pageContext.getOut();

		try
		{
			startTag();

			out.print(">");
			out.print(message);
			out.print("</");
			out.print(TAG_NAME);
			out.print(">");
		}

		catch (java.io.IOException ex)
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

	/** Accessor method - gets the CSS class property. Overrides the
	    parent method with a constant value for the class.
	*/
	public String getCssClass() { return CSS_CLASS; }
}

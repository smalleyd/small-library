package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

import com.small.library.biz.ValidationException;

/***********************************************************************
*
*	Tag class that renders a the page level validation message.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 12/19/2002
*
***********************************************************************/

public class ErrorTag extends PageElement
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

	/** Constant - CSS class that represents page errors. */
	public static final String CSS_CLASS = "error";

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
		JspWriter out = pageContext.getOut();

		try
		{
			String message = getValidationMessage();

			if (message == null)
			{
				ValidationException exception = getValidationException();

				if (null == exception)
					return SKIP_BODY;

				message = exception.getMessage();
			}

			out.print("<br />");

			setCssClass(CSS_CLASS);
			startTag();
			out.print(">");
			out.print(message);
			endTag();

			out.print("<br />");

			return SKIP_BODY;
		}

		catch (IOException ex)
		{
			throw new JspTagException(ex.getMessage());
		}
	}
}

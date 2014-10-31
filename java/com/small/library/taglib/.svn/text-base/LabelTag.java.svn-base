package com.small.library.taglib;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

/*********************************************************************
*
*	A span class that represents a label to a form element. Provides
*	an asterisk (*) for required fields and color codes the label on
*	fields recently invalidated after a form post.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/29/2002
*
*********************************************************************/

public class LabelTag extends PageElement
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
			startTag();
			out.print(">");
			out.print(caption);

			if (isRequired)
				out.print(" *");

			out.print("</");
			out.print(TAG_NAME);
			out.print(">");
		}

		catch (Exception ex)
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

	/** Accessor method - gets the caption of the label. */
	public String getCaption() { return caption; }

	/** Accessor method - gets the required flag property of the label. */
	public boolean isRequired() { return isRequired; }

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the caption of the label. */
	public void setCaption(String newValue) { caption = newValue; }

	/** Mutator method - sets the required flag property of the label. */
	public void setIsRequired(boolean newValue) { isRequired = newValue; }

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the caption of the label. */
	private String caption = null;

	/** Member variable - contains the required flag property of the label. */
	private boolean isRequired = false;
}

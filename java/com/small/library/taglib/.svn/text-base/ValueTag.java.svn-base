package com.small.library.taglib;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspTagException;

/*********************************************************************
*
*	Helper tag for rendering text data on an HTML page. Can handle single
*	or multiple values. Can handle primitive or complex data.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 12/10/2002
*
*********************************************************************/

public class ValueTag extends PageElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - tag name. */
	public static final String TAG_NAME = null;

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
		try
		{
			pageContext.getOut().print(text);
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

	/** Accessor method - gets the text to display. */
	public String getText() { return text; }

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the text to display. */
	public void setText(String newValue)
	{
		if (null == newValue)
			text = NBSP;
		else
			text = newValue;
	}

	/** Mutator method - sets the text to display with an Object. */
	public void setText(Object newValue)
	{
		if (null == newValue)
			setText((String) null);
		else
			setText(newValue.toString());
	}

	/** Mutator method - sets the text to display with an array
	    of <I>String</I> objects.
	*/
	public void setText(String[] newValues)
	{
		if ((null == newValues) || (0 == newValues.length))
		{
			text = NBSP;
			return;
		}

		text = newValues[0];

		for (int i = 1; i < newValues.length; i++)
			text+= ", " + newValues[i];
	}

	/** Mutator method - sets the text to display with a <I>Collection</I>
	    objects.
	*/
	public void setText(Collection<? extends Object> newValues)
	{
		if ((null == newValues) || (0 == newValues.size()))
			return;

		Iterator<? extends Object> it = newValues.iterator();

		text = it.next().toString();

		while (it.hasNext())
			text+= ", " + it.next().toString();
	}

	/** Mutator method - sets the text property with a URL. */
	public void setUrl(String newValue)
	{
		if ((null == newValue) || (0 == newValue.length()))
		{
			setText((String) null);
			return;
		}

		String href = newValue;

		if (!href.startsWith("http://"))
			href = "http://" + href;

		text = "<a href=\"" + href + "\" target=\"_new\">" +
			newValue + "</a>";
	}

	/** Mutator method - sets the text property with long text. Need
	    to replace new lines with HTML line breaks (&lt;BR&gt;).
	*/
	public void setLongText(String newValue)
	{
		if ((null == newValue) || (0 == newValue.length()))
		{
			setText((String) null);
			return;
		}

		text = com.small.library.util.StringHelper.replace(newValue,
			"\n", "<br />");
	}

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the text to display. */
	protected String text = NBSP;
}

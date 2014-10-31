package com.small.library.taglib;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspTagException;

/***********************************************************************
*
*	Base tag library class that helps implement the different
*	INPUT tags.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/25/2002
*
************************************************************************/

public abstract class InputTag extends FormElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - tag name. */
	public static final String TAG_NAME = "input";

	/** Constant - property name for the type attribute. */
	public static final String PROPERTY_TYPE = "type";

	/** Constant - short date format. */
	public static final String SHORT_DATE_FORMAT = "MMM dd, yyyy";
	
	/** Static member - short date formatter. */
	public static final SimpleDateFormat shortDateFormatter = new SimpleDateFormat(SHORT_DATE_FORMAT);

	/*******************************************************************
	*
	*	Constructors
	*
	*******************************************************************/

	/** Constructor - sets the member variables to default values. */
	public InputTag() { this(null); }

	/** Constructor - sets the member variables to the supplied values.
			@param value The value of the Input tag.
	*/
	public InputTag(String value)
	{
		super();

		this.value = value;
	}

	/*******************************************************************
	*
	*	PageElement methods
	*
	*******************************************************************/

	/** PddTagSupport method - gets the tag name. */
	public String getTagName() { return TAG_NAME; }

	/*******************************************************************
	*
	*	Abstract methods
	*
	*******************************************************************/

	/** Abstract method - gets the INPUT tag type attribute. */
	public abstract String getType();

	/*******************************************************************
	*
	*	Helper methods
	*
	*******************************************************************/

	/** Helper method - starts the INPUT tag. */
	protected void startTag()
		throws JspTagException, IOException
	{
		super.startTag();

		outputProperty(PROPERTY_TYPE, getType());

		// Use the Accessor method to get the value,
		// in case it is overriden.
		outputProperty(PROPERTY_VALUE, getValue());
	}

	/*******************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************/

	/** Accessor method - gets the value of the input tag. Will
	    substitute value in the <I>HttpServletRequest</I> object if
	    the current request is a POST.
	*/
	public String getValue()
		throws JspTagException
	{
		// The prefer post value, when set to false, will cause
		// a check of the passed in "value" property and return
		// it instead if it is valid.
		if (isPost())
		{
			if (!preferPostValue && (value != null))
				return value;

			return getHttpServletRequest().getParameter(name);
		}

		// Return the default value.
		return value;
	}

	/** Accessor method - gets the prefer post value property. */
	public boolean getPreferPostValue() { return preferPostValue; }

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the value of the input tag. */
	public void setValue(String newValue) { value = newValue; }

	/** Mutator method - sets the value of the input tag. */
	public void setValue(Date newValue)
	{
		if (null != newValue)
			setValue(shortDateFormatter.format(newValue));
		else
			setValue((String) null);
	}

	/** Mutator method - sets the value of the input tag. */
	public void setValue(Object newValue)
	{
		if (null != newValue)
			setValue(newValue.toString());
		else
			setValue((String) null);
	}

	/** Mutator method - sets the prefer post value property. */
	public void setPreferPostValue(boolean newValue) { preferPostValue = newValue; }

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the value of the input tag. */
	protected String value;

	/** Member variable - contains the prefer post value property. */
	protected boolean preferPostValue = true;
}

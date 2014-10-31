package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;

import com.small.library.biz.FieldValidationException;
import com.small.library.biz.ValidationException;

/***********************************************************************
*
*	Base tag library class for elements of an HTML Form.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/26/2002
*
************************************************************************/

public abstract class FormElement extends PageElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - flag name of the checked flag. */
	public static final String PROPERTY_CHECKED = "checked";

	/** Constant - property name for the size attribute. */
	public static final String PROPERTY_SIZE = "size";

	/** Constant - property name for the value attribute. */
	public static final String PROPERTY_VALUE = "value";

	/** Constant - method name of an HTTP Get. */
	public static final String METHOD_GET = "get";

	/** Constant - method name of an HTTP Post. */
	public static final String METHOD_POST = "post";

	/*****************************************************************
	*
	*	Helper methods
	*
	*****************************************************************/

	/** Helper method - starts the form element tag. */
	protected void startTag()
		throws JspTagException, IOException
	{
		super.startTag();

		outputProperty(PROPERTY_ON_BLUR, onBlur);
		outputProperty(PROPERTY_ON_FOCUS, onFocus);
	}

	/** Helper method - is the current request a POST. */
	public boolean isPost()
		throws JspTagException
	{
		return METHOD_POST.equals(getHttpServletRequest().getMethod());
	}

	/** Helper method - has the current POST provided an invalid value
	    for this form element.
	*/
	public boolean isError()
		throws JspTagException
	{
		ValidationException exception = getValidationException();

		return ((null != exception) &&
			(exception instanceof FieldValidationException) &&
			(name.equals(((FieldValidationException) exception).getFieldName()))) ? true : false;
	}

	/*******************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************/

	/** Accessor method - gets the blur event of the input tag. */
	public String getOnBlur() { return onBlur; }

	/** Accessor method - gets the focus event of the input tag. */
	public String getOnFocus() { return onFocus; }

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the blur event of the input tag. */
	public void setOnBlur(String newValue) { onBlur = newValue; }

	/** Mutator method - sets the focus event of the input tag. */
	public void setOnFocus(String newValue) { onFocus = newValue; }

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the blur event of the input tag. */
	protected String onBlur = null;

	/** Member variable - contains the focus event of the input tag. */
	protected String onFocus = null;
}

package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

/**********************************************************************
*
*	Text area tag that represents the form element TextArea. The "wrap"
*	property defaults to "virtual".
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/27/2002
*
**********************************************************************/

public class TextAreaTag extends FormElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - tag name. */
	public static final String TAG_NAME = "textarea";

	/** Constant - property name for the columns attribute. */
	public static final String PROPERTY_COLS = "cols";

	/** Constant - property name for the rows attribute. */
	public static final String PROPERTY_ROWS = "rows";

	/** Constant - property name for the wrap attribute. */
	public static final String PROPERTY_WRAP = "wrap";

	/** Constant - wrap value for off. */
	public static final String WRAP_OFF = "off";

	/** Constant - wrap value for virtual. */
	public static final String WRAP_VIRTUAL = "virtual";

	/** Constant - wrap value for physical. */
	public static final String WRAP_PHYSICAL = "physical";

	/** Constant - default wrap value. */
	public static final String WRAP_DEFAULT = WRAP_VIRTUAL;

	/*****************************************************************
	*
	*	Constructors
	*
	*****************************************************************/

	/** Constructor - sets the state to the default values. */
	public TextAreaTag()
	{
		this(NULL_VALUE_INT, NULL_VALUE_INT, WRAP_DEFAULT);

		onChange = null;
		value = null;
	}

	/** Constructor - sets the state to the supplied values.
		@param cols default number of columns.
		@param rows default number of rows.
		@param wrap default wrap property.
	*/
	public TextAreaTag(int cols, int rows, String wrap)
	{
		this.cols = cols;
		this.rows = rows;
		this.wrap = wrap;
		onChange = null;
		value = null;
	}

	/*****************************************************************
	*
	*	PageElement methods
	*
	*****************************************************************/

	/** PageElement method - gets the tag name. */
	public String getTagName() { return TAG_NAME; }

	/*****************************************************************
	*
	*	TagSupport methods
	*
	*****************************************************************/

	/** TagSupport method - handles the start of the tag. */
	public int doStartTag()
		throws JspTagException
	{
		JspWriter out = pageContext.getOut();

		try
		{
			startTag();

			outputProperty(PROPERTY_COLS, cols);
			outputProperty(PROPERTY_ROWS, rows);
			outputProperty(PROPERTY_WRAP, wrap);
			outputProperty(PROPERTY_ON_CHANGE, onChange);

			out.print(">");

			String outputValue = getValue();

			if (null != outputValue)
				out.print(outputValue);

			endTag();
		}

		catch (IOException ex) { throw new JspTagException(ex.getMessage()); }

		return SKIP_BODY;
	}

	/*****************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************/

	/** Accessor method - gets the columns of the textarea. */
	public int getCols() { return cols; }

	/** Accessor method - gets the rows of the textarea. */
	public int getRows() { return rows; }

	/** Accessor method - gets the wrap property of the textarea. */
	public String getWrap() { return wrap; }

	/** Accessor method - gets the change event of the textarea. */
	public String getOnChange() { return onChange; }

	/** Accessor method - gets the value of the input tag. Will
	    substitute value in the <I>HttpServletRequest</I> object if
	    the current request is a POST.
	*/
	public String getValue()
		throws JspTagException
	{
		// If the value is in the request, return that value.
		// Value may be there if request is a Form POST with
		// errors or a POST that requires backend data to complete
		// form details.
		if (isPost())
		{
			String requestValue = getHttpServletRequest().getParameter(name);

			if (null != requestValue)
				return requestValue;
		}

		// Return the default value.
		return value;
	}

	/*****************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************/

	/** Mutator method - sets the columns of the textarea. */
	public void setCols(int newValue) { cols = newValue; }

	/** Mutator method - sets the rows of the textarea. */
	public void setRows(int newValue) { rows = newValue; }

	/** Mutator method - sets the wrap property of the textarea. */
	public void setWrap(String newValue) { wrap = newValue; }

	/** Mutator method - sets the change event of the textarea. */
	public void setOnChange(String newValue) { onChange = newValue; }

	/** Mutator method - sets the value of the textarea. */
	public void setValue(String newValue) { value = newValue; }

	/*****************************************************************
	*
	*	Member variables
	*
	*****************************************************************/

	/** Member variable - contains the columns of the textarea. */
	private int cols;

	/** Member variable - contains the rows of the textarea. */
	private int rows;

	/** Member variable - contains the wrap property of the textarea. */
	private String wrap;

	/** Member variable - contains the change event of the textarea. */
	private String onChange;

	/** Member variable - contains the value of the textarea. */
	private String value;
}

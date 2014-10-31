package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

/***********************************************************************
*
*	Textbox tag class that renders the HTML for a form textbox.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/26/2002
*
***********************************************************************/

public class TextBoxTag extends InputTag
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*******************************************************************
	*
	*	Constants
	*
	*******************************************************************/

	/** Constant - input tag type. */
	public static final String TYPE = "text";

	/** Constant - property name for the maximum length attribute. */
	public static final String PROPERTY_MAX_LENGTH = "maxlength";

	/*******************************************************************
	*
	*	Constructors - must provide the default constructor to provide
	*   a way for the web container to load the tag class.
	*
	*******************************************************************/

	/** Constructor - default. */
	public TextBoxTag() {}

	/** Constructor - friend constructor for usage within other tags.
			@param pPageContext <I>PageContext</I> of the current request.
			@param strName Name of the form element.
			@param strValue Value of the form element.
			@param strClass CSS class of the form element.
			@param strStyle CSS style of the form element.
			@param nMaxLength Maximum length of the form element.
			@param nSize Size of the form element.
			@param strOnChange Change event method of the form element.
	*/
	TextBoxTag(PageContext pPageContext, String strName,
		String strValue, String strClass, String strStyle,
		int nMaxLength, int nSize, String strOnChange)
			throws JspTagException
	{
		setPageContext(pPageContext);
		setName(strName);
		setValue(strValue);
		setCssClass(strClass);
		setStyle(strStyle);
		setMaxLength(nMaxLength);
		setSize(nSize);
		setOnChange(strOnChange);
	}

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
			outputProperty(PROPERTY_MAX_LENGTH, getMaxLength());
			outputProperty(PROPERTY_SIZE, getSize());
			outputProperty(PROPERTY_ON_CHANGE, getOnChange());

			pageContext.getOut().print(">");
		}

		catch (IOException ex) { throw new JspTagException(ex.getMessage()); }

		return SKIP_BODY;
	}

	/*******************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************/

	/** Accessor method - gets the maximum length of the Textbox. */
	public int getMaxLength() { return maxLength; }

	/** Accessor method - gets the size of the Textbox. */
	public int getSize()
	{
		// Has the size been supplied?
		if (NULL_VALUE_INT != size)
			return size;

		// Has the maximum length been supplied?
		if (NULL_VALUE_INT == maxLength)
			return NULL_VALUE_INT;

		// Derive the size based on the maximum length.
		if (50 > maxLength)
			return maxLength;

		return 50;
	}

	/** Accessor method - gets the change event of the Textbox. */
	public String getOnChange() { return onChange; }

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the maximum length of the Textbox. */
	public void setMaxLength(int newValue) { maxLength = newValue; }

	/** Mutator method - sets the size of the Textbox. */
	public void setSize(int newValue) { size = newValue; }

	/** Mutator method - sets the change event of the Textbox. */
	public void setOnChange(String newValue) { onChange = newValue; }

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the maximum length of the Textbox. */
	private int maxLength = NULL_VALUE_INT;

	/** Member variable - contains the size of the Textbox. */
	private int size = NULL_VALUE_INT;

	/** Member variable - contains the change event of the Textbox. */
	private String onChange = null;
}

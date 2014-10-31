package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/**********************************************************************************
*
*	Base class that represents HTML tag elements. HTML tag elements represent
*	formatting elements and data. Formatting elements are "tags" that
*	contain attributes, data, and other "tags". All elements of an HTML
*	page are contained within a tag element.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/24/2001
*
**********************************************************************************/

public abstract class TagElement extends Element
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - Attribute name for the Name attribute. */
	public static final String ATTRIBUTE_NAME = "NAME";

	/** Constant - Attribute name for the Cascading Stylesheet class name attribute. */
	public static final String ATTRIBUTE_CSS_CLASS = "CLASS";

	/** Constant - Attribute name for the Cascading Stylesheet stle string attribute. */
	public static final String ATTRIBUTE_CSS_STYLE = "STYLE";

	/** Constant - integer value that represents "no value" for element
	    attributes, when calling <CODE>writeAttribute</CODE> with an
	    integer value.
	*/
	public static final int ATTR_VALUE_NO_VALUE = Integer.MIN_VALUE;

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the element.
	*/
	public TagElement(String strName) { this(strName, null, null); }

	/** Constructor - constructs an object with a Name, a Cascading Stylesheet
	    class name, and a Cascading Stylesheet style string.
		@param strName Name of the element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
	*/
	public TagElement(String strName, String strCSSClass, String strCSSStyle)
	{
		super();

		m_strName = strName;
		m_strCSSClass = strCSSClass;
		m_strCSSStyle = strCSSStyle;
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Helper method - Writes HTML string attributes. */
	protected void writeAttribute(String strName, String strValue)
		throws IOException
	{ writeAttribute(getWriter(), strName, strValue); }

	/** Helper method - Writes HTML string attributes. */
	protected static void writeAttribute(Writer pWriter, String strName, String strValue)
		throws IOException
	{
		if (null == strValue)
			return;

		write(pWriter, " ");
		write(pWriter, strName);
		write(pWriter, "=");
		writeWithQuotes(pWriter, strValue);
	}

	/** Helper method - Writes HTML integer attributes. */
	protected void writeAttribute(String strName, int nValue)
		throws IOException
	{ writeAttribute(getWriter(), strName, nValue); }

	/** Helper method - Writes HTML integer attributes. */
	protected static void writeAttribute(Writer pWriter, String strName, int nValue)
		throws IOException
	{
		if (ATTR_VALUE_NO_VALUE == nValue)
			return;

		writeAttribute(pWriter, strName, "" + nValue);
	}

	/** Helper method - Writes HTML tags. */
	protected void writeTag(String strTag) throws IOException
	{ writeTag(getWriter(), strTag); }

	/** Helper method - Writes HTML tags. */
	protected static void writeTag(Writer pWriter, String strTag) throws IOException
	{ write(pWriter, "<"); write(pWriter, strTag); write(pWriter, ">"); }

	/** Helper method - Writes HTML closing tags. */
	protected void writeTagClosing(String strTag) throws IOException
	{ writeTagClosing(getWriter(), strTag); }

	/** Helper method - Writes HTML closing tags. */
	protected static void writeTagClosing(Writer pWriter, String strTag) throws IOException
	{ write(pWriter, "</"); write(pWriter, strTag); write(pWriter, ">"); }

	/** Helper method - opens a tag of the specified element name.
		@param strElementName Tag's element name.
	*/
	protected void openTag(String strElementName) throws IOException
	{
		openTag(getWriter(), strElementName);
	}

	/** Helper method - opens a tag of the specified element name.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strElementName Tag's element name.
	*/
	protected void openTag(Writer pWriter, String strElementName)
		throws IOException
	{
		write(pWriter, "<");
		write(pWriter, strElementName);

		writeAttribute(pWriter, ATTRIBUTE_NAME, getName());
		writeAttribute(pWriter, ATTRIBUTE_CSS_CLASS, getCSSClass());
		writeAttribute(pWriter, ATTRIBUTE_CSS_STYLE, getCSSStyle());
	}

	/** Helper method - Closes HTML tags. */
	protected void closeTag() throws IOException { closeTag(getWriter()); }

	/** Helper method - Closes HTML tags. */
	protected static void closeTag(Writer pWriter) throws IOException { write(pWriter, ">"); }

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Name of the element. */
	public String getName() { return m_strName; }

	/** Accessor method - gets the Cascading Stylesheet class name. */
	public String getCSSClass() { return m_strCSSClass; }

	/** Accessor method - gets the Cascading Stylesheet style string. */
	public String getCSSStyle() { return m_strCSSStyle; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the Name of the element. */
	public void setName(String strNewValue) { m_strName = strNewValue; }

	/** Mutator method - sets the Cascading Stylesheet class name. */
	public void setCSSClass(String strNewValue) { m_strCSSClass = strNewValue; }

	/** Mutator method - sets the Cascading Stylesheet style string. */
	public void setCSSStyle(String strNewValue) { m_strCSSStyle = strNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the Name of the input element. */
	private String m_strName = null;

	/** Member variable - contains the Cascading Stylesheet class name. */
	private String m_strCSSClass = null;

	/** Member variable - contains the Cascading Stylesheet style string. */
	private String m_strCSSStyle = null;
}

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
	public static final String ATTRIBUTE_NAME = "name";
	public static final String ATTRIBUTE_CSS_CLASS = "class";
	public static final String ATTRIBUTE_CSS_STYLE = "style";
	public static final int ATTR_VALUE_NO_VALUE = Integer.MIN_VALUE;

	private final String name;
	private final String cssClass;
	private final String cssStyle;

	public TagElement(final String name) { this(name, null, null); }

	public TagElement(final String name, final String cssClass, final String cssStyle)
	{
		super();

		this.name = name;
		this.cssClass = cssClass;
		this.cssStyle = cssStyle;
	}

	/** Helper method - Writes HTML string attributes. */
	protected void writeAttribute(String name, String value)
		throws IOException
	{ writeAttribute(writer, name, value); }

	/** Helper method - Writes HTML string attributes. */
	protected static void writeAttribute(Writer writer, String name, String value)
		throws IOException
	{
		if (null == value)
			return;

		write(writer, " ");
		write(writer, name);
		write(writer, "=");
		writeWithQuotes(writer, value);
	}

	/** Helper method - Writes HTML integer attributes. */
	protected void writeAttribute(String name, int value)
		throws IOException
	{ writeAttribute(writer, name, value); }

	/** Helper method - Writes HTML integer attributes. */
	protected static void writeAttribute(Writer writer, String name, int value)
		throws IOException
	{
		if (ATTR_VALUE_NO_VALUE == value)
			return;

		writeAttribute(writer, name, "" + value);
	}

	/** Helper method - Writes HTML tags. */
	protected void writeTag(String tag) throws IOException
	{ writeTag(writer, tag); }

	/** Helper method - Writes HTML tags. */
	protected static void writeTag(Writer writer, String tag) throws IOException
	{ write(writer, "<"); write(writer, tag); write(writer, ">"); }

	/** Helper method - Writes HTML closing tags. */
	protected void writeTagClosing(String tag) throws IOException
	{ writeTagClosing(writer, tag); }

	/** Helper method - Writes HTML closing tags. */
	protected static void writeTagClosing(Writer writer, String tag) throws IOException
	{ write(writer, "</"); write(writer, tag); write(writer, ">"); }

	/** Helper method - opens a tag of the specified element name.
		@param elementName Tag's element name.
	*/
	protected void openTag(String elementName) throws IOException
	{
		openTag(writer, elementName);
	}

	/** Helper method - opens a tag of the specified element name.
		@param writer <I>Writer</I> object used to output HTML.
		@param elementName Tag's element name.
	*/
	protected void openTag(Writer writer, String elementName)
		throws IOException
	{
		write(writer, "<");
		write(writer, elementName);

		writeAttribute(writer, ATTRIBUTE_NAME, name);
		writeAttribute(writer, ATTRIBUTE_CSS_CLASS, cssClass);
		writeAttribute(writer, ATTRIBUTE_CSS_STYLE, cssStyle);
	}

	/** Helper method - Closes HTML tags. */
	protected void closeTag() throws IOException { closeTag(writer); }

	/** Helper method - Closes HTML tags. */
	protected static void closeTag(Writer writer) throws IOException { write(writer, ">"); }
}

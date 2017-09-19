package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/************************************************************************************
*
*	Class that represents HTML Table Cell elements.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/29/2001
*
************************************************************************************/

public class TableCell extends TagElement
{
	public static final String TAG = "TD";
	public static final String ALIGN_LEFT = Table.ALIGN_LEFT;
	public static final String ALIGN_CENTER = Table.ALIGN_CENTER;
	public static final String ALIGN_RIGHT = Table.ALIGN_RIGHT;
	public static final String ALIGN_NONE = Table.ALIGN_NONE;
	public static final String VALIGN_TOP = "top";
	public static final String VALIGN_MIDDLE = "middle";
	public static final String VALIGN_BOTTOM = "bottom";
	public static final String VALIGN_NONE = null;
	public static final String ATTRIBUTE_HORIZONTAL_ALIGNMENT = "align";
	public static final String ATTRIBUTE_VERTICAL_ALIGNMENT = "valign";
	public static final String ATTRIBUTE_WIDTH = "width";
	public static final String ATTRIBUTE_HEIGHT = "height";
	public static final String ATTRIBUTE_COLUMN_SPAN = "colspan";
	public static final String ATTRIBUTE_ROW_SPAN = "rowspan";
	public static final String ATTRIBUTE_BACKGROUND_COLOR = "bgcolor";
	public static final String ATTRIBUTE_BACKGROUND_IMAGE = "background";

	public final String hAlign;
	public final String vAlign;
	public final String width;
	public final String height;
	public final int colSpan;
	public final int rowSpan;
	public final String backgroundColor;
	public final String backgroundImage;
	public final Element child;

	public String getTag() { return TAG; }	// Can be overridden. DLS on 9/19/2017.

	public TableCell(String strChildElement)
	{
		this(new TextElement(strChildElement));
	}

	public TableCell(Element child)
	{
		this(ALIGN_LEFT, VALIGN_TOP,
			null, null,
			ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE,
			null, null, child);
	}

	public TableCell(String hAlign, String vAlign,
		String width, String height, int colSpan, int rowSpan,
		String backgroundColor, String backgroundImage, Element child)
	{
		this(null, hAlign, vAlign,
			width, height, colSpan, rowSpan,
			backgroundColor, backgroundImage, child);
	}

	public TableCell(String name, String hAlign, String vAlign,
		String width, String height, int colSpan, int rowSpan,
		String backgroundColor, String backgroundImage, Element child)
	{
		this(name, null, null, hAlign, vAlign,
			width, height, colSpan, rowSpan,
			backgroundColor, backgroundImage, child);
	}

	public TableCell(String name, String cssClass, String cssStyle,
		String hAlign, String vAlign,
		String width, String height, int colSpan, int rowSpan,
		String backgroundColor, String backgroundImage, Element child)
	{
		super(name, cssClass, cssStyle);

		this.hAlign = hAlign;
		this.vAlign = vAlign;
		this.width = width;
		this.height = height;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		this.backgroundColor = backgroundColor;
		this.backgroundImage = backgroundImage;
		this.child = child;
	}

	/*****************************************************************************
	*
	*	Required method: Element
	*
	*****************************************************************************/

	/** Action method - creates the HTML table cell element.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer writer) throws IOException
	{
		create(writer, child);
	}

	/** Action method - creates the HTML table cell element.
		@param writer <I>Writer</I> object used to output HTML.
		@param child <I>Element</I> object that resides within the table cell.
	*/
	public void create(Writer writer, Element child) throws IOException
	{
		open(writer);

		if (null != child)
			child.create(writer);

		close(writer);
	}

	/** Action method - opens the HTML table cell element.
	*/
	public void open() throws IOException
	{
		open(writer);
	}

	/** Action method - opens the HTML table cell element.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public void open(Writer writer) throws IOException
	{
		openTag(writer, getTag());

		writeAttribute(writer, ATTRIBUTE_HORIZONTAL_ALIGNMENT, hAlign);
		writeAttribute(writer, ATTRIBUTE_VERTICAL_ALIGNMENT, vAlign);
		writeAttribute(writer, ATTRIBUTE_WIDTH, width);
		writeAttribute(writer, ATTRIBUTE_HEIGHT, height);
		writeAttribute(writer, ATTRIBUTE_COLUMN_SPAN, colSpan);
		writeAttribute(writer, ATTRIBUTE_ROW_SPAN, rowSpan);
		writeAttribute(writer, ATTRIBUTE_BACKGROUND_COLOR, backgroundColor);
		writeAttribute(writer, ATTRIBUTE_BACKGROUND_IMAGE, backgroundImage);

		closeTag(writer);
	}

	/** Action method - closes the HTML table cell element.
	*/
	public void close() throws IOException
	{
		close(writer);
	}

	/** Action method - closes the HTML table cell element.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public void close(Writer writer) throws IOException
	{
		writeTagClosing(writer, TAG);
	}
}

package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Class that represents the HTML Table element. Used to create table, table
*	rows, and table cells.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/11/2000
*
***************************************************************************************/

public class Table extends TagElement
{
	public static final String ALIGN_LEFT = "left";
	public static final String ALIGN_CENTER = "center";
	public static final String ALIGN_RIGHT = "right";
	public static final String ALIGN_NONE = null;
	public static final String TAG = "table";
	public static final String ATTRIBUTE_BORDER = "border";
	public static final String ATTRIBUTE_CELL_PADDING = "cellpadding";
	public static final String ATTRIBUTE_CELL_SPACING = "cellspacing";
	public static final String ATTRIBUTE_WIDTH = "width";
	public static final String ATTRIBUTE_BORDER_COLOR = "bordercolor";
	public static final String ATTRIBUTE_ALIGNMENT = "align";
	public static final String ATTRIBUTE_VERTICAL_ALIGNMENT = "valign";

	private final int border;
	private final int cellPadding;
	private final int cellSpacing;
	private final String width;
	private final String borderColor;
	private final String align;
	private final TableRows rows;

	/** Constructor - constructs an object populated with default values. */
	public Table()
	{
		this(null);
	}

	/** Constructor - constructs an object populated with default values.
		@param rows <I>TableRows</I> object that represents the body
			of the table element.
	*/
	public Table(TableRows rows)
	{
		this(null, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE,
			null, null, null, rows);
	}

	/** Constructor - constructs a populate object.
		@param name Name of the element.
		@param border Border attribute of the table element.
		@param cellPadding Cell Padding attribute of the table element.
		@param cellSpacing Cell Spacing attribute of the table element.
		@param width Width attribute of the table element.
		@param borderColor Border Color attribute of the table element.
		@param align Alignment attribute of the table element.
		@param rows <I>TableRows</I> object that represents the body
			of the table element.
	*/
	public Table(String name, int border, int cellPadding, int cellSpacing,
		String width, String borderColor, String align,
		TableRows rows)
	{
		this(name, null, null, border, cellPadding, cellSpacing,
			width, borderColor, align, rows);
	}

	/** Constructor - constructs a populate object.
		@param name Name of the element.
		@param cssClass Cascading Stylesheet class name.
		@param cssStyle Cascading Stylesheet style string.
		@param border Border attribute of the table element.
		@param cellPadding Cell Padding attribute of the table element.
		@param cellSpacing Cell Spacing attribute of the table element.
		@param width Width attribute of the table element.
		@param borderColor Border Color attribute of the table element.
		@param align Alignment attribute of the table element.
		@param rows <I>TableRows</I> object that represents the body
			of the table element.
	*/
	public Table(String name, String cssClass, String cssStyle,
		int border, int cellPadding, int cellSpacing,
		String width, String borderColor, String align,
		TableRows rows)
	{
		super(name, cssClass, cssStyle);

		this.border = border;
		this.cellPadding = cellPadding;
		this.cellSpacing = cellSpacing;
		this.width = width;
		this.borderColor = borderColor;
		this.align = align;
		this.rows = rows;
	}

	/******************************************************************************
	*
	*	Required methods - Element
	*
	******************************************************************************/

	/** Action method - create the HTML table element.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer writer) throws IOException
	{
		create(writer, rows);
	}

	/** Action method - create the HTML table element.
		@param writer <I>Writer</I> object used to output HTML.
		@param rows <I>TableRows</I> object that represents the body
			of the table element.
	*/
	public void create(Writer writer, TableRows rows) throws IOException
	{
		open(writer);

		if (null != rows)
			rows.create(writer);

		close(writer);
	}

	/******************************************************************************
	*
	*	Action methods
	*
	******************************************************************************/

	/** Action method - opens the HTML table element. */
	public void open() throws IOException
	{
		open(writer);
	}

	/** Action method - opens the HTML table element.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public void open(Writer writer)
		throws IOException
	{
		openTag(writer, TAG);

		writeAttribute(writer, ATTRIBUTE_BORDER, border);
		writeAttribute(writer, ATTRIBUTE_CELL_PADDING, cellPadding);
		writeAttribute(writer, ATTRIBUTE_CELL_SPACING, cellSpacing);
		writeAttribute(writer, ATTRIBUTE_WIDTH, width);
		writeAttribute(writer, ATTRIBUTE_BORDER_COLOR, borderColor);
		writeAttribute(writer, ATTRIBUTE_ALIGNMENT, align);

		closeTag(writer);

		writeNewLine(writer);
	}

	/** Action method - closes the HTML table element. */
	public void close() throws IOException { close(writer); }

	/** Action method - closes the HTML table element.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public void close(final Writer writer) throws IOException
	{ writeTagClosing(writer, TAG); writeNewLine(writer); }
}

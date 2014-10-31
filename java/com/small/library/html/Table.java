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
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - Left alignment attribute constant. */
	public static final String ALIGN_LEFT = "LEFT";

	/** Constant - Center alignment attribute constant. */
	public static final String ALIGN_CENTER = "CENTER";

	/** Constant - Right alignment attribute constant. */
	public static final String ALIGN_RIGHT = "RIGHT";

	/** Constant - No alignment specified attribute constant. */
	public static final String ALIGN_NONE = null;

	/** Constant - Table tag name constant. */
	public static final String TAG = "TABLE";

	/** Constant - Attribute name for the Border attribute. */
	public static final String ATTRIBUTE_BORDER = "BORDER";

	/** Constant - Attribute name for the Cell Padding attribute. */
	public static final String ATTRIBUTE_CELL_PADDING = "CELLPADDING";

	/** Constant - Attribute name for the Cell Spacing attribute. */
	public static final String ATTRIBUTE_CELL_SPACING = "CELLSPACING";

	/** Constant - Attribute name for the Width attribute. */
	public static final String ATTRIBUTE_WIDTH = "WIDTH";

	/** Constant - Attribute name for the Border Color attribute. */
	public static final String ATTRIBUTE_BORDER_COLOR = "BORDERCOLOR";

	/** Constant - Attribute name for the Alignment attribute. */
	public static final String ATTRIBUTE_ALIGNMENT = "ALIGN";

	/** Constant - Attribute name for the Vertical Alignment attribute. */
	public static final String ATTRIBUTE_VERTICAL_ALIGNMENT = "VALIGN";

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object populated with default values. */
	public Table()
	{
		this(null);
	}

	/** Constructor - constructs an object populated with default values.
		@param pTableRows <I>TableRows</I> object that represents the body
			of the table element.
	*/
	public Table(TableRows pTableRows)
	{
		this(null, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE,
			null, null, null, pTableRows);
	}

	/** Constructor - constructs a populate object.
		@param strName Name of the element.
		@param nBorder Border attribute of the table element.
		@param nCellPadding Cell Padding attribute of the table element.
		@param nCellSpacing Cell Spacing attribute of the table element.
		@param strWidth Width attribute of the table element.
		@param strBorderColor Border Color attribute of the table element.
		@param strAlignment Alignment attribute of the table element.
		@param pTableRows <I>TableRows</I> object that represents the body
			of the table element.
	*/
	public Table(String strName, int nBorder, int nCellPadding, int nCellSpacing,
		String strWidth, String strBorderColor, String strAlignment,
		TableRows pTableRows)
	{
		this(strName, null, null, nBorder, nCellPadding, nCellSpacing,
			strWidth, strBorderColor, strAlignment, pTableRows);
	}

	/** Constructor - constructs a populate object.
		@param strName Name of the element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param nBorder Border attribute of the table element.
		@param nCellPadding Cell Padding attribute of the table element.
		@param nCellSpacing Cell Spacing attribute of the table element.
		@param strWidth Width attribute of the table element.
		@param strBorderColor Border Color attribute of the table element.
		@param strAlignment Alignment attribute of the table element.
		@param pTableRows <I>TableRows</I> object that represents the body
			of the table element.
	*/
	public Table(String strName, String strCSSClass, String strCSSStyle,
		int nBorder, int nCellPadding, int nCellSpacing,
		String strWidth, String strBorderColor, String strAlignment,
		TableRows pTableRows)
	{
		super(strName, strCSSClass, strCSSStyle);

		m_nBorder = nBorder;
		m_nCellPadding = nCellPadding;
		m_nCellSpacing = nCellSpacing;
		m_strWidth = strWidth;
		m_strBorderColor = strBorderColor;
		m_strAlignment = strAlignment;
		m_TableRows = pTableRows;
	}

	/******************************************************************************
	*
	*	Required methods - Element
	*
	******************************************************************************/

	/** Action method - create the HTML table element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		create(pWriter, m_TableRows);
	}

	/** Action method - create the HTML table element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param pTableRows <I>TableRows</I> object that represents the body
			of the table element.
	*/
	public void create(Writer pWriter, TableRows pTableRows) throws IOException
	{
		open(pWriter);

		if (null != pTableRows)
			pTableRows.create(pWriter);

		close(pWriter);
	}

	/******************************************************************************
	*
	*	Action methods
	*
	******************************************************************************/

	/** Action method - opens the HTML table element. */
	public void open() throws IOException
	{
		open(getWriter());
	}

	/** Action method - opens the HTML table element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void open(Writer pWriter)
		throws IOException
	{
		openTag(pWriter, TAG);

		writeAttribute(pWriter, ATTRIBUTE_BORDER, getBorder());
		writeAttribute(pWriter, ATTRIBUTE_CELL_PADDING, getCellPadding());
		writeAttribute(pWriter, ATTRIBUTE_CELL_SPACING, getCellSpacing());
		writeAttribute(pWriter, ATTRIBUTE_WIDTH, getWidth());
		writeAttribute(pWriter, ATTRIBUTE_BORDER_COLOR, getBorderColor());
		writeAttribute(pWriter, ATTRIBUTE_ALIGNMENT, getAlignment());

		closeTag(pWriter);

		writeNewLine(pWriter);
	}

	/** Action method - closes the HTML table element. */
	public void close() throws IOException { close(getWriter()); }

	/** Action method - closes the HTML table element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void close(Writer pWriter) throws IOException
	{ writeTagClosing(pWriter, TAG); writeNewLine(pWriter); }

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Border attribute of the table element. */
	public int getBorder() { return m_nBorder; }

	/** Accessor method - gets the Cell Padding attribute of the table element. */
	public int getCellPadding() { return m_nCellPadding; }

	/** Accessor method - gets the Cell Spacing attribute of the table element. */
	public int getCellSpacing() { return m_nCellSpacing; }

	/** Accessor method - gets the Width attribute of the table element. */
	public String getWidth() { return m_strWidth; }

	/** Accessor method - gets the Border Color attribute of the table element. */
	public String getBorderColor() { return m_strBorderColor; }

	/** Accessor method - gets the Alignment attribute of the table element. */
	public String getAlignment() { return m_strAlignment; }

	/** Accessor method - gets the <I>TableRows</I> object that represents the
	    body of the table element.
	*/
	public TableRows getTableRows() { return m_TableRows; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the Border attribute of the table element. */
	public void setBorder(int nNewValue) { m_nBorder = nNewValue; }

	/** Mutator method - sets the Cell Padding attribute of the table element. */
	public void setCellPadding(int nNewValue) { m_nCellPadding = nNewValue; }

	/** Mutator method - sets the Cell Spacing attribute of the table element. */
	public void setCellSpacing(int nNewValue) { m_nCellSpacing = nNewValue; }

	/** Mutator method - sets the Width attribute of the table element. */
	public void setWidth(String strNewValue) { m_strWidth = strNewValue; }

	/** Mutator method - sets the Border Color attribute of the table element. */
	public void setBorderColor(String strNewValue) { m_strBorderColor = strNewValue; }

	/** Mutator method - sets the Alignment attribute of the table element. */
	public void setAlignment(String strNewValue) { m_strAlignment = strNewValue; }

	/** Mutator method - sets the <I>TableRows</I> object that represents the
	    body of the table element.
	*/
	public void setTableRows(TableRows pNewValue) { m_TableRows = pNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the Border attribute of the table element. */
	private int m_nBorder = ATTR_VALUE_NO_VALUE;

	/** Member variable - contains the Cell Padding attribute of the table element. */
	private int m_nCellPadding = ATTR_VALUE_NO_VALUE;

	/** Member variable - contains the Cell Spacing attribute of the table element. */
	private int m_nCellSpacing = ATTR_VALUE_NO_VALUE;

	/** Member variable - contains the Width attribute of the table element. */
	private String m_strWidth = null;

	/** Member variable - contains the Border Color attribute of the table element. */
	private String m_strBorderColor = null;

	/** Member variable - contains the Alignment attribute of the table element. */
	private String m_strAlignment = null;

	/** Member variable - contains a reference to a <I>TableRows</I> object that
	    represents the body of the table element.
	*/
	private TableRows m_TableRows = null;
}

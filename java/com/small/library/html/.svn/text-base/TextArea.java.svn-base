package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Class that represents the HTML textarea element.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public class TextArea extends FormElement
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - prefix for the textarea element. */
	public static final String PREFIX = "txt";

	/** Constant - form element's tag. */
	public static final String TAG = "TEXTAREA";

	/** Constant - textarea wrap mode of "None". */
	public static final String WRAP_NONE = null;

	/** Constant - textarea wrap mode of "Virtual". */
	public static final String WRAP_VIRTUAL = "VIRTUAL";

	/** Constant - textarea wrap mode of "Physical". */
	public static final String WRAP_PHYSICAL = "PHYSICAL";

	/** Constant - attribute name of the Columns attribute. */
	public static final String ATTRIBUTE_COLUMNS = "COLS";

	/** Constant - attribute name of the Rows attribute. */
	public static final String ATTRIBUTE_ROWS = "ROWS";

	/** Constant - attribute name of the Wrap attribute. */
	public static final String ATTRIBUTE_WRAP = "WRAP";

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
		@param nColumns Column width in number of characters.
		@param nRows Row height in number of characters.
		@param strWrap Wrap attribute of the textarea element.
	*/
	public TextArea(String strName,
		int nColumns, int nRows, String strWrap)
	{ this(strName, null, null, nColumns, nRows, strWrap); }

	/** Constructor - constructs an object with a Name, a Default Value attribute,
	    a Cascading Stylesheet class name, and a Cascading Stylesheet style string.
		@param strName Name of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param nColumns Column width in number of characters.
		@param nRows Row height in number of characters.
		@param strWrap Wrap attribute of the textarea element.
	*/
	public TextArea(String strName, String strCSSClass, String strCSSStyle,
		int nColumns, int nRows, String strWrap)
	{
		super(PREFIX + strName, null, strCSSClass, strCSSStyle);

		m_nColumns = nColumns;
		m_nRows = nRows;
		m_strWrap = strWrap;

		m_bHasCSSStyling = ((null != strCSSClass) || (null != strCSSStyle))
			? true : false;
	}

	/******************************************************************************
	*
	*	Required methods: Input
	*
	******************************************************************************/

	/** Action method - creates the HTML textarea element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		open(pWriter);
		close(pWriter);
	}

	/** Action method - creates the HTML textarea element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strValue Value element of the textarea element.
	*/
	public void create(Writer pWriter, String strValue) throws IOException
	{
		open(pWriter);
		write(pWriter, strValue);
		close(pWriter);
	}

	/******************************************************************************
	*
	*	Action methods
	*
	******************************************************************************/

	/** Action method - opens the HTML textarea element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void open(Writer pWriter) throws IOException
	{
		if (m_bHasCSSStyling)
		{
			openTag(pWriter, "SPAN");
			writeAttribute(pWriter, ATTRIBUTE_CSS_CLASS, getCSSClass());
			writeAttribute(pWriter, ATTRIBUTE_CSS_STYLE, getCSSStyle());
			closeTag(pWriter);
		}

		openTag(pWriter, TAG);

		writeAttribute(pWriter, ATTRIBUTE_COLUMNS, getColumns());
		writeAttribute(pWriter, ATTRIBUTE_ROWS, getRows());
		writeAttribute(pWriter, ATTRIBUTE_WRAP, getWrap());

		closeTag(pWriter);
	}

	/** Action method - closes the HTML textarea element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void close(Writer pWriter) throws IOException
	{
		writeTagClosing(pWriter, TAG);

		if (m_bHasCSSStyling)
			writeTagClosing(pWriter, "SPAN");
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the column width in number of characters. */
	public int getColumns() { return m_nColumns; }

	/** Accessor method - gets the row height in number of characters. */
	public int getRows() { return m_nRows; }

	/** Accessor method - gets the Wrap attribute of the textarea element. */
	public String getWrap() { return m_strWrap; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the column width in number of characters. */
	public void setColumns(int nNewValue) { m_nColumns = nNewValue; }

	/** Mutator method - sets the row height in number of characters. */
	public void setRows(int nNewValue) { m_nRows = nNewValue; }

	/** Mutator method - sets the Wrap attribute of the textarea element. */
	public void setWrap(String strNewValue) { m_strWrap = strNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the column width in number of characters. */
	private int m_nColumns = 0;

	/** Member variable - contains the row height in number of characters. */
	private int m_nRows = 0;

	/** Member variable - contains the Wrap attribute of the textarea element. */
	private String m_strWrap = null;

	/** Member variable - indicates whether Cascading Stylesheet styling has
	    be supplied. A styling maybe a CSS Class or CSS Style string.
	*/
	private boolean m_bHasCSSStyling = false;
}

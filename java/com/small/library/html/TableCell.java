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
	/*****************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - element's tag name. */
	public static final String TAG = "TD";

	/** Constant - Left alignment attribute constant. */
	public static final String ALIGN_LEFT = Table.ALIGN_LEFT;

	/** Constant - Center alignment attribute constant. */
	public static final String ALIGN_CENTER = Table.ALIGN_CENTER;

	/** Constant - Right alignment attribute constant. */
	public static final String ALIGN_RIGHT = Table.ALIGN_RIGHT;

	/** Constant - No alignment specified attribute constant. */
	public static final String ALIGN_NONE = Table.ALIGN_NONE;

	/** Constant - Top vertical alignment attribute constant. */
	public static final String VALIGN_TOP = "TOP";

	/** Constant - Middle vertical alignment attribute constant. */
	public static final String VALIGN_MIDDLE = "MIDDLE";

	/** Constant - Bottom vertical alignment attribute constant. */
	public static final String VALIGN_BOTTOM = "BOTTOM";

	/** Constant - No vertical alignment specified attribute constant. */
	public static final String VALIGN_NONE = null;

	/** Constant - attribute name of the Horizontal Alignment attribute. */
	public static final String ATTRIBUTE_HORIZONTAL_ALIGNMENT = "ALIGN";

	/** Constant - attribute name of the Vertical Alignment attribute. */
	public static final String ATTRIBUTE_VERTICAL_ALIGNMENT = "VALIGN";

	/** Constant - attribute name of the Width attribute. */
	public static final String ATTRIBUTE_WIDTH = "WIDTH";

	/** Constant - attribute name of the Height attribute. */
	public static final String ATTRIBUTE_HEIGHT = "HEIGHT";

	/** Constant - attribute name of the Column Span attribute. */
	public static final String ATTRIBUTE_COLUMN_SPAN = "COLSPAN";

	/** Constant - attribute name of the Row Span attribute. */
	public static final String ATTRIBUTE_ROW_SPAN = "ROWSPAN";

	/** Constant - attribute name of the Background Color attribute. */
	public static final String ATTRIBUTE_BACKGROUND_COLOR = "BGCOLOR";

	/** Constant - attribute name of the Background Image attribute. */
	public static final String ATTRIBUTE_BACKGROUND_IMAGE = "BACKGROUND";

	/*****************************************************************************
	*
	*	Constructor
	*
	*****************************************************************************/

	/** Constructor - constructs a populated object with default values.
		@param strChildElement <I>String</I> that resides within the table cell.
	*/
	public TableCell(String strChildElement)
	{
		this(new TextElement(strChildElement));
	}

	/** Constructor - constructs a populated object with default values.
		@param pChildElement <I>Element</I> object that resides within the table cell.
	*/
	public TableCell(Element pChildElement)
	{
		this(ALIGN_LEFT, VALIGN_TOP,
			null, null,
			ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE,
			null, null, pChildElement);
	}

	/** Constructor - constructs a populated object.
		@param strHorizontalAlignment Horizontal Alignment attribute of the table cell.
		@param strVerticalAlignment Vertical Alignment attribute of the table cell.
		@param strWidth Width attribute of the table cell.
		@param strHeight Height attribute of the table cell.
		@param nColumnSpan Column Span attribute of the table cell.
		@param nRowSpan Row Span attribute of the table cell.
		@param strBackgroundColor Background Color attribute of the table cell.
		@param strBackgroundImage Background Image attribute of the table cell.
		@param pChildElement <I>Element</I> object that resides within the table cell.
	*/
	public TableCell(String strHorizontalAlignment, String strVerticalAlignment,
		String strWidth, String strHeight, int nColumnSpan, int nRowSpan,
		String strBackgroundColor, String strBackgroundImage, Element pChildElement)
	{
		this(null, strHorizontalAlignment, strVerticalAlignment,
			strWidth, strHeight, nColumnSpan, nRowSpan,
			strBackgroundColor, strBackgroundImage, pChildElement);
	}

	/** Constructor - constructs a populated object.
		@param strName Name of the element.
		@param strHorizontalAlignment Horizontal Alignment attribute of the table cell.
		@param strVerticalAlignment Vertical Alignment attribute of the table cell.
		@param strWidth Width attribute of the table cell.
		@param strHeight Height attribute of the table cell.
		@param nColumnSpan Column Span attribute of the table cell.
		@param nRowSpan Row Span attribute of the table cell.
		@param strBackgroundColor Background Color attribute of the table cell.
		@param strBackgroundImage Background Image attribute of the table cell.
		@param pChildElement <I>Element</I> object that resides within the table cell.
	*/
	public TableCell(String strName, String strHorizontalAlignment, String strVerticalAlignment,
		String strWidth, String strHeight, int nColumnSpan, int nRowSpan,
		String strBackgroundColor, String strBackgroundImage, Element pChildElement)
	{
		this(strName, null, null, strHorizontalAlignment, strVerticalAlignment,
			strWidth, strHeight, nColumnSpan, nRowSpan,
			strBackgroundColor, strBackgroundImage, pChildElement);
	}

	/** Constructor - constructs a populated object.
		@param strName Name of the element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param strHorizontalAlignment Horizontal Alignment attribute of the table cell.
		@param strVerticalAlignment Vertical Alignment attribute of the table cell.
		@param strWidth Width attribute of the table cell.
		@param strHeight Height attribute of the table cell.
		@param nColumnSpan Column Span attribute of the table cell.
		@param nRowSpan Row Span attribute of the table cell.
		@param strBackgroundColor Background Color attribute of the table cell.
		@param strBackgroundImage Background Image attribute of the table cell.
		@param pChildElement <I>Element</I> object that resides within the table cell.
	*/
	public TableCell(String strName, String strCSSClass, String strCSSStyle,
		String strHorizontalAlignment, String strVerticalAlignment,
		String strWidth, String strHeight, int nColumnSpan, int nRowSpan,
		String strBackgroundColor, String strBackgroundImage, Element pChildElement)
	{
		super(strName, strCSSClass, strCSSStyle);

		m_strHorizontalAlignment = strHorizontalAlignment;
		m_strVerticalAlignment = strVerticalAlignment;
		m_strWidth = strWidth;
		m_strHeight = strHeight;
		m_nColumnSpan = nColumnSpan;
		m_nRowSpan = nRowSpan;
		m_strBackgroundColor = strBackgroundColor;
		m_strBackgroundImage = strBackgroundImage;
		m_ChildElement = pChildElement;
	}

	/*****************************************************************************
	*
	*	Required method: Element
	*
	*****************************************************************************/

	/** Action method - creates the HTML table cell element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		create(pWriter, m_ChildElement);
	}

	/** Action method - creates the HTML table cell element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param pChildElement <I>Element</I> object that resides within the table cell.
	*/
	public void create(Writer pWriter, Element pChildElement) throws IOException
	{
		open(pWriter);

		if (null != pChildElement)
			pChildElement.create(pWriter);

		close(pWriter);
	}

	/** Action method - opens the HTML table cell element.
	*/
	public void open() throws IOException
	{
		open(getWriter());
	}

	/** Action method - opens the HTML table cell element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void open(Writer pWriter) throws IOException
	{
		openTag(pWriter, getTag());

		writeAttribute(pWriter, ATTRIBUTE_HORIZONTAL_ALIGNMENT, m_strHorizontalAlignment);
		writeAttribute(pWriter, ATTRIBUTE_VERTICAL_ALIGNMENT, m_strVerticalAlignment);
		writeAttribute(pWriter, ATTRIBUTE_WIDTH, m_strWidth);
		writeAttribute(pWriter, ATTRIBUTE_HEIGHT, m_strHeight);
		writeAttribute(pWriter, ATTRIBUTE_COLUMN_SPAN, m_nColumnSpan);
		writeAttribute(pWriter, ATTRIBUTE_ROW_SPAN, m_nRowSpan);
		writeAttribute(pWriter, ATTRIBUTE_BACKGROUND_COLOR, m_strBackgroundColor);
		writeAttribute(pWriter, ATTRIBUTE_BACKGROUND_IMAGE, m_strBackgroundImage);

		closeTag(pWriter);
	}

	/** Action method - closes the HTML table cell element.
	*/
	public void close() throws IOException
	{
		close(getWriter());
	}

	/** Action method - closes the HTML table cell element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void close(Writer pWriter) throws IOException
	{
		writeTagClosing(pWriter, TAG);
	}

	/*****************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Parent Override - supplies a more specific element tag name. */
	public String getTag() { return TAG; }

	/** Accessor method - gets the Horizontal Alignment attribute of the table cell. */
	public String getHorizontalAlignment() { return m_strHorizontalAlignment; }

	/** Accessor method - gets the Vertical Alignment attribute of the table cell. */
	public String getVerticalAlignment() { return m_strVerticalAlignment; }

	/** Accessor method - gets the Width attribute of the table cell. */
	public String getWidth() { return m_strWidth; }

	/** Accessor method - gets the Height attribute of the table cell. */
	public String getHeight() { return m_strHeight; }

	/** Accessor method - gets the Column Span attribute of the table cell. */
	public int getColumnSpan() { return m_nColumnSpan; }

	/** Accessor method - gets the Row Span attribute of the table cell. */
	public int getRowSpan() { return m_nRowSpan; }

	/** Accessor method - gets the Background Color attribute of the table cell. */
	public String getBackgroundColor() { return m_strBackgroundColor; }

	/** Accessor method - gets the Background Image attribute of the table cell. */
	public String getBackgroundImage() { return m_strBackgroundImage; }

	/** Accessor method - gets the <I>Element</I> object that resides within
	    the table cell.
	*/
	public Element getChildElement() { return m_ChildElement; }

	/*****************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	/** Mutator method - sets the Horizontal Alignment attribute of the table cell. */
	public void setHorizontalAlignment(String strNewValue) { m_strHorizontalAlignment = strNewValue; }

	/** Mutator method - sets the Vertical Alignment attribute of the table cell. */
	public void setVerticalAlignment(String strNewValue) { m_strVerticalAlignment = strNewValue; }

	/** Mutator method - sets the Width attribute of the table cell. */
	public void setWidth(String strNewValue) { m_strWidth = strNewValue; }

	/** Mutator method - sets the Height attribute of the table cell. */
	public void setHeight(String strNewValue) { m_strHeight = strNewValue; }

	/** Mutator method - sets the Column Span attribute of the table cell. */
	public void setColumnSpan(int nNewValue) { m_nColumnSpan = nNewValue; }

	/** Mutator method - sets the Row Span attribute of the table cell. */
	public void setRowSpan(int nNewValue) { m_nRowSpan = nNewValue; }

	/** Mutator method - sets the Background Color attribute of the table cell. */
	public void setBackgoundColor(String strNewValue) { m_strBackgroundColor = strNewValue; }

	/** Mutator method - sets the Background Image attribute of the table cell. */
	public void setBackgroundImage(String strNewValue) { m_strBackgroundImage = strNewValue; }

	/** Mutator method - sets the <I>Element</I> object that resides within
	    the table cell.
	*/
	public void setChildElement(Element pNewValue) { m_ChildElement = pNewValue; }

	/*****************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - contains the Horizontal Alignment attribute of the table cell. */
	private String m_strHorizontalAlignment = null;

	/** Member variable - contains the Vertical Alignment attribute of the table cell. */
	private String m_strVerticalAlignment = null;

	/** Member variable - contains the Width attribute of the table cell. */
	private String m_strWidth = null;

	/** Member variable - contains the Height attribute of the table cell. */
	private String m_strHeight = null;

	/** Member variable - contains the Column Span attribute of the table cell. */
	private int m_nColumnSpan = ATTR_VALUE_NO_VALUE;

	/** Member variable - contains the Row Span attribute of the table cell. */
	private int m_nRowSpan = ATTR_VALUE_NO_VALUE;

	/** Member variable - contains the Background Color attribute of the table cell. */
	private String m_strBackgroundColor = null;

	/** Member variable - contains the Background Image attribute of the table cell. */
	private String m_strBackgroundImage = null;

	/** Member variable - contains the <I>Element</I> object that resides within
	    the table cell.
	*/
	private Element m_ChildElement = null;
}

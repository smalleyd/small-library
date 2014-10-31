package com.small.library.html;

/************************************************************************************
*
*	Class that represents HTML Table Header elements.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/29/2001
*
************************************************************************************/

public class TableHeader extends TableCell
{
	/*****************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - element's tag name. */
	public static final String TAG = "TH";

	/*****************************************************************************
	*
	*	Constructor
	*
	*****************************************************************************/

	/** Constructor - constructs a populated object with default values.
		@param strChildElement <I>String</I> that resides within the table cell.
	*/
	public TableHeader(String strChildElement)
	{
		this(new TextElement(strChildElement));
	}

	/** Constructor - constructs a populated object with default values.
		@param pChildElement <I>Element</I> object that resides within the table cell.
	*/
	public TableHeader(Element pChildElement)
	{
		this(ALIGN_LEFT, VALIGN_TOP,
			null, null,
			ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE,
			null, null, pChildElement);
	}

	/** Constructor - constructs an populated object.
		@param strHorizontalAlignment Horizontal Alignment attribute of the table cell.
		@param strVerticalAlignment Vertical Alignment attribute of the table cell.
		@param strWidth Width attribute of the table cell.
		@param strHeight Height attribute of the table cell.
		@param nColumnSpan Column Span attribute of the table cell.
		@param nRowSpan Row Span attribute of the table cell.
		@param strBackgroundColor Background Color attribute of the table cell.
		@param strBackgroundImage Background Image attribute of the table cell.
		@param pData <I>Element</I> object that resides within the table cell.
	*/
	public TableHeader(String strHorizontalAlignment, String strVerticalAlignment,
		String strWidth, String strHeight, int nColumnSpan, int nRowSpan,
		String strBackgroundColor, String strBackgroundImage, Element pData)
	{
		this(null, strHorizontalAlignment, strVerticalAlignment,
			strWidth, strHeight, nColumnSpan, nRowSpan,
			strBackgroundColor, strBackgroundImage, pData);
	}

	/** Constructor - constructs an populated object.
		@param strName Name of the element.
		@param strHorizontalAlignment Horizontal Alignment attribute of the table cell.
		@param strVerticalAlignment Vertical Alignment attribute of the table cell.
		@param strWidth Width attribute of the table cell.
		@param strHeight Height attribute of the table cell.
		@param nColumnSpan Column Span attribute of the table cell.
		@param nRowSpan Row Span attribute of the table cell.
		@param strBackgroundColor Background Color attribute of the table cell.
		@param strBackgroundImage Background Image attribute of the table cell.
		@param pData <I>Element</I> object that resides within the table cell.
	*/
	public TableHeader(String strName, String strHorizontalAlignment, String strVerticalAlignment,
		String strWidth, String strHeight, int nColumnSpan, int nRowSpan,
		String strBackgroundColor, String strBackgroundImage, Element pData)
	{
		this(strName, null, null, strHorizontalAlignment, strVerticalAlignment,
			strWidth, strHeight, nColumnSpan, nRowSpan,
			strBackgroundColor, strBackgroundImage, pData);
	}

	/** Constructor - constructs an populated object.
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
		@param pData <I>Element</I> object that resides within the table cell.
	*/
	public TableHeader(String strName, String strCSSClass, String strCSSStyle,
		String strHorizontalAlignment, String strVerticalAlignment,
		String strWidth, String strHeight, int nColumnSpan, int nRowSpan,
		String strBackgroundColor, String strBackgroundImage, Element pData)
	{
		super(strName, strCSSClass, strCSSStyle,
			strHorizontalAlignment, strVerticalAlignment,
			strWidth, strHeight, nColumnSpan, nRowSpan,
			strBackgroundColor, strBackgroundImage, pData);
	}

	/*****************************************************************************
	*
	*	Parent Override: TableCell
	*
	*****************************************************************************/

	/** Parent Override - supplies a more specific element tag name. */
	public String getTag() { return TAG; }
}

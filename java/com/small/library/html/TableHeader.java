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
	public static final String TAG = "th";

	@Override
	public String getTag() { return TAG; }

	public TableHeader(final String child)
	{
		this(new TextElement(child));
	}

	public TableHeader(Element child)
	{
		this(ALIGN_LEFT, VALIGN_TOP,
			null, null,
			ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE,
			null, null, child);
	}

	public TableHeader(String hAlign, String vAlign,
		String width, String height, int colSpan, int rowSpan,
		String backgroundColor, String backgroundImage, Element data)
	{
		this(null, hAlign, vAlign,
			width, height, colSpan, rowSpan,
			backgroundColor, backgroundImage, data);
	}

	public TableHeader(String name, String hAlign, String vAlign,
		String width, String height, int colSpan, int rowSpan,
		String backgroundColor, String backgroundImage, Element data)
	{
		this(name, null, null, hAlign, vAlign,
			width, height, colSpan, rowSpan,
			backgroundColor, backgroundImage, data);
	}

	public TableHeader(String name, String cssClass, String cssStyle,
		String hAlign, String vAlign,
		String width, String height, int colSpan, int rowSpan,
		String backgroundColor, String backgroundImage, Element data)
	{
		super(name, cssClass, cssStyle,
			hAlign, vAlign,
			width, height, colSpan, rowSpan,
			backgroundColor, backgroundImage, data);
	}
}

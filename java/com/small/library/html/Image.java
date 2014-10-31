package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/*****************************************************************************************
*
*	Class that represents HTML Image elements.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 9/25/2000
*
*****************************************************************************************/

public class Image extends TagElement
{
	/*********************************************************************************
	*
	*	Constants
	*
	*********************************************************************************/

	/** Constant - main tag. */
	public static final String TAG = "IMG";

	/** Constant - element's name prefix. */
	public static final String PREFIX = "img";

	/** Constant - source attribute. */
	public static final String ATTRIBUTE_SOURCE = "SRC";

	/** Constant - width attribute. */
	public static final String ATTRIBUTE_WIDTH = "WIDTH";

	/** Constant - height attribute. */
	public static final String ATTRIBUTE_HEIGHT = "HEIGHT";

	/** Constant - caption (alt) attribute. */
	public static final String ATTRIBUTE_CAPTION = "ALT";

	/** Constant - border attribute. */
	public static final String ATTRIBUTE_BORDER = "BORDER";

	/** Constant - empty border attribute. */
	public static final int BORDER_NO_VALUE = ATTR_VALUE_NO_VALUE;

	/*********************************************************************************
	*
	*	Constructors/Destructor
	*
	*********************************************************************************/

	/** Constructor - constructs a populated object.
		@param strSource Source attribute of the image.
	*/
	public Image(String strSource)
	{ this(strSource, null, null, null, BORDER_NO_VALUE); }

	/** Constructor - constructs a populated object.
		@param strSource Source attribute of the image.
		@param strWidth Width attribute of the image.
		@param strHeight Height attribute of the image.
		@param strCaption Caption attribute of the image.
		@param strName Name of the element.
	*/
	public Image(String strSource, String strWidth, String strHeight,
		String strCaption, String strName)
	{ this(strSource, strWidth, strHeight, strCaption, strName, BORDER_NO_VALUE); }

	/** Constructor - constructs a populated object.
		@param strSource Source attribute of the image.
		@param strWidth Width attribute of the image.
		@param strHeight Height attribute of the image.
		@param strCaption Caption attribute of the image.
		@param nBorder Border attribute of the image.
	*/
	public Image(String strSource, String strWidth, String strHeight,
		String strCaption, int nBorder)
	{ this(strSource, strWidth, strHeight, strCaption, null, nBorder); }

	/** Constructor - constructs a populated object.
		@param strSource Source attribute of the image.
		@param strWidth Width attribute of the image.
		@param strHeight Height attribute of the image.
		@param strCaption Caption attribute of the image.
		@param strName Name of the element.
		@param nBorder Border attribute of the image.
	*/
	public Image(String strSource, String strWidth, String strHeight,
		String strCaption, String strName, int nBorder)
	{
		super(PREFIX + strName);

		m_strSource = strSource;
		m_strWidth = strWidth;
		m_strHeight = strHeight;
		m_strCaption = strCaption;
		m_nBorder = nBorder;
	}

	/*********************************************************************************
	*
	*	Required methods - Element
	*
	*********************************************************************************/

	/** Action method - creates the HTML image.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		openTag(pWriter, TAG);

		writeAttribute(pWriter, ATTRIBUTE_SOURCE, m_strSource);
		writeAttribute(pWriter, ATTRIBUTE_WIDTH, m_strWidth);
		writeAttribute(pWriter, ATTRIBUTE_HEIGHT, m_strHeight);
		writeAttribute(pWriter, ATTRIBUTE_CAPTION, m_strCaption);
		writeAttribute(pWriter, ATTRIBUTE_BORDER, m_nBorder);

		closeTag(pWriter);
	}

	/*********************************************************************************
	*
	*	Accessor methods
	*
	*********************************************************************************/

	/** Accessor - gets the Source attribute of the image. */
	public String getSource() { return m_strSource; }

	/** Accessor - gets the Width attribute of the image. */
	public String getWidth() { return m_strWidth; }

	/** Accessor - gets the Height attribute of the image. */
	public String getHeight() { return m_strHeight; }

	/** Accessor - gets the Caption attribute of the image. */
	public String getCaption() { return m_strCaption; }

	/** Accessor - gets the Border attribute of the image. */
	public int getBorder() { return m_nBorder; }

	/*********************************************************************************
	*
	*	Mutator methods
	*
	*********************************************************************************/

	/** Mutator - sets the Source attribute of the image. */
	public void setSource(String strNewValue) { m_strSource = strNewValue; }

	/** Mutator - sets the Width attribute of the image. */
	public void setWidth(String strNewValue) { m_strWidth = strNewValue; }

	/** Mutator - sets the Height attribute of the image. */
	public void setHeight(String strNewValue) { m_strHeight = strNewValue; }

	/** Mutator - sets the Caption attribute of the image. */
	public void setCaption(String strNewValue) { m_strCaption = strNewValue; }

	/** Mutator - sets the Border attribute of the image. */
	public void setBorder(int nNewValue) { m_nBorder = nNewValue; }

	/*********************************************************************************
	*
	*	Member variables
	*
	*********************************************************************************/

	/** Member variable - contains the Source attribute of the image. */
	private String m_strSource = null;

	/** Member variable - contains the Width attribute of the image. */
	private String m_strWidth = null;

	/** Member variable - contains the Height attribute of the image. */
	private String m_strHeight = null;

	/** Member variable - contains the Caption attribute of the image. */
	private String m_strCaption = null;

	/** Member variable - contains the Border attribute of the image. */
	private int m_nBorder = BORDER_NO_VALUE;
}

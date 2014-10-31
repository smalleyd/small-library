package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/****************************************************************************************
*
*	Class that represents the HTML Anchor element.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 11/7/2000
*
****************************************************************************************/

public class Anchor extends TagElement
{
	/*********************************************************************************
	*
	*	Constants
	*
	*********************************************************************************/

	/** Constant - main tag. */
	public static final String TAG = "A";

	/** Constant - HREF attribute. */
	public static final String ATTRIBUTE_HREF = "HREF";

	/** Constant - target attribute. */
	public static final String ATTRIBUTE_TARGET = "TARGET";

	/** Constant - Click event attribute. */
	public static final String ATTRIBUTE_ON_CLICK = "onClick";

	/** Constant - Mouse Over event attribute. */
	public static final String ATTRIBUTE_ON_MOUSE_OVER = "onMouseOver";

	/** Constant - Mouse Out event attribute. */
	public static final String ATTRIBUTE_ON_MOUSE_OUT = "onMouseOut";

	/*********************************************************************************
	*
	*	Constructors
	*
	*********************************************************************************/

	/** Constructor - constructs a populated object.
		@param strHref Href attribute of the anchor.
	*/
	public Anchor(String strHref)
	{
		this(strHref, null);
	}

	/** Constructor - constructs a populated object.
		@param strHref Href attribute of the anchor.
		@param pChildElement Child Element contained within the anchor element.
	*/
	public Anchor(String strHref, Element pChildElement)
	{
		this(strHref, null, null,
			null, null, pChildElement);
	}

	/** Constructor - constructs a populated object.
		@param strHref Href attribute of the anchor.
		@param strTarget Target attribute of the anchor.
		@param strOnClick onClick event attribute of the anchor.
		@param strOnMouseOver onMouseOver event attribute of the anchor.
		@param strOnMouseOut onMouseOut event attribute of the anchor.
		@param pChildElement Child Element contained within the anchor element.
	*/
	public Anchor(String strHref, String strTarget, String strOnClick,
		String strOnMouseOver, String strOnMouseOut, Element pChildElement)
	{
		this(null, strHref, strTarget, strOnClick,
			strOnMouseOver, strOnMouseOut, pChildElement);
	}

	/** Constructor - constructs a populated object.
		@param strName Name of the element.
		@param strHref Href attribute of the anchor.
		@param strTarget Target attribute of the anchor.
		@param strOnClick onClick event attribute of the anchor.
		@param strOnMouseOver onMouseOver event attribute of the anchor.
		@param strOnMouseOut onMouseOut event attribute of the anchor.
		@param pChildElement Child Element contained within the anchor element.
	*/
	public Anchor(String strName, String strHref, String strTarget, String strOnClick,
		String strOnMouseOver, String strOnMouseOut, Element pChildElement)
	{
		this(strName, null, null, strHref, strTarget, strOnClick,
			strOnMouseOver, strOnMouseOut, pChildElement);
	}

	/** Constructor - constructs a populated object.
		@param strName Name of the element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param strHref Href attribute of the anchor.
		@param strTarget Target attribute of the anchor.
		@param strOnClick onClick event attribute of the anchor.
		@param strOnMouseOver onMouseOver event attribute of the anchor.
		@param strOnMouseOut onMouseOut event attribute of the anchor.
		@param pChildElement Child Element contained within the anchor element.
	*/
	public Anchor(String strName, String strCSSClass, String strCSSStyle,
		String strHref, String strTarget, String strOnClick,
		String strOnMouseOver, String strOnMouseOut, Element pChildElement)
	{
		super(strName, strCSSClass, strCSSStyle);

		m_strHref = strHref;
		m_strTarget = strTarget;
		m_strOnClick = strOnClick;
		m_strOnMouseOver = strOnMouseOver;
		m_strOnMouseOut = strOnMouseOut;
		m_ChildElement = pChildElement;
	}

	/*********************************************************************************
	*
	*	Required methods: Element
	*
	*********************************************************************************/

	/** Action method - creates the HTML anchor.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter)
		throws IOException
	{ create(pWriter, getChildElement(), getHref()); }

	/** Action method - creates the HTML anchor.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param pChildElement Child Element contained within the anchor element.
	*/
	public void create(Writer pWriter, Element pChildElement)
		throws IOException
	{ create(pWriter, pChildElement, getHref()); }

	/** Action method - creates the HTML anchor.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strHref Href attribute of the anchor.
	*/
	public void create(Writer pWriter, String strHref)
		throws IOException
	{ create(pWriter, getChildElement(), strHref); }

	/** Action method - creates the HTML anchor.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param pChildElement Child Element contained within the anchor element.
		@param strHref Href attribute of the anchor.
	*/
	public void create(Writer pWriter, Element pChildElement, String strHref)
		throws IOException
	{
		open(pWriter, strHref);

		if (null != pChildElement)
			pChildElement.create(pWriter);

		close(pWriter);
	}

	/*********************************************************************************
	*
	*	Action methods
	*
	*********************************************************************************/

	/** Action method - opens the HTML anchor.
	*/
	public void open() throws IOException
	{ open(getWriter()); }

	/** Action method - opens the HTML anchor.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void open(Writer pWriter) throws IOException
	{ open(pWriter, m_strHref); }

	/** Action method - opens the HTML anchor.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strHref Href attribute of the anchor.
	*/
	public void open(Writer pWriter, String strHref) throws IOException
	{
		openTag(pWriter, TAG);

		writeAttribute(pWriter, ATTRIBUTE_HREF, strHref);
		writeAttribute(pWriter, ATTRIBUTE_TARGET, m_strTarget);
		writeAttribute(pWriter, ATTRIBUTE_ON_CLICK, m_strOnClick);
		writeAttribute(pWriter, ATTRIBUTE_ON_MOUSE_OVER, m_strOnMouseOver);
		writeAttribute(pWriter, ATTRIBUTE_ON_MOUSE_OUT, m_strOnMouseOut);

		closeTag(pWriter);
	}

	/** Action method - closes the HTML anchor.
	*/
	public void close() throws IOException
	{ close(getWriter()); }

	/** Action method - closes the HTML anchor.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void close(Writer pWriter) throws IOException
	{
		writeTagClosing(pWriter, TAG);
	}

	/*********************************************************************************
	*
	*	Accessor methods
	*
	*********************************************************************************/

	/** Mutator - gets the Href attribute of the anchor. */
	public String getHref() { return m_strHref; }

	/** Mutator - gets the Target attribute of the anchor. */
	public String getTarget() { return m_strTarget; }

	/** Mutator - gets the onClick event attribute of the anchor. */
	public String getOnClick() { return m_strOnClick; }

	/** Mutator - gets the onMouseOver event attribute of the anchor. */
	public String getOnMouseOver() { return m_strOnMouseOver; }

	/** Mutator - gets the onMouseOut event attribute of the anchor. */
	public String getOnMouseOut() { return m_strOnMouseOut; }

	/** Mutator - gets the Child Element contained within the anchor. */
	public Element getChildElement() { return m_ChildElement; }

	/*********************************************************************************
	*
	*	Mutator methods
	*
	*********************************************************************************/

	/** Mutator - sets the Href attribute of the anchor. */
	public void setHref(String strNewValue) { m_strHref = strNewValue; }

	/** Mutator - sets the Target attribute of the anchor. */
	public void setTarget(String strNewValue) { m_strTarget = strNewValue; }

	/** Mutator - sets the onClick event attribute of the anchor. */
	public void setOnClick(String strNewValue) { m_strOnClick = strNewValue; }

	/** Mutator - sets the onMouseOver event attribute of the anchor. */
	public void setOnMouseOver(String strNewValue) { m_strOnMouseOver = strNewValue; }

	/** Mutator - sets the onMouseOut event attribute of the anchor. */
	public void setOnMouseOut(String strNewValue) { m_strOnMouseOut = strNewValue; }

	/** Mutator - sets the Child Element contained within the anchor. */
	public void setChildElement(Element pNewValue) { m_ChildElement = pNewValue; }

	/*********************************************************************************
	*
	*	Member variables
	*
	*********************************************************************************/

	/** Member variable - contains the Href attribute of the anchor. */
	private String m_strHref = null;

	/** Member variable - contains the Target attribute of the anchor. */
	private String m_strTarget = null;

	/** Member variable - contains the onClick event attribute of the anchor. */
	private String m_strOnClick = null;

	/** Member variable - contains the onMouseOver event attribute of the anchor. */
	private String m_strOnMouseOver = null;

	/** Member variable - contains the onMouseOut event attribute of the anchor. */
	private String m_strOnMouseOut = null;

	/** Member variable - contains a reference to a child element of the anchor. */
	private Element m_ChildElement;
}

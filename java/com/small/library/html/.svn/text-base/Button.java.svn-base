package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Class that represents the HTML button element.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public class Button extends Input
{
	/******************************************************************************
	*
	*	Public constants
	*
	******************************************************************************/

	/** Constant - General use name prefix. */
	public static final String PREFIX = "cmd";

	/** Constant - button type "BUTTON". */
	public static final String TYPE_BUTTON = "BUTTON";

	/** Constant - button type "SUBMIT". */
	public static final String TYPE_SUBMIT = "SUBMIT";

	/** Constant - button type "RESET". */
	public static final String TYPE_RESET = "RESET";

	/** Constant - default button type. */
	public static final String TYPE_DEFAULT = TYPE_BUTTON;

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the button.
	*/
	public Button(String strName)
	{
		this(strName, TYPE_DEFAULT, null);
	}

	/** Constructor - constructs an object with a Name, a Type attribute,
	    and an onClick attribute.
		@param strName Name of the button.
		@param strType Type attribute of the button.
		@param strOnClick onClick attribute of the button.
	*/
	public Button(String strName, String strType, String strOnClick)
	{ this(strName, strType, null, strOnClick); }

	/** Constructor - constructs an object with a Name, a Caption attribute.
		@param strName Name of the input element.
		@param strType Type attribute of the button.
		@param strCaption Caption attribute of the input element.
		@param strOnClick onClick attribute of the button.
	*/
	public Button(String strName, String strType, String strCaption, String strOnClick)
	{ this(strName, strType, strCaption, null, null, strOnClick); }

	/** Constructor - constructs an object with a Name, a Type attribute, a Default
	    Value attribute, a Cascading Stylesheet class name, a Cascading Stylesheet style string,
	    and an onClick attribute.
		@param strName Name of the input element.
		@param strType Type attribute of the button.
		@param strCaption Caption attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param strOnClick onClick attribute of the button.
	*/
	public Button(String strName, String strType, String strCaption,
		String strCSSClass, String strCSSStyle, String strOnClick)
	{
		super(PREFIX + strName, strType, strCaption, strCSSClass, strCSSStyle);

		m_strOnClick = strOnClick;
	}

	/******************************************************************************
	*
	*	Action methods
	*
	******************************************************************************/

	/** Action method - creates a button form element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		openTag(pWriter);

		writeAttribute(pWriter, "onClick", getOnClick());

		closeTag(pWriter);
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Mutator method - gets the Caption attribute of the button. */
	public String getCaption() { return getDefaultValue(); }

	/** Mutator method - gets the onClick attribute of the button. */
	public String getOnClick() { return m_strOnClick; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the Caption attribute of the button. */
	public void setCaption(String strNewValue) { setDefaultValue(strNewValue); }

	/** Mutator method - sets the onClick attribute of the button. */
	public void setOnClick(String strNewValue) { m_strOnClick = strNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the onClick attribute of the button. */
	private String m_strOnClick = null;
}

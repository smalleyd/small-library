package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Base class for HTML INPUT elements.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public abstract class Input extends FormElement
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - Element tag. */
	public static final String TAG = "INPUT";

	/** Constant - Attribute name for the Type attribute. */
	public static final String ATTRIBUTE_TYPE = "TYPE";
	
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name and a Type attribute.
		@param strName Name of the input element.
		@param strType Type attribute of the input element.
	*/
	public Input(String strName, String strType) { this(strName, strType, null); }

	/** Constructor - constructs an object with a Name, a Type attribute, and
	    a Default Value attribute.
		@param strName Name of the input element.
		@param strType Type attribute of the input element.
		@param strDefaultValue Default Value attribute of the input element.
	*/
	public Input(String strName, String strType, String strDefaultValue)
	{ this(strName, strType, strDefaultValue, null, null); }

	/** Constructor - constructs an object with a Name, a Type attribute,
	    a Default Value attribute, a Cascading Stylesheet class name, and
	    a Cascading Stylesheet style string.
		@param strName Name of the input element.
		@param strType Type attribute of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
	*/
	public Input(String strName, String strType, String strDefaultValue,
		String strCSSClass, String strCSSStyle)
	{
		super(strName, strDefaultValue, strCSSClass, strCSSStyle);

		m_strType = strType;
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Helper method - opens an INPUT tag. */
	protected void openTag() throws IOException
	{
		openTag(getWriter());
	}

	/** Helper method - opens an INPUT tag.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	protected void openTag(Writer pWriter) throws IOException
	{
		openTag(pWriter, getDefaultValue());
	}

	/** Helper method - opens an INPUT tag.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strValue Value attribute of the input element.
	*/
	protected void openTag(Writer pWriter, String strValue) throws IOException
	{
		super.openTag(pWriter, TAG, strValue);

		writeAttribute(pWriter, ATTRIBUTE_TYPE, getType());
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Type attribute of the input element. */
	public String getType() { return m_strType; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the Type attribute of the input element. */
	public void setType(String strNewValue) { m_strType = strNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the Type attribute of the input element. */
	private String m_strType = null;
}

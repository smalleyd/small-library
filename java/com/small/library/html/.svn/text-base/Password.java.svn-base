package com.small.library.html;

/***************************************************************************************
*
*	Class that represents the HTML password element.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 3/30/2000
*
***************************************************************************************/

public class Password extends Textbox
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - Input element's type name. */
	public static final String TYPE = "PASSWORD";

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with just a Name.
		@param strName Name of the input element.
	*/
	public Password(String strName)
	{ this(strName, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE, null); }

	/** Constructor - constructs an object with just a Name.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
	*/
	public Password(String strName, String strDefaultValue)
	{ this(strName, strDefaultValue, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE, null); }

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
		@param nSize Size attribute of the textbox.
		@param nMaxLength Max Length attribute of the textbox.
		@param strOnChange onChange attribute of the textbox.
	*/
	public Password(String strName, int nSize, int nMaxLength, String strOnChange)
	{ this(strName, null, nSize, nMaxLength, strOnChange); }

	/** Constructor - constructs an object with a Name and a Default Value attribute.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param nSize Size attribute of the textbox.
		@param nMaxLength Max Length attribute of the textbox.
		@param strOnChange onChange attribute of the textbox.
	*/
	public Password(String strName, String strDefaultValue,
		int nSize, int nMaxLength, String strOnChange)
	{ this(strName, strDefaultValue, null, null, nSize, nMaxLength, strOnChange); }

	/** Constructor - constructs an object with a Name, a Default Value attribute, a Cascading Stylesheet class name, and
	    a Cascading Stylesheet style string.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param nSize Size attribute of the textbox.
		@param nMaxLength Max Length attribute of the textbox.
		@param strOnChange onChange attribute of the textbox.
	*/
	public Password(String strName, String strDefaultValue,
		String strCSSClass, String strCSSStyle,
		int nSize, int nMaxLength, String strOnChange)
	{
		super(strName, strDefaultValue, strCSSClass, strCSSStyle,
			nSize, nMaxLength, strOnChange);

		// Password element has a different type value.
		setType(TYPE);
	}
}

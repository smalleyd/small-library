package com.small.library.html;

/***************************************************************************************
*
*	Class that represents HTML checkbox elements.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public class Checkbox extends OptionButton
{
	/******************************************************************************
	*
	*	Public constants
	*
	******************************************************************************/

	/** Constant - general use name prefix. */
	public static final String PREFIX = "chk";

	/** Constant - definition of TYPE parameter for checkbox input. */
	public static final String TYPE = "CHECKBOX";

	/** Constant - value of true for VALUE parameter. */
	public static final String VALUE_TRUE = Input.VALUE_TRUE_STRING;
	
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the option button.
	*/
	public Checkbox(String strName)
	{
		this(strName, null);
	}

	/** Constructor - constructs an object with a Name.
		@param strName Name of the option button.
		@param pList Checkbox items.
	*/
	public Checkbox(String strName, IList pList)
	{
		this(strName, null, null, pList);
	}

	/** Constructor - constructs an object with a Name.
		@param strName Name of the option button.
		@param strOnClick onClick attribute of the option button.
		@param strDelimiter Delimiter attribute of the option button.
		@param pList Checkbox items.
	*/
	public Checkbox(String strName, String strOnClick, String strDelimiter, IList pList)
	{ this(strName, null, strOnClick, strDelimiter, pList); }

	/** Constructor - constructs an object with a Name, a Default Value attribute.
		@param strName Name of the option button.
		@param strDefaultValue Default Value attribute of the input element.
		@param strOnClick onClick attribute of the option button.
		@param strDelimiter Delimiter attribute of the option button.
		@param pList Checkbox items.
	*/
	public Checkbox(String strName, String strDefaultValue,
		String strOnClick, String strDelimiter, IList pList)
	{ this(strName, strDefaultValue, null, null, strOnClick, strDelimiter, pList); }

	/** Constructor - constructs an object with a Name, a Default Value attribute,
	    a Cascading Stylesheet class name, and a Cascading Stylesheet style string.
		@param strName Name of the option button.
		@param strDefaultValue Default Value attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param strOnClick onClick attribute of the option button.
		@param strDelimiter Delimiter attribute of the option button.
		@param pList Checkbox items.
	*/
	public Checkbox(String strName, String strDefaultValue,
		String strCSSClass, String strCSSStyle,
		String strOnClick, String strDelimiter, IList pList)
	{
		super(PREFIX + strName, TYPE, strDefaultValue, strCSSClass, strCSSStyle,
			strOnClick, strDelimiter, pList);
	}
}

package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Class that represents the HTML form element text field.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public class Textbox extends Input
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - element prefix. */
	public static final String PREFIX = "txt";

	/** Constant - element type. */
	public static final String TYPE = "TEXT";

	/** Constant - attribute name of the Size attribute. */
	public static final String ATTRIBUTE_SIZE = "SIZE";

	/** Constant - attribute name of the Max Length attribute. */
	public static final String ATTRIBUTE_MAX_LENGTH = "MAXLENGTH";

	/** Constant - attribute name of the onChange attribute. */
	public static final String ATTRIBUTE_ON_CHANGE = "onChange";

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with just a Name.
		@param strName Name of the input element.
	*/
	public Textbox(String strName)
	{ this(strName, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE, null); }

	/** Constructor - constructs an object with just a Name.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
	*/
	public Textbox(String strName, String strDefaultValue)
	{ this(strName, strDefaultValue, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE, null); }

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
		@param nSize Size attribute of the textbox.
		@param nMaxLength Max Length attribute of the textbox.
		@param strOnChange onChange attribute of the textbox.
	*/
	public Textbox(String strName, int nSize, int nMaxLength, String strOnChange)
	{ this(strName, null, nSize, nMaxLength, strOnChange); }

	/** Constructor - constructs an object with a Name and a Default Value attribute.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param nSize Size attribute of the textbox.
		@param nMaxLength Max Length attribute of the textbox.
		@param strOnChange onChange attribute of the textbox.
	*/
	public Textbox(String strName, String strDefaultValue,
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
	public Textbox(String strName, String strDefaultValue,
		String strCSSClass, String strCSSStyle,
		int nSize, int nMaxLength, String strOnChange)
	{
		super(PREFIX + strName, TYPE, strDefaultValue, strCSSClass, strCSSStyle);

		m_nSize = nSize;
		m_nMaxLength = nMaxLength;
		m_strOnChange = strOnChange;
	}

	/******************************************************************************
	*
	*	Required methods: Input
	*
	******************************************************************************/

	/** Action method - creats the Textbox form element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter)
		throws IOException
	{
		create(pWriter, getDefaultValue());
	}

	/** Action method - creats the Textbox form element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strValue Value attribute of the textbox.
	*/
	public void create(Writer pWriter, String strValue)
		throws IOException
	{
		openTag(pWriter, strValue);

		writeAttribute(pWriter, ATTRIBUTE_SIZE, getSize());
		writeAttribute(pWriter, ATTRIBUTE_MAX_LENGTH, getMaxLength());
		writeAttribute(pWriter, ATTRIBUTE_ON_CHANGE, getOnChange());

		closeTag(pWriter);
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Size attribute of the textbox. */
	public int getSize() { return m_nSize; }

	/** Accessor method - gets the Max Length attribute of the textbox. */
	public int getMaxLength() { return m_nMaxLength; }

	/** Accessor method - gets the onChange attribute of the textbox. */
	public String getOnChange() { return m_strOnChange; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the Size attribute of the textbox. */
	public void setSize(int nNewValue) { m_nSize = nNewValue; }

	/** Mutator method - sets the MaxLength attribute of the textbox. */
	public void setMaxLength(int nNewValue) { m_nMaxLength = nNewValue; }

	/** Mutator method - sets the onChange attribute of the textbox. */
	public void setOnChange(String strNewValue) { m_strOnChange = strNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the Size attribute of the textbox. */
	private int m_nSize = 0;

	/** Member variable - contains the Max Length attribute of the textbox. */
	private int m_nMaxLength = 0;

	/** Member variable - contains the onChange attribute of the textbox. */
	private String m_strOnChange = null;
}

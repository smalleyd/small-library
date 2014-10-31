package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Class that represents the HTML File input element.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 4/3/2000
*
***************************************************************************************/

public class FileInput extends Input
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - general use name prefix. */
	public static final String PREFIX = "file";

	/** Constant - element type. */
	public static final String TYPE = "file";

	/** Constant - Attribute name of the Size attribute. */
	public static final String ATTRIBUTE_SIZE = "SIZE";

	/** Constant - Attribute name of the Max Length attribute. */
	public static final String ATTRIBUTE_MAX_LENGTH = "MAXLENGTH";

	/** Constant - Attribute name of the Accept attribute. */
	public static final String ATTRIBUTE_ACCEPT = "ACCEPT";

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
	*/
	public FileInput(String strName)
	{ this(strName, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE, null); }

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
		@param nSize Size attribute of the file element.
		@param nMaxLength Max Length attribute of the file element.
		@param strAccept Accept attribute of the file element.
	*/
	public FileInput(String strName, int nSize, int nMaxLength, String strAccept)
	{ this(strName, null, nSize, nMaxLength, strAccept); }

	/** Constructor - constructs an object with a Name, a Default Value attribute.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param nSize Size attribute of the file element.
		@param nMaxLength Max Length attribute of the file element.
		@param strAccept Accept attribute of the file element.
	*/
	public FileInput(String strName, String strDefaultValue,
		int nSize, int nMaxLength, String strAccept)
	{ this(strName, strDefaultValue, null, null, nSize, nMaxLength, strAccept); }

	/** Constructor - constructs an object with a Name, a Default Value attribute,
	    a Cascading Stylesheet class name, and a Cascading Stylesheet style string.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param nSize Size attribute of the file element.
		@param nMaxLength Max Length attribute of the file element.
		@param strAccept Accept attribute of the file element.
	*/
	public FileInput(String strName, String strDefaultValue,
		String strCSSClass, String strCSSStyle,
		int nSize, int nMaxLength, String strAccept)
	{
		super(PREFIX + strName, TYPE, strDefaultValue, strCSSClass, strCSSStyle);

		m_nSize = nSize;
		m_nMaxLength = nMaxLength;
		m_strAccept = strAccept;
	}

	/******************************************************************************
	*
	*	Required method: Input
	*
	******************************************************************************/

	/** Action method - creates the file input element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		openTag(pWriter);

		writeAttribute(pWriter, ATTRIBUTE_SIZE, getSize());
		writeAttribute(pWriter, ATTRIBUTE_MAX_LENGTH, getMaxLength());
		writeAttribute(pWriter, ATTRIBUTE_ACCEPT, getAccept());

		closeTag(pWriter);
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Size attribute of the file element. */
	public int getSize() { return m_nSize; }

	/** Accessor method - gets the Max Length attribute of the file element. */
	public int getMaxLength() { return m_nMaxLength; }

	/** Accessor method - gets the Accept attribute of the file element. */
	public String getAccept() { return m_strAccept; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the Size attribute of the file element. */
	public void setSize(int nNewValue) { m_nSize = nNewValue; }

	/** Mutator method - sets the Max Length attribute of the file element. */
	public void setMaxLength(int nNewValue) { m_nMaxLength = nNewValue; }

	/** Mutator method - sets the Accept attribute of the file element. */
	public void setAccept(String strNewValue) { m_strAccept = strNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the Size attribute of the file element. */
	private int m_nSize = 0;

	/** Member variable - contains the Max Length attribute of the file element. */
	private int m_nMaxLength = 0;

	/** Member variable - contains the Accept attribute of the file element. */
	private String m_strAccept = null;
}

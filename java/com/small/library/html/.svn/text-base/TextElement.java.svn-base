package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/*********************************************************************************
*
*	Class that represents HTML text content. The class can be derived to
*	produce more elaborate implementations.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/29/2001
*
*********************************************************************************/

public class TextElement extends Element
{
	/*************************************************************************
	*
	*	Constructors
	*
	*************************************************************************/

	/** Constructor - constructs an empty text element. */
	public TextElement() { super(); }

	/** Constructor - constructs a populated text element.
		@param strValue Text value to be written to the page.
	*/
	public TextElement(String strValue)
	{
		super();

		m_strValue = strValue;
	}

	/*************************************************************************
	*
	*	Required methods: Element
	*
	*************************************************************************/

	/** Action method - creates the HTML text element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		create(pWriter, getValue());
	}

	/** Action method - creates the HTML text element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strValue Text value to be written to the page.
	*/
	public static void create(Writer pWriter, String strValue) throws IOException
	{
		write(pWriter, strValue);
	}

	/*************************************************************************
	*
	*	Accessor methods
	*
	*************************************************************************/

	/** Accessor method - gets the text value. */
	public String getValue() { return m_strValue; }

	/*************************************************************************
	*
	*	Mutator methods
	*
	*************************************************************************/

	/** Mutator method - sets the text value. */
	public void setValue(String strNewValue) { m_strValue = strNewValue; }

	/*************************************************************************
	*
	*	Member variables
	*
	*************************************************************************/

	/** Member variable - contains the text value. */
	private String m_strValue = null;
}

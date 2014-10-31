package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Class that represents an HTML hidden field element.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 7/26/2000
*
***************************************************************************************/

public class HiddenField extends Input
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - general use name prefix. */
	public static final String PREFIX = "hdn";

	/** Constant - form element type name. */
	public static final String TYPE = "HIDDEN";

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
	*/
	public HiddenField(String strName) { this(strName, null); }

	/** Constructor - constructs an object with a Name, a Default Value attribute.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
	*/
	public HiddenField(String strName, String strDefaultValue)
	{
		super(PREFIX + strName, TYPE, strDefaultValue);
	}

	/******************************************************************************
	*
	*	Required methods: Input
	*
	******************************************************************************/

	/** Action method - creates a hidden field element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		create(pWriter, getDefaultValue());
	}

	/** Action method - creates a hidden field element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strValue Value attribute of the hidden field element.
	*/
	public void create(Writer pWriter, String strValue) throws IOException
	{
		openTag(pWriter, strValue);
		closeTag(pWriter);
	}
}

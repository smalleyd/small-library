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
	private final String value;

	public TextElement(final String value)
	{
		super();

		this.value = value;
	}

	/** Action method - creates the HTML text element.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public void create(final Writer writer) throws IOException
	{
		create(writer, value);
	}

	/** Action method - creates the HTML text element.
		@param writer <I>Writer</I> object used to output HTML.
		@param value Text value to be written to the page.
	*/
	public static void create(final Writer writer, final String value) throws IOException
	{
		write(writer, value);
	}
}

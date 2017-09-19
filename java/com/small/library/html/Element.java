package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/**********************************************************************************
*
*	Base class that represents HTML elements. HTML elements must implement
*	a single <CODE>create</CODE> method.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/24/2001
*
**********************************************************************************/

public abstract class Element
{
	protected final Writer writer;

	public Element()
	{
		this(null);
	}

	public Element(final Writer writer)
	{
		this.writer = writer;
	}

	/** Abstract method - creates the input element through the <I>PrintWriter</I>
	    output stream.
		@param writer <I>Writer</I> object used to output HTML.
	*/
	public abstract void create(Writer writer) throws IOException;

	/** Helper method - creates the input element through the member
	    <I>Writer</I> stream.
	*/
	public void create() throws IOException { create(writer); }

	/** Helper method -  Writes string information to the Output stream. */
	protected void write(String value) throws IOException { write(writer, value); }

	/** Helper method -  Writes string information to the Output stream. */
	protected static void write(Writer writer, String value) throws IOException { writer.write(value); }

	/** Helper method - Writes integer information to the Output stream. */
	protected void write(int value) throws IOException { write(writer, value); }

	/** Helper method - Writes integer information to the Output stream. */
	protected static void write(Writer writer, int value) throws IOException { write(writer, "" + value); }

	/** Helper method - Writes string information to the Output stream with a carridge return. */
	protected void writeLine(String value) throws IOException { writeLine(writer, value); }

	/** Helper method - Writes string information to the Output stream with a carridge return. */
	protected static void writeLine(Writer writer, String value) throws IOException
	{ writer.write(value); writeNewLine(writer); }

	/** Helper method - Writes integer information to the Output stream with a carridge return. */
	protected void writeLine(int value) throws IOException { writeLine(writer, value); }

	/** Helper method - Writes integer information to the Output stream with a carridge return. */
	protected static void writeLine(Writer writer, int value) throws IOException { writeLine(writer, "" + value); }

	/** Helper method - Places double quotes around a string value. */
	protected void writeWithQuotes(String value) throws IOException
	{ writeWithQuotes(writer, value); }

	/** Helper method - Places double quotes around a string value. */
	protected static void writeWithQuotes(Writer writer, String value) throws IOException
	{ write(writer, "\""); write(writer, value); write(writer, "\""); }

	/** Helper method - Places double quotes around an integer value. */
	protected void writeWithQuotes(int value) throws IOException
	{ writeWithQuotes(writer, value); }

	/** Helper method - Places double quotes around an integer value. */
	protected static void writeWithQuotes(Writer writer, int value) throws IOException
	{ writeWithQuotes(writer, "" + value); }

	/** Helper method - writes a line break in the HTML. */
	protected void writeNewLine() throws IOException { writeNewLine(writer); }

	/** Helper method - writes a line break in the HTML. */
	protected static void writeNewLine(Writer writer) throws IOException { writer.write("\n"); }
}

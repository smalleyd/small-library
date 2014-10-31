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
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an empty object. */
	public Element() {}

	/** Constructor - constructs an object with an output <I>Writer</I>.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public Element(Writer pWriter)
	{
		m_Writer = pWriter;
	}

	/******************************************************************************
	*
	*	Abstract methods
	*
	******************************************************************************/

	/** Abstract method - creates the input element through the <I>PrintWriter</I>
	    output stream.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public abstract void create(Writer pWriter) throws IOException;

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Helper method - creates the input element through the member
	    <I>Writer</I> stream.
	*/
	public void create() throws IOException { create(getWriter()); }

	/******************************************************************************
	*
	*	Helper methods - Output methods
	*
	******************************************************************************/

	/** Helper method -  Writes string information to the Output stream. */
	protected void write(String strValue) throws IOException { write(m_Writer, strValue); }

	/** Helper method -  Writes string information to the Output stream. */
	protected static void write(Writer pWriter, String strValue) throws IOException { pWriter.write(strValue); }

	/** Helper method - Writes integer information to the Output stream. */
	protected void write(int nValue) throws IOException { write(m_Writer, nValue); }

	/** Helper method - Writes integer information to the Output stream. */
	protected static void write(Writer pWriter, int nValue) throws IOException { write(pWriter, "" + nValue); }

	/** Helper method - Writes string information to the Output stream with a carridge return. */
	protected void writeLine(String strValue) throws IOException { writeLine(m_Writer, strValue); }

	/** Helper method - Writes string information to the Output stream with a carridge return. */
	protected static void writeLine(Writer pWriter, String strValue) throws IOException
	{ pWriter.write(strValue); writeNewLine(pWriter); }

	/** Helper method - Writes integer information to the Output stream with a carridge return. */
	protected void writeLine(int nValue) throws IOException { writeLine(m_Writer, nValue); }

	/** Helper method - Writes integer information to the Output stream with a carridge return. */
	protected static void writeLine(Writer pWriter, int nValue) throws IOException { writeLine(pWriter, "" + nValue); }

	/** Helper method - Places double quotes around a string value. */
	protected void writeWithQuotes(String strValue) throws IOException
	{ writeWithQuotes(m_Writer, strValue); }

	/** Helper method - Places double quotes around a string value. */
	protected static void writeWithQuotes(Writer pWriter, String strValue) throws IOException
	{ write(pWriter, "\""); write(pWriter, strValue); write(pWriter, "\""); }

	/** Helper method - Places double quotes around an integer value. */
	protected void writeWithQuotes(int nValue) throws IOException
	{ writeWithQuotes(m_Writer, nValue); }

	/** Helper method - Places double quotes around an integer value. */
	protected static void writeWithQuotes(Writer pWriter, int nValue) throws IOException
	{ writeWithQuotes(pWriter, "" + nValue); }

	/** Helper method - writes a line break in the HTML. */
	protected void writeNewLine() throws IOException { writeNewLine(m_Writer); }

	/** Helper method - writes a line break in the HTML. */
	protected static void writeNewLine(Writer pWriter) throws IOException { pWriter.write("\n"); }

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets a reference to the <I>Writer</I> object used to output
	    the HTML.
	*/
	public Writer getWriter() { return m_Writer; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets a reference to the <I>Writer</I> object used to output
	    the HTML.
	*/
	public void setWriter(Writer pNewValue) { m_Writer = pNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the <I>Writer</I> object used to output
	    the HTML.
	*/
	private Writer m_Writer = null;
}

package com.small.library.html;

/*********************************************************************************
*
*	Class that represents an HTML element attribute. The class structure
*	consists of a Name property and a Value property. The <CODE>toString</CODE>
*	method returns the HTML output. The <CODE>toStringBuffer</CODE> method
*	returns the HTML output within a <I>StringBuffer</I> object.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/29/2001
*
*********************************************************************************/

public class Attribute
{
	/** Constant - error message for a <CODE>null</CODE> Name property. */
	public static final String MSG_ERROR_NULL_NAME = "The \"Name\" property " +
		"of the " + Attribute.class.getName() + " object cannot be null.";

	/*************************************************************************
	*
	*	Constructors
	*
	*************************************************************************/

	/** Constructor - constructs a populated object.
		@param strName Name property of the attribute.
		@param strValue Value property of the attribute.
	*/
	public Attribute(String strName, String strValue)
		throws HtmlException
	{
		if (null == strName)
			throw new HtmlException(MSG_ERROR_NULL_NAME);

		m_strName = strName;
		m_strValue = strValue;
	}

	/*************************************************************************
	*
	*	Output methods
	*
	*************************************************************************/

	/** Output method - outputs the <I>String</I> representation of the
	    attribute.
	*/
	public String toString()
	{
		return toStringBuffer().toString();
	}

	/** Output method - outputs the <I>StringBuffer</I> representation of the
	    attribute.
	*/
	public StringBuffer toStringBuffer()
	{
		if (null != m_strOutput)
			return m_strOutput;

		if (null == m_strValue)
			return (m_strOutput = new StringBuffer(""));

		m_strOutput = new StringBuffer(" ");

		m_strOutput.append(m_strName);
		m_strOutput.append("=\"");
		m_strOutput.append(m_strValue);
		m_strOutput.append("\"");

		return m_strOutput;
	}

	/*************************************************************************
	*
	*	Accessor methods
	*
	*************************************************************************/

	/** Accessor method - gets the Name property. */
	public String getName() { return m_strName; }

	/** Accessor method - gets the Value property. */
	public String getValue() { return m_strValue; }

	/*************************************************************************
	*
	*	Mutator methods
	*
	*************************************************************************/

	/** Mutator method - sets the Name property. */
	public void setName(String strNewValue) { m_strName = strNewValue; reset(); }

	/** Mutator method - sets the Value property. */
	public void setValue(String strNewValue) { m_strValue = strNewValue; reset(); }

	/** Mutator method - resets the Output property. */
	private void reset() { m_strOutput = null; }

	/*************************************************************************
	*
	*	Member variables
	*
	*************************************************************************/

	/** Member variable - contains the Name property. */
	private String m_strName = null;

	/** Member variable - contains the Value property. */
	private String m_strValue = null;

	/** Member variable - contains the concatenation of the Name and Value
	    properties.
	*/
	private StringBuffer m_strOutput = null;
}

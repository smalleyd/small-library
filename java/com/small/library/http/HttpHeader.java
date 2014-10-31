package com.small.library.http;

/*********************************************************************************
*
*	Simple data structure for HTTP headers. It combines the header name
*	and the header value into a single structure for easy parameter passing.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 5/22/2001
*
*********************************************************************************/

public class HttpHeader
{
	/************************************************************************
	*
	*	Constructors
	*
	************************************************************************/

	/** Constructor - accepts the header name and header value.
		@param strName The header name.
		@param strValue The header value.
	*/
	public HttpHeader(String strName, String strValue)
		throws NullPointerException
	{
		setName(strName);
		setValue(strValue);
	}

	/** Constructor - clone constructor.
		@param pClone <I>HttpHeader</I> object to clone.
	*/
	public HttpHeader(HttpHeader pClone)
	{
		m_strName = pClone.m_strName;
		m_strValue = pClone.m_strValue;
	}

	/************************************************************************
	*
	*	Accessor methods
	*
	************************************************************************/

	/** Accessor method - gets the header name. */
	public String getName() { return m_strName; }

	/** Accessor method - gets the header value. */
	public String getValue() { return m_strValue; }

	/** Accessor method - concats the header name and value. */
	public String toHeader() { return m_strName + ": \"" + m_strValue + "\""; }

	/** Implementation of the base <CODE>toString</CODE> method. */
	public String toString() { return toHeader(); }

	/** Implementation of the base <CODE>hashCode</CODE> method. */
	public int hashCode() { return toString().hashCode(); }

	/** Implementation of the base <CODE>clone</CODE> method. */
	public Object clone() { return new HttpHeader(this); }

	/************************************************************************
	*
	*	Mutator methods
	*
	************************************************************************/

	/** Mutator method - sets the header name.
		@throw NullPointerException Thrown when a <CODE>null</CODE>
			value is provided.
	*/
	public void setName(String strNewValue)
		throws NullPointerException
	{
		if (null == strNewValue)
			throw new NullPointerException("HttpHeader: A header name has not been supplied");

		m_strName = strNewValue;
	}

	/** Mutator method - sets the header value. */
	public void setValue(String strNewValue)
	{
		m_strValue = strNewValue;
	}

	/************************************************************************
	*
	*	Member variables
	*
	************************************************************************/

	/** Member variable - contains the header name. */
	private String m_strName = null;

	/** Member variable - contains the header value. */
	private String m_strValue = null;
}

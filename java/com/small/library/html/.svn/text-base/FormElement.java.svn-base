package com.small.library.html;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/***************************************************************************************
*
*	Base class for HTML form input elements.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/26/2001
*
***************************************************************************************/

public abstract class FormElement extends TagElement
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - Attribute name for the Default Value attribute. */
	public static final String ATTRIBUTE_DEFAULT_VALUE = "VALUE";

	/** Constant - True integer value. */
	public static final String VALUE_TRUE_INT = "1";

	/** Constant - True string value. */
	public static final String VALUE_TRUE_STRING = "true";
	
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
	*/
	public FormElement(String strName) { this(strName, null); }

	/** Constructor - constructs an object with a Name and
	    a Default Value attribute.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
	*/
	public FormElement(String strName, String strDefaultValue)
	{ this(strName, strDefaultValue, null, null); }

	/** Constructor - constructs an object with a Name,
	    a Default Value attribute, a Cascading Stylesheet class name, and
	    a Cascading Stylesheet style string.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
	*/
	public FormElement(String strName, String strDefaultValue,
		String strCSSClass, String strCSSStyle)
	{
		super(strName, strCSSClass, strCSSStyle);

		m_strDefaultValue = strDefaultValue;
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Helper method - opens an HTML element with the specified name.
		@param strElementName Name of the element appearing in the tag.
	*/
	protected void openTag(String strElementName) throws IOException
	{
		openTag(getWriter(), strElementName);
	}

	/** Helper method - opens an HTML element with the specified name.
		@param strElementName Name of the element appearing in the tag.
		@param strValue Value attribute of the form element.
	*/
	protected void openTag(String strElementName,
		String strValue) throws IOException
	{
		openTag(getWriter(), strElementName, strValue);
	}

	/** Helper method - opens an HTML element with the specified name.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strElementName Name of the element appearing in the tag.
	*/
	protected void openTag(Writer pWriter, String strElementName)
		throws IOException
	{
		openTag(pWriter, strElementName, getDefaultValue());
	}

	/** Helper method - opens an HTML element with the specified name.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strElementName Name of the element appearing in the tag.
		@param strValue Value attribute of the form element.
	*/
	protected void openTag(Writer pWriter, String strElementName,
		String strValue) throws IOException
	{
		super.openTag(pWriter, strElementName);

		writeAttribute(pWriter, ATTRIBUTE_DEFAULT_VALUE, strValue);
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Default Value attribute of the input element. */
	public String getDefaultValue() { return m_strDefaultValue; }

	/** Accessor method - gets the <I>HttpServletRequest</I> object. */
	public HttpServletRequest getRequest() { return m_Request; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the Default Value attribute of the input element. */
	public void setDefaultValue(String strNewValue) { m_strDefaultValue = strNewValue; }

	/** Mutator method - sets the <I>HttpServletRequest</I> object. */
	public void setRequest(HttpServletRequest pNewValue) { m_Request = pNewValue; }

	/******************************************************************************
	*
	*	HTTP Request Helper methods
	*
	******************************************************************************/

	/** HTTP Request Helper method - gets a boolean value from an HTTP request. */
	public boolean getBoolean()
	{ return getBoolean(m_Request); }

	/** HTTP Request Helper method - gets a boolean value from an HTTP request.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public boolean getBoolean(HttpServletRequest pRequest)
	{
		String strValue = getString(pRequest);

		return ((null != strValue) &&
			(VALUE_TRUE_INT.equals(strValue) ||
			VALUE_TRUE_STRING.equalsIgnoreCase(strValue))) ? true : false;
	}

	/** HTTP Request Helper method - gets a double data type from an HTTP request. */
	public double getDouble()
		throws NumberFormatException, NullPointerException
	{ return getDouble(m_Request); }

	/** HTTP Request Helper method - gets a double data type from an HTTP request.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public double getDouble(HttpServletRequest pRequest)
		throws NumberFormatException, NullPointerException
	{
		return Double.parseDouble(getNonNullString(pRequest));
	}

	/** HTTP Request Helper method - gets a float data type from an HTTP request. */
	public float getFloat()
		throws NumberFormatException, NullPointerException
	{ return getFloat(m_Request); }

	/** HTTP Request Helper method - gets a float data type from an HTTP request.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public float getFloat(HttpServletRequest pRequest)
		throws NumberFormatException, NullPointerException
	{
		return Float.parseFloat(getNonNullString(pRequest));
	}

	/** HTTP Request Helper method - gets an integer data type from an HTTP request. */
	public int getInt()
		throws NumberFormatException, NullPointerException
	{ return getInt(m_Request); }

	/** HTTP Request Helper method - gets an integer data type from an HTTP request.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public int getInt(HttpServletRequest pRequest)
		throws NumberFormatException, NullPointerException
	{
		return Integer.parseInt(getNonNullString(pRequest));
	}

	/** HTTP Request Helper method - gets a short data type values from an HTTP request. */
	public short getShort()
		throws NumberFormatException, NullPointerException
	{ return getShort(m_Request); }

	/** HTTP Request Helper method - gets a short data type values from an HTTP request.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public short getShort(HttpServletRequest pRequest)
		throws NumberFormatException, NullPointerException
	{
		return Short.parseShort(getNonNullString(pRequest));
	}

	/** HTTP Request Helper method - gets a single <I>String</I> value
	    from an HTTP request if non-null and not empty. If null,
	    it throws a <I>NullPointerException</I>.
	*/
	public String getNonNullString()
		throws NullPointerException
	{ return getNonNullString(m_Request); }

	/** HTTP Request Helper method - gets a single <I>String</I> value
	    from an HTTP request if non-null and not empty. If null,
	    it throws a <I>NullPointerException</I>.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public String getNonNullString(HttpServletRequest pRequest)
		throws NullPointerException
	{
		String strValue = getString(pRequest);

		if ((null == strValue) || (0 == strValue.length()))
			throw new NullPointerException("Could not find value for request parameter, \"" + getName() + "\".");

		return strValue;
	}

	/** HTTP Request Helper method - gets a single string from an HTTP request. */
	public String getString()
		throws NullPointerException
	{
		if (null == m_Request)
			throw new NullPointerException("Request object does not exist.");

		return getString(m_Request);
	}

	/** HTTP Request Helper method - gets a single string from an HTTP request.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public String getString(HttpServletRequest pRequest)
		throws NullPointerException
	{
		return pRequest.getParameter(getName());
	}

	/** HTTP Request Helper method - gets an array of string values.
		@return Returns the strings in a java.util.ArrayList object. If
			no values exist, the method returns <I>null</I>.
	*/
	public ArrayList getStrings()
		throws NullPointerException
	{
		if (null == m_Request)
			throw new NullPointerException("Request object does not exist.");

		return getStrings(m_Request);
	}

	/** HTTP Request Helper method - gets an array of string values.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
		@return Returns the strings in a java.util.ArrayList object. If
			no values exist, the method returns <I>null</I>.
	*/
	public ArrayList getStrings(HttpServletRequest pRequest)
		throws NullPointerException
	{
		String[] strValues = pRequest.getParameterValues(getName());

		if (null == strValues)
			return null;

		ArrayList pList = new ArrayList(strValues.length);

		for (int i = 0; i < strValues.length; i++)
			pList.add(strValues[i]);

		return pList;
	}

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the Default Value attribute of the input element. */
	private String m_strDefaultValue = null;

	/** Member variable - contains the <I>HttpServletRequest</I> object. */
	private HttpServletRequest m_Request = null;
}

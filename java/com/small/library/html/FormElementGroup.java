package com.small.library.html;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/***************************************************************************************
*
*	Base class for a group of HTML form input elements that work together to
*	a specific piece of functionality. Examples are date controls and mask edit
*	fields.
*
*	<BR><BR>
*
*	This class merely overrides the input retrieval methods (getBoolean, getInt,
*	...) with empty stub methods. Derived classes may choose to override these
*	with return values appropriate for the group of elements.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/26/2001
*
***************************************************************************************/

public abstract class FormElementGroup extends FormElement
{
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
	*/
	public FormElementGroup(String strName) { this(strName, null); }

	/** Constructor - constructs an object with a Name and
	    a Default Value attribute.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
	*/
	public FormElementGroup(String strName, String strDefaultValue)
	{ this(strName, strDefaultValue, null, null); }

	/** Constructor - constructs an object with a Name,
	    a Default Value attribute, a Cascading Stylesheet class name, and
	    a Cascading Stylesheet style string.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
	*/
	public FormElementGroup(String strName, String strDefaultValue,
		String strCSSClass, String strCSSStyle)
	{
		super(strName, strDefaultValue, strCSSClass, strCSSStyle);
	}

	/******************************************************************************
	*
	*	HTTP Request Helper methods
	*
	******************************************************************************/

	/** HTTP Request Helper method - gets a boolean value from an HTTP request.
	    Stub method that returns <CODE>null</CODE>.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public boolean getBoolean(HttpServletRequest pRequest)
	{
		return false;
	}

	/** HTTP Request Helper method - gets a double data type from an HTTP request.
	    Stub method that returns <CODE>null</CODE>.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public double getDouble(HttpServletRequest pRequest)
		throws NumberFormatException, NullPointerException
	{
		return (double) 0;
	}

	/** HTTP Request Helper method - gets a float data type from an HTTP request.
	    Stub method that returns <CODE>null</CODE>.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public float getFloat(HttpServletRequest pRequest)
		throws NumberFormatException, NullPointerException
	{
		return (float) 0;
	}

	/** HTTP Request Helper method - gets an integer data type from an HTTP request.
	    Stub method that returns <CODE>null</CODE>.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public int getInt(HttpServletRequest pRequest)
		throws NumberFormatException, NullPointerException
	{
		return 0;
	}

	/** HTTP Request Helper method - gets a short data type values from an HTTP request.
	    Stub method that returns <CODE>null</CODE>.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public short getShort(HttpServletRequest pRequest)
		throws NumberFormatException, NullPointerException
	{
		return (short) 0;
	}

	/** HTTP Request Helper method - gets a single string from an HTTP request.
	    Stub method that returns <CODE>null</CODE>.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public String getString(HttpServletRequest pRequest)
		throws NullPointerException
	{
		return null;
	}

	/** HTTP Request Helper method - gets an array of string values.
	    Stub method that returns <CODE>null</CODE>.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
		@return Returns the strings in a java.util.ArrayList object. If
			no values exist, the method returns <I>null</I>.
	*/
	public ArrayList getStrings(HttpServletRequest pRequest)
		throws NullPointerException
	{
		return null;
	}
}

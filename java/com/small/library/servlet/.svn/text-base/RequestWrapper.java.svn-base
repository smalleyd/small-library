package com.small.library.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletRequest;

import com.small.library.biz.FieldValidationException;
import com.small.library.taglib.DateControlTag;

/**********************************************************************************
*
*	Wrapper class for the <I>ServletRequest</I> object. This class simplies
*	conversion of request parameters of <I>String</I> to specific types.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/19/2002
*
**********************************************************************************/

public class RequestWrapper
{
	/**************************************************************************
	*
	*	Static members
	*
	**************************************************************************/

	/** Constant - short date format. */
	public static final String SHORT_DATE_FORMAT =
		com.small.library.taglib.InputTag.SHORT_DATE_FORMAT;

	/** Static member - short date formatter. */
	public static final SimpleDateFormat shortDateFormatter =
		com.small.library.taglib.InputTag.shortDateFormatter;

	/**************************************************************************
	*
	*	Constructors
	*
	**************************************************************************/

	/** Constructor - constructs a populated object.
		@param request Reference to an active <I>ServletRequest</I>
			object.
	*/
	public RequestWrapper(ServletRequest request)
	{
		m_Request = request;
	}

	/**************************************************************************
	*
	*	Parameter retrieval methods
	*
	**************************************************************************/

	/** Retrieval method - gets a request parameter <I>String</I> value.
	    Converts empty strings to <CODE>null</CODE>.
	*/
	public String getParameterString(String name)
	{
		String strReturn = m_Request.getParameter(name);

		if ((null == strReturn) || (0 == strReturn.length()))
			return null;

		return strReturn;
	}

	/** Retrieval method - gets a request parameter <I>int</I> value.
	    Converts empty or null strings to zero.
	*/
	public int getParameterInt(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return Integer.MIN_VALUE;

		try { return Integer.parseInt(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Integer"));
		}
	}

	/** Retrieval method - gets a request parameter <I>Integer</I> value.
	    Convert empty strings to null.
	*/
	public Integer getParameterInteger(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return null;

		try { return new Integer(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Integer"));
		}
	}

	/** Retrieval method - gets a request parameter <I>long</I> value.
	    Converts empty or null strings to zero.
	*/
	public long getParameterLong(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return Long.MIN_VALUE;

		try { return Long.parseLong(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Long Integer"));
		}
	}

	/** Retrieval method - gets a request parameter <I>Long</I> value.
	    Convert empty strings to null.
	*/
	public Long getParameterLongObject(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return null;

		try { return new Long(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Long Integer"));
		}
	}

	/** Retrieval method - gets a request parameter <I>short</I> value.
	    Converts empty or null strings to zero.
	*/
	public short getParameterShort(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return Short.MIN_VALUE;

		try { return Short.parseShort(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Short Integer"));
		}
	}

	/** Retrieval method - gets a request parameter <I>Short</I> value.
	    Convert empty strings to null.
	*/
	public Short getParameterShortObject(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return null;

		try { return new Short(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Short Integer"));
		}
	}

	/** Retrieval method - gets a request parameter <I>byte</I> value.
	    Converts empty or null strings to zero.
	*/
	public byte getParameterByte(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return Byte.MIN_VALUE;

		try { return Byte.parseByte(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Short Integer"));
		}
	}

	/** Retrieval method - gets a request parameter <I>double</I> value.
	    Converts empty or null strings to zero.
	*/
	public double getParameterDouble(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return Double.NaN;

		try { return Double.parseDouble(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Decimal Number"));
		}
	}

	/** Retrieval method - gets a request parameter <I>Double</I> value.
	    Convert empty strings to null.
	*/
	public Double getParameterDoubleObject(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return null;

		try { return new Double(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Decimal Number"));
		}
	}

	/** Retrieval method - gets a request parameter <I>float</I> value.
	    Converts empty or null strings to zero.
	*/
	public float getParameterFloat(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return Float.NaN;

		try { return Float.parseFloat(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Decimal Number"));
		}
	}

	/** Retrieval method - gets a request parameter <I>Float</I> value.
	    Convert empty strings to null.
	*/
	public Float getParameterFloatObject(String name)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return null;

		try { return new Float(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(name, name, value, getReason(name, value, "Decimal Number"));
		}
	}

	/** Retrieval method - gets a request parameter <I>boolean</I> value.
	    Converts empty or null strings to zero.
	*/
	public boolean getParameterBoolean(String name)
	{
		return (null != getParameterString(name)) ? true : false;
	}

	/** Retrieval method - gets a request parameter <I>Date</I> value
	    from a DateControl.
	*/
	public Date getParameterDate(String name)
	{
		return DateControlTag.getValue(name, (javax.servlet.http.HttpServletRequest) m_Request);
	}

	/** Retrieval method - gets a request parameter <I>Date</I> value
		from a textbox with the AMC date format.
			@param name field name.
	*/
	public Date getParameterSimpleDate(String name)
		throws FieldValidationException
	{
		return getParameterSimpleDate(name, name);
	}

	/** Retrieval method - gets a request parameter <I>Date</I> value
	    from a textbox with the AMC date format.
			@param name field name.
			@param caption field caption.
	*/
	public Date getParameterSimpleDate(String name, String caption)
		throws FieldValidationException
	{
		String value = getParameterString(name);

		if (null == value)
			return null;

		try { return shortDateFormatter.parse(value); }
		catch (ParseException ex)
		{
			throw new FieldValidationException(name, caption, value, "it is not a valid date. Should be of format " +
				SHORT_DATE_FORMAT);
		}
	}

	/**************************************************************************
	*
	*	Parameter retrieval methods - arrays
	*
	**************************************************************************/

	/** Retrieval method - gets a request parameter <I>String</I> array. */
	public String[] getParameterStrings(String name)
	{
		return m_Request.getParameterValues(name);
	}

	/** Retrieval method - gets a request parameter <I>int</I> array. When converting
	    each array <I>String</I> to an <I>int</I>, throws out number format
	    exceptions and continues processing.
	*/
	public int[] getParameterInts(String name)
	{
		String[] ids = getParameterStrings(name);

		if ((null == ids) || (0 == ids.length))
			return null;

		ArrayList list = new ArrayList(ids.length);

		for (int i = 0; i < ids.length; i++)
		{
			try { list.add(new Integer(ids[i])); }
			catch (NumberFormatException ex) {}
		}

		if (0 == list.size())
			return null;

		int[] returnIds = new int[list.size()];

		for (int i = 0; i < returnIds.length; i++)
			returnIds[i] = ((Integer) list.get(i)).intValue();

		return returnIds;
	}

	/**************************************************************************
	*
	*	Parameter retrieval methods - complex
	*
	**************************************************************************/

	/** Retrieval method - concats request parameter values from a
	    single request parameter name. Useful for mask edit controls
	    that use multiple edit controls with the same name.
	*/
	public String concatParameterStrings(String name)
	{
		String[] values = getParameterStrings(name);

		if (null == values)
			return null;

		String strReturn = "";

		for (int i = 0; i < values.length; i++)
			strReturn+= values[i];

		return strReturn;
	}

	/**************************************************************************
	*
	*	Helper methods
	*
	**************************************************************************/

	/** Helper method - gets the <I>FieldValidationException</I> reason based on the
	    supplied parameters.
		@param name Name of the field who's value is being requested.
		@param value Value of the field.
		@param dataType Data type trying to convert to.
	*/
	private static String getReason(String name, String value,
		String dataType)
	{
		// Get reason short, as the FieldValidationException, puts together a longer message
		// with this reason as part of it.
		return "could not convert to \"" + dataType + "\"";
	}

	/**************************************************************************
	*
	*	Accessor methods
	*
	**************************************************************************/

	/** Accessor method - gets the wrapped <I>ServletRequest</I> object. */
	public ServletRequest getServletRequest() { return m_Request; }

	/**************************************************************************
	*
	*	Member variables
	*
	**************************************************************************/

	/** Member variable - reference to an active <I>ServletRequest</I> object. */
	private ServletRequest m_Request = null;
}

package com.small.library.atg.repository;

import atg.repository.*;

import java.sql.Timestamp;
import java.util.*;

/************************************************************************************
*
*	Base class for repository handler classes. Supplies methods for extracting
*	properly casted property values from a Repository Item. Subclasses
*	can create entity specific value objects.
*
*	<BR><BR>
*
*	The repository handler can process requests from any number of simultaneous
*	callers as the it does maintain any state.
*
*	@author AMC\David Small
*	@version 1.0.0.0
*	@date 7/21/2002
*
************************************************************************************/

public class RepositoryHandler
{
	/**************************************************************************
	*
	*	Constructors
	*
	**************************************************************************/

	/** Constructor - constructs an empty object. */
	public RepositoryHandler() {}

	/**************************************************************************
	*
	*	Conversion methods
	*
	**************************************************************************/

	/** Conversion method - converts a Repository Item property of type
	    <I>Integer</I> into an <I>int</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return an <I>int</I> value if found otherwise <CODE>Integer.MIN_VALUE</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public int getPropertyInt(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Integer pValue = getPropertyIntegerObject(repositoryItem, strPropertyName);

		if (null == pValue)
			return Integer.MIN_VALUE;

		return pValue.intValue();
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Short</I> into a <I>short</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>short</I> value if found otherwise <CODE>Short.MIN_VALUE</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public short getPropertyShort(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Short pValue = getPropertyShortObject(repositoryItem, strPropertyName);

		if (null == pValue)
			return Short.MIN_VALUE;

		return pValue.shortValue();
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Long</I> into a <I>long</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>long</I> value if found otherwise <CODE>Long.MIN_VALUE</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public long getPropertyLong(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Long pValue = getPropertyLongObject(repositoryItem, strPropertyName);

		if (null == pValue)
			return Long.MIN_VALUE;

		return pValue.longValue();
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Byte</I> into a <I>byte</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>byte</I> value if found otherwise <CODE>Byte.MIN_VALUE</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public byte getPropertyByte(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Byte pValue = getPropertyByteObject(repositoryItem, strPropertyName);

		if (null == pValue)
			return Byte.MIN_VALUE;

		return pValue.byteValue();
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Boolean</I> into a <I>boolean</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>boolean</I> value if found otherwise <CODE>false</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public boolean getPropertyBoolean(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Boolean pValue = getPropertyBooleanObject(repositoryItem, strPropertyName);

		if (null == pValue)
			return false;

		return pValue.booleanValue();
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Float</I> into a <I>float</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>float</I> value if found otherwise <CODE>Float.NaN</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public float getPropertyFloat(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Float pValue = getPropertyFloatObject(repositoryItem, strPropertyName);

		if (null == pValue)
			return Float.NaN;

		return pValue.floatValue();
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Double</I> into a <I>double</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>double</I> value if found otherwise <CODE>Double.NaN</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public double getPropertyDouble(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Double pValue = getPropertyDoubleObject(repositoryItem, strPropertyName);

		if (null == pValue)
			return Double.NaN;

		return pValue.doubleValue();
	}

	/**************************************************************************
	*
	*	Accessor methods
	*
	**************************************************************************/

	/** Accessor method - gets a Repository Item property of type
	    <I>Integer</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return an <I>Integer</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Integer getPropertyIntegerObject(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Integer)
			return (Integer) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Integer.",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Short</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Short</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Short getPropertyShortObject(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Short)
			return (Short) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Short.",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Long</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Long</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Long getPropertyLongObject(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Long)
			return (Long) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Long.",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Byte</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Byte</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Byte getPropertyByteObject(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Byte)
			return (Byte) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Byte.",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Boolean</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Boolean</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Boolean getPropertyBooleanObject(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Boolean)
			return (Boolean) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Boolean.",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Float</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Float</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Float getPropertyFloatObject(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Float)
			return (Float) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Float.",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Double</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Double</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Double getPropertyDoubleObject(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Double)
			return (Double) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Double.",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Date</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Date</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Date getPropertyDate(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Date)
			return (Date) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Date.",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>byte</I> array.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>byte</I> array if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public byte[] getPropertyBinary(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof byte[])
			return (byte[]) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type byte array (byte[]).",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>String</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>String</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public String getPropertyString(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof String)
			return (String) pValue;

		return pValue.toString();
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Timestamp</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Timestamp</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Timestamp getPropertyTimestamp(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Timestamp)
			return (Timestamp) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Timestamp (java.util.Timestamp).",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Object[]</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return an array of objects if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Object[] getPropertyArray(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Object[])
			return (Object[]) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not an array of Objects (Object[]).",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Set</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Set</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Set getPropertySet(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Set)
			return (Set) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Set (java.util.Set).",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>List</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>List</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public List getPropertyList(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof List)
			return (List) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type List (java.util.List).",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Map</I>.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Map</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Map getPropertyMap(RepositoryItem repositoryItem, String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		Object pValue = repositoryItem.getPropertyValue(strPropertyName);

		if (null == pValue)
			return null;

		if (pValue instanceof Map)
			return (Map) pValue;

		throw new RepositoryPropertyConversionException("Property, " +
			strPropertyName + ", is not of type Map (java.util.Map).",
			repositoryItem, strPropertyName);
	}

	/** Accessor method - gets Repository Item property without definition.
		@param repositoryItem Repository Item to extract the property value from.
		@param strPropertyName Name of the Repository Item property value.
	*/
	public Object getPropertyObject(RepositoryItem repositoryItem, String strPropertyName)
	{
		return repositoryItem.getPropertyValue(strPropertyName);
	}

	/**************************************************************************
	*
	*	Member variables
	*
	**************************************************************************/
}

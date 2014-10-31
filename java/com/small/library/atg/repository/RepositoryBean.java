package com.small.library.atg.repository;

import atg.repository.*;

import java.sql.Timestamp;
import java.util.*;

/************************************************************************************
*
*	Base class for repository bean classes. Supplies services for converting
*	a generic RepositoryItem into an entity specific repository bean.
*
*	@author AMC\David Small
*	@version 1.0.0.0
*	@date 6/25/2002
*
************************************************************************************/

public class RepositoryBean
{
	/**************************************************************************
	*
	*	Static members
	*
	**************************************************************************/

	/** Static member - reference to a Repository Handler used to extract
	    property values from Repository Items. Only a single instance is
	    needed to handle all requests as the handler does not maintain any
	    state.
	*/
	private static RepositoryHandler repositoryHandler = new RepositoryHandler();

	/**************************************************************************
	*
	*	Constructors
	*
	**************************************************************************/

	/** Constructor - constructs a populated object.
		@param repositoryItem The underlying Repository Item object.
	*/
	public RepositoryBean(RepositoryItem repositoryItem)
	{
		m_RepositoryItem = repositoryItem;
	}

	/**************************************************************************
	*
	*	Conversion methods
	*
	**************************************************************************/

	/** Conversion method - converts a Repository Item property of type
	    <I>Integer</I> into an <I>int</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return an <I>int</I> value if found otherwise <CODE>Integer.MIN_VALUE</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public int getPropertyInt(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyInt(m_RepositoryItem, strPropertyName);
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Short</I> into a <I>short</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>short</I> value if found otherwise <CODE>Short.MIN_VALUE</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public short getPropertyShort(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyShort(m_RepositoryItem, strPropertyName);
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Long</I> into a <I>long</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>long</I> value if found otherwise <CODE>Long.MIN_VALUE</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public long getPropertyLong(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyLong(m_RepositoryItem, strPropertyName);
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Byte</I> into a <I>byte</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>byte</I> value if found otherwise <CODE>Byte.MIN_VALUE</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public byte getPropertyByte(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyByte(m_RepositoryItem, strPropertyName);
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Boolean</I> into a <I>boolean</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>boolean</I> value if found otherwise <CODE>false</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public boolean getPropertyBoolean(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyBoolean(m_RepositoryItem, strPropertyName);
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Float</I> into a <I>float</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>float</I> value if found otherwise <CODE>Float.NaN</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public float getPropertyFloat(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyFloat(m_RepositoryItem, strPropertyName);
	}

	/** Conversion method - converts a Repository Item property of type
	    <I>Double</I> into a <I>double</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>double</I> value if found otherwise <CODE>Double.NaN</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public double getPropertyDouble(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyDouble(m_RepositoryItem, strPropertyName);
	}

	/**************************************************************************
	*
	*	Accessor methods
	*
	**************************************************************************/

	/** Accessor method - gets the underlying Repository Item object. */
	public RepositoryItem getRepositoryItem() { return m_RepositoryItem; }

	/** Accessor method - gets a Repository Item property of type
	    <I>Integer</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return an <I>Integer</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Integer getPropertyIntegerObject(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyIntegerObject(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Short</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Short</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Short getPropertyShortObject(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyShortObject(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Long</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Long</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Long getPropertyLongObject(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyLongObject(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Byte</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Byte</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Byte getPropertyByteObject(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyByteObject(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Boolean</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Boolean</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Boolean getPropertyBooleanObject(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyBooleanObject(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Float</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Float</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Float getPropertyFloatObject(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyFloatObject(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Double</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Double</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Double getPropertyDoubleObject(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyDoubleObject(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Date</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Date</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Date getPropertyDate(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyDate(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>byte</I> array.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>byte</I> array if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public byte[] getPropertyBinary(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyBinary(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>String</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>String</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public String getPropertyString(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyString(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Timestamp</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Timestamp</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Timestamp getPropertyTimestamp(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyTimestamp(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Object[]</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return an array of objects if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Object[] getPropertyArray(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyArray(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Set</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Set</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Set getPropertySet(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertySet(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>List</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>List</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public List getPropertyList(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyList(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets a Repository Item property of type
	    <I>Map</I>.
		@param strPropertyName Name of the Repository Item property value.
		@return a <I>Map</I> object if found otherwise <CODE>null</CODE>.
		@throws RepositoryPropertyConversionException thrown when the
			type expected to be returned does not match what the
			Repository Item property contains.
	*/
	public Map getPropertyMap(String strPropertyName)
		throws RepositoryPropertyConversionException
	{
		return repositoryHandler.getPropertyMap(m_RepositoryItem, strPropertyName);
	}

	/** Accessor method - gets Repository Item property without definition.
		@param strPropertyName Name of the Repository Item property value.
	*/
	public Object getPropertyObject(String strPropertyName)
	{
		return repositoryHandler.getPropertyObject(m_RepositoryItem, strPropertyName);
	}

	/**************************************************************************
	*
	*	Member variables
	*
	**************************************************************************/

	/** Member variable - reference to the underlying Repository Item object. */
	private RepositoryItem m_RepositoryItem;
}

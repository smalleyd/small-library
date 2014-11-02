package com.small.library.atg.repository;

import atg.repository.*;

import java.sql.Timestamp;
import java.util.*;

/************************************************************************************
*
*	Base class for mutable repository handler classes. Supplies methods for extracting
*	and setting properly casted property values from a Repository Item. Subclasses
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

public class MutableRepositoryHandler extends RepositoryHandler
{
	/**************************************************************************
	*
	*	Constructors
	*
	**************************************************************************/

	/** Constructor - constructs an empty object. */
	public MutableRepositoryHandler() {}

	/**************************************************************************
	*
	*	Mutator methods
	*
	**************************************************************************/

	/** Mutator method - sets a Repository Item property value of type
	    <I>Integer</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Integer newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>int</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values. <CODE>Integer.MIN_VALUE</CODE>
			indicates a Null value.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, int newValue)
	{
		if (Integer.MIN_VALUE != newValue)
			setPropertyValue(repositoryItem, strPropertyName, new Integer(newValue));
		else
			setPropertyValue(repositoryItem, strPropertyName, (Integer) null);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Short</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Short newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>short</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values. <CODE>Short.MIN_VALUE</CODE>
			indicates a Null value.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, short newValue)
	{
		if (Short.MIN_VALUE != newValue)
			setPropertyValue(repositoryItem, strPropertyName, new Short(newValue));
		else
			setPropertyValue(repositoryItem, strPropertyName, (Short) null);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Long</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Long newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>long</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values. <CODE>Long.MIN_VALUE</CODE>
			indicates a Null value.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, long newValue)
	{
		if (Long.MIN_VALUE != newValue)
			setPropertyValue(repositoryItem, strPropertyName, new Long(newValue));
		else
			setPropertyValue(repositoryItem, strPropertyName, (Long) null);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Byte</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Byte newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>byte</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values. <CODE>Byte.MIN_VALUE</CODE>
			indicates a Null value.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, byte newValue)
	{
		if (Byte.MIN_VALUE != newValue)
			setPropertyValue(repositoryItem, strPropertyName, new Byte(newValue));
		else
			setPropertyValue(repositoryItem, strPropertyName, (Byte) null);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Boolean</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Boolean newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>boolean</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, boolean newValue)
	{
		setPropertyValue(repositoryItem, strPropertyName, new Boolean(newValue));
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Float</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Float newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>float</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, float newValue)
	{
		if (!Float.isNaN(newValue))
			setPropertyValue(repositoryItem, strPropertyName, new Float(newValue));
		else
			setPropertyValue(repositoryItem, strPropertyName, (Float) null);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Double</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Double newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>double</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, double newValue)
	{
		if (!Double.isNaN(newValue))
			setPropertyValue(repositoryItem, strPropertyName, new Double(newValue));
		else
			setPropertyValue(repositoryItem, strPropertyName, (Double) null);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Date</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Date newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>byte</I> array.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, byte[] newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>String</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, String newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Timestamp</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Timestamp newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Object[]</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Object[] newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Set</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Set newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>List</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, List newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Map</I>.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Map newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value without definition.
		@param repositoryItem Repository Item to set the property value of.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(MutableRepositoryItem repositoryItem,
		String strPropertyName, Object newValue)
	{
		repositoryItem.setPropertyValue(strPropertyName, newValue);
	}

	/**************************************************************************
	*
	*	Member variables
	*
	**************************************************************************/
}

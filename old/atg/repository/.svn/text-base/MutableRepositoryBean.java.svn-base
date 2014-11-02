package com.small.library.atg.repository;

import atg.repository.*;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.sql.Timestamp;
import java.util.*;

/************************************************************************************
*
*	Base class for mutable repository bean classes. Supplies services for converting
*	a generic RepositoryItem into an entity specific repository bean.
*
*	@author AMC\David Small
*	@version 1.0.0.0
*	@date 6/25/2002
*
************************************************************************************/

public class MutableRepositoryBean extends RepositoryBean
{
	/**************************************************************************
	*
	*	Static members
	*
	**************************************************************************/

	/** Static member - reference to a Repository Handler used to extract
	    and set property values from Repository Items. Only a single instance is
	    needed to handle all requests as the handler does not maintain any
	    state.
	*/
	private static MutableRepositoryHandler repositoryHandler =
		new MutableRepositoryHandler();

	/**************************************************************************
	*
	*	Helper methods
	*
	**************************************************************************/

	/** Helper method - constructs an array of a particular repository bean
	    from an array of Mutable Repository Items. Should only be called by
	    derived classes so as to avoid the possibility of a casting exception.
		@param repositoryItems Array of Mutable Repository Items.
		@param pClassType A <I>Class</I> object that represents the
			specific entity repository bean to construct.
	*/
	protected static MutableRepositoryBean[] createRepositoryBeans(
		MutableRepositoryItem[] repositoryItems, Class pClassType)
			throws RepositoryBeanCreationException
	{
		// Get the specific array.
		MutableRepositoryBean[] repositoryBeans =
			(MutableRepositoryBean[])
			Array.newInstance(pClassType, repositoryItems.length);

		try
		{
			// Get the constructor.
			Class[] parameters = { MutableRepositoryItem.class };
			Constructor constructor = pClassType.getConstructor(parameters);

			// Construct repository beans based on the repository items.
			for (int i = 0; i < repositoryItems.length; i++)
				repositoryBeans[i] = (MutableRepositoryBean)
					constructor.newInstance(new Object[] { repositoryItems[i] });
		}

		// Handle potential exceptions separated to see all possible exceptions.
		catch (InvocationTargetException pEx)
		{
			Throwable pRootCause = pEx.getTargetException();

			if (null == pRootCause)
				throw new RepositoryBeanCreationException(pEx);

			if (pRootCause instanceof Exception)
				throw new RepositoryBeanCreationException((Exception) pRootCause);

			throw new RepositoryBeanCreationException(new Exception(pRootCause.getMessage()));
		}

		catch (IllegalAccessException pEx) { throw new RepositoryBeanCreationException(pEx); }
		catch (InstantiationException pEx) { throw new RepositoryBeanCreationException(pEx); }
		catch (NoSuchMethodException pEx) { throw new RepositoryBeanCreationException(pEx); }

		// Return the array.
		return repositoryBeans;
	}

	/**************************************************************************
	*
	*	Constructors
	*
	**************************************************************************/

	/** Constructor - constructs a populated object.
		@param repositoryItem The underlying Mutable Repository Item object.
	*/
	public MutableRepositoryBean(MutableRepositoryItem repositoryItem)
	{
		super(repositoryItem);
	}

	/**************************************************************************
	*
	*	Accessor methods
	*
	**************************************************************************/

	/** Accessor method - gets the underlying Mutable Repository Item object. */
	public MutableRepositoryItem getMutableRepositoryItem()
	{
		return (MutableRepositoryItem) getRepositoryItem();
	}

	/**************************************************************************
	*
	*	Mutator methods
	*
	**************************************************************************/

	/** Mutator method - sets a Repository Item property value of type
	    <I>Integer</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Integer newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>int</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, int newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Short</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Short newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>short</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, short newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Long</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Long newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>long</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, long newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Byte</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Byte newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>byte</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, byte newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Boolean</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Boolean newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>boolean</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, boolean newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Float</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Float newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>float</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, float newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Double</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Double newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>double</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, double newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Date</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Date newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>byte</I> array.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, byte[] newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>String</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, String newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Timestamp</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Timestamp newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Object[]</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Object[] newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Set</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Set newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>List</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, List newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value of type
	    <I>Map</I>.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Map newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/** Mutator method - sets a Repository Item property value without definition.
		@param strPropertyName Name of Repository Item property value.
		@param newValue New property values.
	*/
	public void setPropertyValue(String strPropertyName, Object newValue)
	{
		repositoryHandler.setPropertyValue(getMutableRepositoryItem(),
			strPropertyName, newValue);
	}

	/**************************************************************************
	*
	*	Member variables
	*
	**************************************************************************/
}

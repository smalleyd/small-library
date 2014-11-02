package com.small.library.atg.repository;

import atg.repository.RepositoryItem;

/*********************************************************************************
*
*	Subclass of the <I>ClassCastException</I> exception class. This exception
*	is thrown when an attempt to retrieve a property of a particular type
*	does not match the actual property cached in the <I>RepositoryItem</I>
*	property map.
*
*	The actual <I>RepositoryItem</I> and property name are included as
*	properties of the exception.
*
*	@author AMC\David Small
*	@version 1.0.0.0
*	@date 6/25/2002
*
*********************************************************************************/

public class RepositoryPropertyConversionException extends ClassCastException
{
	/** Constructor - constructs a populated object.
		@param strMessage <I>String</I> representation of the
			conversion exception.
		@param repositoryItem The <I>RepositoryItem</I> containing
			the property with the conversion exception.
		@param strPropertyName Name of the property within the
			<I>RepositoryItem</I> that caused the exception.
	*/
	public RepositoryPropertyConversionException(String strMessage,
		RepositoryItem repositoryItem, String strPropertyName)
	{
		super(strMessage);

		m_RepositoryItem = repositoryItem;
		m_strPropertyName = strPropertyName;
	}

	/** Accessor method - gets the <I>RepositoryItem</I> containing the
	    property with the conversion exception.
	*/
	public RepositoryItem getRepositoryItem() { return m_RepositoryItem; }

	/** Accessor method - gets the name of the property within the
	    <I>RepositoryItem</I> that caused the exception.
	*/
	public String getPropertyName() { return m_strPropertyName; }

	/** Member variable - reference to the <I>RepositoryItem</I> containing
	    the property with the conversion exception.
	*/
	private RepositoryItem m_RepositoryItem = null;

	/** Member variable - reference to the name of the property within the
	    <I>RepositoryItem</I> that caused the exception.
	*/
	private String m_strPropertyName = null;
}

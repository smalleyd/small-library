package com.small.library.metadata;

import com.small.library.data.DataRecord;

/***************************************************************************************
*
*	Base class of data record classes for meta data.
*	@author David Small
*	@version 1.1.0.0
*	@date 4/15/2002
*
***************************************************************************************/

public abstract class MetaDataRecord extends DataRecord
{
	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the object's hash code. */
	public int hashCode() { return toString().hashCode(); }

	/** Accessor method - gets a <I>String</I> representation of the object. */
	public String toString() { return getName(); }

	/** Accessor method - gets the Name property. */
	public String getName() { return m_strName; }

	/** Accessor method - gets the Remarks property. */
	public String getRemarks() { return m_strRemarks; }

	/*******************************************************************************
	*
	*	Member variables
	*
	*******************************************************************************/

	/** Member variable - contains the Name property. */
	protected String m_strName = null;

	/** Member variable - contains the Remarks property. */
	protected String m_strRemarks = null;
}

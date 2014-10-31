package com.small.library.data;

/***************************************************************************************
*
*	Base database persistence exception object.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 7/22/2000
*
***************************************************************************************/

public abstract class DataPersistenceException extends Exception implements java.io.Serializable
{
	/****************************************************************************
	*
	*	Constants.
	*
	****************************************************************************/

	/** Constant - message when no <I>DataCollection</I> object is supplied. */
	private static final String NO_DATA_COLLECTION = "The DataCollection object " +
		"was not supplied to the DataPersistenceException constructor.";

	/** Constant - message when no <I>DataRecord</I> object is supplied. */
	private static final String NO_DATA_RECORD = "The DataRecord object " +
		"was not supplied to the DataPersistenceException constructor.";

	/****************************************************************************
	*
	*	Constructors/Destructor.
	*
	****************************************************************************/

	/** Constructor - Accepts the <I>DataCollection</I> object and the
	    <I>DataRecord</I> object that generated the exception.
		@param pData The DataCollection object.
		@param pRecord The DataRecord object.
		@throw IllegalArgumentException when the data collection and the
			data record objects are omitted.
	*/
	DataPersistenceException(DataCollection pDataCollection,
		DataRecord pDataRecord) throws IllegalArgumentException
	{ setDataCollection(pDataCollection); setDataRecord(pDataRecord); }

	/****************************************************************************
	*
	*	Accessor methods.
	*
	****************************************************************************/

	/** Accessor - gets a reference to the <I>DataCollection</I> object. */
	public DataCollection getDataCollection() { return m_Data; }

	/** Accessor - gets a reference to the <I>DataRecord</I> object. */
	public DataRecord getDataRecord() { return m_Record; }

	/****************************************************************************
	*
	*	Mutator methods.
	*
	****************************************************************************/

	/** Mutator - sets the <I>DataCollection</I> object. */
	private void setDataCollection(DataCollection pNewValue)
		throws IllegalArgumentException
	{
		if (null == pNewValue) throw new IllegalArgumentException(NO_DATA_COLLECTION);

		m_Data = pNewValue;
	}

	/** Mutator - sets the <I>DataRecord</I> object. */
	private void setDataRecord(DataRecord pNewValue)
		throws IllegalArgumentException
	{
		if (null == pNewValue) throw new IllegalArgumentException(NO_DATA_RECORD);

		m_Record = pNewValue;
	}

	/****************************************************************************
	*
	*	Private member variables.
	*
	****************************************************************************/

	/** Member variable - main <I>DataCollection</I> object. */
	private DataCollection m_Data = null;

	/** Member variable - main <I>DataRecord</I> object. */
	private DataRecord m_Record = null;
}

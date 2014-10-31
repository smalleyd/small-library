package com.small.library.data;

/**************************************************************************************
*
*	Exception thrown when a duplicate record is found before persisting a
*	<I>DataRecord</I> object.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 7/22/2000
*
**************************************************************************************/

public class DuplicateRecordException extends DataPersistenceException implements java.io.Serializable
{
	/*****************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - accepts the <I>DataCollection</I> object and the
	    <I>DataRecord</I> object that generated the duplicate record exception.
		@param pData A DataCollection object.
		@param pRecord A DataRecord object.
	*/
	public DuplicateRecordException(DataCollection pDataCollection,
		DataRecord pDataRecord) throws IllegalArgumentException
	{ super(pDataCollection, pDataRecord); }

	/****************************************************************************
	*
	*	Main functionality
	*
	****************************************************************************/

	/** Creates a generic exception message. */
	public String getMessage()
	{
		return "The " + getDataCollection().getClass().getName() + " generated a " +
			"duplicate record exception with " + getDataRecord().getClass().getName() +
			" (" + getDataRecord().toString() + ").";
	}
}

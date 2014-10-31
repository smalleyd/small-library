package com.small.library.data;

/**************************************************************************************
*
*	Exception thrown when a unremovable record is found before persisting a
*	<I>DataRecord</I> object. A record is considered unremovable from a
*	collection when it has dependencies in other collections (foreign key).
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 7/22/2000
*
**************************************************************************************/

public class UnremovableRecordException extends DataPersistenceException implements java.io.Serializable
{
	/*****************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - accepts the <I>DataCollection</I> object and the
	    <I>DataRecord</I> object that generated the unremovable record exception.
	    A record is considered unremovable from a collection when it has
	    dependencies in other collections (foreign key).
		@param pData A DataCollection object.
		@param pRecord A DataRecord object.
	*/
	public UnremovableRecordException(DataCollection pDataCollection,
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
		return "The " + getDataCollection().getClass().getName() + " generated an " +
			"unremovable record exception with " + getDataRecord().getClass().getName() +
			" (" + getDataRecord().toString() + ").";
	}
}

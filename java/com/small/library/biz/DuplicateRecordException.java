package com.small.library.biz;

/**********************************************************************************
*
*	Exception class thrown when an updated record contains the unique
*	value as an already existing record.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/23/2002
*
**********************************************************************************/

public class DuplicateRecordException extends ValidationException
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*************************************************************************
	*
	*	Constructors
	*
	**************************************************************************

	/** Constructor - constructs a populated object.
		@param entityName name of the entity that contains the duplicate record.
		@param fieldCaption caption of the field that the duplicate record is based on.
		@param fieldValue value of the field that caused the duplicate record.
	*/
	public DuplicateRecordException(String entityName, String fieldCaption,
		String fieldValue)
	{
		super(entityName + " with " + fieldCaption + " \"" +
			fieldValue + "\", already exists.");
	}
}


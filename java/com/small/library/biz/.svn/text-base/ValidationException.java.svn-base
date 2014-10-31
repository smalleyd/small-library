package com.small.library.biz;

/**********************************************************************************
*
*	Exception class thrown when a validation error occurs. Base class
*	for more specific validation exceptions. Inherits from <I>RuntimeException</I>
*	so that transactions are rolled back when thrown from an EJB.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/19/2002
*
**********************************************************************************/

public class ValidationException extends RuntimeException
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*************************************************************************
	*
	*	Constructors
	*
	**************************************************************************

	/** Constructor - constructs a populated object.
		@param message Exception message.
	*/
	public ValidationException(String message)
	{
		super(message);
		this.message = message;	// Needed for web service serializers.
	}

	/** Needed for web service serializers. */
	public String message = null;

	/** Indicates that it was NOT successful. */
	public boolean isOk = false;

	/** Indicates that it is an error. */
	public boolean isError = true;
}

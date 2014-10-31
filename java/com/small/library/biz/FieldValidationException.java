package com.small.library.biz;

/**********************************************************************************
*
*	Exception class thrown when a data validation error occurs. Contains
*	a "field" and a "value" properties to help the caller
*	pinpoint the cause of the exception.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/19/2002
*
**********************************************************************************/

public class FieldValidationException extends ValidationException
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*************************************************************************
	*
	*	Constructors
	*
	**************************************************************************

	/** Constructor - constructs a populated object. The value is
	    assumed to be <CODE>null</CODE> and the reason is assumed to be
	    that the value is required.
		@param field name of the field that caused the exception.
		@param caption caption of the field that caused the exception.
	*/
	public FieldValidationException(String field, String caption)
	{
		super("Field, \"" + caption + "\", is required.");

		this.field = field;
		this.caption = caption;
	}

	/** Constructor - constructs a populated object.
		@param field name of the field that caused the exception.
		@param caption caption of the field that caused the exception.
		@param reason reason the value caused the exception.
	*/
	public FieldValidationException(String field, String caption, String reason)
	{
		super("Could not set field \"" + caption + "\", because " + reason + ".");

		this.field = field;
		this.caption = caption;
		this.reason = reason;
	}

	/** Constructor - constructs a populated object.
		@param field name of the field that caused the exception.
		@param caption caption of the field that caused the exception.
		@param value value of the field that caused the exception.
		@param reason reason the value caused the exception.
	*/
	public FieldValidationException(String field, String caption,
		String value, String reason)
	{
		super("Could not set field \"" + caption + "\" with the " +
			"value, \"" + value + "\", because " + reason + ".");

		this.field = field;
		this.caption = caption;
		this.value = value;
		this.reason = reason;
	}

	/*************************************************************************
	*
	*	Accessor methods
	*
	**************************************************************************

	/** Accessor method - gets the field name that caused the exception. */
	public String getFieldName() { return field; }

	/** Accessor method - gets the field caption that caused the exception. */
	public String getFieldCaption() { return caption; }

	/** Accessor method - gets the field value that caused the exception. */
	public String getFieldValue() { return value; }

	/** Accessor method - gets the reason for the exception. */
	public String getReason() { return reason; }

	/*************************************************************************
	*
	*	Member variables
	*
	**************************************************************************

	/** Member variable - name of the field that caused the exception. */
	public String field = null;

	/** Member variable - caption of the field that caused the exception. */
	public String caption = null;

	/** Member variable - value of the field that caused the exception. */
	public String value = null;

	/** Member variable - reason the field and value caused the exception. */
	public String reason = null;
}

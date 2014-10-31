package com.small.library.rest;

/** A runtime exception thrown when dynamically trying to extract parameters from
 * an HTTP request into strongly typed fields.
 *
 * @author David Small
 * @version 3.0
 * @since 11/25/2008
 *
 */

public class FieldFormatException extends RuntimeException
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/** Populator.
	 * 
	 * @param name field/parameter name.
	 * @param clazz field type attempting to convert request parameter to.
	 * @param param parameter value.
	 */
	public FieldFormatException(String name, Class<? extends Object> clazz, String param)
	{
		this.name = name;
		this.clazz = clazz;
		this.param = param;

		this.typeName = this.clazz.getSimpleName();
		this.message = "Value, \"" + param + "\" is an invalid " + this.typeName + ".";
	}

	/** Get the message. */
	public String getMessage()
	{
		return message;
	}

	/** Field/parameter name. */
	public String name = null;

	/** Class type attempting to convert request parameter to. Keep private so that it is not serialized. */
	private Class<? extends Object> clazz = null;

	/** Class type name. */
	public String typeName = null;

	/** Parameter value. */
	public String param = null;

	/** The error message. MUST have this version so that it is deserialized. */
	public String message = null;

	/** Indicator to be serialized. */
	public boolean isFieldFormatException = true;
}

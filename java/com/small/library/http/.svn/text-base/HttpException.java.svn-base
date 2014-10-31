package com.small.library.http;

/***********************************************************************************
*
*	Hypertext Transport Protocol exception.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/1/2002
*
***********************************************************************************/

public class HttpException extends Exception
{
	/** Constructor - constructs an empty object. */
	public HttpException() {}

	/** Constructor - constructs a populated object.
		@param strMessage Exception message.
	*/
	public HttpException(String strMessage)
	{ m_strMessage = strMessage; }

	/** Accessor method - gets the HTTP Message. */
	public String getMessage() { return m_strMessage; }

	/** Mutator method - sets the HTTP Message. Permits child classes to set the
	    message after initial construction.
	*/
	protected void setMessage(String strNewValue) { m_strMessage = strNewValue; }

	/** Member variable - contains the HTTP Message. */
	private String m_strMessage = null;
}

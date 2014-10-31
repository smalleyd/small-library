package com.small.library.http;

/***********************************************************************************
*
*	Hypertext Transport Protocol reponse status exception.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/1/2002
*
***********************************************************************************/

public class HttpResponseStatusException extends HttpException
{
	/***************************************************************************
	*
	*	Constructors
	*
	***************************************************************************/

	/** Constructor - constructs a populated object.
		@param pResponseStatus <I>HttpResponseStatus</I> that contains an error.
		@param strExtraData Any extra data contained in the body of the HTTP response.
	*/
	HttpResponseStatusException(HttpResponseStatus pResponseStatus,
		String strExtraData)
	{
		this(pResponseStatus.getCode(), pResponseStatus.getMessage());

		setExtraData(strExtraData);
	}

	/** Constructor - constructs a populated object.
		@param strCode HTTP Code.
		@param strMessage HTTP Message.
	*/
	public HttpResponseStatusException(String strCode, String strMessage)
		throws NumberFormatException
	{ this(Integer.parseInt(strCode), strMessage); }

	/** Constructor - constructs a populated object.
		@param nCode HTTP Code.
		@param strMessage HTTP Message.
	*/
	public HttpResponseStatusException(int nCode, String strMessage)
	{
		m_nCode = nCode;
		setMessage(strMessage + " (Code: " + m_nCode + ")");
	}

	/*****************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Accessor method - gets the HTTP Code. */
	public int getCode() { return m_nCode; }

	/** Accessor method - gets any extra data contained in the body of the HTTP response. */
	public String getExtraData() { return m_strExtraData; }

	/*****************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	/** Mutator method - sets any extra data contained in the body of the HTTP response.
	    The method should only be used within the package.
	*/
	void setExtraData(String strNewValue) { m_strExtraData = strNewValue; }

	/*****************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - contains the HTTP Code. */
	private int m_nCode = 0;

	/** Member variable - contains any extra data contained in the body of the
	    HTTP response.
	*/
	private String m_strExtraData = null;
}

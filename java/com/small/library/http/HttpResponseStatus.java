package com.small.library.http;

import java.util.StringTokenizer;

/*********************************************************************************************
*
*	Class that parses and exposes the properties of the HTTP response status line.
*	The response status line is the very first line returned to the caller of an
*	HTTP request.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/1/2002
*
********************************************************************************************/

public class HttpResponseStatus
{
	/************************************************************************************
	*
	*	Constructors
	*
	************************************************************************************/

	/** Constructor - constructs the object by parsing the response status <I>String</I>.
		@param strResponseStatusLine A <I>String</I> representing the response
			status line.
	*/
	public HttpResponseStatus(String strResponseStatusLine)
		throws HttpException
	{
		// Break out the response status line.
		StringTokenizer pTokens = new StringTokenizer(strResponseStatusLine, " ");

		// Get the tokens.
		int i = 0;
		String strCode = null;

		while ((i < 3) && pTokens.hasMoreTokens())
		{
			if (0 == i)
				m_strVersion = pTokens.nextToken();
			else if (1 == i)
				strCode = pTokens.nextToken();
			else
				m_strMessage = pTokens.nextToken();

			i++;
		}

		// Validate the status line?
		if (i < 2)
			throw new HttpException("The HTTP response status line does not include a status code (" +
				strResponseStatusLine + ").");

		else if (!m_strVersion.startsWith("HTTP/"))
			throw new HttpException("The HTTP response status line does not contain a valid protocol (" +
				strResponseStatusLine + ").");

		else if (6 > m_strVersion.length())
			throw new HttpException("The HTTP response status line does not contain a protocol version (" +
				strResponseStatusLine + ").");

		// Remove the "HTTP/" part.
		m_strVersion = m_strVersion.substring(5);

		// Convert the status code to an integer.
		try { m_nCode = Integer.parseInt(strCode); }
		catch (NumberFormatException pEx)
		{
			throw new HttpException("The HTTP response status line does not contain a numeric status code (" +
				strResponseStatusLine + ").");
		}
	}

	/************************************************************************************
	*
	*	Accessor methods
	*
	************************************************************************************/

	/** Accessor method - gets the HTTP version number. */
	public String getVersion() { return m_strVersion; }

	/** Accessor method - gets the HTTP status code. */
	public int getCode() { return m_nCode; }

	/** Accessor method - gets the HTTP status message. */
	public String getMessage() { return m_strMessage; }

	/** Accessor method - indicates whether a Continue request was returned. */
	public boolean isContinue() { return (((100 <= m_nCode) && (200 > m_nCode)) ? true : false); }

	/** Accessor method - indicates whether a OK request was returned. */
	public boolean isOK() { return (((200 <= m_nCode) && (300 > m_nCode)) ? true : false); }

	/** Accessor method - indicates whether a Redirect request was returned. */
	public boolean isRedirect() { return (((300 <= m_nCode) && (400 > m_nCode)) ? true : false); }

	/** Accessor method - indicates whether the status returned an error. */
	public boolean isError() { return isClientError() || isServerError(); }

	/** Accessor method - indicates whether the status returned a client error. */
	public boolean isClientError() { return (((400 <= m_nCode) && (500 > m_nCode)) ? true : false); }

	/** Accessor method - indicates whether the status returned a client error. */
	public boolean isServerError() { return (((500 <= m_nCode) && (600 > m_nCode)) ? true : false); }

	/************************************************************************************
	*
	*	Member variables
	*
	************************************************************************************/

	/** Member variable - contains the HTTP version number. */
	private String m_strVersion = null;

	/** Member variable - contains the HTTP status code. */
	private int m_nCode = 0;

	/** Member variable - contains the HTTP status message. */
	private String m_strMessage = null;
}

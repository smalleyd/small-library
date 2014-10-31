package com.small.library.http;

import java.io.*;
import java.util.ArrayList;

/*************************************************************************************************
*
*	Class that represents an HTTP response. An instance of the class is returned from
*	the <CODE>HttpClient.handleRequest</CODE>.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/5/2002
*
*************************************************************************************************/

public class HttpResponse
{
	/*****************************************************************************************
	*
	*	Constants
	*
	*****************************************************************************************/

	/*****************************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************************/

	/** Constructor - constructs a populated object.
		@param pIn Input stream to the HTTP response.
	*/
	public HttpResponse(InputStream pIn)
		throws HttpException, IOException
	{
		this(new BufferedReader(new InputStreamReader(pIn)));
	}

	/** Constructor - constructs a populated object.
		@param pIn Input stream to the HTTP response.
	*/
	public HttpResponse(BufferedReader pIn)
		throws HttpException, IOException
	{
		readFrom(pIn);
	}

	/*****************************************************************************************
	*
	*	Action methods
	*
	*****************************************************************************************/

	/** Action method - reads the HTTP response from the input stream and caches the
	    response values in the instance's properties.
		@param pIn Input stream to the HTTP response.
	*/
	public void readFrom(BufferedReader pIn)
		throws HttpException, IOException
	{
		// Local variables
		String strLine = null;

		// First get the header information.
		// If the status code is less than 200 (i.e. 100 Continue), assume
		// another header will be coming.
		while (true)
		{
			// Get the status line.
			strLine = pIn.readLine();

			if (null == strLine)
				throw new HttpException("HttpResponse [readFrom]: No data returned from HTTP request.");

			m_Status = new HttpResponseStatus(strLine);

			// Has a Continue request come?
			if (m_Status.isContinue())
			{
				// Eat the header lines.
				eatLines(pIn);

				// Start again.
				continue;
			}

			// Otherwise, just break out.
			break;
		}

		// Get the header information.
		m_Headers = buildHttpHeaderArray(pIn);

		// DO NOT throw any errors until the entire body has been received.
		// Could contain additional error message.

		// Get the data.
		m_strData = "";

		while (null != (strLine = pIn.readLine()))
			m_strData+= strLine + "\n";

		// Has an error been returned?
		if (m_Status.isError())
			throw new HttpResponseStatusException(m_Status, m_strData);

		// Has a redirect been returned?
		// DO NOT handle the redirect here. Let the HttpClient the redirect.
		// Should not assume automatic redirection.
		// Continue to get data though.
	}

	/*****************************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************************/

	/** Helper method - Eats HTTP Response lines until the first blank line.
	    Used to bypass header information that is not needed.
		@param pIn A <I>BufferedReader</I> that is streaming the HTTP Response.
	*/
	private static void eatLines(BufferedReader pIn) throws IOException
	{
		String strLine = null;

		while ((null != (strLine = pIn.readLine())) && (0 < strLine.length()))
			;
	}

	/** Helper method - Builds header array from a response.
		@param pIn A <I>BufferedReader</I> that is streaming the HTTP Response.
		@return A <I>HttpHeader</I> array representation of the HTTP headers.
	*/
	private static HttpHeader[] buildHttpHeaderArray(BufferedReader pIn)
		throws IOException
	{
		String strLine = null;
		ArrayList pReturn = new ArrayList();

		while ((null != (strLine = pIn.readLine())) && (0 <= strLine.length()))
		{
			int nIndex = strLine.indexOf(":");

			// No value.
			if (-1 == nIndex)
				pReturn.add(new HttpHeader(strLine, null));

			else if (0 == nIndex)
				continue;

			else
			{
				String strName = strLine.substring(0, nIndex);

				if (nIndex >= (strLine.length() - 1))
					pReturn.add(new HttpHeader(strName, null));
				else
					pReturn.add(new HttpHeader(strName, strLine.substring(nIndex + 1).trim()));
			}
		}

		// Were any headers found?
		int nSize = pReturn.size();

		if (0 == nSize)
			return null;

		return (HttpHeader[]) pReturn.toArray(new HttpHeader[nSize]);
	}

	/*****************************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************************/

	/** Accessor method - gets a reference to the response status line. */
	public HttpResponseStatus getStatus() { return m_Status; }

	/** Accessor method - gets a reference to the response headers. */
	public HttpHeader[] getHeaders() { return m_Headers; }

	/** Accessor method - gets a reference to the response data. */
	public String getData() { return m_strData; }

	/*****************************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************************/

	/** Member variable - contains a reference to the response status line. */
	private HttpResponseStatus m_Status = null;

	/** Member variable - contains a reference to the response headers. */
	private HttpHeader[] m_Headers = null;

	/** Member variable - contains a reference to the response data. */
	private String m_strData = null;
}

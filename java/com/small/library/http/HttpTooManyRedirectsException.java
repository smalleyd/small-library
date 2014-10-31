package com.small.library.http;

import com.small.library.net.URL;

/***********************************************************************************
*
*	Exception that represents an HTTP client attempt to perform more than
*	the maximum number of redirects.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/6/2002
*
***********************************************************************************/

public class HttpTooManyRedirectsException extends HttpException
{
	/***************************************************************************
	*
	*	Constructors
	*
	***************************************************************************/

	/** Constructor - constructs a populated object.
		@param pRequest The original HTTP request.
		@param nRedirect The number of redirects.
	*/
	public HttpTooManyRedirectsException(HttpRequest pRequest,
		int nRedirects)
	{
		this(pRequest.getURL(), nRedirects);
	}

	/** Constructor - constructs a populated object.
		@param pOriginalURL The original URL of the HTTP request.
		@param nRedirect The number of redirects.
	*/
	public HttpTooManyRedirectsException(URL pOriginalURL,
		int nRedirects)
	{
		m_OriginalURL = pOriginalURL;
		m_nRedirects = nRedirects;

		setMessage("The HTTP request to \"" + pOriginalURL.toString() +
			"\" exceeded the maximum number (" + nRedirects +
			") of redirects.");
	}

	/***************************************************************************
	*
	*	Accessor methods
	*
	***************************************************************************/

	/** Accessor method - gets the orginating URL. */
	public URL getOriginalURL() { return m_OriginalURL; }

	/** Accessor method - gets the number of redirects. */
	public int getRedirects() { return m_nRedirects; }

	/***************************************************************************
	*
	*	Member variables
	*
	***************************************************************************/

	/** Member variable - contains a reference to the URL. */
	private URL m_OriginalURL = null;

	/** Member variables - contains the number of redirects. */
	private int m_nRedirects = 0;
}

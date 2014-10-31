package com.small.library.http;

import java.io.*;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import com.small.library.net.URL;

/*********************************************************************************
*
*	Class that performs HTTP Client functionality. At construction a host
*	name (or IP address) and port can be provided. The class uses the default
*	"createSocket" method to retrieve a simple <I>java.net.Socket</I> object,
*	that handles all transmissions. Classes the extend this class, can
*	override this method to return more sophisticated sockets (i.e. SSL,
*	tunnelling, ...).
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 7/26/2001
*
*********************************************************************************/

public class HttpClient
{
	/************************************************************************
	*
	*	Constants
	*
	************************************************************************/

	/** Constant - Default port number for HTTP. */
	public static final int PORT_HTTP_DEFAULT = 80;

	/** Constant - Default port number for HTTPS. */
	public static final int PORT_HTTPS_DEFAULT = 443;

	/** Constant - Default maximum redirects. */
	public static final int MAX_REDIRECTS_DEFAULT = 5;

	/** Constant - error message representing a missing "Location" header within a
	    redirect response.
	*/
	public static final String ERROR_MSG_LOCATION_NOT_SPECIFIED = "The \"Location\" " +
		"header is missing from the HTTP redirect response.";

	/************************************************************************
	*
	*	Constructors
	*
	************************************************************************/

	/** Constructor - constructs an object with a default <I>SocketFactory</I>. */
	public HttpClient()
	{
		this((SocketFactory) null, MAX_REDIRECTS_DEFAULT);
	}

	/** Constructor - constructs an object with a default <I>SocketFactory</I>.
		@param nMaxRedirects Maximum number of redirects permitted.
	*/
	public HttpClient(int nMaxRedirects)
	{
		this((SocketFactory) null, nMaxRedirects);
	}

	/** Constructor - constructs a populated object.
		@param pSocketFactory A custom <I>SocketFactory</I> used to create socket
			connections requested hosts.
	*/
	public HttpClient(SocketFactory pSocketFactory)
	{
		this(pSocketFactory, MAX_REDIRECTS_DEFAULT);
	}

	/** Constructor - constructs a populated object.
		@param pSocketFactory A custom <I>SocketFactory</I> used to create socket
			connections requested hosts.
		@param nMaxRedirects Maximum number of redirects permitted.
	*/
	public HttpClient(SocketFactory pSocketFactory, int nMaxRedirects)
	{
		if (null == pSocketFactory)
			pSocketFactory = SocketFactory.getDefault();

		setSocketFactory(pSocketFactory);

		if (0 > nMaxRedirects)
			nMaxRedirects = MAX_REDIRECTS_DEFAULT;

		m_nMaxRedirects = nMaxRedirects;
	}

	/************************************************************************
	*
	*	Action methods
	*
	************************************************************************/

	/** Helper method - Handles an HTTP request. Makes the socket connection,
	    delegates the output, delegates the input, and returns an <I>HttpResponse</I>.
		@param pRequest An <I>HttpRequest</I> object that contains the HTTP request
			data.
		@return An <I>HttpResponse</I> object.
	*/
	public HttpResponse handleRequest(HttpRequest pRequest)
		throws HttpException, UnknownHostException, IOException
	{
		return handleRequest(pRequest, pRequest, 0);
	}

	/** Helper method - Handles an HTTP request. Makes the socket connection,
	    delegates the output, delegates the input, and returns an <I>HttpResponse</I>.
	    The method is private and meant for internal use only. The additional parameter
	    keeps track of the number of redirects.
		@param pRequest An <I>HttpRequest</I> object that contains the HTTP request
			data.
		@param pOriginalRequest The original <I>HttpRequest</I> object prior to any redirects.
		@param nRedirects The number of redirects made thus far.
		@return An <I>HttpResponse</I> object.
	*/
	private HttpResponse handleRequest(HttpRequest pRequest, HttpRequest pOriginalRequest,
		int nRedirects)
		throws HttpException, UnknownHostException, IOException
	{
		// Check the number redirections.
		if (nRedirects > m_nMaxRedirects)
			throw new HttpTooManyRedirectsException(pOriginalRequest, nRedirects);

		// Get the request URL.
		URL pUrl = pRequest.getURL();

		// Is the URL valid?
		if (null == pUrl)
			throw new HttpException("HttpResponse [handleRequest]: The request-URL cannot be null.");

		// Get the host.
		InetAddress pHost = pUrl.getHost();

		// Get the port number.
		int nPort = pUrl.getPort();

		// Is valid? If not, use default.
		if (0 > nPort)
		{
			String strProtocol = pUrl.getProtocol();

			if ("https".equalsIgnoreCase(strProtocol))
				nPort = PORT_HTTPS_DEFAULT;
			else
				nPort = PORT_HTTP_DEFAULT;
		}

		// Make a connection to the host.
		Socket pSocket = m_SocketFactory.createSocket(pHost, nPort);

		// Get the output stream.
		OutputStream pOut = pSocket.getOutputStream();

		// Output to the HTTP server.
		pRequest.writeTo(pOut);

		// Get the input stream.
		InputStream pIn = pSocket.getInputStream();

		// Get the response from the HTTP server.
		HttpResponse pResponse = new HttpResponse(pIn);

		// Check the status.
		HttpResponseStatus pResponseStatus = pResponse.getStatus();

		// Was there a redirect.
		if (pResponseStatus.isRedirect())
			return handleRedirect(pResponse, pRequest, nRedirects);

		// Return the response.
		return pResponse;
	}

	/************************************************************************
	*
	*	Helper methods
	*
	************************************************************************/

	/** Helper method - handles any HTTP redirect responses. */
	private HttpResponse handleRedirect(HttpResponse pResponse,
		HttpRequest pRequest, int nRedirects)
		throws HttpException, UnknownHostException, IOException
	{
		// Get headers.
		HttpHeader[] pHeaders = pResponse.getHeaders();

		// Have headers been supplied?
		if (null == pHeaders)
			throw new HttpException("HttpClient [handleRedirect]: " +
				ERROR_MSG_LOCATION_NOT_SPECIFIED);

		// Look for the "Location" header. Cannot use map because
		// multiple headers with the same name can exist.
		String strLocation = null;

		for (int i = 0; ((i < pHeaders.length) && (null == strLocation)); i++)
			if ("Location".equals(pHeaders[i].getName()))
				strLocation = pHeaders[i].getValue();

		// Was an appropriate header found?
		if (null == strLocation)
			throw new HttpException("HttpClient [handleRedirect]: " +
				ERROR_MSG_LOCATION_NOT_SPECIFIED);

		// Clone the current request and change the URL location.
		return handleRequest(pRequest.cloneForRedirection(strLocation),
			pRequest,
			(++nRedirects));
	}

	/************************************************************************
	*
	*	Accessor methods
	*
	************************************************************************/

	/** Accessor method - gets the <I>SocketFactory</I>. */
	public SocketFactory getSocketFactory() { return m_SocketFactory; }

	/** Accessor method - gets the maximum number of redirects permitted. */
	public int getMaxRedirects() { return m_nMaxRedirects; }

	/************************************************************************
	*
	*	Mutator methods
	*
	************************************************************************/

	/** Mutator method - sets the <I>SocketFactory</I>. */
	public void setSocketFactory(SocketFactory pNewValue) { m_SocketFactory = pNewValue; }

	/** Mutator method - sets the maximum number of redirects permitted. */
	public void setMaxRedirects(int nNewValue) { m_nMaxRedirects = nNewValue; }

	/************************************************************************
	*
	*	Members variables
	*
	************************************************************************/

	/** Member variable - contains a reference to the <I>SocketFactory</I> used
	    by this instance.
	*/
	private SocketFactory m_SocketFactory = null;

	/** Member variable - contains a the maximum number of redirects permitted. */
	private int m_nMaxRedirects = MAX_REDIRECTS_DEFAULT;

	/************************************************************************
	*
	*	Driver methods
	*
	************************************************************************/

	/** Driver method - test entry point. */
	public static void main(String strArgs[])
	{
		try
		{
			HttpClient pClient = new HttpClient(new com.small.library.net.TunnelSocketFactory("webproxy.ideal.corp.local", 8080));
			HttpRequest pRequest = new HttpRequestGet(new URL("https", "www.verisign.com", 443, null, null));

			HttpResponse pResponse = pClient.handleRequest(pRequest);
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}

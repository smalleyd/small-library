package com.small.library.http;

import java.io.*;

import com.small.library.net.URL;

/************************************************************************************************
*
*	Class that represents an HTTP POST request. See <I>HttpRequest</I> for details on usage.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/4/2002
*	@see com.small.library.http.HttpRequest HttpRequest for details on usage.
*
************************************************************************************************/

public abstract class HttpRequestPost extends HttpRequest
{
	/*****************************************************************************************
	*
	*	Constants
	*
	*****************************************************************************************/

	/** Constant - <I>String</I> version of the method name. */
	public static final String METHOD = "POST";

	/*****************************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************************/

	/** Constructor - constructs an empty object. */
	public HttpRequestPost() {}

	/** Constructor - constructs a populated object.
		@param pUrl The URL of the resource, script, or page being requested.
	*/
	public HttpRequestPost(URL pUrl)
	{ this(pUrl, (String) null); }

	/** Constructor - constructs a populated object.
		@param pUrl The URL of the resource, script, or page being requested.
		@param strVersion The HTTP version being employed.
	*/
	public HttpRequestPost(URL pUrl, String strVersion)
	{ this(pUrl, strVersion, (HttpHeader[]) null); }

	/** Constructor - constructs a populated object.
		@param pUrl The URL of the resource, script, or page being requested.
		@param strVersion The HTTP version being employed.
		@param pHeaders The array of <I>HttpHeader</I> objects.
	*/
	public HttpRequestPost(URL pUrl, String strVersion, HttpHeader[] pHeaders)
	{
		super(pUrl, strVersion, pHeaders);
	}

	/** Constructor - clone constructor. */
	public HttpRequestPost(HttpRequestPost pToBeCloned)
	{
		super(pToBeCloned);
	}

	/*****************************************************************************************
	*
	*	Required methods: HttpRequest
	*
	*****************************************************************************************/

	/** Accessor method - gets the HTTP method name (GET, POST, ...). */
	public String getMethod() { return METHOD; }
}

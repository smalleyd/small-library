package com.small.library.http;

import java.io.*;

import com.small.library.net.URL;

/************************************************************************************************
*
*	Class that represents an HTTP GET request. See <I>HttpRequest</I> for details on usage.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/4/2002
*	@see com.small.library.http.HttpRequest HttpRequest for details on usage.
*
************************************************************************************************/

public class HttpRequestGet extends HttpRequest
{
	/*****************************************************************************************
	*
	*	Constants
	*
	*****************************************************************************************/

	/** Constant - <I>String</I> version of the method name. */
	public static final String METHOD = "GET";

	/*****************************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************************/

	/** Constructor - constructs an empty object. */
	public HttpRequestGet() {}

	/** Constructor - constructs a populated object.
		@param pUrl The URL of the resource, script, or page being requested.
	*/
	public HttpRequestGet(URL pUrl)
	{ this(pUrl, (String) null); }

	/** Constructor - constructs a populated object.
		@param pUrl The URL of the resource, script, or page being requested.
		@param strVersion The HTTP version being employed.
	*/
	public HttpRequestGet(URL pUrl, String strVersion)
	{ this(pUrl, strVersion, (HttpHeader[]) null); }

	/** Constructor - constructs a populated object.
		@param pUrl The URL of the resource, script, or page being requested.
		@param strVersion The HTTP version being employed.
		@param pHeaders The array of <I>HttpHeader</I> objects.
	*/
	public HttpRequestGet(URL pUrl, String strVersion, HttpHeader[] pHeaders)
	{
		super(pUrl, strVersion, pHeaders);
	}

	/** Constructor - clone constructor. */
	public HttpRequestGet(HttpRequestGet pToBeCloned)
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

	/** Accessor method - gets the length of the body content. */
	public int getContentLength() { return 0; }

	/** Action method - writes the body of the HTTP request. */
	public void writeBody(PrintWriter pOut) throws IOException {}

	/*****************************************************************************************
	*
	*	Overridden methods: Object
	*
	*****************************************************************************************/

	/** Accessor method - clones <CODE>this</CODE>. All derived classes should override this
	    method if the class maintains additional properties.
	*/
	public Object clone() { return new HttpRequestGet(this); }
}

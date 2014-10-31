package com.small.library.http;

import java.io.*;

import com.small.library.net.URL;

/************************************************************************************************
*
*	Class that represents an HTTP POST request with a predefined body.
*	See <I>HttpRequest</I> for details on usage.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/4/2002
*	@see com.small.library.http.HttpRequest HttpRequest for details on usage.
*
************************************************************************************************/

public class HttpRequestPostWithBody extends HttpRequestPost
{
	/*****************************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************************/

	/** Constructor - constructs a populated object.
		@param strBody Body of the HTTP Post.
	*/
	public HttpRequestPostWithBody(String strBody)
		throws NullPointerException
	{ this((URL) null, strBody); }

	/** Constructor - constructs a populated object.
		@param pUrl The URL of the resource, script, or page being requested.
		@param strBody Body of the HTTP Post.
	*/
	public HttpRequestPostWithBody(URL pUrl, String strBody)
		throws NullPointerException
	{ this(pUrl, (String) null, strBody); }

	/** Constructor - constructs a populated object.
		@param pUrl The URL of the resource, script, or page being requested.
		@param strVersion The HTTP version being employed.
		@param strBody Body of the HTTP Post.
	*/
	public HttpRequestPostWithBody(URL pUrl, String strVersion, String strBody)
		throws NullPointerException
	{ this(pUrl, strVersion, (HttpHeader[]) null, strBody); }

	/** Constructor - constructs a populated object.
		@param pUrl The URL of the resource, script, or page being requested.
		@param strVersion The HTTP version being employed.
		@param pHeaders The array of <I>HttpHeader</I> objects.
		@param strBody Body of the HTTP Post.
	*/
	public HttpRequestPostWithBody(URL pUrl, String strVersion, HttpHeader[] pHeaders, String strBody)
		throws NullPointerException
	{
		super(pUrl, strVersion, pHeaders);
		setBody(strBody);
	}

	/** Constructor - clone constructor. */
	public HttpRequestPostWithBody(HttpRequestPost pToBeCloned)
	{
		super(pToBeCloned);
	}

	/*****************************************************************************************
	*
	*	Required methods: HttpRequest
	*
	*****************************************************************************************/

	/** Accessor method - gets the length of the body content. */
	public int getContentLength() { return m_strBody.length(); }

	/** Action method - writes the body of the HTTP request. */
	public void writeBody(PrintWriter pOut) throws IOException { pOut.println(); pOut.println(m_strBody); }

	/*****************************************************************************************
	*
	*	Overridden methods: Object
	*
	*****************************************************************************************/

	/** Accessor method - clones <CODE>this</CODE>. All derived classes should override this
	    method if the class maintains additional properties.
	*/
	public Object clone() { return new HttpRequestPostWithBody(this); }

	/*****************************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************************/

	/** Accessor method - gets the POST body. */
	public String getBody() { return m_strBody; }

	/*****************************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************************/

	/** Mutator method - sets the POST body. */
	public void setBody(String strNewValue)
		throws NullPointerException
	{
		if (null == strNewValue)
			throw new NullPointerException("HttpRequestPostWithBodyWithBody [setBody]: The body value cannot be null.");

		m_strBody = strNewValue;
	}

	/*****************************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************************/

	/** Member variable - contains a reference to the POST body. */
	private String m_strBody = null;
}

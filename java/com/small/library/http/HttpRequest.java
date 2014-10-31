package com.small.library.http;

import java.io.*;
import com.small.library.net.URL;

/************************************************************************************************
*
*	Class that represents an HTTP request. To use the class perform the following tasks:
*
*	<OL>
*		<LI>Construct a derived version of this object.</LI>
*		<LI>Set any properties not set during constructions.</LI>
*		<LI>Call <CODE>writeTo</CODE> with a valid <I>Socket</I> object.</LI>
*	</OL>
*
*	For better handling use this class in conjunction with an instance of the <I>HttpClient</I>
*	class. The <I>HttpClient</I> with manage both the request and the response.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/4/2002
*
************************************************************************************************/

public abstract class HttpRequest
{
	/*****************************************************************************************
	*
	*	Constants
	*
	*****************************************************************************************/

	/** Constant - default HTTP version. */
	public static final String VERSION_DEFAULT = "1.1";

	/*****************************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************************/

	/** Constructor - constructs an empty object. */
	public HttpRequest() {}

	/** Constructor - constructs a populated object.
		@param pURL The URL of the resource, script, or page being requested.
	*/
	public HttpRequest(URL pURL)
	{ this(pURL, (String) null); }

	/** Constructor - constructs a populated object.
		@param pURL The URL of the resource, script, or page being requested.
		@param strVersion The HTTP version being employed.
	*/
	public HttpRequest(URL pURL, String strVersion)
	{ this(pURL, strVersion, (HttpHeader[]) null); }

	/** Constructor - constructs a populated object.
		@param pURL The URL of the resource, script, or page being requested.
		@param strVersion The HTTP version being employed.
		@param pHeaders The array of <I>HttpHeader</I> objects.
	*/
	public HttpRequest(URL pURL, String strVersion, HttpHeader[] pHeaders)
	{
		setURL(pURL);
		setVersion(strVersion);
		setHeaders(pHeaders);
	}

	/** Constructor - clone constructor. */
	public HttpRequest(HttpRequest pToBeCloned)
	{
		this(pToBeCloned.m_URL, pToBeCloned.m_strVersion, pToBeCloned.m_Headers);
	}

	/*****************************************************************************************
	*
	*	Action methods
	*
	*****************************************************************************************/

	/** Action method - writes the HTTP request to the <I>OutputStream</I>.
		@param pOut The <I>OutputStream</I> that accepts the HTTP request.
	*/
	public void writeTo(OutputStream pOut) throws HttpException, IOException
	{
		writeTo(new PrintWriter(new BufferedWriter(new OutputStreamWriter(pOut))));
	}

	/** Action method - writes the HTTP request to the <I>OutputStream</I>.
		@param pOut The <I>PrintWriter</I> that accepts the HTTP request.
	*/
	public void writeTo(PrintWriter pOut) throws HttpException, IOException
	{
		// Get any necessary values.
		int nContentLength = getContentLength();

		// Write the request-line.
		pOut.print(getMethod());
		pOut.print(" ");
		pOut.print(m_URL.getPathExt());
		pOut.print(" HTTP/");
		pOut.println(getVersion());
		pOut.println("Host: " + m_URL.getHostNameExt());

		// Write the content length header.
		if (nContentLength > 0)
			pOut.println("Content-Length: " + nContentLength);

		// Print out the supplied headers.
		if (null != m_Headers)
			for (int i = 0; i < m_Headers.length; i++)
				pOut.println(m_Headers[i].toHeader());

		// Write the body.
		writeBody(pOut);

		// Write the terminating "new-line".
		pOut.println();

		// Make certain to flush the output stream.
		pOut.flush();

		// Check for any additional errors
		if (pOut.checkError())
			throw new IOException("HttpRequest [writeTo]: Unidentified OutputStream error.");
	}

	/** Action method - clones <CODE>this</CODE> instance and changes the location (URL).
		@param strNewLocation Specifies the location value (absolute or relative)
			of the new URL.
		@return an <I>HttpRequest</I> object.
	*/
	public HttpRequest cloneForRedirection(String strNewLocation)
		throws HttpException
	{
		HttpRequest pCloned = (HttpRequest) clone();

		pCloned.changeLocation(strNewLocation);

		return pCloned;
	}

	/*****************************************************************************************
	*
	*	Abstract methods
	*
	*****************************************************************************************/

	/** Abstract method - gets the HTTP method name (GET, POST, ...). */
	public abstract String getMethod();

	/** Abstract method - gets the length of the body content. */
	public abstract int getContentLength();

	/** Abstract method - writes the body of the HTTP request. */
	public abstract void writeBody(PrintWriter pOut) throws IOException;

	/** Abstract method - derived classes must implement the <CODE>clone</CODE>. Derived
	    classes can make use of the clone constructor included in this class.
		@return an <I>HttpRequest</I> object.
	*/
	public abstract Object clone();

	/*****************************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************************/

	/** Accessor method - gets the URL or the request. */
	public URL getURL() { return m_URL; }

	/** Accessor method - gets the HTTP version. */
	public String getVersion() { return m_strVersion; }

	/** Accessor method - gets the array of <I>HttpHeader</I> objects. */
	public HttpHeader[] getHeaders() { return m_Headers; }

	/*****************************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************************/

	/** Mutator method - sets the URL of the request. */
	public void setURL(URL pNewValue)
	{
		m_URL = pNewValue;
	}

	/** Mutator method - sets the HTTP version. */
	public void setVersion(String strNewValue)
	{
		if (null == strNewValue)
			m_strVersion = VERSION_DEFAULT;

		else
			m_strVersion = strNewValue;
	}

	/** Mutator method - sets the array of <I>HttpHeader</I> object. */
	public void setHeaders(HttpHeader[] pNewValue) { m_Headers = pNewValue; }

	/** Mutator method - changes the URL to the location specified. If the location is
	    an absolute URL, the existing URL is changed to the new URL. If the location
	    is a relative URL (path), the existing URL's path (file) is altered.
		@param strNewLocation Specifies the location value (absolute or relative)
			of the new URL.
	*/
	public void changeLocation(String strLocation)
		throws HttpException
	{

		// Temporarily, just change the "Path" property.
		m_URL.setPath(strLocation);
	}

	/*****************************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************************/

	/** Member variable - contains a reference to the URL of the request. */
	private URL m_URL = null;

	/** Member variable - contains a reference to the HTTP version. */
	private String m_strVersion = null;

	/** Member variable - contains a reference to the array of <I>HttpHeader</I> objects. */
	private HttpHeader[] m_Headers = null;
}

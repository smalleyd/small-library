package com.small.library.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/****************************************************************************************************
*
*	Class that represents all the components of a URL. Below is an example of how a
*	standard URL maps to the properties of this class.
*
*	<BR><BR>
*
*	Real Example: http://www.i-deal.com:80/contacts/LookUpByType.html?TypeID=5&Page=3
*	Properties: [Protocol]://[Host]:[Port][Path]?[QueryString]
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/6/2002
*
****************************************************************************************************/

public class URL
{
	/********************************************************************************************
	*
	*	Constructors
	*
	********************************************************************************************/

	/** Constructor - constructs a populated object.
		@param strProtocol The Protocol property.
		@param strHost The Host property.
		@param nPort The Port property.
		@param strPath The Path property.
		@param strQueryString The Query String property.
	*/
	public URL(String strProtocol, String strHost, int nPort, String strPath,
		String strQueryString)
		throws UnknownHostException
	{
		setProtocol(strProtocol);
		setHost(strHost);
		setPort(nPort);
		setPath(strPath);
		setQueryString(strQueryString);
	}

	/** Constructor - constructs a populated object.
		@param strProtocol The Protocol property.
		@param pHost The Host property.
		@param nPort The Port property.
		@param strPath The Path property.
		@param strQueryString The Query String property.
	*/
	public URL(String strProtocol, InetAddress pHost, int nPort, String strPath,
		String strQueryString)
	{
		setProtocol(strProtocol);
		setHost(pHost);
		setPort(nPort);
		setPath(strPath);
		setQueryString(strQueryString);
	}

	/********************************************************************************************
	*
	*	Accessor methods
	*
	********************************************************************************************/

	/** Accessor method - gets the Protocol property. */
	public String getProtocol() { return m_strProtocol; }

	/** Accessor method - gets the Host property. */
	public InetAddress getHost() { return m_Host; }

	/** Accessor method - gets the Host's name. */
	public String getHostName() { return m_Host.getHostName(); }

	/** Accessor method - gets the a concatenation of the Host Name and the Port Number.
	    Excludes the Port Number if the less than 0.
	*/
	public String getHostNameExt()
	{
		if (0 > m_nPort)
			return m_Host.getHostName();

		return m_Host.getHostName() + ":" + m_nPort;
	}

	/** Accessor method - gets the Port property.
		@return a valid port number or -1 if not available.
	*/
	public int getPort() { return m_nPort; }

	/** Accessor method - gets the Path property. */
	public String getPath() { return m_strPath; }

	/** Accessor method - gets the Query String property. */
	public String getQueryString() { return m_strQueryString; }

	/** Accessor method - gets the Path plus the Query String if one exists. */
	public String getPathExt()
	{
		if (null == m_strQueryString)
			return m_strPath;

		return m_strPath + "?" + m_strQueryString;
	}

	/** Accessor method - converts the URL to a <I>String</I> version. */
	public String toString()
	{
		String strReturn = m_strProtocol + "://" + m_Host.getHostName();

		if (0 <= m_nPort)
			strReturn+= ":" + m_nPort;

		return strReturn + getPathExt();
	}

	/** Accessor method - object's hash code. */
	public int hashCode() { return toString().hashCode(); }

	/********************************************************************************************
	*
	*	Mutator methods
	*
	********************************************************************************************/

	/** Mutator method - sets the Protocol property. */
	public void setProtocol(String strNewValue) { m_strProtocol = strNewValue; }

	/** Mutator method - sets the Host property. */
	public void setHost(InetAddress pNewValue) { m_Host = pNewValue; }

	/** Mutator method - sets the Host property by name or IP address. */
	public void setHost(String strHost) throws UnknownHostException
	{
		setHost(InetAddress.getByName(strHost));
	}

	/** Mutator method - sets the Port property. */
	public void setPort(int nNewValue) { m_nPort = nNewValue; }

	/** Mutator method - sets the Path property. */
	public void setPath(String strNewValue)
	{
		if ((null == strNewValue) || (0 == strNewValue.length()))
			strNewValue = "/";

		m_strPath = strNewValue;
	}

	/** Mutator method - sets the Query String property. */
	public void setQueryString(String strNewValue)
	{
		if ((null != strNewValue) && (0 == strNewValue.length()))
			strNewValue = null;

		m_strQueryString = strNewValue;
	}

	/********************************************************************************************
	*
	*	Member variables
	*
	********************************************************************************************/

	/** Member variable - contains the Protocol property. */
	private String m_strProtocol = null;

	/** Member variable - contains the Host property. */
	private InetAddress m_Host = null;

	/** Member variable - contains the Port property. */
	private int m_nPort = 0;

	/** Member variable - contains the Path property. */
	private String m_strPath = null;

	/** Member variable - contains the Query String property. */
	private String m_strQueryString = null;
}

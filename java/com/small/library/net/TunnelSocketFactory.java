package com.small.library.net;

import java.io.*;
import java.net.*;
import javax.net.SocketFactory;

/*********************************************************************************************
*
*	Class factory that manufactures <I>TunnelSocket</I> objects.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 2/6/2002
*
*********************************************************************************************/

public class TunnelSocketFactory extends SocketFactory
{
	/*************************************************************************************
	*
	*	
	*
	*************************************************************************************/

	/** Constructor - create an HTTP tunnelling socket factory.
		@param strProxyAddress <I>String</I> name (IP address) of the proxy server.
		@param nProxyPort Port number of the proxy server.
	*/
	public TunnelSocketFactory(String strProxyAddress, int nProxyPort)
		throws UnknownHostException
	{
		setProxyAddress(strProxyAddress);
		setProxyPort(nProxyPort);
	}

	/** Constructor - create an HTTP tunnelling socket factory.
		@param pProxyAddress <I>InetAddress</I> object of the proxy server.
		@param nProxyPort Port number of the proxy servert.
	*/
	public TunnelSocketFactory(InetAddress pProxyAddress, int nProxyPort)
	{
		setProxyAddress(pProxyAddress);
		setProxyPort(nProxyPort);
	}

	/*************************************************************************************
	*
	*	Required methods: SocketFactory
	*
	*************************************************************************************/

	/** Action method - creates a <I>TunnelSocket</I> object.
		@param pAddress Destination address.
		@param nPort Destination port.
	*/
	public Socket createSocket(InetAddress pAddress, int nPort)
		throws IOException
	{
		return createSocket(pAddress.getHostAddress(), nPort);
	}

	/** Action method - creates a <I>TunnelSocket</I> object.
		@param pAddress Destination address.
		@param nPort Destination port.
		@param pLocalAddress <I>InetAddress</I> object of the local Address's address
			to bind to.
		@param nLocalPort Port number of the local Address's port to bind to.
	*/
	public Socket createSocket(InetAddress pAddress, int nPort,
		InetAddress pLocalAddress, int nLocalPort)
		throws IOException
	{
		return createSocket(pAddress.getHostAddress(), nPort, pLocalAddress, nLocalPort);
	}

	/** Action method - creates a <I>TunnelSocket</I> object.
		@param strAddress Destination address.
		@param nPort Destination port.
	*/
	public Socket createSocket(String strAddress, int nPort)
		throws IOException, UnknownHostException
	{
		return new TunnelSocket(m_ProxyAddress, m_nProxyPort,
			strAddress, nPort);
	}

	/** Action method - creates a <I>TunnelSocket</I> object.
		@param strAddress Destination address.
		@param nPort Destination port.
		@param pLocalAddress <I>InetAddress</I> object of the local Address's address
			to bind to.
		@param nLocalPort Port number of the local Address's port to bind to.
	*/
	public Socket createSocket(String strAddress, int nPort,
		InetAddress pLocalAddress, int nLocalPort)
		throws IOException, UnknownHostException
	{
		return new TunnelSocket(m_ProxyAddress, m_nProxyPort,
			pLocalAddress, nLocalPort,
			strAddress, nPort);
	}

	/*************************************************************************************
	*
	*	Accessor methods
	*
	*************************************************************************************/

	/** Accessor method - gets the proxy server Address. */
	public InetAddress getProxyAddress() { return m_ProxyAddress; }

	/** Accessor method - gets the proxy server port. */
	public int getProxyPort() { return m_nProxyPort; }

	/*************************************************************************************
	*
	*	Mutator methods
	*
	*************************************************************************************/

	/** Mutator method - sets the proxy server Address. */
	public void setProxyAddress(InetAddress pNewValue) { m_ProxyAddress = pNewValue; }

	/** Mutator method - sets the proxy server Address by name or IP address. */
	public void setProxyAddress(String strNewValue)
		throws UnknownHostException
	{
		setProxyAddress(InetAddress.getByName(strNewValue));
	}

	/** Mutator method - sets the proxy server port number. */
	public void setProxyPort(int nNewValue) { m_nProxyPort = nNewValue; }

	/*************************************************************************************
	*
	*	Member variables
	*
	*************************************************************************************/

	/** Member variable - contains a reference to the proxy server Address. */
	private InetAddress m_ProxyAddress = null;

	/** Member variable - contains a reference to the proxy server port number. */
	private int m_nProxyPort = 0;
}

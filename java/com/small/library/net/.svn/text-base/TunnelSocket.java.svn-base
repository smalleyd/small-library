package com.small.library.net;

import java.io.*;
import java.net.*;

import com.small.library.http.*;

/*****************************************************************************************
*
*	Extends the <I>java.net.Socket</I> class to establish the socket as
*	a proxy client socket. Uses HTTP tunnelling.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 7/20/2001
*
****************************************************************************************/

public class TunnelSocket extends Socket
{
	/********************************************************************************
	*
	*	Constructors
	*
	********************************************************************************/

	/** Constructor - create an HTTP tunnelling socket.
		@param pAddress <I>InetAddress</I> object of the proxy server.
		@param nPort Port number of the proxy server's port.
		@param strDestinationHost Name of IP address of the ultimate destination
			from the proxy server.
		@param nDestinationPort Port number of the ultimate destination
			from the proxy server.
	*/
	public TunnelSocket(InetAddress pAddress, int nPort,
		String strDestinationHost, int nDestinationPort)
		throws TunnelSocketException, IOException
	{
		super(pAddress, nPort);
		initialize(strDestinationHost, nDestinationPort);
	}

	/** Constructor - create an HTTP tunnelling socket.
		@param pAddress <I>InetAddress</I> object of the proxy server.
		@param nPort Port number of the proxy server's port.
		@param pLocalAddress <I>InetAddress</I> object of the local host's address
			to bind to.
		@param nLocalPort Port number of the local host's port to bind to.
		@param strDestinationHost Name of IP address of the ultimate destination
			from the proxy server.
		@param nDestinationPort Port number of the ultimate destination
			from the proxy server.
	*/
	public TunnelSocket(InetAddress pAddress, int nPort, InetAddress pLocalAddress, int nLocalPort,
		String strDestinationHost, int nDestinationPort)
		throws TunnelSocketException, IOException
	{
		super(pAddress, nPort, pLocalAddress, nLocalPort);
		initialize(strDestinationHost, nDestinationPort);
	}

	/** Constructor - create an HTTP tunnelling socket.
		@param strHost Name or IP address of the proxy server.
		@param nPort Port number of the proxy server's port.
		@param strDestinationHost Name of IP address of the ultimate destination
			from the proxy server.
		@param nDestinationPort Port number of the ultimate destination
			from the proxy server.
	*/
	public TunnelSocket(String strHost, int nPort,
		String strDestinationHost, int nDestinationPort)
		throws UnknownHostException, TunnelSocketException, IOException
	{
		super(strHost, nPort);
		initialize(strDestinationHost, nDestinationPort);
	}

	/** Constructor - create an HTTP tunnelling socket.
		@param strHost Name or IP address of the proxy server.
		@param nPort Port number of the proxy server's port.
		@param pLocalAddress <I>InetAddress</I> object of the local host's address
			to bind to.
		@param nLocalPort Port number of the local host's port to bind to.
		@param strDestinationHost Name of IP address of the ultimate destination
			from the proxy server.
		@param nDestinationPort Port number of the ultimate destination
			from the proxy server.
	*/
	public TunnelSocket(String strHost, int nPort, InetAddress pLocalAddress, int nLocalPort,
		String strDestinationHost, int nDestinationPort)
		throws UnknownHostException, TunnelSocketException, IOException
	{
		super(strHost, nPort, pLocalAddress, nLocalPort);
		initialize(strDestinationHost, nDestinationPort);
	}

	/********************************************************************************
	*
	*	Accessor methods
	*
	********************************************************************************/

	/** Accessor method - gets the Destination Host address. */
	public String getDestinationHost() { return m_strDestinationHost; }

	/** Accessor method - gets the Destination Port. */
	public int getDestinationPort() { return m_nDestinationPort; }

	/********************************************************************************
	*
	*	Helper methods
	*
	********************************************************************************/

	/** Helper method - Initializes member variables and the proxy server.
		@param strDestinationHost Parameter to the Destination Host name or IP address.
		@param nDestinationPort Parameter to the Destination Host port.
	*/
	private void initialize(String strDestinationHost,
		int nDestinationPort)
		throws TunnelSocketException, IOException
	{
		m_strDestinationHost = strDestinationHost;
		m_nDestinationPort = nDestinationPort;

		PrintWriter pOut = new PrintWriter(new OutputStreamWriter(getOutputStream()));

		pOut.println("CONNECT " + strDestinationHost + ":" + nDestinationPort + " HTTP/1.0");
		pOut.println("User-Agent: sun.net.www.protocol.http.HttpURLConnection.userAgent");
		pOut.println();
		pOut.flush();

		BufferedReader pIn = new BufferedReader(new InputStreamReader(getInputStream()));
		String strLine = pIn.readLine();
		String strFirstLine = strLine;
		String strReply = strLine;

		// Was any data return?
		if (null == strFirstLine)
			throw new TunnelSocketException(getInetAddress().getHostName(),
				getPort(), strDestinationHost, nDestinationPort,
				"No data was returned from the proxy server.");

		// Get the full reply.
		while (null != (strLine = pIn.readLine()))
		{
			// Exit at the first blank line.
			if (0 == strLine.length())
				break;

			strReply+= strLine + "\n";
		}

		// Parse the status line.
		try
		{
			HttpResponseStatus pResponse = new HttpResponseStatus(strFirstLine);

			// Was an error returned?
			if (pResponse.isError())
				throw new TunnelSocketException(getInetAddress().getHostName(),
					getPort(), strDestinationHost, nDestinationPort,
					strReply);
		}

		catch (HttpException pEx)
		{
			throw new TunnelSocketException(getInetAddress().getHostName(),
				getPort(), strDestinationHost, nDestinationPort,
				pEx.getMessage());
		}
	}

	/********************************************************************************
	*
	*	Member variables
	*
	********************************************************************************/

	/** Member variable - contains the Destination Host. */
	private String m_strDestinationHost = null;

	/** Member variable - contains the Destination Port. */
	private int m_nDestinationPort = 0;
}

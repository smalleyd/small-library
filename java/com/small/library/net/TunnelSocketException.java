package com.small.library.net;

import java.net.SocketException;

/**********************************************************************************
*
*	Extends the <I>java.net.SocketException</I> class to define a tunnel
*	socket exception.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 7/20/2001
*
**********************************************************************************/

public class TunnelSocketException extends SocketException
{
	/** Constructor - creates an exception based on a <I>String<I> message.
		@param strMessage Exception message.
	*/
	public TunnelSocketException(String strMessage) { super(strMessage); }

	/** Constructor - builds the error message based on the proxy and
	    destination information.
		@param strProxyHost Proxy host name or IP address.
		@param nProxyPort Proxy port number.
		@param strDestinationHost Destination host name or IP address.
		@param nDestinationPort Destination port number.
		@param strReply The reply message from the proxy.
	*/
	public TunnelSocketException(String strProxyHost, int nProxyPort,
		String strDestinationHost, int nDestinationPort, String strReply)
	{
		super("Unable to tunnel to proxy server at " +
			strProxyHost + ", port " + nProxyPort +
			" for the destination \"" + strDestinationHost +
			":" + nDestinationPort +
			"\". Proxy returned \"" + strReply + "\"");
	}
}

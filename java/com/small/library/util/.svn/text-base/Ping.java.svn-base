package com.small.library.util;

import java.io.IOException;
import java.net.*;

/****************************************************************************************
*
*	The Ping object measures network latency by sending a packet
*	to the UDP Echo Port (Port 7) and timing how long it takes.
*	We use this port instead of ICMP because I would have to
*	use native methods in order to produce ICMP packets.
*
****************************************************************************************/

public class Ping implements Runnable
{
	/********************************************************************************
	*
	*	Constants
	*
	********************************************************************************/

	/** Constant - port number for ECHO packages. */
	public static final int PORT_ECHO = 7;

	/** Constant - maximum time to wait for a response in milliseconds. */
	public static final int MAX_PING_TIME = 3000;

	/** Constant - time to sleep during polling in milliseconds. */
	public static final int POLL_INTERVAL = 100;

	/********************************************************************************
	*
	*	Constructors/Destructor
	*
	********************************************************************************/

	public Ping(String strAddress) throws UnknownHostException
	{ this(InetAddress.getByName(strAddress)); }

	public Ping(InetAddress pAddress)
	{ m_Address = pAddress; }

	/********************************************************************************
	*
	*	Main methods
	*
	********************************************************************************/

	/** If needed, start a listener thread to notice the reply.
	    then we send out a brief message to the echo port.
	    Since the Java thread model does not allow one thread to break
	    another one out of a wait, we sleep for brief intervals, waking
	    up periodically to see if the reply has come in yet.
	*/
	public long getResponseTime()
		throws SocketException, IOException
	{
		byte[] nMsg = new byte[1];
		nMsg[0] = ++m_nPacketNumber;

		m_lTimeMeasured = -1L;

		if (null == m_Socket)
			m_Socket = new DatagramSocket(PORT_ECHO);

		m_PingListenThread = new Thread(this);
		m_PingListenThread.start();

		DatagramPacket pPacket = new DatagramPacket(nMsg, nMsg.length, m_Address, PORT_ECHO);
		m_lSendTime = System.currentTimeMillis();
		long lTimeLimit = m_lSendTime + MAX_PING_TIME;

		m_Socket.send(pPacket);

		while (System.currentTimeMillis() < lTimeLimit)
		{
			try { Thread.sleep(POLL_INTERVAL); }
			catch (InterruptedException pEx) { return m_lTimeMeasured; }

			// Reply information has been set.
			if (-1L < m_lTimeMeasured)
				return m_lTimeMeasured;
		}

		// Stop the execution.
		stop();

		// Return of -1 indicates error.
		return m_lTimeMeasured;
	}

	/** Run method for the listener thread. */
	public void run()
	{
		byte[] nBuffer = new byte[1];
		DatagramPacket pReply = new DatagramPacket(nBuffer, nBuffer.length);

		try
		{
			while (null != m_Socket)
			{
				try {
				System.out.println("run");

				m_Socket.receive(pReply);

				System.out.println(nBuffer[0]);

				if (nBuffer[0] == m_nPacketNumber)
				{
					m_lTimeMeasured = System.currentTimeMillis() - m_lSendTime;
					m_PingListenThread = null;
					return;
				}

				}

				catch (IOException pEx) { System.out.println(pEx.getMessage()); }
			}
		}

		catch (Exception pEx) { pEx.printStackTrace(); m_PingListenThread = null; }
	}

	/** Clean up any dangling listener thread and release the socket. */
	public void stop()
	{
		if(m_PingListenThread != null)
		{
			m_PingListenThread.interrupt();
			m_PingListenThread = null;
		}

		m_Socket.close();
		m_Socket = null;
	}

	/********************************************************************************
	*
	*	Member variables
	*
	********************************************************************************/

	private DatagramSocket m_Socket = null;
	private InetAddress m_Address = null;
	private long m_lSendTime = 0L;
	private long m_lTimeMeasured = 0L;
	private Thread m_PingListenThread = null;
	private byte m_nPacketNumber = 0;

	/********************************************************************************
	*
	*	Driver methods
	*
	********************************************************************************/

	/** Driver method. */
	public static void main(String strArgs[])
	{
		try
		{
			Ping pPing = new Ping(strArgs[0]);

			System.out.println("Response Time: " + pPing.getResponseTime() + " milliseconds");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}

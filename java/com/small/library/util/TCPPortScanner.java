package com.small.library.util;

import java.io.*;
import java.net.*;

public class TCPPortScanner
{
	public static void main(String strArgs[])
	{
		String strHost = null;
		InetAddress pAddress = null;
		PrintWriter pSuccess = null;
		PrintWriter pFailure = null;

		if (0 == strArgs.length)
			strHost = "localhost";
		else
			strHost = strArgs[0];

		try { pAddress = InetAddress.getByName(strHost); }
		catch (UnknownHostException pEx) { pEx.printStackTrace(); return; }

		try
		{
			pSuccess = new PrintWriter(new FileOutputStream(strHost + ".success"));
			pFailure = new PrintWriter(new FileOutputStream(strHost + ".failure"));
		}

		catch (FileNotFoundException pEx) { pEx.printStackTrace(); return; }

		for (int i = 1; i <= Integer.MAX_VALUE; i++)
		{
			try
			{
				Socket pSocket = new Socket(pAddress, i);
				System.out.println("Server found at port: " + i);
				pSuccess.println("Server found at port: " + i);
				pSuccess.flush();
			}

			catch (Exception pEx)
			{ pFailure.println("Could not find server at: " + i); pFailure.flush(); }
		}

		pSuccess.close();
		pFailure.close();
	}
}

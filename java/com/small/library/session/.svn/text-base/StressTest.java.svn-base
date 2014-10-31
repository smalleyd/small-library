package com.small.library.session;

/*****************************************************************************************
*
*	Performs a simple stress test on a remote session.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 12/13/2000
*
*****************************************************************************************/

public class StressTest
{
	public static void main(String strArgs[])
	{
		try
		{
			if (0 == strArgs.length)
			{
				System.err.println("Please supply the name of properties file.");
				System.exit(0);
				return;
			}

			String strURL = RemoteSessionClient.getURL(strArgs[0]);

			RemoteSessionManager pManager = RemoteSessionClient.getSessionManager(strURL);

			for (int i = 0; i < 20000; i++)
			{
				// Create a session that lasts for one minute.
				RemoteSession pSession = pManager.createSession(1);

				// Sleep for alittle while.
				// Thread.sleep(100);
			}
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}

package com.small.library.session;

import java.util.*;

/********************************************************************************************
*
*	Monitors the sessions currently being managed, determines if any have expired, and
*	clears expired sessions.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 12/4/2000
*
*********************************************************************************************/

class RemoteSessionGarbageCollector implements Runnable
{
	/*************************************************************************************
	*
	*	Constructors/Destructor
	*
	*************************************************************************************/

	/** Constructor - accepts a reference to the Remote Session Manager. */
	RemoteSessionGarbageCollector(RemoteSessionManagerImpl pManager)
	{ m_Manager = pManager; }

	/*************************************************************************************
	*
	*	Action methods
	*
	*************************************************************************************/

	/** Action method - monitors the sessions currently being managed. */
	public void run()
	{
		// Run for duration of application.
		while (true)
		{
			Date pNow = new Date();
			Map pMapSessions = m_Manager.getSessions();

			synchronized (pMapSessions)
			{
				Collection pColSessions = pMapSessions.values();
				Iterator pSessions = pColSessions.iterator();

				// Must build up an array list of sessions to destroy. Cannot destroy
				// sessions while also using the iterator that the Map depends on.
				ArrayList pSessionsToDestroy = new ArrayList();

				try
				{
					System.out.println("\nBeginning Garbage Collecting (" + pColSessions.size() + ") ...");

					while (pSessions.hasNext())
					{
						RemoteSessionImpl pSession = (RemoteSessionImpl) pSessions.next();

						if (pSession.hasExpired(pNow))
							pSessionsToDestroy.add(pSession);
					}

					pSessions = null;

					System.out.println("\tGarbage Collecting " + pSessionsToDestroy.size() + " sessions ...");

					for (int i = 0; i < pSessionsToDestroy.size(); i++)
						m_Manager.destroySession((RemoteSession) pSessionsToDestroy.get(i));

					System.out.println("Garbage Collection ended (" + pColSessions.size() + ").");
				}

				catch (Exception pEx)
				{
					System.out.println("\n\n****** Remote Session Garbage Collection Error ******\n");
					pEx.printStackTrace();
				}
			}

			// Now sleep for a minute.
			try { Thread.sleep(60000); }
			catch (Exception pEx) { pEx.printStackTrace(); }
		}
	}

	/*************************************************************************************
	*
	*	Member variables
	*
	*************************************************************************************/

	/** Member variable - Remote Session Manager. */
	private RemoteSessionManagerImpl m_Manager = null;
}

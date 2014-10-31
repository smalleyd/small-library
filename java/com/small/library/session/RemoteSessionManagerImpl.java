package com.small.library.session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/***********************************************************************************************
*
*	Implementation of the Remote Session Manager.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 11/25/2000
*
************************************************************************************************/

public class RemoteSessionManagerImpl extends UnicastRemoteObject implements RemoteSessionManager
{
	/****************************************************************************************
	*
	*	Constants
	*
	****************************************************************************************/

	/** Constant - default maximum inactive interval. */
	public static final int MAX_INACTIVE_INTERVAL_DEFAULT = 20;

	/****************************************************************************************
	*
	*	Constructors/Destructor
	*
	****************************************************************************************/

	/** Constructor - default. */
	public RemoteSessionManagerImpl() throws RemoteException { this(MAX_INACTIVE_INTERVAL_DEFAULT); }

	/** Constructor - accepts the maximum inactive interval in minutes.
		@param nMaxInactiveInterval Number of inactive minutes before a session is destroyed.
	*/
	public RemoteSessionManagerImpl(int nMaxInactiveInterval) throws RemoteException
	{
		m_nMaxInactiveInterval = nMaxInactiveInterval;

		// Start the remote session garbage collector.
		Thread pGC = new Thread(new RemoteSessionGarbageCollector(this));

		pGC.setPriority(Thread.MIN_PRIORITY);
		pGC.setDaemon(true);
		pGC.start();
	}

	/****************************************************************************************
	*
	*	Action methods
	*
	****************************************************************************************/

	/** Retrieves a new Remote Session. */
	public RemoteSession createSession() throws RemoteException
	{ return createSession(m_nMaxInactiveInterval); }

	/** Retrieves a Remote Session if available for returns a new Remote Session. */
	public RemoteSession getSession(String strID) throws RemoteException
	{ return getSession(strID, m_nMaxInactiveInterval); }

	/** Creates a new Remote Session. */
	public RemoteSession createSession(int nMaxInactiveInterval) throws RemoteException
	{
		RemoteSession pSession = new RemoteSessionImpl(nMaxInactiveInterval);

		// Only one thread should update the Map of remote session objects.
		synchronized (m_Sessions) { m_Sessions.put(pSession.toString(), pSession); }

		return pSession;
	}

	/** Retrieves a Remote Session if available or returns a new Remote Session. */
	public RemoteSession getSession(String strID, int nMaxInactiveInterval)
		throws RemoteException
	{
		// Get the session indicated by the key.
		RemoteSessionImpl pSession = null;

		// Only one thread should update the Map of remote session objects.
		// While this is only a read, a write may be going on at the same time.
		synchronized (m_Sessions) { pSession = (RemoteSessionImpl) m_Sessions.get(strID); }

		// If the session is found, mark it as NOT new, and return it.
		if (null != pSession)
		{
			pSession.isNew(false);
			return pSession;
		}

		// Otherwise, create a new remote session.
		return createSession(nMaxInactiveInterval);
	}

	/** Creates a Remote Session object with a specified ID. Used for application caches that need
	    to maintain a single cache across multiple servers.
		@param strID <I>String</I> object.
	*/
	public RemoteSession createSession(String strID)
		throws RemoteException
	{
		RemoteSession pSession = new RemoteSessionImpl(strID);

		// Only one thread should update the Map of remote session objects.
		synchronized (m_Sessions) { m_Sessions.put(pSession.toString(), pSession); }

		return pSession;
	}

	/** Creates a Remote Session object with a specified ID. Used for application caches that need
	    to maintain a single cache across multiple servers.
		@param strID <I>String</I> object.
		@param nMaxInactiveInterval Integer value in minutes.
	*/
	public RemoteSession createSession(String strID, int nMaxInactiveInterval)
		throws RemoteException
	{
		RemoteSession pSession = new RemoteSessionImpl(strID, nMaxInactiveInterval);

		// Only one thread should update the Map of remote session objects.
		synchronized (m_Sessions) { m_Sessions.put(pSession.toString(), pSession); }

		return pSession;
	}

	/** Destroys the Remote Session object specified by the session ID.
		@param strID <I>String</I> representation of the session ID.
	*/
	public void destroySession(String strID) throws RemoteException
	{ synchronized (m_Sessions) { m_Sessions.remove(strID); } }

	/** Destroys the Remote Session object.
		@param pObject The Remote Session object to destroy.
	*/
	public void destroySession(RemoteSession pObject) throws RemoteException
	{ destroySession(pObject.toString()); }

	/****************************************************************************************
	*
	*	Accessor methods
	*
	****************************************************************************************/

	/** Accessor - gets the <I>Map</I> of sessions. For use by the remote session garbage
	    collector only.
	*/
	Map getSessions() { return m_Sessions; }

	/****************************************************************************************
	*
	*	Member variables
	*
	****************************************************************************************/

	private Map m_Sessions = new HashMap();
	private int m_nMaxInactiveInterval = MAX_INACTIVE_INTERVAL_DEFAULT;
}

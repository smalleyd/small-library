package com.small.library.session;

import java.rmi.*;
import java.util.*;

/*******************************************************************************************
*
*	Interface to the Remote Session Manager.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 11/25/2000
*
*******************************************************************************************/

public interface RemoteSessionManager extends Remote
{
	/** Retrieves a new Remote Session. */
	public RemoteSession createSession() throws RemoteException;

	/** Retrieves a Remote Session if available for returns a new Remote Session.
		@param strID <I>String</I> representation of the session ID.
	*/
	public RemoteSession getSession(String strID) throws RemoteException;

	/** Retrieves a new Remote Session. */
	public RemoteSession createSession(int nMaxInactiveInterval) throws RemoteException;

	/** Retrieves a Remote Session if available or returns a new Remote Session. */
	public RemoteSession getSession(String strID, int nMaxInactiveInterval)
		throws RemoteException;

	/** Destroys the Remote Session object specified by the session ID.
		@param strID <I>String</I> representation of the session ID.
	*/
	public void destroySession(String strID) throws RemoteException;

	/** Destroys the Remote Session object.
		@param pObject The Remote Session object to destroy.
	*/
	public void destroySession(RemoteSession pObject) throws RemoteException;
}

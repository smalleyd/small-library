package com.small.library.session;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*******************************************************************************************
*
*	Interface for the Remote Session.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 11/25/2000
*
*******************************************************************************************/

public interface RemoteSession extends Remote
{
	/** Accessor - gets the creation time. */
	public long getCreationTime() throws RemoteException;

	/** Accessor - gets the identity of the object. */
	public String getId() throws RemoteException;

	/** Accessor - gets the last accessed time. */
	public long getLastAccessedTime() throws RemoteException;

	/** Accessor - gets the maximum time a session is inactive before recycling. */
	public int getMaxInactiveInterval() throws RemoteException;

	/** Accessor - gets the value represented by the <I>String</I> key. */
	public Object getValue(String strKey) throws RemoteException;

	/** Accessor - gets the key names. */
	public String[] getValueNames() throws RemoteException;

	/** Accessor - indicates whether the session object is new. */
	public boolean isNew() throws RemoteException;

	/***********************************************************************************
	*
	*	Mutator methods
	*
	***********************************************************************************/

	/** Mutator - invalidates the collection of session data. */
	public void invalidate() throws RemoteException;

	/** Mutator - adds session data to the collection. */
	public void putValue(String strName, Object pValue) throws RemoteException;

	/** Mutator - removes session data from the collection of session data. */
	public void removeValue(String strName) throws RemoteException;

	/** Mutator - sets the maximum inactive interval. */
	public void setMaxInactiveInterval(int nNewValue) throws RemoteException;
}

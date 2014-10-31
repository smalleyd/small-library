package com.small.library.session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/*******************************************************************************************
*
*	Implementation of the Remote Session.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 11/25/2000
*
*******************************************************************************************/

public class RemoteSessionImpl extends UnicastRemoteObject implements RemoteSession
{
	/***********************************************************************************
	*
	*	Constants
	*
	***********************************************************************************/

	/** Constant - No session timeout value. */
	public static final int MAX_INACTIVE_INTERVAL_NONE = 0;

	/** Constant - maximum inactive interval conversion. */
	private static final long MAX_INACTIVE_INTERVAL_CONVERSION = 60000L;

	/***********************************************************************************
	*
	*	Constructors/Destructor
	*
	***********************************************************************************/

	/** Constructor - default. */
	private RemoteSessionImpl() throws RemoteException
	{
		super();

		m_bIsNew = true;
		m_dteCreationTime = new Date();
		m_lCreationTime = m_dteCreationTime.getTime();
		m_dteLastAccessedTime = m_dteCreationTime;
		m_Data = new HashMap();

		m_strID = "" + m_lCreationTime;
	}

	/** Constructor - accepts the maximum inactive interval.
		@param nInterval Integer value in minutes.
	*/
	public RemoteSessionImpl(int nInterval) throws RemoteException
	{ this(); setMaxInactiveInterval(nInterval); }

	/** Constructor - accepts <I>String</I> representing the ID value to used for this
	    instance. Used instead of the creation date.
		@param strID <I>String</I> object.
	*/
	RemoteSessionImpl(String strID) throws RemoteException
	{ this(strID, MAX_INACTIVE_INTERVAL_NONE); }

	/** Constructor - accepts <I>String</I> representing the ID value to used for this
	    instance. Used instead of the creation date.
		@param strID <I>String</I> object.
		@param nInterval Integer value in minutes.
	*/
	RemoteSessionImpl(String strID, int nInterval) throws RemoteException
	{ this(); m_strID = strID; setMaxInactiveInterval(nInterval); }

	/***********************************************************************************
	*
	*	Accessor methods
	*
	***********************************************************************************/

	/** Accessor - gets the creation time. */
	public long getCreationTime() { return m_lCreationTime; }

	/** Accessor - gets the identity of the object. */
	public String getId() { return toString(); }

	/** Accessor - gets the last accessed time. */
	public long getLastAccessedTime() { return m_dteLastAccessedTime.getTime(); }

	/** Accessor - gets the maximum time a session is inactive before recycling. */
	public int getMaxInactiveInterval() { return (int) (m_lMaxInactiveInterval / MAX_INACTIVE_INTERVAL_CONVERSION); }

	/** Accessor - gets the value represented by the <I>String</I> key. */
	public Object getValue(String strKey) { return m_Data.get(strKey); }

	/** Accessor - gets the key names. */
	public String[] getValueNames()
	{
		if (m_Data.isEmpty())
			return null;

		String[] strKeys = new String[m_Data.size()];

		Iterator pKeys = m_Data.keySet().iterator();

		for (int i = 0; pKeys.hasNext(); i++)
			strKeys[i] = pKeys.next().toString();

		return strKeys;
	}

	/** Accessor - indicates whether the session object is new. */
	public boolean isNew() { return m_bIsNew; }

	/** Accessor - <I>String</I> representation. */
	public String toString() { return m_strID; }

	/** Accessor - Hash Code. */
	public int hashCode() { return m_dteCreationTime.hashCode(); }

	/** Accessor - determines equality of the same class. */
	public boolean equals(Object pValue)
	{
		if ((null == pValue) || (!(pValue instanceof RemoteSessionImpl)))
			return false;

		return equals((RemoteSessionImpl) pValue);
	}

	/** Accessor - determines equality of the same class. */
	public boolean equals(RemoteSessionImpl pValue)
	{
		if (null == pValue)
			return false;

		return m_strID.equals(pValue.m_strID);
	}

	/** Accessor - indicates whether is older than the Maximum Inactive Interval.
	    Called by the Remote Session Garbage Collector.
		@param pNow The current date and time.
	*/
	boolean hasExpired(Date pNow)
	{
		if (0L == m_lMaxInactiveInterval)
			return false;

		return (pNow.getTime() > (m_dteLastAccessedTime.getTime() + m_lMaxInactiveInterval)) ? true : false;
	}

	/***********************************************************************************
	*
	*	Mutator methods
	*
	***********************************************************************************/

	/** Mutator - indicates whether the session object is new. */
	void isNew(boolean bNewValue)
	{
		m_bIsNew = bNewValue;
		setLastAccessedTime(new Date());
	}

	/** Mutator - invalidates the collection of session data. */
	public void invalidate()
	{
		m_Data.clear();
		m_Data = new HashMap();
	}

	/** Mutator - adds session data to the collection. */
	public void putValue(String strName, Object pValue)
	{ m_Data.put(strName, pValue); }

	/** Mutator - removes session data from the collection of session data. */
	public void removeValue(String strName)
	{ m_Data.remove(strName); }

	/** Mutator - sets the last accessed time. */
	void setLastAccessedTime(Date pNewValue) { m_dteLastAccessedTime = pNewValue; }

	/** Mutator - sets the maximum inactive interval. */
	public void setMaxInactiveInterval(int nNewValue) { m_lMaxInactiveInterval = (MAX_INACTIVE_INTERVAL_CONVERSION * (long) nNewValue); }

	/***********************************************************************************
	*
	*	Member variables
	*
	***********************************************************************************/

	/** Member variable - Is the object new. */
	private boolean m_bIsNew = false;

	/** Member variable - Creation time. */
	private Date m_dteCreationTime = null;

	/** Member variable - Creation time in milliseconds. */
	private long m_lCreationTime = 0;

	/** Member variable - Last accessed time. */
	private Date m_dteLastAccessedTime = null;

	/** Member variable - Map of session values. */
	private Map m_Data = null;

	/** Member variable - Maximum inactive interval. */
	private long m_lMaxInactiveInterval = 0L;

	/** Member variable - Actual ID value of the object. */
	private String m_strID = null;
}

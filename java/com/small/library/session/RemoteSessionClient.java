package com.small.library.session;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.Properties;

import com.small.library.util.StringHelper;

/***************************************************************************************
*
*	Assists client side usage of the Remote Session framework.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 12/2/2000
*
***************************************************************************************/

public abstract class RemoteSessionClient
{
	/*******************************************************************************
	*
	*	Constants
	*
	*******************************************************************************/

	/** Constant - Property key for the remote object URL. */
	public static final String PROPERTY_REMOTE_OBJECT_URL = "remote.session.url";

	/*******************************************************************************
	*
	*	Static methods
	*
	*******************************************************************************/

	/** Gets the RMI URL for the Remote Session Manager.
		@param strFile Path and name of the properties file.
	*/
	public static String getURL(String strFile)
		throws IllegalArgumentException, FileNotFoundException, IOException
	{
		if (!StringHelper.isValid(strFile))
			throw new IllegalArgumentException("Remote Session Client's getURL: File not supplied.");

		return getURL(new File(strFile));
	}

	/** Gets the RMI URL for the Remote Session Manager.
		@param pFile <I>File</I> object that represents the properties file.
	*/
	public static String getURL(File pFile)
		throws FileNotFoundException, IOException
	{
		Properties pProperties = new Properties();

		pProperties.load(new FileInputStream(pFile));

		return pProperties.getProperty(PROPERTY_REMOTE_OBJECT_URL);
	}

	/** Gets the <I>RemoteSessionManager</I> object specified by the URL.
		@param strURL RMI URL for the <I>RemoteSessionManager</I>.
	*/
	public static RemoteSessionManager getSessionManager(String strURL)
		throws IllegalArgumentException, RemoteException, MalformedURLException, NotBoundException
	{
		if (!StringHelper.isValid(strURL))
			throw new IllegalArgumentException("The remote object URL (" + PROPERTY_REMOTE_OBJECT_URL + ") has not been supplied.");

		return (RemoteSessionManager) Naming.lookup(strURL);
	}

	/*******************************************************************************
	*
	*	Constructors/Destructor
	*
	*******************************************************************************/

	/** Constructor - accepts RMI URL to the remote session manager and current session's
	    HTTP Request object. Need the Request object to check cookies for the session ID.
		@param strURL RMI URL to the remote session manager.
		@param pRequest The HTTP Request object to get the session cookie information.
		@param pResponse The HTTP Response object to set the session cookie information.
	*/
	public RemoteSessionClient(String strURL)
		throws IllegalArgumentException, RemoteException, MalformedURLException, NotBoundException
	{ this(getSessionManager(strURL)); }

	/** Constructor - accepts RMI URL to the remote session manager and current session's
	    HTTP Request object. Need the Request object to check cookies for the session ID.
		@param pManager A reference to the <I>RemoteSessionManager</I> object.
		@param pRequest The HTTP Request object to get the session cookie information.
		@param pResponse The HTTP Response object to set the session cookie information.
	*/
	public RemoteSessionClient(RemoteSessionManager pManager)
		throws IllegalArgumentException
	{ m_Manager = pManager; }

	/*******************************************************************************
	*
	*	Action methods
	*
	*******************************************************************************/

	/** Retrieves a new Remote Session. */
	public RemoteSession createSession() throws RemoteException
	{ return initSession(m_Manager.createSession()); }

	/** Retrieves a Remote Session if available for returns a new Remote Session.
		@param strID <I>String</I> representation of the session ID.
	*/
	public RemoteSession getSession() throws RemoteException
	{
		String strID = getID();

		if (null == strID)
			return createSession();

		RemoteSession pSession = m_Manager.getSession(strID);

		if (pSession.isNew())
			return initSession(pSession);

		return pSession;
	}

	/** Retrieves a new Remote Session. */
	public RemoteSession createSession(int nMaxInactiveInterval) throws RemoteException
	{ return initSession(m_Manager.createSession(nMaxInactiveInterval)); }

	/** Retrieves a Remote Session if available or returns a new Remote Session. */
	public RemoteSession getSession(int nMaxInactiveInterval)
		throws RemoteException
	{
		String strID = getID();

		if (null == strID)
			return createSession(nMaxInactiveInterval);

		RemoteSession pSession = m_Manager.getSession(strID, nMaxInactiveInterval);

		if (pSession.isNew())
			return initSession(pSession);

		return pSession;
	}

	/** Destroys the Remote Session object specified by the session ID.
		@param strID <I>String</I> representation of the session ID.
	*/
	public void destroySession() throws RemoteException
	{
		String strID = getID();

		if (null == strID)
			return;

		m_Manager.destroySession(strID);
	}

	/*******************************************************************************
	*
	*	Abstract methods - placeholder methods for the derived classes
	*
	*******************************************************************************/

	/** Abstract method - returns current thread's session ID. */
	protected abstract String getID();

	/** Abstract method - child class specific initialization of session. */
	protected abstract RemoteSession initSession(RemoteSession pSession) throws RemoteException;

	/*******************************************************************************
	*
	*	Member variables
	*
	*******************************************************************************/

	/** Member variable. */
	private RemoteSessionManager m_Manager = null;
}

package com.small.library.session;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.Properties;

import javax.servlet.http.*;

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

public class ServletSessionClient extends RemoteSessionClient
{
	/*******************************************************************************
	*
	*	Constants
	*
	*******************************************************************************/

	/** Constant - Cookie name for session ID. */
	public static final String COOKIE_NAME_SESSION_ID = "com.small.library.session.remote.session.id";

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
	public ServletSessionClient(String strURL,
		HttpServletRequest pRequest, HttpServletResponse pResponse)
		throws IllegalArgumentException, RemoteException, MalformedURLException, NotBoundException
	{ super(strURL); m_Request = pRequest; m_Response = pResponse; }

	/** Constructor - accepts RMI URL to the remote session manager and current session's
	    HTTP Request object. Need the Request object to check cookies for the session ID.
		@param pManager A reference to the <I>RemoteSessionManager</I> object.
		@param pRequest The HTTP Request object to get the session cookie information.
		@param pResponse The HTTP Response object to set the session cookie information.
	*/
	public ServletSessionClient(RemoteSessionManager pManager,
		HttpServletRequest pRequest, HttpServletResponse pResponse)
		throws IllegalArgumentException
	{ super(pManager); m_Request = pRequest; m_Response = pResponse; }

	/*******************************************************************************
	*
	*	Abstract methods - implementation of parent method placeholders
	*
	*******************************************************************************/

	/** Abstract method - Returns the current session ID in use by the thread. */
	protected String getID()
	{
		Cookie pCookie = getCookie();

		if (null == pCookie)
			return null;

		return pCookie.getValue();
	}

	/** Abstract method - initiate a new session with Cookie. */
	protected RemoteSession initSession(RemoteSession pSession)
		throws RemoteException
	{
		Cookie pCookie = getCookie();

		if (null == pCookie)
			pCookie = new Cookie(COOKIE_NAME_SESSION_ID, pSession.getId());

		else
			pCookie.setValue(pSession.getId());

		m_Response.addCookie(pCookie);

		return pSession;
	}

	/*******************************************************************************
	*
	*	Helper methods
	*
	*******************************************************************************/

	/** Helper method - retrieves the cookie associated with the current session. */
	private Cookie getCookie()
	{
		if (null != m_Cookie)
			return m_Cookie;

		Cookie[] pCookies = m_Request.getCookies();

		for (int i = 0; i < pCookies.length; i++)
		{
			Cookie pCookie = pCookies[i];

			if (pCookie.getName().equals(COOKIE_NAME_SESSION_ID))
			{
				m_Cookie = pCookie;
				return m_Cookie;
			}
		}

		return null;
	}

	/*******************************************************************************
	*
	*	Member variables
	*
	*******************************************************************************/

	/** Member variable. */
	private HttpServletRequest m_Request = null;

	/** Member variable. */
	private HttpServletResponse m_Response = null;

	/** Member variable - Cookie associated with the current session. Kept <CODE>null</CODE>
	    until used.
	*/
	private Cookie m_Cookie = null;
}

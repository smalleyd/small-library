package com.small.library.ejb.util;

import java.rmi.RemoteException;

import javax.ejb.EJBLocalHome;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;

/***************************************************************************************
*
*	Base factory class for EJB home interfaces.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/18/2002
*
***************************************************************************************/

public abstract class AbstractHomeFactory
{
	/** Constructor - constructs a populated object.
		@param context JNDI <I>Context</I> object used to located
			the EJB Home interfaces. Should be direct parent
			to the EJB home interfaces.
	*/
	public AbstractHomeFactory(Context context)
	{
		m_Context = context;
	}

	/** Helper method - resolves a relative JNDI name into a home
	    interface.
		@param strRelativeName Relative name within the JNDI Context.
		@param homeInterface Class object used to "narrow" the RMI
			reference.
	*/
	protected Object getHomeInterface(String strRelativeName,
		Class homeInterface)
			throws NamingException
	{
		Object ref = m_Context.lookup(strRelativeName);
		return PortableRemoteObject.narrow(ref, homeInterface);
	}

	/** Helper method - resolves a relative JNDI name into a local home
	    interface.
		@param relativeName Relative name within the JNDI Context.
	*/
	public EJBLocalHome getLocalHomeInterface(String relativeName)
		throws NamingException
	{
		return (EJBLocalHome) m_Context.lookup(relativeName);
	}

	/** Accessor method - gets JNDI <I>Context</I> object used to located
	    the EJB home interfaces.
	*/
	public Context getContext() { return m_Context; }

	/** Member variable - JNDI <I>Context</I> object used to located
	    the EJB home interfaces.
	*/
	private Context m_Context = null;
}

package com.small.library.tomcat;

import java.util.*;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.naming.*;
import javax.naming.spi.ObjectFactory;

/***************************************************************************************
*
*	Tomcat resource factory that gets a JDO PersistenceManagerFactory based on
*	the resource parameters specified in the Tomcat server.xml file.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 12/16/2004
*
***************************************************************************************/

public class JdoResourceFactory implements ObjectFactory
{
	/** Constant - the factory resource parameter - to be ignored. */
	public static final String FACTORY = "factory";

	/** Static method - cache of factories. */
	public static final Map factories = new HashMap();

	/** ObjectFactory method - gets the PersistenceManagerFactory based on the
	    parameters specified.
	*/
	public Object getObjectInstance(Object parameters, Name name,
		Context context, Hashtable environment)
			throws NamingException
	{
		PersistenceManagerFactory item = (PersistenceManagerFactory)
			factories.get(name);

		if (null != item)
			return item;

		Reference ref = (Reference) parameters;
		Enumeration items = ref.getAll();
		Properties properties = new Properties();

		while (items.hasMoreElements())
		{
			RefAddr parameter = (RefAddr) items.nextElement();
			String type = parameter.getType();

			if (FACTORY.equals(type))
				continue;

			String value = (String) parameter.getContent();

			properties.setProperty(type, value);
		}

		item = JDOHelper.getPersistenceManagerFactory(properties);

		factories.put(name, item);

		return item;
	}
}


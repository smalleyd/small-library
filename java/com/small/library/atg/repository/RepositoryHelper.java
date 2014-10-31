package com.small.library.atg.repository;

import java.io.PrintStream;
import javax.naming.*;

import atg.repository.*;
import atg.repository.rql.*;

/*************************************************************************************
*
*	Helper class that exposes several static methods that wrap commonly
*	used Dynamo Repository functionality.
*
*	@author AMC\David Small
*	@version 1.0.0.0
*	@date 7/11/2002
*
*************************************************************************************/

public class RepositoryHelper
{
	/****************************************************************************
	*
	*	Factory methods
	*
	****************************************************************************/

	/** Factory method - gets a reference to the <I>Repository</I> object
	    through the supplied JNDI URL.
		@param strRepositoryURL JNDI URL to a Repository object.
		@return a <I>Repository</I> object.
	*/
	public static Repository getRepository(String strRepositoryURL)
		throws NamingException
	{
		// Get the initial context.
		Context context = new InitialContext();

		// Get the Repository.
		Repository repository = (Repository) context.lookup(strRepositoryURL);

		// Is it valid?
		if (null == repository)
			throw new NamingException("Could not find object referenced by \"" +
				strRepositoryURL + "\".");

		return repository;
	}

	/****************************************************************************
	*
	*	Query methods
	*
	****************************************************************************/

	/** Query method - queries the supplied Repository object with an
	    unconstrained view.
		@param strRepositoryURL JNDI URL to a Repository object.
		@param strItemDescriptor Item descriptor within the Repository
			to query.
		@return an array of <I>RepositoryItem</I> objects based on
			an unconstrained query on the supplied Repository.
	*/
	public static RepositoryItem[] queryRepository(String strRepositoryURL,
		String strItemDescriptor)
			throws RepositoryException, NamingException
	{
		// Get the repository items.
		return queryRepository(getRepository(strRepositoryURL), strItemDescriptor);
	}

	/** Query method - queries the supplied Repository object with an
	    unconstrained view.
		@param repository Repository object being queried.
		@param strItemDescriptor Item descriptor within the Repository
			to query.
		@return an array of <I>RepositoryItem</I> objects based on
			an unconstrained query on the supplied Repository.
	*/
	public static RepositoryItem[] queryRepository(Repository repository,
		String strItemDescriptor)
			throws RepositoryException
	{
		// Get the repository items.
		RepositoryItemDescriptor itemDescriptor =
			repository.getItemDescriptor(strItemDescriptor);
		RepositoryView view = itemDescriptor.getRepositoryView();
		Query query = view.getQueryBuilder().createUnconstrainedQuery();

		return view.executeQuery(query);
	}

	/** Query method - queries the supplied Repository object with an
	    RQL filter.
		@param strRepositoryURL JNDI URL to a Repository object.
		@param strItemDescriptor Item descriptor within the Repository
			to query.
		@param strRQL RQL query to apply to the Repository object.
		@param rqlParameters An array of objects supplied to the RQL
			query placeholders.
		@return an array of <I>RepositoryItem</I> objects based on
			an RQL query on the supplied Repository.
	*/
	public static RepositoryItem[] queryRepositoryByRQL(String strRepositoryURL,
		String strItemDescriptor, String strRQL, Object[] rqlParameters)
			throws RepositoryException, NamingException
	{
		// Get the repository items.
		return queryRepositoryByRQL(getRepository(strRepositoryURL),
			strItemDescriptor, strRQL, rqlParameters);
	}

	/** Query method - queries the supplied Repository object with an
	    RQL filter.
		@param repository Repository object being queried.
		@param strItemDescriptor Item descriptor within the Repository
			to query.
		@param strRQL RQL query to apply to the Repository object.
		@param rqlParameters An array of objects supplied to the RQL
			query placeholders.
		@return an array of <I>RepositoryItem</I> objects based on
			an RQL query on the supplied Repository.
	*/
	public static RepositoryItem[] queryRepositoryByRQL(Repository repository,
		String strItemDescriptor, String strRQL, Object[] rqlParameters)
			throws RepositoryException
	{
		// Get the repository items.
		RepositoryItemDescriptor itemDescriptor =
			repository.getItemDescriptor(strItemDescriptor);
		RepositoryView view = itemDescriptor.getRepositoryView();

		RqlStatement statement = RqlStatement.parseRqlStatement(strRQL);
		return statement.executeQuery(view, rqlParameters);
	}

	/****************************************************************************
	*
	*	Debug methods
	*
	****************************************************************************/

	/** Debug method - dumps the contents of a <I>RepositoryItem</I> to the
	    supplied <I>PrintStream</I>.
	    	@param repositoryItem The <I>RepositoryItem</I> to be dumped.
		@param printStream The <I>PrintStream</I> that outputs the dump.
	*/
	public static void dumpRepositoryItem(RepositoryItem repositoryItem,
		PrintStream printStream)
			throws RepositoryException
	{
		RepositoryItemDescriptor descriptor = repositoryItem.getItemDescriptor();
		String[] propertyNames = descriptor.getPropertyNames();
		String name = descriptor.getItemDescriptorName();

		printStream.println();
		printStream.println("Item Descriptor Dump of " + name);
		printStream.println();

		for (int i = 0; i < propertyNames.length; i++)
		{
			String propertyName = propertyNames[i];

			Object value = repositoryItem.getPropertyValue(propertyName);

			printStream.print("\t" + propertyName + " -> ");

			if (null == value)
				printStream.println("NULL");
			else
				printStream.println(value.toString());
		}

		printStream.println();
	}
}

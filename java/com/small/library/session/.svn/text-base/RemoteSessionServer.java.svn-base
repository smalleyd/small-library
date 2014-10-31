package com.small.library.session;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.Naming;
import java.util.*;

import com.small.library.util.StringHelper;

/***********************************************************************************************
*
*	Starts up the Remote Session Manager and registers the remote object in the RMI
*	registry.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 12/2/2000
*
************************************************************************************************/

public class RemoteSessionServer
{
	/****************************************************************************************
	*
	*	Constants
	*
	****************************************************************************************/

	/** Constant - property key for the name of the remote object in the RMI registry. */
	public static final String PROPERTY_REMOTE_OBJECT_NAME = "remote.session.name";

	/** Constant - property key for the max inactive interval in minutes. */
	public static final String PROPERTY_MAX_INACTIVE_INTERVAL = "remote.session.timeout";

	/****************************************************************************************
	*
	*	Entry method
	*
	****************************************************************************************/

	/** Entry method - Starts the main execution path. */
	public static void main(String strArgs[])
	{
		// Local variables.
		RemoteSessionManager pManager = null;

		// The first and only argument should contain the path and name of the properties file.
		if (0 == strArgs.length)
		{
			handleFatalError("Please provide the path and name of the properties file.");
			return;
		}

		printStatus("Loading properties...");

		// Does the properties file exist?
		File pFileProperties = new File(strArgs[0]);

		if (!pFileProperties.exists())
		{
			handleFatalError("The properties file \"" + strArgs[0] + "\" does not exist.");
			return;
		}

		// Load the properties.
		Properties pProperties = new Properties();

		try { pProperties.load(new FileInputStream(pFileProperties)); }

		catch (Exception pEx)
		{
			handleFatalError("Could not read properties file.\n\t" + pEx.getMessage());
			return;
		}

		// Get the properties.
		String strObjectName = pProperties.getProperty(PROPERTY_REMOTE_OBJECT_NAME);
		String strMaxInactiveInterval = pProperties.getProperty(PROPERTY_MAX_INACTIVE_INTERVAL);

		// Validate remote object name property.
		if (!StringHelper.isValid(strObjectName))
		{
			handleFatalError("The remote object name (" + PROPERTY_REMOTE_OBJECT_NAME + ") could not be found in the properties file.");
			return;
		}

		printStatus("Loaded Properties.\n");

		try
		{
			printStatus("Creating the Remote Session Manager...");

			// Is the max inactive interval value valid?
			if (StringHelper.isValid(strMaxInactiveInterval))
			{
				try
				{
					pManager = new RemoteSessionManagerImpl(Integer.parseInt(strMaxInactiveInterval));
					printStatus("Using " + strMaxInactiveInterval + " minutes as the session timeout.");
				}

				catch (NumberFormatException pEx) {}
			}

			else
				printStatus("Using default remote session timeout.");

			// If the session manager has not been instantiated yet, do so with the default constructor.
			if (null == pManager)
				pManager = new RemoteSessionManagerImpl();

			printStatus("Created the Remote Session Manager.\n");

			printStatus("Registering the Remote Session Manager with name \"" + strObjectName + "\".");

			// Register the remote object.
			Naming.rebind(strObjectName, pManager);

			printStatus("Registered the Remote Session Manager.");
		}

		catch (Exception pEx) { handleFatalError(pEx.getMessage()); }
	}

	/** Prints status information to the console.
		@param strStatus <I>String</I> that represents status.
	*/
	private static void printStatus(String strStatus)
	{ System.out.println(strStatus); }

	/** Handle a fatal error.
		@param strMessage The error message to display or log.
	*/
	private static void handleFatalError(String strMessage)
	{
		System.err.println(strMessage);
		System.exit(0);
	}
}

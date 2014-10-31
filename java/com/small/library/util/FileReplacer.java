package com.small.library.util;

import java.io.*;

/*****************************************************************************************
*
*	Class that provides a command line interface for performing string searches
*	and replacements on files.
*
*	<BR><BR>
*
*	<B>Command line arguments defined:</B>
*
*	<OL>
*		<LI>Directory or file name.</LI>
*		<LI>String value to be replaced.</LI>
*		<LI>String value to replace the old value. This value is optional. If omitted,
*		    a blank string is used as the replacement value.</LI>
*	</OL>
*
*	@author David Small
*	@version 1.0.0.0
*	@date 3/6/2002
*
******************************************************************************************/

public class FileReplacer
{
	/** Entry point to the command line interface. See the class description
	    for usage of the command line arguments.
	*/
	public static void main(String[] strArgs)
	{
		try
		{
			// Have enough arguments been specified.
			if (2 > strArgs.length)
			{
				System.out.println("Please specify at least a file name and string value to be replaced.");
				System.exit(1);
			}

			// Local variables - the command line arguments.
			String strFile = strArgs[0];
			String strOld = strArgs[1];
			String strNew = null;
			FileFilter pFileFilter = null;

			if (3 <= strArgs.length)
				strNew = strArgs[2];

			if (null == strNew)
				strNew = "";

			if (4 <= strArgs.length)
				pFileFilter = new FileFilterByExtensionWithDirectories(strArgs[3]);

			// Get the file.
			File pFile = new File(strFile);

			// Does it exist?
			if (!pFile.exists())
				throw new Exception("File, \"" + strFile + "\", does not exist.");

			// Perform the search and replace.
			FileHelper.replaceString(pFile, pFileFilter, strOld, strNew, System.out);
		}

		catch (Exception pEx) { pEx.printStackTrace(); System.exit(2); }
	}
}

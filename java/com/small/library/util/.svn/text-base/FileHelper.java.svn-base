package com.small.library.util;

import java.io.*;

/****************************************************************************************
*
*	Provides simple methods for handling File I/O.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 11/16/2000
*
****************************************************************************************/

public class FileHelper
{
	/********************************************************************************
	*
	*	File loading methods
	*
	********************************************************************************/

	/** Loads the entire contents of a text file into a <I>String</I>.
		@param strFileName Relative or absolute name of file to load.
		@return a <I>String</I> of the text file contents.
	*/
	public static String loadTextFile(String strFileName)
		throws FileNotFoundException, IOException
	{
		return loadTextFile(new File(strFileName));
	}

	/** Loads the entire contents of a text file into a <I>String</I>.
		@param file Relative or absolute reference to a file to load.
		@return a <I>String</I> of the text file contents.
	*/
	public static String loadTextFile(File file)
		throws FileNotFoundException, IOException
	{
		BufferedReader pIn = new BufferedReader(new FileReader(file));
		String strContents = "";
		String strLine = null;

		while (null != (strLine = pIn.readLine()))
			strContents+= strLine + "\r\n";

		pIn.close();

		if (0 == strContents.length())
			return null;

		return strContents;
	}

	/********************************************************************************
	*
	*	String replace methods
	*
	********************************************************************************/

	/** Replaces <CODE>oldValue</CODE> with <CODE>newValue</CODE> throughout the files specified by
	    <CODE>file</CODE> and/or <CODE>filter</CODE>. As the replacement procedes the
	    activities are written out to the <I>PrintStream</I> specified by
	    <CODE>pOut</CODE>.
	    <BR><BR>
	    <B>Note:</B> All directories returned by <CODE>filter</CODE> will be
	    searched recursively.
		@param file A file or directory used to specify the root of the replacement
			action. If <CODE>file</CODE> is a directory, the <CODE>filter</CODE>
			is used to get a list of files to perform the search and replace on.
			If <CODE>filter</CODE> is <CODE>null</CODE>, then all files and
			subdirectories are searched recursively.
		@param filter A class that implements the <I>java.io.FileFilter</I>
			interface. The filter can return files and subdirectories. All
			subdirectories will be search recursively.
		@param oldValue The <I>String</I> value to be replaced.
		@param newValue The <I>String</I> value used to replace <CODE>oldValue</CODE>.
		@param stream A <I>PrintStream</I> to activity information.
	*/
	public static void replaceString(File file, FileFilter filter,
		String oldValue, String newValue, PrintStream stream)
		throws FileNotFoundException, IOException
	{
		// If not a directory, perform a single replacement.
		if (!file.isDirectory())
		{
			replaceString(file, oldValue, newValue, stream);
			return;
		}

		/**** Otherwise, use filter to loop through contents of directory. ***/
		stream.println("Processing directory, " + file.getAbsolutePath() + ".");

		// Get child files.
		File[] files = null;

		if (null == filter)
			files = file.listFiles();
		else
			files = file.listFiles(filter);

		// Since the file is a directory the return value should not be null.

		// Loop through the files.
		for (int i = 0; i < files.length; i++)
		{
			File child = files[i];
			String name = child.getName();

			// Recurse through subdirectories.
			if (child.isDirectory())
			{
				// DO not process hidden directories.
				if (child.isHidden())
				{
					stream.println("Skipping " + child.getAbsolutePath() + " directory because it is hidden.");
					continue;
				}

				replaceString(child, filter, oldValue, newValue, stream);
			}

			// Disregard binary files.
			else if (name.endsWith(".pdf") || name.endsWith(".zip") || name.endsWith(".eps"))
			{
				stream.println("Skipping " + child.getAbsolutePath() + " file because it is not textual.");
				continue;
			}
			else
				replaceString(child, oldValue, newValue, stream);
		}
	}

	/** Replaces <CODE>oldValue</CODE> with <CODE>newValue</CODE> within the file specified by
	    <CODE>file</CODE>. As the replacement procedes the activities are written out to
	    the<I>PrintStream</I> specified by <CODE>pOut</CODE>.
		@param file A file to be searched and replaced.
		@param oldValue The <I>String</I> value to be replaced.
		@param newValue The <I>String</I> value used to replace <CODE>oldValue</CODE>.
		@param stream A <I>PrintStream</I> to activity information.
	*/
	public static void replaceString(File file, String oldValue, String newValue,
		PrintStream stream)
		throws FileNotFoundException, IOException
	{
		if (!file.isFile())
			return;

		String strContents = loadTextFile(file);

		StringHelper.ReplacementStruct pStruct =
			StringHelper.replaceEx(strContents, oldValue, newValue);

		if (0 == pStruct.count)
			return;

		FileWriter pOut = new FileWriter(file);

		pOut.write(pStruct.value);
		pOut.close();

		if (null != stream)
		{
			stream.print(file.getPath());
			stream.print(" - performed ");
			stream.print(pStruct.count);
			stream.println(" replacement(s).");
		}
	}

	/********************************************************************************
	*
	*	Delete methods
	*
	********************************************************************************/

	/** Deletes all the files and directories specified by the path. The directory
	    itself is not deleted.
		@param strPath String representation of the path who's contents should be deleted.
	*/
	public static void deleteAll(String strPath) { deleteAll(new File(strPath)); }

	/** Deletes all the files and directories specified by the <I>File</I> object. The directory
	    itself is not deleted.
		@param pPath File object who's contents should be deleted.
	*/
	public static void deleteAll(File pPath)
	{
		if (!pPath.isDirectory())
			return;

		File files[] = pPath.listFiles();

		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];

			// First delete the contents of the directory recursively.
			if (file.isDirectory())
				deleteAll(file);

			file.delete();
		}
	}

	/********************************************************************************
	*
	*	Driver method
	*
	********************************************************************************/

	public static void main(String strArgs[])
	{
		for (int i = 0; i < strArgs.length; i++)
			deleteAll(strArgs[i]);
	}
}

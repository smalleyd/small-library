package com.small.library.util;

import java.io.File;

/***************************************************************************************
*
*	Class that implements the <I>java.io.FileFilter</I> interface. The class
*	extends the capabilities of the <I>FileFilterByExtension</I> class to also
*	accept all directories in the search.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 3/6/2002
*
***************************************************************************************/

public class FileFilterByExtensionWithDirectories extends FileFilterByExtension
{
	/*******************************************************************************
	*
	*	Constructors
	*
	*******************************************************************************/

	/** Constructor - constructs a file filter with a single extension to
	    filter on.
	*/
	public FileFilterByExtensionWithDirectories(String strExtension)
	{
		super(strExtension);
	}

	/** Constructor - constructs a file filter with an array of extensions to
	    filter on.
	*/
	public FileFilterByExtensionWithDirectories(String[] strExtensions)
	{
		super(strExtensions);
	}

	/*******************************************************************************
	*
	*	Required methods: FileFilter
	*
	*******************************************************************************/

	/** Required method - indicates whether a <I>File</I> passes the filter.
		@param pFile The <I>File</I> to filter.
	*/
	public boolean accept(File pFile)
	{
		if (pFile.isDirectory())
			return true;

		return super.accept(pFile);
	}
}

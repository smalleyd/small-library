package com.small.library.util;

import java.io.File;
import java.io.FileFilter;

/***************************************************************************************
*
*	Class that implements the <I>java.io.FileFilter</I> interface. The class
*	can be used by the <I>java.io.File</I> <CODE>listFiles</CODE> method to
*	filter files by extension.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 11/28/2001
*
***************************************************************************************/

public class FileFilterByExtension implements FileFilter
{
	/*******************************************************************************
	*
	*	Constructors
	*
	*******************************************************************************/

	/** Constructor - constructs a file filter with a single extension to
	    filter on.
	*/
	public FileFilterByExtension(String strExtension)
	{
		this(new String[] { strExtension });
	}

	/** Constructor - constructs a file filter with an array of extensions to
	    filter on.
	*/
	public FileFilterByExtension(String[] strExtensions)
	{
		// Copy array and a period (".") before the extension.
		m_strExtensions = new String[strExtensions.length];

		for (int i = 0; i < m_strExtensions.length; i++)
			m_strExtensions[i] = "." + strExtensions[i];
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
		String strName = pFile.getName();

		for (int i = 0; i < m_strExtensions.length; i++)
			if (strName.endsWith(m_strExtensions[i]))
				return true;

		return false;
	}

	/*******************************************************************************
	*
	*	Member variables
	*
	*******************************************************************************/

	/** Member variable - contains the list of extensions to filter by. */
	private String[] m_strExtensions = null;
}

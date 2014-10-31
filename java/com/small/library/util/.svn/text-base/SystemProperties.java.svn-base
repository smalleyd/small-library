package com.small.library.util;

import java.io.*;
import java.util.*;

/***********************************************************************************
*
*	Writes to a PrintStream the contents of the System.getProperties()
*	reference.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 6/19/2000
*
***********************************************************************************/

public class SystemProperties
{
	/***************************************************************************
	*
	*	Writes System properties to the standard output.
	*
	***************************************************************************/

	public static void main(String strArgs[])
	{
		SystemProperties pProperties = new SystemProperties();

		pProperties.write(System.out);
	}

	/***************************************************************************
	*
	*	Performs writing the System properties.
	*	@param pOutput A reference to a <I>PrintStream</I> for writing
	*		the output.
	*
	***************************************************************************/

	public void write(PrintStream pStream)
	{
		Properties pProperties = System.getProperties();
		Enumeration pList = pProperties.propertyNames();

		while (pList.hasMoreElements())
		{
			String strName = pList.nextElement().toString();
			String strValue = pProperties.getProperty(strName);

			if (null == strValue)
				strValue = "None";

			pStream.println(strName + "\t" + strValue);
		}
	}
}

package com.small.library.util;

import java.io.*;
import java.net.*;
import java.util.*;

/******************************************************************************************
*
*	Assists with URL related tasks.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 1/17/2000
*
******************************************************************************************/

public class URLHelper
{
	/** Retrieves all the client-side header information returned from the server.
		@param pUrl <I>URLConnection</I> object that represents the resource to contact.
		@return Returns a <I>Map</I> of header key-value pairs.
	*/
	public static Map getHeaderInfo(URLConnection pUrl)
	{
		int i = 1;
		Map pReturn = new HashMap();
		String strKey = null;

		while (null != (strKey = pUrl.getHeaderFieldKey(i)))
			pReturn.put(strKey, pUrl.getHeaderField(i++));

		return pReturn;
	}

	/** Retrieves all the client-side header information returned from the server.
		@param pUrl <I>URL</I> object that represents the resource to contact.
		@return Returns a <I>Map</I> of header key-value pairs.
	*/
	public static Map getHeaderInfo(URL pUrl) throws IOException
	{ return getHeaderInfo(pUrl.openConnection()); }

	/** Retrieves all the client-side header information returned from the server.
		@param pUrl <I>String</I> or the URL that represents the resource to contact.
		@return Returns a <I>Map</I> of header key-value pairs.
	*/
	public static Map getHeaderInfo(String strUrl) throws MalformedURLException, IOException
	{ return getHeaderInfo(new URL(strUrl)); }

	/*********************************************************************************************
	*
	*	Entry-point method
	*
	*********************************************************************************************/

	public static void main(String[] strArgs)
	{
		try
		{
			Map pMapHeaders = getHeaderInfo(strArgs[0]);
			Collection pColHeaders = pMapHeaders.keySet();
			Iterator pHeaders = pColHeaders.iterator();

			while (pHeaders.hasNext())
			{
				String strHeader = (String) pHeaders.next();
				String strValue = (String) pMapHeaders.get(strHeader);

				System.out.println(strHeader + " - " + strValue);
			}
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}

package com.small.library.util;

import java.io.*;
import java.util.*;

/***************************************************************************************
*
*	Exposes a set of static method for manipulating strings.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 4/6/2000
*
***************************************************************************************/

public class StringHelper
{
	/******************************************************************************
	*
	* Constructors/Destructor
	*
	******************************************************************************/

	/** Replace a string value in a string with a another string value.
		@param strValue The string to be searched and replaced.
		@param strOldValue The string value being searched for.
		@param strNewValue The string value used to replace the old value.
	*/
	public static String replace(String strValue,
		String strOldValue, String strNewValue)
	{
		return replaceEx(strValue, strOldValue, strNewValue).value;
	}

	/** Replace a string value in a string with a another string value.
		@param strValue The string to be searched and replaced.
		@param strOldValue The string value being searched for.
		@param strNewValue The string value used to replace the old value.
		@return a <I>ReplacementStruct</I> with the new value plus the
			number of times the old value is found and
			replaced in the <I>String</I>. <CODE>null</CODE> is never
			returned, but the <CODE>value</CODE> member variable of the
			returned <CODE>ReplacementStruct</CODE> maybe <CODE>null</CODE>.
	*/
	public static ReplacementStruct replaceEx(String strValue,
		String strOldValue, String strNewValue)
	{
		ReplacementStruct pReturn = new ReplacementStruct();

		if (null == strValue)
			return pReturn;

		int nPos = 0;
		int nSearch = -1;
		pReturn.value = "";

		while (-1 < (nSearch = strValue.indexOf(strOldValue, nPos)))
		{
			// The last parameter is always the end character plus 1.
			pReturn.value+= strValue.substring(nPos, nSearch) + strNewValue;

			// Indicate that a replacement occurred.
			pReturn.count++;

			// Reposition for the next search.
			nPos = nSearch + strOldValue.length();
		}

		// Get the remainder of the string.
		pReturn.value+= strValue.substring(nPos);
		
		// Return the full struct.
		return pReturn;
	}

	/** Determines whether a <I>String</I> is valid. A <I>String</I> is valid
	    if the it is not <I>null</I> and is not empty.
	*/
	public static boolean isValid(String strValue)
	{ return ((null != strValue) && (0 < strValue.length())) ? true : false; }

	/** Checks to see if a string is blank. If so, it returns a "null".
		@param strValue The String object to be evaluated.
	*/
	public static String convertEmptyToNull(String strValue)
	{
		if (!isValid(strValue))
			return null;

		return strValue;
	}

	/** Converts a <I>byte</I> array to a <I>char</I> array. Simple cast conversion.
		@param pBytes An array of primitive bytes.
		@return An array of primitive chars.
	*/
	public static char[] convertBytesToChars(byte[] pBytes)
	{
		if ((null == pBytes) || (0 == pBytes.length))
			return null;

		char[] pChars = new char[pBytes.length];

		for (int i = 0; i < pBytes.length; i++)
			pChars[i] = (char) (pBytes[i] + 128);

		return pChars;
	}

	/** Returns an ASCII <I>String</I> object from a <I>byte</I> array.
		@param pBytes An array of primitive bytes.
		@return A String object.
	*/
	public static String getStringASCII(byte[] pBytes)
	{
		if (null == pBytes)
			return null;

		return new String(convertBytesToChars(pBytes));
	}

	/** Returns a <I>char</I> array from a <I>String</I> object.
		@param strValue A String value.
		@return An array of primitive chars.
	*/
	public static char[] getChars(String strValue)
	{
		if (!isValid(strValue))
			return null;

		char[] pChars = new char[strValue.length()];

		strValue.getChars(0, pChars.length, pChars, 0);

		return pChars;
	}

	/** Returns a <I>byte</I> array of ASCII characters from a <I>String</I> object.
		@param strValue A String value.
		@return An array of primitive bytes.
	*/
	public static byte[] getBytesAscii(String strValue)
	{
		if (!isValid(strValue))
			return null;

		char[] pChars = getChars(strValue);

		byte[] pBytes = new byte[pChars.length];

		for (int i = 0; i < pChars.length; i++)
			pBytes[i] = (byte) ((int) pChars[i] - 128);

		return pBytes;
	}

	/** Converts an object to a <I>String</I> object. If the object supports
	    the <I>Serializable</I> interface, the object is first converted to bytes.
	    The bytes are then converted to an ASCII <I>String</I>.
	    Otherwise the <I>toString</I> method is invoked.
		@param pValue Any object.
		@return A String value.
	*/
	public static String convertObjectToString(Object pValue)
		throws IOException
	{
		if (null == pValue) return null;

		if (pValue instanceof Serializable)
			return convertObjectToString((Serializable) pValue);
		else
			return pValue.toString();
	}

	/** Converts serializable objects to strings. */
	private static String convertObjectToString(Serializable pValue)
		throws IOException
	{
		ByteArrayOutputStream pByteStream = new ByteArrayOutputStream();
		(new ObjectOutputStream(pByteStream)).writeObject(pValue);

		return getStringASCII(pByteStream.toByteArray());
	}

	/** Converts an ASCII <I>String</I> object to an array of bytes, then to
	    an object. Only works if the object was previously serialized and converted
	    to an ASCII String.
		@param strValue Any String.
		@param An Object.
	*/
	public static Object convertStringToObject(String strValue)
		throws IOException
	{
		if (null == strValue) return null;

		try
		{
			return (new ObjectInputStream(
				new ByteArrayInputStream(
					getBytesAscii(strValue)))).readObject();
		}

		catch (IOException pEx) { throw pEx; }
		catch (Exception pEx) { return null; }
	}

	/*********************************************************************************************
	*
	*	Object conversion methods
	*
	********************************************************************************************/

	/** Converts a String to Int. If the <I>String</I> is invalid, the default value is returned.
		@param strValue String value to convert.
		@param nDefault Default integer value returned in case of a conversion error.
	*/
	public static int convertToInt(String strValue, int nDefault)
	{
		try { return Integer.parseInt(strValue); }
		catch (Exception e) { return nDefault; }
	}	

	/** Converts a String to <I>Date</I>. If the <I>String</I> is invalid, the default value is returned.
		@param strValue String value to convert.
		@param dteDefault Default <I>Date</I> value returned in case of a conversion error.
	*/
	public static java.util.Date convertToDate(String strValue, java.util.Date dteDefault)
	{
		try { return (java.util.Date) java.text.DateFormat.getDateInstance().parse(strValue); }
		catch (Exception e) { return dteDefault; }
	}

	/** Converts a String to <I>Date</I>. If the <I>String</I> is invalid, the current date is returned.
		@param strValue String value to convert.
	*/
	public static java.util.Date convertToDate(String strValue)
	{ return convertToDate(strValue, new java.util.Date()); }

	/*********************************************************************************************
	*
	*	Inner Class: ReplacementStruct
	*
	********************************************************************************************/

	/** Inner class that contains information pertainent to the replacement of
	    <I>String</I> values during the call to <CODE>replace</CODE>. The class
	    contains the converted value plus the number of replacements performed.
	*/
	public static class ReplacementStruct
	{
		/** Member variable - convert <I>String</I> value. */
		public String value = null;

		/** Member variable - number of replacements performed. */
		public int count = 0;
	}

	/*********************************************************************************************
	*
	*	Driver methods
	*
	********************************************************************************************/

	/** Driver method - tests the serialize and deserialize methods. */
	public static void main(String strArgs[])
	{
		Date dteNow = new Date();

		System.out.println("Pre-serialized Date: " + dteNow.toString());

		try
		{
			String strNow = convertObjectToString(dteNow);
			System.out.println("Serialized Date: " + strNow);

			Thread.sleep(3000);

			dteNow = (Date) convertStringToObject(strNow);
			System.out.println("Post-serialized Date: " + dteNow.toString());
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}

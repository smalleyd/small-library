package com.small.library.util;

import java.io.IOException;

/*******************************************************************************************
*
*	Class the supplies static methods for performing Base64 encoding and decoding.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 12/19/2001
*
*******************************************************************************************/

public class Base64
{
	/** Member variable - contains a reference to the global decoder. The decoder is
	    stateless, so several threads can share safely.
	*/
	private static org.apache.commons.codec.binary.Base64 base64 =
		new org.apache.commons.codec.binary.Base64();

	/** Action method - gets a decoded <I>String</I> based on an encoded <I>String</I>.
		@param strValue Encode value.
		@return decoded value.
	*/
	public static String decode(String strValue) throws IOException
	{
		return new String(base64.decode(strValue.getBytes()));
	}

	/** Action method - gets a decoded <I>byte</I> array based on an encoded <I>String</I>.
		@param strValue Encode value.
		@return decoded value.
	*/
	public static byte[] decodeToBytes(String strValue) throws IOException
	{
		return base64.decode(strValue.getBytes());
	}

	/** Action method - encodes a String as Base64.
		@param value String value to encode.
		@return an encoded String.
	*/
	public static String encode(String value) throws IOException
	{
		return new String(base64.encode(value.getBytes()));
	}

	/** Action method - encodes a byte array to Base64 String.
		@param value Byte array to encode.
		@return an encoded String.
	*/
	public static String encode(byte[] value) throws IOException
	{
		return new String(base64.encode(value));
	}

	/** Action method - encodes a String as Base64.
		@param value String value to encode.
		@return an encoded byte array.
	*/
	public static byte[] encodeToBytes(String value) throws IOException
	{
		return base64.encode(value.getBytes());
	}
}

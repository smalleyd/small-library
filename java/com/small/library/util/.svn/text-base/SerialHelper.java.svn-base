package com.small.library.util;

import java.io.*;

/********************************************************************************************
*
*	Assists with the serialization and de-serialization of objects.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 12/3/2000
*
********************************************************************************************/

public class SerialHelper
{
	/** Converts an object that supports the Serializable interface to an array of bytes.
		@param pObject Serializable object to convert.
		@return Array of bytes that represent the serialized object.
	*/
	public static byte[] getBytes(Serializable pObject)
		throws InvalidClassException, NotSerializableException, IOException
	{
		ByteArrayOutputStream pBytesOut = new ByteArrayOutputStream();
		ObjectOutputStream pObjectOut = new ObjectOutputStream(pBytesOut);

		pObjectOut.writeObject(pObject);

		return pBytesOut.toByteArray();
	}

	/** Converts an array of bytes to a serializable object.
		@param pSerializedObject An array of bytes that represent a serialized object.
		@return Deserialized version of the array of bytes.
	*/
	public static Serializable getObject(byte[] pSerializedObject)
		throws ClassNotFoundException, InvalidClassException, StreamCorruptedException,
			OptionalDataException, IOException
	{ return (Serializable) (new ObjectInputStream(new ByteArrayInputStream(pSerializedObject))).readObject(); }
}

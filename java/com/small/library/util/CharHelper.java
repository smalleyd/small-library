package com.small.library.util;

import java.io.PrintStream;

public class CharHelper
{
	public static void printASCIICharSet(PrintStream pOut)
	{
		pOut.println("ASCII Character Set\n\n");

		for (int i = 0; i < 256; i++)
			pOut.println(i + " - " + (char) i);
	}

	public static void main(String strArgs[])
	{
		printASCIICharSet(System.out);
	}
}

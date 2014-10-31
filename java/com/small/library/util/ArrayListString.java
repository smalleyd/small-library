package com.small.library.util;

import java.util.*;

/***************************************************************************************
*
*	Extends the ArrayList class of <I>java.util</I> for adding a list of strings
*	stored in a single string separated by a known delimiter.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 3/24/2000
*
***************************************************************************************/

public class ArrayListString extends ArrayList
{
	/******************************************************************************
	*
	* Constructors/Destructor
	*
	******************************************************************************/

	/** Constructor - accepts string values to build the ArrayList.
		@param strValues The string object with the list of values separated
			by the value in the second the parameter.
		@param strSeparator The string that separates the values in the first
			parameter.
	*/
	public ArrayListString(String strValues, String strSeparator)
	{ super(); add(strValues, strSeparator); }

	/** Constructor - accepts string values to build the ArrayList.
		@param strValues The string object with the list of values separated
			by the value in the second the parameter.
		@param strSeparator The string that separates the values in the first
			parameter.
		@param nInitialCapacity Sets the initial size of the array list.
	*/
	public ArrayListString(String strValues, String strSeparator,
		int nInitialCapacity)
	{ super(nInitialCapacity); add(strValues, strSeparator); }

	/** Augments the array list with values in the first parameter.
		@param strValues The string object with the list of values separated
			by the value in the second the parameter.
		@param strSeparator The string that separates the values in the first
			parameter.
	*/
	public void add(String strValues, String strSeparator)
	{
		// Have parameters been supplied?
		if ((null == strValues) || (null == strSeparator))
			return;

		int nLocation = 0;
		int nLastLocation = 0;

		do
		{
			nLocation = strValues.indexOf(strSeparator, nLastLocation);

			if (0 <= nLocation)
			{
				// DO NOT SUBSTRACT "1" FROM the last parameter.
				add(strValues.substring(nLastLocation, nLocation));
				nLastLocation = nLocation + 2;
			}

		} while (0 <= nLocation);

		// Always add the last value.
		add(strValues.substring(nLastLocation));
	}

	/** Augments the array list with values in the first parameter.
		@param strValues The string object with the list of values separated
			by the value in the second the parameter.
		@param strSeparator The string that separates the values in the first
			parameter.
		@param bReset Should the list be reset.
	*/
	public void add(String strValues, String strSeparator, boolean bReset)
	{
		clear();
		add(strValues, strSeparator);
	}
}

package com.small.library.util;

/*************************************************************************************
*
*	Helper class that fosters the conversion of primitives to their respective
*	object representative and objects to primitives. The helper class takes into
*	account the framework's methodology for indicating that a primitive is
*	<CODE>null</CODE>. Below are the values for each primitive type that
*	indicate <CODE>null</CODE>.
*
*	<BR><BR>
*
*	int -> Integer.MIN_VALUE<BR>
*	long -> Long.MIN_VALUE<BR>
*	short -> Short.MIN_VALUE<BR>
*	float -> Float.NaN<BR>
*	double -> Double.NaN
*
*	<BR><BR>
*
*	boolean and byte do not have any indicators.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 1/6/2003
*
***************************************************************************************/

public class PrimitiveHelper
{
	/**********************************************************************************
	*
	*	Constants
	*
	**********************************************************************************/

	/** Constant - null indicator for <I>int</I> data types. */
	public static final int NULL_VALUE_INT = Integer.MIN_VALUE;

	/** Constant - null indicator for <I>long</I> data types. */
	public static final long NULL_VALUE_LONG = Long.MIN_VALUE;

	/** Constant - null indicator for <I>short</I> data types. */
	public static final short NULL_VALUE_SHORT = Short.MIN_VALUE;

	/** Constant - null indicator for <I>float</I> data types. */
	public static final float NULL_VALUE_FLOAT = Float.NaN;

	/** Constant - null indicator for <I>double</I> data types. */
	public static final double NULL_VALUE_DOUBLE = Double.NaN;

	/**********************************************************************************
	*
	*	Helper methods - null indicators
	*
	**********************************************************************************/

	/** Helper method - indicates whether the <I>int</I> is <CODE>null</CODE>. */
	public static boolean isNull(int value)
	{
		return ((NULL_VALUE_INT == value) ? true : false);
	}

	/** Helper method - indicates whether the <I>long</I> is <CODE>null</CODE>. */
	public static boolean isNull(long value)
	{
		return ((NULL_VALUE_LONG == value) ? true : false);
	}

	/** Helper method - indicates whether the <I>short</I> is <CODE>null</CODE>. */
	public static boolean isNull(short value)
	{
		return ((NULL_VALUE_SHORT == value) ? true : false);
	}

	/** Helper method - indicates whether the <I>float</I> is <CODE>null</CODE>. */
	public static boolean isNull(float value)
	{
		return Float.isNaN(value);
	}

	/** Helper method - indicates whether the <I>double</I> is <CODE>null</CODE>. */
	public static boolean isNull(double value)
	{
		return Double.isNaN(value);
	}

	/**********************************************************************************
	*
	*	Helper methods - conversion from primitive to object
	*
	**********************************************************************************/

	/** Helper method - converts an <I>int</I> to an <I>Integer</I> object. */
	public static Integer toObject(int value)
	{
		if (isNull(value))
			return null;

		return new Integer(value);
	}

	/** Helper method - converts a <I>long</I> to a <I>Long</I> object. */
	public static Long toObject(long value)
	{
		if (isNull(value))
			return null;

		return new Long(value);
	}

	/** Helper method - converts a <I>short</I> to a <I>Short</I> object. */
	public static Short toObject(short value)
	{
		if (isNull(value))
			return null;

		return new Short(value);
	}

	/** Helper method - converts a <I>float</I> to a <I>Float</I> object. */
	public static Float toObject(float value)
	{
		if (isNull(value))
			return null;

		return new Float(value);
	}

	/** Helper method - converts a <I>double</I> to a <I>Double</I> object. */
	public static Double toObject(double value)
	{
		if (isNull(value))
			return null;

		return new Double(value);
	}

	/**********************************************************************************
	*
	*	Helper methods - conversion from object to primitive
	*
	**********************************************************************************/

	/** Helper method - converts an <I>Integer</I> object to an <I>int</I>. */
	public static int toPrimitive(Integer value)
	{
		if (null == value)
			return NULL_VALUE_INT;

		return value.intValue();
	}

	/** Helper method - converts a <I>Long</I> object to a <I>long</I>. */
	public static long toPrimitive(Long value)
	{
		if (null == value)
			return NULL_VALUE_LONG;

		return value.longValue();
	}

	/** Helper method - converts a <I>Short</I> object to a <I>short</I>. */
	public static short toPrimitive(Short value)
	{
		if (null == value)
			return NULL_VALUE_SHORT;

		return value.shortValue();
	}

	/** Helper method - converts a <I>Float</I> object to a <I>float</I>. */
	public static float toPrimitive(Float value)
	{
		if (null == value)
			return NULL_VALUE_FLOAT;

		return value.floatValue();
	}

	/** Helper method - converts a <I>Double</I> object to a <I>double</I>. */
	public static double toPrimitive(Double value)
	{
		if (null == value)
			return NULL_VALUE_DOUBLE;

		return value.doubleValue();
	}
}

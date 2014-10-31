package com.small.library.util;

import java.util.*;

/***************************************************************************************
*
*	Exposes a set of static method for manipulating date information.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 5/8/2000
*
***************************************************************************************/

public class CalendarHelper
{
	/** Constant - String array of the month values. */
	public static final String MONTHS[] = {"January", "February", "March",
		"April", "May", "June", "July", "August", "September",
		"October", "November", "December"};

	/** Constant - String array of the month abbreviates. */
	public static final String MONTH_ABBRS[] = {"Jan.", "Feb.", "Mar",
		"Apr.", "May", "June", "July", "Aug.", "Sept.",
		"Oct.", "Nov.", "Dec."};

	/** Constructor - Default constructor - nothing special. */
	public CalendarHelper() {}

	/** Creates a string based on a beginning date and ending date. Does not
	    repeat the month and/or year if they are the same.
		@param dteFrom A reference to the java.util.Date object as the beginning date.
		@param dteTo A reference to the java.util.Date object as the ending date.
	*/
	public static String createFromToDateString(Date dteFrom, Date dteTo)
	{
		Calendar calFrom = new GregorianCalendar();
		Calendar calTo = new GregorianCalendar();

		calFrom.setTime(dteFrom);
		calTo.setTime(dteTo);

		return createFromToDateString(calFrom, calTo);
	}

	/** Creates a string based on a beginning date and ending date. Does not
	    repeat the month and/or year if they are the same.
		@param calFrom A reference to the java.util.Calendar object as the beginning date.
		@param calTo A reference to the java.util.Calendar object as the ending date.
	*/
	public static String createFromToDateString(Calendar calFrom, Calendar calTo)
	{
		int nFromYear = calFrom.get(Calendar.YEAR);
		int nFromMonth = calFrom.get(Calendar.MONTH);
		int nFromDay = calFrom.get(Calendar.DAY_OF_MONTH);

		int nToYear = calTo.get(Calendar.YEAR);
		int nToMonth = calTo.get(Calendar.MONTH);
		int nToDay = calTo.get(Calendar.DAY_OF_MONTH);

		String strReturn = null;

		if (nFromYear != nToYear)
			strReturn = MONTH_ABBRS[nFromMonth] + " " + nFromDay +
				", " + nFromYear + " - " + MONTH_ABBRS[nToMonth] +
				" " + nToDay + ", " + nToYear;

		else if (nFromMonth != nToMonth)
			strReturn = MONTH_ABBRS[nFromMonth] + " " + nFromDay +
				" - " + MONTH_ABBRS[nToMonth] + " " + nToDay +
				", " + nFromYear;

		else if (nFromDay != nToDay)
			strReturn = MONTH_ABBRS[nFromMonth] + " " + nFromDay +
				" - " + nToDay + ", " + nFromYear;

		else
			strReturn = MONTH_ABBRS[nFromMonth] + " " + nFromDay +
				", " + nFromYear;

		return strReturn;
	}

	/** Creates a <I>Date</I> object that only contains the current date not the time. */
	public static Date getToday() { return removeTimePart(new Date()); }

	/** Remove the time part from a <I>Date</I> object.
		@param pDate <I>Date</I> object to remove the time part from.
		@return Also returns the passed in value for convenience.
	*/
	public static Date removeTimePart(Date pDate)
	{
		Calendar pCalendar = Calendar.getInstance();

		pCalendar.setTime(pDate);

		int nYear = pCalendar.get(Calendar.YEAR);
		int nMonth = pCalendar.get(Calendar.MONTH);
		int nDay = pCalendar.get(Calendar.DAY_OF_MONTH);

		pCalendar.clear();

		pCalendar.set(nYear, nMonth, nDay);

		return pCalendar.getTime();
	}
}

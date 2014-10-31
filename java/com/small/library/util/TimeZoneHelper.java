package com.small.library.util;

import java.util.TimeZone;

/***********************************************************************************
*
*	Helper class that provides additional support for working with
*	the <I>TimeZone</I> class.
*
*	@author AMC\David Small
*	@version 1.0.0.0
*	@date 3/18/2003
*
***********************************************************************************/

public class TimeZoneHelper
{
	/** Helper method - gets an array of all available <I>TimeZone</I> objects. */
	public static TimeZone[] getAllAvailable()
	{
		String[] ids = TimeZone.getAvailableIDs();

		if (0 == ids.length)
			return null;

		TimeZone[] timeZones = new TimeZone[ids.length];

		for (int i = 0; i < ids.length; i++)
			timeZones[i] = TimeZone.getTimeZone(ids[i]);

		return timeZones;
	}

	/** Entry method - Entry point a simple output application. */
	public static void main(String[] args)
	{
		TimeZone[] timeZones = getAllAvailable();

		if (null == timeZones)
		{
			System.out.println("There are no available time zones.");
			System.exit(1);

			return;
		}

		for (int i = 0; i < timeZones.length; i++)
		{
			TimeZone timeZone = timeZones[i];

			System.out.println(timeZone.getID() + " - " + timeZone.getDisplayName() +
				" - " + timeZone.getRawOffset());
		}
	}
}


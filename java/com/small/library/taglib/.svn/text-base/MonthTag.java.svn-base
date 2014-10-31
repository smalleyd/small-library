package com.small.library.taglib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.servlet.jsp.JspTagException;

/***********************************************************************************
*
*	Displays list of months. Uses the Calendar.JANUARY value as the first
*	numeric value of months.
*
*	@author David Small
*	@version 1.0.1.0
*	@date 7/1/2003
*
***********************************************************************************/

public class MonthTag extends SelectTag
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/** Static member - collection of select list options for the month field. */
	private static Collection<ListItem> MONTH_OPTIONS = null;

	/** Static constructor - Used to create the select list options
	    collection of months.
	*/
	static
	{
		MONTH_OPTIONS = new ArrayList<ListItem>(12);

		MONTH_OPTIONS.add(new ListItemImpl(Calendar.JANUARY + "", "January"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.FEBRUARY + "", "February"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.MARCH + "", "March"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.APRIL + "", "April"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.MAY + "", "May"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.JUNE + "", "June"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.JULY + "", "July"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.AUGUST + "", "August"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.SEPTEMBER + "", "September"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.OCTOBER + "", "October"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.NOVEMBER + "", "November"));
		MONTH_OPTIONS.add(new ListItemImpl(Calendar.DECEMBER + "", "December"));
	}

	/** TagSupport method - handles the start of the tag. */
	public int doStartTag()
		throws JspTagException
	{
		setOptions(MONTH_OPTIONS);
		
		return super.doStartTag();
	}
}

package com.small.library.taglib;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

/***************************************************************************************
*
*	Tag class that represents a group of HTML elements for inputting date values.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/29/2002
*
***************************************************************************************/

public class DateControlTag extends FormElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - number of year values to display in the year select list that
	    follow the year of date supplied.
	*/
	public static final int YEAR_SPAN_DEFAULT = 5;

	/** Constant - numeric value of the first month. */
	public static final int MONTH_FIRST = Calendar.JANUARY;

	/** Constant - numeric value of the first month for the <I>Date</I> object. */
	public static final int MONTH_DIFF = 1 - MONTH_FIRST;

	/******************************************************************************
	*
	*	PageElement methods
	*
	******************************************************************************/

	/** PageElement method - gets the tag name for the page element.
			@return <CODE>null</CODE> as there is no single element.
	*/
	public String getTagName() { return null; }

	/******************************************************************************
	*
	*	TagSupport methods
	*
	******************************************************************************/

	/** TagSupport method - handles the start of the tag. */
	public int doStartTag()
		throws JspTagException
	{
		String strName = getName();
		Calendar calendar = Calendar.getInstance();
		Date currentValue = getValue();

		if (null != currentValue)
			calendar.setTime(currentValue);

		// Get default date values.
		int nYear = calendar.get(Calendar.YEAR);
		int nMonth = calendar.get(Calendar.MONTH) + MONTH_DIFF; // Months go from 0 to 11.
		int nDay = calendar.get(Calendar.DAY_OF_MONTH);
		String strScriptName = "Date_" + strName + "_OnChange(this.form);";

		// Start with the current year if one has not been supplied.
		if (NULL_VALUE_INT == yearStart)
			yearStart = nYear;

		// Write the month select list.
		(new SelectTag(pageContext,
			"Month_" + strName, createMonths(), "" + nMonth, cssClass, style,
			strScriptName)).doStartTag();

		// Write the day text box.
		(new TextBoxTag(pageContext,
			"Day_" + strName, "" + nDay, cssClass, style,
			2, 2, strScriptName)).doStartTag();

		// Write the year select list.
		(new SelectTag(pageContext,
			"Year_" + strName, createYears(yearStart, yearStart + yearSpan),
			"" + nYear, cssClass, style, strScriptName)).doStartTag();

		// Write the scripts to manage the date control.
		try { outputScripts(); }
		catch (IOException ex) { throw new JspTagException(ex.getMessage()); }

		return SKIP_BODY;
	}

	/***********************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Helper method - gets the date value represented by the HTML element.
			@param strName Name of the form element.
			@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public static Date getValue(String strName,
		HttpServletRequest pRequest)
	{
		int nMonth = getIntParameter("Month_" + strName, pRequest);
		int nDay = getIntParameter("Day_" + strName, pRequest);
		int nYear = getIntParameter("Year_" + strName, pRequest);

		// None of the values can equal zero.
		if ((0 == nMonth) || (0 == nDay) || (0 == nYear))
			return null;

		// Create a Gregorian Calendar with a blank time.
		GregorianCalendar pCalendar = new GregorianCalendar(nYear, nMonth - 1,
			nDay, 0, 0, 0);

		// Return a date object.
		return pCalendar.getTime();
	}

	/** Helper method - gets an integer request parameter. */
	private static int getIntParameter(String strName,
		HttpServletRequest pRequest)
	{
		try
		{
			String strValue = pRequest.getParameter(strName);

			if (null == strValue)
				return 0;

			return Integer.parseInt(strValue);
		}

		catch (NumberFormatException ex) { return 0; }
	}

	/** Helper method - creates a list of months usable by the months select list. */
	private static Collection<ListItem> createMonths()
	{
		Collection<ListItem> pMonths = new ArrayList<ListItem>(12);

		pMonths.add(new ListItemImpl("1", "January"));
		pMonths.add(new ListItemImpl("2", "February"));
		pMonths.add(new ListItemImpl("3", "March"));
		pMonths.add(new ListItemImpl("4", "April"));
		pMonths.add(new ListItemImpl("5", "May"));
		pMonths.add(new ListItemImpl("6", "June"));
		pMonths.add(new ListItemImpl("7", "July"));
		pMonths.add(new ListItemImpl("8", "August"));
		pMonths.add(new ListItemImpl("9", "September"));
		pMonths.add(new ListItemImpl("10", "October"));
		pMonths.add(new ListItemImpl("11", "November"));
		pMonths.add(new ListItemImpl("12", "December"));

		return pMonths;
	}

	/** Helper method - creates a collection of years usable by the years select list. */
	private static Collection<ListItem> createYears(int nYearLower, int nYearUpper)
	{
		// Local variables.
		String strYear = null;

		if (nYearLower > nYearUpper)
			nYearUpper = nYearLower;

		Collection<ListItem> pYears = new ArrayList<ListItem>(nYearUpper - nYearLower + 1);

		for (int i = nYearLower; i <= nYearUpper; i++)
		{
			strYear = "" + i;
			pYears.add(new ListItemImpl(strYear, strYear));
		}

		return pYears;
	}

	/** Helper method - Outputs the JavaScript used to control and validate the
	    date control.
	*/
	private void outputScripts()
			throws IOException
	{
		JspWriter out = pageContext.getOut();

		String strName = getName();
		String strNameYear = "Year_" + strName;
		String strNameMonth = "Month_" + strName;
		String strNameDay = "Day_" + strName;

		out.println("<script type=\"text/javascript\">");
		out.println("<!--");
		out.println("\tfunction Date_" + strName + "_GetMaxDay(pForm)");
		out.println("\t{");
		out.println("\t\tvar FebMax = 28;");
		out.println("\t\tvar nYearIndex = pForm." + strNameYear + ".selectedIndex;");
		out.println("\t\tif (nYearIndex == -1) {return false;}");
		out.println("\t\tvar nYear = pForm." + strNameYear + ".options[nYearIndex].value;");
		out.println("\t\tvar nMonthID = pForm." + strNameMonth + ".selectedIndex;");
		out.println("\t\tif (nMonthID == -1) {return false;}");
		out.println("\t\telse if (nMonthID == 1) {");
		out.println("\t\t\tif ((nYear % 100)==0) {");
		out.println("\t\t\t\tif ((nYear % 400)==0) {FebMax=29;}");
		out.println("\t\t\t}");
		out.println("\t\t\telse if ((nYear % 4)==0) {FebMax=29;}");
		out.println("\t\t}");
		out.println("");
		out.println("\t\tvar arrMaxDay = [31, FebMax, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];");
		out.println("\t\treturn arrMaxDay[nMonthID];");
		out.println("\t}");
		out.println("");
		out.println("\tfunction Date_" + strName + "_Valid(pForm)");
		out.println("\t{");
		out.println("\t\tvar nDay = pForm." + strNameDay + ".value;");
		out.println("");
		out.println("\t\tif (isNaN(parseInt(nDay))) {return -1;}");
		out.println("\t\telse if (parseInt(nDay) < 1) {return -1;}");
		out.println("\t\telse if (parseInt(nDay) > Date_" + strName + "_GetMaxDay(pForm)) {return 1;}");
		out.println("");
		out.println("\t\treturn 0;");
		out.println("\t}");
		out.println("");
		out.println("\tfunction Date_" + strName + "_IsValid(pForm)");
		out.println("\t{");
		out.println("\t\treturn ((0 == Date_" + strName + "_Valid(pForm)) ? true : false);");
		out.println("\t}");
		out.println("");
		out.println("\tfunction Date_" + strName + "_OnChange(pForm)");
		out.println("\t{");
		out.println("\t\tvar nResult = Date_" + strName + "_Valid(pForm);");
		out.println("\t\tif (-1 == nResult) {pForm." + strNameDay + ".value='1';}");
		out.println("\t\telse if (1 == nResult) {pForm." + strNameDay + ".value = Date_" + strName + "_GetMaxDay(pForm);}");
		out.println("");
		out.println("\t\treturn true;");
		out.println("\t}");
		out.println("// -->");
		out.println("</script>");
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the date value represented by the Date Control. */
	public Date getValue()
		throws JspTagException
	{
		if (isPost())
			return getValue(getName(), getHttpServletRequest());

		return value;
	}

	/** Accessor method - Indicates whether the requested HTML element contains a valid date. */
	public boolean isValid()
		throws JspTagException
	{
		return (null != getValue()) ? true : false;
	}

	/** Accessor method - gets the starting year option of the date control's
	    year select list.
	*/
	public int getYearStart() { return yearStart; }

	/** Accessor method - gets the span of the year options of the date control's
	    year select list.
	*/
	public int getYearSpan() { return yearSpan; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator methods - sets the date value represented by the date control. */
	public void setValue(Date newValue) { value = newValue; }

	/** Mutator method - sets the starting year option of the date control's 
	    year select list.
	*/
	public void setYearStart(int newValue) { yearStart = newValue; }

	/** Mutator method - sets the span of the year options of the date control's
	    year select list.
	*/
	public void setYearSpan(int newValue) { yearSpan = newValue; }

	/*****************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variabl

	/** Member variable - contains the value of the date control. */
	private Date value = null;

	/** Member variable - contains the starting year option of the date control's
	    year select list.
	*/
	private int yearStart = NULL_VALUE_INT;

	/** Member variable - contains the span of the year options of the date control's
	    year select list.
	*/
	private int yearSpan = YEAR_SPAN_DEFAULT;
}

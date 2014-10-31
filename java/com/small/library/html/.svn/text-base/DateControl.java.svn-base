package com.small.library.html;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

/***************************************************************************************
*
*	Class that represents a group of HTML elements for inputting date values.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/16/2000
*
***************************************************************************************/

public class DateControl extends FormElementGroup
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - number of year values to display in the year select list that
	    follow the year of date supplied.
	*/
	public static final int DEFAULT_SPAN_YEAR = 5;

	/** Constant - numeric value of the first month. */
	public static final int MONTH_FIRST = Calendar.JANUARY;

	/** Constant - numeric value of the first month for the <I>Date</I> object. */
	public static final int MONTH_DIFF = 1 - MONTH_FIRST;

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
	*/
	public DateControl(String strName)
	{
		this(strName, null, ATTR_VALUE_NO_VALUE, ATTR_VALUE_NO_VALUE);
	}

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
		@param dteDefaultValue Default date value.
		@param nYearStart First year in the year select list.
		@param nYearSpan Number of years on the year select list.
	*/
	public DateControl(String strName,
		Date dteDefaultValue, int nYearStart, int nYearSpan)
	{ this(strName, null, null, dteDefaultValue, nYearStart, nYearSpan); }

	/** Constructor - constructs an object with a Name, a Default Value attribute,
	    a Cascading Stylesheet class name, and a Cascading Stylesheet style string.
		@param strName Name of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param dteDefaultValue Default date value.
		@param nYearStart First year in the year select list.
		@param nYearSpan Number of years on the year select list.
	*/
	public DateControl(String strName, String strCSSClass, String strCSSStyle,
		Date dteDefaultValue, int nYearStart, int nYearSpan)
	{
		super(strName, null, strCSSClass, strCSSStyle);

		m_DefaultDate = Calendar.getInstance();

		if (null != dteDefaultValue)
			m_DefaultDate.setTime(dteDefaultValue);

		// Get default date values.
		int nYear = m_DefaultDate.get(Calendar.YEAR);
		int nMonth = m_DefaultDate.get(Calendar.MONTH) + MONTH_DIFF; // Months go from 0 to 11.
		int nDay = m_DefaultDate.get(Calendar.DAY_OF_MONTH);
		String strScriptName = "Date_" + strName + "_OnChange(this.form);";

		// Start with the current year if one has not been supplied.
		if (ATTR_VALUE_NO_VALUE == nYearStart)
			nYearStart = nYear;

		m_SelectListMonth = new SelectList("Month_" + strName, "" + nMonth, strCSSClass, strCSSStyle,
			null, 0, false, strScriptName, 0, createMonths());

		m_TextboxDay = new Textbox("Day_" + strName, "" + nDay, strCSSClass, strCSSStyle,
			2, 2, strScriptName);

		m_SelectListYear = new SelectList("Year_" + strName, "" + nYear, strCSSClass, strCSSStyle,
			null, 0, false, strScriptName, 0, createYears(nYearStart, nYearStart + nYearSpan));

		m_strDateScripts = createScripts(strName, m_SelectListYear.getName(),
			m_SelectListMonth.getName(), m_TextboxDay.getName());
	}

	/******************************************************************************
	*
	*	Create functionality
	*
	******************************************************************************/

	/** Action method - creates HTML elements to get date input.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		m_SelectListMonth.create(pWriter);
		m_TextboxDay.create(pWriter);
		m_SelectListYear.create(pWriter);

		writeNewLine(pWriter);
		write(pWriter, m_strDateScripts);
	}

	/** Action method - creates HTML elements to get date input.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param dteValue Date value used to set the input elements.
	*/
	public void create(Writer pWriter, Date dteValue) throws IOException
	{
		Calendar pCalValue = Calendar.getInstance();
		pCalValue.setTime(dteValue);

		int nYear = pCalValue.get(Calendar.YEAR);
		int nMonth = pCalValue.get(Calendar.MONTH) + MONTH_DIFF;
		int nDay = pCalValue.get(Calendar.DAY_OF_MONTH);

		m_SelectListMonth.create(pWriter, "" + nYear);
		m_TextboxDay.create(pWriter, "" + nDay);
		m_SelectListYear.create(pWriter, "" + nMonth);

		writeNewLine(pWriter);
		write(pWriter, m_strDateScripts);
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the date value represented by the HTML element. */
	public Date getDate()
	{
		return getDate(getRequest());
	}

	/** Accessor method - gets the date value represented by the HTML element.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public Date getDate(HttpServletRequest pRequest)
	{
		int nMonth = m_SelectListMonth.getInt(pRequest);
		int nDay = m_TextboxDay.getInt(pRequest);
		int nYear = m_SelectListYear.getInt(pRequest);

		// None of the values can equal zero.
		if ((0 == nMonth) || (0 == nDay) || (0 == nYear)) return null;

		// Create a Gregorian Calendar with a blank time.
		GregorianCalendar pCalendar = new GregorianCalendar(nYear, nMonth - 1,
			nDay, 0, 0, 0);

		// Return a date object.
		return pCalendar.getTime();
	}

	/** Accessor method - Indicates whether the requested HTML element contains a valid date. */
	public boolean isValid()
	{
		return (null != getDate()) ? true : false;
	}

	/** HTTP Request Helper method - gets a boolean value from an HTTP request.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public boolean getBoolean(HttpServletRequest pRequest)
	{
		return ((null != getDate(pRequest)) ? true : false);
	}

	/** HTTP Request Helper method - gets a single string from an HTTP request.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
	*/
	public String getString(HttpServletRequest pRequest)
		throws NullPointerException
	{
		Date pReturn = getDate(pRequest);

		if (null == pReturn)
			return null;

		return pReturn.toString();
	}

	/** HTTP Request Helper method - gets an array of string values.
		@param pRequest <I>HttpServletRequest</I> object used to get input.
		@return Returns the strings in a java.util.ArrayList object. If
			no values exist, the method returns <I>null</I>.
	*/
	public ArrayList getStrings(HttpServletRequest pRequest)
		throws NullPointerException
	{
		Date pDate = getDate(pRequest);

		if (null == pDate)
			return null;

		ArrayList pReturn = new ArrayList(1);

		pReturn.add(pDate);

		return pReturn;
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Helper method - creates a list of months usable by the months select list. */
	private static List createMonths()
	{
		List pMonths = new List(12);

		pMonths.add(new ListItem("1", "January"));
		pMonths.add(new ListItem("2", "February"));
		pMonths.add(new ListItem("3", "March"));
		pMonths.add(new ListItem("4", "April"));
		pMonths.add(new ListItem("5", "May"));
		pMonths.add(new ListItem("6", "June"));
		pMonths.add(new ListItem("7", "July"));
		pMonths.add(new ListItem("8", "August"));
		pMonths.add(new ListItem("9", "September"));
		pMonths.add(new ListItem("10", "October"));
		pMonths.add(new ListItem("11", "November"));
		pMonths.add(new ListItem("12", "December"));

		return pMonths;
	}

	/** Helper method - creates a list of years usable by the years select list. */
	private static List createYears(int nYearLower, int nYearUpper)
	{
		// Local variables.
		String strYear = null;

		if (nYearLower > nYearUpper)
			nYearUpper = nYearLower;

		List pYears = new List(nYearUpper - nYearLower + 1);

		for (int i = nYearLower; i <= nYearUpper; i++)
		{
			strYear = "" + i;
			pYears.add(new ListItem(strYear, strYear));
		}

		return pYears;
	}

	/** Helper method - Creates the JavaScript to manage and validate the Date Control.
		@param strName Name of the Date Control element.
		@param strNameYear Name of the Year Selection List.
		@param strNameMonth Name of the Month Selection List.
		@param strNameDay Name of the Day textbox.
	*/
	private static String createScripts(String strName, String strNameYear,
		String strNameMonth, String strNameDay)
	{
		StringBuffer strBuffer = new StringBuffer();

		strBuffer.append("<SCRIPT LANGUAGE=\"javascript\">\n");
		strBuffer.append("<!--\n");
		strBuffer.append("\tfunction Date_" + strName + "_GetMaxDay(pForm)\n");
		strBuffer.append("\t{\n");
		strBuffer.append("\t\tvar FebMax = 28;\n");
		strBuffer.append("\t\tvar nYearIndex = pForm." + strNameYear + ".selectedIndex;\n");
		strBuffer.append("\t\tif (nYearIndex == -1) {return false;}\n");
		strBuffer.append("\t\tvar nYear = pForm." + strNameYear + ".options[nYearIndex].value;\n");
		strBuffer.append("\t\tvar nMonthID = pForm." + strNameMonth + ".selectedIndex;\n");
		strBuffer.append("\t\tif (nMonthID == -1) {return false;}\n");
		strBuffer.append("\t\telse if (nMonthID == 1) {\n");
		strBuffer.append("\t\t\tif ((nYear % 100)==0) {\n");
		strBuffer.append("\t\t\t\tif ((nYear % 400)==0) {FebMax=29;}\n");
		strBuffer.append("\t\t\t}\n");
		strBuffer.append("\t\t\telse if ((nYear % 4)==0) {FebMax=29;}\n");
		strBuffer.append("\t\t}\n");
		strBuffer.append("\n");
		strBuffer.append("\t\tvar arrMaxDay = [31, FebMax, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];\n");
		strBuffer.append("\t\treturn arrMaxDay[nMonthID];\n");
		strBuffer.append("\t}\n");
		strBuffer.append("\n");
		strBuffer.append("\tfunction Date_" + strName + "_Valid(pForm)\n");
		strBuffer.append("\t{\n");
		strBuffer.append("\t\tvar nDay = pForm." + strNameDay + ".value;\n");
		strBuffer.append("\n");
		strBuffer.append("\t\tif (isNaN(parseInt(nDay))) {return -1;}\n");
		strBuffer.append("\t\telse if (parseInt(nDay) < 1) {return -1;}\n");
		strBuffer.append("\t\telse if (parseInt(nDay) > Date_" + strName + "_GetMaxDay(pForm)) {return 1;}\n");
		strBuffer.append("\n");
		strBuffer.append("\t\treturn 0;\n");
		strBuffer.append("\t}\n");
		strBuffer.append("\n");
		strBuffer.append("\tfunction Date_" + strName + "_IsValid(pForm)\n");
		strBuffer.append("\t{\n");
		strBuffer.append("\t\treturn ((0 == Date_" + strName + "_Valid(pForm)) ? true : false);\n");
		strBuffer.append("\t}\n");
		strBuffer.append("\n");
		strBuffer.append("\tfunction Date_" + strName + "_OnChange(pForm)\n");
		strBuffer.append("\t{\n");
		strBuffer.append("\t\tvar nResult = Date_" + strName + "_Valid(pForm);\n");
		strBuffer.append("\t\tif (-1 == nResult) {pForm." + strNameDay + ".value='1';}\n");
		strBuffer.append("\t\telse if (1 == nResult) {pForm." + strNameDay + ".value = Date_" + strName + "_GetMaxDay(pForm);}\n");
		strBuffer.append("\n");
		strBuffer.append("\t\treturn true;\n");
		strBuffer.append("\t}\n");
		strBuffer.append("// -->\n");
		strBuffer.append("</SCRIPT>\n");

		return strBuffer.toString();
	}

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the month select list. */
	private SelectList m_SelectListMonth = null;

	/** Member variable - contains the day textbox. */
	private Textbox m_TextboxDay = null;

	/** Member variable - contains the year select list. */
	private SelectList m_SelectListYear = null;

	/** Member variable - contains the default date value. */
	private Calendar m_DefaultDate = null;

	/** Member variable - contains the client JavaScript to manage the elements. */
	private String m_strDateScripts = null;
}

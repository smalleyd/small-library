package com.small.library.taglib;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import java.util.*;
import java.io.*;

/***************************************************************************
*
* Custom tag that creates a complex form input to send a date to an HTTP
* server.
*
* @author Ryan Small
* @since Oct 5, 2005
* @version 1
*
***************************************************************************/
public class DateControlTag2 extends FormElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/** Constant - CSS Class that represents normal operations. */
	public static final String CSS_CLASS = "text";

	/** Constant - CSS Class that represents that the field has an error. */
	public static final String CSS_CLASS_ERROR = "textError";

	/***********************************************************************
	*
	* Array Index Constants for CSS Style Classes of Calendar Elements
	*
	***********************************************************************/

	/** Array Index Constant - DateControl's Label CSS Class. */
	public static final int CSS_CLASS_DATE_CONTROL_LABEL = 0;

	/** Array Index Constant - DateControl's Calendar Frame CSS Class. */
	public static final int CSS_CLASS_CALENDAR_FRAME = 1;

	/** Array Index Constant - DateControl's Calendar Navigation CSS Class. */
	public static final int CSS_CLASS_CALENDER_NAVIGATION = 2;

	/** Array Index Constant - DateControl's Calendar Navigation Link CSS Class. */
	public static final int CSS_CLASS_CALENDER_NAV_LINK = 3;

	/** Array Index Constant - DateControl's Calendar Navigation Title CSS Class. */
	public static final int CSS_CLASS_CALENDER_NAV_TITLE = 4;

	/** Array Index Constant - DateControl's Calendar Content CSS Class. */
	public static final int CSS_CLASS_CALENDAR_CONTENT = 5;

	/** Array Index Constant - DateControl's Calendar Day Header Row CSS Class. */
	public static final int CSS_CLASS_CALENDAR_DAY_HEADER_ROW = 6;

	/** Array Index Constant - DateControl's Calendar Day Row CSS Class. */
	public static final int CSS_CLASS_CALENDAR_DAY_ROW = 7;

	/** Array Index Constant - DateControl's Calendar Footer CSS Class. */
	public static final int CSS_CLASS_CALENDAR_FOOTER = 8;

	/** Array Index Constant - DateControl's Calendar Chosen Day CSS Class. */
	public static final int CSS_CLASS_CHOSEN_DAY = 9;

	/** Array Index Constant - DateControl's Calendar Unavailable Day CSS Class. */
	public static final int CSS_CLASS_UNAVAILABLE_DAY = 10;

	/** Implements the abstract method by indicating a complex tag. */
	public String getTagName() { return null; }

	/** JSP Tag Object implementation that handles the beginning of JSP Custom Tag. */
	public int doStartTag()
		throws JspTagException
	{
		String 		strName		 = getName();

		try {
			JspWriter	out			= pageContext.getOut();

			out.println("\t<p id=\"" + 
						strName + 
						"DateControlLbl\" class=\"" + 
						popupCal_StyleClasses[DateControlTag2.CSS_CLASS_DATE_CONTROL_LABEL] + 
						"\" onclick=\"loadCalendar(\'" + 
						strName + "\');\"></p>");
			
			out.println("\t<div id=\"" + 
						strName + 
						"CalFr\" class=\"" + 
						popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CALENDAR_FRAME] + "\">");

			out.println();
			
			out.println("\t\t<table id=\"" + 
						strName + 
						"CalNav\" class=\"" + 
						popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CALENDER_NAVIGATION] + 
						"\">");
			
			out.println("\t\t\t<tr>");
			
			out.println("\t\t\t\t<td class=\"" + 
						popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CALENDER_NAV_LINK] + 
						"\"><a href=\"javascript: void(0);\">&lt;</a></td>");
			
			out.println("\t\t\t\t<td class=\"" + 
						popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CALENDER_NAV_TITLE] + 
						"\"></td>");
			
			out.println("\t\t\t\t<td class=\"" + 
						popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CALENDER_NAV_LINK] +
						"\"><a href=\"javascript: void(0);\">&gt;</a></td>");
			
			out.println("\t\t\t</tr>");
			
			out.println("\t\t</table>");
			
			out.println();
			
			out.println("\t\t<table id=\"" + 
						strName + 
						"CalContent\" class=\"" +
						popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CALENDAR_CONTENT] + 
						"\">");
			
			out.println("\t\t\t<tr class=\"" + 
						popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CALENDAR_DAY_HEADER_ROW] + 
						"\">");

			out.println("\t\t\t\t<td>S</td><td>M</td><td>T</td><td>W</td><td>T</td><td>F</td><td>S</td>");
		
			out.println("\t\t\t</tr>");
			
			out.print( getCalendarContentGenerateScript());
			
			out.println("\t\t</table>");
			
			out.println();
			
			out.println("\t\t<div id=\"" + strName + "CalFooterActions\" class=\"" + 
						popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CALENDAR_FOOTER] + "\">");

			out.println("\t\t\t<hr/>");
			
			out.println("\t\t\t<a href=\"javascript: resetDatetimeControl(\'" + 
						strName + 
						"\');\">Reset</a>");
			
			out.println("\t\t\t<a href=\"javascript: closeCalendar(\'" + 
						strName + 
						"\');\">Close</a>");
			
			out.println("\t\t\t<hr/>");
			
			out.println("\t\t</div>");
			
			out.println();
			
			out.println("\t</div>");
			
			out.println();
			
			out.print( getDeclarationScript());
			
			out.println();
			
			out.println("\t<input type=\"hidden\" id=\"Month_" + 
						strName + 
						"\" name=\"Month_" + strName + "\" value=\"\"/>");
			
			out.println("\t<input type=\"hidden\" id=\"Day_" + 
						strName + 
						"\" name=\"Day_" + 
						strName + 
						"\" value=\"\"/>");
			
			out.println("\t<input type=\"hidden\" id=\"Year_" + 
						strName + 
						"\" name=\"Year_" + strName + "\" value=\"\"/>");
		}
		catch ( IOException jspStreamEx) { throw new JspTagException(jspStreamEx); }

		return EVAL_PAGE;
	}

	/** JSP Tag Object implementation that handles the end of JSP Custom Tag. */
	public int doEndTag() {
		return EVAL_PAGE;
	}

	/***********************************************************
	*
	*	Helper methods
	*
	************************************************************/
	private String getJavascriptDateInstantiation( Date value) {
		if ( value == null )
			return "null";

		GregorianCalendar valueFmt = DateControlTag2.getGregorianDate(value.getTime());

		return "new Date( " + valueFmt.get( Calendar.YEAR) +
				", " + valueFmt.get( Calendar.MONTH) +
				", " + valueFmt.get( Calendar.DAY_OF_MONTH) + ")";
	}
	private String getDeclarationScript() {
		StringBuffer	sb = new StringBuffer( 768);

		GregorianCalendar minDateFmt = minDate != null ? 
DateControlTag2.getGregorianDate( minDate.getTime()) : null;
		GregorianCalendar maxDateFmt = maxDate != null ? 
DateControlTag2.getGregorianDate( maxDate.getTime()) : null;

		String valStr = new String();

		try {
			Date currValue = getValue();

			valStr = getJavascriptDateInstantiation( currValue);
		}
		catch ( JspTagException ex) { }

		sb.append( "\t<script type=\"text/javascript\" language=\"Javascript\">\n");
		sb.append( "\t  <!--\n");
		sb.append( "\t    " + getName() + 
					"         = new DatetimeControl( \"" + 
					getName() + "\", " + valStr + ");\n");
		sb.append( "\t    " + getName() + ".minDate = " + 
					( minDate == null ? 
							"null" : 
							"new Date( " + minDateFmt.get( Calendar.YEAR) + ", " + 
								minDateFmt.get( Calendar.MONTH) + ", " + 
								minDateFmt.get( Calendar.DAY_OF_MONTH) + ")") + 
					";\n");
		sb.append( "\t    " + getName() + ".maxDate = " + 
					( maxDate == null ? 
							"null" : 
							"new Date( " + maxDateFmt.get( Calendar.YEAR) + ", " + 
								maxDateFmt.get( Calendar.MONTH) + ", " + 
								maxDateFmt.get( Calendar.DAY_OF_MONTH) + ")") + 
					";\n");
		sb.append( "\t    " + getName() + ".cal_UnavailableDayClass = \"" + 
					popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CHOSEN_DAY] + "\";\n");
		sb.append( "\t    " + getName() + ".cal_DefaultDayClass = \"" + 
					popupCal_StyleClasses[DateControlTag2.CSS_CLASS_UNAVAILABLE_DAY] + "\";\n");

		sb.append( "\t  //-->\n");
		sb.append( "\t</script>\n");

		//-> RETURN the appended script string
		return sb.toString();
	}

	private String getCalendarContentGenerateScript() {
		StringBuffer sb = new StringBuffer( 256);

		sb.append( "\t\t\t<script type=\"text/javascript\" language=\"Javascript\">\n");
		sb.append( "\t\t\t\t<!--\n");
		sb.append( "\t\t\t\t  for ( row = 0; row < 6; row++) {\n");
		sb.append( "\t\t\t\t    document.write( \"\t\t\t<tr class=\\\"" + 
					popupCal_StyleClasses[DateControlTag2.CSS_CLASS_CALENDAR_DAY_ROW] + 
					"\\\">\");\n\n");
		sb.append( "\t\t\t\t    for ( dayOfWk = 0; dayOfWk < 7; dayOfWk++) {\n");
		sb.append( "\t\t\t\t      document.write( \"\\t <td><a href=\\\"javascript: void(0);\\\"></a></td>\");\n");
		sb.append( "\t\t\t\t    }\n\n");
		sb.append( "\t\t\t\t    document.write( \"       </tr>\");\n");
		sb.append( "\t\t\t\t  }\n");
		sb.append( "\t\t\t\t//-->\n");
		sb.append( "\t\t\t</script>\n");

		return sb.toString();
	}

	public static GregorianCalendar getGregorianDate( long millisFromDefDate) {
		Calendar dateAnalyzer = Calendar.getInstance();

		dateAnalyzer.setTimeInMillis( millisFromDefDate);

		return new GregorianCalendar( dateAnalyzer.get( Calendar.YEAR),
										dateAnalyzer.get( Calendar.MONTH),
										dateAnalyzer.get( Calendar.DAY_OF_MONTH));
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
			return DateControlTag.getValue(getName(), getHttpServletRequest());

		return value;
	}

	/** Accessor method - gets the min date limiting the value of the Date Control. */
	public Date getMinValue()
		throws JspTagException
	{
		return minDate;
	}

	/** Accessor method - gets the max date limiting the value of the Date Control. */
	public Date getMaxValue()
		throws JspTagException
	{
		return minDate;
	}

	/** Accessor method - Indicates whether the requested HTML element contains a valid date. */
	public boolean isValid()
		throws JspTagException
	{
		return (null != getValue()) ? true : false;
	}

	/** Accessor method - gets CSS Class based on the error status of the field. */
	public String getCssClass()
	{
		boolean isError = false;

		try { isError = isError(); }
		catch (JspTagException ex) {}

		if (isError)
			return CSS_CLASS_ERROR;

		return CSS_CLASS;
	}

	/** Accessor method - gets CSS Classes for the Date Control. */
	public Collection<String> getCssClasses()
	{
		return Arrays.asList( popupCal_StyleClasses);
	}

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator methods - sets the date value represented by the date control. */
	public void setValue(Date newValue) { value = newValue; }

	/** Mutator methods - sets the min date limiting the value of the date control. */
	public void setMinValue(Date newValue) { minDate = newValue; }

	/** Mutator methods - sets the max date limiting the value of the date control. */
	public void setMaxValue(Date newValue) { maxDate = newValue; }

	/** Mutator methods - sets the CSS Classes for the Date Control. */
	public void setCssClasses(Collection<String> classes) {
		Iterator<String> itr = classes.iterator();

		for (int i = 0; i < classes.size(); i++) {
			popupCal_StyleClasses[i] = (String)itr.next();
		}
	}

	/*****************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the value of the date control. */
	private Date value = null;

	/** Member variable - contains the minimum date of the date control. */
	private Date minDate = null;

	/** Member variable - contains the maximum date of the date control. */
	private Date maxDate = null;

	/** Member variable - contains all styles used in elements of the date control. */
	private String []popupCal_StyleClasses = { "" , "" , "" , "" , "" , "" , "" , "" , "" , "" , "" };

}


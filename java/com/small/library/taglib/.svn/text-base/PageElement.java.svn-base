package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.small.library.servlet.ControllerServlet;
import com.small.library.servlet.Model;
import com.small.library.biz.ValidationException;

/********************************************************************
*
*	Base class for PDD tag libraries. Provides framework services
*	such as getting the <I>Model</I>.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/20/2002
*
*********************************************************************/

public abstract class PageElement extends TagSupport
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*******************************************************************
	*
	*	Constants
	*
	*******************************************************************/

	/** Constant - <CODE>null</CODE> value indicator for integers. */
	public static final int NULL_VALUE_INT = Integer.MIN_VALUE;

	/*******************************************************************
	*
	*	Constants - page elements
	*
	*******************************************************************/

	/** Constant - tag for line breaks. */
	public static final String TAG_BREAK = "<br />";

	/** Constant - non-breaking spacing page element. */
	public static final String NBSP = "&nbsp;";

	/*******************************************************************
	*
	*	Constants - general usage properties.
	*
	*******************************************************************/

	/** Constant - property name for the ID attribute. */
	public static final String PROPERTY_ID = "id";

	/** Constant - property name for the border attribute. */
	public static final String PROPERTY_BORDER = "border";

	/** Constant - property name for the CSS class attribute. */
	public static final String PROPERTY_CLASS = "class";

	/** Constant - property name for the CSS style attribute. */
	public static final String PROPERTY_STYLE = "style";

	/** Constant - property name for the height attribute. */
	public static final String PROPERTY_HEIGHT = "height";

	/** Constant - property name for the href attribute. */
	public static final String PROPERTY_HREF = "href";

	/** Constant - property name for the "name" attribute. */
	public static final String PROPERTY_NAME = "name";

	/** Constant - property name for the onBlur attribute. */
	public static final String PROPERTY_ON_BLUR = "onblur";

	/** Constant - property name for the onChange attribute. */
	public static final String PROPERTY_ON_CHANGE = "onchange";

	/** Constant - property name for the onClick attribute. */
	public static final String PROPERTY_ON_CLICK = "onclick";

	/** Constant - property name for the onDoubleClick attribute. */
	public static final String PROPERTY_ON_DOUBLE_CLICK = "ondblclick";

	/** Constant - property name for the onFocus attribute. */
	public static final String PROPERTY_ON_FOCUS = "onfocus";

	/** Constant - property name for the source attribute. */
	public static final String PROPERTY_SOURCE = "src";

	/** Constant - property name for the style attribute. */
	public static final String PROEPRTY_STYLE = "style";

	/** Constant - property name for the width attribute. */
	public static final String PROPERTY_WIDTH = "width";

	/*******************************************************************
	*
	*	Static members
	*
	*******************************************************************/

	/** Static member - context root of the application. Retrieved
	    during the first access and cached for later use.
	*/
	private static String contextRoot = null;

	/****************************************************************
	*
	*	Constructors
	*
	****************************************************************/

	/** Constructor - sets the default values. */
	public PageElement()
	{
		this(null, null, null);
	}

	/** Constructor - provides the default values.
			@param name Name of the page element.
			@param CSS Class of the page element.
			@param CSS Style of the page element.
	*/
	public PageElement(String name, String cssClass, String style)
	{
		this.name = name;
		this.cssClass = cssClass;
		this.style = style;
	}

	/****************************************************************
	*
	*	Abstract methods
	*
	****************************************************************/

	/** Abstract method - gets the derived class's tag name. Used
	    by <CODE>startTag</CODE>.
	    	@see PageElement#startTag()
	*/
	public abstract String getTagName();

	/****************************************************************
	*
	*	Helper methods
	*
	****************************************************************/

	/** Helper method - starts the tag. Uses the derived class's
	    <CODE>getTag</CODE> value.
	    	@see PageElement#getTagName()
	*/
	protected void startTag()
		throws JspTagException, IOException
	{
		JspWriter out = pageContext.getOut();

		out.print("<");
		out.print(getTagName());

		outputProperty(PROPERTY_ID, getId());
		outputProperty(PROPERTY_NAME, getName());
		outputProperty(PROPERTY_CLASS, getCssClass());
		outputProperty(PROPERTY_STYLE, getStyle());
	}

	/** Helper method - ends the tag. Uses the derived class's
	    <CODE>getTag</CODE> value.
	    	@see PageElement#getTagName()
	*/
	protected void endTag()
		throws JspTagException, IOException
	{
		JspWriter out = pageContext.getOut();

		out.print("</");
		out.print(getTagName());
		out.print(">");
	}

	/** Helper method - gets the page's <I>Model</I>. */
	protected Model getModel()
		throws JspTagException
	{
		return (Model) pageContext.getRequest().
			getAttribute(ControllerServlet.ATTR_NAME_MODEL);
	}

	/** Helper method - gets the page's <I>ValidationException</I> object. */
	protected ValidationException getValidationException()
		throws JspTagException
	{
		return (ValidationException) pageContext.getRequest().
			getAttribute(ControllerServlet.ATTR_NAME_VALIDATION_EXCEPTION);
	}

	/** Helper method - gets the page's validation message. */
	protected String getValidationMessage()
		throws JspTagException
	{
		return (String) pageContext.getRequest().
			getAttribute(ControllerServlet.ATTR_NAME_VALIDATION_MESSAGE);
	}

	/** Helper method - gets an <I>HttpServletRequest</I> object
	    from the <I>PageContext</I>.
	*/
	protected HttpServletRequest getHttpServletRequest()
		throws JspTagException
	{
		return (HttpServletRequest) pageContext.getRequest();
	}

	/** Helper method - gets an <I>HttpServletResponse</I> object
	    from the <I>PageContext</I>.
	*/
	protected HttpServletResponse getHttpServletResponse()
		throws JspTagException
	{
		return (HttpServletResponse) pageContext.getResponse();
	}

	/** Helper method - get the context root of the application. */
	protected String getContextRoot()
		throws JspTagException
	{
		if (null != contextRoot)
			return contextRoot;

		return (contextRoot = getHttpServletRequest().getContextPath());
	}

	/****************************************************************
	*
	*	Helper methods - output
	*
	****************************************************************/

	/** Output method - outputs a <I>String</I> property. If the value
	    is <CODE>null</CODE>, the property is not output.
			@param propertyName Name of the property.
			@param propertyValue Value of the property.
	*/
	protected void outputProperty(String propertyName,
		String propertyValue)
			throws IOException
	{
		if (null == propertyValue)
			return;

		JspWriter out = pageContext.getOut();

		out.print(" ");
		out.print(propertyName);
		out.print("=\"");
		out.print(propertyValue);
		out.print("\"");
	}

	/** Output method - outputs an <I>int</I> property. If the value
	    is less than zero, the property is not output.
			@param propertyName Name of the property.
			@param propertyValue Value of the property.
	*/
	protected void outputProperty(String propertyName,
		int propertyValue)
			throws IOException
	{
		if (NULL_VALUE_INT == propertyValue)
			return;

		outputProperty(propertyName, "" + propertyValue);
	}

	/** Output method - outputs a unary property based on the flag.
			@param propertyName Name of the property.
			@param flag If the <CODE>flag</CODE> is <CODE>true</CODE>,
				the unary property is output.
	*/
	protected void outputUnaryProperty(String propertyName,
		boolean flag)
			throws IOException
	{
		if (!flag)
			return;

		JspWriter out = pageContext.getOut();

		out.print(" ");
		out.print(propertyName);
	}

	/*****************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************/

	/** Accessor method - gets the name of the tag. */
	public String getName() { return name; }

	/** Accessor method - gets the CSS class of the tag. */
	public String getCssClass() { return cssClass; }

	/** Accessor method - gets the CSS style of the tag. */
	public String getStyle() { return style; }

	/*****************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************/

	/** Mutator method - sets the name of the tag. */
	public void setName(String newValue) { name = newValue; }

	/** Mutator method - sets the CSS class of the tag. */
	public void setCssClass(String newValue) { cssClass = newValue; }

	/** Mutator method - sets the style of the tag. */
	public void setStyle(String newValue) { style = newValue; }

	/*****************************************************************
	*
	*	Member variables
	*
	*****************************************************************/

	/** Member variable - contains the name of the tag. */
	protected String name;

	/** Member variable - contains the CSS class of the tag. */
	protected String cssClass;

	/** Member variable - contains the CSS style of the tag. */
	protected String style;
}

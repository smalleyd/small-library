package com.small.library.taglib;

import java.io.IOException;
import java.util.Set;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

/***********************************************************************
*
*	Base option buttons tag class that renders the HTML for a form option button
*	list. Option buttons can be either radio buttons or checkboxes.
*
*	Uses the DHTML "div" tag to display a scrollable region within
*	a page for the list of options.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/27/2002
*
***********************************************************************/

public abstract class OptionButtonsElement extends SelectionTag
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - tag name. */
	public static final String TAG_NAME = "div";

	/** Constant - tag for the individual option. */
	public static final String TAG_OPTION = InputTag.TAG_NAME;

	/** Constant - CSS class for a selection region. */
	public static final String CSS_CLASS_SELECTION_REGION = "selectionRegion";

	/*****************************************************************
	*
	*	Constructors
	*
	*****************************************************************/

	/** Constructor - sets the default values. */
	public OptionButtonsElement()
	{
		this(TAG_BREAK, true);
	}

	/** Constructor - accepts the default values.
			@param delimiter The delimiter for the option buttons.
			@param hasBorder Indicates whether the option buttons group
				has a border.
	*/
	protected OptionButtonsElement(String delimiter, boolean hasBorder)
	{
		this.delimiter = delimiter;
		this.hasBorder = hasBorder;
	}

	/*******************************************************************
	*
	*	Abstract methods
	*
	*******************************************************************/

	/** Abstract method - gets the Input tag type. */
	public abstract String getType();

	/*******************************************************************
	*
	*	PageElement methods
	*
	*******************************************************************/

	/** PageElement method - gets the tag name. */
	public String getTagName() { return TAG_NAME; }

	/*******************************************************************
	*
	*	TagSupport methods
	*
	*******************************************************************/

	/** TagSupport method - handles the start of the tag. */
	public int doStartTag()
		throws JspTagException
	{
		// Get the writer.
		JspWriter out = pageContext.getOut();
		String inputType = getType();

		try
		{
			if (hasBorder)
			{
				startTag();
				out.println(">");
			}

			// Get the items select so far.
			Set<String> selectedItems = getSelectedItems();

			// Loop through the options
			for (ListItem code : getOptions())
			{
				String codeId = code.getListItemId();
				String description = code.getListItemDesc();
				
				out.print("<");
				out.print(TAG_OPTION);
				outputProperty(PROPERTY_NAME, name);
				outputProperty(InputTag.PROPERTY_TYPE, inputType);
				outputProperty(PROPERTY_VALUE, codeId);
				outputUnaryProperty(PROPERTY_CHECKED, selectedItems.contains(codeId));
				outputProperty(PROPERTY_ON_CLICK, onClick);
				out.print(">");

				out.print(description);
				out.println(delimiter);
			}

			if (!hasBorder)
			{
				out.print("</");
				out.print(TAG_NAME);
				out.println(">");
			}
		}

		catch (IOException ex) { throw new JspTagException(ex.getMessage()); }
		catch (ClassCastException ex)
		{
			throw new JspTagException("SelectTag: One of the selectable " +
				"options was not of type ListItem.");
		}

		return SKIP_BODY;
	}

	/*******************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************/

	/** Accessor method - gets the name of the tag. Returns <CODE>null</CODE>
	    since, the group does not get a name, but the individual
	    options.
	*/
	public String getName() { return null; }

	/** Accessor method - gets the CSS class for the selection
		region.
	*/
	public String getCssClass()
	{
		return CSS_CLASS_SELECTION_REGION;
	}

	/** Accessor method - gets the click event of the tag. */
	public String onClick() { return onClick; }

	/** Accessor method - gets the delimiter property for each option
	    button.
	*/
	public String getDelimiter() { return delimiter; }

	/** Accessor method - gets the border indicator for the option buttons. */
	public boolean hasBorder() { return hasBorder; }

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the click event of the tag. */
	public void setOnClick(String newValue) { onClick = newValue; }

	/** Mutator method - sets the delimiter property for each option
	    button.
	*/
	public void setDelimiter(String newValue) { delimiter = newValue; }

	/** Mutator method - sets the border indicator for the option buttons. */
	public void setHasBorder(boolean newValue) { hasBorder = newValue; }

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the click event of the individual option
	    buttons.
	*/
	protected String onClick = null;

	/** Member variable - contains the delimiter property for each option
	    button.
	*/
	protected String delimiter;

	/** Member variable - indicator of whether to use a border for the option
	    buttons.
	*/
	protected boolean hasBorder;
}

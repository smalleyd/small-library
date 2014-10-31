package com.small.library.taglib;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/***********************************************************************
*
*	Select tag class that renders the HTML for a form select list.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/26/2002
*
***********************************************************************/

public class SelectTag extends SelectionTag
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - tag name. */
	public static final String TAG_NAME = "select";

	/** Constant - tag name for the OPTION tag. */
	public static final String TAG_OPTION = "option";

	/** Constant - property name for the multiple flag. */
	public static final String PROPERTY_MULTIPLE = "multiple";

	/** Constant - property name for the selected flag. */
	public static final String PROPERTY_SELECTED = "selected";

	/*******************************************************************
	*
	*	Constructors - must provide the default constructor to provide
	*   a way for the web container to load the tag class.
	*
	*******************************************************************/

	/** Constructor - default. */
	public SelectTag() {}

	/** Constructor - friend constructor for usage within other tags.
			@param pPageContext <I>PageContext</I> of the current request.
			@param strName Name of the form element.
			@param colOptions <I>Collection</I> of options of the form element.
			@param strValue Value of the form element.
			@param strClass CSS class of the form element.
			@param strStyle CSS style of the form element.
			@param strOnChange Change event method of the form element.
	*/
	public SelectTag(PageContext pPageContext, String strName, Collection<ListItem> colOptions,
		String strValue, String strClass, String strStyle,
		String strOnChange)
			throws JspTagException
	{
		setPageContext(pPageContext);
		setName(strName);
		setOptions(colOptions);
		setSelectedItem(strValue);
		setCssClass(strClass);
		setStyle(strStyle);
		setOnChange(strOnChange);
	}

	/*******************************************************************
	*
	*	PageElement methods
	*
	*******************************************************************/

	/** PddTagSupport method - gets the tag name. */
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

		try
		{
			startTag();
			outputProperty(PROPERTY_SIZE, size);
			outputUnaryProperty(PROPERTY_MULTIPLE, isMultiple);
			outputProperty(PROPERTY_ON_CHANGE, onChange);
			outputProperty(PROPERTY_ON_DOUBLE_CLICK, onDoubleClick);
			out.println(">");

			// Get the items select so far.
			Set<String> selectedItems = getSelectedItems();

			// Output the first entry if one has been supplied.
			// Pass blank string as value. Otherwise uses caption of the option
			// in the Post.
			if (null != firstEntry)
				outputOption(out, "",
					(0 == selectedItems.size()), firstEntry);

			// Get the selectable options.
			Collection<ListItem> options = getOptions();
			if ((null == options) || (0 == options.size()))
			{
				outputEndingTag(out);
				return SKIP_BODY;
			}

			// Loop through the options
			for (ListItem code : options)
			{
				String codeId = code.getListItemId();

				outputOption(out, codeId,
					selectedItems.contains(codeId),
					code.getListItemDesc());
			}

			outputEndingTag(out);
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
	*	Helper method
	*
	*******************************************************************/

	/** Helper method - output ending tag. */
	private void outputEndingTag(JspWriter out) throws IOException
	{
		out.print("</");
		out.print(TAG_NAME);
		out.println(">");
	}

	/** Helper method - outputs an individual select list option. */
	private void outputOption(JspWriter out,
		String value, boolean isSelected, String description)
			throws IOException
	{
			out.print("<");
			out.print(TAG_OPTION);
			outputProperty(PROPERTY_VALUE, value);
			outputUnaryProperty(PROPERTY_SELECTED, isSelected);
			out.print(">");

			out.print(description);

			out.print("</");
			out.print(TAG_OPTION);
			out.println(">");
	}

	/*******************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************/

	/** Accessor method - gets the first entry caption of the select list. */
	public String getFirstEntry() { return firstEntry; }

	/** Accessor method - gets the size of the select list. */
	public int getSize() { return size; }

	/** Accessor method - gets the multiple flag of the select list. */
	public boolean isMultiple() { return isMultiple; }

	/** Accessor method - gets the change event of the select list. */
	public String getOnChange() { return onChange; }

	/** Accessor method - gets the double click event of the select list. */
	public String getOnDoubleClick() { return onDoubleClick; }

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the first entry caption of the select list. */
	public void setFirstEntry(String newValue) { firstEntry = newValue; }

	/** Mutator method - sets the size of the select list. */
	public void setSize(int newValue) { size = newValue; }

	/** Mutator method - sets the multiple flag of the select list. */
	public void setMultiple(boolean newValue) { isMultiple = newValue; }

	/** Mutator method - sets the change event of the select list. */
	public void setOnChange(String newValue) { onChange = newValue; }

	/** Mutator method - sets the double click event of the select list. */
	public void setOnDoubleClick(String newValue) { onDoubleClick = newValue; }

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the first entry caption of the
	    select list.
	*/
	private String firstEntry = null;

	/** Member variable - contains the size of the select list. */
	private int size = NULL_VALUE_INT;

	/** Member variable - contains the multiple flag of the select list. */
	private boolean isMultiple = false;

	/** Member variable - contains the change event of the select list. */
	private String onChange = null;

	/** Member variable - contains the double click event of the select list. */
	private String onDoubleClick = null;
}

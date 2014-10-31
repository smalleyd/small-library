package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Class that represents HTML select lists (i.e. Combo boxes and Listboxes).
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public class SelectList extends FormElement
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - Element name's prefix. */
	public static final String PREFIX = "lst";

	/** Constant - Tag's element name. */
	public static final String TAG = "SELECT";

	/** Constant - Attribute name of the Size attribute. */
	public static final String ATTRIBUTE_SIZE = "SIZE";

	/** Constant - Attribute name of the Multiple attribute. */
	public static final String ATTRIBUTE_MULTIPLE = " MULTIPLE";

	/** Constant - Attribute name of the onChange attribute. */
	public static final String ATTRIBUTE_ONCHANGE = "onChange";

	/** Constant - Attribute name of the Width attribute. */
	public static final String ATTRIBUTE_WIDTH = "WIDTH";

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object populated with default values. */
	public SelectList() { this(null); }

	/** Constructor - constructs an object populated with default values.
		@param pList Select list options.
	*/
	public SelectList(IList pList)
	{
		this(null, null, ATTR_VALUE_NO_VALUE, false,
			null, ATTR_VALUE_NO_VALUE, pList);
	}

	/** Constructor - constructs an object with a Name.
		@param strName Name of the select list.
		@param strHeader Header attribute of the select list.
		@param nSize Size attribute of the select list. Use "-1" for no value.
		@param bIsMultiple Multiple attribute of the select list.
		@param strOnChange onChange attribute of the select list.
		@param nWidth Width attribute of the select list. Use "-1" for no value.
		@param pList Select list options.
	*/
	public SelectList(String strName,
		String strHeader, int nSize, boolean bIsMultiple,
		String strOnChange, int nWidth, IList pList)
	{ this(strName, null, strHeader, nSize, bIsMultiple, strOnChange, nWidth, pList); }

	/** Constructor - constructs an object with a Name, a Default Value attribute.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param strHeader Header attribute of the select list.
		@param nSize Size attribute of the select list. Use "-1" for no value.
		@param bIsMultiple Multiple attribute of the select list.
		@param strOnChange onChange attribute of the select list.
		@param nWidth Width attribute of the select list. Use "-1" for no value.
		@param pList Select list options.
	*/
	public SelectList(String strName, String strDefaultValue,
		String strHeader, int nSize, boolean bIsMultiple,
		String strOnChange, int nWidth, IList pList)
	{ this(strName, strDefaultValue, null, null, strHeader, nSize, bIsMultiple, strOnChange, nWidth, pList); }

	/** Constructor - constructs an object with a Name, a Default Value attribute,
	    a Cascading Stylesheet class name, and a Cascading Stylesheet style string.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param strHeader Header attribute of the select list.
		@param nSize Size attribute of the select list. Use "-1" for no value.
		@param bIsMultiple Multiple attribute of the select list.
		@param strOnChange onChange attribute of the select list.
		@param nWidth Width attribute of the select list. Use "-1" for no value.
		@param pList Select list options.
	*/
	public SelectList(String strName, String strDefaultValue,
		String strCSSClass, String strCSSStyle,
		String strHeader, int nSize, boolean bIsMultiple,
		String strOnChange, int nWidth, IList pList)
	{
		super(PREFIX + strName, strDefaultValue, strCSSClass, strCSSStyle);

		m_strHeader = strHeader;
		m_nSize = nSize;
		m_bIsMultiple = bIsMultiple;
		m_strOnChange = strOnChange;
		m_nWidth = nWidth;
		m_List = pList;
	}

	/******************************************************************************
	*
	*	Required methods: Input
	*
	******************************************************************************/

	/** Action method - creates the SelectList form element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter)
		throws IOException
	{
		create(pWriter, m_List, getDefaultValue());
	}

	/** Action method - creates the SelectList form element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param pList <I>IList</I> object used to build the select list.
	*/
	public void create(Writer pWriter, IList pList)
		throws IOException
	{
		create(pWriter, pList, getDefaultValue());
	}

	/** Action method - creates the SelectList form element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strValue Value of the selected item.
	*/
	public void create(Writer pWriter, String strValue)
		throws IOException
	{
		create(pWriter, m_List, strValue);
	}

	/** Action method - creates the SelectList form element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param pList <I>IList</I> object used to build the select list.
		@param strValue Value of the selected item.
	*/
	public void create(Writer pWriter, IList pList, String strValue)
		throws IOException
	{
		openTag(pWriter, TAG);

		String strMultiple = "";
		String strHeader = getHeader();

		if (isMultiple()) strMultiple = ATTRIBUTE_MULTIPLE;

		writeAttribute(pWriter, ATTRIBUTE_SIZE, getSize());
		write(pWriter, strMultiple);
		writeAttribute(pWriter, ATTRIBUTE_WIDTH, getWidth());
		writeAttribute(pWriter, ATTRIBUTE_ONCHANGE, getOnChange());

		closeTag(pWriter);

		if (null != strHeader)
			addItem(pWriter, "", strHeader,
				(((null == strValue) || (0 == strValue.length())) ? true : false));

		for (int i = 0; i < pList.getListCount(); i++)
			addItem(pWriter, pList.getListItem(i), strValue);

		writeTagClosing(pWriter, "SELECT");

		writeNewLine(pWriter);
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Helper method - Adds an individual list item.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param pListItem Individual list item to add to the Selection List.
		@param strDefault The default value supplied in the create methods.
	*/
	protected void addItem(Writer pWriter, IListItem pListItem,
		String strDefault) throws IOException
	{
		String strID = pListItem.getListItemID();

		addItem(pWriter, strID, pListItem.getListItemDesc(), strID.equals(strDefault));
	}

	/** Helper method - Adds an individual list item.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strID The value of the list item.
		@param strDesc The caption of the list item.
		@param bSelected Should this list item be selected?
	*/
	protected void addItem(Writer pWriter, String strID, String strDesc,
		boolean bSelected) throws IOException
	{
		write(pWriter, "<OPTION");
		writeAttribute(pWriter, "VALUE", strID);

		if (bSelected)
			write(pWriter, " SELECTED");

		closeTag(pWriter);

		write(pWriter, strDesc);

		writeTagClosing(pWriter, "OPTION");
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the select list header. */
	public String getHeader() { return m_strHeader; }

	/** Accessor method - gets the List of the select list options. */
	public IList getList() { return m_List; }

	/** Accessor method - gets the Size attribute of the select list. */
	public int getSize() { return m_nSize; }

	/** Accessor method - gets the Multiple attribute of the select list. */
	public boolean isMultiple() { return m_bIsMultiple; }

	/** Accessor method - gets the onChange attribute of the select list. */
	public String getOnChange() { return m_strOnChange; }

	/** Accessor method - gets the Width attribute of the select list. */
	public int getWidth() { return m_nWidth; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the select list header. */
	public void setHeader(String strValue) { m_strHeader = strValue; }

	/** Mutator method - sets the List of the select list options. */
	public void setList(IList pNewValue) { m_List = pNewValue; }

	/** Mutator method - sets the Size attribute of the select list. */
	public void setSize(int nNewValue) { m_nSize = nNewValue; }

	/** Mutator method - sets the Multiple attribute of the select list. */
	public void isMultiple(boolean bNewValue) { m_bIsMultiple = bNewValue; }

	/** Mutator method - sets the onChange attribute of the select list. */
	public void setOnChange(String strNewValue) { m_strOnChange = strNewValue; }

	/** Mutator method - sets the Width attribute of the select list. */
	public void setWidth(int nNewValue) { m_nWidth = nNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the select list header. */
	private String m_strHeader = null;

	/** Member variable - contains the List of the select list options. */
	private IList m_List = null;

	/** Member variable - contains the Size attribute of the select list. */
	private int m_nSize = 0;

	/** Member variable - contains the Multiple attribute of the select list. */
	private boolean m_bIsMultiple = false;

	/** Member variable - contains the onChange attribute of the select list. */
	private String m_strOnChange = null;

	/** Member variable - contains the Width attribute of the select list. */
	private int m_nWidth = 0;
}

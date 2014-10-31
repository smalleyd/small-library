package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Base class for radio buttons and checkboxes.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public abstract class OptionButton extends Input
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - Attribute name of the onClick attribute. */
	public static final String ATTRIBUTE_ONCLICK = "onClick";

	/** Constant - Attribute name of the Selected attribute. */
	public static final String ATTRIBUTE_SELECTED = " SELECTED";

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the option button.
		@param strType Type attribute of the option button.
		@param strOnClick onClick attribute of the option button.
		@param strDelimiter Delimiter attribute of the option button.
		@param pList Option button items.
	*/
	public OptionButton(String strName, String strType,
		String strOnClick, String strDelimiter, IList pList)
	{ this(strName, strType, null, strOnClick, strDelimiter, pList); }

	/** Constructor - constructs an object with a Name, a Default Value attribute.
		@param strName Name of the option button.
		@param strType Type attribute of the option button.
		@param strDefaultValue Default Value attribute of the input element.
		@param strOnClick onClick attribute of the option button.
		@param strDelimiter Delimiter attribute of the option button.
		@param pList Option button items.
	*/
	public OptionButton(String strName, String strType, String strDefaultValue,
		String strOnClick, String strDelimiter, IList pList)
	{ this(strName, strType, strDefaultValue, null, null, strOnClick, strDelimiter, pList); }

	/** Constructor - constructs an object with a Name, a Default Value attribute,
	    a Cascading Stylesheet class name, and a Cascading Stylesheet style string.
		@param strName Name of the option button.
		@param strType Type attribute of the option button.
		@param strDefaultValue Default Value attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param strOnClick onClick attribute of the option button.
		@param strDelimiter Delimiter attribute of the option button.
		@param pList Option button items.
	*/
	public OptionButton(String strName, String strType, String strDefaultValue,
		String strCSSClass, String strCSSStyle,
		String strOnClick, String strDelimiter, IList pList)
	{
		super(strName, strType, strDefaultValue, strCSSClass, strCSSStyle);

		m_strOnClick = strOnClick;
		m_strDelimiter = strDelimiter;
		m_List = pList;
	}

	/******************************************************************************
	*
	*	Required methods: Input
	*
	******************************************************************************/

	/** Action method - creates the option buttons.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		create(pWriter, m_List, getDefaultValue());
	}

	/** Action method - creates a list of option buttons.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter, IList pList, String strValue)
		throws IOException
	{
		int nCount = pList.getListCount();

		for (int i = 0; i < nCount; i++)
		{
			IListItem pListItem = pList.getListItem(i);

			create(pWriter, pListItem, pListItem.getListItemID().equals(strValue));

			if (null != m_strDelimiter)
				write(m_strDelimiter);
		}
	}

	/** Action method - creates an individual option button.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter, IListItem pListItem, boolean bIsSelected)
		throws IOException
	{
		openTag(pWriter);

		writeAttribute(pWriter, ATTRIBUTE_ONCLICK, getOnClick());

		if (bIsSelected)
			write(pWriter, ATTRIBUTE_SELECTED);

		closeTag(pWriter);
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the list of options. */
	public IList getList() { return m_List; }

	/** Accessor method - gets the onClick attribute of the option buttons. */
	public String getOnClick() { return m_strOnClick; }

	/** Accessor method - gets the Delimiter attribute of the option buttons. */
	public String getDelimiter() { return m_strDelimiter; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the list of options.*/
	public void setList(IList pNewValue) { m_List = pNewValue; }

	/** Mutator method - sets the onClick attribute of the option buttons.*/
	public void setOnClick(String strNewValue) { m_strOnClick = strNewValue; }

	/** Mutator method - sets the Delimiter attribute of the option buttons.*/
	public void setDelimiter(String strNewValue) { m_strDelimiter = strNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the list of options. */
	private IList m_List = null;

	/** Member variable - contains the onClick attribute of the option buttons. */
	private String m_strOnClick = null;

	/** Member variable - contains the Delimiter attribute of the option buttons. */
	private String m_strDelimiter = null;
}

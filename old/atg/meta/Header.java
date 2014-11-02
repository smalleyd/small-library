package com.small.library.atg.meta;

import org.w3c.dom.*;

import com.small.library.xml.XMLHelper;

/****************************************************************************************
*
*	Meta class that represents an ATG SQL Repository Header. An Repository
*	meta object contains a Header meta object.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/8/2002
*
****************************************************************************************/

public class Header
{
	/*******************************************************************************
	*
	*	Constants
	*
	*******************************************************************************/

	/** Constant - tag name of the Header element. */
	public static final String TAG_NAME = "header";

	/** Constant - element name of the Header's name. */
	public static final String ELEMENT_NAME = "name";

	/** Constant - element name of the Header's author. */
	public static final String ELEMENT_AUTHOR = "author";

	/** Constant - element name of the Header's version. */
	public static final String ELEMENT_VERSION = "version";

	/** Constant - element name of the Header's description. */
	public static final String ELEMENT_DESCRIPTION = "description";

	/*******************************************************************************
	*
	*	Constructors
	*
	*******************************************************************************/

	/** Constructor - constructs a populated object.
		@param node Node containing the header information.
	*/
	public Header(Node node)
		throws DynamoMetaException
	{
		m_strName = XMLHelper.findNodeText(node, ELEMENT_NAME);
		m_strAuthor = XMLHelper.findNodeText(node, ELEMENT_AUTHOR);
		m_strVersion = XMLHelper.findNodeText(node, ELEMENT_VERSION);
		m_strDescription = XMLHelper.findNodeText(node, ELEMENT_DESCRIPTION);

		if (null != m_strDescription)
			m_strDescription = m_strDescription.trim();
	}

	/*******************************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************************/

	/** Accessor method - gets the name of the header. */
	public String getName() { return m_strName; }

	/** Accessor method - gets the author of the header. */
	public String getAuthor() { return m_strAuthor; }

	/** Accessor method - gets the version of the header. */
	public String getVersion() { return m_strVersion; }

	/** Accessor method - gets the description of the header. */
	public String getDescription() { return m_strDescription; }

	/** Accessor method - gets the <I>String</I> representation of the object.
		@return the name property.
	*/
	public String toString() { return m_strName; }

	/*******************************************************************************
	*
	*	Member variables
	*
	*******************************************************************************/

	/** Member variable - represents the name of the header. */
	private String m_strName = null;

	/** Member variable - represents the author of the header. */
	private String m_strAuthor = null;

	/** Member variable - represents the version of the header. */
	private String m_strVersion = null;

	/** Member variable - represents the description of the header. */
	private String m_strDescription = null;
}

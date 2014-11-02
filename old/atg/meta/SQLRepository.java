package com.small.library.atg.meta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.*;

import com.small.library.xml.XMLHelper;

/*************************************************************************************
*
*	Meta class that represents the SQL Repository object. It contains
*	a <I>Header</I> and a <I>Map</I> of <I>ItemDescriptor</I> objects.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/8/2002
*
*************************************************************************************/

public class SQLRepository
{
	/*****************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - tag name of the SQL Repository element. */
	public static final String TAG_NAME = "gsa-template";

	/*****************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs a populated object.
		@param document XML Document that represents the SQL Repository.
	*/
	public SQLRepository(Document document)
		throws DynamoMetaException
	{
		this(document.getDocumentElement());
	}

	/** Constructor - constructs a populated object.
		@param node Node that contains the SQL Repository information.
	*/
	public SQLRepository(Node node)
		throws DynamoMetaException
	{
		if (!(node instanceof Element))
			throw new DynamoMetaException("SQL Repository (gsa-template) node must be " +
				"of type Element.");

		Node nodeHeader = XMLHelper.findNode(node, Header.TAG_NAME);

		if (null != nodeHeader)
			m_Header = new Header(nodeHeader);

		NodeList listItemDescriptors = ((Element) node).getElementsByTagName(
			ItemDescriptor.TAG_NAME);
		int nItemDescriptors = listItemDescriptors.getLength();

		m_mapItemDescriptors = new HashMap(nItemDescriptors);

		for (int i = 0; i < nItemDescriptors; i++)
		{
			ItemDescriptor itemDescriptor = new ItemDescriptor(
				listItemDescriptors.item(i));

			// Assume that the first item descriptor is the default.
			if (itemDescriptor.isDefault() || (0 == i))
				m_DefaultItemDescriptor = itemDescriptor;
		}
	}

	/*****************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Header of the Repository. */
	public Header getHeader() { return m_Header; }

	/** Accessor method - gets an Item Descriptor based upon its name.
		@param strName name of the <I>ItemDescriptor</I>.
	*/
	public ItemDescriptor getItemDescriptor(String strName)
	{
		return (ItemDescriptor) m_mapItemDescriptors.get(strName);
	}

	/** Accessor method - gets a <I>Collection</I> of <I>ItemDescriptor</I>
	    objects.
	*/
	public Collection getItemDescriptors() { return m_mapItemDescriptors.values(); }

	/** Accessor method - gets the default <I>ItemDescriptor</I> object. */
	public ItemDescriptor getDefaultItemDescriptor() { return m_DefaultItemDescriptor; }

	/*****************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - represents the Header of the Repository. */
	private Header m_Header = null;

	/** Member variable - represents the Map of the Item Descriptor objects. */
	private Map m_mapItemDescriptors = null;

	/** Member variable - represents the default Item Descriptor. */
	private ItemDescriptor m_DefaultItemDescriptor = null;
}

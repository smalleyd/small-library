package com.small.library.atg.meta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.*;

import com.small.library.xml.XMLHelper;

/****************************************************************************************
*
*	Meta class that represents a SQL Repository Item Descriptor.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/8/2002
*
****************************************************************************************/

public class ItemDescriptor
{
	/*******************************************************************************
	*
	*	Constants
	*
	*******************************************************************************/

	/** Constant - tag name of the Item Descriptor element. */
	public static final String TAG_NAME = "item-descriptor";

	/** Constant - attribute name of the Item Descriptor's name. */
	public static final String ATTR_NAME = "name";

	/** Constant - attribute name of the Item Descriptor's default property. */
	public static final String ATTR_DEFAULT = "default";

	/** Constant - attribute value of the Item Descriptor's default property - true. */
	public static final String ATTR_VALUE_DEFAULT_TRUE = "true";

	/** Constant - attribute value of the Item Descriptor's default property - false. */
	public static final String ATTR_VALUE_DEFAULT_FALSE = "false";

	/*******************************************************************************
	*
	*	Constructors
	*
	*******************************************************************************/

	/** Constructor - constructs a populated object.
		@param node Node object that represents an Item Descriptor.
	*/
	public ItemDescriptor(Node node)
		throws DynamoMetaException
	{
		if (!(node instanceof Element))
			throw new DynamoMetaException("Item Descriptor node must be " +
				"of type Element.");

		NamedNodeMap nodeMap = node.getAttributes();

		m_strName = XMLHelper.getAttributeString(nodeMap, ATTR_NAME);

		if ((null == m_strName) || (0 == m_strName.length()))
			throw new DynamoMetaException("Item Descriptor is missing " +
				"a name attribute.");

		m_isDefault = ATTR_VALUE_DEFAULT_TRUE.equals(
			XMLHelper.getAttributeString(nodeMap, ATTR_DEFAULT));

		// Get the Table elements.
		NodeList listTables = ((Element) node).getElementsByTagName(Table.TAG_NAME);
		int nTables = listTables.getLength();

		m_mapTables = new HashMap(nTables);

		for (int i = 0; i < nTables; i++)
		{
			Table table = new Table(listTables.item(i));

			if ((0 == i) || (table.isPrimary()))
				m_PrimaryTable = table;

			m_mapTables.put(table.getName(), table);
		}

		// Get the Property elements.
		NodeList listProperties = ((Element) node).getElementsByTagName(Property.TAG_NAME);
		int nProperties = listProperties.getLength();

		m_mapProperties = new HashMap(nProperties);

		for (int i = 0; i < nProperties; i++)
		{
			Property property = new Property(listProperties.item(i));
			m_mapProperties.put(property.getName(), property);
		}
	}

	/*******************************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************************/

	/** Accessor method - gets the name of the Item Descriptor. */
	public String getName() { return m_strName; }

	/** Accessor method - indicates whether this Item Descriptor is the default
	    Item Descriptor within the repository.
	*/
	public boolean isDefault() { return m_isDefault; }

	/** Accessor method - gets <I>Table</I> object based on its name.
		@param strName name of the table.
	*/
	public Table getTable(String strName)
	{
		return (Table) m_mapTables.get(strName);
	}

	/** Accessor method - gets a <I>Collection</I> of <I>Table</I> objects. */
	public Collection getTables()
	{
		return m_mapTables.values();
	}

	/** Accessor method - gets the primary table. */
	public Table getPrimaryTable() { return m_PrimaryTable; }

	/** Accessor method - gets <I>Property</I> object based on its name.
		@param strName name of the property.
	*/
	public Property getProperty(String strName)
	{
		return (Property) m_mapProperties.get(strName);
	}

	/** Accessor method - gets a <I>Collection</I> of <I>Property</I> objects. */
	public Collection getProperties()
	{
		return m_mapProperties.values();
	}

	/** Accessor method - gets the <I>String</I> representation of the object.
		@return the name property.
	*/
	public String toString() { return m_strName; }

	/*******************************************************************************
	*
	*	Member variables
	*
	*******************************************************************************/

	/** Member variable - reference to the name of the Item Descriptor. */
	private String m_strName = null;

	/** Member variable - indicates whether this Item Descriptor is the default
	    Item Descriptor within the repository.
	*/
	private boolean m_isDefault = false;

	/** Member variable - map of the Table objects. */
	private Map m_mapTables = null;

	/** Member variable - reference to the primary table. */
	private Table m_PrimaryTable = null;

	/** Member variable - map of the Property objects. */
	private Map m_mapProperties = null;
}

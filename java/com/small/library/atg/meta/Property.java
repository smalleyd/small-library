package com.small.library.atg.meta;

import org.w3c.dom.*;

import com.small.library.xml.XMLHelper;

/****************************************************************************************
*
*	Meta class that represents an ATG SQL Repository Item Descriptor's
*	Property. An Item Descriptor meta object contains a map of Property meta
*	objects.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/8/2002
*
****************************************************************************************/

public class Property
{
	/********************************************************************************
	*
	*	Constants
	*
	********************************************************************************/

	/** Constant - tag name of the Property element. */
	public static final String TAG_NAME = "property";

	/** Constant - attribute name of the Propery's name. */
	public static final String ATTR_NAME = "name";

	/** Constant - attribute name of the Property's column name. */
	public static final String ATTR_COLUMN_NAME = "column-name";

	/** Constant - attribute name of the Property's column names. */
	public static final String ATTR_COLUMN_NAMES = "column-names";

	/** Constant - attribute name of the Property's data type. */
	public static final String ATTR_DATA_TYPE = "data-type";

	/** Constant - attribute name of the Property's data types. */
	public static final String ATTR_DATA_TYPES = "data-types";

	/** Constant - attribute name of the Property's item type. */
	public static final String ATTR_ITEM_TYPE = "item-type";

	/** Constant - attribute name of the Property's component item type. */
	public static final String ATTR_COMPONENT_ITEM_TYPE = "component-item-type";

	/** Constant - attribute name of the Property's external repository reference. */
	public static final String ATTR_REPOSITORY = "repository";

	/** Constant - attribute name of the Property's required flag. */
	public static final String ATTR_REQUIRED = "required";

	/** Constant - attribute value of the Property's required flag - true. */
	public static final String ATTR_VALUE_REQUIRED_TRUE = "true";

	/** Constant - attribute value of the Property's required flag - false. */
	public static final String ATTR_VALUE_REQUIRED_FALSE = "false";

	/********************************************************************************
	*
	*	Constructors
	*
	********************************************************************************/

	/** Constructor - constructs a populated object.
		@param node An XML node that contains the property values.
	*/
	public Property(Node node)
		throws DynamoMetaException
	{
		NamedNodeMap nodeMap = node.getAttributes();

		m_strName = XMLHelper.getAttributeString(nodeMap, ATTR_NAME);

		if (null == m_strName)
			throw new DynamoMetaException("Item Descriptor Property is missing " +
				"a name attribute.");

		m_strColumnName = XMLHelper.getAttributeString(nodeMap, ATTR_COLUMN_NAME);

		if (null == m_strColumnName)
			m_strColumnName = XMLHelper.getAttributeString(nodeMap, ATTR_COLUMN_NAMES);

		m_strDataType = XMLHelper.getAttributeString(nodeMap, ATTR_DATA_TYPE);

		if (null == m_strDataType)
			m_strDataType = XMLHelper.getAttributeString(nodeMap, ATTR_DATA_TYPES);

		m_strItemType = XMLHelper.getAttributeString(nodeMap, ATTR_ITEM_TYPE);

		if (null == m_strItemType)
			m_strItemType = XMLHelper.getAttributeString(nodeMap, ATTR_COMPONENT_ITEM_TYPE);

		m_strRepository = XMLHelper.getAttributeString(nodeMap, ATTR_REPOSITORY);
		m_isRequired = ATTR_VALUE_REQUIRED_TRUE.equals(
			XMLHelper.getAttributeString(nodeMap, ATTR_REQUIRED));
	}

	/********************************************************************************
	*
	*	Accessor methods
	*
	********************************************************************************/

	/** Accessor method - gets the name of the property. */
	public String getName() { return m_strName; }

	/** Accessor method - gets the column name. */
	public String getColumnName() { return m_strColumnName; }

	/** Accessor method - gets the data type of the property. */
	public String getDataType() { return m_strDataType; }

	/** Accessor method - gets the item-type or component-item-type. */
	public String getItemType() { return m_strItemType; }

	/** Accessor method - gets the external repository. */
	public String getRepository() { return m_strRepository; }

	/** Accessor method - indicates whether the property value is required. */
	public boolean isRequired() { return m_isRequired; }

	/** Accessor method - gets the <I>String</I> representation of the object.
		@return the name property.
	*/
	public String toString() { return m_strName; }

	/********************************************************************************
	*
	*	Member variables
	*
	********************************************************************************/

	/** Member variable - represents the name of the property. */
	private String m_strName = null;

	/** Member variable - reference to the column name. */
	private String m_strColumnName = null;

	/** Member variable - reference to the data type of the property. */
	private String m_strDataType = null;

	/** Member variable - reference to the item-type or the component-item-type. */
	private String m_strItemType = null;

	/** Member variable - reference to the external repository. */
	private String m_strRepository = null;

	/** Member variable - indicates whether the property value is required. */
	private boolean m_isRequired = false;
}

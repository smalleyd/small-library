package com.small.library.atg.meta;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.w3c.dom.*;

import com.small.library.xml.XMLHelper;

/****************************************************************************************
*
*	Meta class that represents an ATG SQL Repository Item Descriptor's
*	Table. An Item Descriptor meta object contains a map of Table meta
*	objects.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/29/2002
*
****************************************************************************************/

public class Table
{
	/********************************************************************************
	*
	*	Constants
	*
	********************************************************************************/

	/** Constant - tag name of the Property element. */
	public static final String TAG_NAME = "table";

	/** Constant - attribute name of the table's name. */
	public static final String ATTR_NAME = "name";

	/** Constant - attribute name of the table's ID columns. */
	public static final String ATTR_ID_COLUMNS = "id-column-names";

	/** Constant - attribute name of the table's type. */
	public static final String ATTR_TYPE = "type";

	/** Constant - attribute value for table type of "primary". */
	public static final String ATTR_TYPE_PRIMARY = "primary";

	/** Constant - attribute value for table type of "auxiliary". */
	public static final String ATTR_TYPE_AUXILIARY = "auxiliary";

	/** Constant - attribute value for table type of "multi". */
	public static final String ATTR_TYPE_MULTI = "multi";

	/********************************************************************************
	*
	*	Constructors
	*
	********************************************************************************/

	/** Constructor - constructs a populated object.
		@param node An XML node that contains the table values.
	*/
	public Table(Node node)
		throws DynamoMetaException
	{
		NamedNodeMap nodeMap = node.getAttributes();

		m_strName = XMLHelper.getAttributeString(nodeMap, ATTR_NAME);

		if (null == m_strName)
			throw new DynamoMetaException("Item Descriptor Table is missing " +
				"a name attribute.");

		// Get the type.
		m_strType = XMLHelper.getAttributeString(nodeMap, ATTR_TYPE);
		m_isPrimary = ATTR_TYPE_PRIMARY.equals(m_strType);

		// Get and parse the columns.
		String strIdColumns = XMLHelper.getAttributeString(nodeMap, ATTR_ID_COLUMNS);

		// Primary and auxiliary must have an ID column.
		if (null != strIdColumns)
		{
			StringTokenizer tokens = new StringTokenizer(strIdColumns, ",");
			m_strIdColumns = new String[tokens.countTokens()];

			for (int i = 0; tokens.hasMoreTokens(); i++)
				m_strIdColumns[i] = tokens.nextToken().trim();
		}

		else if (!ATTR_TYPE_MULTI.equals(m_strType))
			throw new DynamoMetaException("The table, \"" + m_strName +
				"\", does not contain a \"" + ATTR_ID_COLUMNS +
				"\" attribute.");
	}

	/********************************************************************************
	*
	*	Accessor methods
	*
	********************************************************************************/

	/** Accessor method - gets the name of the table. */
	public String getName() { return m_strName; }

	/** Accessor method - gets the array of ID column names. */
	public String[] getIdColumns()
	{
		if (null == m_strIdColumns)
			return null;

		return (String[]) m_strIdColumns.clone();
	}

	/** Accessor method - gets a <I>Set</I> of the ID column names. */
	public Set getIdColumnsSet()
	{
		if (null == m_strIdColumns)
			return null;

		// Always return a new Set to avoid changes by the
		// caller.
		Set idColumns = new HashSet(m_strIdColumns.length);

		for (int i = 0; i < m_strIdColumns.length; i++)
			idColumns.add(m_strIdColumns[i]);

		return idColumns;
	}

	/** Accessor method - gets the type of table. */
	public String getType() { return m_strType; }

	/** Accessor method - indicates whether the table is the primary or
	    otherwise.
	*/
	public boolean isPrimary() { return m_isPrimary; }

	/********************************************************************************
	*
	*	Member variables
	*
	********************************************************************************/

	/** Member variable - reference to the name of the table. */
	private String m_strName = null;

	/** Member variable - an array of ID column names for the table. */
	private String[] m_strIdColumns = null;

	/** Member variable - type of table. */
	private String m_strType = null;

	/** Member variable - indicates whether the table is primary or
	    otherwise.
	*/
	private boolean m_isPrimary = false;
}

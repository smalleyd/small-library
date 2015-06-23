package com.small.library.ejb.gen;

import java.io.PrintWriter;
import java.sql.SQLException;

import com.small.library.generator.BaseTable;
import com.small.library.generator.ColumnInfo;
import com.small.library.generator.GeneratorException;
import com.small.library.metadata.*;

/************************************************************************************
*
*	Base class for the EJB Entity Bean generators.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/20/2002
*
***********************************************************************************/

public abstract class EntityBeanBase extends BaseTable
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - base EJB package name. */
	public static final String EJB_PACKAGE_NAME = "javax.ejb";

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public EntityBeanBase() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanBase(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable)
	{
		this(pWriter, strAuthor, pTable, null);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
	*/
	public EntityBeanBase(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName)
	{
		super(pWriter, strAuthor, pTable);

		m_strPackageName = strPackageName;
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
		@param version Represents the version of the resource.
	*/
	public EntityBeanBase(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName, String version)
	{
		this(pWriter, strAuthor, pTable, strPackageName);

		this.version = version;
	}

	/*****************************************************************************
	*
	*	Helper method
	*
	*****************************************************************************/

	/** Helper method - gets a column's object version of the column name.
	    Overrides the same method from <I>BaseTable</I>.
		@param column A table column object.
	*/
	public String getColumnObjectName(Columns.Record column)
	{
		String strName = column.getName();

		if ("descript".equalsIgnoreCase(strName))
			return "Name";

		return super.getColumnObjectName(column);
	}

	/** Helper method - populates the <CODE>protected</CODE> <I>ColumnInfo</I>
	    member variable (m_ColumnInfo).
	*/
	protected void populateColumnInfo()
		throws GeneratorException
	{
		try { m_ColumnInfo = getColumnInfo(); }
		catch (SQLException pEx) { throw new GeneratorException(pEx); }
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Accessor method - gets the package name of the wrapper class. */
	public String getPackageName() { return m_strPackageName; }

	/** Accessor method - gets the version number of the resource. */
	public String getVersion() { return version; }

	/** Helper method - gets the Java type of the primary key column. */
	public String getPkJavaType()
	{
		for (ColumnInfo i : m_ColumnInfo)
			if (i.isPartOfPrimaryKey)
				return i.javaType;

		return JAVA_OBJECT_TYPE_LONG;
	}

	/******************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	/** Mutator method - sets the package name of the wrapper class. */
	public void setPackageName(String newValue) { m_strPackageName = newValue; }

	/** Mutator method - sets the version number of the resource. */
	public void setVersion(String newValue) { version = newValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - reference to the package name of the wrapper class. */
	private String m_strPackageName = null;

	/** Member variable - represents the version of the resource. */
	private String version = null;

	/** Member variable - array of column information objects. */
	protected ColumnInfo[] m_ColumnInfo = null;
}

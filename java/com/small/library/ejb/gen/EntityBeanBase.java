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
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityBeanBase(PrintWriter writer,
		String author, Tables.Record table, String packageName)
	{
		super(writer, author, table);

		if (null != (m_strPackageName = packageName))
		{
			// Get the domain portion of the package name.
			final int len = packageName.length() - 1;
			int i = packageName.indexOf('.');
			if ((0 > i) || (len <= i))
				return;
			i = packageName.indexOf('.', i + 1);
			domainPackageName = (0 > i) ? packageName.substring(0) : packageName.substring(0, i);

			// Get the base/application portion of the package name.
			if ((0 > i) || (len <= i))
				return;
			int domainIndex = i;
			i = packageName.indexOf('.', i + 1);
			basePackageName = (0 > i) ? packageName.substring(0) : packageName.substring(0, i);

			appName = basePackageName.substring(domainIndex + 1, domainIndex + 2).toUpperCase();
			if (basePackageName.length() >= domainIndex + 3)
				appName+= basePackageName.substring(domainIndex + 2);
		}
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

	/** Accessor method - gets domain name portion of the package name. */
	public String getDomainPackageName() { return domainPackageName; }

	/** Accessor method - gets the base/application portion of the package name. */
	public String getBasePackageName() { return basePackageName; }

	/** Accessor method - gets the application name derived from the full package name. */
	public String getAppName() { return appName; }

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

	/** Mutator method - sets domain name portion of the package name. */
	public void setDomainPackageName(String newValue) { domainPackageName = newValue; }

	/** Mutator method - sets the base/application portion of the package name. */
	public void setBasePackageName(String newValue) { basePackageName = newValue; }

	/** Mutator method - sets the application name derived from the full package name. */
	public void setAppName(String newValue) { appName = newValue; }

	/** Mutator method - sets the version number of the resource. */
	public void setVersion(String newValue) { version = newValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - reference to the package name of the wrapper class. */
	private String m_strPackageName = null;

	/** Member variable - represents the domain name portion of the package name. */
	private String domainPackageName = null;

	/** Member variable - represents the base/application portion of the package name. */
	private String basePackageName = null;

	/** Member variables - represents the application name derived from the full package name. */
	public String appName = null;

	/** Member variable - represents the version of the resource. */
	private String version = null;

	/** Member variable - array of column information objects. */
	protected ColumnInfo[] m_ColumnInfo = null;
}

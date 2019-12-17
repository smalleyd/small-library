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
	public static final String EJB_PACKAGE_NAME = "javax.ejb";
	public static final String VERSION_DEFAULT = "1.0.1";

	private final String packageName;
	private final String domainPackageName;
	private final String basePackageName;
	public final String appName;
	private final String version;
	protected ColumnInfo[] columnInfo = null;

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityBeanBase(final PrintWriter writer,
		final String author, final Table table)
	{
		this(writer, author, table, null);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityBeanBase(final PrintWriter writer,
		final String author, final Table table, final String packageName)
	{
		this(writer, author, table, packageName, null);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version Represents the version of the resource.
	*/
	public EntityBeanBase(final PrintWriter writer,
		final String author, final Table table, final String packageName, final String version)
	{
		super(writer, author, table);

		this.version = version;

		if (null != (this.packageName = packageName))
		{
			// Get the domain portion of the package name.
			final int len = packageName.length() - 1;
			int i = packageName.indexOf('.');
			if ((0 > i) || (len <= i))
			{
				domainPackageName = basePackageName = appName = null;
				return;
			}
			i = packageName.indexOf('.', i + 1);
			domainPackageName = (0 > i) ? packageName.substring(0) : packageName.substring(0, i);

			// Get the base/application portion of the package name.
			if ((0 > i) || (len <= i))
			{
				basePackageName = appName = null;
				return;
			}
			int domainIndex = i;
			i = packageName.indexOf('.', i + 1);
			basePackageName = (0 > i) ? packageName.substring(0) : packageName.substring(0, i);

			String appName_ = basePackageName.substring(domainIndex + 1, domainIndex + 2).toUpperCase();
			if (basePackageName.length() >= domainIndex + 3)
				appName_+= basePackageName.substring(domainIndex + 2);
			appName = appName_;
		}
		else
			domainPackageName = basePackageName = appName = null;
	}

	/** Helper method - gets a column's object version of the column name.
	    Overrides the same method from <I>BaseTable</I>.
		@param column A table column object.
	*/
	public String getColumnObjectName(final Column column)
	{
		if ("descript".equalsIgnoreCase(column.name))
			return "Name";

		return super.getColumnObjectName(column);
	}

	/** Helper method - populates the <CODE>protected</CODE> <I>ColumnInfo</I>
	    member variable (columnInfo).
	*/
	protected void populateColumnInfo()
		throws GeneratorException
	{
		try { columnInfo = getColumnInfo(); }
		catch (SQLException ex) { throw new GeneratorException(ex); }
	}

	/** Accessor method - gets the package name of the wrapper class. */
	public String getPackageName() { return packageName; }

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
		for (final ColumnInfo i : columnInfo)
			if (i.isPartOfPrimaryKey)
				return i.javaType;

		return JAVA_OBJECT_TYPE_LONG;
	}

	protected String writeEquals(final ColumnInfo item)
	{
		if (item.isTime)
			return "DateUtils.truncatedEquals(" + item.memberVariableName + ", v." + item.memberVariableName + ", Calendar.SECOND)";

		if (item.isPrimitive)
			return "(" + item.memberVariableName + " == v." + item.memberVariableName + ")";

		return "Objects.equals(" + item.memberVariableName + ", v." + item.memberVariableName + ")";
	}
}

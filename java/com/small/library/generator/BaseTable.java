package com.small.library.generator;

import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.small.library.metadata.*;

/*********************************************************************************************
*
*	Base class for all generator classes that rely upon database table information.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 12/27/2000
*
*********************************************************************************************/

public abstract class BaseTable extends BaseJDBC
{
	public static final String PREFIX_ACCESSOR_METHOD = "get";
	public static final String PREFIX_ACCESSOR_METHOD_BOOL = "is";
	public static final String PREFIX_MUTATOR_METHOD = "set";
	public static final String PREFIX_WITH_METHOD = "with";

	private Table table = null;
	private String objectName = null;
	private List<Column> columns = null;
	private List<PrimaryKey> primaryKeys = null;
	private List<ForeignKey> importKeys = null;

	/** Constructor - constructs an empty object. */
	public BaseTable() { this(null, AUTHOR_DEFAULT, null); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public BaseTable(PrintWriter writer, String author, Table table)
	{
		super(writer, author);

		setTable(table);
	}

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public abstract String getOutputFileName(Table table)
		throws GeneratorException;

	/** Accessor method - gets the table object. */
	public Table getTable()	{ return table; }

	/** Accessor method - gets object name representation of the table name. */
	public String getObjectName() { return objectName; }

	/** Accessor method - gets the table's columns.
		@throw SQLException during loading of the column information.
	*/
	public List<Column> getColumns()
		throws SQLException
	{
		if (null == columns)
			columns = table.getColumns();

		return columns;
	}

	/** Accessor method - gets the table's primary keys. */
	public List<PrimaryKey> getPrimaryKeys()
		throws SQLException
	{
		if (null == primaryKeys)
			primaryKeys = table.getPrimaryKeys();

		return primaryKeys;
	}

	/** Accessor method - gets the table's imported foreign keys. */
	public List<ForeignKey> getImportedKeys() throws SQLException
	{
		if (null == importKeys)
			return (importKeys = table.getImportedKeys());

		return importKeys;
	}

	/** Mutator method - sets the table object. */
	public void setTable(Table value)
	{
		table = value;

		if (null == value)
			objectName = null;
		else
			objectName = createObjectName(value.name);

		columns = null;
		primaryKeys = null;
		importKeys = null;
	}

	/** Helper method - gets a member variable name based on a column.
		@param column A table column object.
	*/
	public String getMemberVariableName(final Column column)
	{
		return fromObjectNameToMemberName(getColumnObjectName(column));
	}

	/** Helper method - converts Object/Class names to member names.
	    Just lower case the first letter.
	*/
	public String fromObjectNameToMemberName(String objectName)
	{
		char[] chars = objectName.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);

		return new String(chars);
	}

	/** Helper method - gets a local variable name based on a column.
		@param column A table column object.
	*/
	public String getLocalVariableName(final Column column)
	{
		return getVariablePrefix(column) + getColumnObjectName(column);
	}

	/** Helper method - gets the accessor method name for a column. */
	public String getAccessorMethodName(ColumnInfo value)
	{
		if (value.isBoolean)
			return PREFIX_ACCESSOR_METHOD_BOOL + value.name;

		return PREFIX_ACCESSOR_METHOD + value.name;
	}

	/** Helper method - gets the mutator method name for a column. */
	public String getMutatorMethodName(ColumnInfo value)
	{
		if (value.columnName.startsWith("is_") && (3 < value.columnName.length()))
			return PREFIX_MUTATOR_METHOD + value.name.substring(2);

		return PREFIX_MUTATOR_METHOD + value.name;
	}

	/** Helper method - gets the "with" method name for a column. */
	public String getWithMethodName(ColumnInfo value)
	{
		return PREFIX_WITH_METHOD + value.name;
	}

	/** Helper method - indicates whether the column is an auto incrementing
	    column.
	*/
	public boolean isColumnAutoIncrementing(final Column value)
	{
		String typeName = value.typeName;
		String remarks = value.remarks;

		return (value.autoIncrement ||
				(-1 < typeName.indexOf("identity")) ||
		        (-1 < typeName.indexOf("COUNTER")) ||
			((null != remarks) && (-1 < remarks.indexOf("auto_increment")))) ? true : false;
	}

	/** Helper method - is the column part of the primary key.
		@param column A column record object.
	*/
	public boolean isPartOfPrimaryKey(final Column column)
		throws SQLException
	{
		return getPrimaryKeys().stream().filter(o -> column.name.equals(o.key)).findFirst().isPresent();
	}

	/** Helper method - gets the <I>ImportedKey</I> that represents the column.
		@param column A column record object.
		@return an <I>ImportedKey</I> object or <CODE>null</CODE> if the column
			is not an imported foreign key.
	*/
	public ForeignKey getImportedKey(final Column column) throws SQLException
	{
		return getImportedKeys().stream()
			.filter(o -> 1 == o.fks.size() && column.name.equals(o.fks.get(0).name))
			.findFirst().orElse(null);
	}

	/** Helper method - gets the object version of the column name as an imported key name.
	    Removes the foreign key suffix of "id", "i", or "c".
		@param column A column record object.
	*/
	public String getImportedKeyName(final Column column)
	{
		String name = column.name;

		// Find the last word in the name.
		int index = name.lastIndexOf("_");
		int size = name.length();

		if ((1 > index) || ((index + 1) == size))
			return name;

		return createObjectName(name.substring(0, index + 1));
	}

	/** Helper method - creates a <I>ColumnInfo</I> object from the column's
	    meta data
		@param column A column record object.
	*/
	public ColumnInfo getColumnInfo(final Column column)
	{
		final ColumnInfo info = new ColumnInfo();

		try
		{
			info.columnName = column.name;
			info.name = getColumnObjectName(column);
			info.size = column.size;
			info.decimalDigits = column.decimalDigits;
			info.isNullable = column.nullable;
			info.isBoolean = isBoolean(column);
			info.isCharacter = isCharacter(column);
			info.isString = isString(column);
			info.dataType = column.dataType;
			info.isPartOfPrimaryKey = isPartOfPrimaryKey(column);
			info.isAutoIncrementing = isColumnAutoIncrementing(column);
			info.dataTypeName = column.typeName;
			info.javaType = getJavaType(column);
			info.dynamoDbType = getDynamoDbType(column);
			info.isPrimitive = isPrimitive(info.javaType);
			info.jdbcTypeString = getJDBCType(column);
			info.variablePrefix = getVariablePrefix(column);
			info.jdbcMethodSuffix = getJdbcMethodSuffix(column);
			info.doesTypeRequireSize = doesTypeRequireSize(column);
			info.doesTypeRequireScale = doesTypeRequireScale(column);
			info.typeDefinition = getTypeDefinition(column);
			info.memberVariableName = getMemberVariableName(column);
			info.localVariableName = getLocalVariableName(column);
			info.accessorMethodName = getAccessorMethodName(info);
			info.mutatorMethodName = getMutatorMethodName(info);
			info.withMethodName = getWithMethodName(info);
	
			ForeignKey importedKey = getImportedKey(column);
	
			info.isImportedKey = ((null == importedKey) ? false : true);
	
			if (info.isImportedKey)
			{
				info.importedKeyName = getImportedKeyName(column);
				info.importedKeyMemberName = fromObjectNameToMemberName(
					info.importedKeyName);
				info.importedTableName = importedKey.pkTable;
				info.importedObjectName = createObjectName(info.importedTableName);
			}
	
			// If is primitive and primary key or if it is nullable or an imported foreign key, make the Java type an object.
			if (info.isPrimitive && (info.isPartOfPrimaryKey || info.isNullable || info.isImportedKey))
			{
				info.isPrimitive = false;
				info.javaType = fromPrimitiveToObject(info.javaType);
			}
		}
		catch (final SQLException ex) { throw new RuntimeException(ex); }

		return info;
	}

	/** Helper method - creates an array of <I>ColumnInfo</I> objects from the
	    columns' meta data.
	*/
	public ColumnInfo[] getColumnInfo()
		throws SQLException
	{
		return getColumnInfo(getColumns());
	}

	/** Helper method - creates an array of <I>ColumnInfo</I> objects from the
	    columns' meta data.
		@param columns An array column record objects.
	*/
	public ColumnInfo[] getColumnInfo(final List<Column> columns)
		throws SQLException
	{
		return columns.stream().map(o -> getColumnInfo(o)).collect(Collectors.toList()).toArray(new ColumnInfo[columns.size()]);
	}

	/******************************************************************************
	*
	*	Helper methods: Used by class entry methods (i.e. main)
	*
	*****************************************************************************/

	/** Helper method - generates resource based on a <I>BaseTable</I>
	    generator object and a collection of table record objects. Used by
	    class entry methods (i.e. main).
		@param generator <I>BaseTable</I> class used to generate a
			table resource.
		@param tables Collection of table record objects used to generate
			the resources.
		@param directory Directory to output the generated resources.
	*/
	public static void generateTableResources(BaseTable generator,
		List<Table> tables, File directory)
			throws GeneratorException, IOException
	{
		// Loop through the tables and generate.
		for (final Table table : tables)
		{
			// Set the table immediately so that the generator can
			// use the table object to generate the name.
			generator.setTable(table);

			File fileOutput = new File(directory, generator.getOutputFileName(table));
			PrintWriter writer = new PrintWriter(new FileWriter(fileOutput));

			generator.setWriter(writer);
			generator.generate();

			writer.close();
		}
	}

	/** Helper method - gets a <I>Tables</I> object based on the command line
	    arguments.
		@param args An array of command line arguments.
		@param firstArgument Index in the array that indicates the first
			data source argument. The argument order should be
			URL, User ID, Password, & Driver.
		@param tableNamePatternArg Table Name Pattern.
	*/
	protected static List<Table> extractTables(final String[] args,
		final int firstArgument, final String tableNamePattern)
			throws IllegalArgumentException, SQLException
	{
		final DBMetadata metadata = new DBMetadata(extractDataSource(args, firstArgument));

		if (StringUtils.isEmpty(tableNamePattern))
			return metadata.getTables();

		return metadata.getTables(tableNamePattern);
	}

	/** Helper method - gets a <I>Tables</I> object based on the command line
	    arguments.
		@param args An array of command line arguments.
		@param firstArgument Index in the array that indicates the first
			data source argument. The argument order should be
			URL, User ID, Password, & Driver.
		@param tableNamePatternArg Index in the array that indicates the
			Table Name Pattern argument.
	*/
	protected static List<Table> extractTables(String[] args,
		int firstArgument, int tableNamePatternArg)
			throws IllegalArgumentException,
				SQLException
	{
		return extractTables(args, firstArgument,
			extractArgument(args, tableNamePatternArg, null));
	}

	/** Helper method - gets a <I>Tables</I> object based on the command line
	    arguments.
		@param args An array of command line arguments.
		@param firstArgument Index in the array that indicates the first
			data source argument. The argument order should be
			URL, User ID, Password, & Driver.
	*/
	protected static List<Table> extractTables(String[] args,
		int firstArgument)
			throws IllegalArgumentException,
				SQLException
	{
		return extractTables(args, firstArgument, (String) null);
	}
}

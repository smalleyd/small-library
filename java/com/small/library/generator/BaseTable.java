package com.small.library.generator;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
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
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - accessor method prefix (get). */
	public static final String PREFIX_ACCESSOR_METHOD = "get";

	/** Constant - accessor method prefix for booleans (is). */
	public static final String PREFIX_ACCESSOR_METHOD_BOOL = "is";

	/** Constant - mutator method prefix (set). */
	public static final String PREFIX_MUTATOR_METHOD = "set";

	/** Constant - "with" method prefix (with). */
	public static final String PREFIX_WITH_METHOD = "with";

	/******************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public BaseTable() { this(null, AUTHOR_DEFAULT, null); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public BaseTable(PrintWriter pWriter, String strAuthor, Tables.Record pTable)
	{
		super(pWriter, strAuthor);

		setTable(pTable);
	}

	/******************************************************************************
	*
	*	Abstract methods
	*
	*****************************************************************************/

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public abstract String getOutputFileName(Tables.Record pTable)
		throws GeneratorException;

	/******************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Accessor method - gets the table object. */
	public Tables.Record getTable()	{ return table; }

	/** Accessor method - gets object name representation of the table name. */
	public String getObjectName() { return objectName; }

	/** Accessor method - gets the table's columns.
		@throw SQLException during loading of the column information.
	*/
	public Columns getColumns()
		throws SQLException
	{
		if (null == columns)
		{
			columns = table.getColumns();
			columns.load();
		}

		return columns;
	}

	/** Accessor method - gets the table's primary keys. */
	public PrimaryKeys getPrimaryKeys()
		throws SQLException
	{
		if (null == primaryKey)
		{
			primaryKey = table.getPrimaryKeys();
			primaryKey.load();
		}

		return primaryKey;
	}

	/** Accessor method - gets the table's imported foreign keys. */
	public ImportedKeys getImportedKeys()
		throws SQLException
	{
		if (null == importKeys)
		{
			importKeys = table.getImportedKeys();
			importKeys.load();
		}

		return importKeys;
	}

	/******************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	/** Mutator method - sets the table object. */
	public void setTable(Tables.Record pValue)
	{
		table = pValue;

		if (null == pValue)
			objectName = null;
		else
			objectName = createObjectName(pValue.getName());

		columns = null;
		primaryKey = null;
		importKeys = null;
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/** Helper method - gets a member variable name based on a column.
		@param column A table column object.
	*/
	public String getMemberVariableName(Columns.Record column)
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
	public String getLocalVariableName(Columns.Record column)
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
	public boolean isColumnAutoIncrementing(Columns.Record value)
	{
		String typeName = value.getTypeName();
		String remarks = value.getRemarks();

		return (value.isAutoIncrement() ||
				(-1 < typeName.indexOf("identity")) ||
		        (-1 < typeName.indexOf("COUNTER")) ||
			((null != remarks) && (-1 < remarks.indexOf("auto_increment")))) ? true : false;
	}

	/** Helper method - is the column part of the primary key.
		@param pColumn A column record object.
	*/
	public boolean isPartOfPrimaryKey(Columns.Record column)
		throws SQLException
	{
		String columnName = column.getName();
		PrimaryKeys records = getPrimaryKeys();

		for (int i = 0; i < records.size(); i++)
		{
			PrimaryKeys.Record record = (PrimaryKeys.Record) records.item(i);
			if (columnName.equals(record.getName()))
				return true;
		}

		return false;
	}

	/** Helper method - gets the <I>ImportedKey</I> that represents the column.
		@param pColumn A column record object.
		@return an <I>ImportedKey</I> object or <CODE>null</CODE> if the column
			is not an imported foreign key.
	*/
	public ImportedKeys.Record getImportedKey(Columns.Record pColumn)
		throws SQLException
	{
		String columnName = pColumn.getName();
		ImportedKeys importedKeys = getImportedKeys();

		for (int i = 0; i < importedKeys.size(); i++)
		{
			ImportedKeys.Record importedKey = (ImportedKeys.Record) importedKeys.item(i);
			Keys keys = importedKey.getColumns_FK();

			// If more than one key, will not implement as now.
			if (1 != keys.size())
				continue;

			if (columnName.equals(keys.item(0).getName()))
				return importedKey;
		}

		// None found.
		return null;
	}

	/** Helper method - gets the object version of the column name as an imported key name.
	    Removes the foreign key suffix of "id", "i", or "c".
		@param pColumn A column record object.
	*/
	public String getImportedKeyName(Columns.Record pColumn)
	{
		String name = pColumn.getName();

		// Find the last word in the name.
		int index = name.lastIndexOf("_");
		int size = name.length();

		if ((1 > index) || ((index + 1) == size))
			return name;

		return createObjectName(name.substring(0, index + 1));
	}

	/** Helper method - creates a <I>ColumnInfo</I> object from the column's
	    meta data
		@param pColumn A column record object.
	*/
	public ColumnInfo getColumnInfo(Columns.Record pColumn)
		throws SQLException
	{
		ColumnInfo info = new ColumnInfo();

		info.columnName = pColumn.getName();
		info.name = getColumnObjectName(pColumn);
		info.size = pColumn.getSize();
		info.decimalDigits = pColumn.getDecimalDigits();
		info.isNullable = pColumn.isNullable();
		info.isBoolean = isBoolean(pColumn);
		info.isCharacter = isCharacter(pColumn);
		info.isString = isString(pColumn);
		info.dataType = pColumn.getDataType();
		info.isPartOfPrimaryKey = isPartOfPrimaryKey(pColumn);
		info.isAutoIncrementing = isColumnAutoIncrementing(pColumn);
		info.dataTypeName = pColumn.getTypeName();
		info.javaType = getJavaType(pColumn);
		info.dynamoDbType = getDynamoDbType(pColumn);
		info.isPrimitive = isPrimitive(info.javaType);
		info.jdbcTypeString = getJDBCType(pColumn);
		info.variablePrefix = getVariablePrefix(pColumn);
		info.jdbcMethodSuffix = getJdbcMethodSuffix(pColumn);
		info.doesTypeRequireSize = doesTypeRequireSize(pColumn);
		info.doesTypeRequireScale = doesTypeRequireScale(pColumn);
		info.typeDefinition = getTypeDefinition(pColumn);
		info.memberVariableName = getMemberVariableName(pColumn);
		info.localVariableName = getLocalVariableName(pColumn);
		info.accessorMethodName = getAccessorMethodName(info);
		info.mutatorMethodName = getMutatorMethodName(info);
		info.withMethodName = getWithMethodName(info);

		ImportedKeys.Record importedKey = getImportedKey(pColumn);

		info.isImportedKey = ((null == importedKey) ? false : true);

		if (info.isImportedKey)
		{
			info.importedKeyName = getImportedKeyName(pColumn);
			info.importedKeyMemberName = fromObjectNameToMemberName(
				info.importedKeyName);
			info.importedTableName = importedKey.getTable_PK();
			info.importedObjectName = createObjectName(info.importedTableName);
		}

		// If is primitive and primary key or if it is nullable or an imported foreign key, make the Java type an object.
		if (info.isPrimitive && (info.isPartOfPrimaryKey || info.isNullable || info.isImportedKey))
		{
			info.isPrimitive = false;
			info.javaType = fromPrimitiveToObject(info.javaType);
		}

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
	public ColumnInfo[] getColumnInfo(Columns columns)
		throws SQLException
	{
		ColumnInfo[] info = new ColumnInfo[columns.size()];

		for (int i = 0; i < columns.size(); i++)
			info[i] = getColumnInfo((Columns.Record) columns.item(i));

		return info;
	}

	/******************************************************************************
	*
	*	Helper methods: Used by class entry methods (i.e. main)
	*
	*****************************************************************************/

	/** Helper method - generates resource based on a <I>BaseTable</I>
	    generator object and a collection of table record objects. Used by
	    class entry methods (i.e. main).
		@param pGenerator <I>BaseTable</I> class used to generate a
			table resource.
		@param pTables Collection of table record objects used to generate
			the resources.
		@param fileOutputDirectory Directory to output the generated resources.
	*/
	public static void generateTableResources(BaseTable pGenerator,
		Tables pTables, File fileOutputDirectory)
			throws GeneratorException, IOException
	{
		// Loop through the tables and generate.
		for (int i = 0; i < pTables.size(); i++)
		{
			Tables.Record pTable = (Tables.Record) pTables.item(i);

			// Set the table immediately so that the generator can
			// use the table object to generate the name.
			pGenerator.setTable(pTable);

			File fileOutput = new File(fileOutputDirectory, pGenerator.getOutputFileName(pTable));
			PrintWriter pWriter = new PrintWriter(new FileWriter(fileOutput));

			pGenerator.setWriter(pWriter);
			pGenerator.generate();

			pWriter.close();
		}
	}

	/** Helper method - gets a <I>Tables</I> object based on the command line
	    arguments.
		@param strArgs An array of command line arguments.
		@param nFirstArgument Index in the array that indicates the first
			data source argument. The argument order should be
			URL, User ID, Password, & Driver.
		@param tableNamePatternArg Table Name Pattern.
	*/
	protected static Tables extractTables(String[] strArgs,
		int nFirstArgument, String tableNamePattern)
			throws DataSourceException, IllegalArgumentException,
				SQLException
	{
		DataSource dataSource = extractDataSource(strArgs, nFirstArgument);

		if ((null == tableNamePattern) || (0 == tableNamePattern.length()))
			return new Tables(dataSource.getConnectionPool());
		else
			return new Tables(dataSource.getConnectionPool(),
				(String[]) null, tableNamePattern);
	}

	/** Helper method - gets a <I>Tables</I> object based on the command line
	    arguments.
		@param strArgs An array of command line arguments.
		@param nFirstArgument Index in the array that indicates the first
			data source argument. The argument order should be
			URL, User ID, Password, & Driver.
		@param tableNamePatternArg Index in the array that indicates the
			Table Name Pattern argument.
	*/
	protected static Tables extractTables(String[] strArgs,
		int nFirstArgument, int tableNamePatternArg)
			throws DataSourceException, IllegalArgumentException,
				SQLException
	{
		return extractTables(strArgs, nFirstArgument,
			extractArgument(strArgs, tableNamePatternArg, null));
	}

	/** Helper method - gets a <I>Tables</I> object based on the command line
	    arguments.
		@param strArgs An array of command line arguments.
		@param nFirstArgument Index in the array that indicates the first
			data source argument. The argument order should be
			URL, User ID, Password, & Driver.
	*/
	protected static Tables extractTables(String[] strArgs,
		int nFirstArgument)
			throws DataSourceException, IllegalArgumentException,
				SQLException
	{
		return extractTables(strArgs, nFirstArgument, (String) null);
	}

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - reference to the table record object that the
	    output is based on.
	*/
	private Tables.Record table = null;

	/** Member variable - object name representation of the table name. */
	private String objectName = null;

	/** Member variable - reference to the table's columns. */
	private Columns columns = null;

	/** Member variable - reference to the table's primary key. */
	private PrimaryKeys primaryKey = null;

	/** Member variable - reference to the table's imported foreign keys. */
	private ImportedKeys importKeys = null;
}

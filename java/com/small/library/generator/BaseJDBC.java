package com.small.library.generator;

import java.io.*;
import java.sql.Types;
import java.util.*;

import com.small.library.metadata.*;

/*********************************************************************************************
*
*	Base class for all generator classes that rely upon JDBC information.
*
*	@author David Small
*	@version 1.1.0.0
*	@date 7/15/2002
*
*********************************************************************************************/

public abstract class BaseJDBC extends Base
{
	/******************************************************************************
	*
	*	Static members
	*
	******************************************************************************/

	/** Static member - contains a mapping of column data type name to
	    JDBC method suffix. Used by JDBC <I>ResultSet</I> "getters" and
	    <I>PreparedStatement</I> "setters".
	*/
	private static Map<Integer, String> JDBC_METHOD_SUFFIXES = null;

	/** Static member - map of SQL data types (Types) to a Java data type. */
	private static Map<Integer, String> JAVA_TYPES = null;

	/** Static member - map of SQL data types (Types) to a DynamoDB data type. */
	private static Map<Integer, String> DYNAMO_DB_TYPES = null;

	/** Static member - map of SQL data types (Types) as a number to
	    a <I>String</String> representation.
	*/
	private static Map<Integer, String> JDBC_TYPES = null;

	/** Static member - map of SQL data types (Types) to a boolean
	    indicator of whether the type requires a size attribute.
	*/
	private static Map<Integer, Boolean> TYPES_REQUIRE_SIZE = null;

	/** Static member - map of SQL data types (Types) to a boolean
	    indicator of whether the type requires a scale (decimal digits) attribute.
	*/
	private static Map<Integer, Boolean> TYPES_REQUIRE_SCALE = null;

	/** Static member - map of SQL data types (Types) to a Hungarian notation
	    variable prefix.
	*/
	private static Map<Integer, String> VARIABLE_PREFIXES = null;

	/** Static member - set of Boolean objects indicating boolean attribute of data type. */
	private static Set<Integer> BOOLEAN_TYPES = new HashSet<>(Arrays.asList(Types.BIT, Types.BOOLEAN));

	/** Static member - map of Boolean objects indicating character attribute of data type. */
	private static Set<Integer> CHARACTER_ATTRIBUTE = new HashSet<>(Arrays.asList(Types.BINARY, Types.BLOB, Types.LONGVARBINARY,
		Types.VARBINARY, Types.CHAR, Types.VARCHAR, Types.CLOB, Types.LONGVARCHAR,
		Types.DATE, Types.TIME, Types.TIMESTAMP));

	/** Static member - set of SQL types that map to a Java String. */
	private static Set<Integer> STRING_TYPES = new HashSet<>(Arrays.asList(Types.CHAR, Types.CLOB, Types.LONGVARCHAR, Types.VARCHAR));

	/** Static constructor - initializes static member variables. */
	static
	{
		JDBC_METHOD_SUFFIXES = new HashMap<Integer, String>();

		JDBC_METHOD_SUFFIXES.put(Types.BIGINT, JDBC_METHOD_SUFFIX_LONG);
		JDBC_METHOD_SUFFIXES.put(Types.BINARY, "Bytes");
		JDBC_METHOD_SUFFIXES.put(Types.BLOB, "Blob");
		JDBC_METHOD_SUFFIXES.put(Types.LONGVARBINARY, "Bytes");
		JDBC_METHOD_SUFFIXES.put(Types.VARBINARY, "Bytes");
		JDBC_METHOD_SUFFIXES.put(Types.BIT, "Boolean");
		JDBC_METHOD_SUFFIXES.put(Types.BOOLEAN, "Boolean");
		JDBC_METHOD_SUFFIXES.put(Types.CHAR, "String");
		JDBC_METHOD_SUFFIXES.put(Types.VARCHAR, "String");
		JDBC_METHOD_SUFFIXES.put(Types.CLOB, "Clob");
		JDBC_METHOD_SUFFIXES.put(Types.LONGVARCHAR, "String");
		JDBC_METHOD_SUFFIXES.put(Types.DATE, "Date");
		JDBC_METHOD_SUFFIXES.put(Types.TIME, "Timestamp");
		JDBC_METHOD_SUFFIXES.put(Types.TIMESTAMP, "Timestamp");
		JDBC_METHOD_SUFFIXES.put(Types.DECIMAL, "Double");
		JDBC_METHOD_SUFFIXES.put(Types.DOUBLE, "Double");
		JDBC_METHOD_SUFFIXES.put(Types.NUMERIC, "Double");
		JDBC_METHOD_SUFFIXES.put(Types.REAL, "Double");
		JDBC_METHOD_SUFFIXES.put(Types.FLOAT, "Float");
		JDBC_METHOD_SUFFIXES.put(Types.INTEGER, JDBC_METHOD_SUFFIX_INTEGER);
		JDBC_METHOD_SUFFIXES.put(Types.SMALLINT, JDBC_METHOD_SUFFIX_SMALLINT);
		JDBC_METHOD_SUFFIXES.put(Types.TINYINT, JDBC_METHOD_SUFFIX_SMALLINT);
		JDBC_METHOD_SUFFIXES.put(Types.ARRAY, "Object");
		JDBC_METHOD_SUFFIXES.put(Types.DISTINCT, "Object");
		JDBC_METHOD_SUFFIXES.put(Types.JAVA_OBJECT, "Object");
		JDBC_METHOD_SUFFIXES.put(Types.NULL, "Object");
		JDBC_METHOD_SUFFIXES.put(Types.OTHER, "Object");
		JDBC_METHOD_SUFFIXES.put(Types.REF, "Object");
		JDBC_METHOD_SUFFIXES.put(Types.STRUCT, "Object");

		JAVA_TYPES = new HashMap<Integer, String>();

		JAVA_TYPES.put(Types.BIGINT, JAVA_TYPE_LONG);
		JAVA_TYPES.put(Types.BINARY, "byte[]");
		JAVA_TYPES.put(Types.BLOB, "byte[]");
		JAVA_TYPES.put(Types.LONGVARBINARY, "byte[]");
		JAVA_TYPES.put(Types.VARBINARY, "byte[]");
		JAVA_TYPES.put(Types.BIT, "boolean");
		JAVA_TYPES.put(Types.BOOLEAN, "boolean");
		JAVA_TYPES.put(Types.CHAR, "String");
		JAVA_TYPES.put(Types.VARCHAR, "String");
		JAVA_TYPES.put(Types.CLOB, "String");
		JAVA_TYPES.put(Types.LONGVARCHAR, "String");
		// JAVA_TYPES.put(Types.DATE, "java.sql.Date");
		JAVA_TYPES.put(Types.DATE, "Date");	// JPA implementation.
		JAVA_TYPES.put(Types.TIME, "java.sql.Time");
		// JAVA_TYPES.put(Types.TIMESTAMP, "java.sql.Timestamp");
		JAVA_TYPES.put(Types.TIMESTAMP, "Date");	// JPA implementation.
		JAVA_TYPES.put(Types.DECIMAL, "BigDecimal");
		JAVA_TYPES.put(Types.DOUBLE, "double");
		JAVA_TYPES.put(Types.NUMERIC, "BigDecimal");
		JAVA_TYPES.put(Types.REAL, "double");
		JAVA_TYPES.put(Types.FLOAT, "float");
		JAVA_TYPES.put(Types.INTEGER, JAVA_TYPE_INTEGER);
		JAVA_TYPES.put(Types.SMALLINT, JAVA_TYPE_SMALLINT);
		JAVA_TYPES.put(Types.TINYINT, JAVA_TYPE_SMALLINT);
		JAVA_TYPES.put(Types.ARRAY, "Object[]");
		JAVA_TYPES.put(Types.DISTINCT, "java.util.Map");
		JAVA_TYPES.put(Types.JAVA_OBJECT, "Object");
		JAVA_TYPES.put(Types.NULL, "Object");
		JAVA_TYPES.put(Types.OTHER, "Object");
		JAVA_TYPES.put(Types.REF, "Object");
		JAVA_TYPES.put(Types.STRUCT, "Object");

		DYNAMO_DB_TYPES = new HashMap<Integer, String>();

		DYNAMO_DB_TYPES.put(Types.BIGINT, "Long");
		DYNAMO_DB_TYPES.put(Types.BINARY, "Binary");
		DYNAMO_DB_TYPES.put(Types.BLOB, "Binary");
		DYNAMO_DB_TYPES.put(Types.LONGVARBINARY, "Binary");
		DYNAMO_DB_TYPES.put(Types.VARBINARY, "Binary");
		DYNAMO_DB_TYPES.put(Types.BIT, "Boolean");
		DYNAMO_DB_TYPES.put(Types.BOOLEAN, "Boolean");
		DYNAMO_DB_TYPES.put(Types.CHAR, "String");
		DYNAMO_DB_TYPES.put(Types.VARCHAR, "String");
		DYNAMO_DB_TYPES.put(Types.CLOB, "String");
		DYNAMO_DB_TYPES.put(Types.LONGVARCHAR, "String");
		// DYNAMO_DB_TYPES.put(Types.DATE, "Long");
		DYNAMO_DB_TYPES.put(Types.DATE, "Long");	// JPA implementation.
		DYNAMO_DB_TYPES.put(Types.TIME, "Long");
		// DYNAMO_DB_TYPES.put(Types.TIMESTAMP, "Long");
		DYNAMO_DB_TYPES.put(Types.TIMESTAMP, "Long");	// JPA implementation.
		DYNAMO_DB_TYPES.put(Types.DECIMAL, "Number");
		DYNAMO_DB_TYPES.put(Types.DOUBLE, "Double");
		DYNAMO_DB_TYPES.put(Types.NUMERIC, "Number");
		DYNAMO_DB_TYPES.put(Types.REAL, "Double");
		DYNAMO_DB_TYPES.put(Types.FLOAT, "Float");
		DYNAMO_DB_TYPES.put(Types.INTEGER, "Int");
		DYNAMO_DB_TYPES.put(Types.SMALLINT, "Short");
		DYNAMO_DB_TYPES.put(Types.TINYINT, "Short");
		DYNAMO_DB_TYPES.put(Types.ARRAY, "List");
		DYNAMO_DB_TYPES.put(Types.DISTINCT, "StringSet");
		DYNAMO_DB_TYPES.put(Types.JAVA_OBJECT, "");
		DYNAMO_DB_TYPES.put(Types.NULL, "Null");
		DYNAMO_DB_TYPES.put(Types.OTHER, "");
		DYNAMO_DB_TYPES.put(Types.REF, "");
		DYNAMO_DB_TYPES.put(Types.STRUCT, "");

		JDBC_TYPES = new HashMap<Integer, String>();

		JDBC_TYPES.put(Types.BIGINT, JDBC_TYPE_LONG);
		JDBC_TYPES.put(Types.BINARY, "BINARY");
		JDBC_TYPES.put(Types.BLOB, "BLOB");
		JDBC_TYPES.put(Types.LONGVARBINARY, "LONGVARBINARY");
		JDBC_TYPES.put(Types.VARBINARY, "VARBINARY");
		JDBC_TYPES.put(Types.BIT, "BIT");
		JDBC_TYPES.put(Types.BOOLEAN, "BOOLEAN");
		JDBC_TYPES.put(Types.CHAR, "CHAR");
		JDBC_TYPES.put(Types.VARCHAR, "VARCHAR");
		JDBC_TYPES.put(Types.CLOB, "CLOB");
		JDBC_TYPES.put(Types.LONGVARCHAR, "LONGVARCHAR");
		JDBC_TYPES.put(Types.DATE, "DATE");
		JDBC_TYPES.put(Types.TIME, "TIME");
		JDBC_TYPES.put(Types.TIMESTAMP, "TIMESTAMP");
		JDBC_TYPES.put(Types.DECIMAL, "DECIMAL");
		JDBC_TYPES.put(Types.DOUBLE, "DOUBLE");
		JDBC_TYPES.put(Types.NUMERIC, "NUMERIC");
		JDBC_TYPES.put(Types.REAL, "REAL");
		JDBC_TYPES.put(Types.FLOAT, "FLOAT");
		JDBC_TYPES.put(Types.INTEGER, JDBC_TYPE_INTEGER);
		JDBC_TYPES.put(Types.SMALLINT, JDBC_TYPE_SMALLINT);
		JDBC_TYPES.put(Types.TINYINT, "TINYINT");
		JDBC_TYPES.put(Types.ARRAY, "ARRAY");
		JDBC_TYPES.put(Types.DISTINCT, "DISTINCT");
		JDBC_TYPES.put(Types.JAVA_OBJECT, "JAVA_OBJECT");
		JDBC_TYPES.put(Types.NULL, "NULL");
		JDBC_TYPES.put(Types.OTHER, "OTHER");
		JDBC_TYPES.put(Types.REF, "REF");
		JDBC_TYPES.put(Types.STRUCT, "STRUCT");

		TYPES_REQUIRE_SIZE = new HashMap<Integer, Boolean>();

		TYPES_REQUIRE_SIZE.put(Types.BIGINT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.BINARY, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(Types.BLOB, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.LONGVARBINARY, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.VARBINARY, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(Types.BIT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.BOOLEAN, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.CHAR, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(Types.VARCHAR, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(Types.CLOB, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.LONGVARCHAR, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.DATE, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.TIME, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.TIMESTAMP, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.DECIMAL, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(Types.DOUBLE, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.NUMERIC, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(Types.REAL, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.FLOAT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.INTEGER, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.SMALLINT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.TINYINT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.ARRAY, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.DISTINCT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.JAVA_OBJECT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.NULL, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.OTHER, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.REF, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(Types.STRUCT, Boolean.FALSE);

		TYPES_REQUIRE_SCALE = new HashMap<Integer, Boolean>();

		TYPES_REQUIRE_SCALE.put(Types.BIGINT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.BINARY, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.BLOB, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.LONGVARBINARY, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.VARBINARY, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.BIT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.BOOLEAN, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.CHAR, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.VARCHAR, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.CLOB, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.LONGVARCHAR, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.DATE, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.TIME, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.TIMESTAMP, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.DECIMAL, Boolean.TRUE);
		TYPES_REQUIRE_SCALE.put(Types.DOUBLE, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.NUMERIC, Boolean.TRUE);
		TYPES_REQUIRE_SCALE.put(Types.REAL, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.FLOAT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.INTEGER, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.SMALLINT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.TINYINT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.ARRAY, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.DISTINCT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.JAVA_OBJECT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.NULL, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.OTHER, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.REF, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(Types.STRUCT, Boolean.FALSE);

		VARIABLE_PREFIXES = new HashMap<Integer, String>();

		VARIABLE_PREFIXES.put(Types.BIGINT, PREFIX_LONG);
		VARIABLE_PREFIXES.put(Types.BINARY, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.BLOB, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.LONGVARBINARY, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.VARBINARY, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.BIT, PREFIX_BOOLEAN);
		VARIABLE_PREFIXES.put(Types.BOOLEAN, PREFIX_BOOLEAN);
		VARIABLE_PREFIXES.put(Types.CHAR, PREFIX_STRING);
		VARIABLE_PREFIXES.put(Types.VARCHAR, PREFIX_STRING);
		VARIABLE_PREFIXES.put(Types.CLOB, PREFIX_STRING);
		VARIABLE_PREFIXES.put(Types.LONGVARCHAR, PREFIX_STRING);
		VARIABLE_PREFIXES.put(Types.DATE, PREFIX_DATE);
		VARIABLE_PREFIXES.put(Types.TIME, PREFIX_DATE);
		VARIABLE_PREFIXES.put(Types.TIMESTAMP, PREFIX_DATE);
		VARIABLE_PREFIXES.put(Types.DECIMAL, PREFIX_DOUBLE);
		VARIABLE_PREFIXES.put(Types.DOUBLE, PREFIX_DOUBLE);
		VARIABLE_PREFIXES.put(Types.NUMERIC, PREFIX_DOUBLE);
		VARIABLE_PREFIXES.put(Types.REAL, PREFIX_DOUBLE);
		VARIABLE_PREFIXES.put(Types.FLOAT, PREFIX_FLOAT);
		VARIABLE_PREFIXES.put(Types.INTEGER, PREFIX_INTEGER);
		VARIABLE_PREFIXES.put(Types.SMALLINT, PREFIX_SMALLINT);
		VARIABLE_PREFIXES.put(Types.TINYINT, PREFIX_SMALLINT);
		VARIABLE_PREFIXES.put(Types.ARRAY, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.DISTINCT, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.JAVA_OBJECT, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.NULL, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.OTHER, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.REF, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(Types.STRUCT, PREFIX_OBJECT_REFERENCE);
	}

	/******************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public BaseJDBC() { this(null, AUTHOR_DEFAULT); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
	*/
	public BaseJDBC(PrintWriter pWriter, String strAuthor)
	{
		super(pWriter, strAuthor);
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Accessor method - gets a column's object version of a name. Default
	    implementation calls <I>createObjectName</I>. Subclasses can override
	    behaviour.
		@param column Column record object.
	*/
	public String getColumnObjectName(final Column column)
	{
		return createObjectName(column.name);
	}

	/******************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/** Helper method - gets the SQL type as an <I>Integer</I> for use as a key in a
	    <I>Map</I>. Also converts DECIMAL and NUMERIC types without decimal digits to
	    the appropriate integer type. The return value is derived from the constants
	    in <I>Types</I>.
	    	@param column A table column object.
	*/
	public Integer getSQLType(final Column column)
	{
		int dataType = column.dataType;
		int decimalDigits = column.decimalDigits;
		int size = column.size;

		if (!(((Types.DECIMAL == dataType) ||
		       (Types.NUMERIC == dataType)) &&
		      (0 == decimalDigits)))
			return dataType;

		if (6 > size)
			return Types.SMALLINT;
		else if (11 > size)
			return Types.INTEGER;

		return Types.BIGINT;
	}

	/** Helper method - gets JDBC method suffix used by JDBC <I>ResultSet</I> "getters" and
	    <I>PreparedStatement</I> "setters".
		@param column A table column object.
	*/
	public String getJdbcMethodSuffix(final Column column)
	{
		return JDBC_METHOD_SUFFIXES.get(getSQLType(column));
	}

	/** Accessor method - gets a <I>String</I> representation of the Java
	    data type that represents the column.
		@param column A table column object.
	*/
	public String getJavaType(final Column column)
	{
		return JAVA_TYPES.get(getSQLType(column));
	}

	/** Accessor method - gets a <I>String</I> representation of the Dynamo DB
	    data type that represents the column.
		@param column A table column object.
	*/
	public String getDynamoDbType(final Column column)
	{
		return DYNAMO_DB_TYPES.get(getSQLType(column));
	}

	/** Accessor method - gets a <I>String</I> representation of the JDBC
	    data type that represents the column.
		@param column A table column object.
	*/
	public String getJDBCType(final Column column)
	{
		return JDBC_TYPES.get(getSQLType(column));
	}

	/** Accessor method - indicates whether the column requires a size attribute
	    in conjunction with the SQL data type.
		@param column A table column object.
	*/
	public boolean doesTypeRequireSize(final Column column)
	{
		return TYPES_REQUIRE_SIZE.get(column.dataType);
	}

	/** Accessor method - indicates whether the column requires a scale
	    (decimal digits) attribute in conjunction with the SQL data type.
		@param column A table column object.
	*/
	public boolean doesTypeRequireScale(final Column column)
	{
		return TYPES_REQUIRE_SCALE.get(column.dataType);
	}

	/** Accessor method - gets a <I>String</I> representation of the variable
	    prefix used by the data type of the column.
		@param column A table column object.
	*/
	public String getVariablePrefix(final Column column)
	{
		return VARIABLE_PREFIXES.get(getSQLType(column));
	}

	/** Accessor method - gets the boolean attribute of the column. */
	public boolean isBoolean(final Column column)
	{
		return BOOLEAN_TYPES.contains(getSQLType(column));
	}

	/** Accessor method - gets the character attribute of the column. */
	public boolean isCharacter(final Column column)
	{
		return CHARACTER_ATTRIBUTE.contains(getSQLType(column));
	}

	/** Accessor method - gets the String attribute of the column. */
	public boolean isString(final Column column)
	{
		return STRING_TYPES.contains(getSQLType(column));
	}

	/** Accessor method - gets a column's type definition used in DDL.
		@param column A table column object.
	*/
	public String getTypeDefinition(final Column column)
	{
		String strReturn = column.typeName;

		if (doesTypeRequireSize(column))
		{
			strReturn+= "(" + column.size;

			if (doesTypeRequireScale(column))
				strReturn+= ", " + column.decimalDigits;

			strReturn+= ")";
		}

		return strReturn;
	}

	/******************************************************************************
	*
	*	Helper methods: Used by class entry methods (i.e. main)
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/
}

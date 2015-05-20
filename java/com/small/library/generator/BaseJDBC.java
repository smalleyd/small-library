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

	/** Static member - map of SQL data types (java.sql.Types) to a Java data type. */
	private static Map<Integer, String> JAVA_TYPES = null;

	/** Static member - map of SQL data types (java.sql.Types) as a number to
	    a <I>String</String> representation.
	*/
	private static Map<Integer, String> JDBC_TYPES = null;

	/** Static member - map of SQL data types (java.sql.Types) to a boolean
	    indicator of whether the type requires a size attribute.
	*/
	private static Map<Integer, Boolean> TYPES_REQUIRE_SIZE = null;

	/** Static member - map of SQL data types (java.sql.Types) to a boolean
	    indicator of whether the type requires a scale (decimal digits) attribute.
	*/
	private static Map<Integer, Boolean> TYPES_REQUIRE_SCALE = null;

	/** Static member - map of SQL data types (java.sql.Types) to a Hungarian notation
	    variable prefix.
	*/
	private static Map<Integer, String> VARIABLE_PREFIXES = null;

	/** Static member - set of Boolean objects indicating boolean attribute of data type. */
	private static Set<Integer> BOOLEAN_TYPES = new HashSet<>(Arrays.asList(Types.BIT, Types.BOOLEAN));

	/** Static member - map of Boolean objects indicating character attribute of data type. */
	private static Map<Integer, Boolean> CHARACTER_ATTRIBUTE = null;

	/** Static member - set of SQL types that map to a Java String. */
	private static Set<Integer> STRING_TYPES = new HashSet<>(Arrays.asList(Types.CHAR, Types.CLOB, Types.LONGVARCHAR, Types.VARCHAR));

	/** Static constructor - initializes static member variables. */
	static
	{
		JDBC_METHOD_SUFFIXES = new HashMap<Integer, String>();

		JDBC_METHOD_SUFFIXES.put(java.sql.Types.BIGINT, JDBC_METHOD_SUFFIX_LONG);
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.BINARY, "Bytes");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.BLOB, "Blob");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.LONGVARBINARY, "Bytes");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.VARBINARY, "Bytes");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.BIT, "Boolean");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.BOOLEAN, "Boolean");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.CHAR, "String");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.VARCHAR, "String");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.CLOB, "Clob");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.LONGVARCHAR, "String");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.DATE, "Date");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.TIME, "Timestamp");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.TIMESTAMP, "Timestamp");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.DECIMAL, "Double");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.DOUBLE, "Double");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.NUMERIC, "Double");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.REAL, "Double");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.FLOAT, "Float");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.INTEGER, JDBC_METHOD_SUFFIX_INTEGER);
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.SMALLINT, JDBC_METHOD_SUFFIX_SMALLINT);
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.TINYINT, JDBC_METHOD_SUFFIX_SMALLINT);
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.ARRAY, "Object");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.DISTINCT, "Object");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.JAVA_OBJECT, "Object");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.NULL, "Object");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.OTHER, "Object");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.REF, "Object");
		JDBC_METHOD_SUFFIXES.put(java.sql.Types.STRUCT, "Object");

		JAVA_TYPES = new HashMap<Integer, String>();

		JAVA_TYPES.put(java.sql.Types.BIGINT, JAVA_TYPE_LONG);
		JAVA_TYPES.put(java.sql.Types.BINARY, "byte[]");
		JAVA_TYPES.put(java.sql.Types.BLOB, "byte[]");
		JAVA_TYPES.put(java.sql.Types.LONGVARBINARY, "byte[]");
		JAVA_TYPES.put(java.sql.Types.VARBINARY, "byte[]");
		JAVA_TYPES.put(java.sql.Types.BIT, "boolean");
		JAVA_TYPES.put(java.sql.Types.BOOLEAN, "boolean");
		JAVA_TYPES.put(java.sql.Types.CHAR, "String");
		JAVA_TYPES.put(java.sql.Types.VARCHAR, "String");
		JAVA_TYPES.put(java.sql.Types.CLOB, "String");
		JAVA_TYPES.put(java.sql.Types.LONGVARCHAR, "String");
		// JAVA_TYPES.put(java.sql.Types.DATE, "java.sql.Date");
		JAVA_TYPES.put(java.sql.Types.DATE, "Date");	// JPA implementation.
		JAVA_TYPES.put(java.sql.Types.TIME, "java.sql.Time");
		// JAVA_TYPES.put(java.sql.Types.TIMESTAMP, "java.sql.Timestamp");
		JAVA_TYPES.put(java.sql.Types.TIMESTAMP, "Date");	// JPA implementation.
		JAVA_TYPES.put(java.sql.Types.DECIMAL, "double");
		JAVA_TYPES.put(java.sql.Types.DOUBLE, "double");
		JAVA_TYPES.put(java.sql.Types.NUMERIC, "double");
		JAVA_TYPES.put(java.sql.Types.REAL, "double");
		JAVA_TYPES.put(java.sql.Types.FLOAT, "float");
		JAVA_TYPES.put(java.sql.Types.INTEGER, JAVA_TYPE_INTEGER);
		JAVA_TYPES.put(java.sql.Types.SMALLINT, JAVA_TYPE_SMALLINT);
		JAVA_TYPES.put(java.sql.Types.TINYINT, JAVA_TYPE_SMALLINT);
		JAVA_TYPES.put(java.sql.Types.ARRAY, "Object[]");
		JAVA_TYPES.put(java.sql.Types.DISTINCT, "java.util.Map");
		JAVA_TYPES.put(java.sql.Types.JAVA_OBJECT, "Object");
		JAVA_TYPES.put(java.sql.Types.NULL, "Object");
		JAVA_TYPES.put(java.sql.Types.OTHER, "Object");
		JAVA_TYPES.put(java.sql.Types.REF, "Object");
		JAVA_TYPES.put(java.sql.Types.STRUCT, "Object");

		JDBC_TYPES = new HashMap<Integer, String>();

		JDBC_TYPES.put(java.sql.Types.BIGINT, JDBC_TYPE_LONG);
		JDBC_TYPES.put(java.sql.Types.BINARY, "BINARY");
		JDBC_TYPES.put(java.sql.Types.BLOB, "BLOB");
		JDBC_TYPES.put(java.sql.Types.LONGVARBINARY, "LONGVARBINARY");
		JDBC_TYPES.put(java.sql.Types.VARBINARY, "VARBINARY");
		JDBC_TYPES.put(java.sql.Types.BIT, "BIT");
		JDBC_TYPES.put(java.sql.Types.BOOLEAN, "BOOLEAN");
		JDBC_TYPES.put(java.sql.Types.CHAR, "CHAR");
		JDBC_TYPES.put(java.sql.Types.VARCHAR, "VARCHAR");
		JDBC_TYPES.put(java.sql.Types.CLOB, "CLOB");
		JDBC_TYPES.put(java.sql.Types.LONGVARCHAR, "LONGVARCHAR");
		JDBC_TYPES.put(java.sql.Types.DATE, "DATE");
		JDBC_TYPES.put(java.sql.Types.TIME, "TIME");
		JDBC_TYPES.put(java.sql.Types.TIMESTAMP, "TIMESTAMP");
		JDBC_TYPES.put(java.sql.Types.DECIMAL, "DECIMAL");
		JDBC_TYPES.put(java.sql.Types.DOUBLE, "DOUBLE");
		JDBC_TYPES.put(java.sql.Types.NUMERIC, "NUMERIC");
		JDBC_TYPES.put(java.sql.Types.REAL, "REAL");
		JDBC_TYPES.put(java.sql.Types.FLOAT, "FLOAT");
		JDBC_TYPES.put(java.sql.Types.INTEGER, JDBC_TYPE_INTEGER);
		JDBC_TYPES.put(java.sql.Types.SMALLINT, JDBC_TYPE_SMALLINT);
		JDBC_TYPES.put(java.sql.Types.TINYINT, "TINYINT");
		JDBC_TYPES.put(java.sql.Types.ARRAY, "ARRAY");
		JDBC_TYPES.put(java.sql.Types.DISTINCT, "DISTINCT");
		JDBC_TYPES.put(java.sql.Types.JAVA_OBJECT, "JAVA_OBJECT");
		JDBC_TYPES.put(java.sql.Types.NULL, "NULL");
		JDBC_TYPES.put(java.sql.Types.OTHER, "OTHER");
		JDBC_TYPES.put(java.sql.Types.REF, "REF");
		JDBC_TYPES.put(java.sql.Types.STRUCT, "STRUCT");

		TYPES_REQUIRE_SIZE = new HashMap<Integer, Boolean>();

		TYPES_REQUIRE_SIZE.put(java.sql.Types.BIGINT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.BINARY, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.BLOB, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.LONGVARBINARY, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.VARBINARY, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.BIT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.BOOLEAN, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.CHAR, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.VARCHAR, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.CLOB, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.LONGVARCHAR, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.DATE, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.TIME, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.TIMESTAMP, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.DECIMAL, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.DOUBLE, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.NUMERIC, Boolean.TRUE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.REAL, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.FLOAT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.INTEGER, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.SMALLINT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.TINYINT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.ARRAY, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.DISTINCT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.JAVA_OBJECT, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.NULL, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.OTHER, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.REF, Boolean.FALSE);
		TYPES_REQUIRE_SIZE.put(java.sql.Types.STRUCT, Boolean.FALSE);

		TYPES_REQUIRE_SCALE = new HashMap<Integer, Boolean>();

		TYPES_REQUIRE_SCALE.put(java.sql.Types.BIGINT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.BINARY, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.BLOB, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.LONGVARBINARY, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.VARBINARY, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.BIT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.BOOLEAN, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.CHAR, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.VARCHAR, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.CLOB, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.LONGVARCHAR, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.DATE, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.TIME, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.TIMESTAMP, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.DECIMAL, Boolean.TRUE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.DOUBLE, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.NUMERIC, Boolean.TRUE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.REAL, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.FLOAT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.INTEGER, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.SMALLINT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.TINYINT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.ARRAY, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.DISTINCT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.JAVA_OBJECT, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.NULL, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.OTHER, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.REF, Boolean.FALSE);
		TYPES_REQUIRE_SCALE.put(java.sql.Types.STRUCT, Boolean.FALSE);

		VARIABLE_PREFIXES = new HashMap<Integer, String>();

		VARIABLE_PREFIXES.put(java.sql.Types.BIGINT, PREFIX_LONG);
		VARIABLE_PREFIXES.put(java.sql.Types.BINARY, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.BLOB, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.LONGVARBINARY, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.VARBINARY, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.BIT, PREFIX_BOOLEAN);
		VARIABLE_PREFIXES.put(java.sql.Types.BOOLEAN, PREFIX_BOOLEAN);
		VARIABLE_PREFIXES.put(java.sql.Types.CHAR, PREFIX_STRING);
		VARIABLE_PREFIXES.put(java.sql.Types.VARCHAR, PREFIX_STRING);
		VARIABLE_PREFIXES.put(java.sql.Types.CLOB, PREFIX_STRING);
		VARIABLE_PREFIXES.put(java.sql.Types.LONGVARCHAR, PREFIX_STRING);
		VARIABLE_PREFIXES.put(java.sql.Types.DATE, PREFIX_DATE);
		VARIABLE_PREFIXES.put(java.sql.Types.TIME, PREFIX_DATE);
		VARIABLE_PREFIXES.put(java.sql.Types.TIMESTAMP, PREFIX_DATE);
		VARIABLE_PREFIXES.put(java.sql.Types.DECIMAL, PREFIX_DOUBLE);
		VARIABLE_PREFIXES.put(java.sql.Types.DOUBLE, PREFIX_DOUBLE);
		VARIABLE_PREFIXES.put(java.sql.Types.NUMERIC, PREFIX_DOUBLE);
		VARIABLE_PREFIXES.put(java.sql.Types.REAL, PREFIX_DOUBLE);
		VARIABLE_PREFIXES.put(java.sql.Types.FLOAT, PREFIX_FLOAT);
		VARIABLE_PREFIXES.put(java.sql.Types.INTEGER, PREFIX_INTEGER);
		VARIABLE_PREFIXES.put(java.sql.Types.SMALLINT, PREFIX_SMALLINT);
		VARIABLE_PREFIXES.put(java.sql.Types.TINYINT, PREFIX_SMALLINT);
		VARIABLE_PREFIXES.put(java.sql.Types.ARRAY, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.DISTINCT, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.JAVA_OBJECT, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.NULL, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.OTHER, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.REF, PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put(java.sql.Types.STRUCT, PREFIX_OBJECT_REFERENCE);

		CHARACTER_ATTRIBUTE = new HashMap<Integer, Boolean>();

		CHARACTER_ATTRIBUTE.put(java.sql.Types.BIGINT, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.BINARY, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.BLOB, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.LONGVARBINARY, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.VARBINARY, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.BIT, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.BOOLEAN, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.CHAR, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.VARCHAR, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.CLOB, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.LONGVARCHAR, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.DATE, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.TIME, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.TIMESTAMP, Boolean.TRUE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.DECIMAL, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.DOUBLE, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.NUMERIC, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.REAL, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.FLOAT, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.INTEGER, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.SMALLINT, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.TINYINT, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.ARRAY, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.DISTINCT, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.JAVA_OBJECT, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.NULL, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.OTHER, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.REF, Boolean.FALSE);
		CHARACTER_ATTRIBUTE.put(java.sql.Types.STRUCT, Boolean.FALSE);
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
	public String getColumnObjectName(Columns.Record column)
	{
		return createObjectName(column.getName());
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
	    in <I>java.sql.Types</I>.
	    	@param column A table column object.
	*/
	public Integer getSQLType(Columns.Record column)
	{
		int dataType = column.getDataType();
		int decimalDigits = column.getDecimalDigits();
		int size = column.getSize();

		if (!(((java.sql.Types.DECIMAL == dataType) ||
		       (java.sql.Types.NUMERIC == dataType)) &&
		      (0 == decimalDigits)))
			return dataType;

		if (6 > size)
			return java.sql.Types.SMALLINT;
		else if (11 > size)
			return java.sql.Types.INTEGER;

		return java.sql.Types.BIGINT;
	}

	/** Helper method - gets JDBC method suffix used by JDBC <I>ResultSet</I> "getters" and
	    <I>PreparedStatement</I> "setters".
		@param column A table column object.
	*/
	public String getJdbcMethodSuffix(Columns.Record column)
	{
		return JDBC_METHOD_SUFFIXES.get(getSQLType(column));
	}

	/** Accessor method - gets a <I>String</I> representation of the Java
	    data type that represents the column.
		@param column A table column object.
	*/
	public String getJavaType(Columns.Record column)
	{
		return JAVA_TYPES.get(getSQLType(column));
	}

	/** Accessor method - gets a <I>String</I> representation of the JDBC
	    data type that represents the column.
		@param column A table column object.
	*/
	public String getJDBCType(Columns.Record column)
	{
		return JDBC_TYPES.get(getSQLType(column));
	}

	/** Accessor method - indicates whether the column requires a size attribute
	    in conjunction with the SQL data type.
		@param column A table column object.
	*/
	public boolean doesTypeRequireSize(Columns.Record column)
	{
		return TYPES_REQUIRE_SIZE.get(column.getDataType());
	}

	/** Accessor method - indicates whether the column requires a scale
	    (decimal digits) attribute in conjunction with the SQL data type.
		@param column A table column object.
	*/
	public boolean doesTypeRequireScale(Columns.Record column)
	{
		return TYPES_REQUIRE_SCALE.get(column.getDataType());
	}

	/** Accessor method - gets a <I>String</I> representation of the variable
	    prefix used by the data type of the column.
		@param column A table column object.
	*/
	public String getVariablePrefix(Columns.Record column)
	{
		return VARIABLE_PREFIXES.get(getSQLType(column));
	}

	/** Accessor method - gets the boolean attribute of the column. */
	public boolean isBoolean(Columns.Record column)
	{
		return BOOLEAN_TYPES.contains(getSQLType(column));
	}

	/** Accessor method - gets the character attribute of the column. */
	public boolean isCharacter(Columns.Record column)
	{
		int sqlType = getSQLType(column);
		try
		{
			return CHARACTER_ATTRIBUTE.get(sqlType);
		}
		catch (NullPointerException ex)
		{
			System.out.println("Invalid SQL Type (" + sqlType + ")");
			ex.printStackTrace();
			return false;
		}
	}

	/** Accessor method - gets the String attribute of the column. */
	public boolean isString(Columns.Record column)
	{
		return STRING_TYPES.contains(getSQLType(column));
	}

	/** Accessor method - gets a column's type definition used in DDL.
		@param column A table column object.
	*/
	public String getTypeDefinition(Columns.Record column)
	{
		String strReturn = column.getTypeName();

		if (doesTypeRequireSize(column))
		{
			strReturn+= "(" + column.getSize();

			if (doesTypeRequireScale(column))
				strReturn+= ", " + column.getDecimalDigits();

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

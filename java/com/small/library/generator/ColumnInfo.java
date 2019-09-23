package com.small.library.generator;

/**************************************************************************************
*
*	Value class that represents a table column's meta information with members
*	used by generators.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/31/2002
*
**************************************************************************************/

public class ColumnInfo
{
	public String columnName;	// Name of column
	public String name;	// Object version of the column name.
	public int size;	// Size of column
	public int decimalDigits;	// Number of decimal digits contained in the column definition.
	public boolean isNullable;
	public boolean isBoolean;
	public boolean isCharacter;
	public boolean isDecimal;
	public boolean isString;
	public boolean isTime;
	public int dataType;	// <I>java.sql.Types</I> value as integer.
	public boolean isPartOfPrimaryKey;
	public boolean isAutoIncrementing;
	public String dataTypeName;
	public String javaType;	// Represents the column Java type name.
	public String dynamoDbType;	// Represents the column DynamoDB type name.
	public boolean isPrimitive;
	public String jdbcTypeString;	// <I>java.sql.Types</I> value as String.
	public String variablePrefix;	// Variable prefix for the column type in Hungarian notation.
	public String jdbcMethodSuffix;	// Method suffix to "getters" of <I>ResultSet</I> and "setters" of <I>PreparedStatement</I>.
	public boolean doesTypeRequireSize;	// Does the type require a size attribute?
	public boolean doesTypeRequireScale;	// Does the type require a scale attribute?
	public String typeDefinition;	// Full column type definition
	public String memberVariableName;	// Name of the column's member variable representation.
	public String localVariableName;	// Name of the column's local variable representation.
	public String accessorMethodName;	// Name of the column's accessor method.
	public String mutatorMethodName;	// Name of the column's mutator method.
	public String withMethodName;	// Name of the column's "with" method.
	public boolean isImportedKey;	// Indicates whether the field is an imported foreign key.
	public String importedKeyName;	// Object version of the field name minus the standard imported key suffixes of "id", "i", or "c". The first letter is upper case.
	public String importedKeyMemberName;	// Class member version of the field name minus the standard imported key suffixes of "id", "i", or "c". The first letter is lower case.
	public String importedTableName;	// The table name that the imported foreign key represents. If the field is not an imported key, the value is <CODE>null</CODE>.
	public String importedObjectName;	// The object name of the table that represents the imported foreign key.
	public String resultSetGetter;	// The ResultSet retrieval call.

	/** Indicates that the field is numeric or date that can be searched by range. */
	public boolean isRange()
	{
		return !(isImportedKey || isPartOfPrimaryKey || isBoolean || isString);		
	}
}

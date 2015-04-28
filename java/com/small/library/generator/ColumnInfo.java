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
	/** Member variable - name of the column. */
	public String columnName;

	/** Member variable - object version of the column name. */
	public String name;

	/** Member variable - size of the column. */
	public int size;

	/** Member variable - number of decimal digits contained in the column definition. */
	public int decimalDigits;

	/** Member variable - is the column nullable. */
	public boolean isNullable;

	/** Member variable - is this a character field (i.e. CHAR, VARCHAR, BLOB, CLOB, ...) */
	public boolean isCharacter;

	/** Member variable - <I>java.sql.Types</I> value as integer. */
	public int dataType;

	/** Member variable - indicates whether the column is part of a primary key. */
	public boolean isPartOfPrimaryKey;

	/** Member variable - indicates whether the column is auto incrementing. */
	public boolean isAutoIncrementing;

	/** Member variable - String representation of the column data type name. */
	public String dataTypeName;

	/** Member variable - String representation of the column java type. */
	public String javaType;

	/** Member variable - indicates whether the java type is a primitive. */
	public boolean isPrimitive;

	/** Member variable - <I>java.sql.Types</I> value as String. */
	public String jdbcTypeString;

	/** Member variable - variable prefix for the column type in Hungarian
	    notation.
	*/
	public String variablePrefix;

	/** Member variable - method suffix to "getters" of <I>ResultSet</I> and
	    "setters" of <I>PreparedStatement</I>.
	*/
	public String jdbcMethodSuffix;

	/** Member variable - does the type require a size attribute. */
	public boolean doesTypeRequireSize;

	/** Member variable - does the type require a scale attribute. */
	public boolean doesTypeRequireScale;

	/** Member variable - full column type definition. */
	public String typeDefinition;

	/** Member variable - name of the column's member variable representation. */
	public String memberVariableName;

	/** Member variable - name of the column's local variable representation. */
	public String localVariableName;

	/** Member variable - name of the column's accessor method. */
	public String accessorMethodName;

	/** Member variable - name of the column's mutator method. */
	public String mutatorMethodName;

	/** Member variable - name of the column's "with" method. */
	public String withMethodName;

	/** Member variable - indicates whether the field is an imported foreign key. */
	public boolean isImportedKey;

	/** Member variable - object version of the field name minus the standard imported
	    key suffixes of "id", "i", or "c". The first letter is upper case.
	*/
	public String importedKeyName;

	/** Member variable - class member version of the field name minus the standard imported
	    key suffixes of "id", "i", or "c". The first letter is lower case.
	*/
	public String importedKeyMemberName;

	/** Member variable - the table name that the imported foreign key represents. If
	    the field is not an imported key, the value is <CODE>null</CODE>.
	*/
	public String importedTableName;

	/** Member variable - the object name of the table that represents the imported
	    foreign key.
	*/
	public String importedObjectName;
}

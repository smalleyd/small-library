package com.small.library.atg.gen;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generator class for Dynamo SQL Repository Item Descriptors. Item Descriptors
*	are XML files that instruct the repository how to map database tables and
*	fields to object properties.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 6/20/2002
*
***************************************************************************************/

public class SQLRepositoryItemDescriptor extends BaseTable
{
	/******************************************************************************
	*
	*	Constants - item descriptor data types
	*
	******************************************************************************/

	/** Constant - Item descriptor data type for "long". */
	public static final String DATA_TYPE_LONG = "long";

	/** Constant - Item descriptor data type for "int". */
	public static final String DATA_TYPE_INTEGER = "int";

	/** Constant - Item descriptor data type for "short". */
	public static final String DATA_TYPE_SMALLINT = "short";

	/******************************************************************************
	*
	*	Static members
	*
	******************************************************************************/

	/** Static member - map of SQL data types (java.sql.Types) to an ATG
	    Dynamo SQL Repository data-type attribute names.
	*/
	private static Map DATA_TYPES = null;

	/** Static constructor - initializes static member variables. */
	static
	{
		DATA_TYPES = new HashMap();

		DATA_TYPES.put(new Integer(java.sql.Types.BIGINT), DATA_TYPE_LONG);
		DATA_TYPES.put(new Integer(java.sql.Types.BINARY), "binary");
		DATA_TYPES.put(new Integer(java.sql.Types.BLOB), "binary");
		DATA_TYPES.put(new Integer(java.sql.Types.LONGVARBINARY), "binary");
		DATA_TYPES.put(new Integer(java.sql.Types.VARBINARY), "binary");
		DATA_TYPES.put(new Integer(java.sql.Types.BIT), "boolean");
		DATA_TYPES.put(new Integer(java.sql.Types.CHAR), "string");
		DATA_TYPES.put(new Integer(java.sql.Types.VARCHAR), "string");
		DATA_TYPES.put(new Integer(java.sql.Types.CLOB), "big string");
		DATA_TYPES.put(new Integer(java.sql.Types.LONGVARCHAR), "big string");
		DATA_TYPES.put(new Integer(java.sql.Types.DATE), "date");
		DATA_TYPES.put(new Integer(java.sql.Types.TIME), "timestamp");
		DATA_TYPES.put(new Integer(java.sql.Types.TIMESTAMP), "timestamp");
		DATA_TYPES.put(new Integer(java.sql.Types.DECIMAL), "double");
		DATA_TYPES.put(new Integer(java.sql.Types.DOUBLE), "double");
		DATA_TYPES.put(new Integer(java.sql.Types.NUMERIC), "double");
		DATA_TYPES.put(new Integer(java.sql.Types.REAL), "double");
		DATA_TYPES.put(new Integer(java.sql.Types.FLOAT), "float");
		DATA_TYPES.put(new Integer(java.sql.Types.INTEGER), DATA_TYPE_INTEGER);
		DATA_TYPES.put(new Integer(java.sql.Types.SMALLINT), DATA_TYPE_SMALLINT);
		DATA_TYPES.put(new Integer(java.sql.Types.TINYINT), DATA_TYPE_SMALLINT);
		DATA_TYPES.put(new Integer(java.sql.Types.ARRAY), "array");
		DATA_TYPES.put(new Integer(java.sql.Types.DISTINCT), "map");
		DATA_TYPES.put(new Integer(java.sql.Types.JAVA_OBJECT), null);
		DATA_TYPES.put(new Integer(java.sql.Types.NULL), null);
		DATA_TYPES.put(new Integer(java.sql.Types.OTHER), null);
		DATA_TYPES.put(new Integer(java.sql.Types.REF), null);
		DATA_TYPES.put(new Integer(java.sql.Types.STRUCT), null);
	}

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public SQLRepositoryItemDescriptor() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public SQLRepositoryItemDescriptor(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable)
	{
		super(pWriter, strAuthor, pTable);
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the SQL Repository Item Descriptor. */
	public void generate() throws GeneratorException, IOException
	{
		writeXMLHeader();
		openXMLBody();
		writeHeader();

		try { writeItemDescriptorBody(); }
		catch (SQLException pEx) { throw new GeneratorException(pEx); }

		closeXMLBody();
	}

	/******************************************************************************
	*
	*	Required methods: BaseTable
	*
	*****************************************************************************/

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Tables.Record pTable)
	{
		return pTable.getName() + "Repository.xml";
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/** Helper method - gets the an Item Descriptor property data-type base
	    on the column data type.
		@param pColumn A table column object.
	*/
	public String getPropertyDataType(Columns.Record pColumn)
	{
		return (String) DATA_TYPES.get(getSQLType(pColumn));
	}

	/******************************************************************************
	*
	*	Output methods
	*
	*****************************************************************************/

	/** Helper method - writes the descriptor XML header. */
	private void writeXMLHeader() throws IOException
	{
		writeLine("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
		writeLine();
		writeLine("<!DOCTYPE gsa-template");
		writeLine("  PUBLIC \"-//Art Technology Group, Inc.//DTD Dynamo Security//EN\"");
		writeLine("         \"http://www.atg.com/dtds/gsa/gsa_1.0.dtd\">");
	}

	/** Helper method - opens the XML body. */
	private void openXMLBody() throws IOException
	{
		writeLine();
		writeLine("<gsa-template>");
	}

	/** Helper method - writes the descriptor header. */
	private void writeHeader() throws IOException
	{
		String strObjectName = getObjectName();
		String strTableName = getTable().getName();

		writeLine();
		writeLine("\t<header>");
		write("\t\t<name>");
			write(strObjectName);
			writeLine("Repository</name>");
		write("\t\t<author>");
			write(getAuthor());
			writeLine("</author>");
		writeLine("\t\t<description>");
		write("\t\t\t");
			write(strTableName);
			writeLine(" Entity SQL to object mapping");
		writeLine("\t\t</description>");
		writeLine("\t</header>");
	}

	/** Helper method - writes the Item Descriptor body. */
	private void writeItemDescriptorBody() throws SQLException, IOException
	{
		String strObjectName = getObjectName();
		String strTableName = getTable().getSchema() + "." + getTable().getName();
		Columns pColumns = getColumns();
		String strPrimaryKeyColumns = getPrimaryKeyColumns();

		if (null == strPrimaryKeyColumns)
			strPrimaryKeyColumns = ((Columns.Record) pColumns.item(0)).getName();

		writeLine();
		write("\t<item-descriptor name=\"");
			write(strObjectName);
			writeLine("\" default=\"true\">");
		write("\t\t<table name=\"");
			write(strTableName);
			write("\" type=\"primary\" id-column-names=\"");
			write(strPrimaryKeyColumns);
			writeLine("\">");

		for (int i = 0; i < pColumns.size(); i++)
		{
			Columns.Record pColumn = (Columns.Record) pColumns.item(i);
			String strColumnName = pColumn.getName();
			String strPropertyName = getColumnObjectName(pColumn);
			String strDataType = getPropertyDataType(pColumn);
			String strRequired = pColumn.isNullable() ? "false" : "true";

			write("\t\t\t<property name=\"");
				write(strPropertyName);
				write("\" column-names=\"");
				write(strColumnName);
				write("\" data-types=\"");
				write(strDataType);
				write("\" required=\"");
				write(strRequired);
				writeLine("\" />");
		}

		writeLine("\t\t</table>");
		writeLine("\t</item-descriptor>");
	}

	/** Helper method - closes the XML body. */
	private void closeXMLBody() throws IOException
	{
		writeLine();
		writeLine("</gsa-template>");
	}

	/** Helper method - gets the primary key columns.
		@return a <I>String</I> list of the primary key columns separated by
			commas.
	*/
	private String getPrimaryKeyColumns()
		throws SQLException
	{
		PrimaryKeys pPrimaryKeys = getTable().getPrimaryKeys();

		pPrimaryKeys.load();

		if (0 == pPrimaryKeys.size())
			return null;

		// Should use StringBuffer, but this will most likely be only be one
		// field anyway. Rarely more than 2.
		String strReturn = ((PrimaryKeys.Record) pPrimaryKeys.item(0)).getName();

		for (int i = 1; i < pPrimaryKeys.size(); i++)
			strReturn+= "," + ((PrimaryKeys.Record) pPrimaryKeys.item(i)).getName();

		return strReturn;
	}

	/******************************************************************************
	*
	*	Class entry point
	*
	*****************************************************************************/

	/** Command line entry point.
		@param strArg1 Output directory.
		@param strArg2 URL to the data source.
		@param strArg3 data source login name.
		@param strArg4 data source password.
		@param strArg5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param strArg6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param strArg7 Schema name pattern
	*/
	public static void main(String strArgs[])
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > strArgs.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			File fileOutputDir = extractOutputDirectory(strArgs, 0);
			String strAuthor = extractAuthor(strArgs, 5);

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 6);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			SQLRepositoryItemDescriptor pGenerator =
				new SQLRepositoryItemDescriptor((PrintWriter) null, strAuthor, (Tables.Record) null);

			// Call the BaseTable method to handle the outputing.
			generateTableResources(pGenerator, pTables, fileOutputDir);
		}

		catch (IllegalArgumentException pEx)
		{
			String strMessage = pEx.getMessage();

			if (null != strMessage)
			{
				System.out.println(strMessage);
				System.out.println();
			}

			System.out.println("Usage: java " + SQLRepositoryItemDescriptor.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}

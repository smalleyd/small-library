package com.small.library.doc;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Reverse engineers tables in a database/schema to produce the export scripts.
*
*	@author David Small
*	@version 1.0.0.0
*	@since 9/27/2005
*
***************************************************************************************/

public class TablesDB2Export extends BaseTable
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public TablesDB2Export() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param pTable A table record object to base the output on.
	*/
	public TablesDB2Export(PrintWriter pWriter, Tables.Record pTable)
	{
		super(pWriter, null, pTable);
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the table's create SQL.  */
	public void generate() throws GeneratorException, IOException
	{
		Tables.Record table = getTable();
		String schemaName = table.getSchema();
		String tableName = table.getName();
		ColumnInfo[] columns = null;

		if (null != schemaName)
			tableName = schemaName + "." + tableName;

		// Build the list of columns string.
		String columnsList = "";
		try
		{
			columns = getColumnInfo();
		}
		catch (SQLException ex) { throw new GeneratorException(ex); }

		for (int i = 0; i < columns.length; i++)
		{
			ColumnInfo column = columns[i];

			if (0 < i)
				columnsList+= ", ";

			columnsList+= column.columnName;
		}

		write("EXPORT TO ");
		write(tableName);
		writeLine(".ixf OF IXF MESSAGES export.log");
		write("SELECT ");
		writeLine(columnsList);
		write("FROM ");
		writeLine(tableName);
		writeLine("WITH UR FOR READ ONLY;");
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
		return null;
	}

	/******************************************************************************
	*
	*	Class entry point
	*
	*****************************************************************************/

	/** Command line entry point.
		@param strArg1 Output file name.
		@param strArg2 URL to the data source.
		@param strArg3 data source login name.
		@param strArg4 data source password.
		@param strArg5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param strArg6 optional database schema name.
	*/
	public static void main(String strArgs[])
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > strArgs.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			File fileOutput = extractFile(strArgs, 0, "output");

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 5);
			pTables.load();

			// Get the output writer.
			PrintWriter writer = new PrintWriter(new FileWriter(fileOutput));

			// Create the DB2 load generator.
			TablesDB2Export pGenerator =
				new TablesDB2Export(writer,
				(Tables.Record) null);

			// Generate the SQL script file of export statements.
			for (int i = 0; i < pTables.size(); i++)
			{
				if (0 < i)
					writer.println();

				pGenerator.setTable((Tables.Record) pTables.item(i));
				pGenerator.generate();

				writer.flush();
			}

			// Close the writer.
			writer.close();
		}

		catch (IllegalArgumentException pEx)
		{
			String strMessage = pEx.getMessage();

			if (null != strMessage)
			{
				System.out.println(strMessage);
				System.out.println();
			}

			System.out.println("Usage: java " + TablesDB2Export.class.getName() + " Output file");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}

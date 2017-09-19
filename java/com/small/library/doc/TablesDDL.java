package com.small.library.doc;

import java.io.*;
import java.sql.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Reverse engineers tables in a database/schema to produce the DDL.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/24/2003
*
***************************************************************************************/

public class TablesDDL extends BaseTable
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
	public TablesDDL() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param pTable A table record object to base the output on.
	*/
	public TablesDDL(PrintWriter pWriter, Tables.Record pTable)
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
		PrimaryKeys primaryKeys = table.getPrimaryKeys();

		if (null != schemaName)
			tableName = schemaName + "." + tableName;

		try
		{
			columns = getColumnInfo();
			primaryKeys.load();
		}
		catch (SQLException ex) { throw new GeneratorException(ex); }

		write("CREATE TABLE ");
		write(tableName);
		writeLine(" (");

		for (int i = 0; i < columns.length; i++)
		{
			ColumnInfo column = columns[i];
			write("\t");
			write(column.columnName);
			write("\t\t");
			write(column.typeDefinition);

			if (!column.isNullable)
				write(" NOT NULL");

			writeLine(",");
		}

		if (0 < primaryKeys.size())
		{
			write("\t");

			if (null != primaryKeys.getName())
			{
				write("CONSTRAINT ");
				write(primaryKeys.getName());
				write(" ");
			}

			write("PRIMARY KEY (");

			for (int i = 0; i < primaryKeys.size(); i++)
			{
				if (0 < i)
					write(", ");
				write(((PrimaryKeys.Record) primaryKeys.item(i)).getName());
			}

			writeLine(")");
		}

		writeLine(");");
	}

	/** Action method - generates the table's foreign key alter SQL.  */
	public void generateForeignKeys()
		throws GeneratorException, IOException
	{
		Tables.Record table = getTable();
		ImportedKeys importedKeys = table.getImportedKeys();

		try { importedKeys.load(); }
		catch (SQLException ex) { throw new GeneratorException(ex); }

		int size = importedKeys.size();

		if (0 == size)
			return;

		String schemaName = table.getSchema();
		String tableName = table.getName();

		if (null != schemaName)
			tableName = schemaName + "." + tableName;

		for (int i = 0; i < size; i++)
		{
			ImportedKeys.Record importedKey = (ImportedKeys.Record) importedKeys.item(i);

			write("ALTER TABLE ");
			write(tableName);
			write(" ADD CONSTRAINT ");
			write(importedKey.getName());
			write(" FOREIGN KEY (");
			outputKeys(importedKey.getColumns_FK());
			write(") REFERENCES ");
			write(importedKey.getTable_PK());
			write(" (");
			outputKeys(importedKey.getColumns_PK());
			writeLine(");");
		}
	}

	/** Helper method - outputs a list of Keys. */
	protected void outputKeys(Keys keys) throws IOException
	{
		int size = keys.size();

		if (0 == size)
			return;

		for (int i = 0; i < size; i++)
		{
			Keys.Record key = (Keys.Record) keys.item(i);

			if (0 < i)
				write(", ");

			write(key.getName());
		}
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

			// Create the Deployment Descriptor generator.
			TablesDDL pGenerator =
				new TablesDDL(writer,
				(Tables.Record) null);

			// Generate the table create SQL.
			for (int i = 0; i < pTables.size(); i++)
			{
				if (0 < i)
					writer.println();

				pGenerator.setTable((Tables.Record) pTables.item(i));
				pGenerator.generate();

				writer.flush();
			}

			writer.println();

			// Generate the table foreign key alter SQL.
			for (int i = 0; i < pTables.size(); i++)
			{
				if (0 < i)
					writer.println();

				pGenerator.setTable((Tables.Record) pTables.item(i));
				pGenerator.generateForeignKeys();

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

			System.out.println("Usage: java " + TablesDDL.class.getName() + " Output file");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}

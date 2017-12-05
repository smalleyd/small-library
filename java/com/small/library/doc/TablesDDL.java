package com.small.library.doc;

import java.io.*;
import java.sql.*;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
	public TablesDDL(final PrintWriter writer)
	{
		this(writer, null);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param table A table record object to base the output on.
	*/
	public TablesDDL(final PrintWriter writer, final Table table)
	{
		super(writer, null, table);
	}

	@Override
	public void generate() throws GeneratorException, IOException
	{
		Table table = getTable();
		String schemaName = table.schema;
		String tableName = table.name;
		ColumnInfo[] columns = null;
		final List<PrimaryKey> primaryKeys;
		try { primaryKeys = table.getPrimaryKeys(); }
		catch (final SQLException ex) { throw new RuntimeException(ex); }

		if (null != schemaName)
			tableName = schemaName + "." + tableName;

		try
		{
			columns = getColumnInfo();
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

			final String pkName = primaryKeys.get(0).name;
			if (null != pkName)
			{
				write("CONSTRAINT ");
				write(pkName);
				write(" ");
			}

			write("PRIMARY KEY (");

			for (int i = 0; i < primaryKeys.size(); i++)
			{
				if (0 < i)
					write(", ");
				write((primaryKeys.get(i)).key);
			}

			writeLine(")");
		}

		writeLine(");");
	}

	/** Action method - generates the table's foreign key alter SQL.  */
	public void generateForeignKeys()
		throws GeneratorException, IOException
	{
		final Table table = getTable();
		final List<ForeignKey> importedKeys;
		try { importedKeys = table.getImportedKeys(); }
		catch (SQLException ex) { throw new RuntimeException(ex); }

		int size = importedKeys.size();

		if (0 == size)
			return;

		String schemaName = table.schema;
		String tableName = table.name;

		if (null != schemaName)
			tableName = schemaName + "." + tableName;

		for (final ForeignKey importedKey : importedKeys)
		{
			write("ALTER TABLE ");
			write(tableName);
			write(" ADD CONSTRAINT ");
			write(importedKey.name);
			write(" FOREIGN KEY (");
			write(keys(importedKey.fks));
			write(") REFERENCES ");
			write(importedKey.pkTable);
			write(" (");
			write(keys(importedKey.pks));
			writeLine(");");
		}
	}

	/** Helper method - outputs a list of Keys. */
	protected String keys(List<Key> keys) throws IOException
	{
		return StringUtils.join(keys, ", ");
	}

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Table table)
	{
		return null;
	}

	/** Command line entry point.
		@param strArg1 Output file name.
		@param strArg2 URL to the data source.
		@param strArg3 data source login name.
		@param strArg4 data source password.
		@param strArg5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param strArg6 optional database schema name.
	*/
	public static void main(final String... args)
	{
		try (final PrintWriter writer = new PrintWriter(new FileWriter(extractFile(args, 0, "output"))))
		{
			// Have enough arguments been supplied?
			if (3 > args.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			final List<Table> tables = extractTables(args, 1, 5);

			// Create the Deployment Descriptor generator.
			final TablesDDL generator = new TablesDDL(writer);

			// Generate the table create SQL.
			boolean first = true;
			for (final Table o : tables)
			{
				if (first)
					first = false;
				else
					writer.println();

				generator.setTable(o);
				generator.generate();

				writer.flush();
			}

			writer.println();

			// Generate the table foreign key alter SQL.
			first = true;
			for (final Table o : tables)
			{
				if (first)
					first = false;
				else
					writer.println();

				generator.setTable(o);
				generator.generateForeignKeys();

				writer.flush();
			}
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + TablesDDL.class.getName() + " Output file");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}

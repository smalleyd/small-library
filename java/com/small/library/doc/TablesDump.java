package com.small.library.doc;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Dumps the contents of database tables as a set of INSERT statements.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 5/25/2004
*
***************************************************************************************/

public class TablesDump extends BaseTable
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - simple date format - only for DB2. */
	public static final DateFormat db2TimestampFormat = new SimpleDateFormat(
		"yyyy-MM-dd-HH.mm.ss.S");

	/** Constant - simple date format - only for DB2. */
	public static final DateFormat db2DateFormat = new SimpleDateFormat(
		"yyyy-MM-dd");

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public TablesDump() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param pTable A table record object to base the output on.
		@param dataSource Data Source.
		@param insertSchema Optional schema name used on the insert scripts.
			If not provided, will use the schema of the selected table.
			If that is not provided, will not include a schema on the
			insert scripts.
	*/
	public TablesDump(PrintWriter pWriter, Table pTable, DataSource dataSource,
		String insertSchema)
	{
		super(pWriter, null, pTable);
		this.dataSource = dataSource;
		this.insertSchema = insertSchema;
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the table dump data. */
	public void generate() throws GeneratorException, IOException
	{
		// Get table and column information.
		Table table = getTable();
		String schemaName = table.schema;
		String selectTableName = table.name;
		String insertTableName = selectTableName;
		ColumnInfo[] columns = null;

		if (null != schemaName)
			selectTableName = schemaName + "." + selectTableName;

		if ((null != schemaName) || (null != insertSchema))
		{
			if (null != insertSchema)
				insertTableName = insertSchema + "." +
					insertTableName;
			else
				insertTableName = schemaName + "." +
					insertTableName;
		}

		try
		{
			columns = getColumnInfo();
		}
		catch (SQLException ex) { throw new GeneratorException(ex); }

		// Get beginning of INSERT statement and the SELECT statement.
		String insert = "INSERT INTO ";
		insert+= insertTableName;
		insert+= " (";
		String select = "SELECT ";

		for (int i = 0; i < columns.length; i++)
		{
			ColumnInfo column = columns[i];

			if (0 < i)
			{
				insert+= ", ";
				select+= ", ";
			}

			insert+= column.columnName;
			select+= column.columnName;
		}

		insert+= ") VALUES (";
		select+= " FROM " + selectTableName;

		// Call the SELECT statement and write-out each INSERT statement.
		try (final Connection connection = dataSource.getConnection();
		     final ResultSet rs = connection.createStatement().executeQuery(select))
		{
			while (rs.next())
			{
				write(insert);

				for (int i = 0; i < columns.length; i++)
				{
					ColumnInfo column = columns[i];

					if (0 < i)
						write(", ");

					Object value = rs.getObject(i + 1);

					if (null == value)
						write("NULL");
					else if (value instanceof java.sql.Timestamp)
					{
						write("'");
						write(db2TimestampFormat.format(
							(java.util.Date) value));
						write("'");
					}
					else if (value instanceof java.sql.Date)
					{
						write("'");
						write(db2DateFormat.format((java.util.Date) value));
						write("'");
					}

					else
					{
						if (column.isCharacter)
							write("'");

						write(StringUtils.replace(value.toString(), "'", "''"));

						if (column.isCharacter)
							write("'");
					}
				}

				writeLine(");");
			}
		}
		catch (SQLException ex) { throw new GeneratorException(ex); }
	}

	/******************************************************************************
	*
	*	Required methods: BaseTable
	*
	*****************************************************************************/

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Table pTable)
	{
		return null;
	}

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - data source. */
	private DataSource dataSource = null;

	/** Member variable - insert schema. */
	private String insertSchema = null;

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
		@param strArg6 optional database schema name of the tables to query.
		@param strArg7 optional database schema name of the tables to insert.
	*/
	public static void main(String strArgs[])
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > strArgs.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Get the optional insert schema name.
			String insertSchema = null;
			if (6 < strArgs.length)
				insertSchema = strArgs[6];

			// Local variables
			File fileOutput = extractFile(strArgs, 0, "output");

			// Create and load the tables object.
			List<Table> tables = extractTables(strArgs, 1, 5);

			// Get the output writer.
			PrintWriter writer = new PrintWriter(new FileWriter(fileOutput));

			// Create the Deployment Descriptor generator.
			TablesDump pGenerator =
				new TablesDump(writer,
				(Table) null, extractDataSource(strArgs, 1),
				insertSchema);

			// Buld the body of the deployment descriptor.
			boolean first = true;
			for (final Table o : tables)
			{
				if (first)
					first = false;
				else
					writer.println();

				pGenerator.setTable(o);
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

			System.out.println("Usage: java " + TablesDump.class.getName() + " Output file");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}

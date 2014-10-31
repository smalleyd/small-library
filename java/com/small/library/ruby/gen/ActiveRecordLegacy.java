package com.small.library.ruby.gen;

import java.io.*;

import com.small.library.ejb.gen.EntityBeanBase;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for a Ruby ActiveRecord. The ActiveRecord classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.1
*	@since 3/6/2011
*
***************************************************************************************/

public class ActiveRecordLegacy extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public ActiveRecordLegacy() { super(); }

	/** Constructor - constructs a populated object.
		@param out The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public ActiveRecordLegacy(PrintWriter out,
		String author, Tables.Record table)
	{
		super(out, author, table);
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the Entity Bean primary key class. */
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeImportedKeysAccessorMethods();

		writeFooter();
	}

	/******************************************************************************
	*
	*	Required methods: BaseTable
	*
	*****************************************************************************/

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Tables.Record table)
	{
		// Name should NOT have a suffix.
		return table.getName() + ".rb";
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Output methods
	*
	*****************************************************************************/

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		writeLine("###################################################################################");
		writeLine("#");
		writeLine("#  ActiveRecord class that represents the " + getTable().getName() + " table.");
		writeLine("#");
		writeLine("#  @author " + getAuthor());
		writeLine("#  @version 1.0");
		writeLine("#  @date " + getDateString());
		writeLine("#");
		writeLine("###################################################################################");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("class " + getClassName() + " < ActiveRecord::Base");
		write("  set_table_name '");
		write(getTable().getName());
		writeLine("'");
	}

	/** Output method - writes the imported foreign key accessor methods. */
	private void writeImportedKeysAccessorMethods() throws IOException
	{
		// Start the section.
		writeLine();
		writeLine("  # Imported foreign keys.");

		// Write accessors.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo columnInfo = m_ColumnInfo[i];

			if (!columnInfo.isImportedKey)
				continue;

			String name = columnInfo.importedTableName;

			writeLine();
			writeLine("  # Foreign key reference to " + name + ".");
			writeLine("  belongs_to :" + name);
		}
	}

	/** Output method - writes the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("end");
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Class Name of the resource. */
	public String getClassName() { return getClassName(getObjectName()); }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

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
		@param strArg7 package name of the entity bean CMP classes.
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
			String author = extractAuthor(strArgs, 5);

			// Create and load the tables object.
			Tables tables = extractTables(strArgs, 1, 6);
			tables.load();

			// Create the SQL Repository Item Descriptor generator.
			ActiveRecordLegacy pGenerator =
				new ActiveRecordLegacy((PrintWriter) null, author,
				(Tables.Record) null);

			// Call the BaseTable method to handle the outputing.
			generateTableResources(pGenerator, tables, fileOutputDir);
		}

		catch (IllegalArgumentException pEx)
		{
			String strMessage = pEx.getMessage();

			if (null != strMessage)
			{
				System.out.println(strMessage);
				System.out.println();
			}

			System.out.println("Usage: java " + ActiveRecordLegacy.class.getName() + " Output directory");
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

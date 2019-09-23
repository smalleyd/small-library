package com.small.library.ejb.gen;

import java.io.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean CMP 3.x classes. The CMP 3.x classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	The classes are annotated fully.
*
*	@author David Small
*	@version 1.1.0.0
*	@since 11/25/2005
*
***************************************************************************************/

public class EntityBeanES extends EntityBeanBase
{
	public static final String CLASS_NAME_SUFFIX = "ES";

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityBeanES(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityBeanES(PrintWriter writer,
		String author, Table table, String packageName)
	{
		super(writer, author, table, packageName);
	}

	public EntityBeanES(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityBeanES(PrintWriter writer,
		String author, Table table, String packageName,
		String version)
	{
		super(writer, author, table, packageName, version);
	}

	/** Action method - generates the Entity Bean primary key class. */
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeAccessorMethods();

		writeFooter();
	}

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Table table)
	{
		// Name should NOT have a suffix.
		return getClassName() + ".java";
	}

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		String packageName = getPackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tElasticsearch Mapping class that represents the " + getTable().name + " table.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version " + getVersion());
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("public class " + getClassName());
		writeLine("{");
	}

	/** Output method - writes the accessor methods. */
	private void writeAccessorMethods() throws IOException
	{
		// Any columns available?
		if (0 >= columnInfo.length) return;

		writeLine("public static final String TYPE = \"" + getTable().name + "\";", 1);
		writeLine("public static final String MAPPING = \"{ \\\"\" + TYPE + \"\\\": { \\\"properties\\\": { \" +", 1);

		// Write accessors.
		int i = 0;
		for (var item : columnInfo)
		{
			if (item.isString)
				write("\"\\\"" + item.memberVariableName + "\\\": { \\\"type\\\": \\\"text\\\", \\\"fields\\\": { \\\"raw\\\": { \\\"type\\\": \\\"keyword\\\" } } }", 2);
			else
			{
				var type = item.isDecimal ? "double" : item.isTime ? "date" : item.javaType.toLowerCase();
				write("\"\\\"" + item.memberVariableName + "\\\": { \\\"type\\\": \\\"" + type + "\\\" }", 2);
			}

			writeLine((++i < columnInfo.length) ? ", \" +" : "}}}\";");
		}
	}

	/** Output method - writes the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

	/** Accessor method - gets the Class Name of the resource. */
	public String getClassName() { return getClassName(getObjectName()); }

	/** Command line entry point.
		@param args1 Output directory.
		@param args2 URL to the data source.
		@param args3 data source login name.
		@param args4 data source password.
		@param args5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param args6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param args7 package name of the entity bean CMP classes.
		@param args8 application version number
		@param args9 table name filter
	*/
	public static void main(final String... args)
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > args.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			var dir = extractOutputDirectory(args, 0);
			var author = extractAuthor(args, 5);
			var packageName = extractArgument(args, 6, null);
			var version = extractArgument(args, 7, VERSION_DEFAULT);

			// Create and load the tables object.
			var tables = extractTables(args, 1, 8);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(new EntityBeanES(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			var message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + EntityBeanES.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Package Name]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}

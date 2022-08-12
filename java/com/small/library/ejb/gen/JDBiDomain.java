package com.small.library.ejb.gen;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for JDBi domain classes. The domain classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	The classes are annotated fully.
*
*	@author David Small
*	@version 3.0
*	@since 8/11/2022
*
***************************************************************************************/

public class JDBiDomain extends EntityBeanBase
{
	public static final String CLASS_NAME_SUFFIX = "";

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(final String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public JDBiDomain(final PrintWriter writer,
		final String author, final Table table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public JDBiDomain(final PrintWriter writer,
		final String author, final Table table, final String packageName)
	{
		super(writer, author, table, packageName);
	}

	public JDBiDomain(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public JDBiDomain(final PrintWriter writer,
		final String author, final Table table, final String packageName,
		final String version)
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
		writeConstructors();
		writeTransients();

		writeFooter();
	}

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(final Table table)
	{
		// Name should NOT have a suffix.
		return getClassName() + ".java";
	}

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		var packageName = getPackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		writeLine("import java.io.Serializable;");
		writeLine("import java.math.BigDecimal;");
		writeLine("import java.sql.Timestamp;");
		writeLine("import java.util.*;");
		writeLine();
		writeLine("import org.apache.commons.lang3.time.DateUtils;");
		writeLine("import org.jdbi.v3.core.mapper.reflect.ColumnName;");
		writeLine("import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;");
		writeLine();
		writeLine("import " + getDomainPackageName() + ".common.ObjectUtils;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tJDBi domain class that represents the " + getTable().name + " table.");
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
		writeLine("public class " + getClassName() + " implements Serializable");
		writeLine("{");
		writeLine("\tprivate static final long serialVersionUID = 1L;");
	}

	/** Output method - writes the accessor methods. */
	private void writeAccessorMethods() throws IOException
	{
		// Any columns available?
		if (0 >= columnInfo.length)
			return;

		writeLine();

		// Write accessors.
		for (var item : columnInfo)
			writeLine("\tpublic final " + item.javaType + " " + item.memberVariableName + ";");
	}

	/** Output method - writes the <CODE>constructors</CODE>. */
	private void writeConstructors() throws IOException
	{
		// Write constructor with all possible values.
		writeLine();
		writeLine("@JdbiConstructor", 1);

		// Constructor signature.
		write("public " + getClassName() + "(", 1);
		for (int i = 0, last = columnInfo.length - 1; i < columnInfo.length; i++)
		{
			ColumnInfo item = columnInfo[i];

			if (0 < i)
				write("\t\t");

			write("@ColumnName(\"" + item.columnName + "\") final " + item.javaType + " " + item.memberVariableName);

			if (last == i)
				writeLine(")");
			else
				writeLine(",");
		}

		// Write body.
		writeLine("{", 1);
		for (final ColumnInfo item : columnInfo)
			writeLine("this." + item.memberVariableName + " = " + item.memberVariableName + ";", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the transient helper methods. */
	private void writeTransients() throws IOException
	{
		var clazz = getClassName();

		// Write the equals method. */
		writeLine();
		writeLine("@Override", 1);
		writeLine("public boolean equals(final Object o)", 1);
		writeLine("{", 1);
		writeLine("if (!(o instanceof " + clazz + ")) return false;", 2);
		writeLine();
		writeLine("var v = (" + clazz + ") o;", 2);
		ColumnInfo item = columnInfo[0];
		writeLine("return " + writeEquals(item) + " &&", 2);
		int last = columnInfo.length - 1;
		for (int i = 1; i < columnInfo.length; i++)
		{
			var term = (last > i) ? " &&" : ";";
			writeLine(writeEquals(item = columnInfo[i]) + term, 3);
		}
		writeLine("}", 1);

		// Write the equals method. */
		var pks = Arrays.stream(columnInfo).filter(c -> c.isPartOfPrimaryKey).collect(toList());

		writeLine();
		writeLine("@Override", 1);
		writeLine("public int hashCode()", 1);
		writeLine("{", 1);
		if (1 == pks.size())
			writeLine("return Objects.hashCode(" + pks.get(0).memberVariableName + ");", 2);
		else if (1 < pks.size())
			writeLine("return Objects.hash(" + pks.stream().map(c -> c.memberVariableName).collect(joining(", ")) + ");", 2);
		else
			writeLine("return 0;", 2);
		writeLine("}", 1);

		// Write the toString method. */
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String toString()", 1);
		writeLine("{", 1);
		writeLine("return ObjectUtils.toString(this);", 2);
		writeLine("}", 1);
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
			final File dir = extractOutputDirectory(args, 0);
			final String author = extractAuthor(args, 5);
			final String packageName = extractArgument(args, 6, null);
			final String version = extractArgument(args, 7, VERSION_DEFAULT);

			// Create and load the tables object.
			final List<Table> tables = extractTables(args, 1, 8);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(new JDBiDomain(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + JDBiDomain.class.getName() + " Output directory");
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

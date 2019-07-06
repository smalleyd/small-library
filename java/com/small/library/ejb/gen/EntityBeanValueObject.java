package com.small.library.ejb.gen;

import java.io.*;
import java.util.List;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean value classes. The value classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@since 7/18/2002
*
***************************************************************************************/

public class EntityBeanValueObject extends EntityBeanBase
{
	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "Value";

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param strEntityName Name of the entity.
	*/
	public static String getClassName(String strEntityName)
	{
		return strEntityName + CLASS_NAME_SUFFIX;
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityBeanValueObject(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	public EntityBeanValueObject(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version Represents the application version number.
	*/
	public EntityBeanValueObject(PrintWriter writer,
		String author, Table table, String packageName, String version)
	{
		super(writer, author, table, packageName, version);
	}

	@Override
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeMembers();
		writeMutators();
		// writeImportedKeysAccessorMethods();
		writeConstructors();
		writeHelperMethods();
		writeObjectMethods();

		writeFooter();
	}

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Table table)
	{
		// Name should NOT have a suffix.
		return getClassName(createObjectName(table.name)) + ".java";
	}

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		final String packageName = getPackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		writeLine("import java.io.Serializable;");
		writeLine("import java.math.BigDecimal;");
		writeLine("import java.util.Date;");
		writeLine("import java.util.Objects;");
		writeLine();
		writeLine("import org.apache.commons.lang3.StringUtils;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tValue object class that represents the " + getTable().name + " table.");
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
		writeLine("private static final long serialVersionUID = 1L;", 1);
		writeLine();
		writeLine("public static final String TABLE = \"" + getTable().name + "\";", 1);
		for (final ColumnInfo i : columnInfo)
			if (i.isString)
				writeLine("public static final int MAX_LEN_" + i.columnName.toUpperCase() + " = " + i.size + ";", 1);
	}

	/** Output method - writes the accessor methods. */
	private void writeMembers() throws IOException
	{
		writeLine();
		writeLine("// Members", 1);

		for (final ColumnInfo i : columnInfo)
		{
			// write("\tpublic " + i.javaType + " " + i.accessorMethodName + "()");
			// 	writeLine(" { return " + i.memberVariableName + "; }");
			write("public " + i.javaType + " " + i.memberVariableName, 1);
			if (!i.isPrimitive)
				write(" = null");
			writeLine(";");
			// write("\tpublic void " + i.mutatorMethodName + "(final " + i.javaType + " newValue)");
			// writeLine(" { " + i.memberVariableName + " = newValue; }");

			if (i.isImportedKey)
				writeLine("public String " + i.importedKeyMemberName + "Name = null;", 1);
		}
	}

	/** Output method - writes the accessor methods. */
	private void writeMutators() throws IOException
	{
		writeLine();
		writeLine("// Mutators", 1);

		final String className = getClassName();
		for (ColumnInfo i : columnInfo)
		{
			write("public " + className + " " + i.withMethodName + "(final " + i.javaType + " newValue)", 1);
			writeLine(" { " + i.memberVariableName + " = newValue; return this; }");

			if (i.isImportedKey)
				writeLine("public " + className + " with" + i.importedKeyName + "Name(final String newValue) { " + i.importedKeyMemberName + "Name = newValue; return this; }", 1);
		}
	}

	/** Output method - writes the imported foreign key accessor methods. */
	@SuppressWarnings("unused")
	private void writeImportedKeysAccessorMethods() throws IOException
	{
		// Write accessors.
		for (final ColumnInfo columnInfo : columnInfo)
		{
			if (!columnInfo.isImportedKey)
				continue;

			String name = columnInfo.importedKeyName;
			String memberName = columnInfo.importedKeyMemberName;

			writeLine();
			write("public String get" + name + "Name()", 1);
			writeLine(" { return " + memberName + "Name; }");
			writeLine("public String " + memberName + "Name = null;", 1);
			write("public void set" + name + "Name(final String newValue)", 1);
			writeLine(" { " + memberName + "Name = newValue; }");
			writeLine("public " + getClassName() + " with" + name + "Name(final String newValue) { " + memberName + "Name = newValue; return this; }", 1);
		}
	}

	/** Output method - writes the <CODE>constructors</CODE>. */
	private void writeConstructors() throws IOException
	{
		// Write the default/empty constructor. */
		writeLine();
		writeLine("\tpublic " + getClassName() + "() {}");

		// Write constructor with all possible values.
		writeLine();
		write("\tpublic " + getClassName() + "(");

		int i = 0;
		final int last = columnInfo.length;
		for (ColumnInfo item : columnInfo)
		{
			if (1 < ++i)
				write("\t\t");

			write("final " + item.javaType + " " + item.memberVariableName);

			if (last == i)
				writeLine(")");
			else
				writeLine(",");
		}

		// Write body.
		writeLine("\t{");
		for (final ColumnInfo item : columnInfo)
			writeLine("\t\tthis." + item.memberVariableName + " = " +
				item.memberVariableName + ";");
		writeLine("\t}");			
	}

	/** Output method - writes the internal helper methods. */
	private void writeHelperMethods() throws IOException
	{
		// Output the clean method for String cleansing.
		writeLine();
		writeLine("/** Helper method - trims all string fields and converts empty strings to NULL. */", 1);
		writeLine("public void clean()", 1);
		writeLine("{", 1);
		for (ColumnInfo i : columnInfo)
		{
			if (!i.isString)
				continue;

			writeLine(i.memberVariableName + " = StringUtils.trimToNull(" + i.memberVariableName + ");", 2);
		}
		writeLine("}", 1);
	}

	/** Output method - writes Object class override methods. */
	private void writeObjectMethods() throws IOException
	{
		final String clazz = getClassName();

		// Write the equals method. */
		writeLine();
		writeLine("@Override", 1);
		writeLine("public boolean equals(final Object o)", 1);
		writeLine("{", 1);
		writeLine("if (!(o instanceof " + clazz + ")) return false;", 2);
		writeLine();
		writeLine("final " + clazz + " v = (" + clazz + ") o;", 2);
		ColumnInfo item = columnInfo[0];
		writeLine("return " + writeEquals(item) + " &&", 2);
		final int last = columnInfo.length - 1;
		for (int i = 1; i < columnInfo.length; i++)
		{
			final String term = (last > i) ? " &&" : ";";
			writeLine(writeEquals(item = columnInfo[i]) + term, 3);
			if (null != item.importedKeyMemberName)
				writeLine("Objects.equals(" + item.importedKeyMemberName + "Name, v." + item.importedKeyMemberName + "Name)" + term, 3);
		}
		writeLine("}", 1);
		
		// Write the toString method. */
		item = columnInfo[0];
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String toString()", 1);
		writeLine("{", 1);
		writeLine("return new StringBuilder(\"{ " + item.memberVariableName + ": \").append(" + item.memberVariableName + ")", 2);

		for (int i = 1; i < columnInfo.length; i++)
		{
			writeLine(".append(\", " + (item = columnInfo[i]).memberVariableName + ": \").append(" + item.memberVariableName + ")", 3);
			if (null != item.importedKeyMemberName)
				writeLine(".append(\", " + item.importedKeyMemberName + "Name: \").append(" + item.importedKeyMemberName + "Name)", 3);
		}
		writeLine(".append(\" }\").toString();", 3);
		writeLine("}", 1);
	}

	private String writeEquals(final ColumnInfo item)
	{
		if (item.isPrimitive)
			return "(" + item.memberVariableName + " == v." + item.memberVariableName + ")";

		return "Objects.equals(" + item.memberVariableName + ", v." + item.memberVariableName + ")";
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
		@param args7 package name of the entity bean value object.
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
			generateTableResources(new EntityBeanValueObject(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + EntityBeanValueObject.class.getName() + " Output directory");
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

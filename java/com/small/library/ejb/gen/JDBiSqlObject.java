package com.small.library.ejb.gen;

import static java.util.stream.Collectors.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates a JDBi SQL Object interface based on the underlying table.
*
*	The classes are annotated fully.
*
*	@author David Small
*	@version 2.0
*	@since 10/6/2018
*
***************************************************************************************/

public class JDBiSqlObject extends EntityBeanBase
{
	public String version = null;

	public static final String CLASS_NAME_SUFFIX = "JDBI";
	public static final String VERSION_DEFAULT = "1.0";
	public static final String PARAM = "@Bind(\"%s\") final %s %s";

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
	public JDBiSqlObject(PrintWriter writer,
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
	public JDBiSqlObject(PrintWriter writer,
		String author, Table table, String packageName)
	{
		super(writer, author, table, packageName);
	}

	public JDBiSqlObject(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public JDBiSqlObject(PrintWriter writer,
		String author, Table table, String packageName,
		String version)
	{
		super(writer, author, table, packageName);

		this.version = version;
	}

	@Override
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeMethods();

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
		final String packageName = getPackageName();
		final String basePackageName = getBasePackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		writeLine("import org.jdbi.v3.sqlobject.config.RegisterRowMapper;");
		writeLine("import org.jdbi.v3.sqlobject.customizer.Bind;");
		writeLine("import org.jdbi.v3.sqlobject.customizer.BindFields;");
		writeLine("import org.jdbi.v3.sqlobject.statement.*;");
		writeLine();
		writeLine("import " + basePackageName + ".mapper." + JDBiMapper.getClassName(getObjectName()) + ";");
		writeLine("import " + basePackageName + ".value." + EntityBeanValueObject.getClassName(getObjectName()) + ";");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tJDBi SQL Object interface that represents the " + getTable().name + " table.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version " + version);
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("@RegisterRowMapper(" + getObjectName() + "Mapper.class)");
		writeLine("public interface " + getClassName());
		writeLine("{");
	}

	/** Output method - writes the member variables. */
	private void writeMethods() throws IOException
	{
		final String table = getTable().name;
		final String valueName = EntityBeanValueObject.getClassName(getObjectName());
		final String params = Arrays.stream(columnInfo)
			.filter(v -> v.isPartOfPrimaryKey)
			.map(v -> String.format(PARAM, v.memberVariableName, v.javaType, v.memberVariableName))
			.collect(joining(", "));
		final String fields = Arrays.stream(columnInfo).map(v -> "o." + v.columnName).collect(joining(", "));
		final String where = "WHERE " + Arrays.stream(columnInfo).filter(v -> v.isPartOfPrimaryKey).map(v -> v.columnName + " = :" + v.memberVariableName).collect(joining(" AND "));
		final String selectWhere = "WHERE " + Arrays.stream(columnInfo).filter(v -> v.isPartOfPrimaryKey).map(v -> "o." + v.columnName + " = :" + v.memberVariableName).collect(joining(" AND "));
		final String insert = "INSERT INTO " + table + " (" +
			Arrays.stream(columnInfo).map(v -> v.columnName).collect(joining(", ")) +
			") VALUES (" +
			Arrays.stream(columnInfo).map(v -> ":" + v.memberVariableName).collect(joining(", ")) +
			")";
		final String update = "UPDATE " + table + " SET " +
			Arrays.stream(columnInfo).filter(v -> !v.isPartOfPrimaryKey).map(v -> v.columnName + " = :" + v.memberVariableName).collect(joining(", ")) +
			" " + where;
		final String delete = "DELETE " + table + " " + where;

		writeLine("@SqlQuery(\"SELECT " + fields + " FROM " + table + " o " + selectWhere + "\")", 1);
		writeLine("public " + valueName + " get(" + params + ");", 1);
		writeLine();
		writeLine("@SqlUpdate(\"" + insert + "\")", 1);
		writeLine("public int insert(@BindFields final " + valueName + " value);", 1);
		writeLine();
		writeLine("@SqlUpdate(\"" + update + "\")", 1);
		writeLine("public int update(@BindFields final " + valueName + " value);", 1);
		writeLine();
		writeLine("@SqlUpdate(\"" + delete + "\")", 1);
		writeLine("public int remove(" + params + ");", 1);
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
			generateTableResources(new JDBiSqlObject(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + JDBiSqlObject.class.getName() + " Output directory");
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
